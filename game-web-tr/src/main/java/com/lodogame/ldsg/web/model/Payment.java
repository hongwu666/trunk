package com.lodogame.ldsg.web.model;

import java.math.BigDecimal;

import com.lodogame.ldsg.web.util.Json;

public abstract class Payment {

	public abstract String getGameOrderId();
	public abstract String getPartnerOrderId();
	public abstract boolean isPaySucceed();
	
	/**
	 * 支付方传过来的数据有没有通过验证。不同支付渠道验证的方法不同，因此要为各个支付渠道要实现自己的验证方法
	 * @return
	 */
	public abstract boolean isPaymentDataVerified();
	
	/**
	 * @param orderAmount 生成订单时应该支付的金额
	 */
	public abstract BigDecimal getFinishAmount(BigDecimal gameOrderAmount) throws Exception;
	
	/**
	 * 获取游戏中的套餐id
	 */
	public int getGameWaresId(PaymentOrder order) {
		return order.getWaresId();
	}
	
	/**
	 * 这笔交易可以获得游戏中金币的数量
	 */
	public int getGoldNum(PaymentOrder order) {
		int goldNum = 0;
		
		try {
			goldNum = getFinishAmount(order.getAmount()).intValue();
		} catch (Exception e) {
			goldNum =  0;
		}
		
		return goldNum;
	}

	
	public String printPaymentInfo() {
		return Json.toJson(this);
	}
	
	public String getChannel() {
		return "";
	}
	
	public String getExtInfo() {
		return "";
	}
	
	
}
