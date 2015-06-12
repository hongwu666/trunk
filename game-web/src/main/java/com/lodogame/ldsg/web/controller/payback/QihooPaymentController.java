package com.lodogame.ldsg.web.controller.payback;

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
import com.lodogame.ldsg.web.model.qihoo.PayCallbackObject;
import com.lodogame.ldsg.web.sdk.QihooSdk;
import com.lodogame.ldsg.web.service.PartnerService;
import com.lodogame.ldsg.web.service.WebApiService;
import com.lodogame.ldsg.web.util.Json;

@Controller
public class QihooPaymentController {
private static Logger LOG = Logger.getLogger(QihooPaymentController.class);
	
	@Autowired
	private PartnerServiceFactory serviceFactory;
	
	@RequestMapping(value = "/webApi/qhRefreshToken.do", method = RequestMethod.GET)
	public ModelAndView qhRefreshToken(HttpServletRequest req){
		String accessToken = req.getParameter("accessToken");
		String refreshToken = req.getParameter("refreshToken");
		Map<String, String> ret = QihooSdk.instance().refreshToken(accessToken, refreshToken);
		Map<String, Object> map = new HashMap<String, Object>();
		if(ret != null && ret.containsKey("access_token")){
			map.put("rc", WebApiService.SUCCESS);
			Map<String, String> data = new HashMap<String, String>();
			data.put("ptk", ret.get("access_token"));
			data.put("rtk", ret.get("refresh_token"));
			map.put("dt", data);
		}else{
			LOG.info("刷新token失败");
			map.put("rc", WebApiService.UNKNOWN_ERROR);
		}
		
		ModelAndView modelView = new ModelAndView();
		MappingJacksonJsonView view = new MappingJacksonJsonView();
		view.setAttributesMap(map);
		modelView.setView(view);
		LOG.debug(Json.toJson(map));
		return modelView;
	}
	
	@RequestMapping(value = "/webApi/qhPayment.do", method = RequestMethod.GET)
	public ModelAndView qihooCallBack(HttpServletRequest req, HttpServletResponse res) {
		PartnerService ps = serviceFactory.getBean(QihooSdk.instance().getPartnerId());
		PayCallbackObject cb = new PayCallbackObject();
		cb.setApp_key(req.getParameter("app_key"));
		//针对360以分为单位处理,除以100,得出实际以元为单位的数额
		cb.setAmount(req.getParameter("amount"));
		cb.setApp_ext1(req.getParameter("app_ext1"));
		cb.setApp_ext2(req.getParameter("app_ext2"));
		cb.setApp_order_id(req.getParameter("app_order_id"));
		cb.setApp_uid(req.getParameter("app_uid"));
		cb.setGateway_flag(req.getParameter("gateway_flag"));
		cb.setOrder_id(req.getParameter("order_id"));
		cb.setProduct_id(req.getParameter("product_id"));
		cb.setSign(req.getParameter("sign"));
		cb.setSign_return(req.getParameter("sign_return"));
		cb.setSign_type(req.getParameter("sign_type"));
		cb.setUser_id(req.getParameter("user_id"));
		
		String retVal = "fail";
			
		
		try {
			if(ps.doPayment(cb)){
				retVal = "ok";
			}
		} catch (Exception e) {
			retVal = "fail";
			LOG.info(e.getMessage(), e);
		}
			
		
		ModelAndView modelView = new ModelAndView("empty", "output", retVal);

		return modelView;
	}
}
