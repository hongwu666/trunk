package com.lodogame.model;

public class SystemBlackRoomConfig {

	/**
	 * 挑战次数
	 */
	private int challengeTime;

	/**
	 * 发费金币
	 */
	private int costGold;
	
	/**
	 * 修炼密室部队ID
	 */
	private int blackRoomForcesId;
	
	/**
	 * 萧家宝库部队ID
	 */
	private int treasuryForcesId;
	
	/**
	 * 图片颜色
	 */
	private int color;

	public int getChallengeTime() {
		return challengeTime;
	}

	public void setChallengeTime(int challengeTime) {
		this.challengeTime = challengeTime;
	}

	public int getCostGold() {
		return costGold;
	}

	public void setCostGold(int costGold) {
		this.costGold = costGold;
	}

	public int getBlackRoomForcesId() {
		return blackRoomForcesId;
	}

	public void setBlackRoomForcesId(int blackRoomForcesId) {
		this.blackRoomForcesId = blackRoomForcesId;
	}

	public int getTreasuryForcesId() {
		return treasuryForcesId;
	}

	public void setTreasuryForcesId(int treasuryForcesId) {
		this.treasuryForcesId = treasuryForcesId;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}
	
	
}
