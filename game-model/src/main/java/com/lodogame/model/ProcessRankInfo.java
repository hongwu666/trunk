package com.lodogame.model;

import java.io.Serializable;

public class ProcessRankInfo implements Serializable{

	private static final long serialVersionUID = 1L;

	private String userId;
	
	private String username;
	
	private int vip;
	
	private double proce;

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

	public int getVip() {
		return vip;
	}

	public void setVip(int vip) {
		this.vip = vip;
	}

	public double getProce() {
		return proce;
	}

	public void setProce(double proce) {
		this.proce = proce;
	}
	
	
	
}
