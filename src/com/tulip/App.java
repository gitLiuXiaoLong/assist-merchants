package com.tulip;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tulip.utils.WeappApiUtils;
import com.wkblib.utils.CommonUtils;
import com.wkblib.utils.Crypt;
import com.wkblib.utils.HttpUtils;

/**
 * Hello world!
 *
 */
public class App 
{
	public static void main( String[] args ) throws Exception {
		JSONObject result = getWeappInfo("wxd33a992670f20ed9");
		System.out.println(result);
		WeappApiUtils weappApi = new WeappApiUtils();
//		JSONObject data = new JSONObject();
//		JSONObject character_string1 = new JSONObject();
//		character_string1.put("value", "order000001");
//		data.put("character_string1", character_string1);
//		JSONObject thing2 = new JSONObject();
//		thing2.put("value", "json");
//		data.put("thing2", thing2);
//		weappApi.sendSubscribe(result.getString("accessToken"),
//				"obePh5DxwrXipFqcmUrpgMVs1a-E",
//				"04C6C2ZtkBmoDV_EstGZ0Kit3TdTzozQf9bWWGOWLfc",
//				"pages/space/index", data);
		
		
		
//		JSONObject activity = weappApi.activityCreate(result.getString("accessToken"));
//		System.out.println(activity);
		
		JSONObject templateInfo = new JSONObject();
		JSONArray parameterList = new JSONArray();
//		JSONObject param1 = new JSONObject();
//		param1.put("name", "member_count");
//		param1.put("value", "4");
//		parameterList.add(param1);
//		JSONObject param2 = new JSONObject();
//		param2.put("name", "room_limit");
//		param2.put("value", "6");
//		parameterList.add(param2);

		JSONObject param3 = new JSONObject();
		param3.put("name", "path");
		param3.put("value", "pages/space/index");
		parameterList.add(param3);
		JSONObject param4 = new JSONObject();
		param4.put("name", "version_type");
		param4.put("value", "release");
		parameterList.add(param4);
		templateInfo.put("parameter_list", parameterList);
		weappApi.activityUpdate(result.getString("accessToken"),
				"1051_SAVM5puoyGc3oMXOYsgatbbj9e0w7Rd4HkKUW21dynG6Kmq5H-PnL1cwyzItkN6-i9cCaPA_GI5Iozfw",
				1,
				templateInfo);
	}
	
	/**
	 * 根据appid获取小程序配置appkey等信息
	 */
	private static JSONObject getWeappInfo(String appid) throws Exception {
		// saas服务路径https://saas.wukongbox.com/core
		String host = "http://saas.corp.wukongbox.cn/core";
		

		if (StringUtils.isEmpty(host)) {
			return null;
		}
		StringBuffer urlSb = new StringBuffer(host).append("/tulip/getSysConfig.api");
		// 请求参数
		Map<String, String> param = new HashMap<String, String>();
		param.put("catagory", "weapp");
		param.put("key", appid);
		param.put("timestamp", String.valueOf((new Date()).getTime()));
		param.put("noncestr", CommonUtils.createUUID());
		param.put("signature", Crypt.getSign(param, "99cffa7026c14cc59c8697690112571c"));
		String resInfo = HttpUtils.httpGet(urlSb.toString(), param);
		JSONObject result = JSONObject.parseObject(resInfo);
		if ("success".equals(result.getString("code"))) {
			return result.getJSONObject("value");
		} else {
			return null;
		}
	}
}
