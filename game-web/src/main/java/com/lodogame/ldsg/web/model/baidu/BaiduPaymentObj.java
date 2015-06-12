package com.lodogame.ldsg.web.model.baidu;

import java.math.BigDecimal;
import java.util.Date;

import com.lodogame.ldsg.web.model.PaymentObj;

public class BaiduPaymentObj extends PaymentObj {
	private int appId;
	private String orderSerial;
	private String cooperatorOrderSerial;
	private String sign;
	private String uid;
	private String merchandiseName;
	private BigDecimal orderMoney;
	private Date startDateTime;
	private Date bankDateTime;
	private int orderStatus;
	private String statusMsg;
	private String extInfo;
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public String getOrderSerial() {
		return orderSerial;
	}

	public void setOrderSerial(String orderSerial) {
		this.orderSerial = orderSerial;
	}

	public String getCooperatorOrderSerial() {
		return cooperatorOrderSerial;
	}

	public void setCooperatorOrderSerial(String cooperatorOrderSerial) {
		this.cooperatorOrderSerial = cooperatorOrderSerial;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getMerchandiseName() {
		return merchandiseName;
	}

	public void setMerchandiseName(String merchandiseName) {
		this.merchandiseName = merchandiseName;
	}

	public BigDecimal getOrderMoney() {
		return orderMoney;
	}

	public void setOrderMoney(BigDecimal orderMoney) {
		this.orderMoney = orderMoney;
	}

	public Date getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}

	public Date getBankDateTime() {
		return bankDateTime;
	}

	public void setBankDateTime(Date bankDateTime) {
		this.bankDateTime = bankDateTime;
	}

	public int getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getStatusMsg() {
		return statusMsg;
	}

	public void setStatusMsg(String statusMsg) {
		this.statusMsg = statusMsg;
	}

	public String getExtInfo() {
		return extInfo;
	}

	public void setExtInfo(String extInfo) {
		this.extInfo = extInfo;
	}
}
