package com.lodogame.ldsg.event;

public class UpdateRankEvent extends BaseEvent{
	public UpdateRankEvent(String userId,int type,Object obj) {
		this.userId = userId;
		this.data.put("type", type);
		this.data.put("obj", obj);
	}
}
