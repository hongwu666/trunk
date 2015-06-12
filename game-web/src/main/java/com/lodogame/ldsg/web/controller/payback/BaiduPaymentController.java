package com.lodogame.ldsg.web.controller.payback;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import com.lodogame.ldsg.web.exception.ServiceException;
import com.lodogame.ldsg.web.factory.PartnerServiceFactory;
import com.lodogame.ldsg.web.model.baidu.BaiduPaymentObj;
import com.lodogame.ldsg.web.sdk.BaiduSdk;
import com.lodogame.ldsg.web.service.PartnerService;
import com.lodogame.ldsg.web.util.DateUtil;
import com.lodogame.ldsg.web.util.EncryptUtil;
import com.lodogame.ldsg.web.util.Json;

@Controller
public class BaiduPaymentController {
	private static Logger LOG = Logger.getLogger(BaiduPaymentController.class);

	@Autowired
	private PartnerServiceFactory serviceFactory;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/webApi/baiduPayment.do", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView ucCallBack(HttpServletRequest req, HttpServletResponse res) {
		Map<String, Object> retVal = new HashMap<String, Object>();
		int resultCode = 0;
		PartnerService ps = serviceFactory.getBean(BaiduSdk.instance().getPartnerId());
		BaiduPaymentObj paymentObj = new BaiduPaymentObj();
		try {
			paymentObj.setAppId(Integer.parseInt(req.getParameter("AppID")));
			paymentObj.setOrderSerial(req.getParameter("OrderSerial"));
			paymentObj.setCooperatorOrderSerial(req.getParameter("CooperatorOrderSerial"));
			paymentObj.setContent(new String(Base64.decodeBase64(req.getParameter("Content")), "utf-8"));
			paymentObj.setSign(req.getParameter("Sign"));
			Map<String, Object> cntMap = Json.toObject(paymentObj.getContent(), HashMap.class);
			paymentObj.setUid(cntMap.get("UID") == null ? "" : cntMap.get("UID").toString());
			paymentObj.setMerchandiseName((String) cntMap.get("MerchandiseName"));
			paymentObj.setOrderMoney(new BigDecimal((String) cntMap.get("OrderMoney")));
			paymentObj.setStartDateTime(DateUtil.stringToDate((String) cntMap.get("StartDateTime"), DateUtil.STRING_DATE_FORMAT));
			paymentObj.setBankDateTime(DateUtil.stringToDate((String) cntMap.get("BankDateTime"), DateUtil.STRING_DATE_FORMAT));
			paymentObj.setOrderStatus((Integer) cntMap.get("OrderStatus"));
			paymentObj.setStatusMsg((String) cntMap.get("StatusMsg"));
			paymentObj.setExtInfo((String) cntMap.get("ExtInfo"));
			paymentObj.setContent(req.getParameter("Content"));
			if (ps.doPayment(paymentObj)) {
				resultCode = 1;
			}

		} catch (ServiceException e) {
			LOG.info(e.getMessage(), e);
			resultCode = e.getCode();
		} catch (Exception e) {
			LOG.info(e.getMessage(), e);
			resultCode = 0;
		}
		retVal.put("AppID", paymentObj.getAppId());
		retVal.put("ResultMsg", "");
		retVal.put("Sign", EncryptUtil.getMD5(paymentObj.getAppId() + resultCode + BaiduSdk.instance().getAppSecret()));
		retVal.put("ResultCode", resultCode);
		retVal.put("Content", "");
		MappingJacksonJsonView view = new MappingJacksonJsonView();
		view.setAttributesMap(retVal);
		ModelAndView modelView = new ModelAndView();
		modelView.setView(view);
		return modelView;
	}
}
