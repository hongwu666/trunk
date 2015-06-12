package com.lodogame.game.dao.impl.cache;

import com.lodogame.game.dao.UpStarBreakConfigDao;
import com.lodogame.model.UpstarBreakConfig;

public class UpStarBreakConfigDaoCacheImpl extends BaseSystemCacheDao<UpstarBreakConfig> implements UpStarBreakConfigDao {

	@Override
	public UpstarBreakConfig get(int baseStar, int nextStar) {
		return super.get(baseStar + "_" + nextStar);
	}
}
