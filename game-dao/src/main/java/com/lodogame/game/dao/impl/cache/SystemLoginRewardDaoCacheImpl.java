package com.lodogame.game.dao.impl.cache;

import java.util.List;

import com.lodogame.game.dao.SystemLoginRewardDao;
import com.lodogame.model.SystemLoginReward;

public class SystemLoginRewardDaoCacheImpl extends BaseSystemCacheDao<SystemLoginReward> implements SystemLoginRewardDao {

	@Override
	public SystemLoginReward getSystemLoginRewardByDay(int day) {
		return this.get(String.valueOf(day));
	}

	@Override
	public List<SystemLoginReward> getList() {
		return super.getList();
	}

}
