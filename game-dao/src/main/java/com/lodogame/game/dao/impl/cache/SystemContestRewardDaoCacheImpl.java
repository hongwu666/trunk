package com.lodogame.game.dao.impl.cache;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.SystemContestRewardDao;
import com.lodogame.model.SystemContestReward;

public class SystemContestRewardDaoCacheImpl implements SystemContestRewardDao {

	private SystemContestRewardDao systemContestRewardDaoMysqlImpl;

	public void setSystemContestRewardDaoMysqlImpl(
			SystemContestRewardDao systemContestRewardDaoMysqlImpl) {
		this.systemContestRewardDaoMysqlImpl = systemContestRewardDaoMysqlImpl;
	}


	private Map<Integer, SystemContestReward> cacheMap = new ConcurrentHashMap<Integer, SystemContestReward>();


	@Override
	public SystemContestReward getSystemContestReward(int rewardId) {
		if (!cacheMap.containsKey(rewardId)) {
			SystemContestReward systemContestReward = this.systemContestRewardDaoMysqlImpl.getSystemContestReward(rewardId);
			if (systemContestReward != null) {
				cacheMap.put(rewardId, systemContestReward);
			}
		}
		
		return cacheMap.get(rewardId);
	}

	


}
