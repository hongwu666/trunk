package com.lodogame.ldsg.web.service.impl;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.ldsg.web.bo.OrderBO;
import com.lodogame.ldsg.web.dao.SystemMyCardCardPaymentDao;
import com.lodogame.ldsg.web.dao.impl.MyCardUnconfirmedOrderDaoMysqlImpl;
import com.lodogame.ldsg.web.model.Payment;
import com.lodogame.ldsg.web.model.PaymentOrder;
import com.lodogame.ldsg.web.model.PurchaseInfo;
import com.lodogame.ldsg.web.model.UserToken;
import com.lodogame.ldsg.web.model.mycard.MyCardApiPayment;
import com.lodogame.ldsg.web.model.mycard.MyCardPayment;
import com.lodogame.ldsg.web.model.mycard.MyCardServerToServerPayment;
import com.lodogame.ldsg.web.model.mycard.MycardUnconfirmedOrder;
import com.lodogame.ldsg.web.model.mycard.SystemMyCardCardPayment;
import com.lodogame.ldsg.web.sdk.GameApiSdk;
import com.lodogame.ldsg.web.sdk.mycard.MyCardSdk;
import com.lodogame.ldsg.web.service.BasePartnerService;
import com.lodogame.ldsg.web.service.MycardService;
import com.lodogame.ldsg.web.util.EncryptUtil;
import com.lodogame.ldsg.web.util.Json;
import com.lodogame.ldsg.web.util.UrlRequestUtils;

public class MyCardServiceImpl extends BasePartnerService implements MycardService{

	private Queue<MycardUnconfirmedOrder> mycardUnconfirmedOrderQueue = new ConcurrentLinkedQueue<MycardUnconfirmedOrder>();
	
	private static final Logger LOGGER = Logger.getLogger(BasePartnerService.class);
	
	@Autowired
	private SystemMyCardCardPaymentDao systemMyCardCardPaymentDao;
	
	@Autowired
	private MyCardUnconfirmedOrderDaoMysqlImpl myCardUnconfirmedOrderDaoMysqlImpl;
	
	@Override
	public UserToken login(String sessionId, String partnerId, String serverId, long timestamp, String sign, Map<String, String> params) {
		return null;
	}

	@Override
	public boolean doPayment(String orderId, String partnerUserId, boolean success, String partnerOrderId, BigDecimal finishAmount, String reqInfo) {
		return false;
	}

	@Override
	public String getPayBackUrl() {
		return null;
	}
	
	@Override
	public boolean doPayment(Payment payObj) {
		MyCardPayment payment = (MyCardPayment)payObj;
		
		if (payment == null) {
			LOGGER.error("payment 为空");
			return false;
		}
		
		if (payment.isPaymentDataVerified() == false) {
			LOGGER.error("支付数据验证失败");
			return false;
		}
		
		PaymentOrder order = paymentOrderDao.get(payment.getGameOrderId());
		
		LOGGER.info("游戏中订单信息 " + order.printPaymentOrderInfo());
		
		if (order.isOrderFinished() == true) {
			LOGGER.error("订单已经完成, 支付渠道订单信息 " + payment.printPaymentInfo());
			return false;
		}
			
		int gold = (int) (order.getAmount().doubleValue());
		String gameOrderId = order.getOrderId();
		String partnerOrderId = payment.getPartnerOrderId();
		String extinfo = payment.getExtInfo();
				
		BigDecimal finishAmount;
		try {
			finishAmount = payment.getFinishAmount(order.getAmount());
		} catch (Exception e) {
			LOGGER.error("游戏中订单和支付渠道的订单金额不符, 支付渠道订单信息" + payment.printPaymentInfo());
			return false;
		}

		
		// 更新订单状态
		if (this.paymentOrderDao.updateStatus(gameOrderId, PaymentOrder.STATUS_FINISH, partnerOrderId, finishAmount, extinfo)) {
			int port = serverService.getServerHttpPort(order.getServerId());
			// 请求游戏服，发放游戏货币
			if (!GameApiSdk.getInstance().doPayment(order.getPartnerId(), order.getServerId(), port, order.getPartnerUserId(), order.getWaresId(), "", gameOrderId, finishAmount, gold, "", "")) {
				// 如果失败，要把订单置为未支付
				this.paymentOrderDao.updateStatus(gameOrderId, PaymentOrder.STATUS_NOT_PAY, partnerOrderId, finishAmount, extinfo);
				LOGGER.error("发货失败, 支付渠道订单信息 " + payment.printPaymentInfo());
				return false;
			} else {
				LOGGER.info("支付成功, 支付渠道订单信息" + payment.printPaymentInfo());
				
				MycardUnconfirmedOrder unconfirmedOrder = new MycardUnconfirmedOrder((MyCardPayment) payment);

				mycardUnconfirmedOrderQueue.add(unconfirmedOrder);
				boolean isSuccess = myCardUnconfirmedOrderDaoMysqlImpl.add(unconfirmedOrder);
				if (isSuccess == false) {
					LOGGER.error("添加失败[" + Json.toJson(unconfirmedOrder) + "]");
				}
				
				return true;
			}
		}
		
		this.paymentOrderDao.updateStatus(gameOrderId, PaymentOrder.STATUS_ERROR, partnerOrderId, finishAmount, extinfo);
		LOGGER.error("充值失败, 支付渠道订单信息 " + payment.printPaymentInfo());
		
		return false;
	}
	
	private void sendBillConfirm() {
			
			while (true) {
				if (mycardUnconfirmedOrderQueue.isEmpty() == false) {
					
					// 从队列中取出一个等待确认的订单（这时这个订单已经从队列中移除了）
					MycardUnconfirmedOrder order = mycardUnconfirmedOrderQueue.poll();
					
					String sendConfirmParams = order.getSendConfirmParams();
					
					String sendConfirmUrl = MyCardSdk.instance().getSendConfirmUrl();
					
					LOGGER.info("游戏中支付成功, 向 MyCard回传交易数据 - 请求地址[" + sendConfirmUrl + "] 参数[" + sendConfirmParams + "]");
					
					// 一定要设置 header
					// <code>httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");</code>
					String jsonStr = UrlRequestUtils.executeHttpByString(sendConfirmUrl, sendConfirmParams);
					
					LOGGER.info("游戏中支付成功, 向 MyCard回传交易数据 - 返回数据[" + jsonStr + "]");
					
					Map<String, Object> retVal = Json.toObject(jsonStr, Map.class);
					int rc = Integer.parseInt(retVal.get("ResultCode").toString());
					if (rc == 0) {
						// 确认失败，将订单再加入到等待确认的队列中
						mycardUnconfirmedOrderQueue.add(order);
					} else {
						myCardUnconfirmedOrderDaoMysqlImpl.update(order.getMycardOrderId(), MycardUnconfirmedOrder.STATUS_CONFIRMED);
					}
					
				}
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		
	}
	
	public void init() {
		List<MycardUnconfirmedOrder> list = myCardUnconfirmedOrderDaoMysqlImpl.getAllUnConfirmedOrders();
		for (MycardUnconfirmedOrder order : list) {
			mycardUnconfirmedOrderQueue.add(order);
		}
		
		new Thread(new Runnable() {
			public void run() {
				sendBillConfirm();
			}
		}).start();
	}

	public String serverToServerPay(String authCode, String facMemid, String cardId, String cardPwd) {
		
		StringBuffer sb = new StringBuffer();
		sb.append(MyCardSdk.instance().getAuthKeyMyCard());
		sb.append(MyCardSdk.instance().getFacId());
		sb.append(authCode).append(facMemid).append(cardId).append(cardPwd);
		sb.append(MyCardSdk.instance().getAuthKeyCP());
		
		String strToComputeSign = sb.toString();
		String sign = EncryptUtil.getSHA256(strToComputeSign);
		
		LOGGER.debug("MyCard 客户端发送支付请求，计算签名 - 签名使用的字符串[" + strToComputeSign + "] 签名[" + sign + "]");
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("facId", MyCardSdk.instance().getFacId());
		param.put("authCode", authCode);
		param.put("facMemId", facMemid);
		param.put("cardId", cardId);
		param.put("cardPwd", cardPwd);
		param.put("hash", sign);
		
		String url = MyCardSdk.instance().getServerToServerPayUrl();

		String jsonStr = UrlRequestUtils.execute(url, param, UrlRequestUtils.Mode.GET);
		
		LOGGER.info("MyCard server-to-server 支付返回信息[" + jsonStr + "]");

		MyCardServerToServerPayment payment = convertToMyCardServerToServerPayment(jsonStr);
		payment.setCardId(cardId);
		
		if (payment.isPaySucceed() == true) {
			int cardPoint = payment.getCardPoint();
			
			SystemMyCardCardPayment systemMyCardCardPayment = systemMyCardCardPaymentDao.get(String.valueOf(cardPoint));
			
			if (systemMyCardCardPayment != null) {
				payment.setFinishAmount(systemMyCardCardPayment.getTwdAmount());
				payment.setGoldNum(systemMyCardCardPayment.getGoldNum());
			}
			
			super.doPayment(payment);
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", payment.getReturnMsgNo());
		map.put("msg", URLEncoder.encode(payment.getReturnMsg()));
		
		
		return Json.toJson(map);
	}
	
	private MyCardServerToServerPayment convertToMyCardServerToServerPayment(String jsonStr) {
		Map<String, Object> map = Json.toObject(jsonStr, Map.class);
		
		MyCardServerToServerPayment payment = new MyCardServerToServerPayment();
		payment.setCardKind((Integer) map.get("CardKind"));
		payment.setCardPoint((Integer) map.get("CardPoint"));
		payment.setFacTradeSeq((String) map.get("facTradeSeq"));
		payment.setOProjNo((String) map.get("oProjNo"));
		payment.setReturnMsg((String) map.get("ReturnMsg"));
		payment.setReturnMsgNo((Integer) map.get("ReturnMsgNo"));
		payment.setSaveSeq((String) map.get("SaveSeq"));
		
		return payment;
	}

	public void apiPayDoPayment(MyCardApiPayment payment) {
		int cardPoint = payment.getCardPoint();
		
		SystemMyCardCardPayment systemMyCardCardPayment = systemMyCardCardPaymentDao.get(String.valueOf(cardPoint));
		
		payment.setFinishAmount(systemMyCardCardPayment.getTwdAmount());
		payment.setGoldNum(systemMyCardCardPayment.getGoldNum());
		
		super.doPayment(payment);
	}

	@Override
	public OrderBO createOrder(PurchaseInfo order) {
//		order.setQn("mycard");
		OrderBO orderBO = super.createOrder(order);
		return orderBO;
	}

	@Override
	public String getMycardOrders(Date startDate, Date endDate, String mycardNo) {
		List<PaymentOrder> list = null;
		if(startDate != null && endDate != null){
			list = paymentOrderDao.getMycardSuccessByDate(startDate, endDate);
		}else if(!StringUtils.isBlank(mycardNo)){
			list = paymentOrderDao.getMycardSuccessByExtinfo(mycardNo);
		}
		
		StringBuilder sb = new StringBuilder();
		if(list != null && !list.isEmpty()){
			for(PaymentOrder order : list){
				sb.append(order.getExtInfo()).append(",")
				  .append(order.getPartnerUserId()).append(",")
				  .append(order.getPartnerOrderId()).append(",")
				  .append(order.getOrderId()).append(",")
				  .append(order.getUpdatedTime()).append("<BR>");
			}
		}
		return sb.toString();
	}


}
