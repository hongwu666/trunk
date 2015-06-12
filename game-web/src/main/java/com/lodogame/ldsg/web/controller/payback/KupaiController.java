package com.lodogame.ldsg.web.controller.payback;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.CharEncoding;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.lodogame.ldsg.web.factory.PartnerServiceFactory;
import com.lodogame.ldsg.web.model.jinli.JinLiPaymentObj;
import com.lodogame.ldsg.web.sdk.AnZhiSdk;
import com.lodogame.ldsg.web.sdk.JinLiSdk;
import com.lodogame.ldsg.web.service.PartnerService;
import com.lodogame.ldsg.web.util.jinli.RSASignature;

@Controller
public class KupaiController {

	private static Logger LOG = Logger.getLogger(KupaiController.class);

	@Autowired
	private PartnerServiceFactory serviceFactory;

	@RequestMapping(value = "/webApi/kupaiPayment.do")
	public ModelAndView payCallback(HttpServletRequest req, HttpServletResponse res) {

		PartnerService ps = serviceFactory.getBean(JinLiSdk.instance().getPartnerId());

		String retVal = "success";

		ModelAndView modelView = new ModelAndView("empty", "output", retVal);

		return modelView;
	}
}
