package com.lodogame.game.dao.impl.cache;

import java.util.List;

import com.lodogame.game.dao.OnlyoneTimesRewardDao;
import com.lodogame.model.OnlyoneTimesReward;

public class OnlyoneTimesRewardDaoCacheImpl extends BaseSystemCacheDao<OnlyoneTimesReward> implements OnlyoneTimesRewardDao {

	@Override
	public int getPoint(int times) {
		int point = 0;
		OnlyoneTimesReward onlyoneTimesReward = super.get(String.valueOf(times));
		if (onlyoneTimesReward == null) {
			List<OnlyoneTimesReward> list = super.getList();
			for (OnlyoneTimesReward reward : list) {
				if (onlyoneTimesReward == null || onlyoneTimesReward.getTimes() < reward.getTimes()) {
					onlyoneTimesReward = reward;
				}
			}
		}
		if (onlyoneTimesReward != null) {
			point = onlyoneTimesReward.getPoint();
		}
		return point;
	}
}
