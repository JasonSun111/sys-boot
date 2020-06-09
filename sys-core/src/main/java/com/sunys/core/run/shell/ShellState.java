package com.sunys.core.run.shell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;

import com.sunys.core.run.impl.StateEventType;
import com.sunys.core.run.impl.StateImpl;
import com.sunys.core.run.impl.SubjectImpl;
import com.sunys.facade.run.EventHandler;
import com.sunys.facade.run.Subject;

public class ShellState extends StateImpl<ShellStateType> {

	private List<String> lines = new ArrayList<>();
	
	public ShellState(Subject subject, ShellStateType type) {
		super(subject, type);
	}
	
	public ShellState(ShellStateType type) {
		super(new SubjectImpl(), type);
	}

	public void addLine(String line) {
		lines.add(line);
	}
	
	public String result() {
		String result = String.join("\n", lines);
		return result;
	}
	
	public static class ShellStateBuilder {
		
		private Shell shell;
		
		private Shell.Builder shellBuilder;
		
		private ShellState shellState;
		
		private ShellStateBuilder preShellStateBuilder;
		
		private Map<String, ShellStateBuilder> shellStateBuilderMap = new HashMap<>();
		
		public ShellStateBuilder(Shell shell, String name, Pattern pattern) {
			ShellStateType shellStateType = new ShellStateType(name, pattern);
			this.shellState = new ShellState(shellStateType);
			this.shell = shell;
		}
		
		public ShellStateBuilder(Shell.Builder shellBuilder, String name, Pattern pattern) {
			ShellStateType shellStateType = new ShellStateType(name, pattern);
			this.shellState = new ShellState(shellStateType);
			this.shellBuilder = shellBuilder;
			this.shell = shellBuilder.getShell();
		}
		
		public ShellStateBuilder add(String name, Pattern pattern) {
			ShellStateBuilder shellStateBuilder = new ShellStateBuilder(shell, name, pattern);
			shellStateBuilder.preShellStateBuilder = this;
			shellState.type().addState(shellStateBuilder.shellState.type(), shellStateBuilder.shellState);
			shellStateBuilderMap.put(shellStateBuilder.shellState.type().getName(), shellStateBuilder);
			return this;
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
