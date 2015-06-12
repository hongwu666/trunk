package com.lodogame.ldsg.service;


import com.lodogame.ldsg.bo.ActivityCopyBO;
import com.lodogame.ldsg.event.EventHandle;

/**
 * 练功密室
 * 
 * @author zyz
 * 
 */
public interface BlackRoomService {

	/**
	 * 挑战部队,挑战次数不足
	 */
	public final static int FIGHT_TIMES_NOT_ENOUGH  = 2006;

	/**
	 * 	等级不足
	 */
	public final static int FIGHT_LEVEL_NOT_ENOUGH = 2007;
	
	/**
	 * 	购买挑战次数,vip等级不够
	 */
	public final static int FIGHT_VIP_LEVEL_NOT_ENOUGH = 2008;
	
	/**
	 * 	购买挑战次数金币不够
	 */
	public final static int FIGHT_GOLD_NOT_ENOUGH = 2005;
	
	/**
	 * 挑战部队,已经到达了最高购买次数
	 */
	public final static int FIGHT_TIMES_LIMIT = 2005;
	
	/**
	 * 挑战
	 * 
	 * @param userId
	 * @param targetId
	 */
	public void fight(String userId, int type, EventHandle eventHandle);
	
	/**
	 * 进入副本
	 * 
	 * @param userId
	 * @param type (10,密室。11,宝库)
	 */
	public ActivityCopyBO enter(String userId, int type);

}


