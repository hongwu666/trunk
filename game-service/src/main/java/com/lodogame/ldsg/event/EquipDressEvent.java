package com.lodogame.ldsg.event;

public class EquipDressEvent extends BaseEvent {
	public EquipDressEvent(String userId, String userEquipId) {
		this.userId = userId;
		setObject("userHeroId", userEquipId);
	}
}
