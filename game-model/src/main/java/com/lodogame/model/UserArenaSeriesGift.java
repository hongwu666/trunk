package com.lodogame.model;

public class UserArenaSeriesGift {
	private String userId;
	private int winCount;
	private int num;
	
	

	public UserArenaSeriesGift() {
		super();
	}

	public UserArenaSeriesGift(String userId, int winCount, int num) {
		super();
		this.userId = userId;
		this.winCount = winCount;
		this.num = num;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getWinCount() {
		return winCount;
	}

	public void setWinCount(int winCount) {
		this.winCount = winCount;
	}
	public void addNum(int v){
		num+=v;
	}
}
