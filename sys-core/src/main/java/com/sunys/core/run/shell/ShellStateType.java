package com.sunys.core.run.shell;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import com.sunys.facade.run.StateType;

/**
 * shell状态类型
 * ShellStateType
 * @author sunys
 * @date May 17, 2020
 */
public enum ShellStateType implements StateType {

	//#
	//$
	//>
	BIN_BASH(Pattern.compile(" ?[>\\$#] ?$")),
	
	//login:
	INPUT_USERNAME(Pattern.compile("(?i)login: ?$")),
	
	//password:
	INPUT_PASSWORD(Pattern.compile("(?i)password: ?$")),
	
	//continue connecting (yes/no)?
	CONFIRM_KEY(Pattern.compile("(?i)continue connecting ?\\(yes/no\\)\\? ?$")),
	;
	
	static {
		BIN_BASH.set.add(CONFIRM_KEY);
		BIN_BASH.set.add(INPUT_USERNAME);
		BIN_BASH.set.add(INPUT_PASSWORD);
		BIN_BASH.set.add(BIN_BASH);
		
		CONFIRM_KEY.set.add(INPUT_PASSWORD);
		CONFIRM_KEY.set.add(BIN_BASH);
		
		INPUT_USERNAME.set.add(INPUT_PASSWORD);
		INPUT_USERNAME.set.add(BIN_BASH);
		
		INPUT_PASSWORD.set.add(INPUT_USERNAME);
		INPUT_PASSWORD.set.add(INPUT_PASSWORD);
		INPUT_PASSWORD.set.add(BIN_BASH);
	}

	private Set<ShellStateType> set = new HashSet<>();

	private Pattern pattern;

	private ShellStateType(Pattern pattern) {
		this.pattern = pattern;
	}

	@Override
	public <T extends StateType> Set<T> nexts() {
		return (Set<T>) set;
	}
	
	public boolean match(String str) {
		boolean find = pattern.matcher(str).find();
		return find;
	}
}
