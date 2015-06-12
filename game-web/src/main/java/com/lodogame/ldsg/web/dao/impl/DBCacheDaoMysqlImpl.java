package com.lodogame.ldsg.web.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.ldsg.web.dao.DbCacheDao;
import com.lodogame.ldsg.web.model.DbCache;

public class DBCacheDaoMysqlImpl implements DbCacheDao {

	@Autowired
	private Jdbc jdbc;

	@Override
	public boolean replace(String key, String value) {
		DbCache dbCache = new DbCache();
		dbCache.setCacheKey(key);
		dbCache.setCacheValue(value);
		this.jdbc.replace(dbCache);
		return true;
	}

	@Override
	public String get(String key) {
		String sql = "SELECT * FROM db_cache WHERE cache_key = ? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(key);
		DbCache dbCache = this.jdbc.get(sql, DbCache.class, parameter);
		if (dbCache != null) {
			return dbCache.getCacheValue();
		}
		return "";
	}
}
