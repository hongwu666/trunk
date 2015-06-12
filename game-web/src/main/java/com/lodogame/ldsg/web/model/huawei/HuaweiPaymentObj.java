package com.lodogame.ldsg.web.model.huawei;

import java.util.Map;

import com.lodogame.ldsg.web.model.PaymentObj;


public class HuaweiPaymentObj extends PaymentObj {
	
	private Map<String, Object> params;


	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
	
	
}
