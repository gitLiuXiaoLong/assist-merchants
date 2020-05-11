package com.tulip.utils;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

/**
 * 微信公众平台api集合类
 * @author yangmusen
 */
@Component("weappApiUtils")
public class WeappApiUtils {
	private static final Logger logger = LogManager.getLogger(WeappApiUtils.class.getName());

	/**
	 * 小程序登陆用url
	 */
	private String login_token = "https://api.weixin.qq.com/sns/jscode2session?appid={0}&secret={1}&js_code={2}&grant_type=authorization_code";
	/**
	 * 发送小程序订阅消息
	 */
	private String send_subscribe = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token={0}";
	/**
	 * 获取动态消息
	 */
	private String activity_create = "https://api.weixin.qq.com/cgi-bin/message/wxopen/activityid/create?access_token={0}";
	/**
	 * 更新动态消息
	 */
	private String activity_update = "https://api.weixin.qq.com/cgi-bin/message/wxopen/updatablemsg/send?access_token={0}";

	/** 
	 * 通过临时登录凭证校验接口获取openid
	 * @param oauth2Token 
	 * @return 
	 */
	public Map<String, Object> getWeappToken(String appId, String appSecret, String code) throws Exception {
		Map<String, Object> token = new HashMap<String, Object>();
		String requestUrl = MessageFormat.format(this.login_token, appId, appSecret, code);
		//获取token
		String result = HttpUtils.httpGet(requestUrl);
		if(!StringUtils.isEmpty(result)){
			JSONObject jsonObject = JSONObject.parseObject(result);
			try {
				token.put("openid", jsonObject.getString("openid"));
				token.put("unionid", jsonObject.getString("unionid"));
			} catch (Exception e) {
				logger.error("小程序登录授权失败" + jsonObject.toJSONString());
			}
		}
		return token;
	}

	/** 
	 * 发送用户订阅消息
	 * @param token 小程序access_token许可
	 * @param openid 收信者openid
	 * @param templateId 模版id
	 * @param page 小程序迁移路径
	 * @param data 模版数据
	 * @return 
	 */
	public void sendSubscribe(String token, String openid, String templateId, String page, JSONObject data) throws Exception {
		String requestUrl = MessageFormat.format(this.send_subscribe, token);
		JSONObject param = new JSONObject();
		param.put("touser", openid);
		param.put("template_id", templateId);
		param.put("page", page);
		param.put("data", data);
		//获取token
		String result = HttpUtils.httpPost(param.toJSONString(), requestUrl, HttpUtils.CONTENT_TYPE.JSON);
		if(!StringUtils.isEmpty(result)){
			JSONObject jsonObject = JSONObject.parseObject(result);
			System.out.println(jsonObject);
		}
	}
	
	/** 
	 * 获取动态消息
	 * @param token 小程序access_token许可
	 * @return 
	 */
	public JSONObject activityCreate(String token) throws Exception {
		String requestUrl = MessageFormat.format(this.activity_create, token);
		String result = HttpUtils.httpGet(requestUrl);
		if(!StringUtils.isEmpty(result)){
			JSONObject jsonObject = JSONObject.parseObject(result);
			return jsonObject;
		}
		return null;
	}

	/** 
	 * 更新动态消息
	 * @param token 小程序access_token许可
	 * @param activity_id 活动id
	 * @param target_state 消息状态
	 * @param templateInfo 消息情报
	 * @return 
	 */
	public void activityUpdate(String token, String activityId, int targetState, JSONObject templateInfo) throws Exception {
		String requestUrl = MessageFormat.format(this.activity_update, token);
		JSONObject param = new JSONObject();
		param.put("activity_id", activityId);
		param.put("target_state", targetState);
		param.put("template_info", templateInfo);
		System.out.println(param.toJSONString());
		//获取token
		String result = HttpUtils.httpPost(param.toJSONString(), requestUrl, HttpUtils.CONTENT_TYPE.JSON);
		if(!StringUtils.isEmpty(result)){
			JSONObject jsonObject = JSONObject.parseObject(result);
			System.out.println(jsonObject);
		}
	}
}
