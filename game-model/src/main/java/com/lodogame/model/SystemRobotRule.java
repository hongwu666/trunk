package com.lodogame.model;

public class SystemRobotRule implements SystemModel {

	private int rankUpper;

	private int rankLower;

	private int level;

	private int num;

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

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getListeKey() {
		return null;
	}

	public String getObjKey() {
		return null;
	}

}
