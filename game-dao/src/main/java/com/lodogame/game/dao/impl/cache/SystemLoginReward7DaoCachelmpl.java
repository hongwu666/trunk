package com.lodogame.game.dao.impl.cache;

import java.util.List;

import com.lodogame.game.dao.SystemLoginReward7Dao;
import com.lodogame.model.SystemLoginReward7;

public class SystemLoginReward7DaoCachelmpl extends BaseSystemCacheDao<SystemLoginReward7> implements SystemLoginReward7Dao {

	@Override
	public List<SystemLoginReward7> getAll() {
		return this.getList();
	}

	@Override
	public List<SystemLoginReward7> getByDay(int day) {
		return this.getList(String.valueOf(day));
	}

}
