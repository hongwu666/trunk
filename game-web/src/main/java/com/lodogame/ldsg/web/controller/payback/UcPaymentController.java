package com.lodogame.ldsg.web.controller.payback;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.lodogame.ldsg.web.factory.PartnerServiceFactory;
import com.lodogame.ldsg.web.model.uc.PayCallbackResponse;
import com.lodogame.ldsg.web.sdk.UcSdk;
import com.lodogame.ldsg.web.service.PartnerService;
import com.lodogame.ldsg.web.util.Json;

@Controller
public class UcPaymentController {

	private static Logger LOG = Logger.getLogger(UcPaymentController.class);

	@Autowired
	private PartnerServiceFactory serviceFactory;

	@RequestMapping(value = "/webApi/ucPayment.do", method ={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView ucCallBack(HttpServletRequest req, HttpServletResponse res) {
		PartnerService ps = serviceFactory.getBean(UcSdk.instance().getPartnerId());
		String retVal = "";
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(req.getInputStream(), "utf-8"));
			String ln;
			StringBuffer stringBuffer = new StringBuffer();
			while ((ln = in.readLine()) != null) {
				stringBuffer.append(ln);
				stringBuffer.append("\r\n");
			}

			PayCallbackResponse rsp = Json.toObject(stringBuffer.toString(), PayCallbackResponse.class);

			if (ps.doPayment(rsp)) {
				retVal = "SUCCESS";
			} else {
				// retVal = "FAILURE";
				retVal = "SUCCESS";
			}

		} catch (Exception e) {
			LOG.info(e.getMessage(), e);
			retVal = "SUCCESS";
		}

		ModelAndView modelView = new ModelAndView("empty", "output", retVal);

		return modelView;
	}
}
