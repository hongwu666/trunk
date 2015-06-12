package com.lodogame.game.dao.impl.mysql;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.UserArenaRewardLogDao;
import com.lodogame.model.UserArenaRewardLog;

public class UserArenaRewardLogDaoMysqlImpl implements UserArenaRewardLogDao {

	@Autowired
	private Jdbc jdbc;
	
	@Override
	public boolean add(UserArenaRewardLog userArenaRewardLog) {
		return jdbc.insert(userArenaRewardLog) > 0;
	}

	@Override
	public UserArenaRewardLog get(String userId,int type){
		String sql = "select * from user_arena_reward_log where user_id = ? and reward_type = ? limit 1";
		
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(type);
		
		return jdbc.get(sql, UserArenaRewardLog.class, parameter);
	}

	@Override
	public boolean clear() {
		String sql = "delete from user_arena_reward_log";
		return jdbc.update(sql, new SqlParameter()) > 0;
	}

}
