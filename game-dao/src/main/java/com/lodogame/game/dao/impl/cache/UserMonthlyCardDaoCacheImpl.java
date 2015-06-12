package com.lodogame.game.dao.impl.cache;

import java.util.Date;

import com.lodogame.game.dao.UserMonthlyCardDao;
import com.lodogame.game.dao.clearcache.ClearCacheOnLoginOut;
import com.lodogame.game.dao.impl.mysql.UserMonthlyCardDaoMysqlImpl;
import com.lodogame.game.dao.impl.redis.UserMonthlyCardDaoRedisImpl;
import com.lodogame.model.UserMonthlyCard;

public class UserMonthlyCardDaoCacheImpl implements UserMonthlyCardDao, ClearCacheOnLoginOut {

	private UserMonthlyCardDaoMysqlImpl userMonthlyCardDaoMysqlImpl;
	private UserMonthlyCardDaoRedisImpl userMonthlyCardDaoRedisImpl;

	public void setUserMonthlyCardDaoMysqlImpl(UserMonthlyCardDaoMysqlImpl userMonthlyCardDaoMysqlImpl) {
		this.userMonthlyCardDaoMysqlImpl = userMonthlyCardDaoMysqlImpl;
	}

	public void setUserMonthlyCardDaoRedisImpl(UserMonthlyCardDaoRedisImpl userMonthlyCardDaoRedisImpl) {
		this.userMonthlyCardDaoRedisImpl = userMonthlyCardDaoRedisImpl;
	}

	@Override
	public UserMonthlyCard get(String userId) {
		UserMonthlyCard card = userMonthlyCardDaoRedisImpl.get(userId);
		if (card == null) {
			card = userMonthlyCardDaoMysqlImpl.get(userId);
			if (card != null) {
				userMonthlyCardDaoRedisImpl.add(card);
			}
		}
		return card;
	}

	@Override
	public void clearOnLoginOut(String userId) throws Exception {
		this.userMonthlyCardDaoRedisImpl.cleanCache(userId);
	}

	@Override
	public boolean add(UserMonthlyCard card) {
		boolean isSuccess = userMonthlyCardDaoMysqlImpl.add(card);
		if (isSuccess) {
			isSuccess = userMonthlyCardDaoRedisImpl.add(card);
		}
		return isSuccess;
	}

	@Override
	public boolean updateDueTime(String userId, Date dueTime) {
		boolean isSuccess = userMonthlyCardDaoMysqlImpl.updateDueTime(userId, dueTime);
		if (isSuccess) {
			isSuccess = userMonthlyCardDaoRedisImpl.updateDueTime(userId, dueTime);
		}
		return isSuccess;
	}

}
