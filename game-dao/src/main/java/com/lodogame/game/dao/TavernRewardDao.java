package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.TavernReward;

public interface TavernRewardDao {

	public List<TavernReward> getList(int type);
}
