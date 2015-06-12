package com.lodogame.ldsg.web.dao;

public interface DbCacheDao {

	public boolean replace(String key, String value);

	public String get(String key);
}
