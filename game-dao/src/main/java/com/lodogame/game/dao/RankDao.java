package com.lodogame.game.dao;

import java.util.Date;
import java.util.List;

import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.model.OnlyoneWeekRank;
import com.lodogame.model.PayRankInfo;
import com.lodogame.model.ProcessRankInfo;
import com.lodogame.model.StarRankInfo;
import com.lodogame.model.UserDrawLog;
import com.lodogame.model.UserHeroInfo;
import com.lodogame.model.UserLevelRank;
import com.lodogame.model.UserPowerInfo;
import com.lodogame.model.VipRankInfo;

public interface RankDao {
	/**
	 * 更新剧情副本进度榜
	 * 
	 * @return
	 */

	public void updateStoryRank(ProcessRankInfo processRankInfo);

	/**
	 * 更新精英副本进度榜
	 * 
	 * @return
	 */

	public void updateEliteRank(ProcessRankInfo processRankInfo);

	/**
	 * 更新资源星数榜
	 * 
	 * @return
	 */

	public void updateResourceStarRank(StarRankInfo starRankInfo);

	/**
	 * 更新副本星数榜
	 * 
	 * @return
	 */

	public void updateEctypeStarRank(StarRankInfo starRankInfo);

	/**
	 * 更新vip榜
	 * 
	 * @return
	 */
	public void updateVipRank(VipRankInfo vipRankInfo);

	/**
	 * 更新周充值榜
	 * 
	 * @return
	 */
	public void updatePayRank(PayRankInfo payRankInfo);

	public int getEctypeStarByUserId(String userId);

	public int getResourceStarByUserId(String userId);

	/**
	 * 总战力排行榜
	 * 
	 * @param size
	 * @return
	 */
	public List<UserPowerInfo> getUserPowerInfo(int size);

	/**
	 * 根据排名获得总战力
	 * 
	 * @param rank
	 * @return
	 */
	public int getUserPowerByRank(int rank);

	/**
	 * 根据排名获得总战力
	 * 
	 * @param rank
	 * @return
	 */
	public int getUserPowerByUserId(String userId);

	/**
	 * 英雄战力排行榜
	 * 
	 * @param size
	 * @return
	 */
	public List<UserHeroInfo> getUserHeroInfo(int size);

	/**
	 * 等级排行榜
	 * 
	 * @param size
	 * @return
	 */
	public List<UserLevelRank> getUserLevelRank(int size);

	/**
	 * 剧情副本进度排行榜
	 * 
	 * @param size
	 * @return
	 */
	public List<ProcessRankInfo> getStoryRank(int size);

	/**
	 * 精英副本进度排行榜
	 * 
	 * @param size
	 * @return
	 */
	public List<ProcessRankInfo> getEliteRank(int size);

	/**
	 * 副本星数排行榜
	 * 
	 * @param size
	 * @return
	 */
	public List<StarRankInfo> getEctypeStarRank(int size);

	/**
	 * 资源副本星数行榜
	 * 
	 * @param size
	 * @return
	 */
	public List<StarRankInfo> getResourceStarRank(int size);

	/**
	 * vip排行榜
	 * 
	 * @param size
	 * @return
	 */
	public List<VipRankInfo> getVipRank(int size);

	/**
	 * 周充值排行榜
	 * 
	 * @param size
	 * @return
	 */
	public List<PayRankInfo> getPayRank(int size);

	/**
	 * 千人斬周排行榜
	 * 
	 * @param size
	 * @return
	 */
	public List<OnlyoneWeekRank> getOnlyoneRank(int size);

	/**
	 * 抽奖榜
	 * 
	 * @param size
	 * @return
	 */
	public List<UserDrawLog> getDrawRank(int size, Date date);

	/**
	 * 清空周充值榜
	 */
	public void cleanPayRank();

	/**
	 * 清空千人斬周榜
	 */
	public void cleanOnlyoneRank();

	/**
	 * 清空周充值榜
	 */
	public void cleanResourceStarRank();

}
