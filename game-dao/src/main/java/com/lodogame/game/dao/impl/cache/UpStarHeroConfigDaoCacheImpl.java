package com.lodogame.game.dao.impl.cache;

import com.lodogame.game.dao.UpStarHeroConfigDao;
import com.lodogame.model.UpstarHeroConfig;

public class UpStarHeroConfigDaoCacheImpl extends BaseSystemCacheDao<UpstarHeroConfig> implements UpStarHeroConfigDao {

	@Override
	public UpstarHeroConfig get(int heroId) {
		return super.get(String.valueOf(heroId));
	}

}
