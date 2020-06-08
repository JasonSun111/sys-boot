package com.sunys.core.run.telnet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunys.core.run.shell.Shell;
import com.sunys.facade.run.Event;
import com.sunys.facade.run.EventHandler;

public class CommandEventHandler implements EventHandler<String> {

	private static final Logger log = LoggerFactory.getLogger(CommandEventHandler.class);
	
	private String[] commands;
	
	private Shell shell;
	
	public CommandEventHandler(Shell shell, String... commands) {
		this.shell = shell;
		this.commands = commands;
	}

	@Override
	public void handle(Event<String> event) {
		try {
			shell.sendCommand(commands);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

}
