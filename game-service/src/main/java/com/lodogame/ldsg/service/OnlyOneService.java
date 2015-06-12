package com.lodogame.ldsg.service;

import java.util.Date;
import java.util.List;

import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.OnlyOneRankBO;
import com.lodogame.ldsg.bo.OnlyOneRegBO;
import com.lodogame.ldsg.bo.OnlyOneRewardBO;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.model.OnlyoneUserReg;

/**
 * 百人斩service
 * 
 * @author jacky
 * 
 */
public interface OnlyOneService {

	/**
	 * 活动未开始
	 */
	public final static int ENTER_ARENA_NOT_START = 2001;

	/**
	 * 活动已经结束
	 */
	public final static int ENTER_ARENA_HAS_FINISH = 2002;

	/**
	 * 精力不足
	 */
	public final static int VIGOUR_NOT_ENOUGH = 2001;
	
	/**
	 * 死亡武将
	 */
	public final static int DEAN_HERO = 2003;
	
	/**
	 * 相同武将不能上阵
	 */
	public final static int SIMPLE_HERO = 2004;
	
	/**
	 * 最后一个英雄不能下阵
	 */
	public final static int ONE_HERO = 2005;

	/**
	 * 进入百人斩
	 * 
	 * @param userId
	 * @return
	 */
	public boolean enter(String userId);

	/**
	 * 退出百人斩
	 * 
	 * @param userId
	 * @return
	 */
	public boolean quit(String userId);

	/**
	 * 获取用户报名数据
	 * 
	 * @param userId
	 * @return
	 */
	public OnlyoneUserReg getByUserId(String userId);

	/**
	 * 开始排队
	 * 
	 * @param userId
	 * @return
	 */
	public boolean startMatcher(String userId);

	/**
	 * 获取排行榜
	 * 
	 * @return
	 */
	public List<OnlyOneRankBO> getRankList(String userId, boolean self);

	/**
	 * 获取连胜榜
	 * 
	 * @return
	 */
	public List<OnlyOneRankBO> getWinRankList();

	/**
	 * 获取用户个人信息
	 * 
	 * @param userId
	 * @return
	 */
	public OnlyOneRegBO getRegBO(String userId);

	/**
	 * 执行命令
	 * 
	 * @param cmd
	 */
	public void execute(String cmd);

	/**
	 * 获取开始时间
	 * 
	 * @return
	 */
	public Date getStartTime();

	/**
	 * 获取结束时间
	 * 
	 * @return
	 */
	public Date getEndTime();

	/**
	 * 获取用户排名
	 * 
	 * @param userId
	 * @return
	 */
	public int getRank(String userId);

	/**
	 * 获取奖励列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<OnlyOneRewardBO> getRewardList(String userId);

	/**
	 * 领取奖励
	 * 
	 * @param userId
	 * @param id
	 * @return
	 */
	public CommonDropBO receive(String userId, int id);
	/**
	 * 展现英雄列表
	 * @param userId
	 * @return
	 */
	public List<UserHeroBO> showHero(String userId);
	/**
	 * 换阵
	 * @param userId
	 * @param h1
	 * @param h2
	 * @param pos1
	 * @param pos2
	 */
	public void changePos(String userId,String h1,String h2,int pos1,int pos2);
}
