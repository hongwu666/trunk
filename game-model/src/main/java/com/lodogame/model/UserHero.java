package com.lodogame.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class UserHero implements Serializable {

	private static final long serialVersionUID = 75800777885412119L;

	/**
	 * 用户武将ID
	 */
	private String userHeroId;

	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 系统武将ID
	 */
	private int systemHeroId;

	/**
	 * 武将经验
	 */
	private int heroExp;

	/**
	 * 武将等级
	 */
	private int heroLevel;

	/**
	 * 武将站
	 */
	private int pos;

	/**
	 * 创建时间
	 */
	private Date createdTime;

	/**
	 * 更新时间
	 */
	private Date updatedTime;

	/**
	 * 血祭的等级
	 */
	private int bloodSacrificeStage;

	/**
	 * 锁定状态
	 */
	private int lockStatus;

	/**
	 * 化神等级
	 */
	private int DeifyNodeLevel;

	/**
	 * 进阶点亮过的节点
	 */
	private String upgradeNode;

	/**
	 * 星级
	 */
	private int starLevel;

	/**
	 * 升阶经验
	 */
	private int starExp;

	private int exId;

	private int life;

	private int rage;
	
	private int newexp;

	public int getExId() {
		return exId;
	}

	public void setExId(int exId) {
		this.exId = exId;
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public int getRage() {
		return rage;
	}

	public int getNewexp() {
		return newexp;
	}

	public void setNewexp(int newexp) {
		this.newexp = newexp;
	}

	public void setRage(int rage) {
		this.rage = rage;
	}

	public int getStarLevel() {
		return starLevel;
	}

	public void setStarLevel(int starLevel) {
		this.starLevel = starLevel;
	}

	public int getStarExp() {
		return starExp;
	}

	public void setStarExp(int starExp) {
		this.starExp = starExp;
	}

	public String getUpgradeNode() {
		return upgradeNode;
	}

	public void setUpgradeNode(String upgradeNode) {
		this.upgradeNode = upgradeNode;
	}

	public String getUserHeroId() {
		return userHeroId;
	}

	public void setUserHeroId(String userHeroId) {
		this.userHeroId = userHeroId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getSystemHeroId() {
		return systemHeroId;
	}

	public void setSystemHeroId(int systemHeroId) {
		this.systemHeroId = systemHeroId;
	}

	public int getHeroExp() {
		return heroExp;
	}

	public void setHeroExp(int heroExp) {
		this.heroExp = heroExp;
	}

	public int getHeroLevel() {
		return heroLevel;
	}

	public void setHeroLevel(int heroLevel) {
		this.heroLevel = heroLevel;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public int getBloodSacrificeStage() {
		return bloodSacrificeStage;
	}

	public void setBloodSacrificeStage(int bloodSacrificeStage) {
		this.bloodSacrificeStage = bloodSacrificeStage;
	}

	public int getLockStatus() {
		return lockStatus;
	}

	public void setLockStatus(int lockStatus) {
		this.lockStatus = lockStatus;
	}

	public int getDeifyNodeLevel() {
		return DeifyNodeLevel;
	}

	public void setDeifyNodeLevel(int deifyNodeLevel) {
		DeifyNodeLevel = deifyNodeLevel;
	}

	/**
	 * 这个品质对应的所有升级节点是否都点亮了
	 * 
	 * @return
	 */
	public boolean isAllUpgradeNodesLighted(List<String> nodes) {
		String[] lightedNodes = this.upgradeNode.split(",");

		for (String node : lightedNodes) {
			if (nodes.contains(node) == true) {
				continue;
			} else {
				return false;
			}
		}

		return true;

	}

	@Override
	public String toString() {
		return "UserHero [heroLevel=" + heroLevel + ", DeifyNodeLevel=" + DeifyNodeLevel + ", upgradeNode=" + upgradeNode + ",userID=" + userId + "]";
	}

}
