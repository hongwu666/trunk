package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.TavernGroupRate;

public interface TavernGroupRateDao {

	List<TavernGroupRate> getList(int type);
	
}
