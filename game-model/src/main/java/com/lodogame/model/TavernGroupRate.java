package com.lodogame.model;

public class TavernGroupRate implements SystemModel{
	/**
	 * 
	 */
	private int id;
	
	private static final long serialVersionUID = 1L;
	/**
	 * 抽将类型
	 */
	private int type;
	/**
	 * 分组id
	 */
	private int groupId;
	/**
	 * 随机下限
	 */
	private int lowerNum;
	/**
	 * 随机上限
	 */
	private int upperNum;
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
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
