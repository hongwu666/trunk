package com.lodogame.ldsg.web.model.diyibo;

public class DiyiboOrderInfo {
	
	private String orderId;
	private String userId;
	
	/**
	 * 支付方式: 1表示支付宝支付
	 */
	private String payWay;
	
	/**
	 * 充值金额
	 */
	private String amount;
	
	/**
	 * 订单状态，S表示支付成功，F：支付失败
	 */
	private String orderStatus;
	
	/**
	 * 失败原因描述
	 */
	private String faildDesc;
	
	/**
	 * 生成订单时客户端发送给 SDK 的内容
	 */
	private String callbackinfo;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPayWay() {
		return payWay;
	}

	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getFaildDesc() {
		return faildDesc;
	}

	public void setFaildDesc(String faildDesc) {
		this.faildDesc = faildDesc;
	}

	public String getCallbackinfo() {
		return callbackinfo;
	}

	public void setCallbackinfo(String callbackinfo) {
		this.callbackinfo = callbackinfo;
	}

	/**
	 * 签名计算方法：amount={0}callbackinfo={1}failedDesc={2}orderId={3}orderStatus={4}payWay={5}userId={6}
	 * 放括号中是那个属性对应的值
	 * @return
	 */
	public String getSign() {
		return "amount=" + amount + "callbackinfo=" + callbackinfo + "failedDesc=" + faildDesc + 
				"orderId=" + orderId + "orderStatus=" + orderStatus + "payWay=" + payWay + "userId=" + userId;
	}
}
