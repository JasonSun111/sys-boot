package com.sunys.core.run.shell;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.util.Deque;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunys.core.run.impl.ContextStateImpl;
import com.sunys.core.run.impl.StateEventImpl;
import com.sunys.core.run.impl.StateEventType;
import com.sunys.core.util.LimitQueue;
import com.sunys.facade.run.ContextState;
import com.sunys.facade.run.StateEvent;
import com.sunys.facade.run.Subject;

public class Shell {

	private static final Logger log = LoggerFactory.getLogger(Shell.class);
	
	public static final String LINE_SEPARATOR = System.setProperty("line.separator", "\n");
	public static final String OS_NAME = System.getProperty("os.name");
	
	public static final String[] SHELL_LINUX = {"/bin/bash", "-c"};
	public static final String[] SHELL_WINDOWS = {"cmd.exe", "/c"};
	
	public static final String DEFAULT_ENCODING;
	
	public static final String[] SHELL;
	
	static {
		if (OS_NAME.toLowerCase().startsWith("win")) {
			SHELL = new String[] {SHELL_WINDOWS[0], SHELL_WINDOWS[1]};
			DEFAULT_ENCODING = "gbk";
		} else {
			SHELL = new String[] {SHELL_LINUX[0], SHELL_LINUX[1]};
			DEFAULT_ENCODING = "utf-8";
		}
	}
	
	private boolean async;
	
	private boolean needResult = true;
	
	private String[] startCommand;
	
	private String[] stopCommand;

	private int maxLine = 20;
	
	private String encoding = DEFAULT_ENCODING;


	private StringBuilder sb = new StringBuilder();
	
	private volatile boolean ready;
	
	private BiConsumer<Shell, String> lineConsumer;
	
	private BiConsumer<Shell, String> resultConsumer;
	
	private ContextState<ShellState> contextState;
	
	private ExecutorService executorService;
	
	private Process process;
	
	private int pid = -1;
	
	private BufferedWriter bw;
	
	private Shell() {
	}
	
	public static Shell.Builder builder() {
		Shell.Builder builder = new Shell.Builder();
		return builder;
	}
	
	public static String cmdRun(String cmd) {
		Shell shell = builder().cmdStart(cmd).build();
		String result = shell.start();
		return result;
	}
	
	public String start() {
		log.info("Start Shell:{}", (Object) startCommand);
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
			if (contextState == null) {
				if (async) {
					executorService.submit(this::exec1);
					return null;
				}
				exec1();
			} else {
				if (async) {
					executorService.submit(this::exec2);
					return null;
				}
				exec2();
			}
			return result();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}
	
	public void stop() {
		try {
			String[] cmds = stopCommand;
			if (cmds == null) {
				cmds = new String[] {"/bin/bash", "-c", "ps -ef | grep " + pid + " | grep -v grep | awk '{print $2}' | xargs kill -9"};
			}
			ProcessBuilder processBuilder = new ProcessBuilder(cmds);
			processBuilder.start();
			log.info("Execute Kill Command:{}, shell:{}", cmds, startCommand);
			destoryForcibly();
		} catch (Exception e) {
			log.info(e.getMessage(), e);
		}
	}

	private void destoryForcibly() throws InterruptedException {
		log.info("destroy process, shell:{}", (Object) startCommand);
		process.destroyForcibly();
		process.waitFor();
	}
	
	public int getPid() {
		return pid;
	}
	
	public ShellState currentState() {
		if (contextState == null) {
			return null;
		}
		ShellState currentState = contextState.currentState();
		return currentState;
	}
	
	public synchronized void ready() throws InterruptedException {
		while (!ready) {
			log.info("try ready wait");
			wait();
		}
	}
	
	public synchronized void sendCommand(String... cmds) {
		try {
			ready();
			for (String cmd : cmds) {
				log.info("send cmd:{}", cmd);
				bw.write(cmd);
				bw.newLine();
				bw.flush();
			}
			ready = false;
			notifyAll();
		} catch (Exception e) {
			String msg = String.join(",", cmds);
			throw new ShellException(msg, e);
		}
	}
	
	private void exec1() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), encoding));
			String line = null;
			while ((line = br.readLine()) != null) {
				log.info(line);
				if (needResult) {
					sb.append(line).append(LINE_SEPARATOR);
				}
				if (lineConsumer != null) {
					lineConsumer.accept(this, line);
				}
			}
			process.waitFor();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			log.info("End Shell:{}", (Object) startCommand);
			process.destroyForcibly();
			if (resultConsumer != null) {
				resultConsumer.accept(this, result());
			}
		}
	}
	
	private void exec2() {
		try {
			boolean canCallback = false;
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), encoding));
			Deque<StringBuilder> queue = new LimitQueue<>(maxLine);
			StringBuilder buf = new StringBuilder();
			while (true) {
				if (br.ready()) {
					ready = false;
					char[] cb = new char[2048];
					int len = 0;
					if ((len = br.read(cb)) != -1) {
						canCallback = true;
						ShellState currentState = contextState.currentState();
						buf.append(cb, 0, len);
						int lastIndexOf = buf.lastIndexOf(LINE_SEPARATOR);
						if (lastIndexOf >= 0) {
							String s1 = buf.substring(0, lastIndexOf);
							String s2 = buf.substring(lastIndexOf + LINE_SEPARATOR.length());
							String[] arr = s1.split(LINE_SEPARATOR);
							queue.pollLast();
							for (int i = 0; i < arr.length; i++) {
								String str = arr[i];
								log.info(str);
								queue.offer(new StringBuilder(str));
								if (needResult) {
									currentState.addLine(str);
									sb.append(str).append(LINE_SEPARATOR);
								}
								if (lineConsumer != null) {
									lineConsumer.accept(this, str);
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
					}
				} else {
					ShellState currentState = contextState.currentState();
					if (canCallback) {
						StringBuilder peek = queue.peekLast();
						log.info(peek.toString());
						ready = true;
						Set<ShellStateType> set = currentState.type().nexts();
						String str = String.join(LINE_SEPARATOR, queue);
						for (ShellStateType type : set) {
							if (type.match(str)) {
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
								if (needResult) {
									currentState.addLine(buf.toString());
									sb.append(buf.toString());
								}
								if (lineConsumer != null) {
									lineConsumer.accept(this, buf.toString());
								}
								notifyAll();
								break;
							}
							wait(1000);
						}
					}
				}
			}
			process.waitFor();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			log.info("End Shell:{}", (Object) startCommand);
			process.destroyForcibly();
			if (resultConsumer != null) {
				resultConsumer.accept(this, result());
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
	
	public String waitResult() throws InterruptedException {
		process.waitFor();
		return result();
	}
	
	public String result() {
		String result = sb.toString().trim();
		return result;
	}
	
	public static class Builder {
		
		private Shell shell = new Shell();
		
		private ShellState.ShellStateBuilder shellStateBuilder;
		
		public Builder start(String... cmds) {
			shell.startCommand = cmds;
			return this;
		}
		
		public Builder cmdStart(String cmd) {
			return start(new String[] {SHELL[0], SHELL[1], cmd});
		}
		
		public Builder stop(String... cmds) {
			shell.stopCommand = cmds;
			return this;
		}
		
		public Builder cmdStop(String cmd) {
			return stop(new String[] {SHELL[0], SHELL[1], cmd});
		}
		
		public ShellState.ShellStateBuilder state(ShellState shellState) {
			this.shellStateBuilder = new ShellState.ShellStateBuilder(this, shellState);
			return shellStateBuilder;
		}
		
		public ShellState.ShellStateBuilder state(String name, Pattern pattern, Subject subject) {
			this.shellStateBuilder = new ShellState.ShellStateBuilder(this, name, pattern, subject);
			return shellStateBuilder;
		}
		
		public ShellState.ShellStateBuilder state(String name, Pattern pattern) {
			return state(name, pattern, null);
		}
		
		public ShellState.ShellStateBuilder state() {
			return state(ShellStateType.BIN_BASH_NAME, ShellStateType.BIN_BASH_PATTERN);
		}
		
		public Builder line(BiConsumer<Shell, String> consumer) {
			shell.lineConsumer = consumer;
			return this;
		}
		
		public Builder result(BiConsumer<Shell, String> consumer) {
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
			if (shellStateBuilder != null) {
				ShellState shellState = shellStateBuilder.build();
				shell.contextState = new ContextStateImpl<>(shellState);
			}
			return shell;
		}
		
		public Shell getShell() {
			return shell;
		}
	}

}
