package com.lodogame.ldsg.event;

public class EquiGetEvent extends BaseEvent{
	public EquiGetEvent(String userId,int color){
		this.userId = userId;
		setObject("color",color);
	}
}
