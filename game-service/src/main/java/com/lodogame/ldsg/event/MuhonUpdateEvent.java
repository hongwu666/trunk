package com.lodogame.ldsg.event;


public class MuhonUpdateEvent extends BaseEvent{
	
	public MuhonUpdateEvent(String userId, int muhonNum) {
		this.userId = userId;
		this.data.put("muhon", muhonNum);
	}
}
