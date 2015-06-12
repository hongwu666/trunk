package com.lodogame.ldsg.web.controller.payback;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.lodogame.ldsg.web.factory.PartnerServiceFactory;
import com.lodogame.ldsg.web.model.zhangyue.ZhangYuePaymentObj;
import com.lodogame.ldsg.web.sdk.ZhangYueSdk;
import com.lodogame.ldsg.web.service.PartnerService;
import com.lodogame.ldsg.web.util.Json;

@Controller
public class ZhangYueController {

	private static Logger LOG = Logger.getLogger(ZhangYueController.class);

	@Autowired
	private PartnerServiceFactory serviceFactory;

	@RequestMapping(value = "/webApi/zhangyuePayment.do", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView oppoCallBack(HttpServletRequest req, HttpServletResponse resp) throws Exception {

		String transData = req.getParameter("transData");
		ZhangYuePaymentObj paymentObj = Json.toObject(transData, ZhangYuePaymentObj.class);

		String result = "success";
		try {

			PartnerService ps = serviceFactory.getBean(ZhangYueSdk.instance().getPartnerId());

			if (ps.doPayment(paymentObj)) {
				result = "success";
			} else {
				result = "fail";
			}
		} catch (Exception ex) {
			LOG.error(ex);
			result = "fail";
		}

		ModelAndView modelView = new ModelAndView("empty", "output", result);
		return modelView;

	}

	@RequestMapping(value = "/webApi/zhangyueTokenCheck.do")
	public ModelAndView zhangyueTokenCheck(HttpServletRequest req, HttpServletResponse resp) throws Exception {

		String token = req.getParameter("token");
		String puid = req.getParameter("puid");

		String result = "true";
		try {

			if (!ZhangYueSdk.instance().verify(token, puid)) {
				result = "false";
			}

		} catch (Exception ex) {
			LOG.error(ex);
			result = "false";
		}

		ModelAndView modelView = new ModelAndView("empty", "output", result);
		return modelView;

	}
}
