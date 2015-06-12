package com.lodogame.game.dao;

import com.lodogame.model.SystemTool;

/**
 * 系统道具dao
 * 
 * @author jacky
 * 
 */
public interface SystemToolDao {

	/**
	 * 获取系统道具
	 * 
	 * @param toolId
	 * @return
	 */
	public SystemTool get(int toolId);

}
