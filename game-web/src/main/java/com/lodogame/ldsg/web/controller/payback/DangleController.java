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
import com.lodogame.ldsg.web.model.dangle.DanglePaymentObj;
import com.lodogame.ldsg.web.sdk.DangleSdk;
import com.lodogame.ldsg.web.service.PartnerService;

@Controller
public class DangleController {
	private static Logger LOG = Logger.getLogger(DangleController.class);
	
	@Autowired
	private PartnerServiceFactory serviceFactory;

	@RequestMapping(value = "/webApi/dlPayment.do", method = RequestMethod.GET)
	public ModelAndView payCallback(HttpServletRequest req, HttpServletResponse res) {
		PartnerService ps = serviceFactory.getBean(DangleSdk.instance().getPartnerId());
		DanglePaymentObj data = new DanglePaymentObj();
		data.setSignature(req.getParameter("signature"));
		data.setExt(req.getParameter("ext"));
		data.setMid(req.getParameter("mid"));
		data.setMoney(req.getParameter("money"));
		data.setOrder(req.getParameter("order"));
		data.setResult(req.getParameter("result"));
		data.setTime(req.getParameter("time"));
		LOG.info(data.getExt());
		try {
			if(ps.doPayment(data)){
				res.getWriter().print("success");
			}else{
				res.getWriter().print("error");
			}
		} catch (Exception e) {
			LOG.error("dangle payment error!",e);
		}
		return null;
	}
}
