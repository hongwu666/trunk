package com.lodogame.model;

public class GemAltarUserOpen {
	private String userId;
	private int groups;
	
	

	public GemAltarUserOpen() {
		super();
	}

	public GemAltarUserOpen(String userId, int groups) {
		super();
		this.userId = userId;
		this.groups = groups;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getGroups() {
		return groups;
	}

	public void setGroups(int groups) {
		this.groups = groups;
	}

}
