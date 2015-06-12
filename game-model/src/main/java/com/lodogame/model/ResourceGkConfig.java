package com.lodogame.model;

public class ResourceGkConfig {
	/**
	 * h_id INT, s_id INT, map_id INT, dif INT, npc INT, start_num INT, gift
	 * VARCHAR(200), box VARCHAR(200)
	 */
	private int id;
	private int mapId;
	private int dif;
	private int gk;
	private int npc;
	private int startNum;
	private String gift;
	private String box;

	
	
	public int getGk() {
		return gk;
	}

	public void setGk(int gk) {
		this.gk = gk;
	}


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

	public int getDif() {
		return dif;
	}

	public void setDif(int dif) {
		this.dif = dif;
	}

	public int getNpc() {
		return npc;
	}

	public void setNpc(int npc) {
		this.npc = npc;
	}

	public int getStartNum() {
		return startNum;
	}

	public void setStartNum(int startNum) {
		this.startNum = startNum;
	}

	public String getGift() {
		return gift;
	}

	public void setGift(String gift) {
		this.gift = gift;
	}

	public String getBox() {
		return box;
	}

	public void setBox(String box) {
		this.box = box;
	}

}
