package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.SystemPrice;

public interface SystemPriceDao {

	public SystemPrice get(int type, int times);

	public List<SystemPrice> getList(int type);

}
