package com.lodogame.model;

public class UpstarHeroConfig implements SystemModel {

	private int id;
	private int heroId;
	private int initialLevel;
	private int maxLevel;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getHeroId() {
		return heroId;
	}

	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}

	public int getInitialLevel() {
		return initialLevel;
	}

	public void setInitialLevel(int initialLevel) {
		this.initialLevel = initialLevel;
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}

	public String getListeKey() {
		return null;
	}

	public String getObjKey() {
		return String.valueOf(heroId);
	}

}
