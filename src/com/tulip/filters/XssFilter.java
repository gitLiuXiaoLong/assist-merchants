package com.tulip.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class XssFilter implements Filter {
	FilterConfig filterConfig = null;
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}
	public void destroy() {
		this.filterConfig = null;
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletResponse res = (HttpServletResponse) response;
		res.setHeader("Access-Control-Allow-Origin","*");
		res.setHeader("Access-Control-Allow-Methods","POST,GET,OPTIONS,DELETE");
		res.setHeader("Access-Control-Max-Age","3600");
		res.setHeader("Access-Control-Allow-Headers","Origin, X-Requested-With, Content-Type, Accept");
		res.setHeader("Content-type", "text/html;charset=UTF-8");
		chain.doFilter(new XssHttpServletRequestWrapper((HttpServletRequest) request), response);
	}
}