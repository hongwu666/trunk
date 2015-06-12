package com.lodogame.game.dao.impl.cache;

import com.lodogame.game.dao.SystemHeroUpgradeAddPropDao;
import com.lodogame.model.SystemHeroUpgradeAddProp;

public class SystemHeroUpgradeAddPropDaoCacheImpl extends BaseSystemCacheDao<SystemHeroUpgradeAddProp> implements SystemHeroUpgradeAddPropDao {

	@Override
	public SystemHeroUpgradeAddProp get(int nodeId) {
		return super.get(String.valueOf(nodeId));
	}

}
