package com.lodogame.model;

public class SystemRefineSoulData implements SystemModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;

	private int equipType;

	private int mapId;

	private int refineSoulType;

	private String cost;

	private int addLuck;

	private int minLuck;

	private int chance;

	private int maxLuck;

	public String getListeKey() {
		return String.valueOf(equipType);
	}

	public String getObjKey() {
		return equipType + "_" + refineSoulType;
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

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	public int getRefineSoulType() {
		return refineSoulType;
	}

	public void setRefineSoulType(int refineSoulType) {
		this.refineSoulType = refineSoulType;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public int getAddLuck() {
		return addLuck;
	}

	public void setAddLuck(int addLuck) {
		this.addLuck = addLuck;
	}

	public int getMinLuck() {
		return minLuck;
	}

	public void setMinLuck(int minLuck) {
		this.minLuck = minLuck;
	}

	public int getChance() {
		return chance;
	}

	public void setChance(int chance) {
		this.chance = chance;
	}

	public int getMaxLuck() {
		return maxLuck;
	}

	public void setMaxLuck(int maxLuck) {
		this.maxLuck = maxLuck;
	}

}
