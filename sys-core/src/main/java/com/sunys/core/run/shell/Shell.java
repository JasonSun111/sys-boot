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
import com.sunys.facade.run.Subject;

/**
 * 执行命令
 * Shell
 * @author sunys
 * @date Jun 11, 2020
 */
public class Shell {

	private static final Logger log = LoggerFactory.getLogger(Shell.class);
	
	public static final String LINE_SEPARATOR = System.setProperty("line.separator", "\n");
	public static final String OS_NAME = System.getProperty("os.name");
	
	public static final String[] LINUX_SHELL = {"/bin/bash", "-c"};
	public static final String[] WINDOWS_SHELL = {"cmd.exe", "/c"};
	
	public static final String DEFAULT_ENCODING;
	
	public static final String[] DEFAULT_SHELL;
	
	static {
		if (OS_NAME.toLowerCase().startsWith("win")) {
			DEFAULT_SHELL = new String[] {WINDOWS_SHELL[0], WINDOWS_SHELL[1]};
			DEFAULT_ENCODING = "gbk";
		} else {
			DEFAULT_SHELL = new String[] {LINUX_SHELL[0], LINUX_SHELL[1]};
			DEFAULT_ENCODING = "utf-8";
		}
	}
	
	//是否异步
	private boolean async;
	
	//是否需要获取结果
	private boolean needResult = true;
	
	//日志
	private Logger logger = log;
	
	//开始命令
	private String[] startCommand;
	
	//停止命令
	private String[] stopCommand;

	//缓存行数
	private int maxLine = 20;
	
	//编码方式
	private String encoding = DEFAULT_ENCODING;


	//命令结果
	private StringBuilder sb = new StringBuilder();
	
	//判断是否为可交互状态
	private volatile boolean ready;
	
	//读取到一行的处理器
	private BiConsumer<Shell, String> lineConsumer;
	
	//读取到执行结果的处理器
	private BiConsumer<Shell, String> resultConsumer;
	
	//保存shell的状态
	private ContextState<ShellState> contextState;
	
	//如果为异步，需要用到线程池
	private ExecutorService executorService;
	
	//当前process
	private Process process;
	
	//进程id
	private int pid = -1;
	
	//process的输出流
	private BufferedWriter bw;
	
	private Shell() {
	}
	
	/**
	 * 获取shell创建器
	 * @return
	 */
	public static Shell.Builder builder() {
		Shell.Builder builder = new Shell.Builder();
		return builder;
	}
	
	/**
	 * 执行命令返回结果
	 * @param cmd
	 * @return
	 */
	public static String cmdRun(String cmd) {
		Shell shell = builder().cmdStart(cmd).build();
		String result = shell.start();
		return result;
	}
	
	/**
	 * 开始执行
	 * @return
	 */
	public String start() {
		logger.info("Start Shell:{}", (Object) startCommand);
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
					executorService.submit(this::execLine);
					return null;
				}
				execLine();
			} else {
				if (async) {
					executorService.submit(this::execChar);
					return null;
				}
				execChar();
			}
			return result();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * 停止命令
	 */
	public void stop() {
		try {
			String[] cmds = stopCommand;
			if (cmds == null && process.isAlive()) {
				if (!OS_NAME.toLowerCase().startsWith("win")) {
					cmds = new String[] {LINUX_SHELL[0], LINUX_SHELL[1], "ps -ef | awk '{print $2,$3}' | grep -P \"^(" + pid + " )|( " + pid + ")$\" | awk '{print $1}' | xargs kill -9"};
				}
			}
			if (cmds != null) {
				ProcessBuilder processBuilder = new ProcessBuilder(cmds);
				processBuilder.start();
				logger.info("Execute Kill Command:{}, shell:{}", cmds, startCommand);
			}
			destoryForcibly();
		} catch (Exception e) {
			logger.info(e.getMessage(), e);
		}
	}

	private void destoryForcibly() throws InterruptedException {
		logger.info("destroy process, shell:{}", (Object) startCommand);
		process.destroyForcibly();
		process.waitFor();
	}
	
	/**
	 * 获取进程id
	 * @return
	 */
	public int getPid() {
		return pid;
	}
	
	/**
	 * 获取当前shell状态
	 * @return
	 */
	public ShellState currentState() {
		if (contextState == null) {
			return null;
		}
		ShellState currentState = contextState.currentState();
		return currentState;
	}
	
	/**
	 * 如果shell不是可以交互状态，等待
	 * @throws InterruptedException
	 */
	public synchronized void ready() throws InterruptedException {
		while (!ready) {
			logger.info("try ready wait");
			wait();
		}
	}
	
	/**
	 * 发送命令，如果shell不是可以交互的状态，等待
	 * @param cmds
	 */
	public synchronized void sendCommand(String... cmds) {
		try {
			ready();
			for (String cmd : cmds) {
				logger.info("send cmd:{}", cmd);
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
	
	/**
	 * 执行命令读取行
	 */
	private void execLine() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), encoding));
			String line = null;
			while ((line = br.readLine()) != null) {
				logger.info(line);
				if (needResult) {
					sb.append(line).append(LINE_SEPARATOR);
				}
				if (lineConsumer != null) {
					lineConsumer.accept(this, line);
				}
			}
			process.waitFor();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			logger.info("End Shell:{}", (Object) startCommand);
			process.destroyForcibly();
			if (resultConsumer != null) {
				resultConsumer.accept(this, result());
			}
		}
	}
	
	/**
	 * 执行命令读取字符
	 */
	private void execChar() {
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
							String s2 = buf.substring(lastIndexOf + LINE_SEPARATOR.length()).replace("\r", "");
							String[] arr = s1.split(LINE_SEPARATOR);
							queue.pollLast();
							for (int i = 0; i < arr.length; i++) {
								String str = arr[i].replace("\r", "");
								logger.info(str);
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
						if (peek != null) {
							logger.info(peek.toString().replace("\r", ""));
						}
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
									String str = buf.toString().replace("\r", "");
									currentState.addLine(str);
									sb.append(str);
								}
								if (lineConsumer != null) {
									lineConsumer.accept(this, buf.toString());
								}
								ShellState state = currentState.type().getState(ShellStateType.END_BASH);
								if (state != null) {
									StateEvent<String, ShellState> event = new StateEventImpl<>(buf.toString(), state, new StateEventType(ShellStateType.END_BASH.getName()));
									contextState.change(event);
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
			logger.error(e.getMessage(), e);
		} finally {
			logger.info("End Shell:{}", (Object) startCommand);
			process.destroyForcibly();
			if (resultConsumer != null) {
				resultConsumer.accept(this, result());
			}
		}
	}
	
	/**
	 * 获取shell执行返回值
	 * @return
	 */
	public int exitValue() {
		int value = -1;
		if (process != null) {
			value = process.exitValue();
		}
		return value;
	}
	
	/**
	 * 等待命令执行完成，返回执行结果
	 * @return
	 * @throws InterruptedException
	 */
	public String waitResult() throws InterruptedException {
		process.waitFor();
		return result();
	}
	
	/**
	 * 获取执行结果
	 * @return
	 */
	public String result() {
		String result = sb.toString().trim();
		return result;
	}
	
	/**
	 * Builder
	 * @author sunys
	 * @date Jun 11, 2020
	 */
	public static class Builder {
		
		private Shell shell = new Shell();
		
		private ShellState.ShellStateBuilder shellStateBuilder;
		
		/**
		 * 设置开始命令
		 * @param cmds
		 * @return
		 */
		public Builder start(String... cmds) {
			shell.startCommand = cmds;
			return this;
		}
		
		/**
		 * 设置开始命令
		 * @param cmd
		 * @return
		 */
		public Builder cmdStart(String cmd) {
			return start(new String[] {DEFAULT_SHELL[0], DEFAULT_SHELL[1], cmd});
		}
		
		/**
		 * 设置停止命令
		 * @param cmds
		 * @return
		 */
		public Builder stop(String... cmds) {
			shell.stopCommand = cmds;
			return this;
		}
		
		/**
		 * 设置停止命令
		 * @param cmd
		 * @return
		 */
		public Builder cmdStop(String cmd) {
			return stop(new String[] {DEFAULT_SHELL[0], DEFAULT_SHELL[1], cmd});
		}
		
		/**
		 * 设置ShellState
		 * @param shellState
		 * @return
		 */
		public ShellState.ShellStateBuilder state(ShellState shellState) {
			this.shellStateBuilder = new ShellState.ShellStateBuilder(this, shellState);
			return shellStateBuilder;
		}
		
		/**
		 * 设置ShellState
		 * @param name
		 * @param pattern
		 * @param subject
		 * @return
		 */
		public ShellState.ShellStateBuilder state(String name, Pattern pattern, Subject subject) {
			this.shellStateBuilder = new ShellState.ShellStateBuilder(this, name, pattern, subject);
			return shellStateBuilder;
		}
		
		/**
		 * 设置ShellState
		 * @param name
		 * @param pattern
		 * @return
		 */
		public ShellState.ShellStateBuilder state(String name, Pattern pattern) {
			return state(name, pattern, null);
		}
		
		/**
		 * 设置ShellState
		 * @param shellStateParam
		 * @return
		 */
		public ShellState.ShellStateBuilder state(ShellStateParam shellStateParam) {
			this.shellStateBuilder = new ShellState.ShellStateBuilder(this, shellStateParam);
			return shellStateBuilder;
		}
		
		/**
		 * 设置ShellState
		 * @return
		 */
		public ShellState.ShellStateBuilder state() {
			return state(ShellStateType.BIN_BASH_NAME, ShellStateType.BIN_BASH_PATTERN);
		}
		
		/**
		 * 设置读取行处理器
		 * @param consumer
		 * @return
		 */
		public Builder line(BiConsumer<Shell, String> consumer) {
			shell.lineConsumer = consumer;
			return this;
		}
		
		/**
		 * 设置读取结果处理器
		 * @param consumer
		 * @return
		 */
		public Builder result(BiConsumer<Shell, String> consumer) {
			shell.resultConsumer = consumer;
			return this;
		}
		
		/**
		 * 设置为异步执行
		 * @param executorService
		 * @return
		 */
		public Builder async(ExecutorService executorService) {
			shell.async = true;
			shell.executorService = executorService;
			return this;
		}
		
		/**
		 * 设置最大缓存行数
		 * @param maxLine
		 * @return
		 */
		public Builder maxLine(int maxLine) {
			shell.maxLine = maxLine;
			return this;
		}
		
		/**
		 * 设置是否需要获取执行结果
		 * @param needResult
		 * @return
		 */
		public Builder needResult(boolean needResult) {
			shell.needResult = needResult;
			return this;
		}
		
		/**
		 * 设置读取编码
		 * @param encoding
		 * @return
		 */
		public Builder encoding(String encoding) {
			shell.encoding = encoding;
			return this;
		}
		
		/**
		 * 设置打印日志
		 * @param logger
		 * @return
		 */
		public Builder logger(Logger logger) {
			shell.logger = logger;
			return this;
		}
		
		/**
		 * 创建
		 * @return
		 */
		public Shell build() {
			if (shellStateBuilder != null) {
				//如果shellStateBuilder不为空，创建可交互的shell
				ShellState shellState = shellStateBuilder.build();
				shell.contextState = new ContextStateImpl<>(shellState);
			}
			return shell;
		}
		
		/**
		 * 查看shell
		 * @param consumer
		 */
		public void peek(Consumer<Shell> consumer) {
			consumer.accept(shell);
		}
	}

}
