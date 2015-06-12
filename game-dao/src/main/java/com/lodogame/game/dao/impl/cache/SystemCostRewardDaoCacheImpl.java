package com.lodogame.game.dao.impl.cache;

import java.util.List;

import com.lodogame.game.dao.SystemCostRewardDao;
import com.lodogame.model.SystemCostReward;

public class SystemCostRewardDaoCacheImpl extends BaseSystemCacheDao<SystemCostReward> implements SystemCostRewardDao {

	@Override
	public SystemCostReward get(int activityId, int id) {
		return super.get(activityId + "_" + id);
	}

	@Override
	public List<SystemCostReward> getList(int activityId) {
		return super.getList(String.valueOf(activityId));
	}

}
