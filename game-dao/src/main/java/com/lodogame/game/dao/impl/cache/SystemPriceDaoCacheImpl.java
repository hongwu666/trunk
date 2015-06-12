package com.lodogame.game.dao.impl.cache;

import java.util.List;

import com.lodogame.game.dao.SystemPriceDao;
import com.lodogame.model.SystemPrice;

public class SystemPriceDaoCacheImpl extends BaseSystemCacheDao<SystemPrice> implements SystemPriceDao {

	@Override
	public SystemPrice get(int mallType, int times) {
		String key = mallType + "_" + times;
		return this.get(key);
	}

	@Override
	public List<SystemPrice> getList(int type) {
		return super.getList(String.valueOf(type));
	}

}
