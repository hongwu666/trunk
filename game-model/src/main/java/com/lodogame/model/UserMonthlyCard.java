package com.lodogame.model;

import java.util.Date;

public class UserMonthlyCard {

	private String userId;
	private Date createdTime;
	private Date updatedTime;
	private Date dueTime;
	
	public UserMonthlyCard() {
		
	}
	
	public UserMonthlyCard(String userId, Date dueTime) {
		this.userId = userId;
		
		Date now = new Date();
		this.createdTime = now;
		this.updatedTime = now;
		this.dueTime = dueTime;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
	public Date getDueTime() {
		return dueTime;
	}
	public void setDueTime(Date dueTime) {
		this.dueTime = dueTime;
	}
	
	public boolean isExpires() {
		Date now = new Date();
		if (now.after(dueTime)) {
			return true;
		}
		return false;
	}
	
}
