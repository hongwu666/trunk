package com.lodogame.model;

public class SystemEquipSellTool implements SystemModel {

	/**
	 * 装备的品质
	 */
	private int equipColor;

	/**
	 * 对应品质的装备出售可以获得的东西
	 */
	private String tools;

	public int getEquipColor() {
		return equipColor;
	}

	public void setEquipColor(int equipColor) {
		this.equipColor = equipColor;
	}

	public String getTools() {
		return tools;
	}

	public void setTools(String tools) {
		this.tools = tools;
	}

	public String getListeKey() {
		return null;
	}

	public String getObjKey() {
		return String.valueOf(this.equipColor);
	}

}
