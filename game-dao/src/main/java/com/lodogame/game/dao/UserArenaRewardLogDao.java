package com.lodogame.game.dao;

import com.lodogame.model.UserArenaRewardLog;

public interface UserArenaRewardLogDao {
	
	public boolean add(UserArenaRewardLog userArenaRewardLog);
	
	public UserArenaRewardLog get(String userId,int type);
	
	public boolean clear();
}
