package com.lodogame.model;

public class RefineSoulCondition {

	/**
	 * 装备类型
	 */
	private int equipType;
	/**
	 * 装备星级
	 */
	private int equipStar;
	/**
	 * 装备职业
	 */
	private int career;

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

	public int getEquipStar() {
		return equipStar;
	}

	public void setEquipStar(int equipStar) {
		this.equipStar = equipStar;
	}

	public RefineSoulCondition(SystemEquip systemEquip) {
		this.equipType = systemEquip.getEquipType();
		this.equipStar = systemEquip.getEquipStar();
		this.career = systemEquip.getCareer();
	}

}
