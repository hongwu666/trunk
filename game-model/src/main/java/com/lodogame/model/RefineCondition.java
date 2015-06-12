package com.lodogame.model;

public class RefineCondition {

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
	 * 等级
	 */
	private int refineLevel;

	public int getCareer() {
		return career;
	}

	public void setCareer(int career) {
		this.career = career;
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

	public RefineCondition(EquipRefine equipRefine, SystemEquip systemEquip) {
		this.refinePoint = equipRefine.getRefinePoint();
		this.refineLevel = equipRefine.getRefineLevel();
		this.equipType = systemEquip.getEquipType();
		this.career = systemEquip.getCareer();
	}

}
