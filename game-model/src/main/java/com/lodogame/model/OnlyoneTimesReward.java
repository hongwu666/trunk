package com.lodogame.model;

public class OnlyoneTimesReward implements SystemModel {

	private int times;

	private int point;

	private int copperA;

	private int copperB;

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public int getCopperA() {
		return copperA;
	}

	public void setCopperA(int copperA) {
		this.copperA = copperA;
	}

	public int getCopperB() {
		return copperB;
	}

	public void setCopperB(int copperB) {
		this.copperB = copperB;
	}

	public String getListeKey() {
		return null;
	}

	public String getObjKey() {
		return String.valueOf(this.times);
	}

}
