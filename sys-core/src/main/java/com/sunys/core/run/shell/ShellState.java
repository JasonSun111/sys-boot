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
			this.shell = shellBuilder.peek();
		}
		
		public ShellStateBuilder(Shell.Builder shellBuilder, String name, Pattern pattern, Subject subject) {
			ShellStateType shellStateType = new ShellStateType(name, pattern);
			this.shellState = new ShellState(subject, shellStateType);
			this.shellBuilder = shellBuilder;
			this.shell = shellBuilder.peek();
		}
		
		public ShellStateBuilder(Shell.Builder shellBuilder, String name, Pattern pattern) {
			this(shellBuilder, name, pattern, null);
		}
		
		public ShellStateBuilder(Shell.Builder shellBuilder, ShellStateParam shellStateParam) {
			this(shellBuilder, shellStateParam.getName(), shellStateParam.getPattern(), shellStateParam.getSubject());
			shellStateParam.setShellState(shellState);
		}
		
		public ShellStateBuilder add(String name, Pattern pattern, Subject subject) {
			ShellStateBuilder shellStateBuilder = new ShellStateBuilder(shell, name, pattern, subject);
			shellStateBuilder.preShellStateBuilder = this;
			putShellStateBuilder(shellStateBuilder);
			return this;
		}
		
		public ShellStateBuilder add(ShellStateParam shellStateParam) {
			ShellStateBuilder shellStateBuilder = new ShellStateBuilder(shell, shellStateParam);
			shellStateBuilder.preShellStateBuilder = this;
			putShellStateBuilder(shellStateBuilder);
			return this;
		}
		
		public ShellStateBuilder add(String name, Pattern pattern) {
			return add(name, pattern, null);
		}
		
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
		
		public ShellStateBuilder addCurrent(BiConsumer<Shell, String> consumer) {
			shellState.type().addState(shellState.type(), shellState);
			EventHandler<?> eventHandler = new ShellReadyEventHandler(shell, consumer);
			shellState.registEventHandler(new StateEventType(shellState.type().getName()), eventHandler);
			return this;
		}
		
		public ShellStateBuilder addCurrent() {
			return addCurrent(null);
		}
		
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
		
		public ShellStateBuilder addPre(BiConsumer<Shell, String> consumer) {
			return addPre(1, consumer);
		}
		
		public ShellStateBuilder addPre(int count) {
			return addPre(count, null);
		}
		
		public ShellStateBuilder next(String name) {
			ShellStateBuilder shellStateBuilder = shellStateBuilderMap.get(name);
			return shellStateBuilder;
		}
		
		public ShellStateBuilder pre() {
			return preShellStateBuilder;
		}
		
		public ShellStateBuilder addHandler(String name, BiConsumer<Shell, String> consumer) {
			EventHandler<?> eventHandler = new ShellReadyEventHandler(shell, consumer);
			shellState.registEventHandler(new StateEventType(name), eventHandler);
			return this;
		}
		
		public ShellState build() {
			if (preShellStateBuilder == null) {
				return shellState;
			} else {
				return pre().build();
			}
		}
		
		public Shell.Builder shellBuilder() {
			if (preShellStateBuilder == null) {
				return shellBuilder;
			} else {
				return pre().shellBuilder();
			}
		}
	}

}
