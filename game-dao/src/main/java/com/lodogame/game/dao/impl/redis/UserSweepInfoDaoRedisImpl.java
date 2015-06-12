package com.lodogame.game.dao.impl.redis;


import java.util.Date;

import com.lodogame.game.dao.UserSweepInfoDao;
import com.lodogame.game.utils.JedisUtils;
import com.lodogame.game.utils.RedisKey;
import com.lodogame.game.utils.json.Json;
import com.lodogame.model.UserSweepInfo;

public class UserSweepInfoDaoRedisImpl implements UserSweepInfoDao {

	@Override
	public boolean add(UserSweepInfo sweepInfo) {
		if (sweepInfo != null) {
			String key = RedisKey.getUserSweepCacheKey();
			String json = Json.toJson(sweepInfo);
			JedisUtils.setFieldToObject(key, sweepInfo.getUserId(), json);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public UserSweepInfo getCurrentSweep(String userId) {
		String key = RedisKey.getUserSweepCacheKey();
		String json = JedisUtils.getFieldFromObject(key, userId);
		return Json.toObject(json, UserSweepInfo.class);
	}

	@Override
	public boolean updateSweepComplete(String userId, Date date) {
		String key = RedisKey.getUserSweepCacheKey();
		String json = JedisUtils.getFieldFromObject(key, userId);
		UserSweepInfo sweepInfo = Json.toObject(json, UserSweepInfo.class);
		if (sweepInfo != null) {
			sweepInfo.setEndTime(date);
			JedisUtils.setFieldToObject(key, sweepInfo.getUserId(), Json.toJson(sweepInfo));
		}
		return true;
	}

	
   public void deleteEntry(String userId){
	   JedisUtils.delFieldFromObject(RedisKey.getUserSweepCacheKey(), userId);
   }
}
