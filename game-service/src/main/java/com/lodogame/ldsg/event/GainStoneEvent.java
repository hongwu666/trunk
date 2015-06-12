package com.lodogame.ldsg.event;

public class GainStoneEvent extends BaseEvent {

	public GainStoneEvent(String userId, int stoneId) {
		this.userId = userId;
		setObject("stoneId", stoneId);
	}
}
