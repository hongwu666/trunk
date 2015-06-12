package com.lodogame.model;

public class MeridianNodeConfig implements SystemModel {

	private static final long serialVersionUID = 1L;

	private int nodeId;

	private int meridian;

	private int level;

	private int value;

	private int goldNeed;

	private int muhonNeed;

	private int increment;

	private int upgradeRequired;

	public int getGoldNeed() {
		return goldNeed;
	}

	public void setGoldNeed(int goldNeed) {
		this.goldNeed = goldNeed;
	}

	public int getNodeId() {
		return nodeId;
	}

	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}

	public int getMeridian() {
		return meridian;
	}

	public void setMeridian(int meridian) {
		this.meridian = meridian;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getIncrement() {
		return increment;
	}

	public void setIncrement(int increment) {
		this.increment = increment;
	}

	public int getUpgradeRequired() {
		return upgradeRequired;
	}

	public int getMuhonNeed() {
		return muhonNeed;
	}

	public void setMuhonNeed(int muhonNeed) {
		this.muhonNeed = muhonNeed;
	}

	public void setUpgradeRequired(int upgradeRequired) {
		this.upgradeRequired = upgradeRequired;
	}

	public String getListeKey() {
		return null;
	}

	public String getObjKey() {
		return this.nodeId + "_" + this.level;
	}

}
