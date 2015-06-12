package com.lodogame.model;

public class OnlyoneWeekRankReward implements SystemModel {

	private int rankUpper;

	private int rankLower;

	private String toolIds;

	public int getRankUpper() {
		return rankUpper;
	}

	public void setRankUpper(int rankUpper) {
		this.rankUpper = rankUpper;
	}

	public int getRankLower() {
		return rankLower;
	}

	public void setRankLower(int rankLower) {
		this.rankLower = rankLower;
	}

	public String getToolIds() {
		return toolIds;
	}

	public void setToolIds(String toolIds) {
		this.toolIds = toolIds;
	}

	public String getListeKey() {
		return null;
	}

	public String getObjKey() {
		return null;
	}

}
