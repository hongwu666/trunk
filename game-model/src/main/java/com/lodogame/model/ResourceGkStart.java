package com.lodogame.model;

public class ResourceGkStart {
	private int id;
	private int gk;
	private int startLevel;
	private String userId;
	public ResourceGkStart() {
		super();
	}
	public ResourceGkStart(int id, int gk, int startLevel, String userId) {
		super();
		this.id = id;
		this.gk = gk;
		this.startLevel = startLevel;
		this.userId = userId;
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
	public int getGk() {
		return gk;
	}
	public void setGk(int gk) {
		this.gk = gk;
	}
	public int getStartLevel() {
		return startLevel;
	}
	public void setStartLevel(int startLevel) {
		this.startLevel = startLevel;
	}
}
