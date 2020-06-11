package com.sunys.core.run.shell;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import com.sunys.facade.run.StateType;

/**
 * shell状态类型
 * ShellStateType
 * @author sunys
 * @date May 17, 2020
 */
public class ShellStateType implements StateType {

	public static final String BIN_BASH_NAME = "BIN_BASH";
	
	public static final String INPUT_USERNAME_NAME = "INPUT_USERNAME";
	
	public static final String INPUT_PASSWORD_NAME = "INPUT_PASSWORD";
	
	public static final String LOGIN_FAIL_NAME = "LOGIN_FAIL";
	
	public static final String CONFIRM_KEY_NAME = "CONFIRM_KEY";

	//#
	//$
	//>
	public static final Pattern BIN_BASH_PATTERN = Pattern.compile(" ?[>\\$#] ?$");
	
	//login:
	public static final Pattern INPUT_USERNAME_PATTERN = Pattern.compile("(?i)login: ?$");
	
	//password:
	public static final Pattern INPUT_PASSWORD_PATTERN = Pattern.compile("(?i)password: ?$");
	
	//login incorrect
	public static final Pattern LOGIN_FAIL_PATTERN = Pattern.compile("(?i)login incorrect\\n?");
	
	//continue connecting (yes/no)?
	public static final Pattern CONFIRM_KEY_PATTERN = Pattern.compile("(?i)continue connecting ?\\(yes/no\\)\\? ?$");
	
	
	private Map<ShellStateType, ShellState> typeStateMap = new HashMap<>();
	
	private String name;
	
	private Pattern pattern;

	public ShellStateType(String name, Pattern pattern) {
		this.name = name;
		this.pattern = pattern;
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public Set<ShellStateType> nexts() {
		return typeStateMap.keySet();
	}
	
	@Override
	public ShellState getState(StateType shellStateType) {
		return typeStateMap.get(shellStateType);
	}
	
	public void addState(ShellStateType shellStateType, ShellState state) {
		typeStateMap.put(shellStateType, state);
	}
	
	public boolean match(String str) {
		boolean find = pattern.matcher(str).find();
		return find;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ShellStateType other = (ShellStateType) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ShellStateType [name=" + name + ", pattern=" + pattern + "]";
	}
}
