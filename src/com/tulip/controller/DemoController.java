package com.tulip.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tulip.services.IDemoServices;
@Controller
@RequestMapping(value = "demo")
public class DemoController {
	private static final Logger logger = LogManager.getLogger(DemoController.class.getName());
	
	@Autowired
	private IDemoServices demoServices;
	/**
	 * 测试
	 * @throws IOException 
	 */
	@RequestMapping(value = "test")
	public void test(HttpServletResponse response, HttpServletRequest request, String setId, String fromDate) throws Exception {
		logger.error("测试");
		response.getWriter().print(demoServices.test(""));
	}
}