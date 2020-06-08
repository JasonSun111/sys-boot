package com.sunys.core.run.shell;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
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
import com.sunys.facade.run.StateEvent;

public class Shell {

	private static final Logger log = LoggerFactory.getLogger(Shell.class);
	
	private boolean async;
	
	private boolean needResult = true;
	
	private boolean ready = true;
	
	private String startCommand;
	
	private String stopCommand;

	private int maxLine = 40;
	
	private String encoding = "utf-8";
	
	private ExecutorService executorService;
	
	private Future<String> future;
	
	private Process process;
	
	private Integer pid;
	
	private BufferedWriter bw;
	
	private ContextState<ShellState> contextState;
	
	public String start() {
		String[] cmds = {"/bin/bash", "-c", startCommand};
		log.info("Execute Start Command:{}", (Object) cmds);
		ProcessBuilder processBuilder = new ProcessBuilder(cmds);
		processBuilder.redirectErrorStream(true);
		try {
			process = processBuilder.start();
			Class<?> clazz = Class.forName("java.lang.UNIXProcess");
			Field field = clazz.getDeclaredField("pid");
			field.setAccessible(true);
			pid = field.getInt(process);
			bw = new BufferedWriter(new OutputStreamWriter(process.getOutputStream(), encoding));
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
	
	public void stop() throws IOException, InterruptedException {
		String cmd = stopCommand;
		if (stopCommand == null) {
			cmd = "ps -ef | grep " + pid + " | grep -v grep | awk '{print $2}' | xargs kill -9";
		}
		String[] cmds = {"/bin/bash", "-c", cmd};
		ProcessBuilder processBuilder = new ProcessBuilder(cmds);
		processBuilder.start();
		log.info("Execute Kill Command:{}, shell:{}", (Object) cmds, startCommand);
		destoryForcibly();
	}

	private void destoryForcibly() throws InterruptedException {
		log.info("destroy process, shell:{}", startCommand);
		process.destroyForcibly();
		process.waitFor();
	}
	
	private synchronized void ready() throws InterruptedException {
		if (!ready) {
			wait();
		}
	}
	
	public void sendCommand(String... cmds) throws IOException, InterruptedException {
		ready();
		for (String cmd : cmds) {
			bw.write(cmd);
			bw.newLine();
			bw.flush();
		}
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
						ShellState currentState = contextState.currentState();
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
								currentState.addLine(str);
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
					ShellState currentState = contextState.currentState();
					if (canCallback) {
						Set<ShellStateType> set = currentState.type().nexts();
						String str = String.join("\n", queue);
						for (ShellStateType type : set) {
							if (type.match(str.toString())) {
								ShellState state = currentState.type().getState(type);
								StateEvent<String, ShellState> event = new StateEventImpl<>(str, state, new StateEventType(type.getName()));
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
