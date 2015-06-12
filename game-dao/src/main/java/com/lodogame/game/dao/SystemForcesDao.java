package com.lodogame.game.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.model.SystemForces;

public interface SystemForcesDao {

	@Autowired
	public List<SystemForces> getSysForcesList();

	/**
	 * 获取单个关卡部队
	 * 
	 * @param forcesId
	 * @return
	 */
	public SystemForces get(int forcesId);

	/**
	 * 根据前置怪获取怪物部队
	 * 
	 * @param preForcesId
	 * @return
	 */
	public List<SystemForces> getSystemForcesByPreForcesId(int preForcesId);

	public List<SystemForces> getSystemForcesByGroupId(int forcesGroupId);

	public SystemForces getLastForcesByGroupId(int forcesGroupId);

}
