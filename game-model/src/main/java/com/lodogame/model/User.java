package com.lodogame.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户模型
 * 
 * @author jacky
 * 
 */

public class User implements Serializable, IUser {

	private static final long serialVersionUID = -8711904146612278418L;

	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 等级
	 */
	private int level;

	/**
	 * 金币
	 */
	private long goldNum;

	/**
	 * 银币
	 */
	private long copper;

	/**
	 * 经验
	 */
	private long exp;

	/**
	 * 武魂
	 */
	private long muhon;

	/**
	 * 体力加时间
	 */
	private Date powerAddTime;

	/**
	 * 注册时间
	 */
	private Date regTime;

	/**
	 * 更新时间
	 */
	private Date updatedTime;

	/**
	 * 体力
	 */
	private int power;

	/**
	 * lodo id
	 */
	private long lodoId;

	/**
	 * vip等级
	 */
	private int vipLevel;

	/**
	 * 0：没有被封号，1：被封号
	 * 
	 * @return
	 */
	private int isBanned;

	/**
	 * 封号截止日期，过了这个日期以后，自动解除封号
	 * 
	 * @return
	 */
	private Date dueTime;

	/**
	 * 禁言时间
	 * 
	 * @return
	 */
	private Date bannedChatTime;

	/**
	 * 论剑声望
	 */
	private int reputation;

	/**
	 * 个人头像
	 */
	private int imgId;

	/**
	 * 魂力
	 */
	private int soul;

	/**
	 * 精元
	 */
	private int energy;

	/**
	 * 荣誉
	 */
	private int honour;

	/**
	 * 硬币
	 */
	private int coin;

	public int getCoin() {
		return coin;
	}

	public void setCoin(int coin) {
		this.coin = coin;
	}

	/**
	 * 铭文
	 */
	private int mingwen;
	/**
	 * 熟练度
	 */
	private int skill;

	public int getIsBanned() {
		return isBanned;
	}

	public int getSkill() {
		return skill;
	}

	public void setSkill(int skill) {
		this.skill = skill;
	}

	public void setIsBanned(int isBanned) {
		this.isBanned = isBanned;
	}

	public Date getDueTime() {
		return dueTime;
	}

	public void setDueTime(Date dueTime) {
		this.dueTime = dueTime;
	}

	public long getLodoId() {
		return lodoId;
	}

	public void setLodoId(long lodoId) {
		this.lodoId = lodoId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public long getGoldNum() {
		return goldNum;
	}

	public void setGoldNum(long goldNum) {
		this.goldNum = goldNum;
	}

	public long getCopper() {
		return copper;
	}

	public void setCopper(long copper) {
		this.copper = copper;
	}

	public long getExp() {
		return exp;
	}

	public void setExp(long exp) {
		this.exp = exp;
	}

	public Date getRegTime() {
		return regTime;
	}

	public void setRegTime(Date regTime) {
		this.regTime = regTime;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public Date getPowerAddTime() {
		return powerAddTime;
	}

	public void setPowerAddTime(Date powerAddTime) {
		this.powerAddTime = powerAddTime;
	}

	public int getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}

	public Date getBannedChatTime() {
		return bannedChatTime;
	}

	public void setBannedChatTime(Date bannedChatTime) {
		this.bannedChatTime = bannedChatTime;
	}

	public long getMuhon() {
		return muhon;
	}

	public void setMuhon(long muhon) {
		this.muhon = muhon;
	}

	public int getReputation() {
		return reputation;
	}

	public void setReputation(int reputation) {
		this.reputation = reputation;
	}

	public int getImgId() {
		return imgId;
	}

	public void setImgId(int imgId) {
		this.imgId = imgId;
	}

	public int getSoul() {
		return soul;
	}

	public void setSoul(int soul) {
		this.soul = soul;
	}

	public int getEnergy() {
		return energy;
	}

	public void setEnergy(int energy) {
		this.energy = energy;
	}

	public int getMingwen() {
		return mingwen;
	}

	public void setMingwen(int mingwen) {
		this.mingwen = mingwen;
	}

	public int getHonour() {
		return honour;
	}

	public void setHonour(int honour) {
		this.honour = honour;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof User) {
			if (this.getUserId().equals(((User) obj).getUserId())) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
}
