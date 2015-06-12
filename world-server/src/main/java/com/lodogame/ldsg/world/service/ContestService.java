package com.lodogame.ldsg.world.service;

import java.util.List;
import java.util.Map;

import com.lodogame.ldsg.bo.ContestHistoryBO;
import com.lodogame.ldsg.bo.ContestScheduleBO;
import com.lodogame.ldsg.bo.ContestTopUserBO;
import com.lodogame.ldsg.bo.ContestUserBO;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.world.model.ContestUserReady;

public interface ContestService {

	/**
	 * 保存进入跨服决赛的队伍列表
	 * 
	 * @param list
	 * @return
	 */
	public boolean saveContestReadyUser(String serverId, List<ContestUserReady> list);

	/**
	 * 获取状态
	 * 
	 * @return
	 */
	public int getStatus();

	/**
	 * 获取英雄
	 * 
	 * @param userId
	 * @param handle
	 */
	public void getHeros(String userId, final EventHandle handle);

	/**
	 * 进入
	 * 
	 * @param userId
	 * @return
	 */
	public Map<String, Object> enter(String userId);

	/**
	 * 获取赛程
	 * 
	 * @return
	 */
	public List<ContestScheduleBO> getScheduleList();

	/**
	 * 获取对战记录
	 * 
	 * @param userId
	 * @return
	 */
	public List<ContestHistoryBO> getHistorys(String userId);

	/**
	 * 获取对手信息
	 * 
	 * @param userId
	 * @return
	 */
	public ContestUserBO getTargetInfo(String userId);

	/**
	 * 获取神殿玩家列表
	 * 
	 * @return
	 */
	public List<ContestTopUserBO> getContestTopUserList();

}
