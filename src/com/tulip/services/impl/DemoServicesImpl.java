package com.tulip.services.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tulip.mapper.DemoMapper;
import com.tulip.services.IDemoServices;

@Service("loginServices")
public class DemoServicesImpl implements IDemoServices {
	private static final Logger logger = LogManager.getLogger(DemoServicesImpl.class.getName());

	@Autowired
	DemoMapper demoMapper;

	@Override
	public String test(String param) {
		logger.info("service.info");
		return demoMapper.getTime();
	}
}