package com.lodogame.model;

import java.util.Date;

public class EquipRefine {

	private int id;
	/**
	 * 用户id
	 */
	private String userId;
	/**
	 * 用户装备id
	 */
	private String userEquipId;

	/**
	 * 精炼点
	 */
	private int refinePoint;
	/**
	 * 精炼等级
	 */
	private int refineLevel;

	/**
	 * created time
	 */
	private Date createdTime;

	/**
	 * 装备ID
	 */
	private int equipId;

	public String getUserEquipId() {
		return userEquipId;
	}

	public void setUserEquipId(String userEquipId) {
		this.userEquipId = userEquipId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getRefinePoint() {
		return refinePoint;
	}

	public void setRefinePoint(int refinePoint) {
		this.refinePoint = refinePoint;
	}

	public int getRefineLevel() {
		return refineLevel;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setRefineLevel(int refineLevel) {
		this.refineLevel = refineLevel;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public int getEquipId() {
		return equipId;
	}

	public void setEquipId(int equipId) {
		this.equipId = equipId;
	}

	public EquipRefine(String userId, String userEquipId, int refinePoint, int refineLevel, Date createdTime, int equipId) {
		super();
		this.userId = userId;
		this.userEquipId = userEquipId;
		this.refinePoint = refinePoint;
		this.refineLevel = refineLevel;
		this.createdTime = createdTime;
		this.equipId = equipId;
	}

	public EquipRefine() {
		super();
	}

}
