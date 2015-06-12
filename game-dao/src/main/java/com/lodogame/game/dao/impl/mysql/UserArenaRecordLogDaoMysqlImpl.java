package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserArenaRecordLogDao;
import com.lodogame.model.UserArenaRecordLog;

public class UserArenaRecordLogDaoMysqlImpl implements UserArenaRecordLogDao {

	@Autowired
	private Jdbc jdbc;
	
	@Override
	public boolean add(UserArenaRecordLog userArenaRecordLog) {
		return this.jdbc.insert(userArenaRecordLog) > 0;
	}

	@Override
	public boolean isEnemy(String userId, String enemyId) {
		String sql = "select count(*) from user_arena_record_log where attack_user_id = ? and defense_user_id = ? and is_revenge = 0 and result = 1";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(enemyId);
		parameter.setString(userId);
		
		return jdbc.getInt(sql, parameter) > 0;
	}

	@Override
	public boolean deleteRevenge(String userId,String enemyId) {
		String sql = "update user_arena_record_log set is_revenge = 1 where attack_user_id = ? and defense_user_id = ? and result = 1";
		
		SqlParameter parameter = new SqlParameter();
		parameter.setString(enemyId);
		parameter.setString(userId);
		
		return jdbc.update(sql, parameter) > 0;
	}

	@Override
	public List<UserArenaRecordLog> getListByAttackUserId(String userId,
			int itemNum) {
		String sql = "select * from user_arena_record_log where attack_user_id = ? or defense_user_id = ? order by created_time desc limit ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setString(userId);
		parameter.setInt(itemNum);
		
		return jdbc.getList(sql, UserArenaRecordLog.class,parameter);
	}

	@Override
	public boolean clearRecord() {
		String sql = "delete from user_arena_record_log";
		return jdbc.update(sql, new SqlParameter()) > 0;
	}

}
