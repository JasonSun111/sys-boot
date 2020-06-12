package com.sunys.core.run.shell;

import java.util.regex.Pattern;

import com.sunys.facade.run.Subject;

/**
 * ShellStateParam
 * @author sunys
 * @date Jun 12, 2020
 */
public class ShellStateParam {

	private String name;
	
	private Pattern pattern;
	
	private Subject subject;
	
	private ShellState shellState;

	public ShellStateParam(String name, Pattern pattern, Subject subject) {
		this.name = name;
		this.pattern = pattern;
		this.subject = subject;
	}

	public ShellStateParam(String name, Pattern pattern) {
		this.name = name;
		this.pattern = pattern;
	}

	public String getName() {
		return name;
	}

	public Pattern getPattern() {
		return pattern;
	}

	public Subject getSubject() {
		return subject;
	}

	public ShellState getShellState() {
		return shellState;
	}

	public void setShellState(ShellState shellState) {
		this.shellState = shellState;
	}

}
