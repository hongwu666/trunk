package com.lodogame.model;

import java.util.Date;

public class UserGiftLog {

	private String userId;

	private int bigType;

	private String giftCode;

	private Date createdTime;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getBigType() {
		return bigType;
	}

	public void setBigType(int bigType) {
		this.bigType = bigType;
	}

	public String getGiftCode() {
		return giftCode;
	}

	public void setGiftCode(String giftCode) {
		this.giftCode = giftCode;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

}
