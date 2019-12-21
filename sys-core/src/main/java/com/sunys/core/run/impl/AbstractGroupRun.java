package com.sunys.core.run.impl;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunys.facade.run.GroupRun;
import com.sunys.facade.run.Run;
import com.sunys.facade.run.RunStatus;
import com.sunys.facade.run.RunType;

/**
 * AbstractGroupRun
 * @author sunys
 * @date Dec 21, 2019
 */
public abstract class AbstractGroupRun<T extends Run> extends AbstractRun implements GroupRun<T> {

	private static final Logger logger = LoggerFactory.getLogger(AbstractGroupRun.class);

	protected List<Future<RunStatus>> futures;

	protected RunType runType = RunType.serial;

	@Override
	public void init() throws Exception {
		List<T> runs = getRuns();
		for (Run run : runs) {
			run.init();
			if (!RunStatus.Idle.equals(run.getStatus())) {
				status = run.getStatus();
				break;
			}
		}
		if (status == null) {
			status = RunStatus.Idle;
		}
	}

	/**
	 * 获取线程池接口
	 * @return
	 */
	protected abstract ExecutorService getExecutorService();

	@Override
	public void run() throws Exception {
		setStatus(RunStatus.Running);
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
		if (RunStatus.Running.equals(status)) {
			setStatus(RunStatus.Success);
		}
	}

	protected void serialRun() throws Exception {
		List<T> runs = getRuns();
		for (Run run : runs) {
			if (RunStatus.Running.equals(status)) {
				run.run();
			}
			RunStatus status = run.getStatus();
			if (RunStatus.Fail.equals(status)) {
				setStatus(status);
				break;
			}
		}
	}

	protected void parallelRun() {
		List<T> runs = getRuns();
		ExecutorService pool = getExecutorService();
		try {
			futures = pool.invokeAll(runs);
			for (Future<RunStatus> future : futures) {
				try {
					RunStatus status = future.get();
					if (RunStatus.Fail.equals(status)) {
						setStatus(RunStatus.Fail);
					}
				} catch (ExecutionException e) {
					logger.error(e.getMessage(), e);
					setStatus(RunStatus.Fail);
				}
			}
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		}
	}

	protected void eventRun() {
		Lock lock = timeoutCheck.getLock();
		lock.lock();
		try {
			while (RunStatus.Running.equals(status)) {
				logger.info("eventRun wait...");
				timeoutCheck.await();
				logger.info("eventRun notify, status:{}", status);
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
		if (!RunType.event.equals(getRunType()) || !RunStatus.Running.equals(this.status)) {
			return;
		}
		if (status == null) {
			List<T> runs = getRuns();
			boolean fail = runs.stream().anyMatch(run -> RunStatus.Fail.equals(run.getStatus()));
			if (fail) {
				setStatus(RunStatus.Fail);
				timeoutCheck.signalAll();
				return;
			}
			boolean success = runs.stream().allMatch(run -> RunStatus.Success.equals(run.getStatus()));
			if (success) {
				setStatus(RunStatus.Success);
				timeoutCheck.signalAll();
				return;
			}
		} else {
			setStatus(status);
			timeoutCheck.signalAll();
		}
	}

	@Override
	public void eventRun(int eventIndex) {
		if (!RunType.event.equals(getRunType())) {
			return;
		}
		Lock lock = timeoutCheck.getLock();
		List<T> runs = getRuns();
		ExecutorService pool = getExecutorService();
		lock.lock();
		try {
			if (RunStatus.Running.equals(status)) {
				Run run = runs.get(eventIndex);
				if (!RunStatus.Running.equals(run.getStatus())) {
					logger.info("eventRun run, index:{}", eventIndex);
					pool.execute(() -> {
						try {
							run.run();
						} catch (Exception e) {
							logger.error(e.getMessage(), e);
						}
					});
				}
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
		getRuns().forEach(Run::reset);
		setStatus(RunStatus.Idle);
		timeoutCheck.reset();
	}

	@Override
	public void destroy() {
		getRuns().forEach(Run::destroy);
		setStatus(RunStatus.Destory);
	}

	@Override
	public double getProgress() {
		//完成的进度
		double finish = getRuns().stream().filter(run -> RunStatus.Success.equals(getStatus()) || RunStatus.Fail.equals(getStatus())).map(Run::calculateRunDuration).reduce(0L, (v1, v2) -> v1 + v2);
		//所有的进度
		double total = calculateRunDuration();
		if (total == 0) {
			logger.error("total duration is 0, total:{}, finish:{}", total, finish);
			return 0;
		}
		double result = finish * 100 / total;
		return result;
	}

	@Override
	public long calculateRunDuration() {
		if (runDuration == null) {
			//获取所有子节点，估算当前节点的运行时间
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
