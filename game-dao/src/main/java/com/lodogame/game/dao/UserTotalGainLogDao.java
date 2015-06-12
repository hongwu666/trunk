package com.lodogame.game.dao;

public interface UserTotalGainLogDao {

	/**
	 * 获取总的记录
	 * 
	 * @param userId
	 * @param type
	 * @return
	 */
	public int getUserTotalGain(String userId, int type);

	/**
	 * 累加用户总的掉落
	 * 
	 * @param userId
	 * @param type
	 * @param amount
	 * @return
	 */
	public boolean addUserTotalGain(String userId, int type, int amount);
}
