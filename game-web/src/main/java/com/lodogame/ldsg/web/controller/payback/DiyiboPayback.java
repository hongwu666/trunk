package com.lodogame.ldsg.web.controller.payback;

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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import com.lodogame.ldsg.web.factory.PartnerServiceFactory;
import com.lodogame.ldsg.web.model.diyibo.DiyiboOrderInfo;
import com.lodogame.ldsg.web.model.diyibo.DiyiboPayRequest;
import com.lodogame.ldsg.web.sdk.DiyiboSdk;
import com.lodogame.ldsg.web.service.impl.DiyiboServiceImpl;
import com.lodogame.ldsg.web.util.Json;
import com.lodogame.ldsg.web.util.MD5;

@Controller
public class DiyiboPayback {

	@Autowired
	private PartnerServiceFactory serviceFactory;

	private static final Logger LOG = Logger.getLogger(DiyiboPayback.class);

	@RequestMapping(value = "/webApi/payment.do", method = RequestMethod.POST)
	public ModelAndView payment(HttpServletRequest req, HttpServletResponse res) throws IOException {

		String json = getRequestJson(req);

		LOG.info("支付成功后，回调 payment.do 接口请求的参数: " + json);

		DiyiboPayRequest payRequest = Json.toObject(json, DiyiboPayRequest.class);
		DiyiboServiceImpl ps = (DiyiboServiceImpl) serviceFactory.getBean(payRequest.getCpPrivateInfo());

		int code = 99;
		String message = "充值失败";

		Map<String, Object> rt = new HashMap<String, Object>();

		if (verifySign(payRequest, payRequest.getSign())) {

			boolean success = false;
			if (payRequest.isPaySucceeded() == true) {
				success = true;
			}

			BigDecimal finishAmount = new BigDecimal(payRequest.getData().getAmount());
			DiyiboOrderInfo order = payRequest.getData();
			String orderId = order.getCallbackinfo();

			if (ps.doPayment(orderId, order.getUserId(), success, order.getOrderId(), finishAmount)) {
				code = 1;
				message = "充值成功";
			}

		} else {
			code = 10;
			message = "参数较验失败";
		}

		Map<String, Object> state = new HashMap<String, Object>();
		state.put("code", code);
		state.put("msg", message);

		rt.put("id", Long.parseLong(payRequest.getId()));
		rt.put("state", state);

		ModelAndView modelView = new ModelAndView();
		MappingJacksonJsonView view = new MappingJacksonJsonView();
		view.setAttributesMap(rt);
		modelView.setView(view);
		return modelView;

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

	private boolean verifySign(DiyiboPayRequest payRequest, String sign) {

		String cpid = DiyiboSdk.instance().getGame().getCpid();
		String apikey = DiyiboSdk.instance().getApikey();

		String str = cpid + payRequest.getData().getSign() + apikey;
		boolean isVerifyed = MD5.MD5Encode(str).equals(sign);
		return isVerifyed;
	}
}
