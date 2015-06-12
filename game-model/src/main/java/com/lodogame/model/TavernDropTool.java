package com.lodogame.model;

public class TavernDropTool implements SystemModel,RollAble{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 分组id=即星数
	 */
	private int groupId;
	/**
	 * vip等级
	 */
	private int vipLevel;
	/**
	 * 系统武将ID
	 */
	private int systemHeroId;
	/**
	 * 权重
	 */
	private int rate;
	
	
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public int getSystemHeroId() {
		return systemHeroId;
	}
	public void setSystemHeroId(int systemHeroId) {
		this.systemHeroId = systemHeroId;
	}
	public void setRate(int rate) {
		this.rate = rate;
	}
	public int getRate() {
		return rate;
	}
	public String getListeKey() {
		return String.valueOf(groupId);
	}
	public String getObjKey() {
		return null;
	}
	public int getVipLevel() {
		return vipLevel;
	}
	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}
	
}
