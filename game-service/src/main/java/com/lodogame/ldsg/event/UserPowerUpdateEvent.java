package com.lodogame.ldsg.event;

public class UserPowerUpdateEvent extends BaseEvent implements Event {

	public UserPowerUpdateEvent(String userId) {
		this.userId = userId;
	}

}
