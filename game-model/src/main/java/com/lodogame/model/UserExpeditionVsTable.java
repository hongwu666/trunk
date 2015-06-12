package com.lodogame.model;

public class UserExpeditionVsTable {
	private String userId;
	private int index;
	private long exId;
	private int stat;
	private int boxStat;

	
	
	public UserExpeditionVsTable() {
		super();
	}

	public UserExpeditionVsTable(String userId, int index, long exId, int stat, int boxStat) {
		super();
		this.userId = userId;
		this.index = index;
		this.exId = exId;
		this.stat = stat;
		this.boxStat = boxStat;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public long getExId() {
		return exId;
	}

	public void setExId(long exId) {
		this.exId = exId;
	}

	public int getStat() {
		return stat;
	}

	public void setStat(int stat) {
		this.stat = stat;
	}

	public int getBoxStat() {
		return boxStat;
	}

	public void setBoxStat(int boxStat) {
		this.boxStat = boxStat;
	}

}
