package com.lodogame.ldsg.constants;

public class UserPowerGetType {
	/**
	 * 竞技场
	 */
	public static final UserPowerGetType ARENA = new UserPowerGetType(1);
	/**
	 * 远征
	 */
	public static final UserPowerGetType EXPEDITION = new UserPowerGetType(2);
	
	
	private int type;
	public UserPowerGetType(int type) {
		super();
		this.type = type;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}
