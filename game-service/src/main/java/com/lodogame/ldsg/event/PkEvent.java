package com.lodogame.ldsg.event;

public class PkEvent extends BaseEvent implements Event {

	/**
	 * 添加
	 */
	public static int EVENT_ENTER = 1;

	/**
	 * 挑战
	 */
	public static int EVENT_FIGHT = 2;

	/**
	 * 1 进入 2 打
	 */
	private int type;

	private EventHandle handle;

	public PkEvent(String userId, int type, EventHandle handle) {
		this.userId = userId;
		this.type = type;
		this.handle = handle;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public EventHandle getHandle() {
		return handle;
	}

	public void setHandle(EventHandle handle) {
		this.handle = handle;
	}

}
