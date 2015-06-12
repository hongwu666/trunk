package com.lodogame.game.dao.impl.mysql;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserCostRewardDao;
import com.lodogame.model.UserCostReward;

public class UserCostRewardDaoMysqlImpl implements UserCostRewardDao {

	@Autowired
	private Jdbc jdbc;

	@Override
	public boolean add(UserCostReward userCostReward) {
		return this.jdbc.insert(userCostReward) > 0;
	}

	@Override
	public UserCostReward getUserCostReward(String userId, int aid, int rid, Date startTime, Date endTime) {

		String sql = "SELECT * FROM user_cost_reward WHERE user_id = ? AND activity_id = ? AND reward_id = ? AND created_time >= ? AND created_time <= ? LIMIT 1";

		SqlParameter spm = new SqlParameter();
		spm.setString(userId);
		spm.setInt(aid);
		spm.setInt(rid);
		spm.setDate(startTime);
		spm.setDate(endTime);

		return this.jdbc.get(sql, UserCostReward.class, spm);

	}

}
