package com.lodogame.ldsg.web.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lodogame.ldsg.web.bo.ApplePaybackBO;
import com.lodogame.ldsg.web.factory.PartnerServiceFactory;
import com.lodogame.ldsg.web.model.apple.ApplePaymentObj;
import com.lodogame.ldsg.web.sdk.AppleSdk;
import com.lodogame.ldsg.web.service.PartnerService;
import com.lodogame.ldsg.web.util.Json;

@Controller
public class ApplePayback {
	private static Logger LOG = Logger.getLogger(ApplePayback.class);

	@Autowired
	private PartnerServiceFactory serviceFactory;

	@RequestMapping(value = "/webApi/applePayment.do", method = RequestMethod.POST)
	@ResponseBody
	public String payCallback(HttpServletRequest req, HttpServletResponse res) {
		PartnerService ps = serviceFactory.getBean(AppleSdk.instance().getPartnerId());
		
		ApplePaymentObj data = new ApplePaymentObj(req);
		
		if (data.isValid() == false) {
			ApplePaybackBO bo = new ApplePaybackBO("fail");
			return Json.toJson(bo);
		}

		ApplePaybackBO bo = tryDoPayment(ps, data);
		
		return Json.toJson(bo);
	}

	@SuppressWarnings("deprecation")
	private ApplePaybackBO tryDoPayment(PartnerService ps, ApplePaymentObj data) {
		ApplePaybackBO bo = new ApplePaybackBO();
		try {

			if (ps.doPayment(data)) {
				bo.setErrcode("success");
			} else {
				bo.setErrcode("fail");
			}

		} catch (Exception e) {
			bo.setErrcode("fail");
			LOG.error("decode data error!data:" + data.toString(), e);
		}
		return bo;
	}
}
