package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.SystemCostReward;

public interface SystemCostRewardDao {
	/**
	 * 根据id获得奖励
	 * 
	 * @param pay
	 * @return
	 */
	public SystemCostReward get(int activityId, int id);

	/**
	 * 根据类型获得奖励列表
	 * 
	 * @return
	 */
	public List<SystemCostReward> getList(int activityId);

}
