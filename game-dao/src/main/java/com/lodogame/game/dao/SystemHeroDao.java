package com.lodogame.game.dao;

import com.lodogame.model.SystemHero;

/**
 * 系统武将DAO
 * 
 * @author jacky
 * 
 */

public interface SystemHeroDao {

	/**
	 * 获取系统武将
	 * 
	 * @param systemHeroId
	 * @return
	 */
	public SystemHero get(Integer systemHeroId);

	/**
	 * 获取 systemHeroId
	 */
	public int getSystemHeroId(Integer heroId, Integer heroColor);

}
