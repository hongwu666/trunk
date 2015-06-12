package com.lodogame.ldsg.service;

import java.util.List;

import com.lodogame.ldsg.bo.BattleHeroBO;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.model.RobotUser;

public interface RobotService {

	/**
	 * 根据战半力区间随机一个机器人
	 * 
	 * @param minCapability
	 * @param maxCapability
	 * @return
	 */
	public RobotUser getRobotUser(int minCapability, int maxCapability);

	/**
	 * 根据ID获取机器人
	 * 
	 * @param id
	 * @return
	 */
	public RobotUser getById(long id);

	/**
	 * 根据用户名获取机器人
	 * 
	 * @param username
	 * @return
	 */
	public RobotUser getByName(String username);

	/**
	 * 根据user id 获取机器人
	 * 
	 * @param userId
	 * @return
	 */
	public RobotUser get(String userId);

	/**
	 * 获取机器人英雄列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserHeroBO> getRobotUserHeroBOList(String userId);

	/**
	 * 获取机器人战斗英雄列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<BattleHeroBO> getRobotUserBattleHeroBOList(String userId);

}
