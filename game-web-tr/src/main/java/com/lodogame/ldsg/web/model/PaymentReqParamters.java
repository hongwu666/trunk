package com.lodogame.ldsg.web.model;

import java.util.HashMap;
import java.util.Map;

public class PaymentReqParamters {
	private String invoice;
	private String tradeId;
	private String paidFee;
	private String tradeStatus;
	private String payerId;
	private String appId;
	private String reqFee;
	private String sign;
	private String tradeName;
	private String notifyDatetime;
	private String partnerId;
	
	public PaymentReqParamters() {
		
	}
	
	public PaymentReqParamters(String invoice, String tradeId, String paidFee, String tradeStatus, String payerId, String appId, String reqFee,
			String sign, String tradeName, String notifyDatetime, String partnerId) {
		super();
		this.invoice = invoice;
		this.tradeId = tradeId;
		this.paidFee = paidFee;
		this.tradeStatus = tradeStatus;
		this.payerId = payerId;
		this.appId = appId;
		this.reqFee = reqFee;
		this.sign = sign;
		this.tradeName = tradeName;
		this.notifyDatetime = notifyDatetime;
		this.partnerId = partnerId;
	}

	@Override
	public String toString() {
		return "PaymentReqParamters [invoice=" + invoice + ", tradeId=" + tradeId + ", paidFee=" + paidFee + ", tradeStatus=" + tradeStatus
				+ ", payerId=" + payerId + ", appId=" + appId + ", reqFee=" + reqFee + ", sign=" + sign + ", tradeName=" + tradeName
				+ ", notifyDatetime=" + notifyDatetime + ", partnerId=" + partnerId + "]";
	}

	public String getInvoice() {
		return invoice;
	}
	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}
	public String getTradeId() {
		return tradeId;
	}
	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}
	public String getPaidFee() {
		return paidFee;
	}
	public void setPaidFee(String paidFee) {
		this.paidFee = paidFee;
	}
	public String getTradeStatus() {
		return tradeStatus;
	}
	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}
	public String getPayerId() {
		return payerId;
	}
	public void setPayerId(String payerId) {
		this.payerId = payerId;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getReqFee() {
		return reqFee;
	}
	public void setReqFee(String reqFee) {
		this.reqFee = reqFee;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getTradeName() {
		return tradeName;
	}
	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}
	public String getNotifyDatetime() {
		return notifyDatetime;
	}
	public void setNotifyDatetime(String notifyDatetime) {
		this.notifyDatetime = notifyDatetime;
	}
	public String getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}
	
	public Map<String, String> getPropsToCreateSign() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("invoice", this.invoice);
		map.put("tradeId", this.tradeId);
		map.put("paidFee", this.paidFee);
		map.put("tradeStatus", this.tradeStatus);
		map.put("payerId", this.payerId);
		map.put("appId", this.appId);
		map.put("reqFee", this.reqFee);
		map.put("tradeName", this.tradeName);
		map.put("notifyDatetime", this.notifyDatetime);
		
		return map;
	}

}
