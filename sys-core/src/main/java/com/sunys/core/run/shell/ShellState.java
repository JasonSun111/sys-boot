package com.sunys.core.run.shell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;

import com.sunys.core.run.impl.StateEventType;
import com.sunys.core.run.impl.StateImpl;
import com.sunys.facade.run.EventHandler;
import com.sunys.facade.run.Subject;

/**
 * shell执行状态
 * ShellState
 * @author sunys
 * @date Jun 11, 2020
 */
public class ShellState extends StateImpl<ShellStateType> {

	private List<String> lines = new ArrayList<>();
	
	public ShellState(Subject subject, ShellStateType type) {
		super(subject, type);
	}
	
	public ShellState(ShellStateType type) {
		this(null, type);
	}
	
	/**
	 * 添加命令执行结果
	 * @param line
	 */
	public void addLine(String line) {
		lines.add(line);
	}
	
	/**
	 * 获取命令执行结果
	 * @return
	 */
	public String result() {
		String result = String.join(Shell.LINE_SEPARATOR, lines);
		return result;
	}
	
	/**
	 * ShellStateBuilder
	 * @author sunys
	 * @date Jun 11, 2020
	 */
	public static class ShellStateBuilder {
		
		private Shell shell;
		
		private Shell.Builder shellBuilder;
		
		private ShellState shellState;
		
		private ShellStateBuilder preShellStateBuilder;
		
		private Map<String, ShellStateBuilder> shellStateBuilderMap = new HashMap<>();
		
		public ShellStateBuilder(Shell shell, ShellState shellState) {
			this.shellState = shellState;
			this.shell = shell;
		}
		
		public ShellStateBuilder(Shell shell, String name, Pattern pattern, Subject subject) {
			ShellStateType shellStateType = new ShellStateType(name, pattern);
			this.shellState = new ShellState(subject, shellStateType);
			this.shell = shell;
		}
		
		public ShellStateBuilder(Shell shell, String name, Pattern pattern) {
			this(shell, name, pattern, null);
		}
		
		public ShellStateBuilder(Shell shell, ShellStateParam shellStateParam) {
			this(shell, shellStateParam.getName(), shellStateParam.getPattern(), shellStateParam.getSubject());
			shellStateParam.setShellState(shellState);
		}
		
		public ShellStateBuilder(Shell.Builder shellBuilder, ShellState shellState) {
			this.shellState = shellState;
			this.shellBuilder = shellBuilder;
			shellBuilder.peek(sh -> shell = sh);
		}
		
		public ShellStateBuilder(Shell.Builder shellBuilder, String name, Pattern pattern, Subject subject) {
			ShellStateType shellStateType = new ShellStateType(name, pattern);
			this.shellState = new ShellState(subject, shellStateType);
			this.shellBuilder = shellBuilder;
			shellBuilder.peek(sh -> shell = sh);
		}
		
		public ShellStateBuilder(Shell.Builder shellBuilder, String name, Pattern pattern) {
			this(shellBuilder, name, pattern, null);
		}
		
		public ShellStateBuilder(Shell.Builder shellBuilder, ShellStateParam shellStateParam) {
			this(shellBuilder, shellStateParam.getName(), shellStateParam.getPattern(), shellStateParam.getSubject());
			shellStateParam.setShellState(shellState);
		}
		
		/**
		 * 添加状态
		 * @param name
		 * @param pattern
		 * @param subject
		 * @return
		 */
		public ShellStateBuilder add(String name, Pattern pattern, Subject subject) {
			ShellStateBuilder shellStateBuilder = new ShellStateBuilder(shell, name, pattern, subject);
			shellStateBuilder.preShellStateBuilder = this;
			putShellStateBuilder(shellStateBuilder);
			return this;
		}
		
		/**
		 * 添加状态
		 * @param shellStateParam
		 * @return
		 */
		public ShellStateBuilder add(ShellStateParam shellStateParam) {
			ShellStateBuilder shellStateBuilder = new ShellStateBuilder(shell, shellStateParam);
			shellStateBuilder.preShellStateBuilder = this;
			putShellStateBuilder(shellStateBuilder);
			return this;
		}
		
		/**
		 * 添加状态
		 * @param name
		 * @param pattern
		 * @return
		 */
		public ShellStateBuilder add(String name, Pattern pattern) {
			return add(name, pattern, null);
		}
		
		/**
		 * 添加状态
		 * @param shellState
		 * @return
		 */
		public ShellStateBuilder add(ShellState shellState) {
			ShellStateBuilder shellStateBuilder = new ShellStateBuilder(shell, shellState);
			shellStateBuilder.preShellStateBuilder = this;
			putShellStateBuilder(shellStateBuilder);
			return this;
		}
		
		private void putShellStateBuilder(ShellStateBuilder shellStateBuilder) {
			shellState.type().addState(shellStateBuilder.shellState.type(), shellStateBuilder.shellState);
			shellStateBuilderMap.put(shellStateBuilder.shellState.type().getName(), shellStateBuilder);
		}
		
		/**
		 * 添加当前状态，设置处理器
		 * @param consumer
		 * @return
		 */
		public ShellStateBuilder addCurrent(BiConsumer<Shell, String> consumer) {
			shellState.type().addState(shellState.type(), shellState);
			EventHandler<?> eventHandler = new ShellReadyEventHandler(shell, consumer);
			shellState.registEventHandler(new StateEventType(shellState.type().getName()), eventHandler);
			return this;
		}
		
		/**
		 * 添加当前状态
		 * @return
		 */
		public ShellStateBuilder addCurrent() {
			return addCurrent(null);
		}
		
		/**
		 * 添加前几个的状态，设置处理器
		 * @param count
		 * @param consumer
		 * @return
		 */
		public ShellStateBuilder addPre(int count, BiConsumer<Shell, String> consumer) {
			ShellStateBuilder shellStateBuilder = this;
			while (count > 0) {
				shellStateBuilder = shellStateBuilder.pre();
				count--;
			}
			shellState.type().addState(shellStateBuilder.shellState.type(), shellStateBuilder.shellState);
			EventHandler<?> eventHandler = new ShellReadyEventHandler(shell, consumer);
			shellState.registEventHandler(new StateEventType(shellState.type().getName()), eventHandler);
			return this;
		}
		
		/**
		 * 添加前一个状态，设置处理器
		 * @param consumer
		 * @return
		 */
		public ShellStateBuilder addPre(BiConsumer<Shell, String> consumer) {
			return addPre(1, consumer);
		}
		
		/**
		 * 添加前几个状态
		 * @param count
		 * @return
		 */
		public ShellStateBuilder addPre(int count) {
			return addPre(count, null);
		}
		
		/**
		 * 设置下一个状态
		 * @param name
		 * @return
		 */
		public ShellStateBuilder next(String name) {
			ShellStateBuilder shellStateBuilder = shellStateBuilderMap.get(name);
			return shellStateBuilder;
		}
		
		/**
		 * 返回上一个状态
		 * @return
		 */
		public ShellStateBuilder pre() {
			return preShellStateBuilder;
		}
		
		/**
		 * 设置转化状态的处理器
		 * @param name
		 * @param consumer
		 * @return
		 */
		public ShellStateBuilder addHandler(String name, BiConsumer<Shell, String> consumer) {
			EventHandler<?> eventHandler = new ShellReadyEventHandler(shell, consumer);
			shellState.registEventHandler(new StateEventType(name), eventHandler);
			return this;
		}
		
		/**
		 * 返回创建好的状态
		 * @return
		 */
		public ShellState build() {
			if (preShellStateBuilder == null) {
				return shellState;
			} else {
				return pre().build();
			}
		}
		
		/**
		 * 返回shell的创建器
		 * @return
		 */
		public Shell.Builder shellBuilder() {
			if (preShellStateBuilder == null) {
				return shellBuilder;
			} else {
				return pre().shellBuilder();
			}
		}
	}

}
