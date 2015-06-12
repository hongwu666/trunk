package com.lodogame.game.dao.impl.mysql;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserPraiseDao;
import com.lodogame.model.UserPraise;

public class UserPraiseDaoMysqlImpl implements UserPraiseDao {

	private static final String table = "user_praise";
	
	@Autowired
	private Jdbc jdbc;
	
	
	@Override
	public UserPraise get(String uid, String praisedUserId) {
		String sql = "SELECT * FROM " + table + " WHERE user_id = ? AND praised_user_id = ?";
		SqlParameter param = new SqlParameter();
		param.setString(uid);
		param.setString(praisedUserId);

		return this.jdbc.get(sql, UserPraise.class, param);
	}


	@Override
	public boolean add(UserPraise userPraise) {
		return this.jdbc.insert(userPraise) > 0;
	}


	@Override
	public boolean update(String uid, String praisedUserId, Date updatedTime) {
		String sql = "UPDATE " + table + " SET updated_time = ? WHERE user_id = ? AND praised_user_id = ?";
		SqlParameter param = new SqlParameter();
		param.setObject(updatedTime);
		param.setString(uid);
		param.setString(praisedUserId);
		return this.jdbc.update(sql, param) > 0;
	}


	@Override
	public int getTodayPraiseNum(String uid) {
		String sql = "SELECT COUNT(id) FROM " + table + " WHERE user_id = ? AND TO_DAYS(updated_time) = TO_DAYS(now())";
		SqlParameter param = new SqlParameter();
		param.setString(uid);
		return this.jdbc.getInt(sql, param);
	}
	
	@Override
	public int getTodayPraisedNum(String uid) {
		String sql = "SELECT COUNT(id) FROM " + table + " WHERE praised_user_id = ? AND TO_DAYS(updated_time) = TO_DAYS(now())";
		SqlParameter param = new SqlParameter();
		param.setString(uid);
		return this.jdbc.getInt(sql, param);
	}
	
}
