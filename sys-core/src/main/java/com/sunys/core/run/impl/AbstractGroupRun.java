package com.sunys.core.run.impl;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.sunys.facade.run.GroupRun;
import com.sunys.facade.run.Run;
import com.sunys.facade.run.RunStatus;
import com.sunys.facade.run.RunType;

public abstract class AbstractGroupRun<T extends Run> extends AbstractRun implements GroupRun<T> {

	@Override
	public void init() {
		List<T> runs = getRuns();
		for (Run run : runs) {
			run.init();
			if (!RunStatus.idle.equals(run.getStatus())) {
				status = run.getStatus();
			}
		}
		if (status == null) {
			status = RunStatus.idle;
		}
	}

	@Override
	public void run() {
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

	protected void serialRun() {
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
		ExecutorService pool = Executors.newFixedThreadPool(runs.size());
		try {
			List<Future<RunStatus>> futures = pool.invokeAll(runs);
			for (Future<RunStatus> future : futures) {
				try {
					RunStatus status = future.get();
					if (RunStatus.fail.equals(status)) {
						setStatus(RunStatus.fail);
					}
				} catch (ExecutionException e) {
					setStatus(RunStatus.fail);
					e.printStackTrace();
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		shutdown(pool);
	}

	private void shutdown(ExecutorService pool) {
		pool.shutdown();
		try {
			if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
				pool.shutdownNow();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			pool.shutdownNow();
			Thread.currentThread().interrupt();
		}
	}

	protected void eventRun() {
		
	}

	public void eventRun(int index) {
		
	}

	@Override
	public void clean() {
		getRuns().forEach(Run::clean);
	}

	@Override
	public void reset() {
		clean();
		getRuns().forEach(Run::reset);
		setStatus(RunStatus.idle);
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
		return RunType.serial;
	}

}
