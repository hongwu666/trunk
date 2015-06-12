package com.lodogame.model;

import java.io.Serializable;

/**
 * 装备攻击力相关属性
 * 
 * @author jacky
 * 
 */
public class SystemEquipAttr implements SystemModel, Serializable {

	private static final long serialVersionUID = 3904753795842823755L;

	private int systemEquipAttrId;

	private int equipStar;

	private int equipLevel;

	private int recyclePrice;

	private int upgradePrice;

	public String getListeKey() {
		return null;
	}

	public String getObjKey() {
		return this.equipStar + "_" + this.equipLevel;
	}

	public int getSystemEquipAttrId() {
		return systemEquipAttrId;
	}

	public void setSystemEquipAttrId(int systemEquipAttrId) {
		this.systemEquipAttrId = systemEquipAttrId;
	}

	public int getEquipStar() {
		return equipStar;
	}

	public void setEquipStar(int equipStar) {
		this.equipStar = equipStar;
	}

	public int getEquipLevel() {
		return equipLevel;
	}

	public void setEquipLevel(int equipLevel) {
		this.equipLevel = equipLevel;
	}

	public int getRecyclePrice() {
		return recyclePrice;
	}

	public void setRecyclePrice(int recyclePrice) {
		this.recyclePrice = recyclePrice;
	}

	public int getUpgradePrice() {
		return upgradePrice;
	}

	public void setUpgradePrice(int upgradePrice) {
		this.upgradePrice = upgradePrice;
	}

}
