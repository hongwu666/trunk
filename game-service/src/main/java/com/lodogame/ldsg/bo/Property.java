package com.lodogame.ldsg.bo;

public class Property {

	/**
	 * 属性类型
	 */
	private int type;
	/**
	 * 属性值
	 */
	private int value;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public Property(int type, int value) {
		super();
		this.type = type;
		this.value = value;
	}

}
