package com.lodogame.game.dao.impl.cache;

import com.lodogame.game.dao.UpStarValueConfigDao;
import com.lodogame.model.UpstarValueConfig;

public class UpStarValueConfigDaoCacheImpl extends BaseSystemCacheDao<UpstarValueConfig> implements UpStarValueConfigDao {

	@Override
	public UpstarValueConfig get(int starLevel, int job) {
		return super.get(starLevel + "_" + job);
	}

}
