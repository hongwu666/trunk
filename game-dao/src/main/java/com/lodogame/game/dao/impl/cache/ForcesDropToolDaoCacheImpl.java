package com.lodogame.game.dao.impl.cache;

import java.util.List;

import com.lodogame.game.dao.ForcesDropToolDao;
import com.lodogame.model.ForcesDropTool;

public class ForcesDropToolDaoCacheImpl extends BaseSystemCacheDao<ForcesDropTool> implements ForcesDropToolDao {

	@Override
	public List<ForcesDropTool> getForcesGroupDropToolList(int forcesGroup) {
		return super.getList(String.valueOf(forcesGroup));
	}

}
