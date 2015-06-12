package com.lodogame.ldsg.service;

import com.lodogame.ldsg.event.Event;

public interface EventService {

	/**
	 * 分发事件
	 * 
	 * @param event
	 */
	public void dispatchEvent(Event event);

	public void init();

	public void addVipLevelEvent(String userId, int vipLevel);

	public void addCopperUpdateEvent(String userId, int copperNum);

	public void addMuhonUpdateEvent(String userId, int muhonNum);

	public void addHeroStarEvent(String userId, int heroColor);

	public void addHeroColorEvent(String userId, int heroColor);
	/**
	 * 添加排行榜更新事件
	 * @param userId
	 * @param type
	 * @param obj
	 */
	public void addUpdateRankEvent(String userId,int type,Object obj);

	/**
	 * 侠士战力更新事件
	 * 
	 * @param userId
	 * @param userHeroId
	 */
	public void addHeroPowerUpdateEvent(String userId, String userHeroId);

	/**
	 * 总战力更新事件
	 * 
	 * @param userId
	 */
	public void addUserPowerUpdateEvent(String userId);
}
