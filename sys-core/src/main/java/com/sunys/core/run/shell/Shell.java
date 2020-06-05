package com.sunys.core.run.shell;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunys.core.run.impl.ShellState;
import com.sunys.facade.run.ContextState;
import com.sunys.facade.run.State;
import com.sunys.facade.run.StateEvent;

public class Shell {

	private static final Logger log = LoggerFactory.getLogger(Shell.class);
	
	private boolean async;
	
	private boolean needResult = true;
	
	private String startCommand;
	
	private String stopCommand;

	private ExecutorService executorService;
	
	private Future<String> future;
	
	private Process process;
	
	private BufferedWriter bw;
	
	private ContextState<ShellState> contextState;
	
	public String start() {
		String[] cmds = {"/bin/bash", "-c", startCommand};
		log.info("Execute Start Command:{}", (Object) cmds);
		ProcessBuilder processBuilder = new ProcessBuilder(cmds);
		processBuilder.redirectErrorStream(true);
		try {
			process = processBuilder.start();
			bw = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
			if (async) {
				future = executorService.submit(this::exec);
				return null;
			}
			String result = exec();
			return result;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}
	
	public void kill() throws IOException, InterruptedException {
		if (stopCommand == null) {
			log.info("destroy process, shell:{}", startCommand);
			process.destroyForcibly();
		} else {
			String[] cmds = {"/bin/bash", "-c", stopCommand};
			log.info("Execute Kill Command:{}, shell:{}", (Object) cmds, startCommand);
			ProcessBuilder processBuilder = new ProcessBuilder(cmds);
			processBuilder.start();
		}
		process.waitFor();
	}
	
	private String exec() throws Exception {
		StringBuilder sb = new StringBuilder();
		try {
			boolean canCallback = false;
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			StringBuilder buf = new StringBuilder();
			while (process.isAlive()) {
				if (br.ready()) {
					char[] cb = new char[4096];
					int len = 0;
					if ((len = br.read(cb)) != -1) {
						canCallback = true;
						buf.append(cb, 0, len);
						int lastIndexOf = buf.lastIndexOf("\n");
						String s1 = buf.substring(0, lastIndexOf);
						String s2 = buf.substring(lastIndexOf + 1);
						String[] arr = s1.split("\n");
						for (String str : arr) {
							log.info(str);
							if (needResult) {
								sb.append(str).append("\n");
							}
						}
						buf.delete(0, buf.length());
						buf.append(s2);
					} else {
						break;
					}
				} else {
					State currentState = contextState.currentState();
					if (canCallback) {
						Set<ShellStateType> set = currentState.type().nexts();
						for (ShellStateType type : set) {
							if (type.match(buf.toString())) {
								StateEvent<String, ShellState> event = null;
								contextState.change(event);
								break;
							}
						}
						canCallback = false;
					} else {
						synchronized (this) {
							if (!process.isAlive()) {
								notifyAll();
								break;
							}
							wait(1000);
						}
					}
				}
			}
			process.waitFor();
			String result = sb.toString().trim();
			return result;
		} finally {
			process.destroyForcibly();
		}
	}
	
	public int exitValue() {
		int value = -1;
		if (process != null) {
			value = process.exitValue();
		}
		return value;
	}
	
	public String result() {
		try {
			if (future != null) {
				return future.get();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
}
