package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.game.dao.SystemVipLevelDao;
import com.lodogame.model.SystemVipLevel;

public class SystemVipLevelDaoMysqlImpl implements SystemVipLevelDao {

	@Autowired
	private Jdbc jdbc;

	@Override
	public SystemVipLevel get(int vipLevel) {
		throw new NotImplementedException();
	}

	@Override
	public SystemVipLevel getBuyMoney(int money) {
		throw new NotImplementedException();
	}

	@Override
	public List<SystemVipLevel> getList() {
		String sql = "SELECT * FROM system_vip_level";
		return this.jdbc.getList(sql, SystemVipLevel.class);
	}
}
