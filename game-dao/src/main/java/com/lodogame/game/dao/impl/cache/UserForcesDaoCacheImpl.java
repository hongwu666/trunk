package com.lodogame.game.dao.impl.cache;

import java.util.List;
import java.util.Map;

import com.lodogame.game.dao.UserForcesDao;
import com.lodogame.game.dao.clearcache.ClearCacheOnLoginOut;
import com.lodogame.game.dao.impl.mysql.UserForcesDaoMysqlImpl;
import com.lodogame.game.dao.impl.redis.UserForcesDaoRedisImpl;
import com.lodogame.model.UserForces;
import com.lodogame.model.UserForcesCount;

public class UserForcesDaoCacheImpl implements UserForcesDao, ClearCacheOnLoginOut {

	private UserForcesDaoMysqlImpl userForcesDaoMysqlImpl;

	private UserForcesDaoRedisImpl userForcesDaoRedisImpl;

	@Override
	public List<UserForces> getUserForcesList(String userId, int sceneId) {
		List<UserForces> result = userForcesDaoRedisImpl.getUserForcesList(userId, sceneId);
		if (result != null) {
			return result;
		}
		result = userForcesDaoMysqlImpl.getUserForcesList(userId, sceneId);
		if (result != null) {
			userForcesDaoRedisImpl.initUserSceneCache(userId, sceneId, result);
		}
		return result;
	}

	@Override
	public boolean add(UserForces userForces) {
		boolean result = this.userForcesDaoMysqlImpl.add(userForces);
		if (result) {
			userForcesDaoRedisImpl.add(userForces);
		}
		return result;
	}

	@Override
	public UserForces getUserCurrentForces(String userId, int forcesType) {
		UserForces userForces = userForcesDaoRedisImpl.getUserCurrentForces(userId, forcesType);
		if (userForces != null) {
			return userForces;
		}
		userForces = this.userForcesDaoMysqlImpl.getUserCurrentForces(userId, forcesType);
		userForcesDaoRedisImpl.initUserForceTypeCache(userId, forcesType, userForces);
		return userForces;
	}

	@Override
	public UserForces get(String userId, int forcesGroup) {
		UserForces userForces = userForcesDaoRedisImpl.get(userId, forcesGroup);
		if (userForces != null) {
			return userForces;
		}
		userForces = this.userForcesDaoMysqlImpl.get(userId, forcesGroup);

		userForcesDaoRedisImpl.initUserForceCache(userId, forcesGroup, userForces);
		return userForces;
	}

	@Override
	public boolean updateStatus(String userId, int forcesGroup, int status, int times) {
		boolean result = this.userForcesDaoMysqlImpl.updateStatus(userId, forcesGroup, status, times);
		if (result) {
			userForcesDaoRedisImpl.updateStatus(userId, forcesGroup, status, times);
		}
		return result;
	}

	@Override
	public boolean updateTimes(String uid, int times, List<Integer> forcesGroups) {
		boolean result = this.userForcesDaoMysqlImpl.updateTimes(uid, times, forcesGroups);
		if (result) {
			userForcesDaoRedisImpl.updateTimes(uid, times, forcesGroups);
		}
		return result;
	}

	@Override
	public boolean updateTimes(String userId, int forcesGroup, int times) {
		boolean result = this.userForcesDaoMysqlImpl.updateTimes(userId, forcesGroup, times);
		if (result) {
			userForcesDaoRedisImpl.updateTimes(userId, forcesGroup, times);
		}
		return result;
	}

	@Override
	public List<UserForcesCount> listOrderByForceCntDesc(int offset, int size) {
		return this.userForcesDaoMysqlImpl.listOrderByForceCntDesc(offset, size);
	}

	@Override
	public boolean resetForcesTimes(String userId, int groupId) {
		boolean result = this.userForcesDaoMysqlImpl.resetForcesTimes(userId, groupId);
		if (result) {
			userForcesDaoRedisImpl.resetForcesTimes(userId, groupId);
		}
		return result;
	}

	@Override
	public long getAmendEmbattleTime(String userId) {
		return this.userForcesDaoRedisImpl.getAmendEmbattleTime(userId);
	}

	@Override
	public void setAmendEmbattleTime(String userId, long timestamp) {
		this.userForcesDaoRedisImpl.setAmendEmbattleTime(userId, timestamp);
	}

	public void setUserForcesDaoMysqlImpl(UserForcesDaoMysqlImpl userForcesDaoMysqlImpl) {
		this.userForcesDaoMysqlImpl = userForcesDaoMysqlImpl;
	}

	public void setUserForcesDaoRedisImpl(UserForcesDaoRedisImpl userForcesDaoRedisImpl) {
		this.userForcesDaoRedisImpl = userForcesDaoRedisImpl;
	}

	@Override
	public void clearOnLoginOut(String userId) throws Exception {

		userForcesDaoRedisImpl.remove(userId);

		userForcesDaoRedisImpl.removeCache(userId);
	}

	@Override
	public boolean updatePassStar(String userId, int forcesGroup, int passStar) {
		boolean result = this.userForcesDaoMysqlImpl.updatePassStar(userId, forcesGroup, passStar);
		if (result) {
			userForcesDaoRedisImpl.updatePassStar(userId, forcesGroup, passStar);
		}
		return result;
	}

	@Override
	public Map<Integer, Integer> getUserResetTimes(String userId) {
		Map<Integer, Integer> result = userForcesDaoRedisImpl.getUserResetTimes(userId);
		if (result == null || result.isEmpty()) {
			result = this.userForcesDaoMysqlImpl.getUserResetTimes(userId);
			result.put(-1, -1);
			this.userForcesDaoRedisImpl.initUserResetTimesCache(userId, result);
		}
		return result;
	}

	@Override
	public boolean setUserResetTimes(String userId, int forcesGroup, int times) {
		boolean success = this.userForcesDaoMysqlImpl.setUserResetTimes(userId, forcesGroup, times);
		if (success) {
			this.userForcesDaoRedisImpl.setUserResetTimes(userId, forcesGroup, times);
		}
		return success;
	}

	@Override
	public int getUserResetTimes(String userId, int forcesGroup) {
		return this.userForcesDaoRedisImpl.getUserResetTimes(userId, forcesGroup);
	}

}
