package com.lodogame.model;

import java.util.Date;

/**
 * 好友点赞
 * @author chengevo
 *
 */
public class UserPraise {
	
	private String userId;
	
	/**
	 * 被点赞的玩家id
	 */
	private String praisedUserId;
	private Date createdTime;
	private Date updatedTime;
	
	
	public UserPraise(String uid, String praisedUserId) {
		this.userId = uid;
		this.praisedUserId = praisedUserId;
		Date now = new Date();
		this.createdTime = now;
		this.updatedTime = now;
	}
	
	public UserPraise() {
		
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPraisedUserId() {
		return praisedUserId;
	}
	public void setPraisedUserId(String praisedUserId) {
		this.praisedUserId = praisedUserId;
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
