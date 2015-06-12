package com.lodogame.ldsg.web.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {
	
	private static final Logger LOG = Logger.getLogger(TestController.class);
	
	@RequestMapping("/test.do")
	@ResponseBody
	public String test() {
		LOG.info("success");
		return "success";
	}
}
