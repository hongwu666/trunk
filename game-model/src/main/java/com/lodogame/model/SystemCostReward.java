package com.lodogame.model;

public class SystemCostReward implements SystemModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;

	/**
	 * 活动ID
	 */
	private int activityId;

	private int gold;

	private String title;

	private String rewards;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getActivityId() {
		return activityId;
	}

	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public String getRewards() {
		return rewards;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setRewards(String rewards) {
		this.rewards = rewards;
	}

	public String getListeKey() {
		return String.valueOf(activityId);
	}

	public String getObjKey() {
		return activityId + "_" + id;
	}

}
