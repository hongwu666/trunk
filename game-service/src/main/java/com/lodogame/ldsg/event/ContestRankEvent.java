package com.lodogame.ldsg.event;

public class ContestRankEvent extends BaseEvent {
	public ContestRankEvent(String userId, int rank) {
		this.userId = userId;
		this.setObject("rank", rank);
	}
}
