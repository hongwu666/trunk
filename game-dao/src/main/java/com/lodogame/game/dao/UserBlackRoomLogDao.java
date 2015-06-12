package com.lodogame.game.dao;



import com.lodogame.model.UserBlackRoomLog;

public interface UserBlackRoomLogDao {

	/**
	 * 获取用户练功密室的信息
	 * 
	 * @param userId
	 * @return
	 */
	public UserBlackRoomLog getUserBlackRoomLog(String userId, int type);

	/**
	 * 更改用户练功密室的信息
	 * 
	 * @param userBlackRoomLog
	 * @return
	 */
	public boolean updateUserBlackRoomLog(UserBlackRoomLog userBlackRoomLog);
	
	/**
	 * 增加用户练功密室的信息
	 * 
	 * @param userBlackRoomLog
	 * @return
	 */
	public boolean addUserBlackRoomLog(UserBlackRoomLog userBlackRoomLog);
	
}
