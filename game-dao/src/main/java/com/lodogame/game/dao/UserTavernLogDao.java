package com.lodogame.game.dao;

public interface UserTavernLogDao {

	/**
	 * 是否有抽过武将的
	 * 
	 * @param userId
	 * @return
	 */
	public boolean isExistLog(String userId);

	/**
	 * 添加抽过武将的日志
	 * 
	 * @param userId
	 * @return
	 */
	public boolean addTavernLog(String userId);
}
