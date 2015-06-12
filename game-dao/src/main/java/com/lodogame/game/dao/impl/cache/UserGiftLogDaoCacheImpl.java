package com.lodogame.game.dao.impl.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.UserGiftLogDao;
import com.lodogame.game.dao.impl.mysql.UserGiftLogDaoMysqlImpl;
import com.lodogame.model.UserGiftLog;

public class UserGiftLogDaoCacheImpl implements UserGiftLogDao {

	private UserGiftLogDaoMysqlImpl userGiftLogDaoMysqlImpl;

	private Map<String, UserGiftLog> cacheMap = new ConcurrentHashMap<String, UserGiftLog>();

	public void setUserGiftLogDaoMysqlImpl(UserGiftLogDaoMysqlImpl userGiftLogDaoMysqlImpl) {
		this.userGiftLogDaoMysqlImpl = userGiftLogDaoMysqlImpl;
	}

	@Override
	public boolean add(UserGiftLog userGiftLog) {
		String key = userGiftLog.getUserId() + "-" + userGiftLog.getBigType();
		boolean succ = this.userGiftLogDaoMysqlImpl.add(userGiftLog);
		if (succ) {
			cacheMap.put(key, userGiftLog);
		}
		return succ;
	}

	@Override
	public UserGiftLog get(String userId, int bigType) {
		String key = userId + "-" + bigType;
		if (cacheMap.containsKey(key)) {
			return this.cacheMap.get(key);
		}
		UserGiftLog userGiftLog = this.userGiftLogDaoMysqlImpl.get(userId, bigType);
		if (null != userGiftLog) {
			cacheMap.put(key, userGiftLog);
			return userGiftLog;
		}
		return null;
	}

}
