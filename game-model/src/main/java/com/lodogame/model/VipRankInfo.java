package com.lodogame.model;

import java.io.Serializable;

public class VipRankInfo implements Serializable{

	private static final long serialVersionUID = 1L;

	private String userId;
	
	private String username;
	
	private int vipLevel;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}

	
	
}
