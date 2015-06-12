package com.lodogame.model;

public class SystemRobotHero implements SystemModel {

	private int robotHeroId;

	private int systemHeroId;

	private int attackRatio;

	private int defenseRatio;

	private int lifeRatio;

	public int getRobotHeroId() {
		return robotHeroId;
	}

	public void setRobotHeroId(int robotHeroId) {
		this.robotHeroId = robotHeroId;
	}

	public int getSystemHeroId() {
		return systemHeroId;
	}

	public void setSystemHeroId(int systemHeroId) {
		this.systemHeroId = systemHeroId;
	}

	public int getAttackRatio() {
		return attackRatio;
	}

	public void setAttackRatio(int attackRatio) {
		this.attackRatio = attackRatio;
	}

	public int getDefenseRatio() {
		return defenseRatio;
	}

	public void setDefenseRatio(int defenseRatio) {
		this.defenseRatio = defenseRatio;
	}

	public int getLifeRatio() {
		return lifeRatio;
	}

	public void setLifeRatio(int lifeRatio) {
		this.lifeRatio = lifeRatio;
	}

	public String getListeKey() {
		return String.valueOf(robotHeroId);
	}

	public String getObjKey() {
		return null;
	}

}
