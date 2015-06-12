package com.lodogame.game.dao.impl.cache;

import java.util.List;

import com.lodogame.game.dao.SystemDrawDao;
import com.lodogame.model.SystemDraw;

public class SystemDrawDaoCacheImpl extends BaseSystemCacheDao<SystemDraw> implements SystemDrawDao {

	@Override
	public List<SystemDraw> getList() {
		return super.getList();
	}

	@Override
	public SystemDraw get(int id) {
		return super.get(String.valueOf(id));
	}

}
