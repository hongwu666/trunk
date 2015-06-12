package com.lodogame.model.log;

import java.util.Date;

public class ContestRewardLog implements ILog {

	private String userId;

	private int giftBagId;

	private int week;

	private Date createdTime;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getGiftBagId() {
		return giftBagId;
	}

	public void setGiftBagId(int giftBagId) {
		this.giftBagId = giftBagId;
	}

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

}
