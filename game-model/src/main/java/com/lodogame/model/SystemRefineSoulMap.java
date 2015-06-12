package com.lodogame.model;

public class SystemRefineSoulMap implements SystemModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	
	private int mapId;
	
	private int equipId;
	
	private int weight;
	
	private String make;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	public int getEquipId() {
		return equipId;
	}

	public void setEquipId(int equipId) {
		this.equipId = equipId;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getListeKey() {
		return String.valueOf(mapId);
	}

	public String getObjKey() {
		return mapId+"_"+weight;
	}

}
