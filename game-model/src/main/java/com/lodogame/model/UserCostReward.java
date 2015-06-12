package com.lodogame.model;

import java.io.Serializable;
import java.util.Date;

public class UserCostReward implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private String userId;
	private int activityId;
	private int rewardId;
	private Date createdTime;
	private Date updatedTime;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getActivityId() {
		return activityId;
	}

	public void setActivityId(int activityId) {
		this.activityId = activityId;
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

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public UserCostReward(String userId, int activityId, int rewardId) {
		super();
		this.userId = userId;
		this.activityId = activityId;
		this.rewardId = rewardId;
		this.createdTime = new Date();
		this.updatedTime = new Date();
	}

	public UserCostReward() {
		super();
	}

}
