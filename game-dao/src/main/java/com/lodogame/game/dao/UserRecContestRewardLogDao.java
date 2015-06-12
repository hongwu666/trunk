package com.lodogame.game.dao;

import com.lodogame.model.UserRecContestRewardLog;



public interface UserRecContestRewardLogDao {

	/**
	 * 增加擂台赛领取记录
	 * 
	 * @param userRecContestRewardLog
	 * @return
	 */
	public boolean addUserRecContestRewardLog(UserRecContestRewardLog userRecContestRewardLog);

}
