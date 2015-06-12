package com.lodogame.game.dao.impl.redis;

import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;

import com.lodogame.game.dao.UserStoneDao;
import com.lodogame.game.dao.clearcache.ClearCacheOnLoginOut;
import com.lodogame.game.utils.JedisUtils;
import com.lodogame.game.utils.RedisKey;
import com.lodogame.game.utils.json.Json;
import com.lodogame.model.UserStone;

public class UserStoneDaoRedisImpl implements UserStoneDao, ClearCacheOnLoginOut {

	@Override
	public List<UserStone> getUserStoneList(String userId) {

		String key = RedisKey.getUserStoneListCacheKey(userId);
		List<String> jsonList = JedisUtils.getMapValues(key);
		if (jsonList != null && jsonList.size() > 0) {
			return Json.toList(jsonList, UserStone.class);
		}
		return null;
	}

	@Override
	public boolean addUserStone(String userId, int stoneId, int stoneNum) {

		String key = RedisKey.getUserStoneListCacheKey(userId);

		if (!JedisUtils.exists(key)) {// 缓存中没有，不需要刷新
			return false;
		}

		UserStone userStone = get(userId, stoneId);
		if (userStone != null) {
			userStone.setStoneNum(userStone.getStoneNum() + stoneNum);
		} else {
			userStone = new UserStone();
			userStone.setStoneId(stoneId);
			userStone.setStoneNum(stoneNum);
		}

		JedisUtils.setFieldToObject(key, String.valueOf(userStone.getStoneId()), Json.toJson(userStone));

		return true;
	}

	@Override
	public boolean reduceUserStone(String userId, int stoneId, int stoneNum) {

		String key = RedisKey.getUserStoneListCacheKey(userId);

		if (!JedisUtils.exists(key)) {// 缓存中没有，不需要刷新
			return false;
		}

		UserStone userStone = get(userId, stoneId);
		if (userStone != null) {
			userStone.setStoneNum(userStone.getStoneNum() - stoneNum);
		}

		JedisUtils.setFieldToObject(key, String.valueOf(userStone.getStoneId()), Json.toJson(userStone));

		return true;
	}

	@Override
	public UserStone get(String userId, int stoneId) {

		String key = RedisKey.getUserStoneListCacheKey(userId);
		String json = JedisUtils.getFieldFromObject(key, String.valueOf(stoneId));
		if (StringUtils.isNotEmpty(json)) {
			return Json.toObject(json, UserStone.class);
		}

		return null;
	}

	@Override
	public boolean initCache(String userId, List<UserStone> list) {

		if (list.isEmpty()) {
			return false;
		}

		String key = RedisKey.getUserStoneListCacheKey(userId);

		if (JedisUtils.exists(key)) {
			JedisUtils.delete(key);
		}

		for (UserStone userStone : list) {
			JedisUtils.setFieldToObject(key, String.valueOf(userStone.getStoneId()), Json.toJson(userStone));
		}

		return true;

	}

	@Override
	public boolean deleteZero(String userId) {
		throw new NotImplementedException();
	}

	@Override
	public void clearOnLoginOut(String userId) throws Exception {
		String key = RedisKey.getUserStoneListCacheKey(userId);
		JedisUtils.delete(key);

	}

}
