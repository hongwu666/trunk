package com.lodogame.model;

public class SystemPrice implements SystemModel {

	private int type;

	private int times;

	private int toolType;

	private int amount;

	public int getMallType() {
		return type;
	}

	public void setMallType(int mallType) {
		this.type = mallType;
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
		return String.valueOf(type);
	}

	public String getObjKey() {
		return this.type + "_" + this.times;
	}

}
