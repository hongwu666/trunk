package com.lodogame.ldsg.web.model.google;

import com.lodogame.ldsg.web.model.PaymentObj;


public class GooglePaymentObj extends PaymentObj {
	private String json;
	
	private String signature;

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}
}
