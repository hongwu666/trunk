package com.lodogame.ldsg.web.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付订单
 * 
 * @author jacky
 * 
 */
public class PaymentOrder implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 订单ID
	 */
	private String orderId;
	
	/**
	 * 合作商订单ID
	 */
	private String partnerOrderId;

	/**
	 * 游戏中的商品 id
	 */
	private int waresId;
	
	/**
	 * 序列
	 */
	private int seq;

	/**
	 * 游戏ID
	 */
	private String gameId;

	/**
	 * 服务器ID
	 */
	private String serverId;

	/**
	 * 合作商ID
	 */
	private String partnerId;

	/**
	 * 合作商用户ID
	 */
	private String partnerUserId;

	/**
	 * 状态
	 */
	private int status;

	/**
	 * 创建时间
	 */
	private Date createdTime;

	/**
	 * 充值金额
	 */
	private BigDecimal amount;

	/**
	 * 完成的充值金额
	 */
	private BigDecimal finishAmount;

	/**
	 * 更新时间
	 */
	private Date updatedTime;

	/**
	 * qn
	 */
	private String qn;
	
	

	public PaymentOrder(String orderId, PurchaseInfo order, int orderStatus) {
		super();
		this.orderId = orderId;
		this.gameId = "ltcs";
		this.waresId = Integer.valueOf(order.getWaresId());
		this.serverId = order.getServerId();
		this.partnerId = order.getPartnerId();
		this.partnerUserId = order.getPartnerUserId();
		this.status = orderStatus;
		this.amount = order.getAmount();
		this.qn = order.getQn();
		this.finishAmount = new BigDecimal(0);
		this.seq = 0;
		
		Date now = new Date();
		this.createdTime = now;
		this.updatedTime = now;
	
	}
	
	
	public PaymentOrder() {
		
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public String getPartnerUserId() {
		return partnerUserId;
	}

	public void setPartnerUserId(String partnerUserId) {
		this.partnerUserId = partnerUserId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getPartnerOrderId() {
		return partnerOrderId;
	}

	public void setPartnerOrderId(String partnerOrderId) {
		this.partnerOrderId = partnerOrderId;
	}

	public BigDecimal getFinishAmount() {
		return finishAmount;
	}

	public void setFinishAmount(BigDecimal finishAmount) {
		this.finishAmount = finishAmount;
	}

	public String getQn() {
		return qn;
	}

	public void setQn(String qn) {
		this.qn = qn;
	}


	public int getWaresId() {
		return waresId;
	}


	public void setWaresId(int waresId) {
		this.waresId = waresId;
	}
}
