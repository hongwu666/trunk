package com.lodogame.ldsg.web.model.mycard;

import java.util.HashMap;
import java.util.Map;

import com.lodogame.ldsg.web.sdk.mycard.MyCardSdk;
import com.lodogame.ldsg.web.util.EncryptUtil;
import com.lodogame.ldsg.web.util.Json;
import com.sun.xml.bind.v2.model.core.MaybeElement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MycardUnconfirmedOrder {

	public static final int STATUS_CONFIRMED = 0;
	public static final int STATUS_UNCONFIRMED = 1;

	private String mycardOrderId;
	private String authCode;
	private String account;
	private int status;

	public MycardUnconfirmedOrder() {

	}

	public MycardUnconfirmedOrder(MyCardPayment payment) {
		this.mycardOrderId = payment.getMG_TxID();
		this.authCode = payment.getAUTH_CODE();
		this.account = payment.getAccount();
		this.status = MycardUnconfirmedOrder.STATUS_UNCONFIRMED;

	}

	public String getSendConfirmParams() {

		String str = MyCardSdk.instance().getSecurityKey() + this.authCode
				+ this.mycardOrderId + this.account;
		String sign = EncryptUtil.getSHA1(str);

		Map<String, String> map = new HashMap<String, String>();
		map.put("SecurityKey", sign.toLowerCase());
		map.put("AUTH_CODE", this.authCode);
		map.put("MG_TxID", this.mycardOrderId);
		map.put("Account", this.account);

		String params = Json.toJson(map);

		return "DATA=" + params;
	}

	public String getMycardOrderId() {
		return mycardOrderId;
	}

	public void setMycardOrderId(String mycardOrderId) {
		this.mycardOrderId = mycardOrderId;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
