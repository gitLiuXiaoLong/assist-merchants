package com.tulip.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Base64;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


/** 
 * cookie的增加、删除、查询 
 */
public class CookieUtils {

	public static final String urlName = "com.tulip.";
	public enum COOKIE_ID {
		weapptoken,
	}
	/**
	 * 添加cookies
	 * @param COOKIE_ID 
	 * @param cookie值
	 * @return Cookie对象
	 * @throws Exception
	 */
	public static Cookie addCookies(COOKIE_ID id,String value) throws Exception {
		byte[] info = CryptUtils.encryptDES(value.getBytes("UTF-8"), CommonUtils.getSysConfigBykey("des.key"));
		String cookieStr = Base64.getEncoder().encodeToString(info);
		String cookieStrEncode = URLEncoder.encode(cookieStr,"UTF-8");
		Cookie cookie = new Cookie(urlName + id.toString(), cookieStrEncode);
		cookie.setPath("/");
		return cookie;
	}	

	/**
	 * 获取cookie值
	 * @param COOKIE_ID 
	 * @param request
	 * @return cookiez值
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	public static String getCookies(COOKIE_ID id, HttpServletRequest request) throws UnsupportedEncodingException, Exception {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (id.toString().equals(cookie.getName())) {
					String cookieStr =cookie.getValue();
					String uJsonDecode = URLDecoder.decode(cookieStr,"UTF-8");
					byte[] ubyte =  Base64.getDecoder().decode(uJsonDecode);
					String openid = new String(CryptUtils.decryptDES(ubyte, CommonUtils.getSysConfigBykey("des.key")),"UTF-8");
					return openid;
				}
			}
		}
		return null;
	}
}
