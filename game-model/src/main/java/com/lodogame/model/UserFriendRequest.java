package com.lodogame.model;

import java.util.Date;


public class UserFriendRequest {

	private String userId;
	private String sendUserId;
	private int status;
	private Date createdTime;
	private Date updatedTime;
	
	
	public UserFriendRequest(String userId, String sendUserId, int status) {
		super();
		this.userId = userId;
		this.sendUserId = sendUserId;
		this.status = status;
		this.createdTime = new Date();
		this.updatedTime = new Date();
	}
	

	public UserFriendRequest() {

	}
	
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getSendUserId() {
		return sendUserId;
	}
	public void setSendUserId(String sendUserId) {
		this.sendUserId = sendUserId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public Date getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}
	


	
}
