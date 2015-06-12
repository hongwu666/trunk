package com.lodogame.model;

public class OnlyoneHourRankReward implements SystemModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int rankUpper;

	private int rankLower;

	private int coppera;

	private int copperb;

	private double copperc;

	private int honour;

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

	public int getCoppera() {
		return coppera;
	}

	public void setCoppera(int coppera) {
		this.coppera = coppera;
	}

	public int getCopperb() {
		return copperb;
	}

	public void setCopperb(int copperb) {
		this.copperb = copperb;
	}

	public double getCopperc() {
		return copperc;
	}

	public void setCopperc(double copperc) {
		this.copperc = copperc;
	}

	public int getHonour() {
		return honour;
	}

	public void setHonour(int honour) {
		this.honour = honour;
	}

	public String getListeKey() {
		return null;
	}

	public String getObjKey() {
		return null;
	}

}
