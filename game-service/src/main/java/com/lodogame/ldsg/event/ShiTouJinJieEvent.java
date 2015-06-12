package com.lodogame.ldsg.event;

public class ShiTouJinJieEvent extends BaseEvent{
	public ShiTouJinJieEvent(String userId,int color){
		this.userId = userId;
		setObject("color", color);
	}
}	
