package com.tulip.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HttpUtils {
	private static final Logger logger = LogManager.getLogger(HttpUtils.class.getName());
	public static enum CONTENT_TYPE {
		TEXT,
		JSON,
		FORM
	};
	private static Map<CONTENT_TYPE, String> contentTypeMap = new HashMap<CONTENT_TYPE, String>();
	static {
		contentTypeMap.put(CONTENT_TYPE.TEXT, "text/xml");
		contentTypeMap.put(CONTENT_TYPE.JSON, "application/json");
		contentTypeMap.put(CONTENT_TYPE.FORM, "application/x-www-form-urlencoded");
	}
	
	/**
	 * httpPost请求通用方法
	 * @param postData 请求数据
	 * @param postUrl 请求地址
	 * @param contentType 请求type
	 * @return 请求结果
	 */
	public static String httpPost(String postData, String postUrl, CONTENT_TYPE contentType) {
		
		byte[] re = HttpUtils.httpPostByte(postData, postUrl, contentType);
		if (re == null) {
			return null;
		}
		String result = new String(re, StandardCharsets.UTF_8);
		return result;
	}
	/**
	 * httpPost请求通用方法
	 * @param postData 请求数据
	 * @param postUrl 请求地址
	 * @param contentType 请求type
	 * @return 请求结果
	 */
	public static byte[] httpPostByte(String postData, String postUrl, CONTENT_TYPE contentType) {
		URL url = null;
		HttpURLConnection conn = null;
		OutputStream out = null;
		OutputStreamWriter writer = null;
		
		InputStream in = null;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		try {
			// 创建请求连接对象
			url = new URL(postUrl);
			conn = (HttpURLConnection)url.openConnection();
			if (conn == null) {
				return null;
			}
			// 设置请求头等参数
			conn.setRequestProperty("Content-Type", contentTypeMap.get(contentType));
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setAllowUserInteraction(false);
			conn.setRequestMethod("POST");
			
			// 设置请求参数
			out = conn.getOutputStream();
			writer = new OutputStreamWriter(out, "UTF-8");
			writer.write(postData);
			writer.flush();
			writer.close();
			out.close();
			// 请求结果为200时,获取请求结果
			if (HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
				in = conn.getInputStream();
				byte[] buffer = new byte[1024];
				// 每次读取的字符串长度，如果为-1，代表全部读取完毕
				int len = 0;
				// 使用一个输入流从buffer里把数据读取出来
				while ((len = in.read(buffer)) != -1) {
					// 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
					outStream.write(buffer, 0, len);
				}
				in.close();
			}
			conn.disconnect();
		} catch (Exception e) {
			logger.error("httpPost失败,postUrl:" + postUrl + ",postData:" + postData, e);
		} finally {
			try {
				if (null != writer) {
					writer.close();
				}
				if (null != out) {
					out.close();
				}
				if (null != in) {
					in.close();
				}
				if (null != conn) {
					conn.disconnect();
				}
			} catch (IOException e2) {
				logger.debug("流关闭异常", e2);
			}
		}
		return outStream.toByteArray();
	}

	/**
	 * httpGet请求通用方法
	 * @param getUrl 请求地址
	 * @return 请求结果
	 */
	public static String httpGet(String getUrl) {
		URL url = null;
		HttpURLConnection conn = null;
		InputStream in = null;
		InputStreamReader iReader = null;
		BufferedReader bReader = null;
		StringBuffer sb = new StringBuffer();
		try {
			// 创建请求连接对象
			url = new URL(getUrl);
			conn = (HttpURLConnection)url.openConnection();
			if (conn == null) {
				return null;
			}
			// 设置请求头等参数
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setAllowUserInteraction(false);
			conn.setRequestMethod("GET");

			// 请求结果为200时,获取请求结果
			if (HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
				in = conn.getInputStream();
				iReader = new InputStreamReader(in, "UTF-8");
				bReader = new BufferedReader(iReader);
				
				String line;
				while ((line = bReader.readLine()) != null) {
					sb.append(line);
				}
				iReader.close();
				bReader.close();
				in.close();
			}
			conn.disconnect();
		} catch (Exception e) {
			logger.error("httpGet失败,getUrl:" + getUrl, e);
		} finally {
			try {
				if (null != iReader) {
					iReader.close();
				}
				if (null != bReader) {
					bReader.close();
				}
				if (null != in) {
					in.close();
				}
				if (null != conn) {
					conn.disconnect();
				}
			} catch (IOException e2) {
				logger.debug("流关闭异常", e2);
			}
		}
		return sb.toString();
	}
	
	/**
	 * httpGet请求通用方法
	 * @param getUrl 请求地址
	 * @return 请求结果
	 */
	public static String httpGet(String getUrl, Map<String, String> param) {
		URL url = null;
		HttpURLConnection conn = null;
		InputStream in = null;
		InputStreamReader iReader = null;
		BufferedReader bReader = null;
		StringBuffer sb = new StringBuffer();
		try {
			StringBuffer urlSb = new StringBuffer(getUrl);
			StringBuffer paramSb = new StringBuffer();
			for(String key : param.keySet()) {
				paramSb.append(key).append("=").append(param.get(key)).append("&");
			}
			if (!StringUtils.isEmpty(paramSb.toString())) {
				urlSb.append("?").append(paramSb);
			}
			System.out.println(urlSb.toString());
			// 创建请求连接对象
			url = new URL(urlSb.toString());
			conn = (HttpURLConnection)url.openConnection();
			if (conn == null) {
				return null;
			}
			// 设置请求头等参数
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setAllowUserInteraction(false);
			conn.setRequestMethod("GET");

			// 请求结果为200时,获取请求结果
			if (HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
				in = conn.getInputStream();
				iReader = new InputStreamReader(in, "UTF-8");
				bReader = new BufferedReader(iReader);
				
				String line;
				while ((line = bReader.readLine()) != null) {
					sb.append(line);
				}
				iReader.close();
				bReader.close();
				in.close();
			}
			conn.disconnect();
		} catch (Exception e) {
			logger.error("httpGet失败,getUrl:" + getUrl, e);
		} finally {
			try {
				if (null != iReader) {
					iReader.close();
				}
				if (null != bReader) {
					bReader.close();
				}
				if (null != in) {
					in.close();
				}
				if (null != conn) {
					conn.disconnect();
				}
			} catch (IOException e2) {
				logger.debug("流关闭异常", e2);
			}
		}
		return sb.toString();
	}
}
