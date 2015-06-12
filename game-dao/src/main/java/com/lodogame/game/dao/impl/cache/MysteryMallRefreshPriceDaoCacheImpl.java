package com.lodogame.game.dao.impl.cache;

import com.lodogame.game.dao.MysteryMallRefreshPriceDao;
import com.lodogame.model.MysteryMallRefreshPrice;

public class MysteryMallRefreshPriceDaoCacheImpl extends BaseSystemCacheDao<MysteryMallRefreshPrice> implements MysteryMallRefreshPriceDao {

	@Override
	public MysteryMallRefreshPrice get(int mallType, int times) {
		String key = mallType + "_" + times;
		return this.get(key);
	}

}
