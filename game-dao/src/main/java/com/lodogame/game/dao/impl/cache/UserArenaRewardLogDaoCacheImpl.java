package com.lodogame.game.dao.impl.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.UserArenaRewardLogDao;
import com.lodogame.game.dao.impl.mysql.UserArenaRewardLogDaoMysqlImpl;
import com.lodogame.model.UserArenaRewardLog;

public class UserArenaRewardLogDaoCacheImpl implements UserArenaRewardLogDao {

	private Map<String, UserArenaRewardLog> cacheMap = new ConcurrentHashMap<String, UserArenaRewardLog>();
	
	private UserArenaRewardLogDaoMysqlImpl userArenaRewardLogDaoMysqlImpl;
	
	public void setUserArenaRewardLogDaoMysqlImpl(
			UserArenaRewardLogDaoMysqlImpl userArenaRewardLogDaoMysqlImpl) {
		this.userArenaRewardLogDaoMysqlImpl = userArenaRewardLogDaoMysqlImpl;
	}

	@Override
	public boolean add(UserArenaRewardLog userArenaRewardLog) {
		String key = userArenaRewardLog.getUserId() + "-" + userArenaRewardLog.getRewardType();
		cacheMap.put(key, userArenaRewardLog);
		return this.userArenaRewardLogDaoMysqlImpl.add(userArenaRewardLog);
	}

	@Override
	public UserArenaRewardLog get(String userId, int type) {
		String key = userId + "-" + type;
		if(cacheMap.containsKey(key)){
			return cacheMap.get(key);
		}
		UserArenaRewardLog userArenaRewardLog = this.userArenaRewardLogDaoMysqlImpl.get(userId, type);
		if(null != userArenaRewardLog){
			cacheMap.put(key, userArenaRewardLog);
			return userArenaRewardLog;
		}
		
		return null;
	}

	@Override
	public boolean clear() {
		cacheMap.clear();
		return userArenaRewardLogDaoMysqlImpl.clear();
	}

}
