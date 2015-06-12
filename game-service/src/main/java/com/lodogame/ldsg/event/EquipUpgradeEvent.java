package com.lodogame.ldsg.event;

public class EquipUpgradeEvent extends BaseEvent {

	public EquipUpgradeEvent(String userId, String userEquipId) {
		this.userId = userId;
		this.setObject("userEquipId", userEquipId);
	}

}
