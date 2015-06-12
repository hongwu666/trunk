package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.SystemBlackRoomConfigDao;
import com.lodogame.model.SystemBlackRoomConfig;

public class SystemBlackRoomConfigDaoMysqlImpl implements SystemBlackRoomConfigDao {

	@Autowired
	private Jdbc jdbc;

	private static String table = "system_black_room_config";

	public final static String columns = "*";

	@Override
	public SystemBlackRoomConfig getBlackRoomConfigByTime(int time) {

		String sql = "SELECT " + columns + " FROM " + table + " WHERE challenge_time = ? LIMIT 1;";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(time);
		return this.jdbc.get(sql, SystemBlackRoomConfig.class, parameter);
	}

	@Override
	public List<SystemBlackRoomConfig> getBlackRoomConfigByTime() {
		String sql = "SELECT " + columns + " FROM " + table + ";";
		return this.jdbc.getList(sql, SystemBlackRoomConfig.class);
	}

}
