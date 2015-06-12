package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserGiftLogDao;
import com.lodogame.model.UserGiftLog;

public class UserGiftLogDaoMysqlImpl implements UserGiftLogDao {

	@Autowired
	private Jdbc jdbc;
	
	@Override
	public boolean add(UserGiftLog userGiftLog) {
		return this.jdbc.insert(userGiftLog) > 0;
	}

	@Override
	public UserGiftLog get(String userId, int bigType) {
		String sql = "select * from user_gift_log where user_id = ? and big_type = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(bigType);
		return jdbc.get(sql, UserGiftLog.class, parameter);
	}

}
