package com.lodogame.game.dao.impl.redis;

import java.util.List;

import org.apache.commons.lang.NotImplementedException;

import com.lodogame.game.dao.EquipEnchantDao;
import com.lodogame.game.dao.clearcache.ClearCacheOnLoginOut;
import com.lodogame.game.utils.JedisUtils;
import com.lodogame.game.utils.RedisKey;
import com.lodogame.game.utils.json.Json;
import com.lodogame.model.EquipEnchant;

public class EquipEnchantDaoRedisImpl implements ClearCacheOnLoginOut, EquipEnchantDao {

	@Override
	public List<EquipEnchant> getEquipEnchantList(String userId) {

		String key = RedisKey.getUserEquipEnchantListCacheKey(userId);
		List<String> jsonList = JedisUtils.getMapValues(key);
		if (jsonList != null && jsonList.size() > 0) {
			return Json.toList(jsonList, EquipEnchant.class);
		}
		return null;
	}

	@Override
	public EquipEnchant getEquipEnchant(String userId, String userEquipId) {
		throw new NotImplementedException();
	}

	@Override
	public boolean updateEquipEnchant(EquipEnchant equipEnchant) {

		String key = RedisKey.getUserEquipEnchantListCacheKey(equipEnchant.getUserId());

		JedisUtils.setFieldToObject(key, equipEnchant.getUserEquipId(), Json.toJson(equipEnchant));

		return true;
	}

	@Override
	public boolean updateEnchantProperty(String userId, String userEquipId, String enProperty) {
		throw new NotImplementedException();
	}

	@Override
	public void clearOnLoginOut(String userId) throws Exception {
		String key = RedisKey.getUserEquipEnchantListCacheKey(userId);
		JedisUtils.delete(key);
	}

}
