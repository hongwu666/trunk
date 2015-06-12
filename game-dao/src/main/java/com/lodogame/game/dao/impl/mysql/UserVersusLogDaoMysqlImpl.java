package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.game.dao.UserVersusLogDao;
import com.lodogame.model.UserVersusLog;

public class UserVersusLogDaoMysqlImpl implements UserVersusLogDao {

	@Autowired
	private Jdbc jdbc;

	@Override
	public boolean add(UserVersusLog userVersusLog) {
		return jdbc.insert(userVersusLog) > 0;
	}
}
