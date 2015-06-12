package com.lodogame.game.dao.impl.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.UserArenaInfoDao;
import com.lodogame.game.dao.clearcache.ClearCacheOnLoginOut;
import com.lodogame.game.dao.impl.mysql.UserArenaInfoDaoMysqlImpl;
import com.lodogame.game.dao.impl.redis.UserArenaInfoDaoRedisImpl;
import com.lodogame.model.UserArenaHero;
import com.lodogame.model.UserArenaInfo;
import com.lodogame.model.UserArenaSeriesGift;
import com.lodogame.model.UserArenaSeriesGiftInfo;

public class UserArenaInfoDaoCacheImpl implements UserArenaInfoDao, ClearCacheOnLoginOut {

	private Map<String, UserArenaInfo> cacheMap = new ConcurrentHashMap<String, UserArenaInfo>();

	private UserArenaInfoDao userArenaInfoDaoMysqlImpl;

	private UserArenaInfoDao userArenaInfoDaoRedisImpl;

	public void setUserArenaInfoDaoRedisImpl(UserArenaInfoDaoRedisImpl userArenaInfoDaoRedisImpl) {
		this.userArenaInfoDaoRedisImpl = userArenaInfoDaoRedisImpl;
	}

	public void setUserArenaInfoDaoMysqlImpl(UserArenaInfoDaoMysqlImpl userArenaInfoDaoMysqlImpl) {
		this.userArenaInfoDaoMysqlImpl = userArenaInfoDaoMysqlImpl;
	}

	@Override
	public UserArenaInfo get(String userId) {
		if (cacheMap.containsKey(userId)) {
			return cacheMap.get(userId);
		}
		UserArenaInfo userArenaInfo = this.userArenaInfoDaoMysqlImpl.get(userId);
		if (null != userArenaInfo) {
			cacheMap.put(userId, userArenaInfo);
			return userArenaInfo;
		}
		return null;
	}

	@Override
	public boolean update(UserArenaInfo userArenaInfo) {
		if (cacheMap.containsKey(userArenaInfo.getUserId())) {
			cacheMap.put(userArenaInfo.getUserId(), userArenaInfo);
		}
		return this.userArenaInfoDaoMysqlImpl.update(userArenaInfo);
	}

	@Override
	public int getRank(String userId) {
		return this.userArenaInfoDaoRedisImpl.getRank(userId);
	}

	@Override
	public boolean setScore(String userId, int score) {
		// if(cacheMap.containsKey(userId)){
		// UserArenaInfo temp = cacheMap.get(userId);
		// temp.setScore(temp.getScore() + score);
		// cacheMap.put(userId, temp);
		// }
		if (score > 0) {
			this.userArenaInfoDaoRedisImpl.setScore(userId, score);
		}
		// return this.userArenaInfoDaoMysqlImpl.setScore(userId, score);
		return true;
	}

	@Override
	public List<String> getRangeRank(int rank) {
		return this.userArenaInfoDaoRedisImpl.getRangeRank(rank);
	}

	@Override
	public List<UserArenaInfo> getRandByScore(int lowerScore, int upperScore, String userId) {
		return this.userArenaInfoDaoMysqlImpl.getRandByScore(lowerScore, upperScore, userId);
	}

	@Override
	public List<String> getRangeRankTen() {
		return this.userArenaInfoDaoRedisImpl.getRangeRankTen();
	}

	@Override
	public void clear() {
		cacheMap.clear();
		this.userArenaInfoDaoMysqlImpl.clear();

	}

	@Override
	public void clearScore() {
		cacheMap.clear();
		try {
			this.userArenaInfoDaoRedisImpl.clearScore();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.userArenaInfoDaoMysqlImpl.clearScore();

	}

	@Override
	public List<String> getRangeRankHun() {
		return this.userArenaInfoDaoRedisImpl.getRangeRankHun();
	}

	@Override
	public boolean isSendReward(String date) {
		return this.userArenaInfoDaoMysqlImpl.isSendReward(date);
	}

	@Override
	public boolean addSendReward(String date) {
		return this.userArenaInfoDaoMysqlImpl.addSendReward(date);
	}

	public void init() {
		this.userArenaInfoDaoRedisImpl.clearScore();
		List<UserArenaInfo> list = getList();
		if (null != list && list.size() > 0) {
			for (UserArenaInfo userArenaInfo : list) {
				if (userArenaInfo.getScore() == 0) {
					continue;
				}
				setScore(userArenaInfo.getUserId(), userArenaInfo.getScore());
			}
		}

	}

	@Override
	public List<UserArenaInfo> getList() {
		List<UserArenaInfo> list = this.userArenaInfoDaoMysqlImpl.getList();
		return list;

	}

	@Override
	public boolean updateUserLevel(String userId, int level) {
		if (cacheMap.containsKey(userId)) {
			UserArenaInfo userArenaInfo = cacheMap.get(userId);
			userArenaInfo.setUserLevel(level);
			cacheMap.put(userId, userArenaInfo);
		}
		return this.userArenaInfoDaoMysqlImpl.updateUserLevel(userId, level);
	}

	@Override
	public boolean backup() {
		return this.userArenaInfoDaoMysqlImpl.backup();
	}

	private Map<String, UserArenaSeriesGiftInfo> gifts = new ConcurrentHashMap<String, UserArenaSeriesGiftInfo>();

	@Override
	public UserArenaSeriesGiftInfo getSeriesGifts(String userId) {
		UserArenaSeriesGiftInfo list = gifts.get(userId);
		if (list == null) {
			list = new UserArenaSeriesGiftInfo(userArenaInfoDaoMysqlImpl.getSeriesGift(userId));
			gifts.put(userId, list);
		}
		return list;
	}

	@Override
	public void insertSeriesGift(UserArenaSeriesGift gift) {
		userArenaInfoDaoMysqlImpl.insertSeriesGift(gift);
	}

	@Override
	public void updateSeriesGift(UserArenaSeriesGift gift) {
		userArenaInfoDaoMysqlImpl.updateSeriesGift(gift);
	}

	@Override
	public List<UserArenaSeriesGift> getSeriesGift(String userId) {
		return null;
	}

	@Override
	public void clearOnLoginOut(String userId) throws Exception {
		gifts.remove(userId);
	}

	@Override
	public boolean insertArenaHero(List<UserArenaHero> list) {
		return userArenaInfoDaoMysqlImpl.insertArenaHero(list);
	}

	@Override
	public List<UserArenaHero> getUserArenaHero(String userId) {
		return this.userArenaInfoDaoMysqlImpl.getUserArenaHero(userId);
	}

	@Override
	public void clearGift() {
		gifts.clear();
		userArenaInfoDaoMysqlImpl.clearGift();
	}
}
