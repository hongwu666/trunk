package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.game.dao.SystemLoginReward7Dao;
import com.lodogame.model.SystemLoginReward7;

public class SystemLoginReward7DaoMysqlImpl implements SystemLoginReward7Dao {

	private static final String table = "system_login_reward7";
	
	@Autowired
	private Jdbc jdbc;
	
	@Override
	public List<SystemLoginReward7> getAll() {
		String sql = "SELECT * FROM " + table;
		return this.jdbc.getList(sql, SystemLoginReward7.class);
	}

	@Override
	public List<SystemLoginReward7> getByDay(int day) {
		throw new NotImplementedException();
	}

}
