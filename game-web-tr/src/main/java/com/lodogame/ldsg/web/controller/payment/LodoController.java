package com.lodogame.ldsg.web.controller.payment;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import com.lodogame.ldsg.web.factory.PartnerServiceFactory;
import com.lodogame.ldsg.web.model.lodo.LodoOrderInfo;
import com.lodogame.ldsg.web.model.lodo.LodoRequest;
import com.lodogame.ldsg.web.sdk.LodoSdk;
import com.lodogame.ldsg.web.service.PartnerService;
import com.lodogame.ldsg.web.util.Json;
import com.lodogame.ldsg.web.util.MD5;

@Controller
public class LodoController {
	
	@Autowired
	private PartnerServiceFactory serviceFactory;
	
	private static final Logger LOG = Logger.getLogger(LodoController.class);

	@RequestMapping(value = "/webApi/lodoPayment.do", method = RequestMethod.POST)
	@ResponseBody
	public String payment(HttpServletRequest req, HttpServletResponse res) throws IOException {

		String json = getRequestJson(req);

		LOG.info("lodo 支付回调, 请求参数" + json);

		LodoRequest payRequest = Json.toObject(json, LodoRequest.class);
		PartnerService ps = serviceFactory.getBean(LodoSdk.instance().getPartnerId());
		

		String returnValue = "FAILED";
		
		if (verifySign(payRequest, payRequest.getSign())) {

			boolean success = false;
			if (payRequest.isPaySucceeded() == true) {
				success = true;
			}

			BigDecimal finishAmount = new BigDecimal(payRequest.getData().getAmount());
			LodoOrderInfo order = payRequest.getData();
			String orderId = order.getOrderid();

			if (ps.doPayment(orderId, order.getUserid(), success, order.getOrderid(), finishAmount, json)) {
				returnValue = "SUCCESS";
			} 

		}

		return returnValue;

	}
	
	private String getRequestJson(HttpServletRequest req) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = req.getReader();
		try {
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append('\n');
			}
		} finally {
			reader.close();
		}

		String json = sb.toString();
		return json;
	}
	
	private boolean verifySign(LodoRequest payRequest, String sign) {

		String cpid = LodoSdk.instance().getGame().getCpid();
		String apikey = LodoSdk.instance().getApikey();

		String str = cpid + payRequest.getData().getSign() + apikey;
		String computedSign = MD5.MD5Encode(str);
						
		boolean isVerifyed = computedSign.equalsIgnoreCase(sign);
		
		if (isVerifyed == false) {
			cpid = "1000";
			apikey = "f0a97500aa76416981f6d6e580d6167e";
			str = cpid + payRequest.getData().getSign() + apikey;
			computedSign = MD5.MD5Encode(str);
			isVerifyed = computedSign.equalsIgnoreCase(sign);
		}
		
		if (isVerifyed == false) {
			LOG.debug("lodo 支付回调, 签名错误, 请求的签名[" + sign + "] 计算得到的签名[" + computedSign + "] 计算签名使用的字符串[" + str + "]");
		}
		
		return isVerifyed;
	}
}
