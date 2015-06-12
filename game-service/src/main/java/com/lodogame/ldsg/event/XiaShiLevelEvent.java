package com.lodogame.ldsg.event;

public class XiaShiLevelEvent extends BaseEvent{
	public XiaShiLevelEvent(String uid,int xsLevel ){
		this.userId = uid;
		this.setObject("level", xsLevel);
	}
}
