package com.lodogame.model;

import java.util.Date;

/**
 * 挑战记录bean
 * 用来记录所有用户挑战的记录，一个礼拜要清理一次
 * @author Candon
 *
 */
public class UserArenaRecordLog {
	private int recordId;
	
	/**
	 * 挑战用户ID
	 */
	private String attackUserId;
	
	/**
	 * 防守用户ID
	 */
	private String defenseUserId;
	
	/**
	 * 战斗结果
	 */
	private int result;
	
	/**
	 * 是否复仇
	 */
	private int isRevenge;
	
	private Date createdTime;
	
	private String username;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getIsRevenge() {
		return isRevenge;
	}
	public void setIsRevenge(int isRevenge) {
		this.isRevenge = isRevenge;
	}
	public int getRecordId() {
		return recordId;
	}
	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}
	public String getAttackUserId() {
		return attackUserId;
	}
	public void setAttackUserId(String attackUserId) {
		this.attackUserId = attackUserId;
	}
	public String getDefenseUserId() {
		return defenseUserId;
	}
	public void setDefenseUserId(String defenseUserId) {
		this.defenseUserId = defenseUserId;
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	
	
}
