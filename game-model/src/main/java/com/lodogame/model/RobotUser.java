package com.lodogame.model;

public class RobotUser implements IUser {

	private String userId;

	private int level;

	private int imgId;

	private int capability;

	private long lodoId;

	private String username;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getLevel() {
		if (level > 60) {
			return 60;
		}
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getImgId() {
		return imgId;
	}

	public void setImgId(int imgId) {
		this.imgId = imgId;
	}

	public int getCapability() {
		return capability;
	}

	public void setCapability(int capability) {
		this.capability = capability;
	}

	public void setLodoId(long lodoId) {
		this.lodoId = lodoId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public long getLodoId() {
		return lodoId;
	}

}
