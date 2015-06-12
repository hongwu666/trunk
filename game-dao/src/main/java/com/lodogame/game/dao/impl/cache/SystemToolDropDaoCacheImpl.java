package com.lodogame.game.dao.impl.cache;

import java.util.List;

import com.lodogame.game.dao.SystemToolDropDao;
import com.lodogame.model.SystemToolDrop;

public class SystemToolDropDaoCacheImpl extends BaseSystemCacheDao<SystemToolDrop> implements SystemToolDropDao {

	@Override
	public List<SystemToolDrop> getSystemToolDropList(int toolId) {
		return super.getList(String.valueOf(toolId));
	}

}
