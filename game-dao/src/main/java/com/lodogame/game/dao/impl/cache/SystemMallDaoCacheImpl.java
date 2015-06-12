package com.lodogame.game.dao.impl.cache;

import com.lodogame.game.dao.SystemMallDao;
import com.lodogame.model.SystemMall;

public class SystemMallDaoCacheImpl extends BaseSystemCacheDao<SystemMall> implements SystemMallDao {

	@Override
	public SystemMall get(int mallId) {
		return super.get(String.valueOf(mallId));
	}

}
