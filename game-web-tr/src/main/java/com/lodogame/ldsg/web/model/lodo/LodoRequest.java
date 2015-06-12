package com.lodogame.ldsg.web.model.lodo;

import com.google.gson.Gson;
import com.lodogame.ldsg.web.util.Json;

public class LodoRequest {
	
	/**
	 * 时间戳
	 */
	private String id;
	
	private String service;
	private String sign;
	
	private LodoOrderInfo data;

	/**
	 * 用来保存 partnerId
	 */
	private String cpPrivateInfo;
	
	
	
	public String getCpPrivateInfo() {
		return cpPrivateInfo;
	}

	public void setCpPrivateInfo(String cpPrivateInfo) {
		this.cpPrivateInfo = cpPrivateInfo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public LodoOrderInfo getData() {
		return data;
	}

	public void setData(LodoOrderInfo data) {
		this.data = data;
	}
	
	public boolean isPaySucceeded() {
		int orderStatus = this.data.getOrderStatus();
		if (orderStatus == 0) {
			return true;
		}
		return false;
	}
}
