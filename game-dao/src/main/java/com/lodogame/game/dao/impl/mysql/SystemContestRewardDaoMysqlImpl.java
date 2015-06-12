package com.lodogame.game.dao.impl.mysql;


import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.SystemContestRewardDao;
import com.lodogame.model.SystemContestReward;

public class SystemContestRewardDaoMysqlImpl implements SystemContestRewardDao {

	@Autowired
	private Jdbc jdbc;

	private String table = "system_contest_reward";

	@Override
	public SystemContestReward getSystemContestReward(int rewardId) {
		String sql = "SELECT * FROM " + table + " WHERE reward_id = ? LIMIT 1";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(rewardId);
		
		return this.jdbc.get(sql, SystemContestReward.class, parameter);
	}

}
