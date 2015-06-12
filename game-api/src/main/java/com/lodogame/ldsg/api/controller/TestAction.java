package com.lodogame.ldsg.api.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TestAction extends BaseController {

	private static final Logger LOG = Logger.getLogger(UserController.class);

	@RequestMapping("/test.do")
	public ModelAndView test(HttpServletRequest req) {

		String q = req.getParameter("q");

		LOG.debug(q);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", 1);
		map.put("q", q);
		return this.renderJson(map);
	}
}
