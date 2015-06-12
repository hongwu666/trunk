package com.lodogame.ldsg.web.model.wandoujia;

import com.lodogame.ldsg.web.model.PaymentObj;

public class WandoujiaPaymentObj extends PaymentObj {
	/**
	 * 购买人账户ID
	 */
	private String buyerId;
	/**
	 * 豌豆荚订单ID
	 */
	private String orderId;
	/**
	 * 支付金额，单位（分）
	 */
	private String money;
	/**
	 * 支付类型
	 * ALIPAY：支付宝
	 * SHENZHOUPAY：充值卡
	 * BALANCEPAY：余额
	 * CREDITCARD : 信用卡
	 * DEBITCARD：借记卡
	 */
	private String chargeType;
	/**
	 * 应用ID
	 */
	private String appKeyId;
	/**
	 * 游戏订单号
	 */
	private String out_trade_no;
	/**
	 * 充值卡，只有充值卡充值的时候才不为空
	 */
	private String cardNo;
	/**
	 * 签名
	 */
	private String sign;
	/**
	 * 签名类型，值固定 RSA
	 */
	private String signType;
	
	private String data;
	
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getChargeType() {
		return chargeType;
	}

	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}

	public String getAppKeyId() {
		return appKeyId;
	}

	public void setAppKeyId(String appKeyId) {
		this.appKeyId = appKeyId;
	}

	public String getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}


	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
}
