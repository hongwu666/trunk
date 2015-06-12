package com.lodogame.game.dao;

import com.lodogame.model.OnlyoneJoinReward;

public interface OnlyoneJoinRewardDao {

	public OnlyoneJoinReward get(int times);

	public OnlyoneJoinReward getNextReward(int times);

}
