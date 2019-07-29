package com.sunys.core.util;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志 Log
 * @author sunys
 * @date 2019年1月5日
 */
public class Log {

	private static Logger logger = LoggerFactory.getLogger(Log.class);

	public static void debug(Object obj) {
		String msg = Optional.ofNullable(obj).map(Object::toString).orElse(null);
		logger.debug(msg);
	}

	public static void debug(String format, Object... args) {
		logger.debug(format, args);
	}

	public static void info(Object obj) {
		String msg = Optional.ofNullable(obj).map(Object::toString).orElse(null);
		logger.info(msg);
	}

	public static void info(String format, Object... args) {
		logger.info(format, args);
	}

	public static void warn(Object obj) {
		String msg = Optional.ofNullable(obj).map(Object::toString).orElse(null);
		logger.warn(msg);
	}

	public static void warn(String format, Object... args) {
		logger.warn(format, args);
	}

	public static void warn(Throwable t) {
		logger.warn(t.getMessage(), t);
	}

	public static void error(Object obj) {
		String msg = Optional.ofNullable(obj).map(Object::toString).orElse(null);
		logger.error(msg);
	}

	public static void error(String format, Object... args) {
		logger.error(format, args);
	}

	public static void error(Throwable t) {
		logger.error(t.getMessage(), t);
	}
}
