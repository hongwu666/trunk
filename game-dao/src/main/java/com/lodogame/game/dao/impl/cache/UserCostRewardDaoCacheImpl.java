package com.lodogame.game.dao.impl.cache;

import java.util.Date;

import com.lodogame.game.dao.UserCostRewardDao;
import com.lodogame.game.dao.impl.mysql.UserCostRewardDaoMysqlImpl;
import com.lodogame.model.UserCostReward;

public class UserCostRewardDaoCacheImpl implements UserCostRewardDao {

	private UserCostRewardDaoMysqlImpl userCostRewardDaoMysqlImpl;

	public void setUserCostRewardDaoMysqlImpl(UserCostRewardDaoMysqlImpl userCostRewardDaoMysqlImpl) {
		this.userCostRewardDaoMysqlImpl = userCostRewardDaoMysqlImpl;
	}

	@Override
	public boolean add(UserCostReward userCostReward) {
		return this.userCostRewardDaoMysqlImpl.add(userCostReward);
	}

	@Override
	public UserCostReward getUserCostReward(String userId, int aid, int rid, Date startTime, Date endTime) {
		// TODO Auto-generated method stub
		return this.userCostRewardDaoMysqlImpl.getUserCostReward(userId, aid, rid, startTime, endTime);
	}

}
