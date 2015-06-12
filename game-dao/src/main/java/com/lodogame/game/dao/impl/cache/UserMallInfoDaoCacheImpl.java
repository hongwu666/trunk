package com.lodogame.game.dao.impl.cache;

import java.util.List;

import org.apache.commons.lang.NotImplementedException;

import com.lodogame.game.dao.UserMallInfoDao;
import com.lodogame.model.UserMallInfo;

public class UserMallInfoDaoCacheImpl implements UserMallInfoDao {

	private UserMallInfoDao userMallInfoDaoMysqlImpl;

	private UserMallInfoDao userMallInfoDaoRedisImpl;

	public void setUserMallInfoDaoMysqlImpl(UserMallInfoDao userMallInfoDaoMysqlImpl) {
		this.userMallInfoDaoMysqlImpl = userMallInfoDaoMysqlImpl;
	}

	public void setUserMallInfoDaoRedisImpl(UserMallInfoDao userMallInfoDaoRedisImpl) {
		this.userMallInfoDaoRedisImpl = userMallInfoDaoRedisImpl;
	}

	@Override
	public boolean add(String userId, int mallId, int totalBuyNum, int dayBuyNum) {

		boolean success = userMallInfoDaoMysqlImpl.add(userId, mallId, totalBuyNum, dayBuyNum);
		if (success) {
			this.userMallInfoDaoRedisImpl.add(userId, mallId, totalBuyNum, dayBuyNum);
		}
		return success;

	}

	@Override
	public UserMallInfo get(String userId, int mallId) {

		UserMallInfo userMallInfo = this.userMallInfoDaoRedisImpl.get(userId, mallId);
		if (userMallInfo == null) {
			List<UserMallInfo> userMallInfoList = userMallInfoDaoMysqlImpl.getUserMallInfoList(userId);
			if (userMallInfoList != null) {

				for (UserMallInfo info : userMallInfoList) {

					if (mallId == info.getMallId()) {
						userMallInfo = info;
					}

					this.userMallInfoDaoRedisImpl.add(info);
				}
			}
		}

		return userMallInfo;
	}

	@Override
	public boolean add(UserMallInfo userMallInfo) {
		throw new NotImplementedException();
	}

	@Override
	public List<UserMallInfo> getUserMallInfoList(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

}
