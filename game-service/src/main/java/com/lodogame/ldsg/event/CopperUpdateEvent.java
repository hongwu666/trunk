package com.lodogame.ldsg.event;

public class CopperUpdateEvent extends BaseEvent {

	public CopperUpdateEvent(String userId, int copperNum) {
		this.userId = userId;
		this.data.put("copper", copperNum);
	}
}
