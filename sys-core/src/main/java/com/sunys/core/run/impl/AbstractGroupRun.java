package com.sunys.core.run.impl;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Condition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunys.facade.run.GroupRun;
import com.sunys.facade.run.Run;
import com.sunys.facade.run.RunStatus;
import com.sunys.facade.run.RunType;

public abstract class AbstractGroupRun<T extends Run> extends AbstractRun implements GroupRun<T> {

	private static final Logger logger = LoggerFactory.getLogger(AbstractGroupRun.class);

	private Condition eventCondition = lock.newCondition();

	private int eventIndex;

	private RunType runType = RunType.serial;

	@Override
	public void init() throws Exception {
		List<T> runs = getRuns();
		for (Run run : runs) {
			run.init();
			if (!RunStatus.idle.equals(run.getStatus())) {
				status = run.getStatus();
				break;
			}
		}
		if (status == null) {
			status = RunStatus.idle;
		}
	}

	public abstract ExecutorService getExecutorService();

	@Override
	public void run() throws Exception {
		setStatus(RunStatus.running);
		switch (getRunType()) {
		case serial:
			serialRun();
			break;
		case parallel:
			parallelRun();
			break;
		case event:
			eventRun();
			break;
		}
		if (RunStatus.running.equals(status)) {
			setStatus(RunStatus.success);
		}
	}

	protected void serialRun() throws Exception {
		List<T> runs = getRuns();
		for (Run run : runs) {
			if (RunStatus.running.equals(status)) {
				run.run();
			}
			RunStatus status = run.getStatus();
			if (RunStatus.fail.equals(status)) {
				setStatus(status);
				break;
			}
		}
	}

	protected void parallelRun() {
		List<T> runs = getRuns();
		ExecutorService pool = getExecutorService();
		try {
			List<Future<RunStatus>> futures = pool.invokeAll(runs);
			for (Future<RunStatus> future : futures) {
				try {
					RunStatus status = future.get();
					if (RunStatus.fail.equals(status)) {
						setStatus(RunStatus.fail);
					}
				} catch (ExecutionException e) {
					logger.error(e.getMessage(), e);
					setStatus(RunStatus.fail);
				}
			}
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		}
	}

	protected void eventRun() {
		List<T> runs = getRuns();
		ExecutorService pool = getExecutorService();
		lock.lock();
		try {
			while (RunStatus.running.equals(status)) {
				logger.info("eventRun wait...");
				eventCondition.await();
				logger.info("eventRun notify, status {}", status);
				if (RunStatus.running.equals(status)) {
					Run run = runs.get(eventIndex);
					if (!RunStatus.running.equals(run.getStatus())) {
						logger.info("eventRun run index {}", eventIndex);
						pool.submit(run);
					}
				}
			}
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void setEventRunStatus() {
		setEventRunStatus(null);
	}

	@Override
	public void setEventRunStatus(RunStatus status) {
		if (!RunType.event.equals(getRunType())) {
			return;
		}
		lock.lock();
		try {
			if (status == null) {
				List<T> runs = getRuns();
				boolean fail = runs.stream().anyMatch(run -> RunStatus.fail.equals(run.getStatus()));
				if (fail) {
					setStatus(RunStatus.fail);
					eventCondition.signal();
					return;
				}
				boolean success = runs.stream().allMatch(run -> RunStatus.success.equals(run.getStatus()));
				if (success) {
					setStatus(RunStatus.success);
					eventCondition.signal();
					return;
				}
			} else {
				setStatus(status);
				eventCondition.signal();
			}
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void eventRun(int eventIndex) {
		if (!RunType.event.equals(getRunType())) {
			return;
		}
		lock.lock();
		try {
			if (RunStatus.running.equals(status)) {
				this.eventIndex = eventIndex;
				logger.info("eventRun signal..., index {}", eventIndex);
				eventCondition.signal();
			}
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void clean() {
		getRuns().forEach(Run::clean);
	}

	@Override
	public void reset() {
		clean();
		getRuns().forEach(Run::reset);
		lock.lock();
		try {
			setStatus(RunStatus.idle);
			cancelCheckTimeout();
			isTimeout = false;
			logger.info("reset signal...");
			eventCondition.signal();
			condition.signal();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void destory() {
		getRuns().forEach(Run::destory);
		setStatus(RunStatus.destory);
	}

	@Override
	public long calculateRunDuration() {
		if (runDuration == null) {
			runDuration = getRuns().stream().map(Run::calculateRunDuration).reduce(0L, (v1, v2) -> v1 + v2);
		}
		return runDuration;
	}

	@Override
	public RunType getRunType() {
		return runType;
	}

	public void setRunType(RunType runType) {
		this.runType = runType;
	}

}
