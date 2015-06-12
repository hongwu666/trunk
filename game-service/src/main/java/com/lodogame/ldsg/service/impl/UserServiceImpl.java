package com.lodogame.ldsg.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

import com.lodogame.game.dao.CommandDao;
import com.lodogame.game.dao.ConfigDataDao;
import com.lodogame.game.dao.PaymentLogDao;
import com.lodogame.game.dao.RuntimeDataDao;
import com.lodogame.game.dao.SystemBadwordDao;
import com.lodogame.game.dao.SystemRecivePowerDao;
import com.lodogame.game.dao.SystemUserLevelDao;
import com.lodogame.game.dao.SystemVipLevelDao;
import com.lodogame.game.dao.UserArenaInfoDao;
import com.lodogame.game.dao.UserDailyGainLogDao;
import com.lodogame.game.dao.UserDao;
import com.lodogame.game.dao.UserExtinfoDao;
import com.lodogame.game.dao.UserFriendRequestDao;
import com.lodogame.game.dao.UserLoginLogDao;
import com.lodogame.game.dao.UserLoginRewardInfoDao;
import com.lodogame.game.dao.UserMapperDao;
import com.lodogame.game.dao.UserOnlineLogDao;
import com.lodogame.game.dao.UserPersonalMailDao;
import com.lodogame.game.dao.UserTavernDao;
import com.lodogame.game.dao.UserToolDao;
import com.lodogame.game.dao.clearcache.ClearCacheManager;
import com.lodogame.game.dao.initcache.InitCacheManager;
import com.lodogame.game.utils.Constant;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.game.utils.IDGenerator;
import com.lodogame.game.utils.IllegalWordUtills;
import com.lodogame.ldsg.bo.UserBO;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.bo.UserPayRewardBO;
import com.lodogame.ldsg.constants.ActivityTargetType;
import com.lodogame.ldsg.constants.CommandType;
import com.lodogame.ldsg.constants.ConfigKey;
import com.lodogame.ldsg.constants.InitDefine;
import com.lodogame.ldsg.constants.NotificationType;
import com.lodogame.ldsg.constants.PriceType;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.constants.TavernConstant;
import com.lodogame.ldsg.constants.ToolType;
import com.lodogame.ldsg.constants.ToolUseType;
import com.lodogame.ldsg.constants.UserDailyGainLogType;
import com.lodogame.ldsg.constants.UserFriendConstant;
import com.lodogame.ldsg.constants.UserPayRewardStatus;
import com.lodogame.ldsg.constants.UserPowerGetType;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.LoginEvent;
import com.lodogame.ldsg.event.LogoutEvent;
import com.lodogame.ldsg.event.UserInfoUpdateEvent;
import com.lodogame.ldsg.event.UserLevelUpEvent;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.helper.ActivityHelper;
import com.lodogame.ldsg.helper.BOHelper;
import com.lodogame.ldsg.helper.CopperHelper;
import com.lodogame.ldsg.helper.HeroHelper;
import com.lodogame.ldsg.helper.LodoIDHelper;
import com.lodogame.ldsg.helper.RegHelper;
import com.lodogame.ldsg.service.ActivityService;
import com.lodogame.ldsg.service.ActivityTaskService;
import com.lodogame.ldsg.service.DailyTaskService;
import com.lodogame.ldsg.service.EquipService;
import com.lodogame.ldsg.service.EventService;
import com.lodogame.ldsg.service.FriendService;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.MessageService;
import com.lodogame.ldsg.service.PriceService;
import com.lodogame.ldsg.service.RobotService;
import com.lodogame.ldsg.service.SceneService;
import com.lodogame.ldsg.service.TaskService;
import com.lodogame.ldsg.service.TavernService;
import com.lodogame.ldsg.service.UnSynLogService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.ldsg.service.VipService;
import com.lodogame.ldsg.validation.UserValid;
import com.lodogame.model.Command;
import com.lodogame.model.RobotUser;
import com.lodogame.model.RuntimeData;
import com.lodogame.model.SystemHero;
import com.lodogame.model.SystemRecivePower;
import com.lodogame.model.SystemUserLevel;
import com.lodogame.model.SystemVipLevel;
import com.lodogame.model.User;
import com.lodogame.model.UserExtinfo;
import com.lodogame.model.UserFriendRequest;
import com.lodogame.model.UserLoginLog;
import com.lodogame.model.UserLoginRewardInfo;
import com.lodogame.model.UserMapper;
import com.lodogame.model.UserOnlineLog;
import com.lodogame.model.UserPowerInfo;
import com.lodogame.model.UserTavern;

public class UserServiceImpl implements UserService {

	private static final Logger logger = Logger.getLogger(UserServiceImpl.class);

	@Autowired
	private RobotService robotService;

	@Autowired
	private DailyTaskService dailyTaskService;

	@Autowired
	private UserLoginLogDao userLoginLogDao;

	@Autowired
	private UserLoginRewardInfoDao userLoginRewardInfoDao;

	@Autowired
	private TaskService taskService;

	@Autowired
	private UserPersonalMailDao userPersonalMailDao;

	@Autowired
	private UserFriendRequestDao userFriendRequestDao;

	@Autowired
	private FriendService friendService;

	@Autowired
	private TavernService tavernService;

	@Autowired
	private PriceService priceService;

	@Autowired
	private SystemRecivePowerDao systemRecivePowerDao;

	@Autowired
	private RuntimeDataDao runtimeDataDao;

	@Autowired
	private ActivityService activityService;

	@Autowired
	private UserDao userDao;

	@Autowired
	private RuntimeDataDao rumtimeDataDao;

	@Autowired
	private UserMapperDao userMapperDao;

	@Autowired
	private HeroService heroService;

	@Autowired
	private EquipService equipService;

	@Autowired
	private SceneService sceneService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private SystemUserLevelDao systemUserLevelDao;

	@Autowired
	private CommandDao commandDao;

	@Autowired
	private UserOnlineLogDao userOnlineLogDao;

	@Autowired
	private UserExtinfoDao userExtinfoDao;

	@Autowired
	private UserTavernDao userTavernDao;

	@Autowired
	private VipService vipService;

	@Autowired
	private SystemVipLevelDao systemVipLevelDao;

	@Autowired
	private PaymentLogDao paymentLogDao;

	@Autowired
	private UserToolDao userToolDao;

	@Autowired
	private ActivityTaskService activityTaskService;

	@Autowired
	private UnSynLogService unSynLogService;

	@Autowired
	private UserDailyGainLogDao userDailyGainLogDao;

	@Autowired
	private ConfigDataDao configDataDao;

	@Autowired
	private EventService eventService;

	@Autowired
	private SystemBadwordDao systemBadwordDao;

	@Autowired
	private UserArenaInfoDao userArenaInfoDao;

	public UserPowerInfo getRandAttUser(String userId, UserPowerGetType type, int min, int max) {
		return userDao.getUserIdByPowerRand(userId, type.getType(), min, max);
	}

	public UserPowerInfo getRandUser(String userId, int minPower) {
		return userDao.getUserIdRand(userId, minPower);
	}

	public UserBO getUserBO(String userId) {

		User user = this.get(userId);

		UserExtinfo userExtinfo = this.userExtinfoDao.get(userId);

		UserBO userBO = BOHelper.craeteUserBO(user, userExtinfo);

		int payAmount = this.paymentLogDao.getPaymentTotalGold(userId);
		userBO.setPayAmount(payAmount);

		userBO.setCapability(HeroHelper.getCapability(this.heroService.getUserHeroList(userId, 1)));

		return userBO;
	}

	public UserBO getUserBOByPlayerId(String playerId) {

		User user = this.getByPlayerId(playerId);

		UserExtinfo userExtinfo = this.userExtinfoDao.get(user.getUserId());

		return BOHelper.craeteUserBO(user, userExtinfo);
	}

	/**
	 * 检查体力增加
	 * 
	 * @param user
	 */
	public boolean checkPowerAdd(User user) {

		// int maxPower = this.vipService.getPowerMax(user.getVipLevel());
		SystemUserLevel systemUserLevel = this.systemUserLevelDao.get(user.getLevel());
		int maxPower = systemUserLevel.getPowerMax();

		Date now = new Date();// 当前时间
		Date powerAddTime = user.getPowerAddTime();// 上次添加体力的时间
		long timestamp1 = now.getTime();
		long timestamp2 = powerAddTime.getTime();
		long sub = timestamp1 - timestamp2;
		if (sub < InitDefine.POWER_ADD_INTERVAL) {// 时间还没到
			return false;
		}

		Date newPowerAddTime;
		long addPower;
		long newPower;

		if (user.getPower() >= maxPower) {// 如果体力已经超过上限
			addPower = 0;
			newPower = user.getPower();
			newPowerAddTime = new Date();
		} else {
			addPower = sub / InitDefine.POWER_ADD_INTERVAL;// 可以加多少体力
			newPower = user.getPower() + addPower;
			if (newPower > maxPower) {
				newPower = maxPower;
				addPower = maxPower - user.getPower();
				newPowerAddTime = new Date();
			} else {
				long leaveTimes = addPower * InitDefine.POWER_ADD_INTERVAL - sub;// 要把余下的时间给用户算回去
				newPowerAddTime = DateUtils.add(now, Calendar.MILLISECOND, (int) leaveTimes);
			}
		}

		if (addPower == 0) {
			return true;
		}

		logger.debug("用户恢复体力.userId[" + user.getUserId() + "], addPower[" + addPower + "], powerAddTime[" + newPowerAddTime + "]");

		// 更改 bean
		user.setPower((int) newPower);
		user.setPowerAddTime(newPowerAddTime);

		this.addPower(user.getUserId(), (int) addPower, (int) (user.getPower() + addPower), ToolUseType.ADD_AUTO_RESUME, newPowerAddTime);

		// 发push命令.
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", user.getUserId());

		Command command = new Command();
		command.setCommand(CommandType.COMMAND_PUSH_USER_INFO);
		command.setType(CommandType.PUSH_USER);
		command.setParams(params);

		commandDao.add(command);

		return true;
	}

	public User get(String userId) {

		User user = this.userDao.get(userId);

		if (user == null) {
			throw new ServiceException(LOAD_USER_NOT_EXIST, "用户不存在.userId[" + userId + "]");
		}

		UserExtinfo userExtInfo = this.userExtinfoDao.get(userId);
		if (userExtInfo.getRewardVipLevel() > user.getVipLevel()) {
			user.setVipLevel(userExtInfo.getRewardVipLevel());
		}

		return user;
	}

	@Override
	public List<UserBO> listOrderByLevelDesc(int offset, int size) {
		List<User> users = this.userDao.listOrderByLevelDesc(offset, size);
		List<UserBO> userBOs = new ArrayList<UserBO>();
		for (User user : users) {
			UserExtinfo userExtinfo = this.userExtinfoDao.get(user.getUserId());
			UserBO userBO = BOHelper.craeteUserBO(user, userExtinfo);
			userBOs.add(userBO);
		}
		return userBOs;
	}

	@Override
	public List<User> listOrderByCopperDesc(int offset, int size) {
		return this.userDao.listOrderByCopperDesc(offset, size);
	}

	public User getByPlayerId(String playerId) {
		User user = this.userDao.getByPlayerId(playerId);

		if (user == null) {
			throw new ServiceException(LOAD_USER_NOT_EXIST, "用户不存在.playerId[" + playerId + "]");
		}

		return user;
	}

	@Override
	public void checkUsername(String username) {

		if (StringUtils.isBlank(username)) {
			throw new ServiceException(NICKNAME_IS_NULL, "昵称为空.username[" + username + "]");
		} else if (Constant.getBytes(username) > 12) {
			throw new ServiceException(NICKNAME_LENGTH_ILLEGAL, "昵称长度非法.username[" + username + "]");
		} else if (IllegalWordUtills.hasIllegalCharacters(username)) {
			throw new ServiceException(NICKNAME_CHARACTER_ILLEGAL, "用户名只能包含中文、英文和数字");
		} else if (IllegalWordUtills.hasIllegalWords(username)) {
			throw new ServiceException(NICKNAME_WORD_ILLEGAL, "昵称包含非法文字.username[" + username + "]");
		}
		User user = this.userDao.getByName(username);
		if (user != null) {
			throw new ServiceException(NICKNAME_EXIST, "用户名已经存在.username[" + username + "]");
		}

		if (systemBadwordDao.get(username) != null) {
			throw new ServiceException(NICKNAME_WORD_ILLEGAL, "昵称包含非法文字.username[" + username + "]");
		}

		RobotUser robotUser = this.robotService.getByName(username);
		if (robotUser != null) {
			throw new ServiceException(NICKNAME_EXIST, "用户名已经存在.username[" + username + "]");
		}
	}

	@Override
	public boolean create(String userId, int systemHeroId, String username, EventHandle handle) {

		checkUsername(username);
		UserValid.isValidInitHeroId(systemHeroId);

		UserMapper userMapper = this.userMapperDao.get(userId);

		if (userMapper == null) {
			logger.error("用户创建失败,usermapper表没记录.userId[" + userId + "], systemHeroId[" + systemHeroId + "], username[" + username + "]");
			throw new ServiceException(ServiceReturnCode.FAILD, "用户创建失败.userId[" + userId + "]");
		}

		User user = this.userDao.get(userId);

		if (user != null) {
			throw new ServiceException(CREATE_USER_EXIST, "用户已经存在.userId[" + userId + "]");
		}

		boolean success = this.addUser(userMapper.getServerId(), userId, systemHeroId, username);

		// 创建扩展信息
		UserExtinfo userExtinfo = new UserExtinfo();
		userExtinfo.setEquipMax(InitDefine.EQUIP_BAG_INIT);
		userExtinfo.setHeroMax(InitDefine.HERO_BAG_INIT);
		userExtinfo.setLastBuyCopperTime(new Date());
		userExtinfo.setBuyCopperTimes(0);
		userExtinfo.setUserId(userId);
		userExtinfo.setRecordGuideStep("0");
		userExtinfo.setUserNation(systemHeroId);
		this.userExtinfoDao.add(userExtinfo);

		if (!success) {
			logger.error("用户创建失败.userId[" + userId + "], systemHeroId[" + systemHeroId + "], username[" + username + "]");
			throw new ServiceException(ServiceReturnCode.FAILD, "用户创建失败.userId[" + userId + "]");
		}

		// 给两个普通武将
		this.heroService.addUserHero(userId, IDGenerator.getID(), InitDefine.INIT_USER_HERO_ID1, 2, ToolUseType.ADD_INIT_GIVE_HERO);

		//
		// this.heroService.addUserHero(userId, IDGenerator.getID(),
		// InitDefine.INIT_USER_HERO_ID2, 5, ToolUseType.ADD_INIT_GIVE_HERO);

		sceneService.openAfterForces(userId, 0);

		// 创建酒馆信息
		Date now = new Date();

		UserTavern userTavern = new UserTavern();
		userTavern.setType(TavernConstant.DRAW_TYPE_1);
		userTavern.setUserId(userId);
		userTavern.setCreatedTime(now);
		userTavern.setUpdatedTime(DateUtils.add(now, Calendar.MILLISECOND, -1 * TavernConstant.DRAW_TYPE_1_CD_TIME));
		this.userTavernDao.add(userTavern);

		userTavern = new UserTavern();
		userTavern.setType(TavernConstant.DRAW_TYPE_2);
		userTavern.setUserId(userId);
		userTavern.setCreatedTime(now);
		userTavern.setUpdatedTime(DateUtils.add(now, Calendar.MILLISECOND, -1 * (2 * 60 * 60 * 24 * 1000)));
		this.userTavernDao.add(userTavern);

		// userTavern = new UserTavern();
		// userTavern.setType(TavernConstant.DRAW_TYPE_3);
		// userTavern.setUserId(userId);
		// userTavern.setCreatedTime(now);
		// userTavern.setUpdatedTime(DateUtils.add(now, Calendar.MILLISECOND, -1
		// * (TavernConstant.DRAW_TYPE_3_CD_TIME - 15 * 60 * 1000)));
		// this.userTavernDao.add(userTavern);

		return success;

	}

	/**
	 * 添加用户
	 * 
	 * @param userId
	 * @param systemHeroId
	 * @param username
	 * @return
	 */

	private boolean addUser(String serverId, String userId, int systemHeroId, String username) {

		User user = new User();

		Date now = new Date();

		long lodoId = this.getLdid(serverId);
		long fullLodoId = LodoIDHelper.getLodoId(serverId, lodoId);

		user.setLodoId(fullLodoId);
		user.setUserId(userId);
		user.setCopper(InitDefine.INIT_USER_COPPER);
		user.setExp(InitDefine.INIT_USER_EXP);
		user.setGoldNum(InitDefine.INIT_USER_GOLD);
		user.setLevel(InitDefine.INIT_USER_LEVEL);
		user.setPower(InitDefine.INIT_USER_POWER);
		user.setMuhon(InitDefine.INIT_USER_MUHON);
		user.setRegTime(now);
		user.setUpdatedTime(now);
		user.setUsername(username);
		user.setPowerAddTime(now);
		user.setVipLevel(InitDefine.INIT_USER_VIP);
		user.setImgId(RegHelper.getUserImgId(systemHeroId));

		try {
			return this.userDao.add(user);
		} catch (DuplicateKeyException e) {
			throw new ServiceException(NICKNAME_EXIST, "用户名重复");
		}

	}

	private synchronized long getLdid(String serverId) {

		int id = 1;
		String key = LodoIDHelper.getIdSaveKey(serverId);
		RuntimeData rumtimeData = this.rumtimeDataDao.get(key);
		if (rumtimeData == null) {
			rumtimeData = new RuntimeData();
			rumtimeData.setValueKey(key);
			rumtimeData.setValue(id);
			rumtimeData.setCreatedTime(new Date());
			this.rumtimeDataDao.add(rumtimeData);
		} else {
			id = rumtimeData.getValue() + 1;
			this.rumtimeDataDao.inc(key);
		}

		return id;
	}

	@Override
	public void returnPower(String userId, int power, int powerUseType) {
		boolean success = this.addPower(userId, power, powerUseType, null);
		logger.debug("返还体力.userId[" + userId + "], success[" + success + "]");
	}

	@Override
	public int resumePower(String userId, EventHandle handle) {

		User user = this.get(userId);

		int power = user.getPower();

		int maxTimes = this.vipService.getBuyPowerLimit(user.getVipLevel());

		int times = 1;
		UserExtinfo userExtinfo = this.userExtinfoDao.get(userId);
		if (userExtinfo != null && DateUtils.isSameDay(userExtinfo.getLastBuyPowerTime(), new Date())) {
			times = userExtinfo.getBuyPowerTimes() + 1;
		}

		if (times > maxTimes) {
			String message = "恢复体力出错，超过恢复次数限制.userId[" + userId + "], times[" + times + "], maxTimes[" + maxTimes + "], vipLevel[" + user.getVipLevel() + "]";
			logger.debug(message);
			throw new ServiceException(RESUME_POWER_OVER_MAX_TIMES, message);
		}

		int needMoney = this.priceService.getPrice(PriceType.BUY_POWER, times);
		if (!this.reduceGold(userId, needMoney, ToolUseType.REDUCE_RESUMT_POWER)) {
			String message = "恢复体力出错,金币不足.userId[" + userId + "], power[" + power + "], needMoney[" + needMoney + "]";
			logger.debug(message);
			throw new ServiceException(RESUME_POWER_NOT_ENOUGH_GOLD, message);
		}

		int addPower = 120;

		boolean success = this.addPower(userId, addPower, ToolUseType.ADD_GOLD_RESUME, null);

		// 活跃度任务
		activityTaskService.updateActvityTask(userId, ActivityTargetType.BUY_POWER, 1);

		// 更新购买银币记录
		if (userExtinfo != null) {
			this.userExtinfoDao.updateBuyPowerTimes(userId, times);
		} else {
			userExtinfo = new UserExtinfo();
			userExtinfo.setEquipMax(InitDefine.EQUIP_BAG_INIT);
			userExtinfo.setHeroMax(InitDefine.HERO_BAG_INIT);
			userExtinfo.setBuyPowerTimes(times);
			userExtinfo.setUserId(userId);
			this.userExtinfoDao.add(userExtinfo);
		}

		UserInfoUpdateEvent userInfoUpdateEvent = new UserInfoUpdateEvent(userId);
		handle.handle(userInfoUpdateEvent);

		logger.debug("恢复体力.userId[" + userId + "], success[" + success + "]");

		return times;
	}

	public boolean addCopper(String userId, int amount, int useType) {
		logger.debug("增加银币.userId[" + userId + "], amount[" + amount + "], useType[" + useType + "]");
		boolean success = false;
		try {
			success = this.userDao.addCopper(userId, amount);

			int copperNum = (int) (get(userId).getCopper() + amount);
			eventService.addCopperUpdateEvent(userId, copperNum);
		} finally {
			unSynLogService.copperLog(userId, useType, amount, 1, success);
		}

		return success;
	}

	public boolean addGold(String userId, int amount, int useType, int userLevel) {

		logger.debug("增加金币.userId[" + userId + "], amount[" + amount + "], useType[" + useType + "]");
		if (amount <= 0) {
			return false;
		}

		User user = this.get(userId);
		long beforeAmount = user.getGoldNum();

		boolean success = false;
		try {
			success = this.userDao.addGold(userId, amount);
		} finally {

			user = this.get(userId);
			long afterAmount = user.getGoldNum();

			unSynLogService.goldLog(userId, useType, amount, 1, success, beforeAmount, afterAmount);

		}

		return success;
	}

	@Override
	public boolean buyCopper(String userId) {

		UserExtinfo userExtinfo = this.userExtinfoDao.get(userId);
		User user = this.get(userId);

		Date now = new Date();

		int times = 1;

		if (userExtinfo != null) {

			if (DateUtils.isSameDay(now, userExtinfo.getLastBuyCopperTime())) {
				times += userExtinfo.getBuyCopperTimes();
			}
		}

		Map<String, Integer> copperInfo = CopperHelper.getCopperInfo(times, user.getLevel());
		int needMoney = copperInfo.get("ncm");
		int gainCopper = copperInfo.get("nco");

		// 扣金币
		if (!this.reduceGold(userId, needMoney, ToolUseType.REDUCE_BUY_COPPER)) {
			String message = "购买银币出错，用户金币不足.userId[" + userId + "], needMoney[" + needMoney + "]";
			logger.info(message);
			throw new ServiceException(BUY_COPPER_NOT_ENOUGH_GOLD, message);
		}

		// 更新购买银币记录
		if (userExtinfo != null) {
			this.userExtinfoDao.updateBuyCopperTimes(userId, times);
		} else {
			userExtinfo = new UserExtinfo();
			userExtinfo.setEquipMax(InitDefine.EQUIP_BAG_INIT);
			userExtinfo.setHeroMax(InitDefine.HERO_BAG_INIT);
			userExtinfo.setLastBuyCopperTime(now);
			userExtinfo.setBuyCopperTimes(times);
			userExtinfo.setUserId(userId);
			this.userExtinfoDao.add(userExtinfo);
		}

		// 给银币
		if (!this.addCopper(userId, gainCopper, ToolUseType.ADD_BUY_COPPER)) {
			String message = "购买银币出错，加银币时失败.userId[" + userId + "]";
			logger.info(message);
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

		this.pushUser(userId);

		return true;
	}

	public boolean reduceGold(String userId, int amount, int useType) {
		logger.debug("消费金币.userId[" + userId + "], amount[" + amount + "], useType[" + useType + "]");
		if (amount <= 0) {
			return false;
		}

		User user = this.get(userId);
		long beforeAmount = user.getGoldNum();

		boolean success = false;
		try {
			success = this.userDao.reduceGold(userId, amount);
		} finally {

			user = this.get(userId);
			long afterAmount = user.getGoldNum();

			unSynLogService.goldLog(userId, useType, amount, -1, success, beforeAmount, afterAmount);

		}

		return success;
	}

	public boolean addExp(String userId, int exp, int useType) {

		logger.debug("增加经验.userId[" + userId + "], exp[" + exp + "], useType[" + useType + "]");
		User user = this.userDao.get(userId);
		if (user == null) {
			String message = "更新用户经验失败,用户不存在.userId[" + userId + "], exp[" + exp + "]";
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

		SystemUserLevel systemUserLevel = this.systemUserLevelDao.getUserLevel(user.getExp() + exp);

		int level = configDataDao.getInt(ConfigKey.USER_MAX_LEVEL, 80);

		if (systemUserLevel != null) {
			level = systemUserLevel.getUserLevel();
		}

		final int userLevel = level;

		int power = 0;
		if (level > user.getLevel()) {

			power = user.getPower() + systemUserLevel.getLevelUpAddPower() * (level - user.getLevel());

			userArenaInfoDao.updateUserLevel(userId, userLevel);

			Event event = new UserLevelUpEvent(userId, userLevel);
			eventService.dispatchEvent(event);

			unSynLogService.levelUpLog(userId, (int) user.getExp(), userLevel);
		}

		boolean success = false;

		success = this.userDao.addExp(userId, exp, level, power);

		unSynLogService.toolLog(userId, ToolType.EXP, 0, exp, useType, 1, "", success);

		return success;
	}

	@Override
	public boolean reduceCopper(String userId, int amount, int useType) {

		logger.debug("消费银币.userId[" + userId + "], amount[" + amount + "], useType[" + useType + "]");
		boolean success = false;

		success = this.userDao.reduceCopper(userId, amount);
		unSynLogService.copperLog(userId, useType, amount, -1, success);

		return success;
	}

	@Override
	public boolean addPower(String userId, int power, int useType, Date powerAddTime) {
		User user = this.get(userId);
		// int maxPower = this.vipService.getPowerMax(user.getVipLevel());
		return this.addPower(userId, power, user.getPower() + power, useType, powerAddTime);
	}

	@Override
	public boolean addPower(String userId, int power, int maxPower, int useType, Date powerAddTime) {

		logger.debug("增加体力.userId[" + userId + "], power[" + power + "], useType[" + useType + "], powerAddTime[" + powerAddTime + "]");
		boolean success = false;
		success = this.userDao.addPower(userId, power, maxPower, powerAddTime);

		// log
		if (power > 0) {
			unSynLogService.toolLog(userId, ToolType.POWER, 0, power, useType, 1, "", success);
		}

		return success;
	}

	@Override
	public boolean reducePower(String userId, int power, int useType) {

		logger.debug("减少体力.userId[" + userId + "], power[" + power + "], useType[" + useType + "]");
		boolean success = false;

		success = this.userDao.reducePowre(userId, power);
		unSynLogService.toolLog(userId, ToolType.POWER, 0, power, useType, -1, "", success);

		return success;
	}

	@Override
	public void pushUser(String userId) {

		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", userId);

		Command command = new Command();
		command.setCommand(CommandType.COMMAND_PUSH_USER_INFO);
		command.setType(CommandType.PUSH_USER);
		command.setParams(params);

		commandDao.add(command);

	}

	@Override
	public void login(final String userId, String userIp) {

		User user = userDao.get(userId);
		Date now = new Date();

		/*
		 * 判断用户是否已经被封号 如果还在封号时间内，返回异常，
		 */
		Date dueTime = user.getDueTime();
		if (dueTime != null && now.before(dueTime)) {
			throw new ServiceException(LOGIN_USER_BANNED, "用户已经被封号.userId[" + userId + "]");
		}

		// 添加新的登录日志
		UserOnlineLog userOnlineLog = new UserOnlineLog();
		userOnlineLog.setUserId(userId);
		userOnlineLog.setLoginTime(now);
		userOnlineLog.setLogoutTime(DateUtils.add(now, Calendar.SECOND, 1));
		userOnlineLog.setUserIp(userIp);
		this.userOnlineLogDao.add(userOnlineLog);

		// 清下以前的缓存
		this.userDao.cleanCacheData(userId);

		InitCacheManager.getInstance().executeUserLogin(userId);

		UserLoginLog loginLog = userLoginLogDao.get(userId);

		if (loginLog == null) {
			loginLog = new UserLoginLog(userId);
			userLoginLogDao.add(loginLog);
		} else {

			String date = loginLog.getLastLoginDate();
			Date lastLoginDate = DateUtils.str2Date(date, "yyyy-MM-dd");
			int dayDiff = DateUtils.getDayDiff(lastLoginDate, now);

			if (dayDiff != 0) {
				updateLoginLog(dayDiff, loginLog);
			}
		}

		// 广播用户登录事件
		Event event = new LoginEvent(userId);

		eventService.dispatchEvent(event);

	}

	private void updateLoginLog(int dayDiff, UserLoginLog loginLog) {
		String currentDate = DateUtils.getDate();
		int day = loginLog.getDay();
		String recStatus = loginLog.getRecStatus();

		if (dayDiff == 1) {
			day += 1;
		} else if (dayDiff > 1) {
			day = 1;
			recStatus = "";
		}

		if (day <= 6) {
			recStatus += "0,";
			loginLog.setRecStatus(recStatus);
		}

		loginLog.setDay(day);
		loginLog.setRecToday(0);
		loginLog.setLastLoginDate(currentDate);

		userLoginLogDao.update(loginLog);

	}

	@Override
	public void logout(String userId) {

		UserOnlineLog lastUserOnlineLog = this.userOnlineLogDao.getLastOnlineLog(userId);
		if (lastUserOnlineLog != null) {
			UserExtinfo userExtinfo = this.userExtinfoDao.get(userId);
			User user = this.userDao.get(userId);
			this.userOnlineLogDao.updateLogoutTime(userId, lastUserOnlineLog.getLogId(), new Date(), user.getLevel(), userExtinfo.getRecordGuideStep());
		}

		// 清除缓存数据
		this.userDao.cleanCacheData(userId);
		// 执行缓存管理器的用户登出操作
		ClearCacheManager.getInstance().executeUserLogOut(userId);
		// 删除数量为0的用户道具
		this.userToolDao.deleteZeroNumTools(userId);

		LogoutEvent logoutEvent = new LogoutEvent(userId);
		eventService.dispatchEvent(logoutEvent);
	}

	public void init() {

	}

	@Override
	public void checkUserHeroBagLimit(String userId) {
		UserExtinfo userExtinfo = this.userExtinfoDao.get(userId);
		int bagMax = InitDefine.HERO_BAG_INIT;
		if (userExtinfo != null) {
			bagMax = userExtinfo.getHeroMax();
		}

		int userHeroCount = this.heroService.getUserHeroCount(userId);
		if (userHeroCount >= bagMax) {
			throw new ServiceException(ServiceReturnCode.USER_HERO_BAG_LIMIT_EXCEED, "武将背包超过上限.userId[" + userId + "]");
		}
	}

	@Override
	public void checkUserEquipBagLimit(String userId) {
		UserExtinfo userExtinfo = this.userExtinfoDao.get(userId);
		int bagMax = InitDefine.EQUIP_BAG_INIT;
		if (userExtinfo != null) {
			bagMax = userExtinfo.getEquipMax();
		}

		int userEquipCount = this.equipService.getUserEquipCount(userId);
		if (userEquipCount >= bagMax) {
			throw new ServiceException(ServiceReturnCode.USER_EQUIP_BAG_LIMIT_EXCEED, "装备背包超过上限.userId[" + userId + "]");
		}
	}

	@Override
	public void updateVipLevel(String userId) {

		User user = this.get(userId);

		int payAmount = this.paymentLogDao.getPaymentTotalGold(userId);
		SystemVipLevel systemVipLevel = this.systemVipLevelDao.getBuyMoney(payAmount);

		int vipLevel = systemVipLevel.getVipLevel();

		UserExtinfo userExtinfo = this.userExtinfoDao.get(userId);
		if (vipLevel < userExtinfo.getRewardVipLevel()) {
			vipLevel = userExtinfo.getRewardVipLevel();
		}

		this.userDao.updateVIPLevel(userId, vipLevel);

		eventService.addVipLevelEvent(userId, vipLevel);

		if (vipLevel >= 7 && user.getVipLevel() != vipLevel) {
			messageService.sendVipLevelMsg(userId, user.getUsername(), vipLevel);
		}

	}

	@Override
	public UserMapper getUserMapper(String userId) {
		return userMapperDao.get(userId);
	}

	@Override
	public boolean saveGuideStep(String userId, int step, String ip) {
		return this.userExtinfoDao.updateGuideStep(userId, step);
	}

	@Override
	public boolean recordGuideStep(String userId, int guideStep, String ip) {

		UserExtinfo info = userExtinfoDao.get(userId);

		if (info == null) {
			return false;
		}

		String oldStep = info.getRecordGuideStep();
		if (oldStep == null || oldStep.equals("")) {
			oldStep = "0";
		}
		String newStep = oldStep + "," + String.valueOf(guideStep);

		String[] datas = newStep.split(",");
		Set<String> set = new HashSet<String>();
		for (String d : datas) {
			set.add(d);
		}

		return userExtinfoDao.recordGuideStep(userId, StringUtils.join(set, ","));
	}

	@Override
	public boolean isOnline(String userId) {
		return userDao.isOnline(userId);
	}

	@Override
	public boolean addUserHeroBag(String userId, int equipMax) {
		return userExtinfoDao.updateHeroMax(userId, equipMax);
	}

	@Override
	public boolean addUserEquipBag(String userId, int equipMax) {
		return userExtinfoDao.updateEquipMax(userId, equipMax);
	}

	@Override
	public int getUserPower(List<UserHeroBO> userHeros) {
		int power = 0;
		if (userHeros != null) {
			for (UserHeroBO hero : userHeros) {
				power += hero.getPhysicalAttack() + hero.getPhysicalDefense() + hero.getLife();
			}
		}
		return power;
	}

	@Override
	public boolean checkUserGoldGainLimit(String userId, int goldNum) {
		int userDailyGainGold = this.userDailyGainLogDao.getUserDailyGain(userId, UserDailyGainLogType.GOLD);

		int gainLimit = this.configDataDao.getInt(ConfigKey.USER_DAILY_GAIN_GOLD_LIMIT, 50);

		// 金币掉落超过上限
		if (userDailyGainGold + goldNum >= gainLimit) {
			return false;
		} else {
			this.userDailyGainLogDao.addUserDailyGain(userId, UserDailyGainLogType.GOLD, goldNum);
			return true;
		}
	}

	@Override
	public List<String> getAllUserIds() {
		return userDao.getAllUserIds();
	}

	@Override
	public boolean reduceExp(String userId, int amount, int useType) {

		logger.debug("消费经验.userId[" + userId + "], amount[" + amount + "], useType[" + useType + "]");
		if (amount <= 0) {
			return false;
		}
		boolean success = false;

		success = this.userDao.reduceExp(userId, amount);

		this.unSynLogService.toolLog(userId, ToolType.EXP, 0, amount, useType, -1, "", success);

		return success;
	}

	@Override
	public void checkCode(int code) {
		User user = userDao.getByPlayerId(String.valueOf(code));

		// 不存在邀请码，抛出异常
		if (user == null) {
			String message = "邀请码不存在 code[" + code + "]";
			logger.error(message);
			throw new ServiceException(CODE_NOT_EXIST, message);
		}

	}

	@Override
	public boolean banUser(String userId, String dueTime) {
		Date date = DateUtils.str2Date(dueTime);
		return userDao.banUser(userId, date);
	}

	@Override
	public boolean assignVipLevel(String userId, final int vipLevel, boolean force) {

		UserExtinfo userExtinfo = this.userExtinfoDao.get(userId);
		User user = this.get(userId);
		if (userExtinfo.getRewardVipLevel() < vipLevel || force) {
			this.userExtinfoDao.updateRewardVipLevel(userId, vipLevel);
		}

		if (user.getVipLevel() < vipLevel || force) {
			this.updateVipLevel(userId);
		}

		return true;
	}

	@Override
	public boolean reduceReputation(String userId, int amount, int useType) {
		logger.debug("消费声望.userId[" + userId + "], amount[" + amount + "], useType[" + useType + "]");
		if (amount <= 0) {
			return false;
		}
		boolean success = false;

		success = this.userDao.reduceReputation(userId, amount);
		// 日志
		unSynLogService.toolLog(userId, ToolType.REPUTATION, 0, amount, useType, -1, "", success);

		return success;
	}

	@Override
	public boolean addReputation(String userId, int amount, int useType) {

		logger.debug("添加声望.userId[" + userId + "], amount[" + amount + "], useType[" + useType + "]");
		if (amount <= 0) {
			return false;
		}
		boolean success = false;

		success = this.userDao.addReputation(userId, amount);
		// 日志
		unSynLogService.toolLog(userId, ToolType.REPUTATION, 0, amount, useType, 1, "", success);

		return success;
	}

	@Override
	public boolean updateUserLevel(String userId, int level, int exp) {
		userArenaInfoDao.updateUserLevel(userId, level);
		return this.userDao.updateUserLevel(userId, level, exp);
	}

	@Override
	public User getUserByUserName(String username) {
		User user = this.userDao.getByName(username);
		if (user != null) {
			return this.get(user.getUserId());
		}
		return null;
	}

	@Override
	public boolean bannedToPost(String userId) {
		return this.userDao.bannedToPost(userId);
	}

	@Override
	public int getImgId(String userId) {

		User user = this.get(userId);
		if (user != null) {
			return user.getImgId();
		}
		return 0;
	}

	@Override
	public User getRandomUserFromDB() {
		// TODO
		RuntimeData runtimeData = runtimeDataDao.get("lodo_id_s1");
		int totalUserNum = runtimeData.getValue();
		long lodoId = 0;

		while (true) {
			try {
				int randomNum = RandomUtils.nextInt(totalUserNum);
				lodoId = LodoIDHelper.getLodoId("s1", randomNum);
				User user = this.getByPlayerId(String.valueOf(lodoId));
				return user;
			} catch (Exception e) {
				logger.debug("用户不存在， lodoId[" + lodoId + "]");
			}
		}

	}

	@Override
	public SystemHero getSystemHero(String userId) {

		long start = System.currentTimeMillis();

		List<UserHeroBO> userHeroList = this.heroService.getUserHeroList(userId, 1);
		if (userHeroList == null || userHeroList.size() == 0) {
			userHeroList = robotService.getRobotUserHeroBOList(userId);
		}
		if (!userHeroList.isEmpty()) {
			int systemHeroId = userHeroList.get(0).getSystemHeroId();
			SystemHero systemHero = this.heroService.getSysHero(systemHeroId);

			logger.debug("get user img.time[" + (System.currentTimeMillis() - start) + "]");

			return systemHero;
		} else {
			logger.debug("用户一个武将上阵武将都没有.userId[" + userId + "]");
		}

		return null;
	}

	@Override
	public boolean reduceMuhon(String userId, int amount, int useType) {

		boolean success = userDao.reduceMuhon(userId, amount);
		this.unSynLogService.toolLog(userId, ToolType.MUHON, 0, amount, useType, -1, "", success);

		return success;
	}

	// 消耗魂力
	public boolean reduceSoul(String userId, int amount, int useType) {

		boolean success = userDao.reduceSoul(userId, amount);
		this.unSynLogService.toolLog(userId, ToolType.SOUL, 0, amount, useType, -1, "", success);

		return success;
	}

	// 消耗精元
	public boolean reduceEnergy(String userId, int amount, int useType) {

		boolean success = userDao.reduceEnergy(userId, amount);
		this.unSynLogService.toolLog(userId, ToolType.ENERGY, 0, amount, useType, -1, "", success);

		return success;
	}

	public boolean reduceCoin(String userId, int amount, int useType) {
		boolean success = userDao.reduceCoin(userId, amount);
		this.unSynLogService.toolLog(userId, ToolType.COIN, 0, amount, useType, -1, "", success);
		return success;
	}

	@Override
	public boolean addMuhon(String userId, int amount, int useType) {

		boolean success = userDao.addMuhon(userId, amount);
		int muhon = (int) get(userId).getMuhon() + amount;
		eventService.addMuhonUpdateEvent(userId, muhon);

		this.unSynLogService.toolLog(userId, ToolType.MUHON, 0, amount, useType, 1, "", success);

		return success;
	}

	public boolean addSoul(String userId, int amount, int useType) {

		boolean success = userDao.addSoul(userId, amount);
		this.unSynLogService.toolLog(userId, ToolType.SOUL, 0, amount, useType, 1, "", success);

		return success;
	}

	public boolean addCoin(String userId, int amount, int useType) {
		boolean success = userDao.addCoin(userId, amount);
		this.unSynLogService.toolLog(userId, ToolType.COIN, 0, amount, useType, 1, "", success);
		return success;
	}

	@Override
	public List<Integer> checkNotification(String userId) {
		List<Integer> notifications = new ArrayList<Integer>();

		checkRecPower(userId, notifications);

		checkOncePayReward(userId, notifications);

		checkTotalPayReward(userId, notifications);

		checkTavernFreeDraw(userId, notifications);

		checkFriendMessage(userId, notifications);

		checkLoginReward(userId, notifications);

		return notifications;
	}

	private void checkLoginReward(String userId, List<Integer> notifications) {
		// 30天登录
		UserLoginRewardInfo lastUserLoginRewardInfo = this.userLoginRewardInfoDao.getUserLastLoginRewardInfo(userId);
		if (lastUserLoginRewardInfo != null && lastUserLoginRewardInfo.getRewardStatus() == ActivityService.LOGIN_REWARD_NOT_GET) {
			notifications.add(NotificationType.LOGIN_REWARD_30);
		}

		UserLoginLog loginLog = userLoginLogDao.get(userId);
		if (loginLog.getRecToday() == 0) {
			notifications.add(NotificationType.LOGIN_REWARD_7);
		}

	}

	private void checkFriendMessage(String userId, List<Integer> notifications) {
		List<UserFriendRequest> requestList = userFriendRequestDao.getByStatus(userId, UserFriendConstant.REQUEST_STATUS_NEW);
		if (requestList != null && requestList.size() != 0) {
			notifications.add(NotificationType.FRIEND_REQUEST);
		}

		// List<UserPersonalMail> newMails =
		// userPersonalMailDao.getNewMails(userId);
		// if (newMails != null && newMails.size() != 0) {
		// notifications.add(NotificationType.FRIND_NEW_PERSONAL_MAIL);
		// }

	}

	/**
	 * 检查是否可以免费抽奖
	 */
	private void checkTavernFreeDraw(String userId, List<Integer> notifications) {
		UserTavern userTavern1 = tavernService.getUserTavern(userId, TavernConstant.DRAW_TYPE_1);
		UserTavern userTavern2 = tavernService.getUserTavern(userId, TavernConstant.DRAW_TYPE_2);
		// 广交豪杰是否免抽
		boolean boolean1 = (userDailyGainLogDao.getUserDailyGain(userId, UserDailyGainLogType.TAVERN_TYPE_1) < 3) && tavernService.isCoolTimeFinished(userTavern1);
		// 大摆延期是否免抽
		boolean boolean2 = tavernService.isCoolTimeFinished(userTavern2);
		if (boolean1 || boolean2) {
			notifications.add(NotificationType.TAVERN);
		}

		User user = this.get(userId);

		if (activityService.canReceiveVipGiftBag(userId) == true) {
			notifications.add(NotificationType.VIP_GIFT_BAG);
		}
	}

	private void checkTotalPayReward(String userId, List<Integer> notifications) {
		List<UserPayRewardBO> totalPayRewards = this.activityService.getUserTotalPayRewardList(userId);
		if (totalPayRewards != null) {
			for (UserPayRewardBO bo : totalPayRewards) {
				if (bo.getStatus() == UserPayRewardStatus.CAN_RECEIVE && bo.getTimes() < bo.getTimesLimit()) {
					notifications.add(NotificationType.TOTAL_PAY_REWARD);
					break;
				}
			}
		}
	}

	private void checkOncePayReward(String userId, List<Integer> notifications) {
		List<UserPayRewardBO> oncePayRewards = this.activityService.getUserOncePayRewardList(userId);
		if (oncePayRewards != null) {
			for (UserPayRewardBO bo : oncePayRewards) {
				if (bo.getTimes() < bo.getTimesLimit()) {
					notifications.add(NotificationType.ONCE_PAY_REWARD);
					break;
				}
			}
		}
	}

	private void checkRecPower(String userId, List<Integer> notifications) {
		List<SystemRecivePower> receivePowers = systemRecivePowerDao.getList();
		for (SystemRecivePower receivePower : receivePowers) {
			int amount = this.userDailyGainLogDao.getUserDailyGain(userId, receivePower.getType());

			if (ActivityHelper.isNowCanRecive(receivePower) && amount < ActivityService.RECEIVE_POWER_NUM) {
				notifications.add(NotificationType.RECEIVE_POWER);
				break;
			}
		}
	}

	@Override
	public boolean addMingwen(String userId, int amount, int useType) {
		boolean success = userDao.addMingwen(userId, amount);
		this.unSynLogService.toolLog(userId, ToolType.MINGWEN, 0, amount, useType, 1, "", success);
		return success;
	}

	@Override
	public boolean reduceMingwen(String userId, int amount, int useType) {
		boolean success = userDao.reduceMingwen(userId, amount);
		this.unSynLogService.toolLog(userId, ToolType.MINGWEN, 0, amount, useType, -1, "", success);
		return success;
	}

	@Override
	public int getUserMaxPower(String userId) {
		return userDao.getUserMaxPower(userId);
	}

	@Override
	public boolean addHonour(String userId, int amount, int useType) {
		boolean success = userDao.addHonour(userId, amount);
		this.unSynLogService.toolLog(userId, ToolType.HONOUR, 0, amount, useType, 1, "", success);
		return success;
	}

	@Override
	public boolean reduceHonour(String userId, int amount, int useType) {
		boolean success = userDao.reduceHonour(userId, amount);
		this.unSynLogService.toolLog(userId, ToolType.HONOUR, 0, amount, useType, -1, "", success);
		return success;
	}

	@Override
	public boolean addSkill(String userId, int amount, int useType) {
		boolean success = userDao.addSkill(userId, amount);
		this.unSynLogService.toolLog(userId, ToolType.HONOUR, 0, amount, useType, -1, "", success);
		return false;
	}

}
