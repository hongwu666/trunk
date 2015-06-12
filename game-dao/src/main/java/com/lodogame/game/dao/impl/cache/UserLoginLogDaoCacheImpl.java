package com.lodogame.game.dao.impl.cache;

import com.lodogame.game.dao.UserLoginLogDao;
import com.lodogame.game.dao.impl.mysql.UserLoginLogDaoMysqlImpl;
import com.lodogame.game.dao.impl.redis.UserLoginLogDaoRedisImpl;
import com.lodogame.model.UserLoginLog;

public class UserLoginLogDaoCacheImpl implements UserLoginLogDao {

	private UserLoginLogDaoMysqlImpl userLoginLogDaoMysqlImpl;
	private UserLoginLogDaoRedisImpl userLoginLogDaoRedisImpl;

	public void setUserLoginLogDaoMysqlImpl(
			UserLoginLogDaoMysqlImpl userLoginLogDaoMysqlImpl) {
		this.userLoginLogDaoMysqlImpl = userLoginLogDaoMysqlImpl;
	}


	public void setUserLoginLogDaoRedisImpl(
			UserLoginLogDaoRedisImpl userLoginLogDaoRedisImpl) {
		this.userLoginLogDaoRedisImpl = userLoginLogDaoRedisImpl;
	}


	@Override
	public UserLoginLog get(String userId) {
		UserLoginLog log = userLoginLogDaoRedisImpl.get(userId);
		
		if (log == null) {
			log = userLoginLogDaoMysqlImpl.get(userId);
			if (log != null) {
				userLoginLogDaoRedisImpl.add(log);
			}
		}
		return log;
	}


	@Override
	public boolean add(UserLoginLog loginLog) {
		boolean success = userLoginLogDaoMysqlImpl.add(loginLog);
		if (success) {
			success = userLoginLogDaoRedisImpl.add(loginLog);
		}
		return success;
	}


	@Override
	public boolean update(UserLoginLog loginLog) {
		boolean success = userLoginLogDaoMysqlImpl.update(loginLog);
		if (success) {
			success = userLoginLogDaoRedisImpl.update(loginLog);
		}
		return success;
	}


	@Override
	public boolean updateRecStatus(String userId, String recStatus) {
		boolean success = userLoginLogDaoMysqlImpl.updateRecStatus(userId, recStatus);
		if (success) {
			success = userLoginLogDaoRedisImpl.updateRecStatus(userId, recStatus);
		}
				
		return success; 
				
	}

}
