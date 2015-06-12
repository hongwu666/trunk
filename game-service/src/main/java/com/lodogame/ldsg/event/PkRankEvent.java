package com.lodogame.ldsg.event;

public class PkRankEvent extends BaseEvent {

	public PkRankEvent(String userId, int rank) {
		this.userId = userId;
		this.setObject("rank", rank);
	}

}
