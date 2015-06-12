package com.lodogame.ldsg.event;

public class TavernDrawEvent extends BaseEvent {
	public TavernDrawEvent(String userId, int type, boolean isNeedMoney, int times) {
		this.userId = userId;
		this.data.put("type", type);
		this.data.put("isNeedMoney", isNeedMoney);
		this.data.put("times", times);
	}
}
