package com.lodogame.ldsg.service;

import java.util.List;
import java.util.Map;

import com.lodogame.ldsg.bo.BattleBO;
import com.lodogame.ldsg.bo.ContestUserBO;
import com.lodogame.ldsg.bo.ContestUserHeroBO;
import com.lodogame.ldsg.event.EventHandle;

public interface ContestService {

	/**
	 * 进入
	 * 
	 * @param userId
	 */
	public void enter(String userId, EventHandle handle);

	/**
	 * 保存出战阵容
	 * 
	 * @param userId
	 * @param m1
	 * @param m3
	 * @param m3
	 * @return
	 */
	public boolean savePos(String userId, Map<String, Integer> m1, Map<String, Integer> m2, Map<String, Integer> m3);

	/**
	 * 选择出战队伍
	 * 
	 * @param userId
	 * @param team
	 * @return
	 */
	public boolean selectTeam(String userId, int team);

	/**
	 * 获取战斗BO
	 * 
	 * @param userId
	 * @return
	 */
	public BattleBO getBattleBO(String userId, boolean force);

	/**
	 * 获取玩家跨服战英雄列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<ContestUserHeroBO> getContestUserHeroBOList(String userId);

	/**
	 * 跨服的比赛开始
	 * 
	 * @return
	 */
	public boolean worldStart();

	/**
	 * 推送状态
	 */
	public void pushStatus(int clientStatus);

	/**
	 * 获取对手信息
	 * 
	 * @param userId
	 * @param handle
	 */
	public void getTargetInfo(String userId, EventHandle handle);

	/**
	 * 获取对手信息
	 * 
	 * @param usreId
	 * @return
	 */
	public ContestUserBO getTargetInfo(String usreId);

	/**
	 * 获取赛程
	 * 
	 * @param handle
	 */
	public void getScheduleList(EventHandle handle);

	/**
	 * 获取跨服神殿用户列表
	 * 
	 * @param handle
	 */
	public void getTopUserList(EventHandle handle);

	/**
	 * 获取用户英雄列表(阵上)
	 * 
	 * @param handle
	 */
	public void getHeros(String userId, EventHandle handle);

	/**
	 * 获取用户英雄列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<ContestUserHeroBO> getHeros(String userId);

	/**
	 * 获取用户对战记录
	 * 
	 * @param handle
	 */
	public void getHistorys(String userId, EventHandle handle);

	/**
	 * 发送奖励
	 * 
	 * @param userId
	 * @param rand
	 */
	public void sendReward(String userId, int rand);

	/**
	 * 发送在线奖励
	 */
	public void sendOnlineReward();

	/**
	 * 发消息
	 * 
	 * @param username
	 * @param title1
	 * @param title2
	 */
	public void sendMsg(String username, String title1, String title2);

	/**
	 * 清除已出场队伍
	 */
	public void cleanFinishTeam();

}
