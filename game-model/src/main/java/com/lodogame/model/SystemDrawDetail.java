package com.lodogame.model;

public class SystemDrawDetail implements SystemModel, RollAble {

	private static final long serialVersionUID = -5484749900411033432L;

	/**
	 * 抽奖主表ID
	 */
	private int systemDrawId;

	/**
	 * 道具类型
	 */
	private int toolType;

	/**
	 * 道具ID
	 */
	private int toolId;

	/**
	 * 道具数量
	 */
	private int toolNum;

	/**
	 * 是否展示
	 */
	private int display;

	/**
	 * 掉落概论
	 */
	private int rate;

	public int getSystemDrawId() {
		return systemDrawId;
	}

	public void setSystemDrawId(int systemDrawId) {
		this.systemDrawId = systemDrawId;
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

	public int getDisplay() {
		return display;
	}

	public void setDisplay(int display) {
		this.display = display;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public int getRate() {
		return this.rate;
	}

	public String getListeKey() {
		return String.valueOf(this.systemDrawId);
	}

	public String getObjKey() {
		return null;
	}

}
