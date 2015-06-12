package com.lodogame.game.dao;

import com.lodogame.model.SystemContestReward;


public interface SystemContestRewardDao {

	/**
	 * 获取擂台赛的奖励
	 * 
	 * @param userId
	 * @return
	 */
	public SystemContestReward getSystemContestReward(int  rewardId);

}
