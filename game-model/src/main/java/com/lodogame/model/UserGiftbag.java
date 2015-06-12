package com.lodogame.model;

import java.util.Date;

public class UserGiftbag {

	private String userId;
	
	private int giftbagId;
	
	private int type;

	private int totalNum;

	private int openNum;

	private Date createdTime = new Date();

	private Date updatedTime= new Date();

	private int remake;
	

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public int getOpenNum() {
		return openNum;
	}

	public void setOpenNum(int openNum) {
		this.openNum = openNum;
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

	public int getGiftbagId() {
		return giftbagId;
	}

	public void setGiftbagId(int giftbagId) {
		this.giftbagId = giftbagId;
	}

	public int getRemake() {
		return remake;
	}

	public void setRemake(int remake) {
		this.remake = remake;
	}

}
