package com.lodogame.ldsg.event;

public class HeroUpgradeEvent extends BaseEvent {

	public HeroUpgradeEvent(String userId, String userHeroId) {
		this.userId = userId;
		this.setObject("userHeroId", userHeroId);
	}

}
