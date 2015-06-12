package com.lodogame.game.dao.impl.cache;

import com.lodogame.game.dao.EnchantPropertyDao;
import com.lodogame.model.EnchantProperty;

public class EnchantPropertyDaoCacheImpl extends BaseSystemCacheDao<EnchantProperty> implements EnchantPropertyDao {

	@Override
	public EnchantProperty getEnchantProperty(int color, int type) {

		return this.get(color + "_" + type);
	}

}
