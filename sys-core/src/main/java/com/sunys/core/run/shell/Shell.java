package com.sunys.core.run.shell;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Shell {

	private static final Logger logger = LoggerFactory.getLogger(Shell.class);
	
	private boolean async;
	
	private String startCommand;
	
	private String stopCommand;

	private ExecutorService executorService;
	
	private Future<String> future;
	
	private Process process;
	
	private BufferedWriter bw;
	
	public String start() {
		String[] cmds = {"/bin/bash", "-c", startCommand};
		logger.info("Execute Start Command:{}", (Object) cmds);
		ProcessBuilder processBuilder = new ProcessBuilder(cmds);
		processBuilder.redirectErrorStream(true);
		try {
			process = processBuilder.start();
			bw = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
			future = executorService.submit(this::exec);
			if (async) {
				return null;
			}
			String result = future.get();
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}
	
	public void kill() throws IOException, InterruptedException {
		if (stopCommand == null) {
			logger.info("destroy process, shell:{}", startCommand);
			process.destroyForcibly();
		} else {
			String[] cmds = {"/bin/bash", "-c", stopCommand};
			logger.info("Execute Kill Command:{}, shell:{}", (Object) cmds, startCommand);
			ProcessBuilder processBuilder = new ProcessBuilder(cmds);
			processBuilder.start();
		}
		process.waitFor();
	}
	
	private String exec() throws Exception {
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			while (process.isAlive()) {
				if (br.ready()) {
					char[] buf = new char[4096];
					int len = 0;
					if ((len = br.read(buf)) != -1) {
						sb.append(new String(buf, 0, len));
					} else {
						break;
					}
				} else {
					
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
			logger.error(e.getMessage(), e);
		}
		return null;
	}
}
