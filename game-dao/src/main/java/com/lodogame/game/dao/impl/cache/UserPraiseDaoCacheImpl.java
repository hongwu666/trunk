package com.lodogame.game.dao.impl.cache;

import java.util.Date;

import com.lodogame.game.dao.UserPraiseDao;
import com.lodogame.game.dao.impl.mysql.UserPraiseDaoMysqlImpl;
import com.lodogame.game.dao.impl.redis.UserPraiseDaoRedisImpl;
import com.lodogame.model.UserPraise;

public class UserPraiseDaoCacheImpl implements UserPraiseDao {

	private UserPraiseDaoMysqlImpl userPraiseDaoMysqlImpl;
	private UserPraiseDaoRedisImpl userPraiseDaoRedisImpl;
	public void setUserPraiseDaoMysqlImpl(
			UserPraiseDaoMysqlImpl userPraiseDaoMysqlImpl) {
		this.userPraiseDaoMysqlImpl = userPraiseDaoMysqlImpl;
	}


	public void setUserPraiseDaoRedisImpl(
			UserPraiseDaoRedisImpl userPraiseDaoRedisImpl) {
		this.userPraiseDaoRedisImpl = userPraiseDaoRedisImpl;
	}


	@Override
	public UserPraise get(String uid, String praisedUserId) {
		UserPraise userPraise = userPraiseDaoRedisImpl.get(uid, praisedUserId);
		if (userPraise == null) {
			userPraise = userPraiseDaoMysqlImpl.get(uid, praisedUserId);
			if (userPraise != null) {
				userPraiseDaoRedisImpl.add(userPraise);
			}
		}
		return userPraise;
	}


	@Override
	public boolean add(UserPraise userPraise) {
		boolean success = userPraiseDaoMysqlImpl.add(userPraise);
		
		if (success) {
			success = userPraiseDaoRedisImpl.add(userPraise);
		}
		return success;
	}


	@Override
	public boolean update(String uid, String praisedUserId, Date updatedTime) {
		boolean success = userPraiseDaoMysqlImpl.update(uid, praisedUserId, updatedTime);
		if (success) {
			userPraiseDaoRedisImpl.update(uid, praisedUserId, updatedTime);
		}
		return success;
	}


	@Override
	public int getTodayPraiseNum(String uid) {
		return userPraiseDaoMysqlImpl.getTodayPraiseNum(uid);
	}
	@Override
	public int getTodayPraisedNum(String uid){
		return userPraiseDaoMysqlImpl.getTodayPraisedNum(uid);
	}

}
