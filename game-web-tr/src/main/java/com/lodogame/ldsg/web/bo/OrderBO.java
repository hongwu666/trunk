package com.lodogame.ldsg.web.bo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.lodogame.ldsg.web.model.PurchaseInfo;
import com.lodogame.ldsg.web.model.PaymentOrder;

/**
 * 封装了创建订单后返回给客户端的信息
 * @author chengevo
 *
 */
public class OrderBO {

	/**
	 * 合作商的用户id 
	 */
	private String userId;
	
	/**
	 * 创建订单是，保存在登陆服 payment_order 表中的订单id
	 */
	private String gameOrderId;
	
	/**
	 * 购买的元宝套餐价格，人民币
	 */
	private String price;
	private String quantity;
	
	/**
	 * 存放了 orderId, 和 gameOrderId 的内容一样
	 */
	private String privateInfo;
	
	/**
	 * 创建订单成功后，调用这个向游戏服中发放元宝
	 */
	private String notifyUrl;
	
	/**
	 * 商品 id 对应于游戏服中 system_gold_set 这张表中的 system_gold_set_id 字段
	 */
	private String waresId;
	
	/**
	 * 角色ID
	 */
	private String roleId;
	
	/**
	 * 角色名称
	 */
	private String roleName;
	
	private String productName;
	/**
	 * 状态码
	 */
	private int rc;

	public OrderBO(PaymentOrder paymentOrder, PurchaseInfo order) {
		this.userId = paymentOrder .getPartnerUserId();
		this.gameOrderId = paymentOrder.getOrderId();
		this.price = String.valueOf(paymentOrder.getAmount());
		this.quantity = "1";
		this.privateInfo = paymentOrder.getOrderId();
		this.waresId = "fshx_tw_" + order.getWaresId();
		this.roleId = order.getRoleId();
		this.roleName = order.getRoleName();
		try {
			this.productName = URLEncoder.encode(order.getTradeName(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
	}
	
	public OrderBO() {
		
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getGameOrderId() {
		return gameOrderId;
	}

	public void setGameOrderId(String gameOrderId) {
		this.gameOrderId = gameOrderId;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getPrivateInfo() {
		return privateInfo;
	}

	public void setPrivateInfo(String privateInfo) {
		this.privateInfo = privateInfo;
	}

	/**
	 * 获取充值回调地址
	 */
	public String getNotifyUrl() {
		return notifyUrl;
	}

	/**
	 * 设置充值回调地址
	 */
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getWaresId() {
		return waresId;
	}

	public void setWaresId(String waresId) {
		this.waresId = waresId;
	}

	public int getRc() {
		return rc;
	}

	public void setRc(int rc) {
		this.rc = rc;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

}
