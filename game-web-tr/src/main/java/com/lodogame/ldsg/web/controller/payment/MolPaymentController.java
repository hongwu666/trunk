package com.lodogame.ldsg.web.controller.payment;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lodogame.ldsg.web.factory.PartnerServiceFactory;
import com.lodogame.ldsg.web.model.mol.MOLPayment;
import com.lodogame.ldsg.web.sdk.GooGlePlaySdk;
import com.lodogame.ldsg.web.sdk.MOLSdk;
import com.lodogame.ldsg.web.sdk.mycard.MyCardSdk;
import com.lodogame.ldsg.web.service.BasePartnerService;
import com.lodogame.ldsg.web.service.PartnerService;
import com.lodogame.ldsg.web.util.RequestUtils;

@Controller
public class MolPaymentController {

	@Autowired
	private PartnerServiceFactory serviceFactory;
	
	private static final Logger LOG = Logger.getLogger(MolPaymentController.class);

	@RequestMapping(value = "/webApi/molPayment.do", method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public int payment(HttpServletRequest req, HttpServletResponse res) throws IOException {

		int returnValue = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		
		PartnerService ps = serviceFactory.getBean(MOLSdk.instance().getPartnerId());

		String requestData = RequestUtils.getRequestData(req);
		
		LOG.info("MOL 支付回调 " + requestData);
		
		try {
			MOLPayment molPayment = RequestUtils.convertRequestDataToBean(requestData, MOLPayment.class);
			if (ps.doPayment(molPayment)) {
				returnValue = HttpServletResponse.SC_OK;
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		
		return returnValue;
	}
	

	@RequestMapping(value = "/webApi/molPayRequest.do", method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String payRequest(HttpServletRequest req, HttpServletResponse res) throws IOException {
		String url = req.getParameter("url");
		String content = req.getParameter("content");
		
		LOG.info("MOL 支付客户端发送支付数据 url[" + url + "] content[" + content + "]");
		
		String result = MOLSdk.payRequest(url, content);
		
		LOG.info("MOL 支付请求返回数据 result[" + result + "]");
		
		return result;
	}
	
	
	
	 @RequestMapping(value="/webApi/molPaymentResult.do",method={RequestMethod.GET, RequestMethod.POST})  
	 public ModelAndView testRedirect(HttpServletRequest req, HttpServletResponse res){  
	      return new ModelAndView("molPayResult");  
	 }
	 
	 
}
