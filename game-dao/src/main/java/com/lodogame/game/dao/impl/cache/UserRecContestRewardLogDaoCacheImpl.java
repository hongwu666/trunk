package com.lodogame.game.dao.impl.cache;


import com.lodogame.game.dao.UserRecContestRewardLogDao;
import com.lodogame.model.UserRecContestRewardLog;

public class UserRecContestRewardLogDaoCacheImpl implements UserRecContestRewardLogDao {

	private UserRecContestRewardLogDao UserRecContestRewardLogDaoMysqlImpl;

	public void setUserRecContestRewardLogDaoMysqlImpl(
			UserRecContestRewardLogDao userRecContestRewardLogDaoMysqlImpl) {
		UserRecContestRewardLogDaoMysqlImpl = userRecContestRewardLogDaoMysqlImpl;
	}

	@Override
	public boolean addUserRecContestRewardLog(
			UserRecContestRewardLog userRecContestRewardLog) {
		return this.UserRecContestRewardLogDaoMysqlImpl.addUserRecContestRewardLog(userRecContestRewardLog);
	}

}
