package com.lodogame.game.dao.impl.cache;

import java.util.List;

import com.lodogame.game.dao.TavernGroupRateDao;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.model.TavernGroupRate;

public class TavernGroupRateDaoCacheImpl extends BaseSystemCacheDao<TavernGroupRate> implements TavernGroupRateDao,ReloadAble {

	@Override
	public List<TavernGroupRate> getList(int type) {
		return super.getList(String.valueOf(type));
	}

}
