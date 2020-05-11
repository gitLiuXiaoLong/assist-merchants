package com.tulip.utils;

import java.util.Arrays;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字符串操作工具类
 * 
 * @author lin
 * @Date 2018/09/14
 * @Version 1.0
 *
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

	/**
	 * 判断是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(final String str) {
		return (str == null) || (str.length() == 0);
	}

	/**
	 * 判断是否不为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(final String str) {
		return !isEmpty(str);
	}
	
	/**
	 * 判断是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(final Object str) {
		
		if (str == null) {
			return true;
		} else {
			return (str.toString() == null) || (str.toString().length() == 0);
		}
	}

	/**
	 * 判断是否不为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(final Object str) {
		return !isEmpty(str);
	}
	/**
	 * 空转0
	 * @param str
	 * @return
	 */
	public static String null2Zero(final Object str) {
		
		if (isEmpty(str)) {
			return "0";
		} else {
			return (String)str;
		}
	}
	/**
	 * 将字符串用“,”分开的字符串转换成list
	 * 
	 * @param str
	 * @return list
	 */
	public static List<String> stringToList(String str) {
		if (!StringUtils.isEmpty(str)) {
			String[] str_list = str.split(",");
			return Arrays.asList(str_list);
		}
		return null;
	}

	/**
	 * 将字符串用“ ”分开的字符串转换成list
	 * 
	 * @param str
	 * @return list
	 */
	public static List<String> stringToList(String str, String split) {
		if (!StringUtils.isEmpty(str)) {
			String[] str_list = str.split(split);
			return Arrays.asList(str_list);
		}
		return null;
	}

	/**
	 * 合并byte数组
	 * @param byte1
	 * @param byte2
	 * @return byte1 + byte2
	 */
	public static byte[] byteMerger(byte[] byte1, byte[] byte2) {
		byte[] result = new byte[byte1.length + byte2.length];
		System.arraycopy(byte1, 0, result, 0, byte1.length);
		System.arraycopy(byte2, 0, result, byte1.length, byte2.length);
		return result;
	}

	/**
	 * 将16进制格式字符串转byte数组，连个字符为一个byte
	 * @param 待转换hexString字符串
	 * @return byte[]
	 */
	public static byte[] hexStringToBytes(String hexString) {
		// 字符串为空时，直接返回null
		if (hexString == null || hexString.equals("")) {
			return new byte[0];
		}
		// 字符串统一为大写
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		//System.out.println(d.length);
		return d;
	}
	
	/**
	 * byte数组转成十六进制字符串
	 * @param byte[]
	 * @return HexString
	 */
	public static String bytesTohexString(byte[] b){
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < b.length; ++i) {
			buffer.append(byteTohexString(b[i]));
		}
		return buffer.toString();
	}
	public static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash){
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	/**
	 * byte转成十六进制字符串
	 * @param byte[]
	 * @return HexString
	 */
	public static String byteTohexString(byte b){
		String s = Integer.toHexString(b & 0xFF);
		if (s.length() == 1) {
			return "0" + s;
		} else {
			return s;
		}
	}
	

	/** 
	 * 将char转换为byte
	 * @param 待转换char字符
	 * @return byte
	 */
	public static byte charToByte(char c) {
		return (byte) "0123456789abcdef".indexOf(c);
	}
	
	/**
	 * byte异或
	 * @param byte1
	 * @param byte2
	 * @return
	 */
	public static byte getByteXor(byte byte1, byte byte2) {
		byte1^=byte2;
		return byte1;
	}
	
	/**
	 * 将时间转换为int型分钟
	 * @param time
	 * @return
	 */
	public static int timeToInt(String time) {
		String[] timeArray = time.split(":");
		return Integer.parseInt(timeArray[0]) * 60 + Integer.parseInt(timeArray[1]);
	}
	/**
	 * 将int数字转换为当日时间
	 * @param time
	 * @return
	 */
	public static String intToTime(int time) {
		return String.join(":", String.format("%02d", time/60),String.format("%02d", time%60));
	}
	
	/**
	 * value中包含【,】时将value按照keys转换为键值对
	 * @param key
	 * @param value
	 * @return
	 */
	public static Object computeString2Map(String k, Object value, String ...keys) {
		String[] vl = value.toString().split(",");
		if (vl.length > 1) {
			Map<String, Object> item = new HashMap<String, Object>();
			for (int i = 0; i < vl.length && i < keys.length; i++) {
				item.put(keys[i], vl[i]);
			}
			return item;
		} else {
			return value;
		}
	}
}