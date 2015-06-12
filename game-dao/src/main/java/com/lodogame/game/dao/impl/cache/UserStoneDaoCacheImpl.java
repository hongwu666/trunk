package com.lodogame.game.dao.impl.cache;

import java.util.List;

import org.apache.commons.lang.NotImplementedException;

import com.lodogame.game.dao.UserStoneDao;
import com.lodogame.game.dao.impl.mysql.UserStoneDaoMysqlImpl;
import com.lodogame.model.UserStone;

public class UserStoneDaoCacheImpl implements UserStoneDao {

	private UserStoneDao userStoneDaoMysqlImpl;

	private UserStoneDao userStoneDaoRedisImpl;

	public void setUserStoneDaoMysqlImpl(UserStoneDaoMysqlImpl userStoneDaoMysqlImpl) {
		this.userStoneDaoMysqlImpl = userStoneDaoMysqlImpl;
	}

	public void setUserStoneDaoRedisImpl(UserStoneDao userStoneDaoRedisImpl) {
		this.userStoneDaoRedisImpl = userStoneDaoRedisImpl;
	}

	@Override
	public boolean addUserStone(String userId, int stoneId, int stoneNum) {

		boolean success = this.userStoneDaoMysqlImpl.addUserStone(userId, stoneId, stoneNum);
		if (success) {
			this.userStoneDaoRedisImpl.addUserStone(userId, stoneId, stoneNum);
		}

		return success;

	}

	@Override
	public boolean reduceUserStone(String userId, int stoneId, int stoneNum) {

		boolean success = this.userStoneDaoMysqlImpl.reduceUserStone(userId, stoneId, stoneNum);
		if (success) {
			this.userStoneDaoRedisImpl.reduceUserStone(userId, stoneId, stoneNum);
		}

		return success;

	}

	@Override
	public UserStone get(String userId, int stoneId) {
		UserStone userStone = this.userStoneDaoRedisImpl.get(userId, stoneId);
		if (userStone == null) {
			List<UserStone> list = this.userStoneDaoMysqlImpl.getUserStoneList(userId);
			this.userStoneDaoRedisImpl.initCache(userId, list);
		}
		return this.userStoneDaoRedisImpl.get(userId, stoneId);
	}

	@Override
	public List<UserStone> getUserStoneList(String userId) {
		List<UserStone> list = this.userStoneDaoRedisImpl.getUserStoneList(userId);
		if (list == null || list.isEmpty()) {
			list = userStoneDaoMysqlImpl.getUserStoneList(userId);
			this.userStoneDaoRedisImpl.initCache(userId, list);
		}

		return list;

	}

	@Override
	public boolean initCache(String userId, List<UserStone> list) {
		throw new NotImplementedException();
	}

	@Override
	public boolean deleteZero(String userId) {
		return this.userStoneDaoMysqlImpl.deleteZero(userId);

	}

}
