package com.lodogame.game.dao.impl.redis;

import org.apache.commons.lang.StringUtils;

import com.lodogame.game.dao.UserLoginLogDao;
import com.lodogame.game.utils.JedisUtils;
import com.lodogame.game.utils.RedisKey;
import com.lodogame.game.utils.json.Json;
import com.lodogame.model.UserLoginLog;

public class UserLoginLogDaoRedisImpl implements UserLoginLogDao {

	@Override
	public UserLoginLog get(String userId) {
		String key = RedisKey.getUserLoginLogCacheKey();
		String json = JedisUtils.getFieldFromObject(key, userId);
		if (StringUtils.isNotEmpty(json)) {
			return Json.toObject(json, UserLoginLog.class);
		}
		return null;
	}

	@Override
	public boolean add(UserLoginLog log) {
		String key = RedisKey.getUserLoginLogCacheKey();
		String json = Json.toJson(log);
		JedisUtils.setFieldToObject(key, log.getUserId(), json);
		return true;

	}

	@Override
	public boolean update(UserLoginLog loginLog) {
		String key = RedisKey.getUserLoginLogCacheKey();
		JedisUtils.delFieldFromObject(key, loginLog.getUserId());
		return true;
	}

	@Override
	public boolean updateRecStatus(String userId, String recStatus) {
		String key = RedisKey.getUserLoginLogCacheKey();
		JedisUtils.delFieldFromObject(key, userId);
		return true;
	}

}
