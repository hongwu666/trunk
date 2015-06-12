package com.lodogame.ldsg.event;

public class HeroColorEvent extends BaseEvent {
	
	public HeroColorEvent(String userId, int heroColor) {
		this.userId = userId;
		this.data.put("color", heroColor);
	}
}
