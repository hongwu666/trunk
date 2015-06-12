package com.lodogame.ldsg.web.model.zhangyue;

import java.math.BigDecimal;
import java.util.Date;

import com.lodogame.ldsg.web.model.PaymentObj;

public class ZhangYuePaymentObj extends PaymentObj {

	private String merId;
	private String appId;
	private String orderId;
	private String merOrderId;
	private String payAmt;
	private String transTime;
	private String orderStatus;
	private String errorCode;
	private String errorMsg;
	private String rechargeType;
	private String extend;
	private String md5SignValue;

	public String getMerId() {
		return merId;
	}

	public void setMerId(String merId) {
		this.merId = merId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getMerOrderId() {
		return merOrderId;
	}

	public void setMerOrderId(String merOrderId) {
		this.merOrderId = merOrderId;
	}

	public String getPayAmt() {
		return payAmt;
	}

	public void setPayAmt(String payAmt) {
		this.payAmt = payAmt;
	}

	public String getTransTime() {
		return transTime;
	}

	public void setTransTime(String transTime) {
		this.transTime = transTime;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getRechargeType() {
		return rechargeType;
	}

	public void setRechargeType(String rechargeType) {
		this.rechargeType = rechargeType;
	}

	public String getExtend() {
		return extend;
	}

	public void setExtend(String extend) {
		this.extend = extend;
	}

	public String getMd5SignValue() {
		return md5SignValue;
	}

	public void setMd5SignValue(String md5SignValue) {
		this.md5SignValue = md5SignValue;
	}

}
