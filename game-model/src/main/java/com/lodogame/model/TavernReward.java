package com.lodogame.model;

public class TavernReward implements SystemModel {

	private int type;

	private int toolType;

	private int toolId;

	private int toolNum;

	private int lowerNum;

	private int upperNum;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getToolType() {
		return toolType;
	}

	public void setToolType(int toolType) {
		this.toolType = toolType;
	}

	public int getToolId() {
		return toolId;
	}

	public void setToolId(int toolId) {
		this.toolId = toolId;
	}

	public int getToolNum() {
		return toolNum;
	}

	public void setToolNum(int toolNum) {
		this.toolNum = toolNum;
	}

	public int getLowerNum() {
		return lowerNum;
	}

	public void setLowerNum(int lowerNum) {
		this.lowerNum = lowerNum;
	}

	public int getUpperNum() {
		return upperNum;
	}

	public void setUpperNum(int upperNum) {
		this.upperNum = upperNum;
	}

	public String getListeKey() {
		return String.valueOf(type);
	}

	public String getObjKey() {
		return null;
	}

}
