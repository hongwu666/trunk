package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserLoginLogDao;
import com.lodogame.model.UserLoginLog;

public class UserLoginLogDaoMysqlImpl implements UserLoginLogDao {

	@Autowired
	private Jdbc jdbc;
	
	private static final String table = "user_login_log";
	
	@Override
	public UserLoginLog get(String userId) {
		String sql = "SELECT * FROM " + table + " WHERE user_id = ?";
		SqlParameter param = new SqlParameter();
		param.setString(userId);
		return this.jdbc.get(sql, UserLoginLog.class, param);
	}

	@Override
	public boolean add(UserLoginLog loginLog) {
		return this.jdbc.insert(loginLog) > 0;
	}

	@Override
	public boolean update(UserLoginLog loginLog) {
		String sql = "UPDATE " + table + " SET day = ?, last_login_date = ?, rec_today = ?, rec_status = ?, updated_time = now() WHERE user_id = ?";
		SqlParameter param = new SqlParameter();
		param.setInt(loginLog.getDay());
		param.setString(loginLog.getLastLoginDate());
		param.setInt(loginLog.getRecToday());
		param.setString(loginLog.getRecStatus());
		param.setString(loginLog.getUserId());
		return this.jdbc.update(sql, param) > 0;
	}

	@Override
	public boolean updateRecStatus(String userId, String recStatus) {
		String sql = "UPDATE " + table + " SET rec_status = ?, rec_today = 1 WHERE user_id = ?";
		SqlParameter param = new SqlParameter();
		param.setString(recStatus);
		param.setString(userId);
		return this.jdbc.update(sql, param) > 0;
	}
}
