package com.lodogame.ldsg.event;

public class StoneUpgradeEvent extends BaseEvent {

	public StoneUpgradeEvent(String userId, int stoneId) {
		this.userId = userId;
		this.setObject("stoneId", stoneId);
	}

}
