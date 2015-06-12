package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.SystemGoldSet;

public interface SystemGoldSetDao {

	/**
	 * 获取单个
	 * 
	 * @param amount
	 * @return
	 */
	public SystemGoldSet getByPayAmount(int type, double amount);

	public List<SystemGoldSet> getAll();

	public SystemGoldSet get(int waresId);

}
