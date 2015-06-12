package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.SystemBlackRoomConfig;



public interface SystemBlackRoomConfigDao {

	/**
	 * 根据挑战次数，获取练功密室的信息
	 * 
	 * @param time
	 * @return
	 */
	public SystemBlackRoomConfig getBlackRoomConfigByTime(int time);
	
	
	/**
	 * 获取练功密室的信息
	 * 
	 * @param time
	 * @return
	 */
	public List<SystemBlackRoomConfig> getBlackRoomConfigByTime();

}
