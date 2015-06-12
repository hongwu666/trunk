package com.lodogame.ldsg.service;

import java.util.List;

import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.UserDrawBO;

public interface DrawService {

	/**
	 * 钻石不足
	 */
	public static int DRAW_GOLD_NOT_ENOUGH = 2001;

	/**
	 * 道具不足
	 */
	public static int DRAW_TOOL_NOT_ENOUGH = 2002;

	/**
	 * 活动已经关闭
	 */
	public static int DRAW_ACTIVITY_CLOSE = 2003;

	/**
	 * 获取用户的抽奖信息
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserDrawBO> getList(String userId);

	/**
	 * 抽奖
	 * 
	 * @param userId
	 * @return
	 */
	public CommonDropBO draw(String userId, int id);

}
