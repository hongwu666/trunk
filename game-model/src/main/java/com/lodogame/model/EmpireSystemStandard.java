package com.lodogame.model;

public class EmpireSystemStandard implements SystemModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 *  层数
	 */
	private int floor;
	/**
	 *  库总数
	 */
	private int empireNum;
	/**
	 *  等级限制
	 */
	private int limitLevel;
	/**
	 *  出战人数
	 */
	private int heroNum;
	/**
	 *  道具类型
	 */
	private int toolType;
	/**
	 *  道具id
	 */
	private int toolId;
	/**
	 *  道具数量
	 */
	private int toolNum;
	/**
	 *  概率
	 */
	private int rate;

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	public int getEmpireNum() {
		return empireNum;
	}

	public void setEmpireNum(int empireNum) {
		this.empireNum = empireNum;
	}

	public int getLimitLevel() {
		return limitLevel;
	}

	public void setLimitLevel(int limitLevel) {
		this.limitLevel = limitLevel;
	}

	public int getHeroNum() {
		return heroNum;
	}

	public void setHeroNum(int heroNum) {
		this.heroNum = heroNum;
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

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public String getListeKey() {
		return floor+"";
	}

	public String getObjKey() {
		return null;
	}

}
