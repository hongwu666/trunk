package com.lodogame.game.dao.impl.cache;

import java.util.List;

import com.lodogame.game.dao.TavernRewardDao;
import com.lodogame.model.TavernReward;

public class TavernRewardDaoCacheImpl extends BaseSystemCacheDao<TavernReward> implements TavernRewardDao {

	@Override
	public List<TavernReward> getList(int type) {
		return super.getList(String.valueOf(type));
	}

}
