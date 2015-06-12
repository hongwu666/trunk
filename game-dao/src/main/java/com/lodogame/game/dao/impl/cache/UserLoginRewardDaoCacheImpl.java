package com.lodogame.game.dao.impl.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.UserLoginRewardInfoDao;
import com.lodogame.game.dao.clearcache.ClearCacheOnLoginOut;
import com.lodogame.model.UserLoginRewardInfo;

public class UserLoginRewardDaoCacheImpl implements UserLoginRewardInfoDao, ClearCacheOnLoginOut {

	private UserLoginRewardInfoDao userLoginRewardDaoMysqlImpl;

	private Map<String, List<UserLoginRewardInfo>> userLoginRewardCacheMap = new ConcurrentHashMap<String, List<UserLoginRewardInfo>>();

	public void setUserLoginRewardDaoMysqlImpl(UserLoginRewardInfoDao userLoginRewardDaoMysqlImpl) {
		this.userLoginRewardDaoMysqlImpl = userLoginRewardDaoMysqlImpl;
	}

	@Override
	public boolean addUserLoginRewardInfo(UserLoginRewardInfo userLoginRewardInfo) {
		boolean succ = this.userLoginRewardDaoMysqlImpl.addUserLoginRewardInfo(userLoginRewardInfo);
		List<UserLoginRewardInfo> userLoginRewardInfoList = userLoginRewardCacheMap.get(userLoginRewardInfo.getUserId());
		if (userLoginRewardInfoList != null) {
			userLoginRewardInfoList.add(userLoginRewardInfo);
		}
		return succ;
	}

	@Override
	public UserLoginRewardInfo getUserLoginRewardInfoByDay(String userId, int day) {
		if (userLoginRewardCacheMap.containsKey(userId)) {
			for (UserLoginRewardInfo userLoginRewardInfo : userLoginRewardCacheMap.get(userId)) {
				if (userLoginRewardInfo.getDay() == day) {
					return userLoginRewardInfo;
				}
			}
		}
		return this.userLoginRewardDaoMysqlImpl.getUserLoginRewardInfoByDay(userId, day);
	}

	@Override
	public boolean updateUserLoginRewardInfoByDay(String userId, int day, String date, int rewardStatus) {
		boolean succ = this.userLoginRewardDaoMysqlImpl.updateUserLoginRewardInfoByDay(userId, day, date, rewardStatus);
		userLoginRewardCacheMap.remove(userId);
		return succ;
	}

	@Override
	public UserLoginRewardInfo getUserLastLoginRewardInfo(String userId) {
		return this.userLoginRewardDaoMysqlImpl.getUserLastLoginRewardInfo(userId);
	}

	@Override
	public List<UserLoginRewardInfo> getUserLoginRewardInfo(String userId) {
		if (!userLoginRewardCacheMap.containsKey(userId)) {
			List<UserLoginRewardInfo> UserLoginRewardInfoList = this.userLoginRewardDaoMysqlImpl.getUserLoginRewardInfo(userId);
			userLoginRewardCacheMap.put(userId, UserLoginRewardInfoList);
		}
		return userLoginRewardCacheMap.get(userId);
	}

	@Override
	public void clearOnLoginOut(String userId) throws Exception {
		userLoginRewardCacheMap.remove(userId);
	}

}
