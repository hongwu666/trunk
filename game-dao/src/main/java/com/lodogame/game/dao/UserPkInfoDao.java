package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.UserPkInfo;

public interface UserPkInfoDao {

	/**
	 * 增加用户排名信息
	 * 
	 * @param userPkInfo
	 * @return
	 */
	public boolean add(UserPkInfo userPkInfo);

	/**
	 * 增加用户排名信息
	 * 
	 * @param userPkInfo
	 * @return
	 */
	public boolean add(List<UserPkInfo> userPkInfoList);

	/**
	 * 根据排名读取用户争霸信息
	 * 
	 * @param rank
	 * @return
	 */
	public UserPkInfo getByRank(int rank);

	/**
	 * 获取全部
	 * 
	 * @return
	 */
	public List<UserPkInfo> getList();

	/**
	 * 根据用户ID读取用户争霸信息
	 * 
	 * @param userId
	 * @return
	 */
	public UserPkInfo getByUserId(String userId);

	/**
	 * 更新用户排名信息
	 * 
	 * @param userPkInfo
	 * @return
	 */
	public boolean update(UserPkInfo userPkInfo, String targetUserId);

	/**
	 * 更新用户排名信息(临时)
	 * 
	 * @param random
	 * 
	 * @param userPkInfo
	 * @return
	 */
	public boolean updateRank(String userId, int random);

	/**
	 * 获取最后一名
	 * 
	 * @return
	 */
	public int getLastRank();

	/**
	 * 跟住范围拿用户比武信息
	 * 
	 * @param lowRank
	 * @param upperRank
	 * @return
	 */
	public List<UserPkInfo> getRangeRank(int lowRank, int upperRank, String userId);

}
