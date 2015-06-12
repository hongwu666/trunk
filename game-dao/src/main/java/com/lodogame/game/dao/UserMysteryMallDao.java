package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.UserMysteryMallDetail;
import com.lodogame.model.UserMysteryMallInfo;

public interface UserMysteryMallDao {

	/**
	 * 获取用户兑换信息
	 * 
	 * @param userId
	 * @param mallType
	 * @return
	 */
	public UserMysteryMallInfo getUserMysteryMallInfo(String userId, int mallType);

	/**
	 * 获取用户兑换项列表
	 * 
	 * @param userId
	 * @param mallType
	 * @return
	 */
	public List<UserMysteryMallDetail> getUserMysteryMallDetail(String userId, int mallType);

	/**
	 * 获取用户具体兑换项
	 * 
	 * @param userId
	 * @param mallType
	 * @param dropId
	 * @return
	 */
	public UserMysteryMallDetail getUserMysteryMallDetail(String userId, int mallType, int dropId);

	/**
	 * 更新用户相应项已经兑换
	 * 
	 * @param userId
	 * @param mallType
	 * @param dropId
	 * @return
	 */
	public boolean updateUserMysteryMallFlag(String userId, int mallType, int dropId);

	/**
	 * 添加用户兑换列表
	 * 
	 * @param userId
	 * @param list
	 * @return
	 */
	public boolean addUserMysterMallDetail(String userId, int mallType, List<UserMysteryMallDetail> list);

	/**
	 * 添加用衣兑换信息
	 * 
	 * @param userId
	 * @param userMysteryMallInfo
	 * @return
	 */
	public boolean addUserMysterMallInfo(String userId, UserMysteryMallInfo userMysteryMallInfo);

}
