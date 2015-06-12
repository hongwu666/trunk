package com.lodogame.game.dao;

import java.util.Collection;

import com.lodogame.model.SystemLoginReward;

/**
 * 用户30天登入奖励dao
 * 
 * @author zyz
 * 
 */
public interface SystemLoginRewardDao {

	/**
	 * 获取某一天的奖励
	 * 
	 * @param day
	 * @return
	 */
	public SystemLoginReward getSystemLoginRewardByDay(int day);

	/**
	 * 获取某一天的奖励
	 * 
	 * @param day
	 * @return
	 */
	public Collection<SystemLoginReward> getList();

}
