package com.lodogame.model;

public class SystemDraw implements SystemModel {

	/**
	 * id
	 */
	private int id;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 货币类型
	 */
	private int moneyType;

	/**
	 * 数量
	 */
	private int toolNum;

	/**
	 * 免费次数
	 */
	private int freeTimes;

	/**
	 * 道具ID
	 */
	private int toolId;

	/**
	 * 积分
	 */
	private int point;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getMoneyType() {
		return moneyType;
	}

	public void setMoneyType(int moneyType) {
		this.moneyType = moneyType;
	}

	public int getFreeTimes() {
		return freeTimes;
	}

	public void setFreeTimes(int freeTimes) {
		this.freeTimes = freeTimes;
	}

	public int getToolId() {
		return toolId;
	}

	public void setToolId(int toolId) {
		this.toolId = toolId;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getToolNum() {
		return toolNum;
	}

	public void setToolNum(int toolNum) {
		this.toolNum = toolNum;
	}

	public String getListeKey() {
		return null;
	}

	public String getObjKey() {
		return String.valueOf(id);
	}
}
