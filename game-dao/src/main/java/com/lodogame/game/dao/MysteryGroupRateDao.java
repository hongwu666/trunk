package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.MysteryGroupRate;

public interface MysteryGroupRateDao {

	public List<MysteryGroupRate> getList(int mallType, int type);

}
