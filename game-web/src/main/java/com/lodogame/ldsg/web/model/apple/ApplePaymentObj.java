package com.lodogame.ldsg.web.model.apple;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.lodogame.ldsg.web.model.PaymentObj;

public class ApplePaymentObj extends PaymentObj {

	private static final Logger LOG = Logger.getLogger(ApplePaymentObj.class);

	private String appleOrderId;
	private String receipt;
	private String gameOrderId;
	private String money;
	private Map<String, Object> result;
	private AppleOrder appleOrder;
	private String version;

	public ApplePaymentObj() {

	}

	public ApplePaymentObj(HttpServletRequest req) {
		this.receipt = req.getParameter("receiptData");
		this.gameOrderId = req.getParameter("gameOrderId");
		this.appleOrderId = req.getParameter("appOrderId");
		this.version = req.getParameter("version");

		LOG.info(this.toString());
	}

	public String getAppleOrderId() {
		return appleOrderId;
	}

	public void setAppleOrderId(String appleOrderId) {
		this.appleOrderId = appleOrderId;
	}

	public String getGameOrderId() {
		return gameOrderId;
	}

	public void setGameOrderId(String gameOrderId) {
		this.gameOrderId = gameOrderId;
	}

	public String getReceipt() {
		return receipt;
	}

	public void setReceipt(String receipt) {
		this.receipt = receipt;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public Map<String, Object> getResult() {
		return result;
	}

	public void setResult(Map<String, Object> result) {
		this.result = result;
	}

	public AppleOrder getAppleOrder() {
		return appleOrder;
	}

	public void setAppleOrder(AppleOrder appleOrder) {
		this.appleOrder = appleOrder;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "ApplePaymentObj [appleOrderId=" + appleOrderId + ", receipt=" + receipt + ", gameOrderId=" + gameOrderId + ", version=" + version + "]";
	}

	public boolean isValid() {
		if (StringUtils.isNotBlank(receipt) && StringUtils.isNotBlank(gameOrderId) && StringUtils.isNotBlank(appleOrderId)) {
			return true;
		}
		return false;
	}
}
