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

/**
 * AbstractGroupRun
 * @author sunys
 * @date Dec 21, 2019
 */
public abstract class AbstractGroupRun<T extends Run> extends AbstractRun implements GroupRun<T> {

	private static final Logger logger = LoggerFactory.getLogger(AbstractGroupRun.class);

	protected byte runType = RUNTYPE_SERIAL;

	@Override
	public void init() throws Exception {
		List<T> runs = getRuns();
		for (Run run : runs) {
			run.init();
			if (STATUS_IDLE != run.getStatus()) {
				status = run.getStatus();
				break;
			}
		}
		if (status == STATUS_NONE) {
			status = STATUS_IDLE;
		}
	}

	/**
	 * 获取线程池接口
	 * @return
	 */
	protected abstract ExecutorService getExecutorService();

	@Override
	public void run() throws Exception {
		setStatus(STATUS_RUNNING);
		switch (getRunType()) {
		case RUNTYPE_SERIAL:
			serialRun();
			break;
		case RUNTYPE_PARALLEL:
			parallelRun();
			break;
		case RUNTYPE_EVENT:
			eventRun();
			break;
		}
		if (STATUS_RUNNING == status) {
			setStatus(STATUS_SUCCESS);
		}
	}

	protected void serialRun() throws Exception {
		List<T> runs = getRuns();
		for (Run run : runs) {
			if (STATUS_RUNNING == status) {
				run.run();
			}
			byte status = run.getStatus();
			if (STATUS_FAIL == status) {
				setStatus(status);
				break;
			}
		}
	}

	protected void parallelRun() {
		List<T> runs = getRuns();
		ExecutorService pool = getExecutorService();
		try {
			List<Future<Byte>> futures = pool.invokeAll(runs);
			for (Future<Byte> future : futures) {
				try {
					Byte status = future.get();
					if (STATUS_FAIL == status) {
						setStatus(STATUS_FAIL);
					}
				} catch (ExecutionException e) {
					logger.error(e.getMessage(), e);
					setStatus(STATUS_FAIL);
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
			while (STATUS_RUNNING == status) {
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
	public void setEventRunStatus(Byte status) {
		if (RUNTYPE_EVENT != getRunType() || STATUS_RUNNING != this.status) {
			return;
		}
		if (status == null) {
			List<T> runs = getRuns();
			boolean fail = runs.stream().anyMatch(run -> STATUS_FAIL == run.getStatus());
			if (fail) {
				setStatus(STATUS_FAIL);
				timeoutCheck.signalAll();
				return;
			}
			boolean success = runs.stream().allMatch(run -> STATUS_SUCCESS == run.getStatus());
			if (success) {
				setStatus(STATUS_SUCCESS);
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
		if (RUNTYPE_EVENT != getRunType()) {
			return;
		}
		Lock lock = timeoutCheck.getLock();
		List<T> runs = getRuns();
		ExecutorService pool = getExecutorService();
		lock.lock();
		try {
			if (STATUS_RUNNING == status) {
				Run run = runs.get(eventIndex);
				if (STATUS_RUNNING != run.getStatus()) {
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
		setStatus(STATUS_IDLE);
		timeoutCheck.reset();
	}

	@Override
	public void destroy() {
		getRuns().forEach(Run::destroy);
		setStatus(STATUS_DESTROY);
	}

	@Override
	public byte getRunType() {
		return runType;
	}

	public void setRunType(byte runType) {
		this.runType = runType;
	}

}
