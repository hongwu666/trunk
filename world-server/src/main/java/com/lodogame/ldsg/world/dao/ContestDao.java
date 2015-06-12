package com.lodogame.ldsg.world.dao;

import java.util.List;

import com.lodogame.ldsg.world.model.ContestUserReady;
import com.lodogame.ldsg.world.model.ServerRegList;
import com.lodogame.model.ContestBattleReport;
import com.lodogame.model.ContestHistory;
import com.lodogame.model.ContestInfo;
import com.lodogame.model.ContestTopUser;
import com.lodogame.model.ContestWorldUser;

public interface ContestDao {

	/**
	 * 
	 * @param report
	 * @return
	 */
	public boolean addReport(ContestBattleReport report);

	/**
	 * 注册服务器
	 * 
	 * @param serverId
	 * @return
	 */
	public boolean regServer(String serverId);

	/**
	 * 获取跨服战本服报名列表
	 * 
	 * @return
	 */
	public List<ContestWorldUser> getList();

	/**
	 * 获取参赛玩家列表
	 * 
	 * @return
	 */
	public List<ContestUserReady> getReadyList();

	/**
	 * 获取
	 * 
	 * @param userId
	 * @return
	 */
	public ContestWorldUser get(String userId);

	/**
	 * 保存
	 * 
	 * @param contestUser
	 * @return
	 */
	public boolean replace(ContestWorldUser contestUser);

	/**
	 * 保存
	 * 
	 * @param contestTopUser
	 * @return
	 */
	public boolean replace(ContestTopUser contestTopUser);

	/**
	 * 清空神殿玩家
	 * 
	 * @return
	 */
	public boolean truncateTopUser();

	/**
	 * 获取神殿玩家列表
	 * 
	 * @return
	 */
	public List<ContestTopUser> getContestTopUserList();

	/**
	 * 保存
	 * 
	 * @param history
	 * @return
	 */
	public boolean replace(ContestHistory history);

	/**
	 * 保存
	 * 
	 * @param contestUserReady
	 * @return
	 */
	public boolean replace(ContestUserReady contestUserReady);

	/**
	 * 获取跨服战状态
	 * 
	 * @param week
	 * @return
	 */
	public ContestInfo getContestStatus(int week);

	/**
	 * 保存状态
	 * 
	 * @param week
	 * @return
	 */
	public boolean saveContestStatus(ContestInfo contestInfo);

	/**
	 * 清空表
	 * 
	 * @return
	 */
	public boolean truncateContestUser();

	/**
	 * 清即将参赛的队伍表
	 * 
	 * @return
	 */
	public boolean truncateContestUserReady();

	/**
	 * 获取
	 * 
	 * @return
	 */
	public List<ServerRegList> getServerRegList();

	/**
	 * 获取跨服战对战历史
	 * 
	 * @param userId
	 * @return
	 */
	public List<ContestHistory> getContestHistory(String userId);
}
