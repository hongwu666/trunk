package com.lodogame.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户场景
 * 
 * @author jacky
 * 
 */
public class UserScene implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户Id
	 */
	private String userId;

	/**
	 * 场景ID
	 */
	private int sceneId;

	/**
	 * 通关标志
	 */
	private int passFlag;

	/**
	 * 创建时间
	 */
	private Date createdTime;

	/**
	 * 更新时间
	 */
	private Date updatedTime;

	/**
	 * 一星奖励是否领取
	 */
	private int oneStarReward;
	
	/**
	 * 二星奖励是否领取
	 */
	private int twoStarReward;
	
	/**
	 * 三星奖励是否领取
	 */
	private int thirdStarReward;
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getSceneId() {
		return sceneId;
	}

	public void setSceneId(int sceneId) {
		this.sceneId = sceneId;
	}

	public int getPassFlag() {
		return passFlag;
	}

	public void setPassFlag(int passFlag) {
		this.passFlag = passFlag;
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

	public int getOneStarReward() {
		return oneStarReward;
	}

	public void setOneStarReward(int oneStarReward) {
		this.oneStarReward = oneStarReward;
	}

	public int getTwoStarReward() {
		return twoStarReward;
	}

	public void setTwoStarReward(int twoStarReward) {
		this.twoStarReward = twoStarReward;
	}

	public int getThirdStarReward() {
		return thirdStarReward;
	}

	public void setThirdStarReward(int thirdStarReward) {
		this.thirdStarReward = thirdStarReward;
	}

	
	
}
