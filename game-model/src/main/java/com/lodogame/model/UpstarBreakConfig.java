package com.lodogame.model;

public class UpstarBreakConfig implements SystemModel {

	private int nowId;
	private int upId;
	private int num;

	public int getNowId() {
		return nowId;
	}

	public void setNowId(int nowId) {
		this.nowId = nowId;
	}

	public int getUpId() {
		return upId;
	}

	public void setUpId(int upId) {
		this.upId = upId;
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
		return this.nowId + "_" + this.upId;
	}

}
