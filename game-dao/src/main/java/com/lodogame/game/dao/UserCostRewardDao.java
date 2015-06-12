package com.lodogame.game.dao;

import java.util.Date;

import com.lodogame.model.UserCostReward;

public interface UserCostRewardDao {

	/**
	 * 新增用户消耗领取记录
	 * 
	 * @param userPayReward
	 * @return
	 */
	boolean add(UserCostReward userCostReward);

	/**
	 * 获取用户已经领取的订单
	 * 
	 * @param userId
	 * @param aid
	 * @param rid
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public UserCostReward getUserCostReward(String userId, int aid, int rid, Date startTime, Date endTime);
}
