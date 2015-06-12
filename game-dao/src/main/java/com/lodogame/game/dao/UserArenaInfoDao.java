package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.UserArenaHero;
import com.lodogame.model.UserArenaInfo;
import com.lodogame.model.UserArenaSeriesGift;
import com.lodogame.model.UserArenaSeriesGiftInfo;

public interface UserArenaInfoDao {

	public UserArenaInfo get(String userId);

	public boolean update(UserArenaInfo userArenaInfo);

	public int getRank(String userId);

	public boolean setScore(String userId, int score);

	public List<String> getRangeRank(int rank);

	public List<UserArenaInfo> getList();

	public void clearGift();

	/**
	 * 跟住积分获取记录
	 * 
	 * @param lowerScore
	 * @param upperScore
	 * @return
	 */
	public List<UserArenaInfo> getRandByScore(int lowerScore, int upperScore, String userId);

	/**
	 * 排名前10
	 * 
	 * @param rank
	 * @return
	 */
	public List<String> getRangeRankTen();

	/**
	 * 排名前100
	 * 
	 * @param rank
	 * @return
	 */
	public List<String> getRangeRankHun();

	public void clear();

	public void clearScore();

	public boolean isSendReward(String date);

	public boolean addSendReward(String date);

	/**
	 * 更新用户等级
	 * 
	 * @param userId
	 * @param level
	 * @return
	 */
	public boolean updateUserLevel(String userId, int level);

	/**
	 * 备份用户比武信息表 <li>一个礼拜备份一次</li>
	 * 
	 * @return
	 */
	public boolean backup();

	public UserArenaSeriesGiftInfo getSeriesGifts(String userId);

	public List<UserArenaSeriesGift> getSeriesGift(String userId);

	public void insertSeriesGift(UserArenaSeriesGift gift);

	public void updateSeriesGift(UserArenaSeriesGift gift);

	public boolean insertArenaHero(List<UserArenaHero> list);

	public List<UserArenaHero> getUserArenaHero(String userId);

}
