package com.lodogame.ldsg.event;

public class QiangHuaEvent extends BaseEvent{
	public QiangHuaEvent(String uid,int type,int level){
		this.userId = uid;
		setObject("type", type);
		setObject("level", level);
	}
}
