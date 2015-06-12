package com.lodogame.game.dao.impl.cache;

import java.util.Date;

import com.lodogame.game.dao.UserSweepInfoDao;
import com.lodogame.game.dao.clearcache.ClearCacheOnLoginOut;
import com.lodogame.game.dao.impl.mysql.UserSweepInfoDaoMysqlImpl;
import com.lodogame.game.dao.impl.redis.UserSweepInfoDaoRedisImpl;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.model.UserSweepInfo;

public class UserSweepInfoDaoCacheImpl implements UserSweepInfoDao, ReloadAble,ClearCacheOnLoginOut {

	private UserSweepInfoDaoMysqlImpl userSweepInfoDaoMysqlImpl;
	private UserSweepInfoDaoRedisImpl userSweepInfoDaoRedisImpl;

	@Override
	public boolean add(UserSweepInfo sweepInfo) {
		if (userSweepInfoDaoMysqlImpl.add(sweepInfo)) {
			return userSweepInfoDaoRedisImpl.add(sweepInfo);
		}
		return false;
	}

	@Override
	public UserSweepInfo getCurrentSweep(String userId) {
		UserSweepInfo sweepInfo = userSweepInfoDaoRedisImpl.getCurrentSweep(userId);
		if (sweepInfo == null) {
			sweepInfo = userSweepInfoDaoMysqlImpl.getCurrentSweep(userId);
			userSweepInfoDaoRedisImpl.add(sweepInfo);
		}
		return sweepInfo;
	}

	@Override
	public boolean updateSweepComplete(String userId, Date date) {
		if (userSweepInfoDaoMysqlImpl.updateSweepComplete(userId, date)) {
			return userSweepInfoDaoRedisImpl.updateSweepComplete(userId, date);
		}
		return false;
	}

	public UserSweepInfoDaoMysqlImpl getUserSweepInfoDaoMysqlImpl() {
		return userSweepInfoDaoMysqlImpl;
	}

	public void setUserSweepInfoDaoMysqlImpl(UserSweepInfoDaoMysqlImpl userSweepInfoDaoMysqlImpl) {
		this.userSweepInfoDaoMysqlImpl = userSweepInfoDaoMysqlImpl;
	}

	public UserSweepInfoDaoRedisImpl getUserSweepInfoDaoRedisImpl() {
		return userSweepInfoDaoRedisImpl;
	}

	public void setUserSweepInfoDaoRedisImpl(UserSweepInfoDaoRedisImpl userSweepInfoDaoRedisImpl) {
		this.userSweepInfoDaoRedisImpl = userSweepInfoDaoRedisImpl;
	}

	@Override
	public void reload() {

	}

	@Override
	public void init() {

	}
	@Override
	public void clearOnLoginOut(String userId) throws Exception {
		userSweepInfoDaoRedisImpl.deleteEntry(userId);
	}
}
