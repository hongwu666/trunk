package com.lodogame.model;

public class UserOnlyOneHero {
	/**
	 * 用户英雄ID
	 */
	private String userHeroId;
	/**
	 * 英雄ID
	 */
	private int heroId;
	/**
	 * 用户ID
	 */
	private String userId;
	/**
	 * 所在位置
	 */
	private int pos;
	/**
	 * 血
	 */
	private int life;
	
	/**
	 * 怒气
	 */
	private int morale;
	
	public String getUserHeroId() {
		return userHeroId;
	}
	public String getUserId() {
		return userId;
	}

	public int getHeroId() {
		return heroId;
	}
	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}
	public int getPos() {
		return pos;
	}
	public int getLife() {
		return life;
	}
	
	public void setUserHeroId(String userHeroId) {
		this.userHeroId = userHeroId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setPos(int pos) {
		this.pos = pos;
	}
	public void setLife(int life) {
		this.life = life;
	}
	public int getMorale() {
		return morale;
	}
	public void setMorale(int morale) {
		this.morale = morale;
	}
	

}
