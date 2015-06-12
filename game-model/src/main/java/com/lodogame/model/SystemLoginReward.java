package com.lodogame.model;

public class SystemLoginReward implements SystemModel {

	private int day;

	private String toolIds;

	private String toolDesc;

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public String getToolIds() {
		return toolIds;
	}

	public void setToolIds(String toolIds) {
		this.toolIds = toolIds;
	}

	public String getToolDesc() {
		return toolDesc;
	}

	public void setToolDesc(String toolDesc) {
		this.toolDesc = toolDesc;
	}

	public String getListeKey() {
		return null;
	}

	public String getObjKey() {
		return String.valueOf(day);
	}

}
