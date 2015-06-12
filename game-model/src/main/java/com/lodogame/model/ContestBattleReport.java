package com.lodogame.model;

import java.util.Date;

public class ContestBattleReport {

	private String attackUserId;

	private String attackUsername;

	private String defenseUserId;

	private String defenseUsername;

	private int flag;

	private String baseCode;

	private String report;

	private String winInfo;

	private Date createdTime;

	public String getAttackUserId() {
		return attackUserId;
	}

	public void setAttackUserId(String attackUserId) {
		this.attackUserId = attackUserId;
	}

	public String getAttackUsername() {
		return attackUsername;
	}

	public void setAttackUsername(String attackUsername) {
		this.attackUsername = attackUsername;
	}

	public String getDefenseUserId() {
		return defenseUserId;
	}

	public void setDefenseUserId(String defenseUserId) {
		this.defenseUserId = defenseUserId;
	}

	public String getDefenseUsername() {
		return defenseUsername;
	}

	public void setDefenseUsername(String defenseUsername) {
		this.defenseUsername = defenseUsername;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getBaseCode() {
		return baseCode;
	}

	public void setBaseCode(String baseCode) {
		this.baseCode = baseCode;
	}

	public String getReport() {
		return report;
	}

	public void setReport(String report) {
		this.report = report;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getWinInfo() {
		return winInfo;
	}

	public void setWinInfo(String winInfo) {
		this.winInfo = winInfo;
	}

}
