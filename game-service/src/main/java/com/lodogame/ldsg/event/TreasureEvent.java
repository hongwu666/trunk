package com.lodogame.ldsg.event;

public class TreasureEvent extends BaseEvent {
	public TreasureEvent(String userName){
		this.data.put("name", userName);
	}
}
