package com.lodogame.game.dao;

import java.util.Date;

import com.lodogame.model.UserPraise;

public interface UserPraiseDao {

	public UserPraise get(String uid, String praisedUserId);

	public boolean add(UserPraise userPraise);

	public boolean update(String uid, String praisedUserId, Date updatedTime);
	/**
	 *  获得今天点赞数
	 * @param uid
	 * @return
	 */
	public int getTodayPraiseNum(String uid);
	/**
	 * 获得今天被点数
	 * @param uid
	 * @return
	 */
	
	public int getTodayPraisedNum(String uid);

}
