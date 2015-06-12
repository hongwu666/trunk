package com.lodogame.model;

public class SystemEquipRefine implements SystemModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;

	/**
	 * 装备类型
	 */
	private int equipType;
	/**
	 * 装备职业
	 */
	private int career;
	/**
	 * 精炼点
	 */
	private int refinePoint;
	/**
	 * 精炼等级
	 */
	private int refineLevel;
	/**
	 * 加成属性类型
	 */
	private int proType;
	/**
	 * 加成属性值
	 */
	private int proValue;
	/**
	 * 消耗
	 */
	private String cost;
	
	public int getCareer() {
		return career;
	}
	public void setCareer(int career) {
		this.career = career;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getEquipType() {
		return equipType;
	}
	public void setEquipType(int equipType) {
		this.equipType = equipType;
	}
	public int getRefinePoint() {
		return refinePoint;
	}
	public void setRefinePoint(int refinePoint) {
		this.refinePoint = refinePoint;
	}
	public int getRefineLevel() {
		return refineLevel;
	}
	public void setRefineLevel(int refineLevel) {
		this.refineLevel = refineLevel;
	}
	public int getProType() {
		return proType;
	}
	public void setProType(int proType) {
		this.proType = proType;
	}
	public int getProValue() {
		return proValue;
	}
	public void setProValue(int proValue) {
		this.proValue = proValue;
	}
	public String getCost() {
		return cost;
	}
	public void setCost(String cost) {
		this.cost = cost;
	}
	public String getListeKey() {
		return equipType+"_"+career;
	}
	public String getObjKey() {
		return equipType+"_"+career+"_"+refinePoint+"_"+refineLevel;
	}
	
	
	
}
