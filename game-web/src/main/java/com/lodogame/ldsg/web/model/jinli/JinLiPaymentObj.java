package com.lodogame.ldsg.web.model.jinli;

import java.math.BigDecimal;

import com.lodogame.ldsg.web.model.PaymentObj;

public class JinLiPaymentObj extends PaymentObj {

	private String content;

	private String sign;

	private String orderId;

	private BigDecimal orderAmount;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public BigDecimal getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(BigDecimal orderAmount) {
		this.orderAmount = orderAmount;
	}

}
