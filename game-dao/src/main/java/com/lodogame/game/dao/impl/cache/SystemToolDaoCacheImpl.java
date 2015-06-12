package com.lodogame.game.dao.impl.cache;

import com.lodogame.game.dao.SystemToolDao;
import com.lodogame.model.SystemTool;

public class SystemToolDaoCacheImpl extends BaseSystemCacheDao<SystemTool> implements SystemToolDao {

	@Override
	public SystemTool get(int toolId) {
		return super.get(toolId);
	}

}
