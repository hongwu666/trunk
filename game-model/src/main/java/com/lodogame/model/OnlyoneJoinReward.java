package com.lodogame.model;

public class OnlyoneJoinReward implements SystemModel {

	private int times;

	private int diffTimes;

	private int honour;

	private int coppera;

	private int copperb;

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public int getDiffTimes() {
		return diffTimes;
	}

	public void setDiffTimes(int diffTimes) {
		this.diffTimes = diffTimes;
	}

	public int getHonour() {
		return honour;
	}

	public void setHonour(int honour) {
		this.honour = honour;
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

	public String getListeKey() {
		return null;
	}

	public String getObjKey() {
		return String.valueOf(times);
	}

}
