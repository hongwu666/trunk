package com.lodogame.ldsg.service;

import java.util.List;
import java.util.Map;

import com.lodogame.ldsg.bo.EmpireHistoryBO;
import com.lodogame.ldsg.bo.EmpireOccupyBO;
import com.lodogame.ldsg.event.EventHandle;

public interface EmpireService {
	//该位置不是该用户所占领
	public static int POS_IS_NOT_USER =1000;
	//挑战次数不足
	public static int TIMES_NOT_ENOUGTH =1001;
	//当前层已占领
	public static int FLOOR_IS_OCCUPY =1002;
	//当玩家寻宝未满30分钟
	public static int TIME_NOT_30 =1003;
	//自己
	public static int ME =1;
	//敌人
	public static int ENEMY =-1;
	//其他
	public static int OTHER =0;
	//该层该位置已经有人
	public static int POS_HSA_USER =2001;
	/**
	 * 进入
	 * @param floor
	 * @param page
	 */
	public int[] enter(String userId);
	/**
	 * 买次数
	 * @param floor
	 * @param page
	 */
	public int[] buy(String userId);
	/**
	 * 英雄占领
	 * @param map
	 */
	public void occupy(String userId,Map<String, Integer> map,int floor,int pos);
	/**
	 * 切换到第floor层，page页
	 * @param floor
	 * @param page
	 */
	public List<EmpireOccupyBO> switchpage(String userId, int floor, int page);
	/**
	 * 查看自己的收获情况
	 * @param userId
	 */
	public void showGain(String userId,int floor,int page,int pos);	
	/**
	 * 收获
	 * @param userId
	 */
	public void gain(String userId,int floor, boolean auto,int pos);
	
	/**
	 *抢占
	 * @param userId
	 */
	public void fight(String userId,int floor,int pos,EventHandle eventHandle );
	
	public List<EmpireHistoryBO> showHistory(String userId);
	
	public List<Integer> trace(String userId);
	
	public void init();
	
}
