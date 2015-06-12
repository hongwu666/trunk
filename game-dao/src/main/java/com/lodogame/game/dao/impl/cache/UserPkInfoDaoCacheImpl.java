package com.lodogame.game.dao.impl.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;

import com.lodogame.game.dao.UserPkInfoDao;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.model.UserPkInfo;

public class UserPkInfoDaoCacheImpl implements UserPkInfoDao, ReloadAble {

	private UserPkInfoDao userPkInfoDaoMysqlImpl;

	public void setUserPkInfoDaoMysqlImpl(UserPkInfoDao userPkInfoDaoMysqlImpl) {
		this.userPkInfoDaoMysqlImpl = userPkInfoDaoMysqlImpl;
	}

	private Map<Integer, UserPkInfo> cache = new ConcurrentHashMap<Integer, UserPkInfo>();

	@Override
	public void reload() {

	}

	@Override
	public void init() {

		List<UserPkInfo> list = this.userPkInfoDaoMysqlImpl.getList();
		for (UserPkInfo userPkInfo : list) {
			cache.put(userPkInfo.getRank(), userPkInfo);
		}
	}

	@Override
	public boolean add(UserPkInfo userPkInfo) {
		this.userPkInfoDaoMysqlImpl.add(userPkInfo);
		this.cache.put(userPkInfo.getRank(), userPkInfo);
		return true;
	}

	@Override
	public boolean add(List<UserPkInfo> userPkInfoList) {
		this.userPkInfoDaoMysqlImpl.add(userPkInfoList);
		for (UserPkInfo userPkInfo : userPkInfoList) {
			this.cache.put(userPkInfo.getRank(), userPkInfo);
		}
		return true;
	}

	@Override
	public UserPkInfo getByRank(int rank) {
		return this.cache.get(rank);
	}

	@Override
	public UserPkInfo getByUserId(String userId) {

		for (UserPkInfo userPkInfo : this.cache.values()) {

			if (StringUtils.equals(userId, userPkInfo.getUserId())) {
				return userPkInfo;
			}
		}

		return null;

	}

	@Override
	public boolean update(UserPkInfo userPkInfo, String targetUserId) {

		this.cache.put(userPkInfo.getRank(), userPkInfo);

		if (targetUserId != null) {
			int random = RandomUtils.nextInt(1000);
			random = Integer.valueOf("-" + random);
			this.userPkInfoDaoMysqlImpl.updateRank(targetUserId, random);
		}

		this.userPkInfoDaoMysqlImpl.update(userPkInfo, targetUserId);

		return true;
	}

	@Override
	public List<UserPkInfo> getList() {
		throw new NotImplementedException();
	}

	@Override
	public boolean updateRank(String userId, int random) {
		throw new NotImplementedException();
	}

	@Override
	public int getLastRank() {
		return this.cache.size();
	}

	@Override
	public List<UserPkInfo> getRangeRank(int lowRank, int upperRank, String userId) {
		if (cache.isEmpty()) {
			return this.userPkInfoDaoMysqlImpl.getRangeRank(lowRank, upperRank, userId);
		}
		List<UserPkInfo> list = new ArrayList<UserPkInfo>();
		UserPkInfo userPkInfo = getByUserId(userId);
		if (null == userPkInfo) {
			userPkInfo = new UserPkInfo();
			userPkInfo.setRank(getLastRank());
		}
		int i = lowRank;
		while (i < upperRank) {
			if (i == userPkInfo.getRank()) {
				i++;// 容错
				continue;
			}
			UserPkInfo userPkInfo2 = getByRank(i++);
			if (userPkInfo2 != null) {
				list.add(userPkInfo2);
			}
		}
		return list;
	}

}
