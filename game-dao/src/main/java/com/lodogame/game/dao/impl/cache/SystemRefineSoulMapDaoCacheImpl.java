package com.lodogame.game.dao.impl.cache;

import java.util.List;

import com.lodogame.game.dao.SystemRefineSoulMapDao;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.model.SystemRefineSoulMap;

public class SystemRefineSoulMapDaoCacheImpl extends BaseSystemCacheDao<SystemRefineSoulMap> implements SystemRefineSoulMapDao,ReloadAble{

	@Override
	public List<SystemRefineSoulMap> getByMapId(int mapId) {
		return super.getList(String.valueOf(mapId));
	}

	
}
