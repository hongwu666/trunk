package com.lodogame.ldsg.service;

import java.util.List;

import com.lodogame.ldsg.bo.HeroPowerRankBO;
import com.lodogame.ldsg.bo.RankBO;
import com.lodogame.ldsg.bo.UserLevelRankBO;
import com.lodogame.ldsg.bo.UserPowerRankBO;

public interface RankService {

	/**
	 * 玩家战力排行
	 * 
	 * @return
	 */
	public List<UserPowerRankBO> getUserPowerRankList();

	/**
	 * 玩家等级排行
	 * 
	 * @return
	 */
	public List<UserLevelRankBO> getUserLevelRankList();

	/**
	 * 玩家武将战力排行
	 * 
	 * @return
	 */
	public List<HeroPowerRankBO> getHeroPowerRankList();
	/**
	 * 根据类型获得榜列表
	 * @param type
	 * @return
	 */
	public List<RankBO> getRankList(int type);

}
