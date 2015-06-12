package com.lodogame.game.dao.impl.cache;

import java.util.List;

import com.lodogame.game.dao.OnlyoneJoinRewardDao;
import com.lodogame.model.OnlyoneJoinReward;

public class OnlyoneJoinRewardDaoCacheImpl extends BaseSystemCacheDao<OnlyoneJoinReward> implements OnlyoneJoinRewardDao {

	@Override
	public OnlyoneJoinReward get(int times) {
		return super.get(String.valueOf(times));
	}

	@Override
	public OnlyoneJoinReward getNextReward(int times) {
		OnlyoneJoinReward onlyoneJoinReward = null;
		List<OnlyoneJoinReward> list = super.getList();
		for (OnlyoneJoinReward r : list) {
			if (onlyoneJoinReward == null || r.getTimes() > onlyoneJoinReward.getTimes()) {
				onlyoneJoinReward = r;
			}
			if (r.getTimes() > times) {
				return r;
			}
		}
		return onlyoneJoinReward;
	}
}
