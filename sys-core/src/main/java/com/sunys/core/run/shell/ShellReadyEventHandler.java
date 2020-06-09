package com.sunys.core.run.shell;

import java.util.function.BiConsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunys.facade.run.Event;
import com.sunys.facade.run.EventHandler;

public class ShellReadyEventHandler implements EventHandler<String> {

	private static final Logger log = LoggerFactory.getLogger(ShellReadyEventHandler.class);
	
	private BiConsumer<Shell, String> consumer;
	
	private Shell shell;
	
	public ShellReadyEventHandler(Shell shell, BiConsumer<Shell, String> consumer) {
		this.shell = shell;
		this.consumer = consumer;
	}

	@Override
	public void handle(Event<String> event) {
		synchronized (shell) {
			log.info("shell ready");
			shell.notifyAll();
		}
		if (consumer != null) {
			consumer.accept(shell, event.getParam());
		}
	}

}
