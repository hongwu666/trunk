package com.lodogame.model;

import java.util.Date;

public class UserBlackRoomLog {

	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 副本类型
	 */
	private int type;
	
	/**
	 * 密室挑战次数
	 */
	private int time;
	
	/**
	 * 密室更新日期
	 */
	private Date updateTime;

	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
}
