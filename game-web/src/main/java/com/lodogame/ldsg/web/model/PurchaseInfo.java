package com.lodogame.ldsg.web.model;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class PurchaseInfo {

	private static final Logger LOG = Logger.getLogger(PurchaseInfo.class);

	private String timestamp;
	private String partnerId;
	private String serverId;
	private String partnerUserId;
	private BigDecimal amount;
	private String tradeName;
	private String sign;
	private String roleId;
	private String roleName;

	/**
	 * 商品 id 对应于游戏服中 system_gold_set 这张表中的 system_gold_set_id 字段
	 */
	private String waresId;

	/**
	 * 二级渠道号
	 */
	private String qn;

	public PurchaseInfo(HttpServletRequest req) {
		this.timestamp = req.getParameter("timestamp");
		this.partnerId = req.getParameter("partnerId");
		this.serverId = req.getParameter("serverId");
		this.partnerUserId = req.getParameter("partnerUserId");
		this.amount = new BigDecimal(req.getParameter("amount"));
		this.waresId = req.getParameter("waresId");
		this.tradeName = req.getParameter("tradeName");
		this.sign = req.getParameter("sign");
		this.qn = req.getParameter("esqn");
		this.roleId = req.getParameter("roleId");
		this.roleName = req.getParameter("roleName");
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

	@Override
	public String toString() {
		return "PurchaseInfo [timestamp=" + timestamp + ", partnerId=" + partnerId + ", serverId=" + serverId + ", partnerUserId=" + partnerUserId + ", amount=" + amount
				+ ", waresId=" + waresId + ", tradeName=" + tradeName + ", sign=" + sign + ", qn=" + qn + "]";
	}
}
