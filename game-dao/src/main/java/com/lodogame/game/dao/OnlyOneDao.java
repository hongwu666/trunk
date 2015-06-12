package com.lodogame.game.dao;

import java.util.Collection;
import java.util.List;

import com.lodogame.model.OnlyoneUserHero;
import com.lodogame.model.OnlyoneUserReg;
import com.lodogame.model.OnlyoneUserReward;
import com.lodogame.model.OnlyoneWeekRank;

/**
 * 千人斩dao
 * 
 * @author jacky
 * 
 */
public interface OnlyOneDao {

	/**
	 * 加载数据
	 */
	public void loadData();

	/**
	 * 设置活动的状态
	 * 
	 * @param status
	 * @return
	 */
	public boolean setStatus(int status);

	/**
	 * 本周是否已经结算
	 * 
	 * @return
	 */
	public boolean isWeekCutOff();

	/**
	 * 添加周结算日志
	 * 
	 * @return
	 */
	public boolean addWeekCutOffLog();

	/**
	 * 获取活动的状态
	 * 
	 * @return
	 */
	public int getStatus();

	/**
	 * 增加用户奖励
	 * 
	 * @param onlyoneUserReward
	 * @return
	 */
	public boolean addReward(OnlyoneUserReward onlyoneUserReward);

	/**
	 * 增加周排行积分
	 * 
	 * @param userId
	 * @param username
	 * @param point
	 * @return
	 */
	public boolean addWeekPoint(String userId, String username, double point);

	/**
	 * 增加用户报名信息
	 * 
	 * @param onlyOneReg
	 * @return
	 */
	public boolean add(OnlyoneUserReg onlyOneReg);

	/**
	 * 根据用户id获取玩家报名信息
	 * 
	 * @param userId
	 * @return
	 */
	public OnlyoneUserReg getByUserId(String userId);

	/**
	 * 获取排行列表
	 * 
	 * @return
	 */
	public List<OnlyoneUserReg> getRankList();

	/**
	 * 获取连胜排行列表
	 * 
	 * @return
	 */
	public List<OnlyoneUserReg> getWinRankList();

	/**
	 * 清除数据，重新开始
	 */
	public void cleanData();

	/**
	 * 获取所有报名列表
	 * 
	 * @return
	 */
	public Collection<OnlyoneUserReg> getRegList();

	/**
	 * 获取某个状态的排名列表
	 * 
	 * @return
	 */
	public List<OnlyoneUserReg> getRegList(int status);

	/**
	 * 获取用户排名
	 * 
	 * @param userId
	 * @return
	 */
	public int getRank(String userId);

	/**
	 * 获取获个奖励
	 * 
	 * @param userId
	 * @param id
	 * @return
	 */
	public OnlyoneUserReward getReward(String userId, int id);

	/**
	 * 获取奖励列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<OnlyoneUserReward> getRewardList(String userId);

	/**
	 * 更改奖励状态
	 * 
	 * @param userId
	 * @param id
	 * @param status
	 * @return
	 */
	public boolean updateReward(String userId, int id, int status);

	/**
	 * 清除周排行数据
	 * 
	 * @return
	 */
	public void cleanWeekRank();

	/**
	 * 获取周排行数据
	 * 
	 * @return
	 */
	public List<OnlyoneWeekRank> getWeekRank();

	/**
	 * 保存用户武将血量
	 * 
	 * @param onlyoneUserHero
	 * @return
	 */
	public boolean addOnlyoneUserhero(OnlyoneUserHero onlyoneUserHero);

	/**
	 * 获取用户武将
	 * 
	 * @param userHeroId
	 * @return
	 */
	public int getOnlyoneUserHeroLife(String userHeroId);

	/**
	 * 获取用户武将士气
	 * 
	 * @param userHeroId
	 * @return
	 */
	public int getOnlyoneUserHeroMorale(String userHeroId);

	/**
	 * 删除用户武将
	 * 
	 * @param userId
	 * @return
	 */
	public boolean deleteOnlyoneUserHero(String userId);

}