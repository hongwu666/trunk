package com.lodogame.model;

import java.util.Date;

public class UserHeroStoneMallLog {
	private String userId;
	private String exchangeIds;
	private String systemIds;
	private Date createdTime;
	private Date updatedTime;
	
	public Date getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}
	public String getExchangeIds() {
		return exchangeIds;
	}
	public void setExchangeIds(String exchangeIds) {
		this.exchangeIds = exchangeIds;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getSystemIds() {
		return systemIds;
	}
	public void setSystemIds(String systemIds) {
		this.systemIds = systemIds;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	
	
}
