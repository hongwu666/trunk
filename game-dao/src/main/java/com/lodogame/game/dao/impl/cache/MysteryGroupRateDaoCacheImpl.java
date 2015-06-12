package com.lodogame.game.dao.impl.cache;

import java.util.List;

import com.lodogame.game.dao.MysteryGroupRateDao;
import com.lodogame.model.MysteryGroupRate;

public class MysteryGroupRateDaoCacheImpl extends BaseSystemCacheDao<MysteryGroupRate> implements MysteryGroupRateDao {

	@Override
	public List<MysteryGroupRate> getList(int mallType, int type) {
		String key = mallType + "_" + type;
		return this.getList(key);
	}
}
