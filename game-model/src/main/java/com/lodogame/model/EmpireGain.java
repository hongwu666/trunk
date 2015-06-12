package com.lodogame.model;


public class EmpireGain {

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
	 * 道具类型
	 */
	private int toolType;
	/**
	 * 道具id
	 */
	private int toolId;
	/**
	 * 道具数量
	 */
	private int toolNum;
	
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

}
