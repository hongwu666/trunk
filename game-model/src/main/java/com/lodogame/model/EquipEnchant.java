package com.lodogame.model;

import java.util.Date;


/**
 * 装备点化实体
 * 
 * @author Administrator
 *
 */
public class EquipEnchant {
	/**
	 * 自增唯一id
	 */
	private int id;
	/**
	 * 用户装备id
	 */
	private String userEquipId;
	/**
	 * 用户id
	 */
	private String userId;

	/**
	 * 装备ID
	 */
	private int equipId;

	/**
	 * 当前属性值
	 */
	private String curProperty;

	/**
	 * 点化属性值
	 */
	private String enProperty;
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

	public String getCurProperty() {
		return curProperty;
	}

	public void setCurProperty(String curProperty) {
		this.curProperty = curProperty;
	}

	public String getEnProperty() {
		return enProperty;
	}

	public void setEnProperty(String enProperty) {
		this.enProperty = enProperty;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public EquipEnchant(String userEquipId, String userId, int equipId, String curProperty, String enProperty) {
		super();
		this.userEquipId = userEquipId;
		this.userId = userId;
		this.equipId = equipId;
		this.curProperty = curProperty;
		this.enProperty = enProperty;
	}

	public EquipEnchant() {
		super();
	}

}
