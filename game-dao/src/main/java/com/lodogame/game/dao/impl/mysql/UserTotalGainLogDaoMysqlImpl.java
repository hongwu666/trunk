package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserTotalGainLogDao;

public class UserTotalGainLogDaoMysqlImpl implements UserTotalGainLogDao {

	private String table = "user_total_gain_log";

	@Autowired
	private Jdbc jdbc;

	@Override
	public int getUserTotalGain(String userId, int type) {

		String sql = "SELECT amount FROM " + table + " WHERE user_id = ? AND type = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(type);

		return this.jdbc.getInt(sql, parameter);
	}

	@Override
	public boolean addUserTotalGain(String userId, int type, int amount) {

		String sql = "INSERT INTO " + table + "(user_id, type, amount, created_time, updated_time) VALUES(?, ?, ?, now(), now()) ";
		sql += "ON DUPLICATE KEY update amount = amount + VALUES(amount), updated_time = now()";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(type);
		parameter.setInt(amount);

		return this.jdbc.update(sql, parameter) > 0;
	}

}
