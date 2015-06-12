package com.lodogame.model;

import java.util.Date;

/**
 * 用户聚宝盆信息
 * 
 * @author
 * 
 */
public class TreasureUserInfo {
	/*
	 * user_id VARCHAR(200) , treasure_type INT COMMENT '宝盆类型', treasure_num INT
	 * COMMENT '打开次数' crated_time DATETIME
	 */
	private String userId;
	private int treasureType;
	private int treasureNum;
	private String date;
	private Date createdTime;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getTreasureType() {
		return treasureType;
	}

	public void setTreasureType(int treasureType) {
		this.treasureType = treasureType;
	}

	public int getTreasureNum() {
		return treasureNum;
	}

	public void setTreasureNum(int treasureNum) {
		this.treasureNum = treasureNum;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public static TreasureUserInfo create(String userId, int type) {
		TreasureUserInfo info = new TreasureUserInfo();
		info.setCreatedTime(new Date());
		info.setUserId(userId);
		info.setTreasureType(type);
		return info;
	}
}
