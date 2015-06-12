package com.lodogame.model.log;

import java.util.Date;


public class GemAltarUserSellLog {
	private String userId;
	private int stoneId;
	private int gold;
	private Date createTime;
	
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
	public int getGold() {
		return gold;
	}
	public void setGold(int gold) {
		this.gold = gold;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
