package com.lodogame.ldsg.event;

public class HeroStarEvent extends BaseEvent{

	public HeroStarEvent(String userId, int heroStar) {
		this.userId = userId;
		this.data.put("star", heroStar);
	}
}
