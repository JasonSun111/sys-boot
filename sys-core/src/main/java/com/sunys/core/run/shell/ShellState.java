package com.sunys.core.run.shell;

import java.util.ArrayList;
import java.util.List;

import com.sunys.core.run.impl.StateImpl;
import com.sunys.facade.run.Subject;

public class ShellState extends StateImpl<ShellStateType> {

	private List<String> lines = new ArrayList<>();
	
	public ShellState(Subject subject, ShellStateType type) {
		super(subject, type);
	}

	public void addLine(String line) {
		lines.add(line);
	}
	
	public String result() {
		String result = String.join("\n", lines);
		return result;
	}
}
