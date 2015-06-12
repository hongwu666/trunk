package com.lodogame.game.dao.impl.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.UserBlackRoomLogDao;
import com.lodogame.model.UserBlackRoomLog;

public class UserBlackRoomLogDaoCacheImpl implements UserBlackRoomLogDao {

	private UserBlackRoomLogDao userBlackRoomLogDaoCacheImpl;
	

	private Map<String, UserBlackRoomLog> cacheMap = new ConcurrentHashMap<String, UserBlackRoomLog>();
	
	public void setUserBlackRoomLogDaoCacheImpl(
			UserBlackRoomLogDao userBlackRoomLogDaoCacheImpl) {
		this.userBlackRoomLogDaoCacheImpl = userBlackRoomLogDaoCacheImpl;
	}

	@Override
	public UserBlackRoomLog getUserBlackRoomLog(String userId, int type) {
		String key = userId + "_" + type;
		if (!cacheMap.containsKey(userId)) {
			UserBlackRoomLog userBlackRoomLog = this.userBlackRoomLogDaoCacheImpl.getUserBlackRoomLog(userId, type);
			if (userBlackRoomLog != null) {
				cacheMap.put(key, userBlackRoomLog);
			}
		}
		
		return cacheMap.get(key);
	}

	@Override
	public boolean updateUserBlackRoomLog(UserBlackRoomLog userBlackRoomLog) {
		boolean succ = this.userBlackRoomLogDaoCacheImpl.updateUserBlackRoomLog(userBlackRoomLog);
		if (succ) {
			cacheMap.put(userBlackRoomLog.getUserId()+"_"+userBlackRoomLog.getType(), userBlackRoomLog);
		}
		return succ;
	}

	@Override
	public boolean addUserBlackRoomLog(UserBlackRoomLog userBlackRoomLog) {
		boolean success =  this.userBlackRoomLogDaoCacheImpl.addUserBlackRoomLog(userBlackRoomLog);
		if (success) {
			cacheMap.put(userBlackRoomLog.getUserId()+"_"+userBlackRoomLog.getType(), userBlackRoomLog);
		}
		return success;
	}



}
