package com.lodogame.game.dao.impl.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.NotImplementedException;

import com.lodogame.game.dao.UserArenaInfoDao;
import com.lodogame.game.utils.JedisUtils;
import com.lodogame.game.utils.RedisKey;
import com.lodogame.model.UserArenaHero;
import com.lodogame.model.UserArenaInfo;
import com.lodogame.model.UserArenaSeriesGift;
import com.lodogame.model.UserArenaSeriesGiftInfo;

public class UserArenaInfoDaoRedisImpl implements UserArenaInfoDao {

	@Override
	public UserArenaInfo get(String userId) {
		throw new NotImplementedException();
	}

	@Override
	public boolean update(UserArenaInfo userArenaInfo) {
		throw new NotImplementedException();
	}

	@Override
	public int getRank(String userId) {
		String key = RedisKey.getUserArenaRankKey();
		long count = JedisUtils.zcard(key);
		if (count == 0) {
			return 0;
		}
		long rank = JedisUtils.zrank(key, userId) + 1;// 因为下标是从0开始所以排名要加1
		return (int) rank;
	}

	@Override
	public boolean setScore(String userId, int score) {
		String key = RedisKey.getUserArenaRankKey();
		JedisUtils.zadd(key, score, userId);

		return true;
	}

	@Override
	public List<String> getRangeRank(int rank) {
		String key = RedisKey.getUserArenaRankKey();
		Set<String> set = JedisUtils.zrange(key, rank - 10, rank + 10);
		if (null == set) {
			return null;
		}
		List<String> usreIdList = new ArrayList<String>(set);
		if (null != usreIdList && usreIdList.size() > 0) {
			return usreIdList;
		}
		return null;
	}

	@Override
	public List<UserArenaInfo> getRandByScore(int lowerScore, int upperScore, String userId) {
		throw new NotImplementedException();
	}

	@Override
	public List<String> getRangeRankTen() {
		String key = RedisKey.getUserArenaRankKey();
		Set<String> set = JedisUtils.zrange(key, 0, 9);
		if (null == set) {
			return null;
		}
		List<String> usreIdList = new ArrayList<String>(set);
		if (null != usreIdList && usreIdList.size() > 0) {
			return usreIdList;
		}
		return null;
	}

	@Override
	public void clear() {
		throw new NotImplementedException();
	}

	@Override
	public void clearScore() {
		String key = RedisKey.getUserArenaRankKey();
		JedisUtils.zremrangeByScore(key);

	}

	@Override
	public List<String> getRangeRankHun() {
		String key = RedisKey.getUserArenaRankKey();
		Set<String> set = JedisUtils.zrange(key, 0, 99);
		if (null == set) {
			return null;
		}
		List<String> usreIdList = new ArrayList<String>(set);
		if (null != usreIdList && usreIdList.size() > 0) {
			return usreIdList;
		}
		return null;
	}

	@Override
	public boolean isSendReward(String date) {
		throw new NotImplementedException();
	}

	@Override
	public boolean addSendReward(String date) {
		throw new NotImplementedException();
	}

	@Override
	public List<UserArenaInfo> getList() {
		throw new NotImplementedException();
	}

	@Override
	public boolean updateUserLevel(String userId, int level) {
		throw new NotImplementedException();
	}

	@Override
	public boolean backup() {
		throw new NotImplementedException();
	}

	@Override
	public List<UserArenaSeriesGift> getSeriesGift(String userId) {
		throw new NotImplementedException();
	}

	@Override
	public void insertSeriesGift(UserArenaSeriesGift gift) {
		throw new NotImplementedException();
	}

	@Override
	public void updateSeriesGift(UserArenaSeriesGift gift) {
		throw new NotImplementedException();
	}

	@Override
	public UserArenaSeriesGiftInfo getSeriesGifts(String userId) {
		throw new NotImplementedException();
	}

	@Override
	public void clearGift() {
		throw new NotImplementedException();
	}

	@Override
	public boolean insertArenaHero(List<UserArenaHero> list) {
		throw new NotImplementedException();
	}

	@Override
	public List<UserArenaHero> getUserArenaHero(String userId) {
		throw new NotImplementedException();
	}

}
