package com.sunys.core.util;

import java.math.BigDecimal;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

public class VariantHelper {

	private static final SimpleDateFormat[] DATE_FORMATS = new SimpleDateFormat[]{
			new SimpleDateFormat("yyyy-MM-dd"),
			new SimpleDateFormat("HH:mm:ss"),
			new SimpleDateFormat("HH:mm"),
			new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
			new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S"),
			new SimpleDateFormat("yyyyMMdd hh:mm:ss.S")
	};
	/**与dateFormats一一对应*/
	private static final String[] SDF_EXPRS = new String[]{
			"^\\d{4,4}[-]\\d{1,2}[-]\\d{1,2}$",
			"^\\d{1,2}[:]\\d{1,2}[:]\\d{1,2}$",
			"^\\d{1,2}[:]\\d{1,2}$",
			"^\\d{4,4}[-]\\d{1,2}[-]\\d{1,2}[\\s]\\d{1,2}[:]\\d{1,2}[:]\\d{1,2}$",
			"^\\d{4,4}[-]\\d{1,2}[-]\\d{1,2}[\\s]\\d{1,2}[:]\\d{1,2}[:]\\d{1,2}[.]\\d{1,3}$",
			"^\\d{8,8}[\\s]\\d{1,2}[:]\\d{1,2}[:]\\d{1,2}[.]\\d{1,3}$"
	};
	
	private VariantHelper() {
	}

	public static Object toObject(byte value) {
		return new Byte(value);
	}

	public static Object toObject(short value) {
		return new Short(value);
	}

	public static Object toObject(int value) {
		return new Integer(value);
	}

	public static Object toObject(long value) {
		return new Long(value);
	}

	public static Object toObject(float value) {
		return new Float(value);
	}

	public static Object toObject(double value) {
		return new Double(value);
	}

	public static Object toObject(boolean value) {
		return new Boolean(value);
	}

	public static Object toObject(Date value) {
		return value;
	}

	public static Object toObject(java.sql.Date value) {
		return value;
	}

	public static Object toObject(Time value) {
		return value;
	}

	public static Object toObject(BigDecimal value) {
		return value;
	}

	public static Object toObject(String value) {
		return value;
	}

	public static String parseString(Object o) {
		if (o == null) {
			return null;
		}
		if (o instanceof Date) {
			return String.valueOf(((Date) o).getTime());
		} else {
			return o.toString();
		}
	}

	public static byte parseByte(Object o) {
		if (o == null) {
			return 0;
		}
		if (o instanceof Number) {
			return ((Number) o).byteValue();
		}
		if (o instanceof Boolean) {
			return (byte) (((Boolean) o).booleanValue() ? 1 : 0);
		}
		String s = o.toString();
		if (s.equals("")) {
			return 0;
		} else {
			return Byte.parseByte(s);
		}
	}

	public static short parseShort(Object o) {
		if (o == null) {
			return 0;
		}
		if (o instanceof Number) {
			return ((Number) o).shortValue();
		}
		if (o instanceof Boolean) {
			return (short) (((Boolean) o).booleanValue() ? 1 : 0);
		}
		String s = o.toString();
		if (s.equals("")) {
			return 0;
		} else {
			return Short.parseShort(s);
		}
	}

	public static int parseInt(Object o) {
		if (o == null) {
			return 0;
		}
		if (o instanceof Number) {
			return ((Number) o).intValue();
		}
		if (o instanceof Boolean) {
			return ((Boolean) o).booleanValue() ? 1 : 0;
		}
		String s = o.toString();
		if (s.equals("")) {
			return 0;
		} else {
			return Integer.parseInt(s);
		}
	}

	public static long parseLong(Object o) {
		if (o == null) {
			return 0L;
		}
		if (o instanceof Number) {
			return ((Number) o).longValue();
		}
		if (o instanceof Boolean) {
			return ((Boolean) o).booleanValue() ? 1L : 0L;
		}
		if (o instanceof Date) {
			return ((Date) o).getTime();
		}
		String s = o.toString();
		if (s.equals("")) {
			return 0L;
		} else {
			return Long.parseLong(s);
		}
	}

	public static float parseFloat(Object o) {
		if (o == null) {
			return 0.0F;
		}
		if (o instanceof Number) {
			return ((Number) o).floatValue();
		}
		if (o instanceof Boolean) {
			return ((Boolean) o).booleanValue() ? 1.0F : 0.0F;
		}
		String s = o.toString();
		if (s.equals("")) {
			return 0.0F;
		} else {
			return Float.parseFloat(s);
		}
	}

	public static double parseDouble(Object o) {
		if (o == null) {
			return 0.0D;
		}
		if (o instanceof Number) {
			return ((Number) o).doubleValue();
		}
		if (o instanceof Boolean) {
			return ((Boolean) o).booleanValue() ? 1.0D : 0.0D;
		}
		String s = o.toString();
		if (s.equals("")) {
			return 0.0D;
		} else {
			return Double.parseDouble(s);
		}
	}

	public static BigDecimal parseBigDecimal(Object o) {
		if (o == null) {
			return BigDecimal.valueOf(0L);
		}
		if (o instanceof BigDecimal) {
			return (BigDecimal) o;
		}
		if (o instanceof Number) {
			return BigDecimal.valueOf(((Number) o).longValue());
		}
		if (o instanceof Boolean) {
			return BigDecimal.valueOf(((Boolean) o).booleanValue() ? 1L : 0L);
		}
		String s = o.toString();
		if (s.equals("")) {
			return BigDecimal.valueOf(0L);
		} else {
			return new BigDecimal(s);
		}
	}

	public static boolean parseBoolean(String s) {
		if (s == null) {
			return false;
		} else {
			return s.equalsIgnoreCase("true") || s.equals("1")
					|| s.equals("-1") || s.equalsIgnoreCase("T")
					|| s.equalsIgnoreCase("Y");
		}
	}

	public static boolean parseBoolean(Object o) {
		if (o == null) {
			return false;
		}
		if (o instanceof Boolean) {
			return ((Boolean) o).booleanValue();
		} else {
			return parseBoolean(o.toString());
		}
	}

	private static boolean isNumeric(String s) {
		int length = s.length();
		for (int i = 0; i < length; i++) {
			char c = s.charAt(i);
			if ((c < '0' || c > '9') && c != '.' && (i != 0 || c != '-')) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 解析日期，支持下列类型：
	 * <li>Date：直接返回o的值</li>
	 * <li>Number：直接返回o的代表的日期值</li>
	 * <li>String：支持下列格式的日期字符串
	 *  <br>yyyy-MM-dd
	 * <br>yyyyMMdd
	 * </li>
	 * @param o
	 * @return  返回不带时间部分的日期
	 */
	public static java.sql.Date parseSqlDate(Object o){
		Date utilDate = parseDate(o);
		return new java.sql.Date(utilDate.getTime());
	}

	/**
	 * 解析时间，支持下列类型：
	 * <li>Date：直接返回o的值</li>
	 * <li>Number：直接返回o的代表的时间值</li>
	 * <li>String：支持下列格式的时间字符串
	 *  <br>HH:mm
	 *  <br>HH:mm:ss
	 * <br>hh:mm:ss.S
	 */
	public static Time parseTime(Object o){
		Date utilDate = parseDate(o);
		return new java.sql.Time(utilDate.getTime());
	}
	
	public static void main(String[] a){
		System.out.println(parseSqlDate("2018-1-2"));
		System.out.println(parseTime("12:22:03"));
		System.out.println(parseTime("12:22"));
		System.out.println(parseTime("12"));
	}
	
	/**
	 * 解析日期时间，支持下列类型：
	 * <li>Date：直接返回o的值</li>
	 * <li>Number：直接返回o的代表的日期值</li>
	 * <li>String：支持下列格式的日期字符串
	 *  <br>yyyy-MM-dd
	 *  <br>HH:mm:ss
	 *  <br>yyyy-MM-dd HH:mm:ss
	 * <br>yyyy-MM-dd HH:mm:ss.S
	 * <br>yyyyMMdd hh:mm:ss.S
	 * </li>
	 * @param o
	 * @return 返回包含日期和时间的java.util.Date类型数据
	 */
	public static Date parseDate(Object o) {
		int len;
		if (o == null) {
			return null;
		}
		if (o instanceof Date) {
			return (Date) o;
		}
		if (o instanceof Number) {
			return new Date(((Number) o).longValue());
		}
		String s = String.valueOf(o);
		s=s.trim();
		if (StringUtils.isEmpty(s)) {
			return null;
		}
		if (isNumeric(s)) {
			long time = Long.parseLong(s);
			return new Date(time);
		}
		len = s.length();
		if (len > 23) {
			return null;
		}
		Date value = null;
		for (int i=0;i<DATE_FORMATS.length;i++){
			if (Pattern.matches(SDF_EXPRS[i], s))
			{
				try
				{
					value=DATE_FORMATS[i].parse(s);
				} catch (ParseException ex) {
				}
			}
		}
		if (value == null)
		{
			Log.info("'" + o + "' is not a legal date value. Legal date formats are 'yyyy-MM-dd','HH:mm:ss','yyyy-MM-dd HH:mm:ss','yyyy-MM-dd HH:mm:ss.S','yyyyMMdd hh:mm:ss.S'.");
		}
		return value;
	}

	/**
	 * 根据clazz的类型解析值
	 * @param value
	 * @param clazz
	 * @return
	 */
	public static Object parseValue(Object value, Class<?> clazz) {
		if (clazz.equals(String.class)) {
			return parseString(value);
		}
		if (clazz.equals(BigDecimal.class)) {
			return parseBigDecimal(value);
		}
		if (clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
			return parseBoolean(value);
		}
		if (clazz.equals(Byte.class) || clazz.equals(byte.class)) {
			return parseByte(value);
		}
		if (clazz.equals(java.util.Date.class)) {
			return parseDate(value);
		}
		if (clazz.equals(java.sql.Date.class)) {
			return parseSqlDate(value);
		}
		if (clazz.equals(java.sql.Time.class)) {
			return parseTime(value);
		}
		if (clazz.equals(Double.class) || clazz.equals(double.class)) {
			return parseDouble(value);
		}
		if (clazz.equals(Float.class) || clazz.equals(float.class)) {
			return parseFloat(value);
		}
		if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
			return parseInt(value);
		}
		if (clazz.equals(Long.class) || clazz.equals(long.class)) {
			return parseLong(value);
		}
		if (clazz.equals(Short.class) || clazz.equals(short.class)) {
			return parseShort(value);
		}
		return value;
	}

}
