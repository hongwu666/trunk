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
public class JinLiController {
	private static Logger LOG = Logger.getLogger(JinLiController.class);

	@Autowired
	private PartnerServiceFactory serviceFactory;

	@RequestMapping(value = "/webApi/jinliPayment.do")
	public ModelAndView payCallback(HttpServletRequest req, HttpServletResponse res) {

		PartnerService ps = serviceFactory.getBean(JinLiSdk.instance().getPartnerId());

		Map<String, String> map = new HashMap<String, String>();

		String sign = req.getParameter("sign");
		String orderId = req.getParameter("out_order_no");
		BigDecimal orderAmount = new BigDecimal(req.getParameter("deal_price"));

		/*************************************** 组装重排序参数 *********************************************/
		Enumeration<String> attributeNames = req.getParameterNames();
		while (attributeNames.hasMoreElements()) {
			String name = attributeNames.nextElement();
			map.put(name, req.getParameter(name));
		}

		StringBuilder contentBuffer = new StringBuilder();
		Object[] signParamArray = map.keySet().toArray();
		Arrays.sort(signParamArray);
		for (Object key : signParamArray) {
			String value = map.get(key);
			if (!"sign".equals(key) && !"msg".equals(key)) {// sign和msg不参与签名
				contentBuffer.append(key + "=" + value + "&");
			}
		}

		String content = StringUtils.removeEnd(contentBuffer.toString(), "&");

		String retVal = "success";

		JinLiPaymentObj paymentObj = new JinLiPaymentObj();
		paymentObj.setContent(content);
		paymentObj.setSign(sign);
		paymentObj.setOrderId(orderId);
		paymentObj.setOrderAmount(orderAmount);

		if (!ps.doPayment(paymentObj)) {
			retVal = "fail";
		}

		ModelAndView modelView = new ModelAndView("empty", "output", retVal);

		return modelView;
	}
}
