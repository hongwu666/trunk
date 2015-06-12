package com.lodogame.model;

import java.io.Serializable;
import java.util.Date;


/**
 * 用户场景军队
 * 
 * @author jacky
 * 
 */
public class UserForces implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 场景ID
	 */
	private int sceneId;

	private int groupId;

	/**
	 * 怪物类型
	 */
	private int forcesType;

	/**
	 * 状态(状态 0 不可攻打 1 可以攻打 2 已经战胜过)
	 */
	private int status;

	/**
	 * 当天已攻打次数
	 */
	private int times;
	
	/**
	 * 当前关卡的过关星级
	 */
	private int passStar;
	

	/**
	 * 创建时间
	 */
	private Date createdTime;

	/**
	 * 更新时间
	 */
	private Date updatedTime;

	
	
	public int getPassStar() {
		return passStar;
	}

	public void setPassStar(int passStar) {
		this.passStar = passStar;
	}

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

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
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

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public int getForcesType() {
		return forcesType;
	}

	public void setForcesType(int forcesType) {
		this.forcesType = forcesType;
	}
	
	/**
	 * 是不是第一支部队
	 */
	public boolean isFirstForces() {
		if (groupId == 1) {
			return true;
		}
		return false;
	}
}
