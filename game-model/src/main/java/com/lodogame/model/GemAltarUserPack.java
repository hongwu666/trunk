package com.lodogame.model;

import java.util.Date;

public class GemAltarUserPack {

	private String userId;
	private int stoneId;
	private Date createTime;

	
	
	public GemAltarUserPack() {
		super();
	}

	public GemAltarUserPack(String userId, int stoneId, Date createTime) {
		super();
		this.userId = userId;
		this.stoneId = stoneId;
		this.createTime = createTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getStoneId() {
		return stoneId;
	}

	public void setStoneId(int stoneId) {
		this.stoneId = stoneId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
