package com.lodogame.ldsg.event;

/**
 * 打怪物事件
 * 
 * @author shixiangwen
 * 
 */
public class ForcesEvent extends BaseEvent implements Event {

	public ForcesEvent(String userId, int forcesId, int times) {
		this.userId = userId;
		this.setObject("forcesId", forcesId);
		this.setObject("times", times);
	}
}
