package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserDrawDao;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.model.UserDrawLog;

public class UserDrawDaoMysqlImpl implements UserDrawDao {

	@Autowired
	private Jdbc jdbc;

	@Override
	public boolean add(UserDrawLog userDrawLog) {
		return this.jdbc.insert(userDrawLog) > 0;
	}

	@Override
	public int getDateDrawCount(String userId) {

		String sql = "SELECT count(1) as total FROM user_draw_log WHERE user_id = ? AND date = ? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setString(DateUtils.getDate());

		return this.jdbc.getInt(sql, parameter);
	}

}
