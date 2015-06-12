package com.lodogame.game.dao;

import com.lodogame.model.UserGiftLog;

public interface UserGiftLogDao {

	public boolean add(UserGiftLog userGiftLog);

	public UserGiftLog get(String userId, int bigType);
}
