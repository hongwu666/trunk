package com.lodogame.ldsg.web.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lodogame.ldsg.web.service.impl.MyCardServiceImpl;
import com.lodogame.ldsg.web.util.DateUtil;

@Controller
public class ServiceController {

	@Autowired
	private MyCardServiceImpl myCardPartner;

	@RequestMapping("/webApi/mycardService.do")
	@ResponseBody
	public String mycardService(HttpServletRequest req) {
		String startDateStr = req.getParameter("StartDate");
		String endDateStr = req.getParameter("EndDate");
		String mycardNo = req.getParameter("MyCardID");
		Date startDate = null;
		Date endDate = null;
		if (!StringUtils.isBlank(startDateStr)) {
			startDate = DateUtil.stringToDate(startDateStr + " 00:00:00", "yyyy/MM/dd HH:mm:ss");
		}
		if (!StringUtils.isBlank(endDateStr)) {
			endDate = DateUtil.stringToDate(endDateStr + " 23:59:59", "yyyy/MM/dd HH:mm:ss");
		}

		String result = myCardPartner.getMycardOrders(startDate, endDate,
				mycardNo);
		return result;
	}
}
