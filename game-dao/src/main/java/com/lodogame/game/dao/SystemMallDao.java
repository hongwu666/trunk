package com.lodogame.game.dao;

import java.util.Collection;

import com.lodogame.model.SystemMall;

public interface SystemMallDao {

	/**
	 * 获取商城物品列表
	 * 
	 * @return
	 */
	public Collection<SystemMall> getList();

	/**
	 * 获取商城物品
	 * 
	 * @param mallId
	 * @return
	 */
	public SystemMall get(int mallId);

}
