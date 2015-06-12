package com.lodogame.ldsg.web.service.impl;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.ldsg.web.bo.OrderBO;
import com.lodogame.ldsg.web.dao.SystemMOLCardPaymentDao;
import com.lodogame.ldsg.web.exception.ServiceException;
import com.lodogame.ldsg.web.model.Payment;
import com.lodogame.ldsg.web.model.PaymentOrder;
import com.lodogame.ldsg.web.model.PurchaseInfo;
import com.lodogame.ldsg.web.model.UserToken;
import com.lodogame.ldsg.web.model.mol.MOLPayment;
import com.lodogame.ldsg.web.model.mol.SystemMOLCardPayment;
import com.lodogame.ldsg.web.sdk.GameApiSdk;
import com.lodogame.ldsg.web.sdk.easou.AuthAPI;
import com.lodogame.ldsg.web.sdk.easou.service.AuthBean;
import com.lodogame.ldsg.web.sdk.easou.service.EucAPIException;
import com.lodogame.ldsg.web.sdk.easou.service.EucApiResult;
import com.lodogame.ldsg.web.sdk.easou.service.JUser;
import com.lodogame.ldsg.web.service.BasePartnerService;
import com.lodogame.ldsg.web.service.WebApiService;

public class MOLServiceImpl extends BasePartnerService {
	
	private static final Logger LOGGER = Logger.getLogger(MOLServiceImpl.class); 
	
	@Autowired
	private SystemMOLCardPaymentDao systemMOLCardPaymentDao;
	
	
	@Override
	public UserToken login(String token, String partnerId, String serverId, long timestamp, String sign, Map<String, String> params) {
		if (StringUtils.isBlank(token) || StringUtils.isBlank(partnerId) || StringUtils.isBlank(serverId) || timestamp == 0 || StringUtils.isBlank(sign)) {
			throw new ServiceException(WebApiService.PARAM_ERROR, "参数不正确");
		}

//		checkSign(token, partnerId, serverId, timestamp, sign);
		try {
		EucApiResult<AuthBean> result = AuthAPI.validate(token, null, null);
			JUser juser = null;
			if (result == null || result.getResult() == null) {
				if (juser == null) {
					throw new ServiceException(WebApiService.LOGIN_VALID_FAILD, "登录验证失败");
				}
			}
			juser = result.getResult().getUser();
			int port = serverService.getServerHttpPort(serverId);
			return GameApiSdk.getInstance().loadUserToken(juser.getId().toString(), partnerId, serverId, port, juser.getQn(), params);

			
		} catch (EucAPIException e) {
			throw new ServiceException(WebApiService.LOGIN_VALID_FAILD, "登录验证失败");
		}
	}
	
	@Override
	public boolean doPayment(Payment payment) {
		if (payment == null) {
			LOGGER.error("payment 为空");
			return false;
		}
		
		MOLPayment molPayment = (MOLPayment) payment;
		
		if (molPayment.isPaymentDataVerified() == false) {
			LOGGER.error("支付数据验证失败");
			return false;
		}
		
		PaymentOrder order = paymentOrderDao.get(molPayment.getGameOrderId());
		
		LOGGER.info("游戏中订单信息 " + order.printPaymentOrderInfo());
		
		if (order.isOrderFinished() == true) {
			LOGGER.error("订单已经完成, 支付渠道订单信息 " + molPayment.printPaymentInfo());
			return false;
		}
		
		
		String gameOrderId = order.getOrderId();
		String partnerOrderId = molPayment.getPartnerOrderId();
		BigDecimal finishAmount;

		try {
			finishAmount = this.getFinishAmount(molPayment, order);
		} catch (Exception e) {
			LOGGER.error("游戏中订单和支付渠道的订单金额不符, 支付渠道订单信息" + payment.printPaymentInfo());
			return false;
		}
		
		int gold = this.getGoldNum(molPayment, order);
		String partnerUserId = order.getPartnerUserId();
		int waresId = this.getGameWaresId(molPayment, order);
		String channel = molPayment.getChannel();
		String extInfo = channel + ":" + molPayment.getCurrencyCode();
		
		// 更新订单状态
		if (this.paymentOrderDao.updateStatus(gameOrderId, PaymentOrder.STATUS_FINISH, partnerOrderId, finishAmount, extInfo)) {
			int port = serverService.getServerHttpPort(order.getServerId());
			
			// 请求游戏服，发放游戏货币
			if (!GameApiSdk.getInstance().doPayment(order.getPartnerId(), order.getServerId(), port, partnerUserId, waresId, channel, gameOrderId, finishAmount, gold, "", "")) {
			
				// 如果失败，要把订单置为未支付
				this.paymentOrderDao.updateStatus(gameOrderId, PaymentOrder.STATUS_NOT_PAY, partnerOrderId, finishAmount, extInfo);
				LOGGER.error("向游戏服中发放游戏货币失败, 支付渠道订单信息 " + payment.printPaymentInfo());
				return false;
			} else {
				LOGGER.info("支付成功, 支付渠道订单信息" + payment.printPaymentInfo());
				return true;
			}
		}
		
		this.paymentOrderDao.updateStatus(gameOrderId, PaymentOrder.STATUS_ERROR, partnerOrderId, finishAmount, extInfo);
		LOGGER.error("充值失败, 支付渠道订单信息 " + payment.printPaymentInfo());
		
		return false;
	}

	
	private int getGameWaresId(MOLPayment molPayment, PaymentOrder order) {
		if (molPayment.getChannelId() == MOLPayment.PAY_WAY_CARD) {
			
			double amountInYuan = molPayment.getAmountInYuan().doubleValue();
			SystemMOLCardPayment molPaymentSetting = systemMOLCardPaymentDao.get(molPayment.getCurrencyCode(), amountInYuan);
			return Integer.valueOf(molPaymentSetting.getWaresId());
		}
		
		return order.getWaresId();
	}
	
	
	
	private int getGoldNum(MOLPayment molPayment, PaymentOrder order) {
		
		BigDecimal amountInYuan = molPayment.getAmountInYuan();
		
		if (molPayment.getChannelId() == MOLPayment.PAY_WAY_CARD) {
			
			SystemMOLCardPayment molPaymentSetting = systemMOLCardPaymentDao.get(molPayment.getCurrencyCode(), amountInYuan.doubleValue());
			if (molPaymentSetting == null) {
				return 0;
			}
			
			return molPaymentSetting.getGoldNum();
		}
		
		return amountInYuan.intValue();
	}
	
	
	private BigDecimal getFinishAmount(MOLPayment molPayment, PaymentOrder order) throws Exception {
		BigDecimal gameOrderAmount = order.getAmount();
		BigDecimal finishAmountInYuan = molPayment.getAmountInYuan();
		
		if (molPayment.getChannelId() == MOLPayment.PAY_WAY_CARD) {
			
			SystemMOLCardPayment molPaymentSetting = systemMOLCardPaymentDao.get(molPayment.getCurrencyCode(), finishAmountInYuan.doubleValue());
			if (molPaymentSetting == null) {
				LOGGER.error("MOL 支付失败, 支付方式[点卡充值], 支付金额[" + finishAmountInYuan + "], 失败原因[没有对应的点卡套餐]");
				throw new Exception();
			} else {

				return finishAmountInYuan;
						
			}

		} else if (finishAmountInYuan.compareTo(gameOrderAmount) == 0) {
			return finishAmountInYuan;
		}
		
		LOGGER.error("游戏中订单和支付渠道的订单金额不符, 支付渠道订单信息" + molPayment.printPaymentInfo());
		throw new Exception();

	}
	
	
	@Override
	public OrderBO createOrder(PurchaseInfo order) {
//		order.setQn("mol");
		OrderBO info = super.createOrder(order);
		info.setPrice(Integer.toString(new BigDecimal(100).multiply(new BigDecimal(info.getPrice())).intValue()));
		
		return info;
	}



}
