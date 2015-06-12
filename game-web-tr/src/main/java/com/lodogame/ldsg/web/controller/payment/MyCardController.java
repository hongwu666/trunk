package com.lodogame.ldsg.web.controller.payment;

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

import com.lodogame.ldsg.web.dao.PaymentOrderDao;
import com.lodogame.ldsg.web.factory.PartnerServiceFactory;
import com.lodogame.ldsg.web.model.PaymentOrder;
import com.lodogame.ldsg.web.model.mycard.CheckEligibleRequest;
import com.lodogame.ldsg.web.model.mycard.MyCardApiPayment;
import com.lodogame.ldsg.web.model.mycard.MyCardPayment;
import com.lodogame.ldsg.web.sdk.mycard.MyCardSdk;
import com.lodogame.ldsg.web.service.PartnerService;
import com.lodogame.ldsg.web.service.impl.MyCardServiceImpl;
import com.lodogame.ldsg.web.util.Json;
import com.lodogame.ldsg.web.util.RequestUtils;

@Controller
public class MyCardController {

	private static final Logger LOGGER = Logger.getLogger(MyCardController.class);
	
	@Autowired
	private PaymentOrderDao paymentOrderDao;
	
	@Autowired
	private MyCardServiceImpl myCardServiceImpl;
	
	@Autowired
	private PartnerServiceFactory serviceFactory;
	
	@RequestMapping(value = "/webApi/isEligible.do", method = {RequestMethod.POST, RequestMethod.GET})
	public ModelAndView isEligible(HttpServletRequest req, HttpServletResponse res) throws IOException {
		
		String requestData = req.getParameter("DATA");

		LOGGER.info("MyCard isEligible - 请求数据 " + requestData);
		
		Map<String, Object> map= Json.toObject(requestData, Map.class);
		CheckEligibleRequest checkPayUser = new CheckEligibleRequest();
		
		checkPayUser.setAccount((String) map.get("Account"));
		checkPayUser.setAmount((Integer) map.get("Amount"));
		checkPayUser.setCharacter_ID((String) map.get("Character_ID"));
		checkPayUser.setCP_TxID((String) map.get("CP_TxID"));
		checkPayUser.setRealm_ID((String) map.get("Realm_ID"));
		checkPayUser.setSecurityKey((String) map.get("SecurityKey"));
		
		Map<String, Integer> returnValue = new HashMap<String, Integer>();
		returnValue.put("ResultCode", CheckEligibleRequest.PAY_NOT_ELIGIBLE);
		
		if (checkPayUser.isRequestDataCorrect() == true) {
			String gameOrderId = checkPayUser.getGameOrderId();
			PaymentOrder paymentOrder = paymentOrderDao.get(gameOrderId);
			if (paymentOrder != null) {
				returnValue.put("ResultCode", CheckEligibleRequest.PAY_ELIGIBLE);
			} else {
				LOGGER.error("MyCard isEligible - 订单为空 gameOrderId[" + gameOrderId + "] MyCard 请求数据 " + requestData );
			}
		}
		
		MappingJacksonJsonView view = new MappingJacksonJsonView();
		view.setAttributesMap(returnValue);
		ModelAndView modelView = new ModelAndView();
		modelView.setView(view);
		return modelView;
	}
	
	@RequestMapping(value = "/webApi/mycardSDKPayment.do", method = RequestMethod.POST)
	public ModelAndView myCardPayment(HttpServletRequest req, HttpServletResponse res) throws IOException {
		PartnerService ps = serviceFactory.getBean(MyCardSdk.instance().getPartnerId());
		
		String requestData = req.getParameter("DATA");

		LOGGER.info("MyCard 钱包支付回调请求数据 " + requestData);
		
		Map<String, Object> map= Json.toObject(requestData, Map.class);
		
		MyCardPayment myCardPayment = new MyCardPayment();
		myCardPayment.setAccount((String) map.get("Account"));
		myCardPayment.setAmount(new BigDecimal((Integer) map.get("Amount")));
		myCardPayment.setAUTH_CODE((String) map.get("AUTH_CODE"));
		myCardPayment.setCharacter_ID((String) map.get("Character_ID"));
		myCardPayment.setCP_TxID((String) map.get("CP_TxID"));
		myCardPayment.setMG_TxID((String) map.get("MG_TxID"));
		myCardPayment.setMyCardProjectNo((String) map.get("MyCardProjectNo"));
		myCardPayment.setMyCardType((String) map.get("MyCardType"));
		myCardPayment.setRealm_ID((String) map.get("Realm_ID"));
		myCardPayment.setSecurityKey((String) map.get("SecurityKey"));
		myCardPayment.setTx_Time((Integer) map.get("Tx_Time"));
		
		
		Map<String, Object> returnValue = new HashMap<String, Object>();
		returnValue.put("CP_TxID", myCardPayment.getCP_TxID());
		returnValue.put("AUTH_CODE", myCardPayment.getAUTH_CODE());
		returnValue.put("MG_TxID", myCardPayment.getMG_TxID());
		returnValue.put("ResultCode", MyCardPayment.CP_PAY_FAILED);
		returnValue.put("Description", "支付失败");
		
		
		try {
			if (ps.doPayment(myCardPayment) == true) {
				returnValue.put("ResultCode", MyCardPayment.CP_PAY_SUCCEED);
				returnValue.put("Description", "支付成功");
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		
		MappingJacksonJsonView view = new MappingJacksonJsonView();
		view.setAttributesMap(returnValue);
		ModelAndView modelView = new ModelAndView();
		modelView.setView(view);
		return modelView;
	}

	/**
	 * MyCard 点卡支付 - 客户端请求服务端去 MyCard 获取授权码
	 */
	 @RequestMapping(value="/webApi/myCardAuthRequest.do",method={RequestMethod.GET, RequestMethod.POST})
	 @ResponseBody
	 public String myCardAuthRequest(HttpServletRequest req, HttpServletResponse res) throws IOException{
		 
	      String gameOrderId = req.getParameter("gameOrderId");
	      
	      LOGGER.info("MyCard server-to-server 点卡支付 - 客户端发送获取授权码请求, 参数 gameOrderId[" + gameOrderId + "]");
	      
	      String result = MyCardSdk.instance().auth(gameOrderId);
	      
	      return result;
	 }
	 
	 /**
	  * MyCard 点卡支付 - 客户端拿到授权码后，将卡号和密码传给服务端，由服务端提交到 MyCard 进行支付
	  */
	 @RequestMapping(value="/webApi/myCardPayRequest.do",method={RequestMethod.GET, RequestMethod.POST})
	 @ResponseBody
	 public String myCardPayRequest(HttpServletRequest req, HttpServletResponse res){
		 
		 String authCode = req.getParameter("authCode");
		 String facMemid = req.getParameter("facMemid");
		 String cardId = req.getParameter("cardId");
		 String cardPwd = req.getParameter("cardPwd");
		 
		 LOGGER.info("MyCard server-to-server 点卡支付 - 客户端发送支付请求, 参数 authCode[" + authCode + "] facMemid[" + facMemid + "] cardId[" + cardId + "] cardPwd[" + cardPwd + "]");
		 
		 String result = myCardServiceImpl.serverToServerPay(authCode, facMemid, cardId, cardPwd);
		 
		 LOGGER.info("MyCard server-to-server 点卡支付 - 返回支付结果给客户端 " + result + "]");

		 return result;
	 }
	 
	 @RequestMapping(value="/webApi/mycardAPIPayment.do",method={RequestMethod.GET, RequestMethod.POST})
	 public void myCardAPIPayment(HttpServletRequest req, HttpServletResponse res) throws IOException{
		 
		 String requestData = RequestUtils.getRequestData(req);
		 LOGGER.info("MyCard web-site 点卡支付回调请求数据 " + requestData);
		 
		 Map<String, String> map = RequestUtils.parseRequestString(requestData);
		 
		 MyCardApiPayment payment = new MyCardApiPayment();
		 
		 payment.setCardId(map.get("CardId"));
		 payment.setCardKind(map.get("CardKind"));
		 payment.setCardPoint(Integer.valueOf(map.get("CardPoint")));
		 payment.setErrorMsg(map.get("ErrorMsg"));
		 payment.setErrorMsgNo(map.get("ErrorMsgNo"));
		 payment.setFacId(map.get("facId"));
		 payment.setFacMemId(map.get("facMemId"));
		 payment.setFacTradeSeq(map.get("facTradeSeq"));
		 payment.setHash(map.get("hash"));
		 payment.setOProjNo(map.get("oProjNo"));
		 payment.setReturnMsgNo(map.get("ReturnMsgNo"));
		 payment.setTradeSeq(map.get("tradeSeq"));
		 
		 myCardServiceImpl.apiPayDoPayment(payment);
	 }
	

}