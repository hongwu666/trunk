package com.lodogame.ldsg.event;

public class FriendAddEvent extends BaseEvent {
	public FriendAddEvent(String uid,int size){
		this.userId = uid;
		this.setObject("size", size);
	}
}
