package com.lodogame.game.dao.impl.mysql;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserMonthlyCardDao;
import com.lodogame.model.UserMonthlyCard;

public class UserMonthlyCardDaoMysqlImpl implements UserMonthlyCardDao {

	@Autowired
	private Jdbc jdbc;
	
	private static final String table = "user_monthly_card";
	
	@Override
	public UserMonthlyCard get(String userId) {
		String sql = "SELECT * FROM " + table + " WHERE user_id = ?";
		SqlParameter parm = new SqlParameter();
		parm.setString(userId);
		return this.jdbc.get(sql, UserMonthlyCard.class, parm);
	}

	@Override
	public boolean add(UserMonthlyCard card) {
		return this.jdbc.insert(card) > 0;
	}

	@Override
	public boolean updateDueTime(String userId, Date dueTime) {
		String sql = "UPDATE " + table + " SET due_time = ? WHERE user_id = ?";
		SqlParameter param = new SqlParameter();
		param.setObject(dueTime);
		param.setString(userId);
		return this.jdbc.update(sql, param) > 0;
	}

}
