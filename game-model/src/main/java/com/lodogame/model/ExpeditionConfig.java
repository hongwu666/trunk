package com.lodogame.model;

public class ExpeditionConfig {
	private int indx;
	private int powerMin;
	private int powerMax;
	private String gift;
	
	private float min;
	private float max;

	
	
	public float getMin() {
		return min;
	}

	public float getMax() {
		return max;
	}

	public int getIndx() {
		return indx;
	}

	public void setIndx(int indx) {
		this.indx = indx;
	}


	public int getPowerMin() {
		return powerMin;
	}

	public int getPowerMax() {
		return powerMax;
	}

	public void setPowerMin(int powerMin) {
		this.powerMin = powerMin;
		this.min = powerMin/1000f;
	}


	public void setPowerMax(int powerMax) {
		this.powerMax = powerMax;
		this.max = powerMax/1000f;
	}

	public String getGift() {
		return gift;
	}

	public void setGift(String gift) {
		this.gift = gift;
	}

}
