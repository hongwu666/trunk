package com.lodogame.game.dao.impl.cache;

import com.lodogame.game.dao.MeridianNodeConfigDao;
import com.lodogame.model.MeridianNodeConfig;

public class MeridianNodeConfigDaoCacheImpl extends BaseSystemCacheDao<MeridianNodeConfig> implements MeridianNodeConfigDao {

	@Override
	public MeridianNodeConfig getNodeConfig(int node, int level) {
		String objKey = node + "_" + level;
		return super.get(objKey);
	}

}
