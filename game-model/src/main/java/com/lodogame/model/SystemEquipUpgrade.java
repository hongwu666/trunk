package com.lodogame.model;

public class SystemEquipUpgrade implements SystemModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 要升级的装备
	 */
	private int equipId;

	/**
	 * 升级后的装备
	 */
	private int upgradeEquipId;

	public int getEquipId() {
		return equipId;
	}

	public void setEquipId(int equipId) {
		this.equipId = equipId;
	}

	public int getUpgradeEquipId() {
		return upgradeEquipId;
	}

	public void setUpgradeEquipId(int upgradeEquipId) {
		this.upgradeEquipId = upgradeEquipId;
	}

	public String getListeKey() {
		return null;
	}

	public String getObjKey() {
		return String.valueOf(this.equipId);
	}

}
