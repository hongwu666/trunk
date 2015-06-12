package com.lodogame.ldsg.web.model.oppo;

import com.lodogame.ldsg.web.model.PaymentObj;

public class OppoPaymentObj extends PaymentObj {

	
	private String sdkOrderId;
	private String cpOrderId;
	
	/**
	 *  单价，以分为单位
	 */
	private int price;
	
	/**
	 * 商品数量
	 */
	private int count;
	
	private String extInfo;
	
	private String sign;

	public String getSdkOrderId() {
		return sdkOrderId;
	}

	public void setSdkOrderId(String sdkOrderId) {
		this.sdkOrderId = sdkOrderId;
	}

	public String getCpOrderId() {
		return cpOrderId;
	}

	public void setCpOrderId(String cpOrderId) {
		this.cpOrderId = cpOrderId;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getExtInfo() {
		return extInfo;
	}

	public void setExtInfo(String extInfo) {
		this.extInfo = extInfo;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
	
}
