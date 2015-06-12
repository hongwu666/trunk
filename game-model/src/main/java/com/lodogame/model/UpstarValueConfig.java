package com.lodogame.model;

public class UpstarValueConfig implements SystemModel {

	private int id;
	private int starLevel;
	private int job;
	private int exp;
	private int event;
	private float ratio;
	private int powerBase;
	private int attBase;
	private int defBase;
	private int powerAdd;
	private int attAdd;
	private int defAdd;
	private int eatExp;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStarLevel() {
		return starLevel;
	}

	public void setStarLevel(int starLevel) {
		this.starLevel = starLevel;
	}

	public int getJob() {
		return job;
	}

	public void setJob(int job) {
		this.job = job;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getEvent() {
		return event;
	}

	public void setEvent(int event) {
		this.event = event;
	}

	public float getRatio() {
		return ratio;
	}

	public void setRatio(float ratio) {
		this.ratio = ratio;
	}

	public int getPowerBase() {
		return powerBase;
	}

	public void setPowerBase(int powerBase) {
		this.powerBase = powerBase;
	}

	public int getAttBase() {
		return attBase;
	}

	public void setAttBase(int attBase) {
		this.attBase = attBase;
	}

	public int getDefBase() {
		return defBase;
	}

	public void setDefBase(int defBase) {
		this.defBase = defBase;
	}

	public int getPowerAdd() {
		return powerAdd;
	}

	public void setPowerAdd(int powerAdd) {
		this.powerAdd = powerAdd;
	}

	public int getAttAdd() {
		return attAdd;
	}

	public void setAttAdd(int attAdd) {
		this.attAdd = attAdd;
	}

	public int getDefAdd() {
		return defAdd;
	}

	public void setDefAdd(int defAdd) {
		this.defAdd = defAdd;
	}

	public int getEatExp() {
		return eatExp;
	}

	public void setEatExp(int eatExp) {
		this.eatExp = eatExp;
	}

	public String getListeKey() {
		return null;
	}

	public String getObjKey() {
		return this.starLevel + "_" + this.job;
	}

}
