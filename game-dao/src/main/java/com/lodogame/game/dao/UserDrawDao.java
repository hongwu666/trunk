package com.lodogame.game.dao;

import com.lodogame.model.UserDrawLog;

public interface UserDrawDao {

	/**
	 * 添加日志
	 * 
	 * @param userDrawDao
	 * @return
	 */
	public boolean add(UserDrawLog userDrawLog);

	/**
	 * 获取当天抽取次数
	 * 
	 * @param userId
	 * @return
	 */
	public int getDateDrawCount(String userId);
}
