package com.lodogame.ldsg.event;

public class HeroPowerUpdateEvent extends BaseEvent implements Event {

	public HeroPowerUpdateEvent(String userId, String userHeroId) {
		this.userId = userId;
		this.setObject("userHeroId", userHeroId);
	}

}
