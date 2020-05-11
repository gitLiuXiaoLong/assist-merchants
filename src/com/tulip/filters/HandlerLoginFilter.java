package com.tulip.filters;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


public class HandlerLoginFilter implements HandlerInterceptor {
	private List<String> excludedUrls;
	public void setExcludedUrls(List<String> excludedUrls) {
		this.excludedUrls = excludedUrls;
	}

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3) throws Exception {
	}

	@Override
	public void postHandle(HttpServletRequest req, HttpServletResponse res, Object arg2, ModelAndView arg3) throws Exception {
	}

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object arg2) throws Exception {
		String requestURI = req.getRequestURI();
		String nextUrl = req.getScheme()+"://"+req.getServerName()+":"+req.getServerPort()+req.getRequestURI(); 
		if(req.getServerPort() == 80){
			nextUrl = req.getScheme()+"://"+req.getServerName()+req.getRequestURI(); 
		}
		if(req.getQueryString() != null && !"".equals(req.getQueryString())){
			nextUrl = nextUrl + "?"+req.getQueryString();
		}
		if(excludedUrls != null){
			for (String url : excludedUrls) {
				if (requestURI.endsWith(url)) {
					return true;
				}
			}
		}
		// 小程序用接口
//		if (requestURI.contains("weapp")) {
//			String token = "";
//			// 运营端小程序验证机制
//			if (requestURI.contains("weappoms")) {
//				token = CookieUtils.getCookies(CookieUtils.COOKIE_ID.weapptoken, req);
//			} else {
//				token = req.getParameter("token");
//			}
//			if (StringUtils.isEmpty(token)) {
//				return false;
//			}
//			IWeAppMineServices weAppMineServices = (IWeAppMineServices) SpringUtils.getBean("weAppMineServices");
//			Map<String, Object> memberInfo = weAppMineServices.checkToken(token);
//			return true;
//		}
		return true;
	}
}
