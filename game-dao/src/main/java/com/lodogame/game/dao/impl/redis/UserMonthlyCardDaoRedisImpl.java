package com.lodogame.game.dao.impl.redis;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.lodogame.game.dao.UserMonthlyCardDao;
import com.lodogame.game.utils.JedisUtils;
import com.lodogame.game.utils.RedisKey;
import com.lodogame.game.utils.json.Json;
import com.lodogame.model.UserMonthlyCard;

public class UserMonthlyCardDaoRedisImpl implements UserMonthlyCardDao {

	@Override
	public UserMonthlyCard get(String userId) {
		String key = RedisKey.getUserMonthlyCardCacheKey();
		String json = JedisUtils.getFieldFromObject(key, userId);

		if (StringUtils.isEmpty(json) == false) {
			return Json.toObject(json, UserMonthlyCard.class);
		}
		return null;
	}

	@Override
	public boolean add(UserMonthlyCard card) {
		String key = RedisKey.getUserMonthlyCardCacheKey();
		String json = Json.toJson(card);
		JedisUtils.setFieldToObject(key, card.getUserId(), json);
		return true;
	}

	@Override
	public boolean updateDueTime(String userId, Date dueTime) {
		String key = RedisKey.getUserMonthlyCardCacheKey();
		JedisUtils.delFieldFromObject(key, userId);
		return true;
	}

	public boolean cleanCache(String userId) {
		String key = RedisKey.getUserMonthlyCardCacheKey();
		JedisUtils.delFieldFromObject(key, userId);
		return true;
	}

}
