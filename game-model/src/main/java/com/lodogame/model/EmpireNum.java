package com.lodogame.model;

public class EmpireNum {

	private String userId;
	
	private int num;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public EmpireNum(String userId, int num) {
		super();
		this.userId = userId;
		this.num = num;
	}
	
}
