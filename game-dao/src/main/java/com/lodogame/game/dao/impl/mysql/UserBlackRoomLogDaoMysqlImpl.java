package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserBlackRoomLogDao;
import com.lodogame.model.UserBlackRoomLog;

public class UserBlackRoomLogDaoMysqlImpl implements UserBlackRoomLogDao {

	@Autowired
	private Jdbc jdbc;
	
	private static String table = "user_black_room_log";
	
	public final static String columns = "*";
	@Override
	public UserBlackRoomLog getUserBlackRoomLog(String userId, int type) {
		String sql = "SELECT " + columns + " FROM " + table + " WHERE user_id = ? AND type = ? LIMIT 1;";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(type);

		return this.jdbc.get(sql, UserBlackRoomLog.class, parameter);
	}

	@Override
	public boolean updateUserBlackRoomLog(UserBlackRoomLog userBlackRoomLog) {
		String sql = "UPDATE  " + table + " SET  time = ?, update_time = ?  WHERE user_id = ? AND type = ?  ;";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(userBlackRoomLog.getTime());
		parameter.setObject(userBlackRoomLog.getUpdateTime());
		parameter.setString(userBlackRoomLog.getUserId());
		parameter.setInt(userBlackRoomLog.getType());
		
		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean addUserBlackRoomLog(UserBlackRoomLog userBlackRoomLog) {
		return this.jdbc.insert(userBlackRoomLog) > 0;
	}

	

}
