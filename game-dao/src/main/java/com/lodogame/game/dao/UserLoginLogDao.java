package com.lodogame.game.dao;

import com.lodogame.model.UserLoginLog;

public interface UserLoginLogDao {
	
	public UserLoginLog get(String userId);

	public boolean add(UserLoginLog loginLog);

	public boolean update(UserLoginLog loginLog);

	public boolean updateRecStatus(String userId, String recStatus);
}
