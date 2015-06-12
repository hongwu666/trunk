package com.lodogame.game.dao.impl.cache;

import com.lodogame.game.dao.SystemHeroUpgradeDao;
import com.lodogame.model.SystemHeroUpgrade;

public class SystemHeroUpgradeDaoCacheImpl extends BaseSystemCacheDao<SystemHeroUpgrade> implements SystemHeroUpgradeDao {

	public SystemHeroUpgrade get(int systemHeroId) {
		return super.get(String.valueOf(systemHeroId));
	}

}
