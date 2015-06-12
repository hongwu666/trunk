package com.lodogame.model;

import java.util.Date;

public class EquipRefineSoul {
	private int id;
	/**
	 * 用户id
	 */
	private String userId;
	/**
	 * 用户装备ID
	 */
	private String userEquipId;
	/**
	 * 系统装备id
	 */
	private int equipId;
	/**
	 * 幸运值
	 */
	private int luck;

	/**
	 * 创建时间
	 */
	private Date createdTime;

	public String getUserEquipId() {
		return userEquipId;
	}

	public void setUserEquipId(String userEquipId) {
		this.userEquipId = userEquipId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getEquipId() {
		return equipId;
	}

	public void setEquipId(int equipId) {
		this.equipId = equipId;
	}

	public int getLuck() {
		return luck;
	}

	public void setLuck(int luck) {
		this.luck = luck;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public EquipRefineSoul(String userId, String userEquipId, int equipId, int luck, Date createdTime) {
		super();
		this.userId = userId;
		this.userEquipId = userEquipId;
		this.equipId = equipId;
		this.luck = luck;
		this.createdTime = createdTime;
	}

	public EquipRefineSoul() {
		super();
	}

}
