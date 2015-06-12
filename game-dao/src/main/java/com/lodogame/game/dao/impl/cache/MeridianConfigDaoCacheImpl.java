package com.lodogame.game.dao.impl.cache;

import com.lodogame.game.dao.MeridianConfigDao;
import com.lodogame.model.MeridianConfig;

public class MeridianConfigDaoCacheImpl extends BaseSystemCacheDao<MeridianConfig> implements MeridianConfigDao {

	@Override
	public MeridianConfig getConfig(int type, int node) {
		return super.get(type + "_" + node);
	}

}
