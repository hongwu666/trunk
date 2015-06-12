package com.lodogame.model;

public class SystemToolDrop implements SystemModel {

	private static final long serialVersionUID = -1537923613223041045L;

	private int toolId;

	private int dropToolType;

	private int dropToolId;

	private int dropToolNum;

	private String dropToolName;

	private int upperNum;

	private int lowerNum;

	private int isPush;

	public int getDropToolType() {
		return dropToolType;
	}

	public void setDropToolType(int dropToolType) {
		this.dropToolType = dropToolType;
	}

	public int getDropToolId() {
		return dropToolId;
	}

	public void setDropToolId(int dropToolId) {
		this.dropToolId = dropToolId;
	}

	public int getDropToolNum() {
		return dropToolNum;
	}

	public void setDropToolNum(int dropToolNum) {
		this.dropToolNum = dropToolNum;
	}

	public int getUpperNum() {
		return upperNum;
	}

	public void setUpperNum(int upperNum) {
		this.upperNum = upperNum;
	}

	public int getLowerNum() {
		return lowerNum;
	}

	public void setLowerNum(int lowerNum) {
		this.lowerNum = lowerNum;
	}

	public String getDropToolName() {
		return dropToolName;
	}

	public void setDropToolName(String dropToolName) {
		this.dropToolName = dropToolName;
	}

	public int getIsPush() {
		return isPush;
	}

	public void setIsPush(int isPush) {
		this.isPush = isPush;
	}

	public int getToolId() {
		return toolId;
	}

	public void setToolId(int toolId) {
		this.toolId = toolId;
	}

	public String getListeKey() {
		return String.valueOf(this.toolId);
	}

	public String getObjKey() {
		return null;
	}

}
