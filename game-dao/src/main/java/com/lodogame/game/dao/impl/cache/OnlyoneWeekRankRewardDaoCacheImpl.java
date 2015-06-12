package com.lodogame.game.dao.impl.cache;

import java.util.List;

import com.lodogame.game.dao.OnlyoneWeekRankRewardDao;
import com.lodogame.model.OnlyoneWeekRankReward;

public class OnlyoneWeekRankRewardDaoCacheImpl extends BaseSystemCacheDao<OnlyoneWeekRankReward> implements OnlyoneWeekRankRewardDao {

	@Override
	public OnlyoneWeekRankReward get(int rank) {
		List<OnlyoneWeekRankReward> list = super.getList();
		for (OnlyoneWeekRankReward reward : list) {
			if (reward.getRankUpper() <= rank && rank <= reward.getRankLower()) {
				return reward;
			}
		}
		return null;
	}

}
