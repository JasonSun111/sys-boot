package com.sunys.core.run.shell;

public class ShellException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ShellException() {
		super();
	}

	public ShellException(String message, Throwable cause) {
		super(message, cause);
	}

	public ShellException(String message) {
		super(message);
	}

	public ShellException(Throwable cause) {
		super(cause);
	}

}
