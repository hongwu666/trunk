package com.lodogame.model;

import java.io.Serializable;
import java.util.Date;

public class UserRecContestRewardLog implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String userId;
		
	private int rewardId;
	
	private Date createdTime;

	public UserRecContestRewardLog(String userId, int rewardId, Date createdTime){
		this.userId = userId;
		this.rewardId = rewardId;
		this.createdTime = createdTime;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getRewardId() {
		return rewardId;
	}

	public void setRewardId(int rewardId) {
		this.rewardId = rewardId;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

}
