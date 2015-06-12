package com.lodogame.ldsg.service;

import java.util.List;

import com.lodogame.ldsg.bo.UserTavernBO;
import com.lodogame.ldsg.ret.TavernDrawRet;
import com.lodogame.model.UserTavern;

/**
 * 酒馆service
 * 
 * @author jacky
 * 
 */
public interface TavernService {

	/**
	 * 银币不够
	 */
	public final static int DRAW_NOT_ENOUGH_COPPER = 2001;

	/**
	 * 金币不够
	 */
	public final static int DRAW_NOT_ENOUGH_GOLD = 2002;

	/**
	 * 次数不够
	 */
	public final static int DRAW_NOT_ENOUGH_TIMES = 2003;

	/**
	 * 抽奖
	 * 
	 * @param type
	 * @return
	 */
	public TavernDrawRet draw(String userId, int type, int times);

	/**
	 * 获取酒馆CD信息
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserTavernBO> getTavernCDInfo(String userId);

	public boolean isCoolTimeFinished(UserTavern userTavern);

	public UserTavern getUserTavern(String userId, int type);
}
