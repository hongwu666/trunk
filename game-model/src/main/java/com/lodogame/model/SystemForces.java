package com.lodogame.model;

import java.io.Serializable;

public class SystemForces implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 部队ID
	 */
	private int forcesId;

	/**
	 * 关卡ID
	 */
	private int sceneId;

	/**
	 * 每一个关卡要攻打多个怪物，用这个字段区分每个怪物属于这个场景中的第几组
	 */
	private int groupId;

	/**
	 * 这个怪物在这一组中出现的顺序
	 */
	private int orderInGroup;

	/**
	 * 这个怪物是不是这一组中最后一个怪物
	 */
	private int isLast;

	/**
	 * 掉格道具
	 */
	private String dropToolList;

	/**
	 * 部队名字
	 */
	private String forcesName;

	/**
	 * 部队类型
	 * 1. 普通
	 * 2. 精英
	 * 3. 活动/资源
	 * 4. 未知
	 */
	private int forcesType;

	/**
	 * 部队等级
	 */
	private int forcesLevel;

	/**
	 * 前置部队
	 */
	private int preForcesId;

	/*
	 * 前置部队2
	 */
	private int preForcesIdb;

	/**
	 * 次数限制
	 */
	private int timesLimit;

	/**
	 * 是否关卡里最后一个怪
	 */
	private int isSceneLast;

	/**
	 * 需要体力
	 */
	private int needPower;

	public int getForcesId() {
		return forcesId;
	}

	public void setForcesId(int forcesId) {
		this.forcesId = forcesId;
	}

	public int getSceneId() {
		return sceneId;
	}

	public void setSceneId(int sceneId) {
		this.sceneId = sceneId;
	}

	public String getDropToolList() {
		return dropToolList;
	}

	public void setDropToolList(String dropToolList) {
		this.dropToolList = dropToolList;
	}

	public String getForcesName() {
		return forcesName;
	}

	public void setForcesName(String forcesName) {
		this.forcesName = forcesName;
	}

	public int getForcesLevel() {
		return forcesLevel;
	}

	public void setForcesLevel(int forcesLevel) {
		this.forcesLevel = forcesLevel;
	}

	public int getPreForcesId() {
		return preForcesId;
	}

	public void setPreForcesId(int preForcesId) {
		this.preForcesId = preForcesId;
	}

	public int getForcesType() {
		return forcesType;
	}

	public void setForcesType(int forcesType) {
		this.forcesType = forcesType;
	}

	public int getNeedPower() {
		return needPower;
	}

	public void setNeedPower(int needPower) {
		this.needPower = needPower;
	}

	public int getPreForcesIdb() {
		return preForcesIdb;
	}

	public void setPreForcesIdb(int preForcesIdb) {
		this.preForcesIdb = preForcesIdb;
	}

	public int getTimesLimit() {
		return timesLimit;
	}

	public void setTimesLimit(int timesLimit) {
		this.timesLimit = timesLimit;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public int getOrderInGroup() {
		return orderInGroup;
	}

	public void setOrderInGroup(int orderInGroup) {
		this.orderInGroup = orderInGroup;
	}

	public int getIsLast() {
		return isLast;
	}

	public void setIsLast(int isLast) {
		this.isLast = isLast;
	}

	public boolean isLastForceInTheGroup() {
		if (isLast == 0) {
			return false;
		}
		return true;
	}

	public boolean isAttackTimeUnlimted() {
		if (timesLimit == 0) {
			return true;
		}

		return false;
	}

	public boolean isFirstFightInGroup() {
		if (orderInGroup == 1) {
			return true;
		}
		return false;
	}

	public int getIsSceneLast() {
		return isSceneLast;
	}

	public void setIsSceneLast(int isSceneLast) {
		this.isSceneLast = isSceneLast;
	}

}
