package com.lodogame.game.dao.impl.redis;

import java.util.Date;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;

import com.lodogame.game.dao.UserPraiseDao;
import com.lodogame.game.utils.JedisUtils;
import com.lodogame.game.utils.RedisKey;
import com.lodogame.game.utils.json.Json;
import com.lodogame.model.UserPraise;

public class UserPraiseDaoRedisImpl implements UserPraiseDao {

	@Override
	public UserPraise get(String uid, String praisedUserId) {
		String key = RedisKey.getUserPraiseCacheKey(uid);
		String json = JedisUtils.getFieldFromObject(key, praisedUserId);
		if (StringUtils.isNotEmpty(json)) {
			return Json.toObject(json, UserPraise.class);
		}
		return null;
	}

	@Override
	public boolean add(UserPraise userPraise) {
		String key = RedisKey.getUserPraiseCacheKey(userPraise.getUserId());
		String json = Json.toJson(userPraise);
		JedisUtils.setFieldToObject(key, userPraise.getPraisedUserId(), json);
		return true;
	}

	@Override
	public boolean update(String uid, String praisedUserId, Date updatedTime) {
		String key = RedisKey.getUserPraiseCacheKey(uid);
		JedisUtils.delFieldFromObject(key, praisedUserId);
		return true;
	}

	@Override
	public int getTodayPraiseNum(String uid) {
		throw new NotImplementedException();
	}
	@Override
	public int getTodayPraisedNum(String uid) {
		throw new NotImplementedException();
	}
}
