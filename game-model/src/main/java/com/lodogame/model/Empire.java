package com.lodogame.model;

import java.util.Date;

/**
 * 帝国宝库
 * 
 * @author Administrator
 *
 */
public class Empire {
	/**
	 * 层数
	 */
	private int floor;
	/**
	 * 位置
	 */
	private int pos;
	/**
	 * 用户id
	 */
	private String userId;
	/**
	 * 英雄ID
	 */
	private String userHeroId;
	/**
	 * 英雄站位
	 */
	private int heroPos;
	/**
	 * 创建时间
	 */
	private Date createdTime;
	/**
	 * 下一次结算时间
	 */
	private Date nextCountTime;
	/**
	 * 自动结算时间
	 */
	private Date endCountTime;

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserHeroId() {
		return userHeroId;
	}

	public void setUserHeroId(String userHeroId) {
		this.userHeroId = userHeroId;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getNextCountTime() {
		return nextCountTime;
	}

	public void setNextCountTime(Date nextCountTime) {
		this.nextCountTime = nextCountTime;
	}

	public Date getEndCountTime() {
		return endCountTime;
	}

	public void setEndCountTime(Date endCountTime) {
		this.endCountTime = endCountTime;
	}

	public int getHeroPos() {
		return heroPos;
	}

	public void setHeroPos(int heroPos) {
		this.heroPos = heroPos;
	}

}
