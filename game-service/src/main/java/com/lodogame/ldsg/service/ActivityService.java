package com.lodogame.ldsg.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lodogame.ldsg.bo.ActivityCostBO;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.CopyActivityBO;
import com.lodogame.ldsg.bo.DropToolBO;
import com.lodogame.ldsg.bo.SystemActivityBO;
import com.lodogame.ldsg.bo.ToolExchangeBO;
import com.lodogame.ldsg.bo.ToolExchangeCountBO;
import com.lodogame.ldsg.bo.UserLoginRewardInfoBO;
import com.lodogame.ldsg.bo.UserOnlineRewardBO;
import com.lodogame.ldsg.bo.UserPayRewardBO;
import com.lodogame.ldsg.bo.UserRecivePowerBO;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.model.SystemActivity;

public interface ActivityService {

	public final static int RECIVE_POWER_NOT_IN_THE_PERIOD = 2001;

	/**
	 * 物品兑换-用户道具数量不足
	 */
	public final static int TOOL_NOT_ENOUGH = 2001;
	/**
	 * 物品兑换-达到兑换次数上限
	 */
	public final static int REACH_TOOL_EXCHANGE_LIMIT = 2002;

	/**
	 * 领体
	 */
	public final static int RECIVE_POWER_RECIVE_ALL = 2002;

	/**
	 * 当天已经签到过
	 */
	public final static int CHECK_IN_HAS_CHECK_IN = 2001;

	/**
	 * 当到达可领取的最大天数
	 */
	public final static int CHECK_IN_IS_MAX_DAY = 2002;

	/**
	 * 当前30天用户登入礼包不可以领，没有登入日志
	 */
	public final static int RECIVE_NO_LOGIN_INFO = 2001;

	/**
	 * 30天登入礼包，该天的已经领取过了
	 */
	public final static int RECIVE_LOGIN_REWARD_HAS_RECEIVE = 2002;

	/**
	 * 30天登入礼包，传入参数不正确
	 */
	public final static int RECIVE_LOGIN_REWARD_HAS_WRONG_PARAM = 2003;

	/**
	 * 30天登入礼包，获取奖励信息失败
	 */
	public final static int GET_SYSTEM_LOGIN_REWARD_INFO_HAS_WRONG = 2004;

	/**
	 * 兑换武将--材料不足
	 */
	public final static int EXCHANGE_HERO_TOOL_NOT_ENOUGH = 2001;

	/**
	 * 领取vip礼包，vip等级不足
	 */
	public final static int RECEIVE_VIP_GIFTBAG_VIP_NOT_ENOUGH = 2001;

	/**
	 * 领取vip礼包,当前已经领取
	 */
	public final static int RECEIVE_VIP_GIFTBAG_HAS_RECEIVE = 2002;

	/**
	 * 领取首充礼包，没有充过值
	 */
	public final static int RECEIVE_FIRST_PAY_GIFTBAG_VIP_NOT_ENOUGH = 2001;

	/**
	 * 领取首充礼包，已经领取过
	 */
	public final static int RECEIVE_FIRST_PAY_GIFTBAG_HAS_RECEIVE = 2002;

	/**
	 * 礼包码礼包,礼包码无效
	 */
	public final static int RECEIVE_GIFT_CODE_GIFTBAG_CODE_ERROR = 2001;

	/**
	 * 礼包码领取，已经领取
	 */
	public final static int RECEIVE_GIFT_CODE_GIFTBAG_HAS_RECEIVE = 2002;

	/**
	 * 刷新可兑换武将列表，用户金币不足
	 */
	public final static int REFRESH_HERO_EXCHANGE_GOLD_NOT_ENOUGH = 2001;

	/**
	 * 领取在线奖励，时间未到
	 */
	public final static int RECIVE_ONLINE_REWARD_TIME_NOT_ENOUGH = 2001;

	/**
	 * 活动已停止
	 */
	public final static int ACTIVITY_IS_CLOSED = 2200;

	/**
	 * 奖励领取次数超过限制
	 */
	public static final int PAY_REWARD_LIMIT_ERROR = 2001;

	/**
	 * 充值奖励数额条件不足
	 */
	public static final int PAY_REWARD_MONEY_NOT_ENOUGH = 2002;

	/**
	 * 七天登陆-领取失败
	 */
	public static final int USER_LOGIN_REWARD7_RECEIVE_FAILED = 2001;

	/**
	 * 七天登陆-奖励已经领取
	 */
	public static final int USER_LOGIN_REWARD7_HAS_RECEIVED = 2002;

	/**
	 * 充值奖励数额条件不足
	 */
	public static final int LOGIN_ = 2002;

	/**
	 * 30天签到的最大天数
	 */

	public static final int LOGIN_REWARD_MAX_DAY = 31;
	/**
	 * 积天充值的最大天数
	 */
	public static final int TOTAL_DAY_PAY_REWARD_MAX_DAY = 30;

	/**
	 * 30天签到的最小天数
	 */
	public static final int LOGIN_REWARD_MIN_DAY = 1;

	/**
	 * 30天签到的的奖励领取状态，1表示为未领取
	 */
	public static final int LOGIN_REWARD_NOT_GET = 1;

	/**
	 * 30天签到的的奖励领取状态，2表示为已领取
	 */
	public static final int LOGIN_REWARD_HAS_GET = 2;

	/**
	 * 30天签到的的奖励领取状态，表示还有可以领取的奖励没有领取
	 */
	public static final int LOGIN_REWARD_NOT_GIVEN = 1;

	/**
	 * 30天签到的的奖励领取状态，表示没有可以领取的奖励了
	 */
	public static final int LOGIN_REWARD_HAS_NOT_GIVEN = 2;

	/**
	 * 30天签到的的奖励领取状态，30天都领取完了
	 */
	public static final int LOGIN_REWARD_BEYOND_MAX_DAY = 3;

	/**
	 * 期限在线奖励-倒计时时间
	 */
	public static final long LIM_ONLINE_REWARD_COUNTDOWN = 1 * 60 * 60 * 1000;

	/**
	 * 期限在线奖励-今天的奖励已经领取
	 */
	public static final int LIM_ONLINE_REWARD_RECEIVED = 2001;

	/**
	 * 期限在线奖励-没到领取时间
	 */
	public static final int LIME_ONLINE_REWARD_BEFORE_RECTIME = 2002;

	/**
	 * 期限在线奖励-没到领取时间
	 */
	public static final int HERO_HAS_LOCKED = 2007;

	/**
	 * 期限在线奖励-已经领取
	 */
	public static final int TOTAL_DAY_PAY_REWARD_RECEIVED = 2001;

	/**
	 * 积天奖励-没满足领取条件
	 */
	public static final int TOTAL_DAY_PAY_REWARD_CANNT_GIVEN = 2002;

	/**
	 * 该奖励已经领取
	 */
	public static final int TAVERN_REBATE_GET = 2001;

	/**
	 * 招募次数不足
	 */
	public static final int TAVERN_REBATE_NO_TIMES = 2002;

	/**
	 * 武将背包不足
	 */
	public static final int EXCHANGE_HERO_BAG_NOT_ENOUGH = 2003;

	/**
	 * 装备背包不足
	 */
	public static final int EXCHANGE_EQUIP_BAG_NOT_ENOUGH = 2004;

	/**
	 * 消耗元宝不足
	 */
	public static final int REDUCE_GOLD_NOT_ENOUGH = 2002;

	/**
	 * 魂石商店-在本次刷新时间内已经兑换过该道具
	 */
	public static final int EXCHANGE_TOOL_EXIST = 2001;

	/**
	 * 魂石商店-兑换的道具不存在
	 */
	public static final int EXCHANGE_TOOL_NOT_EXIST = 2003;

	/**
	 * 魂石商店-江山令数量不足
	 */
	public static final int EXCHANGE_TOOL_NOT_ENOUGH = 2002;

	/**
	 * 魂石商店-元宝不足
	 */
	public static final int GOLD_NOT_ENOUGH = 2001;
	/**
	 * 消费金额不足
	 */
	public static final int COST_REWARD_MONEY_NOT_ENOUGH=2002;
	/**
	 * 魂石商店-刷新令不足
	 */
	public static final int REFRESH_NUM_NOT_ENOUGH = 2002;

	/**
	 * 吃包子个数
	 */
	public static final int RECEIVE_POWER_NUM = 1;
	
	/**
	 * 消耗活动奖励已领取
	 */
	public static final int ACTIVYTY_COST_REWARD_HAS_RECEIVED = 1;
	
	/**
	 * 消耗活动奖励可领取
	 */
	public static final int ACTIVYTY_COST_REWARD_CAN_RECEIVED = 0;
	
	/**
	 * 消耗活动奖励未满足不能领取
	 */
	public static final int ACTIVYTY_COST_REWARD_NOT_RECEIVED = -1;
	/**
	 * 消耗活动消费金额不足
	 */
	public static final int ACTIVYTY_COST_REWARD_NOT_ENOUGHT = 2001;
	/**
	 * 每日活动时间未到
	 */
	public static final int ACTIVYTY_DAY_TIME_NOT_ENOUGHT = 2002;
	/**
	 * 每日活动时间已过
	 */
	public static final int ACTIVYTY_DAY_TIME_PASS = 2003;
	/**
	 * 获取当前副本活动
	 * 
	 * @return
	 */
	public List<CopyActivityBO> getCopyActivityList();

	/**
	 * 领取体力
	 * 
	 * @param userId
	 * @return
	 */
	public boolean receivePower(String userId, int type);

	/**
	 * 获取用户吃包子信息
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserRecivePowerBO> getUserRecivePowerInfo(String userId);

	/**
	 * 领取VIP礼包
	 * 
	 * @param type
	 * @return
	 */
	public CommonDropBO receiveVipGiftBag(String userId, EventHandle handle);

	/**
	 * 领取首充礼包
	 * 
	 * @param type
	 * @return
	 */
	public CommonDropBO receiveFirstPayGiftBag(String userId, EventHandle handle);

	/**
	 * 领取新手引导礼包
	 * 
	 * @param userId
	 * @param giftBagId
	 * @param handle
	 * @return
	 */
	public CommonDropBO receiveRookieGuideGiftBag(String userId, int giftBagId, EventHandle handle);

	/**
	 * 领取礼包码礼包
	 * 
	 * @param type
	 * @return
	 */
	public CommonDropBO receiveGiftCodeGiftBag(String userId, String code, EventHandle handle);

	/**
	 * 获取礼包状态
	 * 
	 * @param type
	 * @return 礼包状态(1 可以领取 2 已经领取 3 不是VIP 4 没有充过值)
	 */
	public int getGiftBagStatus(String userId, int type);

	/**
	 * 领取用户在线礼包
	 * 
	 * @param userId
	 * @return
	 */
	public CommonDropBO receiveOnlineReward(String userId);

	/**
	 * 获取用户在线奖励信息BO
	 * 
	 * @param userId
	 * @return
	 */
	public UserOnlineRewardBO getUserOnlineRewardBO(String userId);

	/**
	 * 领取用户单笔充值奖励
	 * 
	 * @param userId
	 * @param aid
	 * @return
	 */
	public CommonDropBO receiveOncePayReward(String userId, int rid);

	/**
	 * 领取用户累计充值奖励
	 * 
	 * @param userId
	 * @param aid
	 * @return
	 */
	public CommonDropBO receiveTotalPayReward(String userId, int rid);

	/**
	 * 获取活动面板活动BO列表
	 * 
	 * @return
	 */
	public List<SystemActivityBO> getDisplayActivityBOList(String userId);

	List<ToolExchangeCountBO> toolExchangeCount(String userId);

	Map<String, Object> toolExchange(String userId, int toolExchangeId, int num);

	/**
	 * 获取用户单笔充值的奖励信息
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserPayRewardBO> getUserOncePayRewardList(String userId);

	/**
	 * 获取用户累计充值的奖励信息
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserPayRewardBO> getUserTotalPayRewardList(String userId);

	/**
	 * 检测是否有单笔充值奖励激活
	 * 
	 * @param userId
	 */
	public boolean checkOncePayReward(String userId);

	/**
	 * 检测是否有累计充值奖励激活
	 * 
	 * @param userId
	 * @return
	 */
	public boolean checkTotalPayReward(String userId);

	/**
	 * 获取用户的某一个单笔充值奖励信息
	 * 
	 * @param userId
	 * @param rid
	 * @return
	 */
	public UserPayRewardBO getUserOncePayRewardById(String userId, int rid);

	/**
	 * 获取用户的某一个累计充值奖励信息
	 * 
	 * @param userId
	 * @param rid
	 * @return
	 */
	public UserPayRewardBO getUserTotalPayRewardById(String userId, int rid);

	/**
	 * 
	 * @param systemActivity
	 * @return
	 */
	public Map<String, Date> computeActivityTime(SystemActivity systemActivity);

	/**
	 * 感恩节抽奖活动
	 * 
	 * @param userId
	 * @param num
	 *            抽奖次数
	 * @return
	 */
	public List<DropToolBO> activityDraw(String userId, int num);

	/**
	 * 活动副本是否开始
	 * 
	 * @param forcesId
	 * @return
	 */
	public boolean isForcesActivityOpen(int forcesId);

	/**
	 * 活动是否开放
	 * 
	 * @param activityId
	 * @return
	 */
	public boolean isActivityOpen(int activityId);

	/**
	 * 领取礼品包
	 * 
	 * @param userId
	 * @param giftbagId
	 * @param useType
	 * @return
	 */
	public CommonDropBO pickGiftBagReward(String userId, int giftbagId, int useType);

	/**
	 * 
	 * @return
	 */
	public List<ToolExchangeBO> getToolExchangeBoList();

	/**
	 * 检测活动是否开启
	 * 
	 * @param systemActivity
	 */
	public int checkActivityIsOpenAdd(SystemActivity systemActivity);

	/**
	 * 数据同步
	 * 
	 * @param table
	 * @param sqls
	 * @return
	 */
	public boolean dataSync(String table, String sqls);

	/**
	 * 用户30登入礼包
	 * 
	 * @return
	 */
	public CommonDropBO receiveLoginReward(String userId, int loginDay);

	/**
	 * 获取30天登入信息
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserLoginRewardInfoBO> getUserLoginRewardInfo(String userId);

	/**
	 * 30天登陆奖励是否有没有领取得
	 */
	public int checkLoginRewardHasGiven(String userID);

	/**
	 * 验证今天是否已经登入过了
	 * 
	 * @param userId
	 * @return
	 */
	public void checkUserLoginRewardInfo(String userId);

	/**
	 * 刷新魂石商店
	 * 
	 * @param userId
	 * @return
	 */
	public Map<String, Object> refreshStoneMall(String userId, int type);

	/**
	 * 兑换魂石商店物品
	 * 
	 * @param userId
	 * @param toolId
	 * @return
	 */
	public CommonDropBO exchageStoneTool(String userId, int systemId);

	public boolean canReceiveVipGiftBag(String userId);

	/**
	 * 七天登陆
	 */
	public Map<String, Object> getLoginRewardList(String userId);

	public Map<String, Object> recLoginReward(String userId, int day);
	
	/**
	 * 根据活动ID获得消耗奖励列表
	 * @param userId
	 * @param useType
	 * @return
	 */
	public ActivityCostBO getActivityCostReward(String userId, int activityId);

	/**
	 * 根据活动ID,奖励ID领奖
	 * @param userId
	 * @param useType
	 * @return
	 */
	public CommonDropBO reciveActivityCostReward(String userId,int activityId,int rewardId);
	
	/**
	 * 根据活动ID获得消耗奖励列表
	 * @param userId
	 * @param useType
	 * @return
	 */
	public ActivityCostBO getActivityDayReward(String userId, int activityId);

	/**
	 * 根据活动ID,奖励ID领奖
	 * @param userId
	 * @param useType
	 * @return
	 */
	public CommonDropBO reciveActivityDayReward(String userId,int activityId,int rewardId);
}
