package com.lodogame.ldsg.web.model;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.lodogame.ldsg.web.controller.OrderController;

public class PurchaseInfo {

	private static final Logger LOG = Logger.getLogger(PurchaseInfo.class);

	private String timestamp;
	private String partnerId;
	private String serverId;
	private String partnerUserId;
	private String roleId;
	private String roleName;
	private BigDecimal amount;
	private String tradeName;
	private String sign;
	
	/**
	 * 商品 id 对应于游戏服中 system_gold_set 这张表中的 system_gold_set_id 字段
	 */
	private String waresId;
	
	/**
	 * 二级渠道号
	 */
	private String qn;
	
	/**
	 * 支付渠道id：
	 * <p>
	 *  google：google pay 支付
	 *  
	 *  mycard_mobile：MyCard 话费支付
	 *  mycard_pin：MyCard 点卡支付
	 *  mycard_wallet：MyCard 钱包支付
	 *  mol_pin：MOL 点卡支付
	 *  mol_wallet：MOL 钱包支付
	 * </p>
	 */
	private String payChannelId;

	public PurchaseInfo(HttpServletRequest req) {
		this.timestamp = req.getParameter("timestamp");
		this.partnerId = req.getParameter("partnerId");
		this.serverId = req.getParameter("serverId");
		this.partnerUserId = req.getParameter("partnerUserId");
		this.amount = new BigDecimal(req.getParameter("amount"));
		this.waresId = req.getParameter("waresId");
		this.tradeName = req.getParameter("tradeName");
		this.sign = req.getParameter("sign");
		this.qn = req.getParameter("payChannelId"); // 二级渠道号
		this.roleId = req.getParameter("roleId");
		this.roleName = req.getParameter("roleName");
		this.payChannelId = req.getParameter("payChannelId");
		
		if (qn == null) {
			qn = "";
		}
		
		LOG.info(this.toString());
	}

	public PurchaseInfo() {
		
	}
	
	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public String getPartnerUserId() {
		return partnerUserId;
	}

	public void setPartnerUserId(String partnerUserId) {
		this.partnerUserId = partnerUserId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getWaresId() {
		return waresId;
	}

	public void setWaresId(String waresId) {
		this.waresId = waresId;
	}

	public String getTradeName() {
		return tradeName;
	}

	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getQn() {
		return qn;
	}

	public void setQn(String qn) {
		this.qn = qn;
	}
	
	@Override
	public String toString() {
		return "PurchaseInfo [timestamp=" + timestamp + ", partnerId=" + partnerId
				+ ", serverId=" + serverId + ", partnerUserId=" + partnerUserId
				+ ", amount=" + amount + ", waresId=" + waresId
				+ ", tradeName=" + tradeName + ", sign=" + sign + ", qn=" + qn
				+ "]";
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

	public String getPayChannelId() {
		return payChannelId;
	}

	public void setPayChannelId(String payChannelId) {
		this.payChannelId = payChannelId;
	}
	
	/**
	 * 创建订单时客户端会传一个参数 payChannelId 表示支付的方式。这个参数的格式是：支付渠道_支付方式。
	 * <p>
	 * 例如:
	 * MOL 点卡支付的 payChannelId 是 mol_pin
	 * MyCard 钱包支付的 payChannelId 是 mycard_wallet
	 * Google Pay 支付的 payChannelId 是 google
	 * </p>
	 * 
	 * 在服务端只需要根据支付渠道选择对应的处理逻辑，因此需要从 payChannelId 中分离出支付渠道，即将 payChannelId 按下划线 “_”分隔，区第一个字符串
	 * @return
	 */
	public String getPayChannel() {
		if (payChannelId == null) {
			return "";
		}
		return this.payChannelId.split("_")[0];
	}
}
