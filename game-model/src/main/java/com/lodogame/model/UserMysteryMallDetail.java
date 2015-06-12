package com.lodogame.model;

import java.util.Date;

public class UserMysteryMallDetail {

	private int id;

	private String userId;

	private int mallType;

	private int dropId;

	private int flag;

	private int amount;

	private Date createdTime;

	public int getMallType() {
		return mallType;
	}

	public void setMallType(int mallType) {
		this.mallType = mallType;
	}

	public int getDropId() {
		return dropId;
	}

	public void setDropId(int dropId) {
		this.dropId = dropId;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
