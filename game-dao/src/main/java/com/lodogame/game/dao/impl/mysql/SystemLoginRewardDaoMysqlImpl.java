package com.lodogame.game.dao.impl.mysql;

import java.util.Collection;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.game.dao.SystemLoginRewardDao;
import com.lodogame.model.SystemLoginReward;

public class SystemLoginRewardDaoMysqlImpl implements SystemLoginRewardDao {

	private String table = "system_30_login_reward";

	@Autowired
	private Jdbc jdbc;

	@Override
	public SystemLoginReward getSystemLoginRewardByDay(int day) {
		throw new NotImplementedException();
	}

	@Override
	public Collection<SystemLoginReward> getList() {

		String sql = "SELECT * FROM " + table;
		return this.jdbc.getList(sql, SystemLoginReward.class);
	}

}
