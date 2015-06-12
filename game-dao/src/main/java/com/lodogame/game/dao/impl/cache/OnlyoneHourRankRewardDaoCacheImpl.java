package com.lodogame.game.dao.impl.cache;

import java.util.List;

import com.lodogame.game.dao.OnlyoneHourRankRewardDao;
import com.lodogame.model.OnlyoneHourRankReward;

public class OnlyoneHourRankRewardDaoCacheImpl extends BaseSystemCacheDao<OnlyoneHourRankReward> implements OnlyoneHourRankRewardDao {

	@Override
	public OnlyoneHourRankReward get(int rank) {
		List<OnlyoneHourRankReward> list = super.getList();
		for (OnlyoneHourRankReward reward : list) {
			if (reward.getRankUpper() <= rank && rank <= reward.getRankLower()) {
				return reward;
			}
		}
		return null;
	}
}
