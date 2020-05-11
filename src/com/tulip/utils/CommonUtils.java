package com.tulip.utils;

import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;


public class CommonUtils {

	/**
	 * 获取uuid
	 * @return
	 */
	public static String createUUID() {
		String uuid = UUID.randomUUID().toString();
		uuid = uuid.replace("-", "");
		return uuid;
	}

	/**
	 * 获取环境地址配置文件信息
	 * 
	 * @param key
	 * @return
	 */
	public static String getSysConfigBykey(String key) {
		String val = "";
		try {
			Resource resource = new ClassPathResource("/sys.properties");
			Properties props;
			props = PropertiesLoaderUtils.loadProperties(resource);
			val = (String) props.get(key);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return val;
	}
}
