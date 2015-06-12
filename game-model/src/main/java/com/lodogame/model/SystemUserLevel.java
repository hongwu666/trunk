package com.lodogame.model;

import java.io.Serializable;

/**
 * 玩家等级经验配置
 * 
 * @author jacky
 * 
 */
public class SystemUserLevel implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 玩家等级
	 */
	private int userLevel;

	/**
	 * 玩家经验
	 */
	private int exp;

	/**
	 * 玩家上阵人数
	 */
	private int battleNum;

	/**
	 * 体力上限
	 */
	private int powerMax;

	/**
	 * 升级加的体力
	 */
	private int levelUpAddPower;

	/**
	 * 英雄等级上限
	 */
	private int heroLevelMax;

	public int getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(int userLevel) {
		this.userLevel = userLevel;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getBattleNum() {
		return battleNum;
	}

	public void setBattleNum(int battleNum) {
		this.battleNum = battleNum;
	}

	public int getPowerMax() {
		return powerMax;
	}

	public void setPowerMax(int powerMax) {
		this.powerMax = powerMax;
	}

	public int getLevelUpAddPower() {
		return levelUpAddPower;
	}

	public void setLevelUpAddPower(int levelUpAddPower) {
		this.levelUpAddPower = levelUpAddPower;
	}

	public int getHeroLevelMax() {
		return heroLevelMax;
	}

	public void setHeroLevelMax(int heroLevelMax) {
		this.heroLevelMax = heroLevelMax;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
