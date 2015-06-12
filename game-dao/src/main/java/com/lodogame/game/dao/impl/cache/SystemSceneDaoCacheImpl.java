package com.lodogame.game.dao.impl.cache;

import com.lodogame.game.dao.SystemSceneDao;
import com.lodogame.model.SystemScene;

public class SystemSceneDaoCacheImpl extends BaseSystemCacheDao<SystemScene> implements SystemSceneDao {

	@Override
	public SystemScene get(int sceneId) {
		return super.get(String.valueOf(sceneId));
	}

}
