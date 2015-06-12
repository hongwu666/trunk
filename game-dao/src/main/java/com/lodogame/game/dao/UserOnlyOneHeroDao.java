package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.UserOnlyOneHero;

public interface UserOnlyOneHeroDao {
	/**
	 * 获得英雄列表
	 * @param userId
	 * @return
	 */
	List<UserOnlyOneHero> getHeros(String userId);
	/**
	 * 获得英雄
	 * @param userId
	 * @return
	 */
	UserOnlyOneHero getHero(String userId,String userHeroId);
	/**
	 * 获得上阵英雄列表
	 * @param userId
	 * @return
	 */
	List<UserOnlyOneHero> getPosHeros(String userId);
	/**
	 * 跟新英雄
	 * @param userId
	 * @param heos
	 */
	void updateHero(String userId, List<UserOnlyOneHero> heos);
	/**
	 * 换阵
	 * @param userId
	 * @param heos
	 */
	void updateHeroPos(String userId, UserOnlyOneHero heo);
	/**
	 * 结束战斗换血，阵
	 * @param userId
	 * @param heos
	 */
	void updateHero(String userId, UserOnlyOneHero heo);
	/**
	 * 插入英雄
	 * @param userId
	 * @param heos
	 */
	void insertHero(String userId, List<UserOnlyOneHero> heos);
	/**
	 *	删除用户上阵英雄
	 * @param userId
	 * @param heos
	 */
	void delteHero(String userId);
	
	/**
	 *	删除用户的某个英雄
	 * @param userId
	 * @param heos
	 */
	void delteHero(String userId,String userHeroId);
	
	/**
	 *	删除用户的某个英雄
	 * @param userId
	 * @param heos
	 */
	void delteHero(String userId,List<String> userHeroId);
}
