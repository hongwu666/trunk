package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.ContestBattleReport;
import com.lodogame.model.ContestHistory;
import com.lodogame.model.ContestInfo;
import com.lodogame.model.ContestReward;
import com.lodogame.model.ContestUser;
import com.lodogame.model.ContestUserHero;
import com.lodogame.model.log.ContestRewardLog;

public interface ContestDao {

	/**
	 * 获取跨服战本服报名列表
	 * 
	 * @return
	 */
	public List<ContestUser> getList();

	/**
	 * 获取武将列表
	 * 
	 * @return
	 */
	public List<ContestUserHero> getHeroList(String userId);

	/**
	 * 获取
	 * 
	 * @param userId
	 * @return
	 */
	public ContestUser get(String userId);

	/**
	 * 保存
	 * 
	 * @param contestUser
	 * @return
	 */
	public boolean replace(ContestUser contestUser);

	/**
	 * 更新选择的队伍
	 * 
	 * @param userId
	 * @param selectTeam
	 * @return
	 */
	public boolean updateSelectTeam(String userId, int selectTeam);

	/**
	 * 更新淘汰回合和回合胜利场次
	 * 
	 * @param userId
	 * @param roundWin
	 * @param deadRound
	 * @return
	 */
	public boolean updateDeadRound(String userId, int roundWin, int deadRound);

	/**
	 * 更新用户武将team和pos
	 * 
	 * @param userHeroId
	 * @param team
	 * @param pos
	 * @return
	 */
	public boolean updateHeroPosAndTeam(String userHeroId, int team, int pos);

	/**
	 * 更新为已布好阵
	 * 
	 * @param userId
	 * @param roundWin
	 * @param deadRound
	 * @return
	 */
	public boolean updateArrayFinish(String userId);

	/**
	 * 保存
	 * 
	 * @param contestHistory
	 * @return
	 */
	public boolean replace(ContestHistory contestHistory);

	/**
	 * 保存
	 * 
	 * @param contestUserHero
	 * @return
	 */
	public boolean replace(ContestUserHero contestUserHero);

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
	 * 获取跨服战用户武将
	 * 
	 * @param userHeroId
	 * @return
	 */
	public ContestUserHero getContestUserHero(String userHeroId);

	/**
	 * 清空表
	 * 
	 * @return
	 */
	public boolean truncateContestUser();

	/**
	 * 清空英雄表
	 * 
	 * @return
	 */
	public boolean truncateContestHero();

	/**
	 * 设置所有用户为未布阵状态
	 * 
	 * @return
	 */
	public boolean setArrayNotFinish();

	/**
	 * 
	 * @param report
	 * @return
	 */
	public boolean addReport(ContestBattleReport report);

	/**
	 * 按队获取用户英雄列表
	 * 
	 * @param userId
	 * @param team
	 * @return
	 */
	public List<ContestUserHero> getContestUserHeroList(String userId, int team);

	/**
	 * 获取跨服战对战历史
	 * 
	 * @param userId
	 * @return
	 */
	public List<ContestHistory> getContestHistory(String userId);

	/**
	 * 删除没有布阵的队伍
	 * 
	 * @param userId
	 */
	public void deleteNotArrayTeam();

	/**
	 * 保存跨服战奖励日志
	 * 
	 * @param contestRewardLog
	 * @return
	 */
	public boolean add(ContestRewardLog contestRewardLog);

	/**
	 * 获取跨服战奖励
	 * 
	 * @param category
	 * @param rank
	 * @return
	 */
	public ContestReward getReward(int category, int rank);

}
