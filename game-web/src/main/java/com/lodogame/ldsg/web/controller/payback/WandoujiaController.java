package com.lodogame.ldsg.web.controller.payback;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.lodogame.ldsg.web.factory.PartnerServiceFactory;
import com.lodogame.ldsg.web.model.wandoujia.WandoujiaPaymentObj;
import com.lodogame.ldsg.web.sdk.WandoujiaSdk;
import com.lodogame.ldsg.web.service.PartnerService;
import com.lodogame.ldsg.web.util.Json;

@Controller
public class WandoujiaController {
	private static Logger LOG = Logger.getLogger(WandoujiaController.class);
	
	@Autowired
	private PartnerServiceFactory serviceFactory;

	@RequestMapping(value = "/webApi/wandoujiaPayment.do")
	public ModelAndView payCallback(HttpServletRequest req, HttpServletResponse res) {
		PartnerService ps = serviceFactory.getBean(WandoujiaSdk.instance().getPartnerId());
		String responseStr = req.getParameter("content");
		try {
			if(StringUtils.isNotBlank(responseStr)){
				WandoujiaPaymentObj obj = Json.toObject(responseStr, WandoujiaPaymentObj.class);
				obj.setData(responseStr);
				obj.setSign(req.getParameter("sign"));
				if (ps.doPayment(obj)) {
					res.getWriter().print("success");
				}else{
					res.getWriter().print("fail");
				}
			}else{
				res.getWriter().print("fail");
			}
		} catch (Exception e) {
			LOG.error("wandoujia payment error!",e);
		}
		return null;
	}
}
