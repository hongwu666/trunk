package com.lodogame.model;

import java.util.Date;

public class EmpireHistory {
	/**
	 * 用户id
	 */
	private String userId;
	/**
	 * 敌方id
	 */
	private String targetUserId;
	/**
	 * 敌方用户名
	 */
	private String targetUsername;
	/**
	 * 状态，1防守成功，0失败
	 */
	private int state;
	/**
	 * 被掠夺的资源
	 */
	private String toolIds;
	/**
	 * 创建时间
	 */
	private Date createdTime;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getTargetUserId() {
		return targetUserId;
	}
	public void setTargetUserId(String targetUserId) {
		this.targetUserId = targetUserId;
	}
	public String getTargetUsername() {
		return targetUsername;
	}
	public void setTargetUsername(String targetUsername) {
		this.targetUsername = targetUsername;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getToolIds() {
		return toolIds;
	}
	public void setToolIds(String toolIds) {
		this.toolIds = toolIds;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	
	
}
