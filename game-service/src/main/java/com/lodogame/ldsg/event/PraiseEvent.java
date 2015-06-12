package com.lodogame.ldsg.event;

public class PraiseEvent extends BaseEvent {

	public PraiseEvent(String uid, String praisedUserId) {
		this.userId = uid;
		this.setObject("praisedUserId", praisedUserId);
	}
}
