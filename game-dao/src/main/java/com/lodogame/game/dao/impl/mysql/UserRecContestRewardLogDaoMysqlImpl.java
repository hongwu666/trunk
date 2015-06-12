package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.game.dao.UserRecContestRewardLogDao;
import com.lodogame.model.UserRecContestRewardLog;

public class UserRecContestRewardLogDaoMysqlImpl implements UserRecContestRewardLogDao {

	@Autowired
	private Jdbc jdbc;

	@Override
	public boolean addUserRecContestRewardLog(
			UserRecContestRewardLog userRecContestRewardLog) {
		return this.jdbc.insert(userRecContestRewardLog) > 0;
	}
}
