package com.lodogame.ldsg.web.model.diyibo;

public class DiyiboPayRequest {
	
	/**
	 * 时间戳
	 */
	private String id;
	
	/**
	 *  请求服务类型，固定值：djid.user.payconfirm
	 */
	private String service;
	private String sign;
	
	private DiyiboOrderInfo data;

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

	public DiyiboOrderInfo getData() {
		return data;
	}

	public void setData(DiyiboOrderInfo data) {
		this.data = data;
	}
	
	public boolean isPaySucceeded() {
		String orderStatus = this.data.getOrderStatus();
		if (orderStatus.equalsIgnoreCase("S")) {
			return true;
		}
		return false;
	}
}
