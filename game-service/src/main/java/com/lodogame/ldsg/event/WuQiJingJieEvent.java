package com.lodogame.ldsg.event;

public class WuQiJingJieEvent extends BaseEvent {
	public WuQiJingJieEvent(String uid,int color,int type){
		this.userId = uid;
		setObject("color", color);
		setObject("type", type);
	}
}
