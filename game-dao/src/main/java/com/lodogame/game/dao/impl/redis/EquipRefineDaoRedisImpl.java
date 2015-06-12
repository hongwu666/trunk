package com.lodogame.game.dao.impl.redis;

import java.util.List;

import com.lodogame.game.dao.EquipRefineDao;
import com.lodogame.game.dao.clearcache.ClearCacheOnLoginOut;
import com.lodogame.game.utils.JedisUtils;
import com.lodogame.game.utils.RedisKey;
import com.lodogame.game.utils.json.Json;
import com.lodogame.model.EquipRefine;

public class EquipRefineDaoRedisImpl implements EquipRefineDao, ClearCacheOnLoginOut {

	@Override
	public List<EquipRefine> getUserEquipRefineList(String userId) {
		String key = RedisKey.getUserEquipRefineListCacheKey(userId);
		List<String> jsonList = JedisUtils.getMapValues(key);
		if (jsonList != null && jsonList.size() > 0) {
			return Json.toList(jsonList, EquipRefine.class);
		}
		return null;
	}

	@Override
	public boolean addEquipRefine(EquipRefine equipRefine) {

		String key = RedisKey.getUserEquipRefineListCacheKey(equipRefine.getUserId());

		JedisUtils.setFieldToObject(key, equipRefine.getUserEquipId() + "_" + equipRefine.getRefinePoint(), Json.toJson(obj)(equipRefine));

		return true;
	}

	@Override
	public boolean updateEquipRefine(EquipRefine equipRefine) {
		return addEquipRefine(equipRefine);
	}

	@Override
	public EquipRefine getEquipRefine(String userId, String userEquipId, int type) {

		String key = RedisKey.getUserEquipRefineListCacheKey(userId);
		String json = JedisUtils.getFieldFromObject(key, userEquipId + "_" + type);
		if (json != null) {
			return Json.toObject(json, EquipRefine.class);
		}

		return null;
	}

	@Override
	public void clearOnLoginOut(String userId) throws Exception {
		String key = RedisKey.getUserEquipRefineListCacheKey(userId);
		JedisUtils.delete(key);
	}

}
