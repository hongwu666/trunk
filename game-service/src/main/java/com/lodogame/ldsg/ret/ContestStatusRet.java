package com.lodogame.ldsg.ret;

public class ContestStatusRet {

	private int retStatus;

	private int lastBattleType;

	private int lastBattleStatus;

	private int remainRegCount;

	private int maxDeadRound;

	private String targetUserId;

	public int getRetStatus() {
		return retStatus;
	}

	public void setRetStatus(int retStatus) {
		this.retStatus = retStatus;
	}

	public int getLastBattleType() {
		return lastBattleType;
	}

	public void setLastBattleType(int lastBattleType) {
		this.lastBattleType = lastBattleType;
	}

	public int getLastBattleStatus() {
		return lastBattleStatus;
	}

	public void setLastBattleStatus(int lastBattleStatus) {
		this.lastBattleStatus = lastBattleStatus;
	}

	public int getRemainRegCount() {
		return remainRegCount;
	}

	public void setRemainRegCount(int remainRegCount) {
		this.remainRegCount = remainRegCount;
	}

	public int getMaxDeadRound() {
		return maxDeadRound;
	}

	public void setMaxDeadRound(int maxDeadRound) {
		this.maxDeadRound = maxDeadRound;
	}

	public String getTargetUserId() {
		return targetUserId;
	}

	public void setTargetUserId(String targetUserId) {
		this.targetUserId = targetUserId;
	}

}
