package com.lodogame.ldsg.web.model.lodo;

public class LodoOrderInfo {
	
	private String orderid;
	private String userid;
	
	/**
	 * 支付方式: 1表示支付宝支付
	 */
	private String payWay;
	
	/**
	 * 充值金额
	 */
	private String amount;
	
	/**
	 * 订单状态，0表示支付成功，1：支付失败
	 */
	private int orderStatus;
	
	/**
	 * 失败原因描述
	 */
	private String failedDesc;
	
	/**
	 * 生成订单时客户端发送给 SDK 的内容
	 */
	private String callbackinfo;

	

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
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

	
	public int getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getFailedDesc() {
		return failedDesc;
	}

	public void setFailedDesc(String failedDesc) {
		this.failedDesc = failedDesc;
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
		return "amount=" + amount + "callbackinfo=" + callbackinfo + "failedDesc=" + failedDesc + 
				"orderid=" + orderid + "orderStatus=" + orderStatus + "payWay=" + payWay + "userid=" + userid;
	}
}
