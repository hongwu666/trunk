package com.lodogame.model;

/**
 * @see svn://203.195.190.162/ltcs/product/策划/设计文档/6月改版文档20140617
 * @author Candon
 * 
 */
public class SystemStone implements SystemModel {

	private int systemId;

	private int stoneId;

	private String stoneName;

	private String stoneDesc;

	private int stoneType;

	private int stoneColor;

	private int stoneLevel;

	private float stoneValuePercent;

	private int stoneValue;

	private int protectNum;

	private int lowerNum;

	private int upperNum;

	private int upgradStoneId;

	private int sellPrice;

	private int equipColor;

	public int getEquipColor() {
		return equipColor;
	}

	public void setEquipColor(int equipColor) {
		this.equipColor = equipColor;
	}

	public int getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(int sellPrice) {
		this.sellPrice = sellPrice;
	}

	public int getUpgradStoneId() {
		return upgradStoneId;
	}

	public void setUpgradStoneId(int upgradStoneId) {
		this.upgradStoneId = upgradStoneId;
	}

	public int getSystemId() {
		return systemId;
	}

	public void setSystemId(int systemId) {
		this.systemId = systemId;
	}

	public int getStoneId() {
		return stoneId;
	}

	public void setStoneId(int stoneId) {
		this.stoneId = stoneId;
	}

	public String getStoneName() {
		return stoneName;
	}

	public void setStoneName(String stoneName) {
		this.stoneName = stoneName;
	}

	public String getStoneDesc() {
		return stoneDesc;
	}

	public void setStoneDesc(String stoneDesc) {
		this.stoneDesc = stoneDesc;
	}

	public int getStoneType() {
		return stoneType;
	}

	public void setStoneType(int stoneType) {
		this.stoneType = stoneType;
	}

	public int getStoneColor() {
		return stoneColor;
	}

	public void setStoneColor(int stoneColor) {
		this.stoneColor = stoneColor;
	}

	public int getStoneLevel() {
		return stoneLevel;
	}

	public void setStoneLevel(int stoneLevel) {
		this.stoneLevel = stoneLevel;
	}

	public int getProtectNum() {
		return protectNum;
	}

	public void setProtectNum(int protectNum) {
		this.protectNum = protectNum;
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

	public float getStoneValuePercent() {
		return stoneValuePercent;
	}

	public void setStoneValuePercent(float stoneValuePercent) {
		this.stoneValuePercent = stoneValuePercent / 100.0f;
	}

	public int getStoneValue() {
		return stoneValue;
	}

	public void setStoneValue(int stoneValue) {
		this.stoneValue = stoneValue;
	}

	public String getListeKey() {
		return String.valueOf(this.getStoneLevel());
	}

	public String getObjKey() {
		return String.valueOf(this.stoneId);
	}

}
