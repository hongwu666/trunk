package com.lodogame.ldsg.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.ConfigDataDao;
import com.lodogame.game.dao.PaymentLogDao;
import com.lodogame.game.dao.SystemForcesDao;
import com.lodogame.game.dao.SystemPriceDao;
import com.lodogame.game.dao.UserDailyGainLogDao;
import com.lodogame.game.dao.UserDao;
import com.lodogame.game.dao.UserExtinfoDao;
import com.lodogame.game.dao.UserGiftbagDao;
import com.lodogame.game.utils.json.Json;
import com.lodogame.ldsg.bo.AwardDescBO;
import com.lodogame.ldsg.bo.ChatBO;
import com.lodogame.ldsg.bo.CopyActivityBO;
import com.lodogame.ldsg.bo.MaxGoldSetBO;
import com.lodogame.ldsg.bo.SweepInfoBO;
import com.lodogame.ldsg.bo.SystemActivityBO;
import com.lodogame.ldsg.bo.SystemGoldSetBO;
import com.lodogame.ldsg.bo.UserBO;
import com.lodogame.ldsg.bo.UserEquipBO;
import com.lodogame.ldsg.bo.UserForcesBO;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.bo.UserLimOnlineRewardBO;
import com.lodogame.ldsg.bo.UserOnlineRewardBO;
import com.lodogame.ldsg.bo.UserPkInfoBO;
import com.lodogame.ldsg.bo.UserRecivePowerBO;
import com.lodogame.ldsg.bo.UserStoneBO;
import com.lodogame.ldsg.bo.UserTaskBO;
import com.lodogame.ldsg.bo.UserTavernBO;
import com.lodogame.ldsg.bo.UserToolBO;
import com.lodogame.ldsg.constants.ActivityId;
import com.lodogame.ldsg.constants.ConfigKey;
import com.lodogame.ldsg.constants.ForcesStatus;
import com.lodogame.ldsg.constants.ForcesType;
import com.lodogame.ldsg.constants.GiftBagType;
import com.lodogame.ldsg.constants.GoldDefine;
import com.lodogame.ldsg.constants.PriceType;
import com.lodogame.ldsg.constants.PushType;
import com.lodogame.ldsg.constants.TaskStatus;
import com.lodogame.ldsg.constants.UserDailyGainLogType;
import com.lodogame.ldsg.service.ActivityService;
import com.lodogame.ldsg.service.DailyTaskService;
import com.lodogame.ldsg.service.EquipService;
import com.lodogame.ldsg.service.ForcesService;
import com.lodogame.ldsg.service.GoldSetService;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.MailService;
import com.lodogame.ldsg.service.MallService;
import com.lodogame.ldsg.service.PkService;
import com.lodogame.ldsg.service.PriceService;
import com.lodogame.ldsg.service.SceneService;
import com.lodogame.ldsg.service.StoneService;
import com.lodogame.ldsg.service.TaskService;
import com.lodogame.ldsg.service.TavernService;
import com.lodogame.ldsg.service.ToolService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.ldsg.service.VipService;
import com.lodogame.model.ConfigData;
import com.lodogame.model.SystemForces;
import com.lodogame.model.SystemPrice;
import com.lodogame.model.UserExtinfo;
import com.lodogame.model.UserGiftbag;
import com.lodogame.model.UserMapper;

public class PushHandlerImpl extends BasePushHandler implements PushHandler {

	private static final Logger logger = Logger.getLogger(PushHandlerImpl.class);

	@Autowired
	private SystemForcesDao systemForcesDao;

	@Autowired
	private MallService mallService;

	@Autowired
	private UserService userService;

	@Autowired
	private HeroService heroService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private PriceService priceService;

	@Autowired
	private EquipService equipService;

	@Autowired
	private ToolService toolService;

	@Autowired
	private ForcesService forcesService;

	@Autowired
	private GoldSetService goldSetService;

	@Autowired
	private UserExtinfoDao userExtinfoDao;

	@Autowired
	private ConfigDataDao configDataDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private ActivityService activityService;

	@Autowired
	private PkService pkService;

	@Autowired
	private VipService vipService;

	@Autowired
	private TavernService tavernService;

	@Autowired
	private SceneService sceneService;

	@Autowired
	private PaymentLogDao paymentLogDao;

	@Autowired
	private UserGiftbagDao userGiftbagDao;

	@Autowired
	private UserDailyGainLogDao userDailyGainLogDao;

	@Autowired
	private MailService mailService;

	@Autowired
	private StoneService stoneService;

	@Autowired
	private DailyTaskService dailyTaskService;

	@Autowired
	private SystemPriceDao systemPriceDao;

	@Override
	public void pushSweepStatus(String userId, int status, Date endTime) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("st", status);
		map.put("uid", userId);
		map.put("et", endTime);
		push("Scene.pushSweepInfo", map);
	}

	public void pushUser(String uid) {

		UserBO userBO = this.userService.getUserBO(uid);
		if (userBO == null) {
			logger.warn("推送用户数据失败，用户获取不到.uid[" + uid + "]");
			return;
		}

		// this.userDao.setOnline(uid, true);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uid", uid);
		params.put("tp", PushType.PUSH_TYPE_UPDATE);
		params.put("uinfo", userBO);

		this.push("User.pushUser", params);
	}

	public void pushHero(String uid, String userHeroId, int pushType) {

		UserHeroBO userHeroBO = this.heroService.getUserHeroBO(uid, userHeroId);
		if (userHeroBO == null) {
			logger.warn("推送用户武将失败，武将获取不到.uid[" + uid + "], userHeroId[" + userHeroId + "]");
			return;
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uid", uid);
		params.put("tp", pushType);
		params.put("hero", userHeroBO);

		this.push("Hero.pushHero", params);
	}

	public void pushEquip(String uid, String userEquipId, int pushType) {

		UserEquipBO userEquipBO = this.equipService.getUserEquipBO(uid, userEquipId);
		if (userEquipBO == null) {
			logger.warn("推送用户装备失败，装备获取不到.uid[" + uid + "], userEquipId[" + userEquipId + "]");
			return;
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uid", uid);
		params.put("tp", pushType);
		params.put("eq", userEquipBO);

		this.push("Equipment.pushEquip", params);
	}

	public void pushTavernInfo(String uid) {

		// this.tavernService

	}

	public void pushToolList(String uid) {

		List<UserToolBO> userToolBOList = this.toolService.getUserToolList(uid);

		this.pushToolList(uid, userToolBOList);
	}

	@Override
	public void pushToolList(String uid, List<UserToolBO> userToolBOList) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uid", uid);
		params.put("tls", userToolBOList);

		this.push("Hero.pushTools", params);
	}

	@Override
	public void pushHeroList(String uid) {

		List<UserHeroBO> userHeroBOList = this.heroService.getUserHeroList(uid, 0);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uid", uid);
		params.put("uhls", userHeroBOList);

		this.push("Hero.pushHeroList", params);
	}

	@Override
	public void pushEquipList(String uid) {

		List<UserEquipBO> userEquipBOList = this.equipService.getUserEquipList(uid);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uid", uid);
		params.put("eqls", userEquipBOList);

		this.push("Equipment.pushEquipList", params);
	}

	@Override
	public void pushForcesList(String uid) {

		List<UserForcesBO> userForcesBOList = this.forcesService.getUserForcesList(uid, 0);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uid", uid);
		params.put("ufls", userForcesBOList);

		this.push("Scene.pushForcesList", params);
	}

	@Override
	public void pushUserReLogin(String uid) {
		Map<String, Object> params = packUserData(uid, null);

		this.push("User.reLoginRq", params);
	}

	@Override
	public void pushUserData(String uid) {

		Map<String, Object> params = packUserData(uid, null);

		this.push("User.pushUserData", params);
	}

	@Override
	public void pushMidnightData(String uid) {

		Set<String> keys = new HashSet<String>();
		keys.add("fs");
		keys.add("nbpc");
		keys.add("uinfo");
		keys.add("pkif");
		keys.add("rsft");
		keys.add("arc");
		keys.add("cidr");
		keys.add("bsb");
		keys.add("sts");

		Map<String, Object> params = packUserData(uid, keys);

		/**
		 * 凌晨12点时推送期限在线奖励信息 首先检查现在时间是否在期限在线奖励时间内，如果是，将倒计时时间设置为
		 * <code>ActivityService.LIM_ONLINE_REWARD_COUNTDOWN</code> 并设置奖励可以领取
		 */
		UserLimOnlineRewardBO rewardBO = new UserLimOnlineRewardBO();

		boolean open = activityService.isActivityOpen(ActivityId.LIM_ONLINE_REWARD_ID);

		if (open) {
			long recTime = new Date().getTime() + ActivityService.LIM_ONLINE_REWARD_COUNTDOWN;
			rewardBO.setStatus(1);
			rewardBO.setRecTime(recTime);
		} else {
			rewardBO.setStatus(0);
			rewardBO.setRecTime(0);
		}

		params.put("lor", rewardBO);

		this.push("User.pushMidnightData", params);
	}

	private Map<String, Object> packUserData(String uid, Set<String> keys) {

		Map<String, Object> params = new HashMap<String, Object>();

		UserBO userBO = null;

		long start = System.currentTimeMillis();

		// 用户对象
		userBO = this.userService.getUserBO(uid);
		params.put("uinfo", userBO);

		logger.debug("time stat.key[uinfo], time[" + (System.currentTimeMillis() - start) + "]");
		start = System.currentTimeMillis();

		// 用户武将列表
		if (keys == null || keys.contains("hls")) {
			List<UserHeroBO> userHeroBOList = this.heroService.getUserHeroList(uid, 0);
			params.put("hls", userHeroBOList);
		}

		logger.debug("time stat.key[hls], time[" + (System.currentTimeMillis() - start) + "]");
		start = System.currentTimeMillis();

		// 用户装备列表
		if (keys == null || keys.contains("eqs")) {
			List<UserEquipBO> userEquipBOList = this.equipService.getUserEquipList(uid);
			params.put("eqs", userEquipBOList);
		}

		logger.debug("time stat.key[eqs], time[" + (System.currentTimeMillis() - start) + "]");
		start = System.currentTimeMillis();

		// 用户材料列表
		if (keys == null || keys.contains("tls")) {
			List<UserToolBO> userToolBOList = this.toolService.getUserToolList(uid);
			params.put("tls", userToolBOList);
		}

		logger.debug("time stat.key[tls], time[" + (System.currentTimeMillis() - start) + "]");
		start = System.currentTimeMillis();

		// 用户吃包子信息
		if (keys == null || keys.contains("rpl")) {
			List<UserRecivePowerBO> userRecivePowerBOList = this.activityService.getUserRecivePowerInfo(uid);
			params.put("rpl", userRecivePowerBOList);
		}

		logger.debug("time stat.key[rpl], time[" + (System.currentTimeMillis() - start) + "]");
		start = System.currentTimeMillis();

		// 公告地址
		if (keys == null || keys.contains("nurl")) {
			ConfigData configData = this.configDataDao.get("note_url");
			if (configData != null) {
				params.put("nurl", configData.getConfigValue());
			}
		}

		// 扫荡信息
		if (keys == null || keys.contains("swp")) {
			SweepInfoBO sweepInfBO = sceneService.getUserSweepInfo(uid);
			params.put("swp", sweepInfBO);
		}

		// 竞技场奖励信息
		if (keys == null || keys.contains("awd")) {
			List<AwardDescBO> awdList = pkService.getAwardList();
			params.put("awd", awdList);
		}

		start = System.currentTimeMillis();

		// 普通怪最后一个
		if (keys == null || keys.contains("fid")) {
			UserForcesBO userForcesBO = this.forcesService.getUserCurrentForces(uid, ForcesType.FORCES_TYPE_NORMAL);
			params.put("fid", userForcesBO != null ? userForcesBO.getGroupId() : 0);
			if (userForcesBO != null && userForcesBO.getStatus() == ForcesStatus.STATUS_PASS) {
				params.put("adf", 1);
			} else {
				params.put("adf", 0);
			}
		}

		logger.debug("time stat.key[fid], time[" + (System.currentTimeMillis() - start) + "]");
		start = System.currentTimeMillis();

		// 精英怪最后一个
		if (keys == null || keys.contains("efid")) {

			params.put("sefid", 0);

			UserForcesBO userEliteForcesBO = this.forcesService.getUserCurrentForces(uid, ForcesType.FORCES_TYPE_ELITE);
			params.put("efid", userEliteForcesBO != null ? userEliteForcesBO.getGroupId() : 0);
			if (userEliteForcesBO != null && userEliteForcesBO.getStatus() == ForcesStatus.STATUS_PASS) {
				params.put("eadf", 1);

				int groupId = userEliteForcesBO.getGroupId();
				SystemForces systemForces = systemForcesDao.getLastForcesByGroupId(groupId);

				List<SystemForces> list = systemForcesDao.getSystemForcesByPreForcesId(systemForces.getForcesId());
				if (!list.isEmpty()) {
					params.put("sefid", list.get(0).getGroupId());
				}

			} else {
				params.put("eadf", 0);
			}
		}

		logger.debug("time stat.key[efid], time[" + (System.currentTimeMillis() - start) + "]");
		start = System.currentTimeMillis();

		// 怪物部队信息
		if (keys != null && keys.contains("fs")) {
			List<UserForcesBO> rtUserForcesBOList = new ArrayList<UserForcesBO>();
			List<UserForcesBO> userForcesBoList = this.forcesService.getUserForcesList(uid, 0);
			for (UserForcesBO ufbo : userForcesBoList) {
				if (ufbo.getTimes() > 0) {
					rtUserForcesBOList.add(ufbo);
				}
			}
			params.put("fs", rtUserForcesBOList);
		}

		logger.debug("time stat.key[fs], time[" + (System.currentTimeMillis() - start) + "]");
		start = System.currentTimeMillis();

		// 配置信息
		if (keys == null || keys.contains("uc")) {
			params.put("uc", getConfig(uid));
		}

		logger.debug("time stat.key[uc], time[" + (System.currentTimeMillis() - start) + "]");
		start = System.currentTimeMillis();

		// 金币套餐
		if (keys == null || keys.contains("gss")) {
			List<SystemGoldSetBO> systemGoldSetBOList = this.goldSetService.getGoldSetList(uid);
			params.put("gss", systemGoldSetBOList);

			MaxGoldSetBO bo = getMaxGoldSetBO(systemGoldSetBOList);
			params.put("mxp", bo);
		}

		logger.debug("time stat.key[gss], time[" + (System.currentTimeMillis() - start) + "]");
		start = System.currentTimeMillis();

		logger.debug("time stat.key[tks], time[" + (System.currentTimeMillis() - start) + "]");
		start = System.currentTimeMillis();

		// 买体力消耗信息
		if (keys == null || keys.contains("nbpc")) {
			UserExtinfo userExtinfo = this.userExtinfoDao.get(uid);
			int buyPowerTimes = 1;
			if (userExtinfo != null && DateUtils.isSameDay(userExtinfo.getLastBuyPowerTime(), new Date())) {
				buyPowerTimes = userExtinfo.getBuyPowerTimes() + 1;
			}

			// int maxTimes =
			// this.vipService.getBuyPowerLimit(userBO.getVipLevel());

			params.put("nbpc", priceService.getPrice(PriceType.BUY_POWER, buyPowerTimes));
			params.put("bpt", buyPowerTimes);
		}

		logger.debug("time stat.key[nbpc], time[" + (System.currentTimeMillis() - start) + "]");
		start = System.currentTimeMillis();

		// 副本活动信息
		if (keys == null || keys.contains("ac")) {
			List<CopyActivityBO> copyActivityBOList = this.activityService.getCopyActivityList();
			params.put("ac", copyActivityBOList);
		}

		logger.debug("time stat.key[ac], time[" + (System.currentTimeMillis() - start) + "]");
		start = System.currentTimeMillis();

		logger.debug("time stat.key[awd], time[" + (System.currentTimeMillis() - start) + "]");
		start = System.currentTimeMillis();

		// 酒馆抽牌信息
		if (keys == null || keys.contains("tis")) {
			List<UserTavernBO> userTavernBOList = this.tavernService.getTavernCDInfo(uid);
			params.put("tis", userTavernBOList);
		}

		logger.debug("time stat.key[tis], time[" + (System.currentTimeMillis() - start) + "]");
		start = System.currentTimeMillis();

		if (keys == null || keys.contains("pkif")) {
			UserPkInfoBO pkInfoBO = pkService.getUserPkInfo(uid);
			params.put("pkif", pkInfoBO);
		}

		logger.debug("time stat.key[swp], time[" + (System.currentTimeMillis() - start) + "]");
		start = System.currentTimeMillis();

		// 重置副本次数
		if (keys == null || keys.contains("frsm")) {

			int userDayResetForcesTimes = this.userDailyGainLogDao.getUserDailyGain(uid, UserDailyGainLogType.RESET_FORCES);
			SystemPrice systemPrice = systemPriceDao.get(PriceType.RESET_FORCES_TIMES, userDayResetForcesTimes + 1);
			if (systemPrice != null) {
				params.put("frsm", systemPrice.getAmount());
			} else {
				params.put("frsm", 0);
			}

			int userDayResetEliteForcesTimes = this.userDailyGainLogDao.getUserDailyGain(uid, UserDailyGainLogType.RESET_ELITE_FORCES);
			systemPrice = systemPriceDao.get(PriceType.RESET_FORCES_TIMES, userDayResetEliteForcesTimes + 1);
			if (systemPrice != null) {
				params.put("efrsm", systemPrice.getAmount());
			} else {
				params.put("efrsm", 0);
			}
		}

		logger.debug("time stat.key[rsft], time[" + (System.currentTimeMillis() - start) + "]");
		start = System.currentTimeMillis();

		// 首充礼包状态
		if (keys == null || keys.contains("fps")) {

			int fps = 0;

			int payAmount = 0;

			if (userBO != null) {
				payAmount = userBO.getPayAmount();
			} else {
				payAmount = this.paymentLogDao.getPaymentTotalGold(uid);
			}

			if (payAmount > 0) {
				fps = 1;

				UserGiftbag userGiftbag = this.userGiftbagDao.getLast(uid, GiftBagType.FIRST_PAY_GIFTBAG);
				if (userGiftbag != null) {
					fps = 2;
				}
			}

			params.put("fps", fps);
		}

		logger.debug("time stat.key[fps], time[" + (System.currentTimeMillis() - start) + "]");
		start = System.currentTimeMillis();

		logger.debug("time stat.key[hskls], time[" + (System.currentTimeMillis() - start) + "]");
		start = System.currentTimeMillis();

		// 在线礼包状态
		if (keys == null || keys.contains("olgb")) {
			UserOnlineRewardBO userOnlineRewardBO = this.activityService.getUserOnlineRewardBO(uid);
			params.put("olgb", userOnlineRewardBO);
		}

		logger.debug("time stat.key[olgb], time[" + (System.currentTimeMillis() - start) + "]");
		start = System.currentTimeMillis();

		// 系统时间
		if (keys == null || keys.contains("st")) {
			params.put("st", System.currentTimeMillis());
		}

		logger.debug("time stat.key[st], time[" + (System.currentTimeMillis() - start) + "]");
		start = System.currentTimeMillis();

		// 系统配置
		if (keys == null || keys.contains("scfg")) {
			int ack = configDataDao.getInt(ConfigKey.OPEN_ACK, 0);
			Map<String, Integer> map = new HashMap<String, Integer>();
			map.put("ack", ack);
			map.put("hbmx", 800);
			params.put("scfg", map);
		}

		logger.debug("time stat.key[scfg], time[" + (System.currentTimeMillis() - start) + "]");
		start = System.currentTimeMillis();

		logger.debug("time stat.key[arc], time[" + (System.currentTimeMillis() - start) + "]");
		start = System.currentTimeMillis();

		// 活动信息
		if (keys == null || keys.contains("acl")) {
			List<SystemActivityBO> systemActivityBOList = this.activityService.getDisplayActivityBOList(uid);
			params.put("acl", systemActivityBOList);
		}

		logger.debug("time stat.key[acl], time[" + (System.currentTimeMillis() - start) + "]");
		start = System.currentTimeMillis();

		logger.debug("time stat.key[rgs], time[" + (System.currentTimeMillis() - start) + "]");
		start = System.currentTimeMillis();

		if (keys == null || keys.contains("emst")) {
			boolean hasNewMail = mailService.hasNewMail(uid);
			if (hasNewMail) {
				params.put("emst", 1);
			} else {
				params.put("emst", 0);
			}
		}

		logger.debug("time stat.key[emst], time[" + (System.currentTimeMillis() - start) + "]");
		start = System.currentTimeMillis();

		// 是否在商城道具打折活动期间，是返回1，否则返回0；
		if (keys == null || keys.contains("dis")) {
			int dis;

			if (mallService.checkDiscountIsOpen() != null) {
				dis = 1;
			} else {
				dis = 0;
			}
			params.put("dis", dis);
		}

		logger.debug("time stat.key[dis], time[" + (System.currentTimeMillis() - start) + "]");
		start = System.currentTimeMillis();

		if (keys == null || keys.contains("rgs")) {
			UserExtinfo info = userExtinfoDao.get(uid);
			params.put("rgs", info.getRecordGuideStep());
		}

		start = System.currentTimeMillis();

		// 用户宝石
		if (keys == null || keys.contains("sts")) {
			List<UserStoneBO> list = this.stoneService.getUserStoneBos(uid);
			params.put("sts", list);
		}

		// 是否有三十天登陆奖励可领取|1有可领取的；2表示没有可奖励；3三十天都领完了
		if (keys == null || keys.contains("gulri")) {
			int gulri = this.activityService.checkLoginRewardHasGiven(uid);
			params.put("gulri", gulri);
		}

		start = System.currentTimeMillis();

		params.put("uid", uid);

		return params;
	}

	private Map<String, Object> getConfig(String uid) {

		Map<String, Object> config = new HashMap<String, Object>();
		config.put("rpg", GoldDefine.RESUME_POWER);
		UserMapper userMapper = userService.getUserMapper(uid);
		if (userMapper != null) {
			config.put("sid", userMapper.getServerId());
			config.put("puid", userMapper.getPartnerUserId());
		}
		return config;
	}

	@Override
	public void pushCurrentForces(String uid, int type) {

		UserForcesBO userForcesBO = this.forcesService.getUserCurrentForces(uid, type);
		if (userForcesBO != null) {
			Map<String, Object> params = new HashMap<String, Object>();

			if (type == ForcesType.FORCES_TYPE_NORMAL) {
				params.put("fid", userForcesBO.getGroupId());
			} else {
				params.put("efid", userForcesBO.getGroupId());
			}
			params.put("uid", uid);

			this.push("Scene.pushForces", params);

		}
	}

	@Override
	public void pushOncePayRewardActivated(String uid) {
		if (activityService.checkOncePayReward(uid)) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("aid", ActivityId.ONCE_PAY_REWARD_ID);
			this.push("Activity.pushRewardActivated", params);
		}
	}

	@Override
	public void pushTotalPayRewardActivated(String uid) {
		if (activityService.checkTotalPayReward(uid)) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("aid", ActivityId.TOTAL_PAY_REWARD_ID);
			this.push("Activity.pushRewardActivated", params);
		}
	}

	@Override
	public void pushMessage(String uid, List<com.lodogame.ldsg.bo.Message> msgList) {

		logger.debug("推送走马灯.uid[" + uid + "], msgList[" + Json.toJson(msgList) + "]");
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("ml", msgList);
		params.put("uid", uid);

		this.push("System.pushMsg", params);
	}

	@Override
	public void checkUserOnline(String uid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uid", uid);
		this.push("User.checkOnline", params);
	}

	@Override
	public void pushNewMail(String uid, int type) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uid", uid);
		params.put("tp", type);
		this.push("Email.pushNewEmail", params);
	}

	private MaxGoldSetBO getMaxGoldSetBO(List<SystemGoldSetBO> systemGoldSetBOList) {
		SystemGoldSetBO systemGoldSetBO = null;
		for (SystemGoldSetBO bo : systemGoldSetBOList) {
			if (systemGoldSetBO == null || systemGoldSetBO.getMoney().intValue() < bo.getMoney().intValue()) {
				systemGoldSetBO = bo;
			}
		}

		MaxGoldSetBO maxGoldSetBO = new MaxGoldSetBO();
		maxGoldSetBO.setMoney(systemGoldSetBO.getMoney());
		maxGoldSetBO.setGold(systemGoldSetBO.getMoney().intValue() * 10 + systemGoldSetBO.getGold());
		maxGoldSetBO.setId(systemGoldSetBO.getSystemGoldSetId());

		return maxGoldSetBO;
	}

	@Override
	public void pushChat(String userId, ChatBO chatBO) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("uid", userId);
		params.put("cbo", chatBO);

		this.push("Chat.pushChatInfo", params);

	}

	@Override
	public void pushNotification(String userId) {
		Map<String, Object> params = new HashMap<String, Object>();

		List<Integer> notifications = userService.checkNotification(userId);
		params.put("uid", userId);
		params.put("nfs", notifications);

		this.push("User.pushNotifications", params);

	}

	public void pushTask(String uid, int systemTaskId, int pushType) {

		logger.debug("推送用户任务信息.userId[" + uid + "], systemTaskId[" + systemTaskId + "], pushType[" + pushType + "]");

		Map<String, Object> params = new HashMap<String, Object>();

		UserTaskBO userTaskBO = null;
		if (pushType != PushType.PUSH_TYPE_DELETE) {
			userTaskBO = this.taskService.get(uid, systemTaskId);
			if (userTaskBO == null) {
				logger.warn("推送用户任务失败，任务获取不到.uid[" + uid + "], systemTaskId[" + systemTaskId + "]");
				return;
			}
		} else {
			// 删除的不推
			return;
			// userTaskBO = new UserTaskBO();
			// userTaskBO.setSystemTaskId(systemTaskId);
		}

		params.put("tk", userTaskBO);

		params.put("uid", uid);
		params.put("tp", pushType);

		this.push("Task.pushTask", params);

	}

	@Override
	public void pushTaskList(String uid) {

		List<UserTaskBO> userTaskBOList = this.taskService.getUserTaskList(uid, TaskStatus.TASK_STATUS_ALL);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uid", uid);
		params.put("tklt", userTaskBOList);

		this.push("Task.pushTaskList", params);
	}

	@Override
	public void pushPraise(String userId, String sender) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uid", userId);
		params.put("sender", sender);

		this.push("Friend.pushPraise", params);
	}
}
