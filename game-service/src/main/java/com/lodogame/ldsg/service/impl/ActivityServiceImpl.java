package com.lodogame.ldsg.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.CommandDao;
import com.lodogame.game.dao.GiftCodeDao;
import com.lodogame.game.dao.GiftDropDao;
import com.lodogame.game.dao.LogDao;
import com.lodogame.game.dao.PaymentLogDao;
import com.lodogame.game.dao.SystemActivityDao;
import com.lodogame.game.dao.SystemCostRewardDao;
import com.lodogame.game.dao.SystemGiftbagDao;
import com.lodogame.game.dao.SystemHeroStoneMallDao;
import com.lodogame.game.dao.SystemLoginReward7Dao;
import com.lodogame.game.dao.SystemLoginRewardDao;
import com.lodogame.game.dao.SystemOncePayRewardDao;
import com.lodogame.game.dao.SystemRecivePowerDao;
import com.lodogame.game.dao.SystemTotalPayRewardDao;
import com.lodogame.game.dao.ToolExchangeDao;
import com.lodogame.game.dao.UserCostRewardDao;
import com.lodogame.game.dao.UserDailyGainLogDao;
import com.lodogame.game.dao.UserEquipDao;
import com.lodogame.game.dao.UserExtinfoDao;
import com.lodogame.game.dao.UserGiftLogDao;
import com.lodogame.game.dao.UserGiftbagDao;
import com.lodogame.game.dao.UserHeroDao;
import com.lodogame.game.dao.UserLoginLogDao;
import com.lodogame.game.dao.UserLoginRewardInfoDao;
import com.lodogame.game.dao.UserMapperDao;
import com.lodogame.game.dao.UserOnlineLogDao;
import com.lodogame.game.dao.UserOnlineRewardDao;
import com.lodogame.game.dao.UserPayRewardDao;
import com.lodogame.game.dao.UserToolDao;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.game.utils.json.Json;
import com.lodogame.ldsg.bo.ActivityCostBO;
import com.lodogame.ldsg.bo.ActivityCostRewardBO;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.CopyActivityBO;
import com.lodogame.ldsg.bo.DropDescBO;
import com.lodogame.ldsg.bo.DropToolBO;
import com.lodogame.ldsg.bo.HeroStoneMallBo;
import com.lodogame.ldsg.bo.SystemActivityBO;
import com.lodogame.ldsg.bo.ToolExchangeBO;
import com.lodogame.ldsg.bo.ToolExchangeCountBO;
import com.lodogame.ldsg.bo.ToolExchangeEquipBO;
import com.lodogame.ldsg.bo.ToolExchangeHeroBO;
import com.lodogame.ldsg.bo.ToolExchangeLooseBO;
import com.lodogame.ldsg.bo.ToolExchangeReceiveBO;
import com.lodogame.ldsg.bo.ToolExchangeToolBO;
import com.lodogame.ldsg.bo.UserEquipBO;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.bo.UserLoginRewardInfoBO;
import com.lodogame.ldsg.bo.UserOnlineRewardBO;
import com.lodogame.ldsg.bo.UserPayRewardBO;
import com.lodogame.ldsg.bo.UserRecivePowerBO;
import com.lodogame.ldsg.bo.UserToolBO;
import com.lodogame.ldsg.constants.ActivityId;
import com.lodogame.ldsg.constants.ActivityTargetType;
import com.lodogame.ldsg.constants.ActivityType;
import com.lodogame.ldsg.constants.GiftBagType;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.constants.ToolId;
import com.lodogame.ldsg.constants.ToolType;
import com.lodogame.ldsg.constants.ToolUseType;
import com.lodogame.ldsg.constants.UserDailyGainLogType;
import com.lodogame.ldsg.constants.UserPayRewardStatus;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.helper.ActivityHelper;
import com.lodogame.ldsg.helper.BOHelper;
import com.lodogame.ldsg.helper.DropDescHelper;
import com.lodogame.ldsg.helper.MallHelper;
import com.lodogame.ldsg.helper.UserOnlineRewardHelper;
import com.lodogame.ldsg.service.ActivityService;
import com.lodogame.ldsg.service.ActivityTaskService;
import com.lodogame.ldsg.service.DailyTaskService;
import com.lodogame.ldsg.service.EquipService;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.ToolService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.GiftCode;
import com.lodogame.model.GiftDrop;
import com.lodogame.model.GiftbagDropTool;
import com.lodogame.model.PaymentLog;
import com.lodogame.model.StoneMallTimeLog;
import com.lodogame.model.SystemActivity;
import com.lodogame.model.SystemCostReward;
import com.lodogame.model.SystemDailyTask;
import com.lodogame.model.SystemGiftbag;
import com.lodogame.model.SystemHeroStoneMall;
import com.lodogame.model.SystemLoginReward;
import com.lodogame.model.SystemLoginReward7;
import com.lodogame.model.SystemOncePayReward;
import com.lodogame.model.SystemRecivePower;
import com.lodogame.model.SystemTotalPayReward;
import com.lodogame.model.ToolExchange;
import com.lodogame.model.User;
import com.lodogame.model.UserCostReward;
import com.lodogame.model.UserEquip;
import com.lodogame.model.UserGiftLog;
import com.lodogame.model.UserGiftbag;
import com.lodogame.model.UserHero;
import com.lodogame.model.UserHeroStoneMallLog;
import com.lodogame.model.UserLoginLog;
import com.lodogame.model.UserLoginRewardInfo;
import com.lodogame.model.UserMapper;
import com.lodogame.model.UserOnlineLog;
import com.lodogame.model.UserOnlineReward;
import com.lodogame.model.UserPayReward;
import com.lodogame.model.UserToolExchangeLog;

public class ActivityServiceImpl implements ActivityService {

	private static final Logger logger = Logger.getLogger(ActivityServiceImpl.class);

	private static Map<String, List<Integer>> stoneMap = new HashMap<String, List<Integer>>();

	@Autowired
	private DailyTaskService dailyTaskService;

	@Autowired
	private UserLoginLogDao userLoginLogDao;

	@Autowired
	private SystemLoginReward7Dao systemLoginReward7Dao;

	@Autowired
	private SystemTotalPayRewardDao systemTotalPayRewardDao;

	@Autowired
	private UserToolDao userToolDao;

	@Autowired
	private CommandDao commandDao;

	@Autowired
	private UserHeroDao userHeroDao;

	@Autowired
	private EquipService equipService;

	@Autowired
	private UserExtinfoDao userExtinfoDao;

	@Autowired
	private ToolExchangeDao toolExchangeDao;

	@Autowired
	private UserEquipDao userEquipDao;

	@Autowired
	private SystemActivityDao systemActivityDao;

	@Autowired
	private UserDailyGainLogDao userDailyGainLogDao;

	@Autowired
	private SystemRecivePowerDao systemRecivePowerDao;

	@Autowired
	private UserService userService;

	@Autowired
	private ToolService toolService;

	@Autowired
	private SystemGiftbagDao systemGiftbagDao;

	@Autowired
	private HeroService heroService;

	@Autowired
	private UserGiftbagDao userGiftbagDao;

	@Autowired
	private PaymentLogDao paymentLogDao;

	@Autowired
	private UserCostRewardDao userCostRewardDao;

	@Autowired
	private GiftCodeDao giftCodeDao;

	@Autowired
	private ActivityTaskService activityTaskService;

	@Autowired
	private UserOnlineRewardDao userOnlineRewardDao;

	@Autowired
	private UserOnlineLogDao userOnlineLogDao;

	@Autowired
	private SystemOncePayRewardDao systemOncePayRewardDao;

	@Autowired
	private SystemCostRewardDao systemCostRewardDao;

	@Autowired
	private UserPayRewardDao userPayRewardDao;

	@Autowired
	private UserMapperDao userMapperDao;

	@Autowired
	private UserLoginRewardInfoDao userLoginRewardInfoDao;

	@Autowired
	private SystemLoginRewardDao systemLoginRewardDao;

	@Autowired
	private GiftDropDao giftDropDao;
	@Autowired
	private LogDao logDao;

	@Autowired
	private SystemHeroStoneMallDao systemHeroStoneMallDao;

	@Autowired
	private UserGiftLogDao userGiftLogDao;

	@Override
	public List<CopyActivityBO> getCopyActivityList() {

		List<SystemActivity> systemActivityList = this.systemActivityDao.getList(ActivityType.ACTIVITY_TYPE_COPY);

		List<CopyActivityBO> copyActivityBOList = new ArrayList<CopyActivityBO>();
		for (SystemActivity systemActivity : systemActivityList) {

			CopyActivityBO copyActivityBO = BOHelper.crateCopyActivityBO(systemActivity);

			Map<String, Date> startEndTime = computeActivityTime(systemActivity);
			copyActivityBO.setStartTime(startEndTime.get("startTime").getTime());
			copyActivityBO.setEndTime(startEndTime.get("endTime").getTime());

			copyActivityBOList.add(copyActivityBO);
		}

		return copyActivityBOList;

	}

	/**
	 * 根据活动在哪些天开放和现在的时间计算出活动的开始时间和结束时间
	 * 
	 * @param 活动的开放时间
	 *            ，例如：1,3,5 表示在星期一、二、五开放
	 * @return 活动的开始时间和结束时间
	 * @param opemWeeks
	 */
	@Override
	public Map<String, Date> computeActivityTime(SystemActivity systemActivity) {
		Map<String, Date> startEndTime = new HashMap<String, Date>();

		String openWeeks = systemActivity.getOpenWeeks();

		// 如果每天都开放，则将结束时间设置为无限远的时间
		if (openWeeks.equals("1,2,3,4,5,6,7")) {
			startEndTime.put("startTime", systemActivity.getStartTime());
			startEndTime.put("endTime", systemActivity.getEndTime());
			return startEndTime;
		}

		Date now = new Date();

		Date startTime = null;
		Date endTime = null;

		int dayOfWeek = DateUtils.getDayOfWeek(); // 当天这星期中的第几天

		if (openWeeks.indexOf(String.valueOf(dayOfWeek)) != -1) {// 当天有开放

			startTime = now;

			for (int i = 1; i <= 7; i++) {// 找结束时间

				Date date = DateUtils.addDays(now, i);
				int dayWeek = DateUtils.getDayOfWeek(date);
				if (openWeeks.indexOf(String.valueOf(dayWeek)) == -1) {// 不开放了
					endTime = DateUtils.getDateAtMidnight(date);// 那么那天的凌晨就是结束时间
					break;
				}
			}

		} else {// 当天没有开放

			for (int i = 1; i <= 7; i++) {// 找开始时间

				Date date = DateUtils.addDays(now, i);
				int dayWeek = DateUtils.getDayOfWeek(date);

				if (openWeeks.indexOf(String.valueOf(dayWeek)) != -1) {// 开放
					if (startTime == null) {
						startTime = DateUtils.getDateAtMidnight(date);// 那么那天的凌晨就是开始时间
					}
				} else if (startTime != null) {
					endTime = DateUtils.getDateAtMidnight(date);
				}
			}

		}

		startEndTime.put("startTime", startTime);
		if (systemActivity.getEndTime().before(endTime)) {
			startEndTime.put("endTime", systemActivity.getEndTime());
		} else {
			startEndTime.put("endTime", endTime);
		}

		return startEndTime;

	}

	public List<UserRecivePowerBO> getUserRecivePowerInfo(String userId) {

		List<SystemRecivePower> systemRecivePowerList = this.systemRecivePowerDao.getList();

		List<UserRecivePowerBO> userRecivePowerBOList = new ArrayList<UserRecivePowerBO>();

		for (SystemRecivePower systemRecivePower : systemRecivePowerList) {

			int type = systemRecivePower.getType();

			UserRecivePowerBO userRecivePowerBO = new UserRecivePowerBO();

			int times = this.userDailyGainLogDao.getUserDailyGain(userId, type);

			userRecivePowerBO.setTimes(times);
			userRecivePowerBO.setStartTime(getTime(systemRecivePower.getStartTime()));
			userRecivePowerBO.setEndTime(getTime(systemRecivePower.getEndTime()));
			userRecivePowerBO.setType(type);

			userRecivePowerBOList.add(userRecivePowerBO);
		}

		return userRecivePowerBOList;
	}

	private long getTime(String strTime) {

		try {
			Date date = DateUtils.str2Date(DateUtils.getDate() + " " + strTime);
			return date.getTime();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return System.currentTimeMillis();
	}

	@Override
	public boolean receivePower(String userId, int type) {

		SystemRecivePower systemRecivePower = systemRecivePowerDao.get(type);

		if (!ActivityHelper.isNowCanRecive(systemRecivePower)) {
			throw new ServiceException(RECIVE_POWER_NOT_IN_THE_PERIOD, "领取体力出错，当前不在领取时间段userId[" + userId + "], type[" + type + "]");
		}

		int amount = this.userDailyGainLogDao.getUserDailyGain(userId, type);

		if (amount >= RECEIVE_POWER_NUM) {
			throw new ServiceException(RECIVE_POWER_RECIVE_ALL, "领取体力出错，已经领取完.userId[" + userId + "], type[" + type + "]");
		} else {

			// 记录次数
			boolean success = this.userDailyGainLogDao.addUserDailyGain(userId, type, 1);
			if (success) {
				// 给体力,前提是没有爆管

				// this.userService.addPower(userId, 60,
				// ToolUseType.ADD_BY_ACTIVITY, null);
				// 推送用户
				this.userService.pushUser(userId);

				// 活跃度任务
				this.activityTaskService.updateActvityTask(userId, ActivityTargetType.RECIVE_POWER, 1);

			} else {
				throw new ServiceException(ServiceReturnCode.FAILD, "领取体力出错，userId[" + userId + "], type[" + type + "]");
			}
		}

		// 给“领取体力”的每日任务加上1次完成次数
		if (type == 1) {
			dailyTaskService.receive(userId, SystemDailyTask.RECEIVE_POWER_NOON);
		} else if (type == 2) {
			dailyTaskService.receive(userId, SystemDailyTask.RECEIVE_POWER_NIGHT);
		}

		return true;
	}

	@Override
	public CommonDropBO receiveVipGiftBag(String userId, EventHandle handle) {

		User user = userService.get(userId);
		int vipLevel = user.getVipLevel();

		if (canReceiveVipGiftBag(userId) == false) {
			String message = "领取VIP礼包失败，当天已经领取.userId[" + userId + "]";
			throw new ServiceException(RECEIVE_VIP_GIFTBAG_HAS_RECEIVE, message);
		}

		SystemGiftbag systemGiftbag = this.systemGiftbagDao.getVipGiftbag(vipLevel);
		UserGiftbag userGiftbag = new UserGiftbag();
		userGiftbag.setUserId(userId);
		userGiftbag.setType(systemGiftbag.getType());
		userGiftbag.setGiftbagId(systemGiftbag.getGiftbagId());
		userGiftbag.setRemake(vipLevel);

		// 记录日志
		this.userGiftbagDao.addOrUpdateUserGiftbag(userGiftbag);

		// 给奖励
		return this.pickGiftBagReward(userId, systemGiftbag.getGiftbagId(), ToolUseType.ADD_VIP_GIFT_BAG);
	}

	@Override
	public boolean canReceiveVipGiftBag(String userId) {

		UserGiftbag userGiftbag = this.userGiftbagDao.getLast(userId, GiftBagType.VIP_GIFTBAG);
		User user = userService.get(userId);
		int vipLevel = user.getVipLevel();
		if (userGiftbag == null) {
			return true;
		}

		Date now = new Date();

		if (userGiftbag.getRemake() == 0 && vipLevel > 0) {
			// 异常修改时间
			if (userGiftbag.getUpdatedTime().after(now)) {
				return false;
			} else {
				return true;
			}
		}

		if (DateUtils.isSameDay(userGiftbag.getUpdatedTime(), now) || userGiftbag.getUpdatedTime().after(now)) {
			return false;
		}

		return true;
	}

	@Override
	public CommonDropBO pickGiftBagReward(String userId, int giftbagId, int useType) {

		List<GiftbagDropTool> giftBagDropToolList = this.systemGiftbagDao.getGiftbagDropToolList(giftbagId);
		if (giftBagDropToolList.isEmpty()) {
			logger.error("礼包掉落为空.giftbagId[" + giftbagId + "]");
		}

		List<DropDescBO> dropDescBOList = DropDescHelper.toDropDesc(giftBagDropToolList);

		CommonDropBO commonDropBO = this.toolService.give(userId, dropDescBOList, useType);

		return commonDropBO;

	}

	@Override
	public CommonDropBO receiveGiftCodeGiftBag(String userId, String code, EventHandle handle) {

		GiftCode giftCode = this.giftCodeDao.get(code);
		if (giftCode == null || giftCode.getFlag() != 0) {
			String message = "领取礼包码礼包出错.礼包码无效.userId[" + userId + "], code[" + code + "]";
			logger.warn(message);
			throw new ServiceException(RECEIVE_GIFT_CODE_GIFTBAG_CODE_ERROR, message);
		} else {
			// 指定服务器
			String serverIds = giftCode.getServerIds();
			if (serverIds != null && !serverIds.equalsIgnoreCase("all")) {

				UserMapper userMapper = this.userMapperDao.get(userId);
				if (serverIds.indexOf(userMapper.getServerId()) == -1) {
					String message = "领取礼包码礼包出错.礼包码对该服无效.userId[" + userId + "], code[" + code + "], serverIds[" + serverIds + "], serverId[" + userMapper.getServerId() + "]";
					logger.warn(message);
					throw new ServiceException(RECEIVE_GIFT_CODE_GIFTBAG_CODE_ERROR, message);
				}

			}
		}

		int timesLimit = giftCode.getTimesLimit();
		if (timesLimit == 0) {
			timesLimit = 1;
		}

		UserGiftLog userGiftLog = this.userGiftLogDao.get(userId, giftCode.getGiftBagType());
		if (userGiftLog != null) {
			String message = "领取激活码礼包失败，已经领取过.userId[" + userId + "]";
			logger.warn(message);
			throw new ServiceException(RECEIVE_GIFT_CODE_GIFTBAG_HAS_RECEIVE, message);
		}

		// 更新为已经使用
		if (!this.giftCodeDao.update(code, userId)) {
			String message = "领取礼包码礼包出错.礼包码无效.userId[" + userId + "], code[" + code + "]";
			logger.warn(message);
			throw new ServiceException(RECEIVE_GIFT_CODE_GIFTBAG_CODE_ERROR, message);
		}

		GiftDrop giftDrop = this.giftDropDao.get(giftCode.getType(), giftCode.getGiftBagType());
		if (null == giftDrop || StringUtils.isEmpty(giftDrop.getToolIds())) {
			String message = "礼包掉落为空.getType[" + giftCode.getType() + "]" + ",getGiftBagType[" + giftCode.getGiftBagType() + "]";
			logger.error(message);
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

		userGiftLog = new UserGiftLog();
		userGiftLog.setBigType(giftCode.getGiftBagType());
		userGiftLog.setCreatedTime(new Date());
		userGiftLog.setGiftCode(code);
		userGiftLog.setUserId(userId);
		this.userGiftLogDao.add(userGiftLog);

		return this.toolService.give(userId, giftDrop.getToolIds(), ToolUseType.ADD_GIFTKEY_GIFT_BAG);
	}

	@Override
	public CommonDropBO receiveFirstPayGiftBag(String userId, EventHandle handle) {

		int payAmount = paymentLogDao.getPaymentTotalGold(userId);
		if (payAmount <= 0) {
			String message = "领取首充礼包失败，没有充过值.userId[" + userId + "], payAmount[" + payAmount + "]";
			logger.warn(message);
			throw new ServiceException(RECEIVE_FIRST_PAY_GIFTBAG_VIP_NOT_ENOUGH, message);
		}

		UserGiftbag userGiftbag = this.userGiftbagDao.getLast(userId, GiftBagType.FIRST_PAY_GIFTBAG);
		if (userGiftbag != null) {
			String message = "领取首充礼包失败，已经领取过.userId[" + userId + "]";
			logger.warn(message);
			throw new ServiceException(RECEIVE_VIP_GIFTBAG_HAS_RECEIVE, message);
		}

		SystemGiftbag systemGiftbag = this.systemGiftbagDao.getFirstPayGiftbag();

		userGiftbag = new UserGiftbag();
		userGiftbag.setUserId(userId);
		userGiftbag.setType(GiftBagType.FIRST_PAY_GIFTBAG);
		userGiftbag.setGiftbagId(userGiftbag.getGiftbagId());

		// 记录日志
		this.userGiftbagDao.addOrUpdateUserGiftbag(userGiftbag);

		return this.pickGiftBagReward(userId, systemGiftbag.getGiftbagId(), ToolUseType.ADD_FIRST_PAY_GIFT_BAG);
	}

	@Override
	public CommonDropBO receiveRookieGuideGiftBag(String userId, int giftBagId, EventHandle handle) {
		int count = this.userGiftbagDao.getCount(userId, GiftBagType.ROOKIE_GUIDE_GIFTBAT, giftBagId);
		if (count >= 1) {
			String message = "领取新手引导礼包失败，已经领取过.userId[" + userId + "].giftBatId[" + giftBagId + "]";
			logger.warn(message);
			throw new ServiceException(PAY_REWARD_LIMIT_ERROR, message);
		}
		UserGiftbag userGiftbag = new UserGiftbag();
		userGiftbag.setUserId(userId);
		userGiftbag.setType(GiftBagType.ROOKIE_GUIDE_GIFTBAT);
		userGiftbag.setGiftbagId(giftBagId);

		// 记录日志
		this.userGiftbagDao.addOrUpdateUserGiftbag(userGiftbag);

		return this.pickGiftBagReward(userId, giftBagId, ToolUseType.ADD_ROOKIE_GUIDE_GIFT_BAG);
	}

	@Override
	public int getGiftBagStatus(String userId, int type) {

		// 礼包状态(1 可以领取 2 已经领取 3 不是VIP 4 没有充过值)

		int status = 1;

		if (type == GiftBagType.VIP_GIFTBAG) {

			// UserBO userBo = this.userService.getUserBO(userId);
			/*
			 * if (userBo.getVipLevel() == 0) { return 3; }
			 */

			// UserGiftbag userGiftbag = this.userGiftbagDao.getLast(userId,
			// type);
			if (!canReceiveVipGiftBag(userId)) {
				return 2;
			}

		} else if (type == GiftBagType.FIRST_PAY_GIFTBAG) {
			int payAmount = this.paymentLogDao.getPaymentTotalGold(userId);
			if (payAmount == 0) {
				return 4;
			}

			UserGiftbag userGiftbag = this.userGiftbagDao.getLast(userId, type);
			if (userGiftbag != null) {
				return 2;
			}

		}

		return status;
	}

	@Override
	public CommonDropBO receiveOnlineReward(String userId) {

		UserOnlineRewardBO userOnlineRewardBO = this.getUserOnlineRewardBO(userId);
		if (userOnlineRewardBO.getSubType() == 0) {// 已经领取完
			String message = "领取礼包出错，没有可领取的礼包.userId[" + userId + "]";
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

		if (userOnlineRewardBO.getTime() > System.currentTimeMillis()) {// 时间未到
			String message = "领取礼包出错，时间未到.userId[" + userId + "]";
			throw new ServiceException(RECIVE_ONLINE_REWARD_TIME_NOT_ENOUGH, message);
		}
		int subType = userOnlineRewardBO.getSubType();

		SystemGiftbag systemGiftbag = this.systemGiftbagDao.getOnlineGiftBag(subType);

		this.userOnlineRewardDao.update(userId, subType);

		return this.pickGiftBagReward(userId, systemGiftbag.getGiftbagId(), ToolUseType.ADD_ONLINE_GIFT_BAG);

	}

	@Override
	public UserOnlineRewardBO getUserOnlineRewardBO(String userId) {

		UserOnlineRewardBO userOnlineRewardBO = new UserOnlineRewardBO();

		int subType = 0;

		long userOnline = 0;
		long userLastOnline = 0;
		Date now = new Date();

		UserOnlineLog userOnlineLog = this.userOnlineLogDao.getLastOnlineLog(userId);

		UserOnlineReward userOnlineReward = this.userOnlineRewardDao.get(userId);
		if (userOnlineReward == null) {
			userOnlineReward = new UserOnlineReward();
			userOnlineReward.setSubType(0);
			userOnlineReward.setCreatedTime(now);
			userOnlineReward.setUpdatedTime(now);
			userOnlineReward.setUserId(userId);
			this.userOnlineRewardDao.add(userOnlineReward);
		}

		subType = userOnlineReward.getSubType() + 1;
		if (subType <= 7) {
			userOnline = this.userOnlineLogDao.getUserOnline(userId, userOnlineReward.getUpdatedTime()) * 1000;
			if (userOnlineLog == null || userOnlineReward.getUpdatedTime().after(userOnlineLog.getLoginTime())) {// 用户最后领取礼包的时间比最后登录的时间晚
				// 用最后领的时间来算
				userLastOnline = now.getTime() - userOnlineReward.getUpdatedTime().getTime();
			} else {
				// 用最后登录的时间来算
				userLastOnline = now.getTime() - userOnlineLog.getLoginTime().getTime();
			}
		} else {
			subType = 0;
		}

		userOnlineRewardBO.setSubType(subType);

		if (subType != 0) {

			long userTotalOnline = userOnline + userLastOnline;
			long needTotalOnline = UserOnlineRewardHelper.getOnlineRewaradTime(subType);
			long subTime = needTotalOnline - userTotalOnline;
			if (subTime < 0) {
				subTime = 0;
			}

			userOnlineRewardBO.setTime(System.currentTimeMillis() + subTime);
			userOnlineRewardBO.setMinute((int) (needTotalOnline / 60 / 1000));
		}

		return userOnlineRewardBO;

	}

	@Override
	public List<UserPayRewardBO> getUserOncePayRewardList(String userId) {
		List<UserPayRewardBO> ret = new ArrayList<UserPayRewardBO>();
		// 获得活动期间用户的订单以及已经领取过的订单记录
		SystemActivity systemActivity = systemActivityDao.get(ActivityId.ONCE_PAY_REWARD_ID);
		try {
			checkActivityIsOpen(systemActivity);
		} catch (ServiceException e) {
			logger.debug("非活动期间，无需返回");
			return null;
		}

		List<SystemOncePayReward> systemOncePayRewards = systemOncePayRewardDao.getAll();

		for (SystemOncePayReward reward : systemOncePayRewards) {
			UserPayRewardBO bo = getUserOncePayReward(userId, reward, systemActivity);
			ret.add(bo);
		}

		return ret;
	}

	@Override
	public UserPayRewardBO getUserOncePayRewardById(String userId, int rid) {
		SystemOncePayReward reward = this.systemOncePayRewardDao.getById(rid);
		SystemActivity systemActivity = systemActivityDao.get(ActivityId.ONCE_PAY_REWARD_ID);

		return getUserOncePayReward(userId, reward, systemActivity);
	}

	private UserPayRewardBO getUserOncePayReward(String userId, SystemOncePayReward reward, SystemActivity systemActivity) {

		UserPayRewardBO bo = new UserPayRewardBO();
		bo.setRewardId(reward.getId());
		bo.setPayMoney(reward.getPayMoney());

		bo.setDropToolBoList(DropDescHelper.parseDropTool(reward.getDropToolIds()));

		SystemOncePayReward nextReward = systemOncePayRewardDao.getNextById(reward.getId());

		List<PaymentLog> paymentLogs = getPaymentLogsByTimeAndMoney(userId, systemActivity, reward, nextReward);

		if (paymentLogs == null || paymentLogs.isEmpty()) {
			bo.setStatus(UserPayRewardStatus.CANNOT_RECEIVE);
			return bo;
		}

		// 取出已经领取过的订单ID
		List<UserPayReward> receivedOrderList = userPayRewardDao.getReceivedOrderIdList(userId, ActivityId.ONCE_PAY_REWARD_ID, reward.getId(), systemActivity.getStartTime(),
				systemActivity.getEndTime());

		int max = paymentLogs.size();
		if (max > reward.getTimesLimit()) {
			max = reward.getTimesLimit();
		}
		bo.setTimesLimit(max);

		// 如果领取次数已经超出限制，则表示已领取过，否则则表示可领取
		if (receivedOrderList != null && reward.getTimesLimit() <= receivedOrderList.size()) {
			bo.setTimes(receivedOrderList.size());
			bo.setStatus(UserPayRewardStatus.RECEIVED);
		} else {

			for (PaymentLog log : paymentLogs) {
				if (!contaninOrder(receivedOrderList, log.getOrderId())) {
					bo.setTimes(receivedOrderList.size());
					bo.setStatus(UserPayRewardStatus.CAN_RECEIVE);
					break;
				}
				bo.setTimes(receivedOrderList.size());
				bo.setStatus(UserPayRewardStatus.CANNOT_RECEIVE);
			}
		}

		return bo;
	}

	@Override
	public List<UserPayRewardBO> getUserTotalPayRewardList(String userId) {
		List<UserPayRewardBO> ret = new ArrayList<UserPayRewardBO>();

		SystemActivity systemActivity = systemActivityDao.get(ActivityId.TOTAL_PAY_REWARD_ID);
		try {
			checkActivityIsOpen(systemActivity);
		} catch (ServiceException e) {
			logger.debug("非活动期间，无需返回");
			return null;
		}

		List<SystemTotalPayReward> systemTotalPayRewards = systemTotalPayRewardDao.getAll();
		for (SystemTotalPayReward reward : systemTotalPayRewards) {
			UserPayRewardBO bo = getUserTotalPayReward(userId, reward, systemActivity);
			ret.add(bo);
		}
		return ret;
	}

	@Override
	public UserPayRewardBO getUserTotalPayRewardById(String userId, int rid) {
		SystemTotalPayReward reward = this.systemTotalPayRewardDao.getById(rid);
		SystemActivity systemActivity = systemActivityDao.get(ActivityId.TOTAL_PAY_REWARD_ID);

		return getUserTotalPayReward(userId, reward, systemActivity);
	}

	private UserPayRewardBO getUserTotalPayReward(String userId, SystemTotalPayReward reward, SystemActivity systemActivity) {

		UserPayRewardBO bo = new UserPayRewardBO();
		bo.setRewardId(reward.getId());
		bo.setPayMoney(reward.getPayMoney());
		bo.setTimesLimit(reward.getTimesLimit());

		bo.setDropToolBoList(DropDescHelper.parseDropTool(reward.getDropToolIds()));
		bo.setTimesLimit(reward.getTimesLimit());

		// 查看充值总金额是否达到领取要求
		int totalPay = paymentLogDao.getPaymentTotalByTime(userId, systemActivity.getStartTime(), systemActivity.getEndTime());
		bo.setHasPayMoney(totalPay * 10);
		if (totalPay < reward.getPayMoney()) {
			bo.setStatus(UserPayRewardStatus.CANNOT_RECEIVE);
		} else {
			// 查看是否已经领取
			UserPayReward userPayReward = userPayRewardDao.getUserPayReward(userId, ActivityId.TOTAL_PAY_REWARD_ID, reward.getId(), systemActivity.getStartTime(),
					systemActivity.getEndTime());
			if (userPayReward != null) {
				bo.setTimes(1);
				bo.setStatus(UserPayRewardStatus.RECEIVED);
			} else {
				bo.setStatus(UserPayRewardStatus.CAN_RECEIVE);
			}
		}

		return bo;
	}

	/**
	 * 根据时间以及金额金额区间获取订单
	 * 
	 * @param userId
	 * @param systemActivity
	 * @param reward
	 * @param nextReward
	 * @return
	 */
	private List<PaymentLog> getPaymentLogsByTimeAndMoney(String userId, SystemActivity systemActivity, SystemOncePayReward reward, SystemOncePayReward nextReward) {
		List<PaymentLog> paymentLogs = null;
		if (nextReward != null) {
			if (reward.getPayMoney() < nextReward.getPayMoney()) {
				paymentLogs = paymentLogDao.getPaymenList(userId, systemActivity.getStartTime(), systemActivity.getEndTime(), reward.getPayMoney(), nextReward.getPayMoney());
			} else {
				paymentLogs = paymentLogDao.getPaymenList(userId, systemActivity.getStartTime(), systemActivity.getEndTime(), reward.getPayMoney());
			}
		} else {
			paymentLogs = paymentLogDao.getPaymenList(userId, systemActivity.getStartTime(), systemActivity.getEndTime(), reward.getPayMoney());

		}
		return paymentLogs;
	}

	@Override
	public CommonDropBO receiveOncePayReward(String userId, int rid) {
		// 获取活动
		SystemActivity systemActivity = systemActivityDao.get(ActivityId.ONCE_PAY_REWARD_ID);
		// 判断是否活动时间
		checkActivityIsOpen(systemActivity);
		// 获取领取的奖励信息
		SystemOncePayReward reward = systemOncePayRewardDao.getById(rid);

		SystemOncePayReward nextReward = systemOncePayRewardDao.getNextById(rid);

		// 判断用户是否存在单笔充值满足的订单
		// 从数据库中取出
		List<PaymentLog> paymentLogs = getPaymentLogsByTimeAndMoney(userId, systemActivity, reward, nextReward);

		// 取出已经领取过的订单ID
		List<UserPayReward> receivedOrderList = userPayRewardDao.getReceivedOrderIdList(userId, ActivityId.ONCE_PAY_REWARD_ID, rid, systemActivity.getStartTime(),
				systemActivity.getEndTime());

		// 判断是否达到领取次数上限
		if (receivedOrderList != null) {
			checkReceivedPayRewardLimit(reward, receivedOrderList);
		} else {
			receivedOrderList = new ArrayList<UserPayReward>();
		}
		// 取出一个符合条件的订单，发放奖励并保存记录
		PaymentLog paymentLog = null;
		for (PaymentLog log : paymentLogs) {
			// 如果已领取订单中不包括当前订单号，则表示用户存在满足条件的订单
			if (!contaninOrder(receivedOrderList, log.getOrderId())) {
				paymentLog = log;
				break;
			}
		}

		if (paymentLog == null) {
			throw new ServiceException(ActivityService.PAY_REWARD_MONEY_NOT_ENOUGH, "没有满足条件的充值");
		}

		// 保存记录
		saveUserPayReward(userId, ActivityId.ONCE_PAY_REWARD_ID, rid, paymentLog);

		CommonDropBO commonDropBO = this.toolService.give(userId, reward.getDropToolIds(), ToolUseType.ADD_ONCE_PAY_GIFT_BAG);

		return commonDropBO;

	}

	private boolean contaninOrder(List<UserPayReward> receivedOrderList, String orderId) {
		for (UserPayReward reward : receivedOrderList) {
			if (reward.getOrderId().equals(orderId)) {
				return true;
			}
		}
		return false;
	}

	private void checkReceivedPayRewardLimit(SystemOncePayReward reward, List<UserPayReward> receivedOrderList) {
		if (reward.getTimesLimit() <= receivedOrderList.size()) {
			throw new ServiceException(PAY_REWARD_LIMIT_ERROR, "领取次数超出活动限制");
		}
	}

	private void saveUserPayReward(String userId, int aid, int rid, PaymentLog paymentLog) {
		Date now = new Date();
		UserPayReward userPayReward = new UserPayReward();
		userPayReward.setUserId(userId);
		userPayReward.setActivityId(aid);
		userPayReward.setRewardId(rid);
		userPayReward.setCreatedTime(now);
		userPayReward.setUpdatedTime(now);

		if (paymentLog != null) {
			userPayReward.setOrderId(paymentLog.getOrderId());
		}

		userPayRewardDao.add(userPayReward);
	}

	/**
	 * 检测活动是否开放
	 * 
	 * @param systemActivity
	 */
	private void checkActivityIsOpen(SystemActivity systemActivity) {
		Date now = new Date();
		if (systemActivity == null || now.after(systemActivity.getEndTime()) || now.before(systemActivity.getStartTime())) {
			throw new ServiceException(ACTIVITY_IS_CLOSED, "充值活动已停止");
		}
	}

	@Override
	public CommonDropBO receiveTotalPayReward(String userId, int rid) {
		// 获取活动
		SystemActivity systemActivity = systemActivityDao.get(ActivityId.TOTAL_PAY_REWARD_ID);
		// 判断是否活动时间
		checkActivityIsOpen(systemActivity);
		// 获取领取的奖励信息
		SystemTotalPayReward reward = systemTotalPayRewardDao.getById(rid);
		// SystemGiftbag systemGiftbag =
		// systemGiftbagDao.getTotalPayReward(ActivityId.TOTAL_PAY_REWARD_ID,
		// rid);
		// 判断充值金额是否足够（默认每种奖励只能领取一次）
		int totalPay = paymentLogDao.getPaymentTotalByTime(userId, systemActivity.getStartTime(), systemActivity.getEndTime());
		if (totalPay < reward.getPayMoney()) {
			throw new ServiceException(COST_REWARD_MONEY_NOT_ENOUGH, "充值金额不足");
		}

		UserPayReward userPayReward = userPayRewardDao.getUserPayReward(userId, ActivityId.TOTAL_PAY_REWARD_ID, rid, systemActivity.getStartTime(), systemActivity.getEndTime());
		if (userPayReward != null) {
			throw new ServiceException(PAY_REWARD_LIMIT_ERROR, "领取次数超出活动限制");
		}

		saveUserPayReward(userId, ActivityId.TOTAL_PAY_REWARD_ID, rid, null);

		CommonDropBO commonDropBO = this.toolService.give(userId, reward.getDropToolIds(), ToolUseType.ADD_TOTAL_PAY_GIFT_BAG);

		return commonDropBO;

	}

	@Override
	public Map<String, Object> toolExchange(String userId, int toolExchangeId, int num) {
		// 检测是否超出兑换次数

		if (!this.isActivityOpen(ActivityId.TOOL_EXCHANGE_ID)) {
			throw new ServiceException(ServiceReturnCode.ACTIVITY_NOT_OPEN_EXCTPION, "活动未开放");
		}

		SystemActivity activity = systemActivityDao.get(ActivityId.TOOL_EXCHANGE_ID);

		int userTimes = toolExchangeDao.getUserTimes(userId, toolExchangeId, activity.getStartTime(), activity.getEndTime());
		int times = toolExchangeDao.getTimes(toolExchangeId);
		if (userTimes >= times) {
			throw new ServiceException(REACH_TOOL_EXCHANGE_LIMIT, "达到兑换次数上限");
		}

		Map<String, Object> rtMap = new HashMap<String, Object>();
		ToolExchange toolExchange = toolExchangeDao.getExchangeItems(toolExchangeId);

		List<DropDescBO> preExchangeItems = DropDescHelper.parseDropTool(toolExchange.getPreExchangeItems());
		List<DropDescBO> postExchangeItems = DropDescHelper.parseDropTool(toolExchange.getPostExchangeItems());

		ToolExchangeLooseBO looseBO = createToolExchangeLooseBO(userId, preExchangeItems, num);
		reduceToolExchange(userId, looseBO);
		saveExchangeCount(userId, toolExchangeId);
		ToolExchangeReceiveBO receiveBO = createToolExchangeReceiveBO(userId, postExchangeItems, num);

		rtMap.put("tr", receiveBO);
		rtMap.put("te", looseBO);

		return rtMap;
	}

	@Override
	public List<ToolExchangeCountBO> toolExchangeCount(String userId) {
		SystemActivity activity = systemActivityDao.get(ActivityId.TOOL_EXCHANGE_ID);
		List<ToolExchangeCountBO> boList = new ArrayList<ToolExchangeCountBO>();
		int num = toolExchangeDao.getNum();
		for (int i = 1; i <= num; i++) {
			ToolExchangeCountBO countBO = new ToolExchangeCountBO();
			countBO.setMax(toolExchangeDao.getTimes(i));
			countBO.setTimes(toolExchangeDao.getUserTimes(userId, i, activity.getStartTime(), activity.getEndTime()));
			countBO.setToolExchangeId(i);
			boList.add(countBO);
		}
		return boList;
	}

	/**
	 * 保存用户的物品兑换信息
	 * 
	 * @param userId
	 * @param toolExchangeId
	 */
	private void saveExchangeCount(String userId, int toolExchangeId) {
		SystemActivity activity = systemActivityDao.get(ActivityId.TOOL_EXCHANGE_ID);
		int userTimes = toolExchangeDao.getUserTimes(userId, toolExchangeId, activity.getStartTime(), activity.getEndTime());
		if (userTimes == 0) {
			UserToolExchangeLog userToolExchangeLog = new UserToolExchangeLog();
			Date now = new Date();
			userToolExchangeLog.setUserId(userId);
			userToolExchangeLog.setExchangeId(toolExchangeId);
			userToolExchangeLog.setTimes(1);
			userToolExchangeLog.setCreatedTime(now);
			userToolExchangeLog.setUpdateTime(now);
			toolExchangeDao.addExchangeCount(userToolExchangeLog);
		}
		toolExchangeDao.updateExchangeCount(userId, toolExchangeId, userTimes + 1);
	}

	/**
	 * 物品兑换-告诉客户端增加了什么物品
	 * 
	 * @param userId
	 * @param postExchangeItems
	 * @param num
	 * @return
	 */
	private ToolExchangeReceiveBO createToolExchangeReceiveBO(String userId, List<DropDescBO> postExchangeItems, int num) {

		ToolExchangeReceiveBO receiveBO = new ToolExchangeReceiveBO();
		User user = userService.get(userId);
		int toolUseType = ToolUseType.ADD_TOOL_EXCHANGE;

		for (DropDescBO postExchangeItem : postExchangeItems) {
			int toolType = postExchangeItem.getToolType();
			int toolId = postExchangeItem.getToolId();
			int toolNum = postExchangeItem.getToolNum();
			int exchangeNum = toolNum * num; // 兑换后得到的物品数量

			switch (toolType) {
			case ToolType.GOLD:
				userService.addGold(userId, exchangeNum, toolUseType, user.getLevel());
				receiveBO.setGold(exchangeNum);
				break;
			case ToolType.COPPER:
				userService.addCopper(userId, exchangeNum, toolUseType);
				receiveBO.setCopper(exchangeNum);
				break;
			case ToolType.POWER:
				userService.addPower(userId, exchangeNum, toolUseType, null);
				receiveBO.setPower(exchangeNum);
				break;
			case ToolType.EXP:
				userService.addExp(userId, exchangeNum, toolUseType);
				receiveBO.setExp(exchangeNum);
				break;
			case ToolType.HERO_BAG:
				userExtinfoDao.updateHeroMax(userId, exchangeNum);
				receiveBO.setHeroBag(exchangeNum);
				break;
			case ToolType.EQUIP_BAG:
				userExtinfoDao.updateEquipMax(userId, exchangeNum);
				receiveBO.setEquipBag(exchangeNum);
				break;
			case ToolType.HERO:
				List<DropToolBO> heroDropList = toolService.giveTools(userId, ToolType.HERO, toolId, exchangeNum, toolUseType);
				List<UserHeroBO> useHeroBoList = heroService.createUserHeroBOList(userId, heroDropList);
				receiveBO.setUserHeroBOList(useHeroBoList);
				break;
			case ToolType.EQUIP:
				List<DropToolBO> equipDropList = toolService.giveTools(userId, ToolType.EQUIP, toolId, exchangeNum, toolUseType);
				List<UserEquipBO> useEquipBoList = equipService.createUserEquipBOList(userId, equipDropList);
				receiveBO.setUserEquipBOList(useEquipBoList);
				break;
			case ToolType.MATERIAL:
			case ToolType.GIFT_BOX:
			case ToolType.FRAGMENT:
			case ToolType.SKILL_BOOK:
				toolService.giveTools(userId, toolType, toolId, exchangeNum, toolUseType);
				UserToolBO toolBo = new UserToolBO();
				toolBo.setToolId(toolId);
				toolBo.setToolNum(exchangeNum);
				receiveBO.getUserToolBOList().add(toolBo);
				break;
			default:
				break;
			}
		}
		return receiveBO;
	}

	/**
	 * 物品兑换-为用户删除要兑换的物品
	 */
	private void reduceToolExchange(String userId, ToolExchangeLooseBO looseBO) {

		int toolUseType = ToolUseType.REDUCE_TOOL_EXCHANGE;

		if (looseBO.getGold() != 0) {
			userService.reduceGold(userId, looseBO.getGold(), toolUseType);
		}
		if (looseBO.getCopper() != 0) {
			userService.reduceCopper(userId, looseBO.getCopper(), toolUseType);
		}
		if (looseBO.getPower() != 0) {
			userService.reducePower(userId, looseBO.getPower(), toolUseType);
		}
		if (looseBO.getExp() != 0) {
			userService.reduceExp(userId, looseBO.getExp(), toolUseType);
		}
		if (looseBO.getHeroBag() != 0) {
			userExtinfoDao.updateHeroMax(userId, -1 * looseBO.getHeroBag());
		}
		if (looseBO.getEquipBag() != 0) {
			userExtinfoDao.updateEquipMax(userId, -1 * looseBO.getEquipBag());
		}
		if (looseBO.getHeroBOList().size() != 0) {
			List<ToolExchangeHeroBO> heroBoList = looseBO.getHeroBOList();
			for (ToolExchangeHeroBO heroBo : heroBoList) {
				String userHeroId = heroBo.getUserHeroId();
				userHeroDao.delete(userId, userHeroId);
			}
		}
		if (looseBO.getEquipBOList().size() != 0) {
			List<ToolExchangeEquipBO> equipBoList = looseBO.getEquipBOList();
			for (ToolExchangeEquipBO equipBO : equipBoList) {
				userEquipDao.delete(userId, equipBO.getUserEquipId());
			}
		}
		if (looseBO.getToolBOList().size() != 0) {
			List<ToolExchangeToolBO> toolBoList = looseBO.getToolBOList();
			for (ToolExchangeToolBO toolBo : toolBoList) {
				userToolDao.reduceUserTool(userId, toolBo.getToolId(), toolBo.getExchangeNum());
			}
		}
	}

	/**
	 * 物品兑换-告诉客户端丢失了什么物品
	 * 
	 * @param userId
	 * @param exchangeItems
	 * @param num
	 * @return
	 */
	private ToolExchangeLooseBO createToolExchangeLooseBO(String userId, List<DropDescBO> preExchangeItems, int num) {

		ToolExchangeLooseBO looseBO = new ToolExchangeLooseBO();
		User user = userService.get(userId);

		for (DropDescBO preExchangeItem : preExchangeItems) {
			int toolType = preExchangeItem.getToolType();
			int toolId = preExchangeItem.getToolId();
			int toolNum = preExchangeItem.getToolNum();
			int exchangeNum = toolNum * num; // 兑换需要的物品数量

			switch (toolType) {
			case ToolType.GOLD:
				long goldNum = user.getGoldNum();
				if (goldNum < exchangeNum) {
					String message = "用户金币不足.userId[" + userId + "], gold_num[" + goldNum + "]";
					throw new ServiceException(TOOL_NOT_ENOUGH, message);
				} else {
					looseBO.setGold(exchangeNum); // 失去的金币数量
				}
				break;
			case ToolType.COPPER:
				long cooperNum = user.getCopper();
				if (cooperNum < exchangeNum) {
					String message = "用户银币不足.userId[" + userId + "], coopper[" + cooperNum + "]";
					throw new ServiceException(TOOL_NOT_ENOUGH, message);
				} else {
					looseBO.setCopper(exchangeNum); // 失去的银币数量
				}
				break;
			case ToolType.POWER:
				int power = user.getPower();
				if (power < exchangeNum) {
					String message = "用户体力不足.userId[" + userId + "], power[" + power + "]";
					throw new ServiceException(TOOL_NOT_ENOUGH, message);
				} else {
					looseBO.setPower(exchangeNum); // 失去的体力
				}
				break;
			case ToolType.EXP:
				long exp = user.getExp();
				if (exp < exchangeNum) {
					String message = "用户经验值不足.userId[" + userId + "], exp[" + exp + "]";
					throw new ServiceException(TOOL_NOT_ENOUGH, message);
				} else {
					looseBO.setExp(exchangeNum); // 失去的经验
				}
				break;
			case ToolType.HERO_BAG:
				// 查询武将背包上限
				int heroMax = userExtinfoDao.get(userId).getHeroMax();
				if (heroMax < exchangeNum) {
					String message = "武将背包空间不足.userId[" + userId + "], heroMax[" + heroMax + "]";
					throw new ServiceException(TOOL_NOT_ENOUGH, message);
				} else {
					looseBO.setHeroBag(exchangeNum);
				}
				break;
			case ToolType.EQUIP_BAG:
				// 查询装备背包
				int equipMax = userExtinfoDao.get(userId).getEquipMax();
				if (equipMax < exchangeNum) {
					String message = "装备背包不足.userId[" + userId + "], equipMax[" + equipMax + "]";
					throw new ServiceException(TOOL_NOT_ENOUGH, message);
				} else {
					looseBO.setEquipBag(exchangeNum);
				}
				break;
			case ToolType.HERO:
				looseBO.getHeroBOList().addAll(exchangeHero(userId, toolId, exchangeNum));
				break;
			case ToolType.EQUIP:
				looseBO.getEquipBOList().addAll(exchangeEquip(userId, toolId, exchangeNum));
				break;
			case ToolType.MATERIAL:
			case ToolType.GIFT_BOX:
			case ToolType.FRAGMENT:
			case ToolType.SKILL_BOOK:
				int userToolNum = userToolDao.getUserToolNum(userId, toolId);
				if (userToolNum < exchangeNum) {
					String message = "道具数量不够.userId[" + userId + "], equipId[" + toolId + "]";
					throw new ServiceException(TOOL_NOT_ENOUGH, message);
				} else {
					ToolExchangeToolBO toolBo = new ToolExchangeToolBO();
					toolBo.setToolId(toolId);
					toolBo.setExchangeNum(exchangeNum);
					looseBO.getToolBOList().add(toolBo);
				}
				break;
			default:
				break;
			}
		}
		return looseBO;
	}

	private List<ToolExchangeHeroBO> exchangeHero(String userId, int toolId, int exchangeNum) {
		// 将用户中相同 system_hero_id 的武将按照 hero_level 从小大排列
		List<UserHero> userHeroList = userHeroDao.listUserHeroByLevelAsc(userId, toolId);
		List<ToolExchangeHeroBO> heroBoList = new ArrayList<ToolExchangeHeroBO>();

		int userHeroNum = userHeroList.size();

		// 检查这种类型武将数量是否足够
		if (userHeroNum < exchangeNum) {
			String message = "武将数量不够.userId[" + userId + "], systemHeroId[" + toolId + "]";
			throw new ServiceException(TOOL_NOT_ENOUGH, message);
		} else {
			for (int i = 0; i < exchangeNum; i++) {
				UserHero userHero = userHeroList.get(i);
				if (userHero.getLockStatus() < 1) {
					ToolExchangeHeroBO heroBO = new ToolExchangeHeroBO();
					heroBO.setUserHeroId(userHero.getUserHeroId());
					heroBoList.add(heroBO);
				}
			}

			if (heroBoList.size() < exchangeNum) {
				String message = "该类武将有被锁定的,导致数量不足.userId[" + userId + "], systemHeroId[" + toolId + "]";
				throw new ServiceException(HERO_HAS_LOCKED, message);
			}
			return heroBoList;
		}
	}

	private List<ToolExchangeEquipBO> exchangeEquip(String userId, int toolId, int exchangeNum) {
		List<UserEquip> userEquipList = userEquipDao.listUserEquipByLevelAsc(userId, toolId);
		List<ToolExchangeEquipBO> equipBoList = new ArrayList<ToolExchangeEquipBO>();
		int userEquipNum = userEquipList.size();

		if (userEquipNum < exchangeNum) {
			String message = "装备数量不够.userId[" + userId + "], equipId[" + toolId + "]";
			throw new ServiceException(TOOL_NOT_ENOUGH, message);
		} else {
			for (int i = 0; i < exchangeNum; i++) {
				UserEquip userEquip = userEquipList.get(i);
				ToolExchangeEquipBO equipBo = new ToolExchangeEquipBO();
				equipBo.setUserEquipId(userEquip.getUserEquipId());
				equipBoList.add(equipBo);
			}

			return equipBoList;
		}
	}

	@Override
	public List<SystemActivityBO> getDisplayActivityBOList(String uid) {

		List<SystemActivity> activityList = this.systemActivityDao.getDisplayActivityLiset();
		List<SystemActivityBO> systemActivityBOList = new ArrayList<SystemActivityBO>();

		Date now = new Date();

		for (SystemActivity systemActivity : activityList) {

			if (now.before(systemActivity.getStartTime()) || now.after(systemActivity.getEndTime())) {
				continue;
			}

			SystemActivityBO systemActivityBO = new SystemActivityBO();
			systemActivityBO.setDesc(systemActivity.getActivityDesc());
			systemActivityBO.setTitle(systemActivity.getActivityName());
			systemActivityBO.setType(systemActivity.getActivityType());
			systemActivityBO.setTips(systemActivity.getActivityTips());
			systemActivityBO.setEndTime(systemActivity.getEndTime().getTime());
			systemActivityBO.setDate(ActivityHelper.getActivityTimeDesc(systemActivity.getStartTime(), systemActivity.getEndTime()));

			systemActivityBOList.add(systemActivityBO);
		}

		return systemActivityBOList;
	}

	@Override
	public boolean checkOncePayReward(String userId) {
		boolean flg = false;

		// 获得活动期间用户的订单以及已经领取过的订单记录
		SystemActivity systemActivity = systemActivityDao.get(ActivityId.ONCE_PAY_REWARD_ID);
		try {
			checkActivityIsOpen(systemActivity);
		} catch (ServiceException e) {
			return flg;
		}

		List<SystemOncePayReward> systemOncePayRewards = systemOncePayRewardDao.getAll();

		for (SystemOncePayReward reward : systemOncePayRewards) {

			SystemOncePayReward nextReward = systemOncePayRewardDao.getNextById(reward.getId());
			List<PaymentLog> paymentLogs = getPaymentLogsByTimeAndMoney(userId, systemActivity, reward, nextReward);

			// 取出已经领取过的订单ID
			List<UserPayReward> receivedOrderList = userPayRewardDao.getReceivedOrderIdList(userId, ActivityId.ONCE_PAY_REWARD_ID, reward.getId(), systemActivity.getStartTime(),
					systemActivity.getEndTime());

			// 如果领取次数超出限制
			if (receivedOrderList != null && reward.getTimesLimit() <= receivedOrderList.size()) {
				flg = false;
				continue;
			}

			for (PaymentLog log : paymentLogs) {
				// 如果已领取订单中不包括当前订单号，则表示用户存在满足条件的订单
				if (!contaninOrder(receivedOrderList, log.getOrderId())) {
					flg = true;
					return flg;
				}
			}
		}
		return flg;
	}

	@Override
	public boolean checkTotalPayReward(String userId) {
		boolean flg = false;

		SystemActivity systemActivity = systemActivityDao.get(ActivityId.TOTAL_PAY_REWARD_ID);
		try {
			checkActivityIsOpen(systemActivity);
		} catch (ServiceException e) {
			return flg;
		}

		// 计算在活动期间，用户总的充值金额
		int userTotalPay = paymentLogDao.getPaymentTotalByTime(userId, systemActivity.getStartTime(), systemActivity.getEndTime());
		// 查询出所有小于用户充值总金额的奖励
		List<SystemTotalPayReward> systemTotalPayRewards = systemTotalPayRewardDao.getByPayment(userTotalPay);

		for (SystemTotalPayReward reward : systemTotalPayRewards) {
			int rid = reward.getId();
			UserPayReward userpayrePayReward = userPayRewardDao.getUserPayReward(userId, ActivityId.TOTAL_PAY_REWARD_ID, rid, systemActivity.getStartTime(),
					systemActivity.getEndTime());
			if (userpayrePayReward == null) {
				flg = true;
				break;
			}
		}
		return flg;
	}

	@Override
	public List<DropToolBO> activityDraw(String userId, int num) {

		return null;
	}

	@Override
	public boolean isForcesActivityOpen(int sceneId) {

		List<SystemActivity> activityList = this.systemActivityDao.getList(ActivityType.ACTIVITY_TYPE_COPY);
		Date now = new Date();
		for (SystemActivity activity : activityList) {
			String param = activity.getParam();
			int checkSceneId = NumberUtils.toInt(param, 0);
			if (sceneId == checkSceneId) {
				if (activity.getStartTime().after(now) || activity.getEndTime().before(now)) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * 系统活动是否开放
	 * 
	 * @param activityId
	 *            系统活动 id
	 * @return true 活动开放 false 活动不开放
	 */
	public boolean isActivityOpen(int activityId) {

		Date now = new Date();

		SystemActivity systemActivity = this.systemActivityDao.get(activityId);
		if (systemActivity != null) {

			if (systemActivity.getStartTime().after(now) || systemActivity.getEndTime().before(now)) {
				return false;
			}

			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<ToolExchangeBO> getToolExchangeBoList() {

		List<ToolExchangeBO> list = new ArrayList<ToolExchangeBO>();

		List<ToolExchange> toolExchangeList = this.toolExchangeDao.getAll();
		for (ToolExchange toolExchange : toolExchangeList) {
			ToolExchangeBO toolExchangeBO = new ToolExchangeBO();

			toolExchangeBO.setId(toolExchange.getExchangeId());
			toolExchangeBO.setTimes(toolExchange.getTimes());
			toolExchangeBO.setPreDropToolBOList(DropDescHelper.parseDropTool(toolExchange.getPreExchangeItems()));
			toolExchangeBO.setPostDropToolBOList(DropDescHelper.parseDropTool(toolExchange.getPostExchangeItems()));

			list.add(toolExchangeBO);
		}

		return list;

	}

	@Override
	public int checkActivityIsOpenAdd(SystemActivity systemActivity) {
		Date now = new Date();
		if (systemActivity == null || now.after(systemActivity.getEndTime()) || now.before(systemActivity.getStartTime())) {
			return 0;
		} else {
			return 1;
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean dataSync(String table, String sqls) {

		String reloadClass = null;

		if (table.equalsIgnoreCase("system_tool_exchange")) {
			// DO NOT THING
		} else if (table.equalsIgnoreCase("system_once_pay_reward")) {
			reloadClass = "SystemOncePayRewardDaoCacheImpl";
		} else if (table.equalsIgnoreCase("system_total_pay_reward")) {
			// DO NOT THING
		} else if (table.equalsIgnoreCase("system_mall_discount_activity")) {
			// DO NOT THING
		} else if (table.equalsIgnoreCase("system_mall_discount_items")) {
			reloadClass = "SystemMallDiscountDaoCacheImpl";
		} else if (table.equalsIgnoreCase("system_reduce_rebate")) {
			reloadClass = "SystemReduceRebateDaoCacheImpl";
		} else if (table.equalsIgnoreCase("system_tavern_rebate")) {
			reloadClass = "SystemTavernRebateDaoCacheImpl";
		} else if (table.equalsIgnoreCase("system_online_reward")) {
			// DO NOT THING
		} else if (table.equalsIgnoreCase("gift_drop")) {
			reloadClass = "GiftDropDaoCacheImpl";
		} else if (table.equalsIgnoreCase("system_activity")) {
			reloadClass = "SystemActivityDaoCacheImpl";
		} else if (table.equalsIgnoreCase("system_draw")) {
			reloadClass = "SystemDrawDaoCacheImpl";
		} else if (table.equalsIgnoreCase("system_draw_detail")) {
			reloadClass = "SystemDrawDetailDaoCacheImpl";
		} else if (table.equalsIgnoreCase("system_cost_reward")) {
			reloadClass = "SystemCostRewardDaoCacheImpl";
		} else {
			return false;
		}

		List<Map<Object, Object>> list = Json.toObject(sqls, List.class);
		for (Map<Object, Object> map : list) {
			String s = map.get("sql").toString();
			if (s.indexOf("$table") <= 0) {
				return false;
			}
			String sql = s.replaceAll("\\$table", table);
			if (table.equalsIgnoreCase("gift_drop")) {
				this.systemActivityDao.executeCommon(sql);
			} else {
				this.systemActivityDao.execute(sql);
			}
		}

		if (reloadClass != null) {
			this.commandDao.cacheReload(reloadClass);
		}

		return true;
	}

	@Override
	public CommonDropBO receiveLoginReward(String userId, int loginDay) {

		if (loginDay > LOGIN_REWARD_MAX_DAY || loginDay < LOGIN_REWARD_MIN_DAY) {
			String message = "用户领取30天登入奖励失败,传入的参数不对.userId[" + userId + "], 第[" + loginDay + "] 天";
			throw new ServiceException(RECIVE_LOGIN_REWARD_HAS_WRONG_PARAM, message);
		}

		UserLoginRewardInfo userLoginRewardInfo = this.userLoginRewardInfoDao.getUserLoginRewardInfoByDay(userId, loginDay);

		if (userLoginRewardInfo == null) {
			String message = "用户领取30天登入奖励失败,没有达到领取条件.userId[" + userId + "], 第[" + loginDay + "] 天";
			throw new ServiceException(RECIVE_NO_LOGIN_INFO, message);
		}

		// 已领取
		if (userLoginRewardInfo.getRewardStatus() != LOGIN_REWARD_NOT_GET) {
			String message = "用户领取30天登入奖励失败,该天的礼包已领取.userId[" + userId + "], 第[" + loginDay + "] 天";
			throw new ServiceException(RECIVE_LOGIN_REWARD_HAS_RECEIVE, message);
		}
		// 更改领取日志
		int rewardStatus = LOGIN_REWARD_HAS_GET; // 设置为已领取
		this.userLoginRewardInfoDao.updateUserLoginRewardInfoByDay(userId, loginDay, DateUtils.getDate(), rewardStatus);

		SystemLoginReward systemLoginReward = this.systemLoginRewardDao.getSystemLoginRewardByDay(loginDay);
		if (systemLoginReward == null) {
			String message = "获取奖励信息出错.userId[" + userId + "], day[" + loginDay + "]";
			throw new ServiceException(GET_SYSTEM_LOGIN_REWARD_INFO_HAS_WRONG, message);
		}

		List<DropDescBO> dropToolBOList = DropDescHelper.parseDropTool(systemLoginReward.getToolIds());

		CommonDropBO commonDropBO = new CommonDropBO();
		for (DropDescBO dropToolBO : dropToolBOList) {
			int toolType = dropToolBO.getToolType();
			int toolId = dropToolBO.getToolId();
			int toolNum = dropToolBO.getToolNum();

			List<DropToolBO> dropBOList = toolService.giveTools(userId, toolType, toolId, toolNum, ToolUseType.ADD_30_LOGIN_REWARD);

			for (DropToolBO dropBO : dropBOList) {
				this.toolService.appendToDropBO(userId, commonDropBO, dropBO);
			}
		}

		return commonDropBO;

	}

	@Override
	public int checkLoginRewardHasGiven(String userID) {

		int gulri = LOGIN_REWARD_HAS_NOT_GIVEN; // 默认没有可领取的

		List<UserLoginRewardInfoBO> userLoginRewardInfoBOList = this.getUserLoginRewardInfo(userID);

		for (UserLoginRewardInfoBO userLoginRewardInfoBO : userLoginRewardInfoBOList) {
			// 有可领取的
			if (userLoginRewardInfoBO.getRewardStatus() == LOGIN_REWARD_NOT_GET) {
				gulri = LOGIN_REWARD_NOT_GIVEN;
				break;
			}
			// 三十天都领完了
			if (userLoginRewardInfoBO.getDay() == LOGIN_REWARD_MAX_DAY) {
				gulri = LOGIN_REWARD_BEYOND_MAX_DAY;
				break;
			}
		}
		return gulri;
	}

	@Override
	public List<UserLoginRewardInfoBO> getUserLoginRewardInfo(String userId) {

		List<UserLoginRewardInfoBO> UserLoginRewardInfoBOList = new ArrayList<UserLoginRewardInfoBO>();
		List<UserLoginRewardInfo> userLoginRewardInfoList = this.userLoginRewardInfoDao.getUserLoginRewardInfo(userId);

		for (UserLoginRewardInfo userLoginRewardInfo : userLoginRewardInfoList) {
			SystemLoginReward systemLoginReward = this.systemLoginRewardDao.getSystemLoginRewardByDay(userLoginRewardInfo.getDay());
			UserLoginRewardInfoBO userLoginRewardInfoBO = new UserLoginRewardInfoBO();
			userLoginRewardInfoBO.setDay(userLoginRewardInfo.getDay());
			userLoginRewardInfoBO.setRewardStatus(userLoginRewardInfo.getRewardStatus());
			userLoginRewardInfoBO.setDropToolBOList(DropDescHelper.parseDropTool(systemLoginReward.getToolIds()));
			UserLoginRewardInfoBOList.add(userLoginRewardInfoBO);
		}

		Collection<SystemLoginReward> systemLoginRewardList = this.systemLoginRewardDao.getList();
		for (int i = userLoginRewardInfoList.size(); i < systemLoginRewardList.size(); i++) {
			SystemLoginReward systemLoginReward = this.systemLoginRewardDao.getSystemLoginRewardByDay(i + 1);
			UserLoginRewardInfoBO userLoginRewardInfoBO = new UserLoginRewardInfoBO();
			userLoginRewardInfoBO.setDay(i + 1);
			userLoginRewardInfoBO.setRewardStatus(0);
			userLoginRewardInfoBO.setDropToolBOList(DropDescHelper.parseDropTool(systemLoginReward.getToolIds()));
			UserLoginRewardInfoBOList.add(userLoginRewardInfoBO);
		}

		return UserLoginRewardInfoBOList;

	}

	@Override
	public void checkUserLoginRewardInfo(String userId) {

		// 获取最后一次登录信息
		UserLoginRewardInfo lastUserLoginRewardInfo = this.userLoginRewardInfoDao.getUserLastLoginRewardInfo(userId);

		// 第一次登录
		if (lastUserLoginRewardInfo == null) {
			UserLoginRewardInfo newUserLoginRewardInfo = new UserLoginRewardInfo();
			newUserLoginRewardInfo.setCreatedTime(new Date());
			newUserLoginRewardInfo.setRewardStatus(LOGIN_REWARD_NOT_GET);
			newUserLoginRewardInfo.setUserId(userId);
			newUserLoginRewardInfo.setDay(LOGIN_REWARD_MIN_DAY);
			this.userLoginRewardInfoDao.addUserLoginRewardInfo(newUserLoginRewardInfo);
		} else if (DateUtils.str2Date(DateUtils.getDate(), "yyyy-MM-dd").after(lastUserLoginRewardInfo.getCreatedTime()) && lastUserLoginRewardInfo.getDay() < LOGIN_REWARD_MAX_DAY) {// 当天还没有加入数据库
			int day = lastUserLoginRewardInfo.getDay() + 1;
			if (null == userLoginRewardInfoDao.getUserLoginRewardInfoByDay(userId, day)) {// 容错
				UserLoginRewardInfo newUserLoginRewardInfo = new UserLoginRewardInfo();
				newUserLoginRewardInfo.setCreatedTime(new Date());
				newUserLoginRewardInfo.setRewardStatus(LOGIN_REWARD_NOT_GET);
				newUserLoginRewardInfo.setUserId(userId);
				newUserLoginRewardInfo.setDay(day);
				this.userLoginRewardInfoDao.addUserLoginRewardInfo(newUserLoginRewardInfo);
			}
		}
	}

	private List<HeroStoneMallBo> getUserHeroStoneMallBos(UserHeroStoneMallLog userHeroStoneMallLog, String userId) {

		List<HeroStoneMallBo> result = new ArrayList<HeroStoneMallBo>();

		int toolNum = this.userToolDao.getUserToolNum(userId, ToolId.REFRESH_NUM);

		if (toolNum == 0) {
			int times = this.userDailyGainLogDao.getUserDailyGain(userId, UserDailyGainLogType.REFRESH_STONE_MALL);
			int needMoney = MallHelper.getRefreshNeedMoney(times + 1);
			if (!userService.reduceGold(userId, needMoney, ToolUseType.STONE_MALL_EXCHANGE)) {
				throw new ServiceException(GOLD_NOT_ENOUGH, "元宝不足!");
			}
			userDailyGainLogDao.addUserDailyGain(userId, UserDailyGainLogType.REFRESH_STONE_MALL, 1);
		} else {
			if (!toolService.reduceTool(userId, ToolType.MATERIAL, ToolId.REFRESH_NUM, 1, ToolUseType.STONE_MALL_EXCHANGE)) {
				throw new ServiceException(REFRESH_NUM_NOT_ENOUGH, "刷新令不足!");
			}
		}
		if (stoneMap.containsKey(userId)) {
			stoneMap.remove(userId);
		}

		// TODO
		List<HeroStoneMallBo> list = getHeroStoneMallBos(userHeroStoneMallLog, userId);
		for (HeroStoneMallBo bo : list) {
			bo.setIsExChange(0);
		}
		if (null == userHeroStoneMallLog) {
			userHeroStoneMallLog = new UserHeroStoneMallLog();
			userHeroStoneMallLog.setCreatedTime(new Date());
		}
		userHeroStoneMallLog.setExchangeIds("");
		userHeroStoneMallLog.setSystemIds(getSystemIds(list));
		userHeroStoneMallLog.setUserId(userId);
		userHeroStoneMallLog.setUpdatedTime(new Date());
		this.systemHeroStoneMallDao.updateUserStoneMallLog(userHeroStoneMallLog);
		result = list;
		return result;
	}

	private List<HeroStoneMallBo> getSystemHeroStoneMallsBySystemIds(UserHeroStoneMallLog userHeroStoneMallLog, String ids, String userId) {
		List<HeroStoneMallBo> result = new ArrayList<HeroStoneMallBo>();
		List<SystemHeroStoneMall> systemHeroStoneMalls = this.systemHeroStoneMallDao.getListBySystemIds(ids);
		for (SystemHeroStoneMall stoneMall : systemHeroStoneMalls) {
			HeroStoneMallBo heroStoneMallBo = getHeroStoneMallBo(userHeroStoneMallLog, stoneMall, userId);
			result.add(heroStoneMallBo);
		}
		return result;
	}

	@Override
	public Map<String, Object> refreshStoneMall(String userId, int type) {

		Map<String, Object> map = new HashMap<String, Object>();
		List<HeroStoneMallBo> result = new ArrayList<HeroStoneMallBo>();
		UserHeroStoneMallLog userHeroStoneMallLog = this.systemHeroStoneMallDao.getUserStoneMallLog(userId);
		StoneMallTimeLog stoneMallTimeLog = this.systemHeroStoneMallDao.getMallTimeLog();

		// 直接刷新用户的兑换列表
		if (type == 2) {
			result = getUserHeroStoneMallBos(userHeroStoneMallLog, userId);
		} else {
			if (null == stoneMallTimeLog || new Date().after(DateUtils.add(stoneMallTimeLog.getUpdatedTime(), Calendar.HOUR, 2))) {
				result = systemRefresh(stoneMallTimeLog, userHeroStoneMallLog, userId);
				if (null == stoneMallTimeLog) {
					stoneMallTimeLog = this.systemHeroStoneMallDao.getMallTimeLog();
				}
			} else {
				result = getSystemHeroStoneMallsBySystemIds(userHeroStoneMallLog, stoneMallTimeLog.getSystemIds(), userId);
			}
			Date date1 = DateUtils.add(stoneMallTimeLog.getUpdatedTime(), Calendar.HOUR, 2);// 系统下次刷新时间
			Date date2 = stoneMallTimeLog.getUpdatedTime();// 系统刷新时间
			if (null != userHeroStoneMallLog && userHeroStoneMallLog.getUpdatedTime().before(date1) && userHeroStoneMallLog.getUpdatedTime().after(date2)) {
				result = getSystemHeroStoneMallsBySystemIds(userHeroStoneMallLog, userHeroStoneMallLog.getSystemIds(), userId);
			}
		}

		StoneMallTimeLog currStoneMallTimeLog = this.systemHeroStoneMallDao.getMallTimeLog();
		long time = DateUtils.add(currStoneMallTimeLog.getUpdatedTime(), Calendar.HOUR, 2).getTime();

		int times = this.userDailyGainLogDao.getUserDailyGain(userId, UserDailyGainLogType.REFRESH_STONE_MALL);
		int needMoney = MallHelper.getRefreshNeedMoney(times + 1);

		map.put("ngd", needMoney);
		map.put("rct", time);
		map.put("nt", new Date().getTime());
		map.put("mls", sort(result));
		return map;
	}

	private List<HeroStoneMallBo> systemRefresh(StoneMallTimeLog stoneMallTimeLog, UserHeroStoneMallLog userHeroStoneMallLog, String userId) {
		List<HeroStoneMallBo> list = getHeroStoneMallBos(userHeroStoneMallLog, userId);
		if (null == stoneMallTimeLog) {
			stoneMallTimeLog = new StoneMallTimeLog();
			stoneMallTimeLog.setCreatedTime(new Date());
		}
		int hour = DateUtils.getHour();
		String date1 = DateUtils.getDate() + " " + (hour % 2 == 0 ? hour : (hour - 1)) + ":00:00";
		stoneMallTimeLog.setUpdatedTime(DateUtils.str2Date(date1));
		stoneMallTimeLog.setSystemIds(getSystemIds(list));
		if (stoneMap.containsKey(userId)) {
			stoneMap.remove(userId);
		}
		this.systemHeroStoneMallDao.updateMallTimesLog(stoneMallTimeLog);
		this.systemHeroStoneMallDao.clearSystemIds();
		return list;
	}

	private List<HeroStoneMallBo> sort(List<HeroStoneMallBo> list) {

		Comparator<HeroStoneMallBo> comparator = new Comparator<HeroStoneMallBo>() {
			@Override
			public int compare(HeroStoneMallBo o1, HeroStoneMallBo o2) {
				return o1.getSystemId() - o2.getSystemId();
			}
		};
		Collections.sort(list, comparator);
		return list;
	}

	@Override
	public CommonDropBO exchageStoneTool(String userId, int systemId) {

		StoneMallTimeLog stoneMallTimeLog = this.systemHeroStoneMallDao.getMallTimeLog();
		UserHeroStoneMallLog userHeroStoneMallLog = this.systemHeroStoneMallDao.getUserStoneMallLog(userId);
		SystemHeroStoneMall systemHeroStoneMall = this.systemHeroStoneMallDao.get(systemId);

		if (isExchangeTool(userHeroStoneMallLog, userId, systemId)) {
			throw new ServiceException(EXCHANGE_TOOL_EXIST, "在本次刷新时间内已经兑换过该道具");
		}

		if (null == systemHeroStoneMall) {
			throw new ServiceException(EXCHANGE_TOOL_NOT_EXIST, "兑换的道具不存在!");
		}

		int needMoney = systemHeroStoneMall.getNeedMoney();
		if (isActivityOpen(ActivityId.MOON_DAY)) {
			needMoney = Math.round(needMoney * 0.8f);
		}

		if (!toolService.reduceTool(userId, ToolType.MATERIAL, ToolId.JIANGSHAN_ORDER, needMoney, ToolUseType.STONE_MALL_EXCHANGE)) {
			throw new ServiceException(EXCHANGE_TOOL_NOT_ENOUGH, "江山令数量不足!");
		}

		int toolId = systemHeroStoneMall.getToolId();
		int toolType = systemHeroStoneMall.getToolType();
		int toolNum = systemHeroStoneMall.getToolNum();
		DropDescBO dropDescBO = new DropDescBO(toolType, toolId, toolNum);
		CommonDropBO commonDropBO = toolService.give(userId, dropDescBO, ToolUseType.ADD_HERO_SOUL_MALL_EXCHANGE);

		String str = systemId + ",";
		if (userHeroStoneMallLog != null) {
			userHeroStoneMallLog.setExchangeIds(userHeroStoneMallLog.getExchangeIds() + str);
		} else {
			userHeroStoneMallLog = new UserHeroStoneMallLog();
			userHeroStoneMallLog.setUserId(userId);
			userHeroStoneMallLog.setExchangeIds(str);
			userHeroStoneMallLog.setSystemIds(stoneMallTimeLog.getSystemIds());
			userHeroStoneMallLog.setCreatedTime(new Date());
			userHeroStoneMallLog.setUpdatedTime(new Date());
		}

		if (stoneMap.containsKey(userId)) {
			List<Integer> list = stoneMap.get(userId);
			list.add(systemId);
			stoneMap.put(userId, list);
		} else {
			List<Integer> list = new ArrayList<Integer>();
			list.add(systemId);
			stoneMap.put(userId, list);
		}

		this.systemHeroStoneMallDao.updateUserStoneMallLog(userHeroStoneMallLog);

		return commonDropBO;
	}

	private List<HeroStoneMallBo> getHeroStoneMallBos(UserHeroStoneMallLog userHeroStoneMallLog, String userId) {
		List<HeroStoneMallBo> list = new ArrayList<HeroStoneMallBo>();
		int sum = this.systemHeroStoneMallDao.getSumRate();
		List<SystemHeroStoneMall> stoneMalls = new ArrayList<SystemHeroStoneMall>();
		List<SystemHeroStoneMall> tempList = this.systemHeroStoneMallDao.getAllList();
		for (SystemHeroStoneMall stoneMall : tempList) {
			stoneMalls.add(stoneMall);
		}

		// 如果系统表里面的兑换道具项小于等于9直接返回
		if (stoneMalls.size() <= 9) {
			for (SystemHeroStoneMall stoneMall : stoneMalls) {
				list.add(getHeroStoneMallBo(userHeroStoneMallLog, stoneMall, userId));
			}
			return list;
		}
		List<Integer> tempId = new ArrayList<Integer>();
		int count = 0;
		while (list.size() < 9) {
			if (count >= 2500) {// 容错
				break;
			}
			int random = RandomUtils.nextInt(sum) + 1;
			int temp = 0;
			for (int i = 0; i < stoneMalls.size(); i++) {
				SystemHeroStoneMall stoneMall = stoneMalls.get(i);
				if (tempId.contains(stoneMall.getSystemId())) {
					continue;
				}
				temp += stoneMall.getRate();
				if (random <= temp) {
					list.add(getHeroStoneMallBo(userHeroStoneMallLog, stoneMall, userId));
					tempId.add(stoneMall.getSystemId());
					break;
				}
			}
			count++;
		}
		return list;
	}

	private HeroStoneMallBo getHeroStoneMallBo(UserHeroStoneMallLog userHeroStoneMallLog, SystemHeroStoneMall stoneMall, String userId) {

		HeroStoneMallBo heroStoneMallBo = new HeroStoneMallBo();

		heroStoneMallBo.setIsExChange(isExchangeTool(userHeroStoneMallLog, userId, stoneMall.getSystemId()) ? 1 : 0);
		heroStoneMallBo.setNeedMoney(stoneMall.getNeedMoney());
		heroStoneMallBo.setSystemId(stoneMall.getSystemId());
		heroStoneMallBo.setToolId(stoneMall.getToolId());
		heroStoneMallBo.setToolName(stoneMall.getToolName());
		heroStoneMallBo.setToolNum(stoneMall.getToolNum());
		heroStoneMallBo.setToolType(stoneMall.getToolType());

		return heroStoneMallBo;
	}

	/**
	 * 是否已经兑换过该物品
	 * 
	 * @param userId
	 * @param systemId
	 * @return
	 */
	private boolean isExchangeTool(UserHeroStoneMallLog userHeroStoneMallLog, String userId, int systemId) {

		if (!stoneMap.isEmpty() && stoneMap.containsKey(userId)) {
			List<Integer> userExChangeIds = stoneMap.get(userId);
			if (userExChangeIds.contains(systemId)) {
				return true;
			} else {
				return false;
			}
		} else {
			if (userHeroStoneMallLog != null && !StringUtils.isEmpty(userHeroStoneMallLog.getExchangeIds())) {
				String[] systemIds = userHeroStoneMallLog.getExchangeIds().split(",");
				for (String tempId : systemIds) {
					if (tempId.equals(systemId + "")) {
						return true;
					}
				}
			} else {
				return false;
			}
		}
		return false;
	}

	private String getSystemIds(List<HeroStoneMallBo> list) {
		String systemIds = "";
		for (int i = 0; i < list.size(); i++) {
			HeroStoneMallBo heroStoneMallBo = list.get(i);
			systemIds += heroStoneMallBo.getSystemId() + ",";
		}
		systemIds = systemIds.substring(0, systemIds.length() - 1);
		return systemIds;
	}

	@Override
	public Map<String, Object> getLoginRewardList(String userId) {
		Map<String, Object> rt = new HashMap<String, Object>();
		List<DropToolBO> toolBOList = new ArrayList<DropToolBO>();

		// 读取所有的七天登陆奖励信息
		List<SystemLoginReward7> rewardList = systemLoginReward7Dao.getAll();
		for (SystemLoginReward7 reward : rewardList) {
			DropToolBO toolBO = new DropToolBO(reward.getToolType(), reward.getToolId(), reward.getToolNum());
			toolBOList.add(toolBO);
		}

		// 已经连续登陆多少天，包括今天在内
		int day = userLoginLogDao.get(userId).getDay();
		List<Integer> statusList = getReceStatus(userId, day);

		rt.put("day", day);
		rt.put("tls", toolBOList);
		rt.put("sts", statusList);

		return rt;
	}

	@Override
	public Map<String, Object> recLoginReward(String userId, int day) {
		if (day > 7) {
			day = 7;
		}

		Map<String, Object> rt = new HashMap<String, Object>();

		UserLoginLog userLoginLog = userLoginLogDao.get(userId);
		if (userLoginLog.getRecToday() == 1) {
			String message = "七天奖励 - 今天已经领取过 userId[" + userId + "], day[" + day + "]";
			throw new ServiceException(ActivityService.USER_LOGIN_REWARD7_HAS_RECEIVED, message);
		}

		if (day <= 6) {
			String recStatus = userLoginLog.getRecStatus();
			int index = recStatus.lastIndexOf("0");
			recStatus = recStatus.subSequence(0, index).toString() + "1,";
			userLoginLog.setRecStatus(recStatus);
		}
		userLoginLog.setRecToday(1);
		userLoginLogDao.update(userLoginLog);

		CommonDropBO commonDropBO = new CommonDropBO();

		// 领取奖励
		List<SystemLoginReward7> rewards = systemLoginReward7Dao.getByDay(day);
		for (SystemLoginReward7 reward : rewards) {
			int toolId = reward.getToolId();
			int toolNum = reward.getToolNum();
			int toolType = reward.getToolType();
			List<DropToolBO> dropToolBOList = toolService.giveTools(userId, toolType, toolId, toolNum, ToolUseType.ADD_USER_LOGIN7);

			for (DropToolBO dropToolBO : dropToolBOList) {
				// 构建道具掉落
				this.toolService.appendToDropBO(userId, commonDropBO, dropToolBO);
			}
		}

		rt.put("dr", commonDropBO);
		rt.put("day", day);

		return rt;
	}

	private List<Integer> getReceStatus(String userId, int day) {
		List<Integer> recStatus = new ArrayList<Integer>();
		UserLoginLog loginLog = userLoginLogDao.get(userId);
		String[] status = loginLog.getRecStatus().split(",");
		int length = status.length;
		for (int i = 0; i < length; i++) {
			if (status[i].equals("0") && i != length - 1) {
				recStatus.add(2);
			} else {
				recStatus.add(Integer.valueOf(status[i]));
			}
		}

		if (day > 6) {
			if (loginLog.getRecToday() == 0) {
				recStatus.add(0);
			} else if (loginLog.getRecToday() == 1) {
				recStatus.add(1);
			}
		}

		int size = recStatus.size();
		if (size < 7) {
			for (int i = 0; i < 7 - size; i++) {
				recStatus.add(0);
			}
		}

		return recStatus;
	}

	/**
	 * 根据活动找到相应花的钱
	 * 
	 * @param systemActivity
	 * @param activityId
	 * @param userId
	 * @return
	 */
	public int getCount(SystemActivity systemActivity, int activityId, String userId) {
		int useType = ActivityHelper.getUseTypeByActivityId(activityId);
		return logDao.glodCountByType(userId, -1, useType, systemActivity.getStartTime(), systemActivity.getEndTime());
	}

	public ActivityCostBO getActivityCostReward(String userId, int activityId) {
		// 获取活动
		SystemActivity systemActivity = systemActivityDao.get(activityId);
		// 判断是否活动时间
		checkActivityIsOpen(systemActivity);
		ActivityCostBO bo = new ActivityCostBO();

		// 获取奖励列表
		List<SystemCostReward> rewardList = systemCostRewardDao.getList(activityId);
		// 获得消耗记录数
		int count = getCount(systemActivity, activityId, userId);
		List<ActivityCostRewardBO> adtList = new ArrayList<ActivityCostRewardBO>();
		for (SystemCostReward systemCostReward : rewardList) {
			ActivityCostRewardBO acreward = new ActivityCostRewardBO();
			acreward.setDesc(systemCostReward.getTitle());
			UserCostReward userCostReward = userCostRewardDao.getUserCostReward(userId, activityId, systemCostReward.getId(), systemActivity.getStartTime(),
					systemActivity.getEndTime());
			if (systemCostReward.getGold() <= count) {
				if (userCostReward != null) {
					acreward.setFlag(ACTIVYTY_COST_REWARD_HAS_RECEIVED);
				} else {
					acreward.setFlag(ACTIVYTY_COST_REWARD_CAN_RECEIVED);
				}
			} else {
				acreward.setFlag(ACTIVYTY_COST_REWARD_NOT_RECEIVED);
			}
			List<DropDescBO> ddl = DropDescHelper.parseDropTool(systemCostReward.getRewards());
			acreward.setRl(ddl);
			acreward.setRewardId(systemCostReward.getId());
			acreward.setValue(systemCostReward.getGold());
			adtList.add(acreward);
		}
		bo.setValue(count);
		bo.setList(adtList);
		return bo;

	}

	public CommonDropBO reciveActivityCostReward(String userId, int activityId, int rewardId) {
		// 获取活动
		SystemActivity systemActivity = systemActivityDao.get(activityId);
		// 判断是否活动时间
		checkActivityIsOpen(systemActivity);
		SystemCostReward systemCostReward = systemCostRewardDao.get(activityId, rewardId);
		UserCostReward userCostReward = userCostRewardDao.getUserCostReward(userId, activityId, rewardId, systemActivity.getStartTime(), systemActivity.getEndTime());

		if (userCostReward != null) {
			throw new ServiceException(PAY_REWARD_LIMIT_ERROR, "领取次数超出活动限制");
		}

		int count = getCount(systemActivity, activityId, userId);

		if (systemCostReward.getGold() > count) {
			throw new ServiceException(ACTIVYTY_COST_REWARD_NOT_ENOUGHT, "消耗金额不足");
		}
		UserCostReward userCostRewa = new UserCostReward(userId, activityId, rewardId);

		userCostRewardDao.add(userCostRewa);
		List<DropDescBO> dropDescBOList = DropDescHelper.parseDropTool(systemCostReward.getRewards());

		CommonDropBO commonDropBO = this.toolService.give(userId, dropDescBOList, ToolUseType.ACTIVITY_COST_REWARD);

		return commonDropBO;

	}
	public ActivityCostBO getActivityDayReward(String userId, int activityId) {
		// 获取活动
		SystemActivity systemActivity = systemActivityDao.get(activityId);
		// 判断是否活动时间
		checkActivityIsOpen(systemActivity);
		ActivityCostBO bo = new ActivityCostBO();

		// 获取奖励列表
		List<SystemCostReward> rewardList = systemCostRewardDao.getList(activityId);
		// 获得消耗记录数
		int count = getCount(systemActivity, activityId, userId);
		List<ActivityCostRewardBO> adtList = new ArrayList<ActivityCostRewardBO>();
		for (SystemCostReward systemCostReward : rewardList) {
			ActivityCostRewardBO acreward = new ActivityCostRewardBO();
			acreward.setDesc(systemCostReward.getTitle());
			UserCostReward userCostReward = userCostRewardDao.getUserCostReward(userId, activityId, systemCostReward.getId(), systemActivity.getStartTime(),
					systemActivity.getEndTime());
			if(activityId==47&&systemCostReward.getId()==1){
				Date startTime = DateUtils.str2Date(DateUtils.getDate() + " 12:00:00");
				Date endTime = DateUtils.str2Date(DateUtils.getDate() + " 14:00:00");
				userCostReward = userCostRewardDao.getUserCostReward(userId, activityId, systemCostReward.getId(), startTime,endTime);	
			}else if(activityId==47&&systemCostReward.getId()==2){
				Date startTime = DateUtils.str2Date(DateUtils.getDate() + " 18:00:00");
				Date endTime = DateUtils.str2Date(DateUtils.getDate() + " 20:00:00");
				userCostReward = userCostRewardDao.getUserCostReward(userId, activityId, systemCostReward.getId(),startTime,endTime);
			}
			if (systemCostReward.getGold() <= count) {
				if (userCostReward != null) {
					acreward.setFlag(ACTIVYTY_COST_REWARD_HAS_RECEIVED);
				} else {
					acreward.setFlag(ACTIVYTY_COST_REWARD_CAN_RECEIVED);
				}
			} else {
				acreward.setFlag(ACTIVYTY_COST_REWARD_NOT_RECEIVED);
			}
			List<DropDescBO> ddl = DropDescHelper.parseDropTool(systemCostReward.getRewards());
			acreward.setRl(ddl);
			acreward.setRewardId(systemCostReward.getId());
			acreward.setValue(systemCostReward.getGold());
			adtList.add(acreward);
		}
		bo.setValue(count);
		bo.setList(adtList);
		return bo;

	}
	public CommonDropBO reciveActivityDayReward(String userId, int activityId, int rewardId) {
		// 获取活动
		SystemActivity systemActivity = systemActivityDao.get(activityId);
		// 判断是否活动时间
		checkActivityIsOpen(systemActivity);
		SystemCostReward systemCostReward = systemCostRewardDao.get(activityId, rewardId);
		UserCostReward userCostReward = userCostRewardDao.getUserCostReward(userId, activityId, rewardId, systemActivity.getStartTime(), systemActivity.getEndTime());

		if(activityId==47&&rewardId==1){
			Date startTime = DateUtils.str2Date(DateUtils.getDate() + " 12:00:00");
			Date endTime = DateUtils.str2Date(DateUtils.getDate() + " 14:00:00");
			userCostReward = userCostRewardDao.getUserCostReward(userId, activityId, rewardId, startTime,endTime);
			if(new Date().before(startTime)){
				throw new ServiceException(ACTIVYTY_DAY_TIME_NOT_ENOUGHT, "活动时间不到");
			}else if(new Date().after(endTime)){
				throw new ServiceException(ACTIVYTY_DAY_TIME_PASS, "活动时间已过");
			}
		}else if(activityId==47&&rewardId==2){
			Date startTime = DateUtils.str2Date(DateUtils.getDate() + " 18:00:00");
			Date endTime = DateUtils.str2Date(DateUtils.getDate() + " 20:00:00");
			userCostReward = userCostRewardDao.getUserCostReward(userId, activityId, rewardId,startTime,endTime);
			if(new Date().before(startTime)){
				throw new ServiceException(ACTIVYTY_DAY_TIME_NOT_ENOUGHT, "活动时间不到");
			}else if(new Date().after(endTime)){
				throw new ServiceException(ACTIVYTY_DAY_TIME_PASS, "活动时间已过");
			}
		}
		if (userCostReward != null) {
			throw new ServiceException(PAY_REWARD_LIMIT_ERROR, "领取次数超出活动限制");
		}
		
		UserCostReward userCostRewa = new UserCostReward(userId, activityId, rewardId);

		userCostRewardDao.add(userCostRewa);
		List<DropDescBO> dropDescBOList = DropDescHelper.parseDropTool(systemCostReward.getRewards());

		CommonDropBO commonDropBO = this.toolService.give(userId, dropDescBOList, ToolUseType.ACTIVITY_COST_REWARD);

		return commonDropBO;

	}
}
