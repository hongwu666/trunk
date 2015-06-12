package com.lodogame.ldsg.event;

/**
 * 百人斩消息
 * 
 * @author Candon
 * 
 */
public class ArenaEvent extends BaseEvent implements Event {

	public ArenaEvent(String userId) {
		this.userId = userId;
	}
}
