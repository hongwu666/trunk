package com.lodogame.model;

public class MysteryMallRefreshPrice implements SystemModel {

	private static final long serialVersionUID = 8881552075168945999L;

	private int mallType;

	private int times;

	private int toolType;

	private int amount;

	public int getMallType() {
		return mallType;
	}

	public void setMallType(int mallType) {
		this.mallType = mallType;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public int getToolType() {
		return toolType;
	}

	public void setToolType(int toolType) {
		this.toolType = toolType;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getListeKey() {
		return null;
	}

	public String getObjKey() {
		return this.mallType + "_" + this.times;
	}

}
