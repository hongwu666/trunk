package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.SystemForcesMonster;

public interface SystemForcesMonsterDao {

	/**
	 * 获取部队怪物列表
	 * 
	 * @param forcesId
	 * @return
	 */
	public List<SystemForcesMonster> getForcesMonsterList(int forcesId);

	/**
	 * 获取所有部队怪物列表
	 * 
	 * @return
	 */
	public List<SystemForcesMonster> getList();

}
