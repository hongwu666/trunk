package com.lodogame.ldsg.web.controller.payment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.lodogame.ldsg.web.factory.PartnerServiceFactory;
import com.lodogame.ldsg.web.model.google.GooglePayment;
import com.lodogame.ldsg.web.sdk.GooGlePlayNewSdk;
import com.lodogame.ldsg.web.service.BasePartnerService;
import com.lodogame.ldsg.web.util.Json;
import com.lodogame.ldsg.web.util.google.Security;

@Controller
public class GooglePlayNewController {
	private static Logger LOGGER = Logger.getLogger(GooglePlayNewController.class);

	@Autowired
	private PartnerServiceFactory serviceFactory;
	
	@Autowired
	private BasePartnerService basePartnerService;

	@RequestMapping(value = "/webApi/googlePaymentNew.do")
	public ModelAndView payCallback(HttpServletRequest req, HttpServletResponse res) {
		
		String json = req.getParameter("json");
		String signature = req.getParameter("signature");
		//json = "{\"orderId\":\"12999763169054705758.1304537946475907\",\"packageName\":\"com.easou.game.sghhr.google\",\"productId\":\"tkp_money60\",\"purchaseTime\":1390209011184,\"purchaseState\":0,\"developerPayload\":\"0110242014012000000044\",\"purchaseToken\":\"ejfxxitfrgcwzjrdunzfcwrs.AO-J1Oz9VJibC4umtnXfMhGoehLrAE0a7yLP7m5hb8NzoeSG-kbdClI03mURzQoRBrajJ4N-9A3rouSy7iLJhDmngXzTwtO80KPttM0CjQbfyLYqEgVOFfy4ge1P-gok8ctKMaBiNGWD\"}";
		//signature ="FbtYMBbkLybBuvpCvXCX37iqYL27lZiN4lTJWW1YtAz3F2XVrZlWbm9p14ntMqeLHzqihW52Rk+09n2dHDCxkdvuiqhgwCLgI+wgRF6QAQqWc7l8bdNHu0rFodtvW8+BubIQoWhlsb+lcWfKMTqvERcMnInw5IIiEnpBZ2E/NDykRcmo68mB2ACJvCUDdr/0UmzWlV8BwM4vWiSiUhI7VUcFAEQKqhdthQ+/LPjv27LEi5KXNIpNLmtYLPijU1eChnVIsHeX4xhHDWMZR/DaSCtpO913Ty1a7kRC9EbTCz2pHxjBWlJChkcLzVcja7GMAExQ+YZ9x8+MEyG1C/B6Jg==";
		
		LOGGER.info("google new 支付请求数据 json[" + json + "] signature[" + signature + "]");
		
		try {
			if(StringUtils.isNotEmpty(json)	&& StringUtils.isNotEmpty(signature)){

				if (Security.verifyPurchase(GooGlePlayNewSdk.instance().getAppKey(), json, signature)) {
					GooglePayment googlePayment = Json.toObject(json, GooglePayment.class);

					if(basePartnerService.doPayment(googlePayment)){
						res.getOutputStream().print("0");
					}else{
						res.getOutputStream().print("1");
					}
				}
			}else{
				res.getOutputStream().print("1");
			}
		} catch (Exception e) {
			LOGGER.error("googlePayment error!",e);
		}
		return null;
	}
}
