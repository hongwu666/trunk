package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.game.dao.SystemForcesDao;
import com.lodogame.model.SystemForces;

public class SystemForcesDaoMysqlImpl implements SystemForcesDao {

	private final static String table = "system_forces";

	private final static String columns = "*";

	@Autowired
	private Jdbc jdbc;

	public List<SystemForces> getSysForcesList() {

		String sql = "SELECT " + columns + " FROM " + table + " ORDER BY forces_id ASC";

		return this.jdbc.getList(sql, SystemForces.class);
	}

	@Override
	public SystemForces get(int forcesId) {
		throw new NotImplementedException();
	}

	@Override
	public List<SystemForces> getSystemForcesByPreForcesId(int preForcesId) {
		throw new NotImplementedException();
	}

	@Override
	public List<SystemForces> getSystemForcesByGroupId(int forcesGroupId) {
		throw new NotImplementedException();
	}

	@Override
	public SystemForces getLastForcesByGroupId(int forcesGroupId) {
		throw new NotImplementedException();
	}
}
