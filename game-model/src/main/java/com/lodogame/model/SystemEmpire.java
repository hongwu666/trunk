package com.lodogame.model;

public class SystemEmpire implements SystemModel{
	/**
	 * 层数
	 */
	private int floor;
	/**
	 * 等级限制
	 */
	private int level;
	/**
	 * 上限位置数
	 */
	private int posNum;
	/**
	 * 占领英雄数量
	 */
	private int heroNum;
	/**
	 * 相关位置收益加成信息
	 */
	private String addInfo;
	/**
	 * 收获描述
	 */
	private String gainDesc;
	/**
	 * 收获
	 */
	private String gain;
	
	public int getFloor() {
		return floor;
	}

	public int getLevel() {
		return level;
	}

	public int getPosNum() {
		return posNum;
	}

	public int getHeroNum() {
		return heroNum;
	}

	public String getAddInfo() {
		return addInfo;
	}

	public String getGainDesc() {
		return gainDesc;
	}

	public String getGain() {
		return gain;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setPosNum(int posNum) {
		this.posNum = posNum;
	}

	public void setHeroNum(int heroNum) {
		this.heroNum = heroNum;
	}

	public void setAddInfo(String addInfo) {
		this.addInfo = addInfo;
	}

	public void setGainDesc(String gainDesc) {
		this.gainDesc = gainDesc;
	}

	public void setGain(String gain) {
		this.gain = gain;
	}

	public String getListeKey() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getObjKey() {
		return String.valueOf(this.floor);
	}

}
