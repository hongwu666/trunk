package com.lodogame.model;

public class MysteryGroupRate implements SystemModel, RollAble {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8623777876760193036L;

	private int mallType;

	private int type;

	private int groupId;

	private int rate;

	public int getMallType() {
		return mallType;
	}

	public void setMallType(int mallType) {
		this.mallType = mallType;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public String getListeKey() {
		return mallType + "_" + type;
	}

	public String getObjKey() {
		return null;
	}

}
