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
import java.util.function.Consumer;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunys.core.run.impl.ContextStateImpl;
import com.sunys.core.run.impl.StateEventImpl;
import com.sunys.core.run.impl.StateEventType;
import com.sunys.core.util.LimitQueue;
import com.sunys.facade.run.ContextState;
import com.sunys.facade.run.StateEvent;

public class Shell {

	private static final Logger log = LoggerFactory.getLogger(Shell.class);
	
	public static final String lineSeparator = System.setProperty("line.separator", "\n");
	
	public static final String OS_NAME = System.getProperty("os.name");
	
	public static final String[] SHELL_LINUX = {"/bin/bash", "-c"};
	public static final String[] SHELL_WINDOWS = {"cmd.exe", "/c"};
	
	public static String DEFAULT_ENCODING = "utf-8";
	
	public static String[] SHELL = {SHELL_LINUX[0], SHELL_LINUX[1]};
	
	static {
		if (OS_NAME.toLowerCase().startsWith("win")) {
			SHELL = new String[] {SHELL_WINDOWS[0], SHELL_WINDOWS[1]};
			DEFAULT_ENCODING = "gbk";
		}
	}
	
	private boolean async;
	
	private boolean needResult = true;
	
	private String[] startCommand;
	
	private String[] stopCommand;

	private int maxLine = 40;
	
	private String encoding = DEFAULT_ENCODING;


	private StringBuilder sb = new StringBuilder();
	
	private volatile boolean ready = true;
	
	private String result;
	
	private Consumer<String> lineConsumer;
	
	private Consumer<String> resultConsumer;
	
	private ContextState<ShellState> contextState;
	
	private ExecutorService executorService;
	
	private Future<String> future;
	
	private Process process;
	
	private int pid = -1;
	
	private BufferedWriter bw;
	
	private Shell() {
	}
	
	public static Shell.Builder builder() {
		Shell.Builder builder = new Shell.Builder();
		builder.shell = new Shell();
		return builder;
	}
	
	public static String cmdRun(String cmd) {
		Shell shell = builder().cmdStart(cmd).build();
		String result = shell.start();
		return result;
	}
	
	public String start() {
		log.info("Execute Start Command:{}", (Object) startCommand);
		ProcessBuilder processBuilder = new ProcessBuilder(startCommand);
		processBuilder.redirectErrorStream(true);
		try {
			process = processBuilder.start();
			if (!OS_NAME.toLowerCase().startsWith("win")) {
				Class<?> clazz = Class.forName("java.lang.UNIXProcess");
				Field field = clazz.getDeclaredField("pid");
				field.setAccessible(true);
				pid = field.getInt(process);
			}
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
		String[] cmds = stopCommand;
		if (cmds == null) {
			cmds = new String[] {"/bin/bash", "-c", "ps -ef | grep " + pid + " | grep -v grep | awk '{print $2}' | xargs kill -9"};
		}
		ProcessBuilder processBuilder = new ProcessBuilder(cmds);
		processBuilder.start();
		log.info("Execute Kill Command:{}, shell:{}", cmds, startCommand);
		destoryForcibly();
	}

	private void destoryForcibly() throws InterruptedException {
		log.info("destroy process, shell:{}", (Object) startCommand);
		process.destroyForcibly();
		process.waitFor();
	}
	
	public int getPid() {
		return pid;
	}
	
	private synchronized void ready() throws InterruptedException {
		while (!ready) {
			log.info("try ready wait");
			wait();
		}
	}
	
	public synchronized void sendCommand(String... cmds) throws IOException, InterruptedException {
		ready();
		for (String cmd : cmds) {
			log.info("send cmd:{}", cmd);
			bw.write(cmd);
			bw.newLine();
			bw.flush();
		}
		ready = false;
		notifyAll();
	}
	
	private String exec() throws Exception {
		try {
			boolean canCallback = false;
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), encoding));
			Deque<StringBuilder> queue = new LimitQueue<>(maxLine);
			StringBuilder buf = new StringBuilder();
			while (true) {
				if (br.ready()) {
					ready = false;
					char[] cb = new char[3];
					int len = 0;
					if ((len = br.read(cb)) != -1) {
						canCallback = true;
						ShellState currentState = contextState.currentState();
						buf.append(cb, 0, len);
						int lastIndexOf = buf.lastIndexOf(lineSeparator);
						if (lastIndexOf >= 0) {
							String s1 = buf.substring(0, lastIndexOf);
							String s2 = buf.substring(lastIndexOf + lineSeparator.length());
							String[] arr = s1.split(lineSeparator);
							queue.pollLast();
							for (int i = 0; i < arr.length; i++) {
								String str = arr[i];
								log.info(str);
								currentState.addLine(str);
								if (lineConsumer != null) {
									lineConsumer.accept(str);
								}
								queue.offer(new StringBuilder(str));
								if (needResult) {
									sb.append(str).append(lineSeparator);
								}
							}
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
						ready = true;
						Set<ShellStateType> set = currentState.type().nexts();
						String str = String.join(lineSeparator, queue);
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
								currentState.addLine(buf.toString());
								if (lineConsumer != null) {
									lineConsumer.accept(buf.toString());
								}
								sb.append(buf.toString());
								notifyAll();
								break;
							}
							wait(1000);
						}
					}
				}
			}
			process.waitFor();
			result = sb.toString().trim();
			return result;
		} finally {
			log.info("Execute Stop Command:{}", (Object) startCommand);
			process.destroyForcibly();
			if (resultConsumer != null) {
				resultConsumer.accept(result);
			}
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
				future.get();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}
	
	public static class Builder {
		
		private Shell shell;
		
		private ShellState.ShellStateBuilder shellStateBuilder = state();
		
		public Builder start(String... startCommand) {
			shell.startCommand = startCommand;
			return this;
		}
		
		public Builder cmdStart(String cmd) {
			return start(new String[] {SHELL[0], SHELL[1], cmd});
		}
		
		public Builder stop(String... stopCommand) {
			shell.stopCommand = stopCommand;
			return this;
		}
		
		public Builder cmdStop(String cmd) {
			return stop(new String[] {SHELL[0], SHELL[1], cmd});
		}
		
		public ShellState.ShellStateBuilder state(String name, Pattern pattern) {
			this.shellStateBuilder = new ShellState.ShellStateBuilder(this, name, pattern);
			return shellStateBuilder;
		}
		
		public ShellState.ShellStateBuilder state() {
			return state(ShellStateType.BIN_BASH_NAME, ShellStateType.BIN_BASH_PATTERN);
		}
		
		public Builder line(Consumer<String> consumer) {
			shell.lineConsumer = consumer;
			return this;
		}
		
		public Builder result(Consumer<String> consumer) {
			shell.resultConsumer = consumer;
			return this;
		}
		
		public Builder async(ExecutorService executorService) {
			shell.async = true;
			shell.executorService = executorService;
			return this;
		}
		
		public Builder maxLine(int maxLine) {
			shell.maxLine = maxLine;
			return this;
		}
		
		public Builder needResult(boolean needResult) {
			shell.needResult = needResult;
			return this;
		}
		
		public Builder encoding(String encoding) {
			shell.encoding = encoding;
			return this;
		}
		
		public Shell build() {
			if (shell.contextState == null) {
				ShellState shellState = shellStateBuilder.build();
				shell.contextState = new ContextStateImpl<ShellState>(shellState);
			}
			return shell;
		}
		
		public Shell getShell() {
			return shell;
		}
	}

}
