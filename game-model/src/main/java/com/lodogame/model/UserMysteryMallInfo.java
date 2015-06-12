package com.lodogame.model;

import java.util.Date;

public class UserMysteryMallInfo {

	private String userId;

	private int mallType;

	private int normalTimes;

	private int times;

	private Date lastRefreshTime;

	private Date updatedTime;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getMallType() {
		return mallType;
	}

	public void setMallType(int mallType) {
		this.mallType = mallType;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public Date getLastRefreshTime() {
		return lastRefreshTime;
	}

	public void setLastRefreshTime(Date lastRefreshTime) {
		this.lastRefreshTime = lastRefreshTime;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public int getNormalTimes() {
		return normalTimes;
	}

	public void setNormalTimes(int normalTimes) {
		this.normalTimes = normalTimes;
	}

}
