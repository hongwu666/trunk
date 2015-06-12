package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.game.dao.clearcache.ClearCacheOnLoginOut;
import com.lodogame.model.UserStone;

public interface UserStoneDao {

	/**
	 * 获取用户宝石列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserStone> getUserStoneList(String userId);

	/**
	 * 更新
	 * 
	 * @param userStone
	 * @return
	 */
	public boolean addUserStone(String userId, int stoneId, int stoneNum);

	/**
	 * 
	 * @param userId
	 * @param stoneId
	 * @param num
	 * @return
	 */
	public boolean reduceUserStone(String userId, int stoneId, int stoneNum);

	/**
	 * 获取
	 * 
	 * @param userId
	 * @param stoneId
	 * @return
	 */
	public UserStone get(String userId, int stoneId);

	/**
	 * 删除
	 * 
	 * @param userId
	 * @return
	 */
	public boolean deleteZero(String userId);

	/**
	 * 初始化缓存
	 * 
	 * @param userId
	 * @param list
	 * @return
	 */
	public boolean initCache(String userId, List<UserStone> list);

}
