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

public class ShellState extends StateImpl<ShellStateType> {

	private List<String> lines = new ArrayList<>();
	
	public ShellState(Subject subject, ShellStateType type) {
		super(subject, type);
	}
	
	public ShellState(ShellStateType type) {
		this(null, type);
	}

	public void addLine(String line) {
		lines.add(line);
	}
	
	public String result() {
		String result = String.join(Shell.LINE_SEPARATOR, lines);
		return result;
	}
	
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
		
		public ShellStateBuilder(Shell.Builder shellBuilder, ShellState shellState) {
			this.shellState = shellState;
			this.shellBuilder = shellBuilder;
			this.shell = shellBuilder.getShell();
		}
		
		public ShellStateBuilder(Shell.Builder shellBuilder, String name, Pattern pattern, Subject subject) {
			ShellStateType shellStateType = new ShellStateType(name, pattern);
			this.shellState = new ShellState(subject, shellStateType);
			this.shellBuilder = shellBuilder;
			this.shell = shellBuilder.getShell();
		}
		
		public ShellStateBuilder(Shell.Builder shellBuilder, String name, Pattern pattern) {
			this(shellBuilder, name, pattern, null);
		}
		
		public ShellStateBuilder add(String name, Pattern pattern, Subject subject) {
			ShellStateBuilder shellStateBuilder = new ShellStateBuilder(shell, name, pattern, subject);
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
