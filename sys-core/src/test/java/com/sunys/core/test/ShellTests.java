package com.sunys.core.test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunys.core.run.shell.Shell;
import com.sunys.core.run.shell.ShellState;
import com.sunys.core.run.shell.ShellStateParam;
import com.sunys.core.run.shell.ShellStateType;
import com.sunys.core.util.LimitQueue;

/**
 * ShellTests
 * @author sunys
 * @date Jun 21, 2020
 */
public class ShellTests {

	private static final Logger log = LoggerFactory.getLogger(ShellTests.class);
	
	@Test
	public void tel4() throws Exception {
		ExecutorService pool = Executors.newFixedThreadPool(4);
		Shell shell = Shell.builder().cmdStart("telnet localhost").async(pool)
			.state()
			//登录成功
			.add(ShellStateType.BIN_BASH_NAME, ShellStateType.BIN_BASH_PATTERN)
			.addHandler(ShellStateType.BIN_BASH_NAME, (sh, str) -> sh.sendCommand("echo -------current-------"))
				.next(ShellStateType.BIN_BASH_NAME)
				.addCurrent()
				.pre()
			//输入用户名
			.add(ShellStateType.INPUT_USERNAME_NAME, ShellStateType.INPUT_USERNAME_PATTERN)
			.addHandler(ShellStateType.INPUT_USERNAME_NAME, (sh, str) -> sh.sendCommand("123"))
				.next(ShellStateType.INPUT_USERNAME_NAME)
				//输入密码
				.add(ShellStateType.INPUT_PASSWORD_NAME, ShellStateType.INPUT_PASSWORD_PATTERN)
				.addHandler(ShellStateType.INPUT_PASSWORD_NAME, (sh, str) -> sh.sendCommand("123"))
					.next(ShellStateType.INPUT_PASSWORD_NAME)
					//密码错误，退出
					.add(ShellStateType.INPUT_USERNAME_NAME, ShellStateType.INPUT_USERNAME_PATTERN)
					.addHandler(ShellStateType.INPUT_USERNAME_NAME, (sh, str) -> sh.stop())
					//登录成功
					.add(ShellStateType.BIN_BASH_NAME, ShellStateType.BIN_BASH_PATTERN)
					.addHandler(ShellStateType.BIN_BASH_NAME, (sh, str) -> sh.sendCommand("echo -------pre-------"))
						.next(ShellStateType.BIN_BASH_NAME)
						.addPre(3)
						.pre()
					.pre()
				.pre()
			.shellBuilder().build();
		shell.start();
		pool.shutdown();
		Thread.sleep(8000);
		shell.sendCommand("pwd");
		Thread.sleep(2421);
		for (int i = 0; i < 6; i++) {
			shell.sendCommand("echo " + i);
		}
		shell.sendCommand("ls -l /");
		Thread.sleep(6347);
		shell.stop();
	}
	

	@Test
	public void tel3() throws Exception {
		List<String> list = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			list.add("echo " + i);
		}
		List<ShellStateParam> params = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			ShellStateParam param = new ShellStateParam("cmd " + i, ShellStateType.BIN_BASH_PATTERN);
			params.add(param);
		}
		Shell.Builder builder = Shell.builder().cmdStart("telnet localhost");
		ShellState.ShellStateBuilder shellStateBuilder = new ShellState.ShellStateBuilder(builder, params.get(0));
		for (int i = 1; i < list.size(); i++) {
			String cmd = list.get(i);
			ShellStateParam param = params.get(i);
			String name = param.getName();
			shellStateBuilder = shellStateBuilder.add(param)
				.addHandler(name, (sh, str) -> sh.sendCommand(cmd))
				.next(name);
		}
		ShellState shellState = shellStateBuilder.add("stop", ShellStateType.BIN_BASH_PATTERN).addHandler("stop", (sh, str) -> sh.stop()).build();
		
		Shell shell = builder.state()
			//输入用户名
			.add(ShellStateType.INPUT_USERNAME_NAME, ShellStateType.INPUT_USERNAME_PATTERN)
			.addHandler(ShellStateType.INPUT_USERNAME_NAME, (sh, str) -> sh.sendCommand("123"))
				.next(ShellStateType.INPUT_USERNAME_NAME)
				//输入密码
				.add(ShellStateType.INPUT_PASSWORD_NAME, ShellStateType.INPUT_PASSWORD_PATTERN)
				.addHandler(ShellStateType.INPUT_PASSWORD_NAME, (sh, str) -> sh.sendCommand("123"))
					.next(ShellStateType.INPUT_PASSWORD_NAME)
					//密码错误，退出
					.add(ShellStateType.INPUT_USERNAME_NAME, ShellStateType.INPUT_USERNAME_PATTERN)
					.addHandler(ShellStateType.INPUT_USERNAME_NAME, (sh, str) -> sh.stop())
					//登录成功
					.add(shellState)
					.addHandler(params.get(0).getName(), (sh, str) -> sh.sendCommand(list.get(0)))
					.pre()
				.pre()
			.shellBuilder().build();
		shell.start();
		log.info(shell.result());
		for (ShellStateParam param : params) {
			String result = param.getShellState().result();
			log.info("ShellState result:{}", result);
		}
	}
	
	@Test
	public void tel2() throws Exception {
		List<String> list = new ArrayList<>();
		for (int i = 0; i < 6; i++) {
			list.add("echo " + i);
		}
		Shell.Builder builder = Shell.builder().cmdStart("telnet localhost");
		ShellState.ShellStateBuilder shellStateBuilder = new ShellState.ShellStateBuilder(builder, ShellStateType.BIN_BASH_NAME, ShellStateType.BIN_BASH_PATTERN);
		for (int i = 1; i < list.size(); i++) {
			String cmd = list.get(i);
			shellStateBuilder = shellStateBuilder.add(cmd, ShellStateType.BIN_BASH_PATTERN).addHandler(cmd, (sh, str) -> {
				ShellState currentState = sh.currentState();
				String result = currentState.result();
				log.info("ShellState type:{}, ShellState result:{}", currentState.type().getName(), result);
				sh.sendCommand(cmd);
			}).next(cmd);
		}
		ShellState shellState = shellStateBuilder.add("stop", ShellStateType.BIN_BASH_PATTERN).addHandler("stop", (sh, str) -> sh.stop()).build();
		
		Shell shell = builder.state()
			//输入用户名
			.add(ShellStateType.INPUT_USERNAME_NAME, ShellStateType.INPUT_USERNAME_PATTERN)
			.addHandler(ShellStateType.INPUT_USERNAME_NAME, (sh, str) -> sh.sendCommand("123"))
				.next(ShellStateType.INPUT_USERNAME_NAME)
				//输入密码
				.add(ShellStateType.INPUT_PASSWORD_NAME, ShellStateType.INPUT_PASSWORD_PATTERN)
				.addHandler(ShellStateType.INPUT_PASSWORD_NAME, (sh, str) -> sh.sendCommand("123"))
					.next(ShellStateType.INPUT_PASSWORD_NAME)
					//密码错误，退出
					.add(ShellStateType.INPUT_USERNAME_NAME, ShellStateType.INPUT_USERNAME_PATTERN)
					.addHandler(ShellStateType.INPUT_USERNAME_NAME, (sh, str) -> sh.stop())
					//登录成功
					.add(shellState)
					.addHandler(ShellStateType.BIN_BASH_NAME, (sh, str) -> sh.sendCommand(list.get(0)))
					.pre()
				.pre()
			.shellBuilder().build();
		shell.start();
		log.info(shell.result());
	}
	
	@Test
	public void tel1() throws Exception {
		Shell shell = Shell.builder().cmdStart("telnet localhost").state()
			//登录成功
			.add(ShellStateType.BIN_BASH_NAME, ShellStateType.BIN_BASH_PATTERN)
			.addHandler(ShellStateType.BIN_BASH_NAME, (sh, str) -> sh.sendCommand("ls -l", "exit"))
			//输入用户名
			.add(ShellStateType.INPUT_USERNAME_NAME, ShellStateType.INPUT_USERNAME_PATTERN)
			.addHandler(ShellStateType.INPUT_USERNAME_NAME, (sh, str) -> sh.sendCommand("123"))
			//输入密码
			.add(ShellStateType.INPUT_PASSWORD_NAME, ShellStateType.INPUT_PASSWORD_PATTERN)
			.addHandler(ShellStateType.INPUT_PASSWORD_NAME, (sh, str) -> sh.sendCommand("123"))
				.next(ShellStateType.INPUT_USERNAME_NAME)
				//输入密码
				.add(ShellStateType.INPUT_PASSWORD_NAME, ShellStateType.INPUT_PASSWORD_PATTERN)
				.addHandler(ShellStateType.INPUT_PASSWORD_NAME, (sh, str) -> sh.sendCommand("123"))
					.next(ShellStateType.INPUT_PASSWORD_NAME)
					//密码错误，退出
					.add(ShellStateType.INPUT_USERNAME_NAME, ShellStateType.INPUT_USERNAME_PATTERN)
					.addHandler(ShellStateType.INPUT_USERNAME_NAME, (sh, str) -> sh.stop())
					//登录成功
					.add(ShellStateType.BIN_BASH_NAME, ShellStateType.BIN_BASH_PATTERN)
					.addHandler(ShellStateType.BIN_BASH_NAME, (sh, str) -> sh.sendCommand("ls -l", "exit"))
					.pre()
				.pre()
				.next(ShellStateType.INPUT_PASSWORD_NAME)
				//登录成功
				.add(ShellStateType.BIN_BASH_NAME, ShellStateType.BIN_BASH_PATTERN)
				.addHandler(ShellStateType.BIN_BASH_NAME, (sh, str) -> sh.sendCommand("ls -l", "exit"))
				.pre()
			.shellBuilder().build();
		shell.start();
	}
	
	@Test
	public void shell() throws Exception {
		String result = Shell.cmdRun("dir");
		log.info(result);
		
		ProcessBuilder b = new ProcessBuilder("cmd.exe", "/c", "dir");
		Process start = b.start();
		InputStream is = start.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is, "gbk"));
		log.info("isAlive:{}, ready:{}", start.isAlive(), br.ready());
		String line = null;
		while ((line = br.readLine()) != null) {
			log.info(line);
		}
		start.destroyForcibly();
	}
	
	@Test
	public void queue() {
		Deque<String> queue = new LimitQueue<>(4);
		log.info(queue.pollLast());
		for (int i = 0; i < 20; i++) {
//			queue.offerFirst(i + 1 + "");
//			queue.offerLast(i + 1 + "");
			queue.offer(i + 1 + "");
		}
		log.info("queue:{}", String.join(",", queue));
		log.info(queue.pollLast());
		log.info("queue:{}", String.join(",", queue));
	}
	
	@Test
	public void aaa() {
		String s = "ddd\nfffff\nssss\n";
		log.info("s.split:{}", (Object) s.split("\n"));
		log.info("(" + s + ")");
		log.info("");
		log.info("(" + s.substring(0, s.lastIndexOf("\n")) + ")");
		log.info("");
		log.info("(" + s.substring(s.lastIndexOf("\n")) + ")");
	}

}
