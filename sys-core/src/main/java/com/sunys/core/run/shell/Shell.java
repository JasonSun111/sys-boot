package com.sunys.core.run.shell;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Deque;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunys.core.run.impl.StateEventImpl;
import com.sunys.core.run.impl.StateEventType;
import com.sunys.core.util.LimitQueue;
import com.sunys.facade.run.ContextState;
import com.sunys.facade.run.State;
import com.sunys.facade.run.StateEvent;

public class Shell {

	private static final Logger log = LoggerFactory.getLogger(Shell.class);
	
	private boolean async;
	
	private boolean needResult = true;
	
	private String startCommand;
	
	private String stopCommand;

	private int maxLine = 10;
	
	private String encoding = "utf-8";
	
	private ExecutorService executorService;
	
	private Future<String> future;
	
	private Process process;
	
	private BufferedWriter bw;
	
	private ContextState<State<ShellStateType>> contextState;
	
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
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), encoding));
			Deque<StringBuilder> queue = new LimitQueue<>(maxLine);
			StringBuilder buf = new StringBuilder();
			while (process.isAlive()) {
				if (br.ready()) {
					char[] cb = new char[2];
					int len = 0;
					if ((len = br.read(cb)) != -1) {
						canCallback = true;
						buf.append(cb, 0, len);
						int lastIndexOf = buf.lastIndexOf("\n");
						if (lastIndexOf >= 0) {
							String s1 = buf.substring(0, lastIndexOf);
							String s2 = buf.substring(lastIndexOf + 1);
							String[] arr = s1.split("\n");
							queue.pollLast();
							for (int i = 0; i < arr.length; i++) {
								String str = arr[i];
								log.info(str);
								queue.offer(new StringBuilder(str));
								if (needResult) {
									sb.append(str).append("\n");
								}
							}
							log.info(s2);
							buf.delete(0, buf.length());
							buf.append(s2);
							queue.offer(new StringBuilder(s2));
						} else {
							StringBuilder peek = queue.peekLast();
							if (peek != null) {
								peek.append(cb, 0, len);
							}
						}
					} else {
						break;
					}
				} else {
					State<ShellStateType> currentState = contextState.currentState();
					if (canCallback) {
						Set<ShellStateType> set = currentState.type().nexts();
						String str = String.join("\n", queue);
						for (ShellStateType type : set) {
							if (type.match(str.toString())) {
								State<ShellStateType> state = currentState.type().getState(type);
								StateEvent<String, State<ShellStateType>> event = new StateEventImpl<>(str, state, new StateEventType(type.getName()));
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
