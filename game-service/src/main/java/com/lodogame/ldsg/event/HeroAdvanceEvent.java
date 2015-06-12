package com.lodogame.ldsg.event;

public class HeroAdvanceEvent extends BaseEvent {

	public HeroAdvanceEvent(String userId, String userHeroId, int advanceTimes, int heroLevel) {
		this.userId = userId;
		this.setObject("advanceTimes", advanceTimes);
		this.setObject("heroLevel", heroLevel);
		this.setObject("userHeroId", userHeroId);
	}

}
