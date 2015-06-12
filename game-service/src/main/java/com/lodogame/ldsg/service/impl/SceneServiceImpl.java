package com.lodogame.ldsg.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.CommandDao;
import com.lodogame.game.dao.ForcesDropToolDao;
import com.lodogame.game.dao.SystemForcesDao;
import com.lodogame.game.dao.SystemSceneDao;
import com.lodogame.game.dao.SystemUserLevelDao;
import com.lodogame.game.dao.UserForcesDao;
import com.lodogame.game.dao.UserSceneDao;
import com.lodogame.game.dao.UserSweepInfoDao;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.ldsg.bo.BattleBO;
import com.lodogame.ldsg.bo.BattleHeroBO;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.DropToolBO;
import com.lodogame.ldsg.bo.ForcesStarRewardBO;
import com.lodogame.ldsg.bo.SweepInfoBO;
import com.lodogame.ldsg.bo.UserForcesBO;
import com.lodogame.ldsg.bo.UserSceneBO;
import com.lodogame.ldsg.constants.ActivityTargetType;
import com.lodogame.ldsg.constants.BattleType;
import com.lodogame.ldsg.constants.CommandType;
import com.lodogame.ldsg.constants.ForcesStatus;
import com.lodogame.ldsg.constants.ForcesType;
import com.lodogame.ldsg.constants.RankType;
import com.lodogame.ldsg.constants.ScenePassStarReward;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.constants.SweepStatus;
import com.lodogame.ldsg.constants.ToolId;
import com.lodogame.ldsg.constants.ToolType;
import com.lodogame.ldsg.constants.ToolUseType;
import com.lodogame.ldsg.event.BaseEvent;
import com.lodogame.ldsg.event.BattleResponseEvent;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.ForcesEvent;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.helper.DropDescHelper;
import com.lodogame.ldsg.helper.ForcesDropHelper;
import com.lodogame.ldsg.service.ActivityService;
import com.lodogame.ldsg.service.ActivityTaskService;
import com.lodogame.ldsg.service.BattleService;
import com.lodogame.ldsg.service.EventService;
import com.lodogame.ldsg.service.ForcesService;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.SceneService;
import com.lodogame.ldsg.service.ToolService;
import com.lodogame.ldsg.service.UnSynLogService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.ldsg.service.VipService;
import com.lodogame.model.Command;
import com.lodogame.model.ForcesDropTool;
import com.lodogame.model.SystemForces;
import com.lodogame.model.SystemScene;
import com.lodogame.model.User;
import com.lodogame.model.UserForces;
import com.lodogame.model.UserScene;
import com.lodogame.model.UserSweepInfo;

public class SceneServiceImpl implements SceneService {

	private static final Logger logger = Logger.getLogger(SceneServiceImpl.class);

	@Autowired
	private CommandDao commandDao;

	@Autowired
	private UserSceneDao userSceneDao;

	@Autowired
	private UserForcesDao userForcesDao;

	@Autowired
	private SystemForcesDao systemForcesDao;

	@Autowired
	private SystemSceneDao systemSceneDao;

	@Autowired
	private ForcesService forcesService;

	@Autowired
	private HeroService heroService;

	@Autowired
	private BattleService battleService;

	@Autowired
	private ForcesDropToolDao forcesDropToolDao;

	@Autowired
	private ToolService toolService;

	@Autowired
	private UserService userService;

	@Autowired
	private ActivityService activityService;

	@Autowired
	private SystemUserLevelDao systemUserLevelDao;

	@Autowired
	private VipService vipService;

	@Autowired
	private ActivityTaskService activityTaskService;
	@Autowired
	public UnSynLogService unSynLogService;

	@Autowired
	private EventService eventServcie;

	@Autowired
	private UserSweepInfoDao userSweepInfoDao;

	private Map<String, List<String>> deadHeros = new ConcurrentHashMap<String, List<String>>();

	// 最大扫荡次数
	private static final int MAX_SWEEP_TIMES = Integer.MAX_VALUE;

	// 消除扫荡CD，每五分钟
	private static final int FIVE_MIN = 5;

	// 消除扫荡CD，每五分钟消耗的金币
	private static final int CLREAR_CD_COST_GOLD_EVERY_FIVE_MIN = 5;

	public List<UserSceneBO> getUserSceneList(String userId) {

		List<UserSceneBO> userSceneBOList = new ArrayList<UserSceneBO>();

		List<UserScene> userSceneList = this.userSceneDao.getUserSceneList(userId);

		for (UserScene userScene : userSceneList) {
			UserSceneBO userSceneBO = new UserSceneBO();
			userSceneBO.setUserId(userScene.getUserId());
			userSceneBO.setSceneId(userScene.getSceneId());
			userSceneBO.setPassFlag(userScene.getPassFlag());
			userSceneBO.setOneStarReward(userScene.getOneStarReward());
			userSceneBO.setTwoStarReward(userScene.getTwoStarReward());
			userSceneBO.setThirdStarReward(userScene.getThirdStarReward());

			List<UserForcesBO> userForcesList = forcesService.getUserForcesList(userId, userScene.getSceneId());
			userSceneBO.setForcesBOList(userForcesList);
			userSceneBOList.add(userSceneBO);
		}

		return userSceneBOList;
	}

	public boolean attack(String userId, int forcesId, final EventHandle handler) {

		final SystemForces systemForces = this.systemForcesDao.get(forcesId);
		final UserForces userForces = this.userForcesDao.get(userId, systemForces.getGroupId());

		logger.debug("userId[" + userId + "], forcesId[" + forcesId + "]");
		checkFightCondition(systemForces, userForces);

		// 如果攻打的是第一波怪物，则将这个用户标记为死亡的武将清空，即所有武将都可以参加战斗
		if (systemForces.getOrderInGroup() == 1) {
			List<String> list = deadHeros.get(userId);
			if (list != null) {
				list.clear();
			} else {
				list = new ArrayList<String>();
				deadHeros.put(userId, list);
			}
		}

		final BattleBO attack = createAttack(userId);
		BattleBO defense = createDefense(forcesId);

		battleService.fight(attack, defense, BattleType.SCENE, new EventHandle() {

			public boolean handle(Event battleResponse) {

				if (battleResponse instanceof BattleResponseEvent) {
					processFightResult(systemForces, userForces, attack, battleResponse);
					handler.handle(battleResponse);
				}
				return true;
			}
		});

		return true;
	}

	private void checkFightCondition(SystemForces systemForces, UserForces userForces) {
		String userId = userForces.getUserId();

		userService.checkUserHeroBagLimit(userId);
		userService.checkUserEquipBagLimit(userId);

		checkForcesOpen(userForces);
		checkAttackNumLimit(systemForces, userForces);
		systemForces = systemForcesDao.getLastForcesByGroupId(systemForces.getGroupId());
		checkUserPowerEnought(userId, systemForces);

	}

	/**
	 * 如果攻打的是活动副本中的怪物，则要判断活动副本是否到开放时间
	 */
	private void checkForcesOpen(UserForces userForces) {
		String userId = userForces.getUserId();
		int forcesGroup = userForces.getGroupId();
		int forcesType = userForces.getForcesType();
		int sceneId = userForces.getSceneId();

		if (forcesType == ForcesType.FORCES_TYPE_COPY && activityService.isForcesActivityOpen(sceneId) == false) {
			String message = "攻打怪物部队失败,副本活动未开放.userId[" + userId + "], forcesGroup[" + forcesGroup + "]";
			throw new ServiceException(ServiceReturnCode.ACTIVITY_NOT_OPEN_EXCTPION, message);
		}
	}

	private void processFightResult(SystemForces systemForces, UserForces userForces, BattleBO attacker, Event battleResponse) {
		String userId = userForces.getUserId();

		int flag = battleResponse.getInt("flag");

		List<String> deadHeroUhids = updateDeadHerosStatus(userId, attacker, battleResponse);

		if (userForces.getStatus() == ForcesStatus.STATUS_PASS) {
			battleResponse.setObject("ifs", 1);// 打赢过，可以跳过

		} else {
			battleResponse.setObject("ifs", 0);//
		}

		if (flag == 1) {// 打怪打赢了
			int passStar = handleForcesPass(userForces, systemForces);
			CommonDropBO forcesDropBO;
			if (systemForces.isLastForceInTheGroup()) {
				forcesDropBO = pickUpForcesDropToolList(userForces);
				// SceneOverEvent event = new SceneOverEvent(userId,
				// systemForces);
				// eventServcie.dispatchEvent(event);
			} else {
				forcesDropBO = new CommonDropBO();
			}
			battleResponse.setObject("forcesDropBO", forcesDropBO);
			battleResponse.setObject("ps", passStar);

			if (systemForces.isLastForceInTheGroup() == true) {
				reduceUserPower(userId, systemForces);
			}

			// 第一次打赢
			if (userForces.getStatus() != ForcesStatus.STATUS_PASS) {
				if (systemForces.getIsSceneLast() == 1) {
					int sceneId = systemForces.getSceneId();
					this.userSceneDao.updateScenePassed(userId, sceneId);
				}
			}

			ForcesEvent event = new ForcesEvent(userId, systemForces.getForcesId(), 1);
			eventServcie.dispatchEvent(event);
		}

		SystemScene systemScene = this.systemSceneDao.get(systemForces.getSceneId());

		battleResponse.setObject("bgid", systemScene.getImgId());
		battleResponse.setObject("dhl", deadHeroUhids);
		battleResponse.setObject("forcesName", systemForces.getForcesName());

	}

	/**
	 * 每场战斗结束后，如果武将剩余血量为0，要将武将状态标记为“死亡”
	 */
	@SuppressWarnings("unchecked")
	private List<String> updateDeadHerosStatus(String userId, BattleBO attacker, Event battleResponse) {

		Map<String, String> herosBattlePosition = attacker.getHerosBattlePosition();

		Map<String, Integer> herosBloodNum = (Map<String, Integer>) battleResponse.getObject("life");

		List<String> deadHeroUhids = deadHeros.get(userId);
		if (deadHeroUhids == null) {
			deadHeroUhids = new ArrayList<String>();
		}

		for (Entry<String, String> entry : herosBattlePosition.entrySet()) {
			String userHeroId = entry.getKey();
			String posKey = herosBattlePosition.get(userHeroId);

			// 如果根据武将位置拿不到武将血量，说明这个武将已经死亡了
			if (herosBloodNum.containsKey(posKey) == false) {
				deadHeroUhids.add(userHeroId);
			}
		}

		deadHeros.put(userId, deadHeroUhids);
		return deadHeroUhids;
	}

	/**
	 * 扣除攻打怪物时要消耗的体力
	 */
	private void reduceUserPower(String userId, SystemForces systemForces) {
		int forcesId = systemForces.getForcesId();
		int needPower = systemForces.getNeedPower();
		boolean isSuccess = this.userService.reducePower(userId, needPower, systemForces.getForcesType());

		if (isSuccess == false) {
			String message = "攻打怪物异常,更新用户体力出错.userId[" + userId + "], forcesId[" + forcesId + "]";
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}
	}

	private void checkUserPowerEnought(String userId, SystemForces systemForces) {
		int forcesId = systemForces.getForcesId();
		int needPower = systemForces.getNeedPower();
		final User user = this.userService.get(userId);
		if (user.getPower() < needPower) {
			String message = "攻打怪物出错，体力不足.userId[" + userId + "], forcesId[" + forcesId + "]";
			throw new ServiceException(ATTACK_FORCES_POWER_NOT_ENOUGH, message);
		}
	}

	/**
	 * 是否达到每日攻打这支部队的上限次数
	 */
	private void checkAttackNumLimit(SystemForces systemForces, UserForces userForces) {
		String userId = userForces.getUserId();

		boolean isBeyondAttackTimes = false;
		if (userForces.getTimes() >= systemForces.getTimesLimit() && systemForces.isAttackTimeUnlimted() == false) {
			isBeyondAttackTimes = true;
		}

		if (DateUtils.isSameDay(userForces.getUpdatedTime(), new Date()) && isBeyondAttackTimes == true) {
			String message = "攻打怪物部队失败,已经超过当天最大可攻打次数.userId[" + userId + "], forcesId[" + systemForces.getForcesId() + "]";
			throw new ServiceException(SceneService.ATTACK_BEYOND_ATTACK_TIME_LIMIT, message);
		}
	}

	private BattleBO createDefense(int forcesId) {
		BattleBO defense = new BattleBO();
		List<BattleHeroBO> battleHeroBOList = this.forcesService.getForcesHeroBOList(forcesId);

		defense.setBattleHeroBOList(battleHeroBOList);
		return defense;
	}

	private BattleBO createAttack(String userId) {

		List<String> deadHeroList = deadHeros.get(userId);

		BattleBO attack = new BattleBO();
		List<BattleHeroBO> heroBOList = this.heroService.getUserBattleHeroBOList(userId);

		Iterator<BattleHeroBO> iterator = heroBOList.iterator();
		while (iterator.hasNext()) {
			BattleHeroBO bo = iterator.next();
			if (deadHeroList.contains(bo.getUserHeroId())) {
				iterator.remove();
			}
		}
		attack.setBattleHeroBOList(heroBOList);
		return attack;
	}

	/**
	 * 给怪物部队掉落
	 * 
	 * @param userId
	 * @param forcesId
	 */
	public CommonDropBO pickUpForcesDropToolList(UserForces userForces) {
		String userId = userForces.getUserId();
		User user = this.userService.get(userId);
		int old = user.getLevel();
		CommonDropBO forcesDropBO = new CommonDropBO();

		getToolDrops(userForces, forcesDropBO, user);

		user = this.userService.get(userId);

		int news = user.getLevel();
		forcesDropBO.setLevelUp(news - old);
		return forcesDropBO;
	}

	private void getToolDrops(UserForces userForces, CommonDropBO forcesDropBO, User user) {
		String userId = userForces.getUserId();
		int forcesGroup = userForces.getGroupId();

		boolean isFirstForces = isFirstForcesUserAttack(userForces);
		int rand = RandomUtils.nextInt(10000);

		List<ForcesDropTool> forcesDropToolList = this.forcesDropToolDao.getForcesGroupDropToolList(forcesGroup);
		for (ForcesDropTool forcesDropTool : forcesDropToolList) {

			List<DropToolBO> dropToolBOList = this.pickUpForcesDropTool(userId, forcesDropTool, rand, isFirstForces);
			if (dropToolBOList == null) {
				continue;
			}

			for (DropToolBO dropToolBO : dropToolBOList) {
				this.toolService.appendToDropBO(userId, forcesDropBO, dropToolBO);
			}

		}
	}

	private boolean isFirstForcesUserAttack(UserForces userForces) {
		boolean isFirstForces = false;
		if (userForces.isFirstForces() == true && userForces.getStatus() == ForcesStatus.STATUS_ATTACKABLE) {
			isFirstForces = true;
		}
		return isFirstForces;
	}

	/**
	 * 给具体的掉落，需计算概率
	 * 
	 * @param userId
	 * @param forcesDropTool
	 */
	private List<DropToolBO> pickUpForcesDropTool(String userId, ForcesDropTool forcesDropTool, int rand, boolean isFirstForces) {

		User user = this.userService.get(userId);

		List<DropToolBO> dropToolBOList = null;

		int toolType = forcesDropTool.getToolType();
		int toolId = forcesDropTool.getToolId();
		int toolNum = forcesDropTool.getToolNum();
		int lowerNum = forcesDropTool.getLowerNum();
		int upperNum = forcesDropTool.getUpperNum();

		if (isFirstForces) {// 首次打怪
			lowerNum = forcesDropTool.getFirstLowerNum();
			upperNum = forcesDropTool.getFirstUpperNum();
		}

		if (isFirstForces = false || toolType != ToolType.GOLD) {// 首次打怪,如果是金币的话必定掉落
			if (!DropDescHelper.isDrop(lowerNum, upperNum, rand)) {
				logger.debug("该道具未掉落.toolType[" + toolType + "]");
				return null;
			}
		}

		if (toolType == ToolType.GOLD && !userService.checkUserGoldGainLimit(userId, toolNum)) {
			return null;
		}

		dropToolBOList = toolService.giveTools(userId, toolType, toolId, toolNum, ToolUseType.ADD_FORCES);

		return dropToolBOList;
	}

	/**
	 * 给具体的掉落，无需计算概率，直接给
	 * 
	 * @param userId
	 * @param forcesDropTool
	 */
	private List<DropToolBO> pickUpForcesDropTool(String userId, ForcesDropTool forcesDropTool) {

		User user = this.userService.get(userId);

		List<DropToolBO> dropToolBOs = null;

		int toolType = forcesDropTool.getToolType();
		int toolId = forcesDropTool.getToolId();
		int toolNum = forcesDropTool.getToolNum();

		// int gainLimit =
		// this.configDataDao.getInt(ConfigKey.USER_DAILY_GAIN_GOLD_LIMIT, 50);

		if (toolType == ToolType.GOLD && !userService.checkUserGoldGainLimit(userId, toolNum)) {
			return null;
		}

		dropToolBOs = toolService.giveTools(userId, toolType, toolId, toolNum, ToolUseType.ADD_FORCES);

		return dropToolBOs;
	}

	/**
	 * 处理怪物通过
	 */
	private int handleForcesPass(UserForces userForces, SystemForces systemForces) {

		if (systemForces.isLastForceInTheGroup() == false) {
			return 0;
		}

		String userId = userForces.getUserId();
		int forcesGroup = systemForces.getGroupId();

		boolean isSameDay = DateUtils.isSameDay(new Date(), userForces.getUpdatedTime());
		if (isSameDay == true) {
			int times = userForces.getTimes() + 1;
			this.userForcesDao.updateTimes(userId, forcesGroup, times);
		} else {
			this.userForcesDao.updateTimes(userId, forcesGroup, 1);
		}

		// 如果是这个场景中的最后一个怪, 计算通关的星数

		int passStar = 0;
		float onlineUserNum = heroService.getUserHeroList(userId, 1).size();
		float deadHeroNum = deadHeros.get(userId).size();
		float ratio = deadHeroNum / onlineUserNum;

		if (deadHeroNum == 0) {
			passStar = 3;
		} else if (ratio <= 1 / 3.0) {
			passStar = 2;
		} else {
			passStar = 1;
		}
		// 如果是第一次打通这个关
		if (userForces.getStatus() != ForcesStatus.STATUS_PASS) {
			userForcesDao.updateStatus(userId, forcesGroup, ForcesStatus.STATUS_PASS, 0);
			openAfterForces(userId, systemForces.getForcesId());
			double proce = Double.valueOf(userForces.getSceneId() + "." + userForces.getGroupId());
			// 如果是精英副本，更新精英副本进度
			if (userForces.getForcesType() == 2) {
				eventServcie.addUpdateRankEvent(userId, RankType.ELITE.getValue(), proce);
			} else if (userForces.getForcesType() == 1) {// 如果是剧情副本，更新剧情副本进度
				eventServcie.addUpdateRankEvent(userId, RankType.STORY.getValue(), proce);
			}
		}
		// 当现在的通关星数大于以前的通关星数时，更新这一关的通关星数
		if (passStar > userForces.getPassStar()) {
			userForcesDao.updatePassStar(userId, forcesGroup, passStar);
			// 更新副本星数榜
			eventServcie.addUpdateRankEvent(userId, RankType.ECTYPE_STAR.getValue(), passStar - userForces.getPassStar());
		}

		int systemSceneId = systemForces.getSceneId();
		int totalStar = computeScenePassStar(systemSceneId, userId);
		int tp = systemForces.getForcesType();

		if ((tp == 1 && totalStar >= ScenePassStarReward.THIRD_STAR_NUM) || (tp == 2 && totalStar >= ScenePassStarReward.JY_3)) {
			setOneStarRewardCanReceive(userId, systemSceneId);
			setTwoStarRewardCanReceive(userId, systemSceneId);
			setThirdStarRewardCanReceive(userId, systemSceneId);
		} else if (tp == 1 && totalStar >= ScenePassStarReward.TWO_STAR_NUM) {
			setOneStarRewardCanReceive(userId, systemSceneId);
			setTwoStarRewardCanReceive(userId, systemSceneId);
		} else if ((tp == 1 && totalStar >= ScenePassStarReward.ONE_STAR_NUM) || (tp == 2 && totalStar >= ScenePassStarReward.JY_1)) {
			setOneStarRewardCanReceive(userId, systemSceneId);
		}

		return passStar;

	}

	private void setOneStarRewardCanReceive(String userId, int systemSceneId) {
		UserScene userScene = userSceneDao.getUserSceneBySceneId(userId, systemSceneId);
		if (userScene.getOneStarReward() == 0) {
			userSceneDao.updateSceneOneStarReward(userId, systemSceneId, 1);
		}
	}

	private void setTwoStarRewardCanReceive(String userId, int systemSceneId) {
		UserScene userScene = userSceneDao.getUserSceneBySceneId(userId, systemSceneId);
		if (userScene.getTwoStarReward() == 0) {
			userSceneDao.updateSceneTwoStarReward(userId, systemSceneId, 1);
		}
	}

	private void setThirdStarRewardCanReceive(String userId, int systemSceneId) {
		UserScene userScene = userSceneDao.getUserSceneBySceneId(userId, systemSceneId);
		if (userScene.getThirdStarReward() == 0) {
			userSceneDao.updateSceneThirdStarReward(userId, systemSceneId, 1);
		}
	}

	@Override
	public Set<Integer> openAfterForces(String userId, int forcesId) {

		List<UserScene> userSceneList = this.userSceneDao.getUserSceneList(userId);

		Set<Integer> userSceneIds = new HashSet<Integer>();
		for (UserScene userScene : userSceneList) {
			userSceneIds.add(userScene.getSceneId());
		}

		// 后面的怪物置为可攻打
		Date now = new Date();
		Set<Integer> forcesTypeList = new HashSet<Integer>();
		List<SystemForces> nextForcesList = this.systemForcesDao.getSystemForcesByPreForcesId(forcesId);
		SystemForces currentForces = this.systemForcesDao.get(forcesId);

		for (SystemForces nextForces : nextForcesList) {

			if (nextForces.getForcesType() != ForcesType.FORCES_TYPE_NORMAL && nextForces.getForcesType() != ForcesType.FORCES_TYPE_ELITE) {// 爬塔怪不加
				continue;
			}

			int preForcesId = nextForces.getPreForcesId();// 前置怪物1
			int preForcesId2 = nextForces.getPreForcesIdb();// 前置怪物2

			if (preForcesId2 > 0) {
				UserForces userForcesCheck = null;
				if (preForcesId == forcesId) {
					SystemForces systemForces = systemForcesDao.get(preForcesId2);
					userForcesCheck = this.userForcesDao.get(userId, systemForces.getGroupId());
				} else {
					SystemForces systemForces = systemForcesDao.get(preForcesId);
					userForcesCheck = this.userForcesDao.get(userId, systemForces.getGroupId());
				}

				// 第二个前置怪条件未满足
				if (userForcesCheck == null || userForcesCheck.getStatus() != ForcesStatus.STATUS_PASS) {
					continue;
				}
			}

			int openForcesId = nextForces.getGroupId();
			int sceneId = nextForces.getSceneId();
			int forcesType = nextForces.getForcesType();

			forcesTypeList.add(nextForces.getForcesType());

			UserForces userForces = this.userForcesDao.get(userId, openForcesId);
			if (userForces == null) {
				userForces = new UserForces();
				userForces.setGroupId(nextForces.getGroupId());
				userForces.setUserId(userId);
				userForces.setSceneId(sceneId);
				userForces.setStatus(ForcesStatus.STATUS_ATTACKABLE);
				userForces.setTimes(0);
				userForces.setUpdatedTime(now);
				userForces.setCreatedTime(now);
				userForces.setForcesType(forcesType);
				userForces.setPassStar(0);
				this.userForcesDao.add(userForces);

				if (!userSceneIds.contains(sceneId)) {// 新开放

					// 添加关卡
					UserScene userScene = new UserScene();
					userScene.setUserId(userId);
					userScene.setCreatedTime(now);
					userScene.setUpdatedTime(now);
					userScene.setPassFlag(0);
					userScene.setSceneId(sceneId);

					logger.debug("添加用 户场景.userId[" + userId + "], sceneId[" + sceneId + "]");
					this.userSceneDao.add(userScene);
					if (currentForces != null) {
						// this.userSceneDao.updateScenePassed(userId,
						// currentForces.getSceneId());
						// 推送过图星数奖励给前段
						// this.pushUserMessage(userId,
						// currentForces.getSceneId());
					}

					userSceneIds.add(sceneId);
				}

			} else {
				this.userForcesDao.updateStatus(userId, openForcesId, ForcesStatus.STATUS_ATTACKABLE, 0);
			}
		}
		return forcesTypeList;
	}

	public void pushUserMessage(String userId, int sceneId) {

		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", userId);
		params.put("sceneId", String.valueOf(sceneId));

		Command command = new Command();
		command.setCommand(CommandType.COMMAND_PASS_SCENE);
		command.setType(CommandType.PUSH_USER);
		command.setParams(params);

		commandDao.add(command);
	}

	@Override
	public Map<String, Object> drawForcesStarReward(String userId, int sceneId, int passStar, int type) {

		UserScene userScene = this.userSceneDao.getUserSceneBySceneId(userId, sceneId);

		checkDrawCondition(userScene, userId, passStar, type);

		// 发宝箱
		SystemScene systemScene = this.systemSceneDao.get(sceneId);
		CommonDropBO commonDropBO;

		if (passStar == 1) {
			if (!this.userSceneDao.updateSceneOneStarReward(userId, sceneId, 2)) {
				String message = "领取过图奖励,更新数据失败.userId[" + userId + "], sceneId[" + sceneId + "]passStar[" + passStar + "]";
				throw new ServiceException(ServiceReturnCode.FAILD, message);
			}
			commonDropBO = activityService.pickGiftBagReward(userId, systemScene.getOneStarReward(), ToolUseType.DRAW_PASS_SCENE_REWARD);

		} else if (passStar == 2) {
			if (!this.userSceneDao.updateSceneTwoStarReward(userId, sceneId, 2)) {
				String message = "领取过图奖励,更新数据失败.userId[" + userId + "], sceneId[" + sceneId + "]passStar[" + passStar + "]";
				throw new ServiceException(ServiceReturnCode.FAILD, message);
			}
			commonDropBO = activityService.pickGiftBagReward(userId, systemScene.getTwoStarReward(), ToolUseType.DRAW_PASS_SCENE_REWARD);

		} else {
			if (!this.userSceneDao.updateSceneThirdStarReward(userId, sceneId, 2)) {
				String message = "领取过图奖励,更新数据失败.userId[" + userId + "], sceneId[" + sceneId + "]passStar[" + passStar + "]";
				throw new ServiceException(ServiceReturnCode.FAILD, message);
			}
			commonDropBO = activityService.pickGiftBagReward(userId, systemScene.getThreeStarReward(), ToolUseType.DRAW_PASS_SCENE_REWARD);

		}

		ForcesStarRewardBO forcesStarRewardBO = this.createForcesStarRewardBO(sceneId, userId);

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("fsr", forcesStarRewardBO);
		map.put("dr", commonDropBO);

		return map;

	}

	private void checkDrawCondition(UserScene userScene, String userId, int passStar, int type) {

		if (passStar != 1 && passStar != 2 && passStar != 3) {
			String message = "领取过图奖励，参数不正确.userId[" + userId + "], passStar[" + passStar + "], sceneId [" + userScene.getSceneId() + "]";
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

		if (userScene == null || userScene.getPassFlag() == 0) {
			String message = "领取过图奖励，该关卡还没有通过.userId[" + userId + "], passStar[" + passStar + "]";
			throw new ServiceException(DRAW_SCENE_REWARD_HAS_NOT_PASS, message);
		}

		int totalStar = computeScenePassStar(userScene.getSceneId(), userId);
		// 1 普通 2 精英;
		if (passStar == 1) {
			if (userScene.getOneStarReward() == ScenePassStarReward.HAS_DRAWWED_REWARD) {
				String message = "领取过图奖励，已领取.userId[" + userId + "], passStar[" + passStar + "], sceneId [" + userScene.getSceneId() + "]";
				throw new ServiceException(DRAW_SCENE_REWARD_HAS_DRADED, message);
			}
			if ((type == 1 && totalStar < ScenePassStarReward.ONE_STAR_NUM) || (type == 2 && totalStar < ScenePassStarReward.JY_1)) {
				String message = "领取奖励失败，星数不足.userId[" + userId + "], passStar[" + passStar + "], sceneId [" + userScene.getSceneId() + "]";
				throw new ServiceException(DRAW_SCENE_REWARD_HAS_NOT_ENOUGH_STAR, message);
			}
		} else if (passStar == 2) {
			if (userScene.getTwoStarReward() == ScenePassStarReward.HAS_DRAWWED_REWARD) {
				String message = "领取过图奖励，已领取.userId[" + userId + "], passStar[" + passStar + "], sceneId [" + userScene.getSceneId() + "]";
				throw new ServiceException(DRAW_SCENE_REWARD_HAS_DRADED, message);
			}
			if (totalStar < ScenePassStarReward.TWO_STAR_NUM) {
				String message = "领取过图奖励，星数不足.userId[" + userId + "], passStar[" + passStar + "], sceneId [" + userScene.getSceneId() + "]";
				throw new ServiceException(DRAW_SCENE_REWARD_HAS_NOT_ENOUGH_STAR, message);
			}
		} else if (passStar == 3) {
			if (userScene.getThirdStarReward() == ScenePassStarReward.HAS_DRAWWED_REWARD) {
				String message = "领取过图奖励，已领取.userId[" + userId + "], passStar[" + passStar + "], sceneId [" + userScene.getSceneId() + "]";
				throw new ServiceException(DRAW_SCENE_REWARD_HAS_DRADED, message);
			}
			if ((type == 1 && totalStar < ScenePassStarReward.THIRD_STAR_NUM) || (type == 2 && totalStar < ScenePassStarReward.JY_3)) {
				String message = "领取过图奖励，星数不足.userId[" + userId + "], passStar[" + passStar + "], sceneId [" + userScene.getSceneId() + "]";
				throw new ServiceException(DRAW_SCENE_REWARD_HAS_NOT_ENOUGH_STAR, message);
			}
		}

	}

	private int computeScenePassStar(int sceneId, String userId) {
		int totalStar = 0;
		List<UserForcesBO> userForcesBOList = this.forcesService.getUserForcesList(userId, sceneId);
		for (UserForcesBO userForcesBO : userForcesBOList) {
			totalStar += userForcesBO.getPassStar();
		}
		return totalStar;
	}

	private ForcesStarRewardBO createForcesStarRewardBO(int sceneId, String uid) {

		UserScene userScene = userSceneDao.getUserSceneBySceneId(uid, sceneId);

		ForcesStarRewardBO forcesStarRewardBO = new ForcesStarRewardBO();

		forcesStarRewardBO.setSceneId(userScene.getSceneId());
		forcesStarRewardBO.setPassFlag(userScene.getPassFlag());
		forcesStarRewardBO.setOneStarReward(userScene.getOneStarReward());
		forcesStarRewardBO.setTwoStarReward(userScene.getTwoStarReward());
		forcesStarRewardBO.setThirdStarReward(userScene.getThirdStarReward());

		return forcesStarRewardBO;
	}

	@Override
	public void assault(String uid, Integer sid, EventHandle eventHandle) {
		List<UserForces> forcesList = userForcesDao.getUserForcesList(uid, sid);

		// 检测scene是否有可突击的部队
		checkForcesForAssault(uid, forcesList);

		// 减少突击令牌
		if (!toolService.reduceTool(uid, ToolType.MATERIAL, ToolId.TOOL_ID_ASSAULT_TOKEN, 1, ToolUseType.REDUCE_PASS_FORCES_BATTLE)) {
			throw new ServiceException(ASSAULT_TOKEN_NOT_ENOUGH, "突击令不足");
		}

		int totalTimes = 0;
		int forcesType = 0;
		Map<Integer, Integer> assaultNum = new HashMap<Integer, Integer>();

		for (UserForces userForces : forcesList) {

			// 如果玩家没有打赢过这支部队，则不能突击
			if (userForces.getStatus() != ForcesStatus.STATUS_PASS) {
				continue;
			}

			int forcesGroupId = userForces.getForcesType();
			int attackTimeLimit = getForcesAttackTimeLimitByGroupId(forcesGroupId);

			// 将可以突击的部队以及次数加入timesMap，用于后续的奖励发放计算
			// 如果更新日期不同，则计算剩余，并加入map，否则直接将最大次数加入map
			if (DateUtils.isSameDay(userForces.getUpdatedTime(), new Date())) {
				if (userForces.getTimes() < attackTimeLimit) {
					assaultNum.put(forcesGroupId, attackTimeLimit - userForces.getTimes());
				}
			} else {
				assaultNum.put(forcesGroupId, attackTimeLimit);
			}

			// 活跃度奖励任务用到
			totalTimes += attackTimeLimit;
			if (forcesType == 0) {
				forcesType = userForces.getForcesType();
			}

			userForcesDao.updateTimes(uid, forcesGroupId, attackTimeLimit);
		}

		// 发奖励
		User user = userService.get(uid);
		int oldLevel = user.getLevel();

		CommonDropBO commonDropBO = new CommonDropBO();
		for (UserForces userForces : forcesList) {
			int forcesGroupId = userForces.getGroupId();

			if (!assaultNum.containsKey(userForces.getGroupId())) {
				continue;
			}

			List<ForcesDropTool> forcesDropToolList = this.forcesDropToolDao.getForcesGroupDropToolList(forcesGroupId);

			Collection<ForcesDropTool> dropTools = ForcesDropHelper.dropTools(forcesDropToolList, assaultNum.get(forcesGroupId));

			for (ForcesDropTool forcesDropTool : dropTools) {
				try {
					List<DropToolBO> bos = this.pickUpForcesDropTool(uid, forcesDropTool);
					if (bos == null || bos.size() == 0) {
						continue;
					}

					for (DropToolBO dropToolBO : bos) {
						this.toolService.appendToDropBO(uid, commonDropBO, dropToolBO);
					}
				} catch (Exception e) {
					// 发送奖励失败，但仍然返回成功和已发送奖品，只做输出处理
					logger.error(e.getMessage(), e);
				}

			}
		}

		user = this.userService.get(uid);

		int newLevel = user.getLevel();
		if (newLevel > oldLevel) {
			int levelUp = newLevel - oldLevel;
			commonDropBO.setLevelUp(levelUp);
		}

		BaseEvent evt = new BaseEvent();
		evt.setObject("forcesDropBO", commonDropBO);
		eventHandle.handle(evt);

		// 活跃度任务
		if (forcesType == ForcesType.FORCES_TYPE_NORMAL) {
			activityTaskService.updateActvityTask(uid, ActivityTargetType.NORMAL_FORCES, totalTimes);
		} else if (forcesType == ForcesType.FORCES_TYPE_ELITE) {
			activityTaskService.updateActvityTask(uid, ActivityTargetType.ELITE_FORCES, totalTimes);
		}

	}

	/**
	 * 检测场景部队是否打过以及是否有足够的次数可突击
	 * 
	 * @param sid
	 */
	private void checkForcesForAssault(String uid, List<UserForces> forcesList) {
		if (forcesList == null || forcesList.size() == 0) {
			throw new ServiceException(ASSAULT_SCENE_FORCE_CHECK_FAILED, "没有部队可突击");
		}

		if (forcesList != null && forcesList.size() > 0) {
			for (UserForces userForces : forcesList) {
				int attackTimeLimit = getForcesAttackTimeLimitByGroupId(userForces.getGroupId());

				if (userForces.getStatus() == ForcesStatus.STATUS_PASS && !DateUtils.isSameDay(userForces.getUpdatedTime(), new Date())) {
					return;
				}
				if (userForces.getStatus() == ForcesStatus.STATUS_PASS && userForces.getTimes() < attackTimeLimit) {
					return;
				}
			}
		}

		throw new ServiceException(ASSAULT_SCENE_FORCE_CHECK_FAILED, "没有部队可突击");
	}

	private int getForcesAttackTimeLimitByGroupId(int forcesGroupId) {
		SystemForces systemForces = systemForcesDao.getSystemForcesByGroupId(forcesGroupId).get(0);
		return systemForces.getTimesLimit();
	}

	@Override
	public ForcesStarRewardBO getForcesStarReward(int sceneId, String uid) {
		ForcesStarRewardBO forcesStarRewardBO = this.CreateForcesStarRewardBO(sceneId, uid);
		return forcesStarRewardBO;
	}

	public ForcesStarRewardBO CreateForcesStarRewardBO(int sceneId, String uid) {

		UserScene userScene = userSceneDao.getUserSceneBySceneId(uid, sceneId);

		ForcesStarRewardBO forcesStarRewardBO = new ForcesStarRewardBO();

		forcesStarRewardBO.setSceneId(userScene.getSceneId());
		forcesStarRewardBO.setPassFlag(userScene.getPassFlag());
		forcesStarRewardBO.setOneStarReward(userScene.getOneStarReward());
		forcesStarRewardBO.setTwoStarReward(userScene.getTwoStarReward());
		forcesStarRewardBO.setThirdStarReward(userScene.getThirdStarReward());

		return forcesStarRewardBO;
	}

	@Override
	public void sweep(String userId, int groupForcesId, int times, EventHandle handle) {
		User user = userService.get(userId);
		SystemForces systemForces = systemForcesDao.getLastForcesByGroupId(groupForcesId);
		UserForces userForces = userForcesDao.get(userId, groupForcesId);
		int needPower = systemForces.getNeedPower() * times;
		int attTimes = sweepCheck(userId, groupForcesId, times, user, systemForces, userForces);
		userForcesDao.updateTimes(userId, groupForcesId, attTimes);
		userService.reducePower(userId, needPower, systemForces.getForcesType());

		UserSweepInfo sweepInfo = new UserSweepInfo();
		sweepInfo.setUserId(userId);
		Date now = new Date();
		sweepInfo.setCreatedTime(now);
		// 每次扫荡耗时5分钟，计算得出结束时间
		Date endTime = DateUtils.getAfterTime(now, 0);// times * 5 * 60);
														// --weq:去掉冷却时间
		sweepInfo.setEndTime(endTime);
		sweepInfo.setUpdatedTime(now);

		if (!userSweepInfoDao.add(sweepInfo)) {
			throw new ServiceException(ServiceReturnCode.FAILD, "添加扫荡信息失败，未知错误");
		}
		BaseEvent evt = new BaseEvent();
		evt.setObject("sweepInfo", sweepInfo);

		CommonDropBO commonDropBO = giveSweepReward(userId, times, groupForcesId);
		evt.setObject("commonDropBO", commonDropBO);

		// 事件分发
		ForcesEvent event = new ForcesEvent(userId, systemForces.getForcesId(), times);
		eventServcie.dispatchEvent(event);

		handle.handle(evt);
	}

	/**
	 * 扫荡前检测,并返回攻打次数
	 * 
	 * @param userId
	 * @param groupForcesId
	 */
	private int sweepCheck(String userId, int groupForcesId, int times, User user, SystemForces systemForces, UserForces userForces) {
		if (times > MAX_SWEEP_TIMES) {
			throw new ServiceException(ServiceReturnCode.FAILD, "扫荡次数超出上限");
		}

		userService.checkUserHeroBagLimit(userId);
		userService.checkUserEquipBagLimit(userId);

		if (userForces == null || userForces.getStatus() != 2) {
			throw new ServiceException(ADD_SWEEP_RETURN_FORCES_NOT_PASS, "部队没有打过，无法扫荡");
		}

		if (user.getPower() < systemForces.getNeedPower()) {
			throw new ServiceException(ADD_SWEEP_RETURN_POWER_NOT_ENOUGH, "体力不足");
		}
		int needPower = systemForces.getNeedPower() * times;
		if (user.getPower() < needPower) {
			throw new ServiceException(ServiceReturnCode.FAILD, "体力无法满足请求次数，数据异常");
		}
		int timeLimit = systemForces.getTimesLimit();
		int forcesTimes = userForces.getTimes();
		if (!DateUtils.isSameDay(userForces.getUpdatedTime(), new Date())) {
			forcesTimes = 0;
		}

		if (timeLimit - forcesTimes < times) {
			throw new ServiceException(ADD_SWEEP_RETURN_LIMIT_EXCEED, "超出攻打限制");
		}

		return forcesTimes + times;
	}

	public CommonDropBO giveSweepReward(String userId, int times, int groupForcesId) {

		User user = userService.get(userId);

		CommonDropBO commonDropBO = new CommonDropBO();
		int oldLevel = user.getLevel();

		List<ForcesDropTool> forcesDropToolList = this.forcesDropToolDao.getForcesGroupDropToolList(groupForcesId);

		Collection<ForcesDropTool> dropTools = ForcesDropHelper.dropTools(forcesDropToolList, times);

		for (ForcesDropTool forcesDropTool : dropTools) {
			try {
				List<DropToolBO> bos = this.pickUpForcesDropTool(userId, forcesDropTool);
				if (bos == null || bos.size() == 0) {
					continue;
				}

				for (DropToolBO dropToolBO : bos) {
					this.toolService.appendToDropBO(userId, commonDropBO, dropToolBO);
				}
			} catch (Exception e) {
				// 发送奖励失败，但仍然返回成功和已发送奖品，只做输出处理
				logger.error(e.getMessage(), e);
			}

		}
		user = this.userService.get(userId);

		int newLevel = user.getLevel();
		if (newLevel > oldLevel) {
			int levelUp = newLevel - oldLevel;
			commonDropBO.setLevelUp(levelUp);
		}

		return commonDropBO;
	}

	@Override
	public boolean updateSweepComplete(String userId) {
		return userSweepInfoDao.updateSweepComplete(userId, new Date());
	}

	@Override
	public SweepInfoBO getUserSweepInfo(String userId) {
		UserSweepInfo sweepInfo = userSweepInfoDao.getCurrentSweep(userId);
		SweepInfoBO bo = new SweepInfoBO();
		if (sweepInfo != null) {

			bo.setEndTime(sweepInfo.getEndTime().getTime());
			bo.setStartTime(sweepInfo.getCreatedTime().getTime());
			if (checkComplete(sweepInfo)) {
				bo.setStatus(SweepStatus.SWEEP_COMPLETE);
			} else {
				bo.setStatus(SweepStatus.SWEEP_ING);
			}

		} else {
			bo.setStatus(1);
		}
		return bo;
	}

	private boolean checkComplete(UserSweepInfo sweepInfo) {
		long now = System.currentTimeMillis();
		long endTime = sweepInfo.getEndTime().getTime();
		// 表示已完成
		if (now >= endTime) {
			return true;
		}
		return false;
	}

	@Override
	public int getClearCDGold(String userId) {

		UserSweepInfo sweepInfo = userSweepInfoDao.getCurrentSweep(userId);

		if (sweepInfo == null || checkComplete(sweepInfo)) {
			throw new ServiceException(GET_CLEAR_CD_GOLD_NO_CD, "获取消除扫荡的CD的金币");
		}
		int min = DateUtils.getMinuteDiff(new Date(), sweepInfo.getEndTime());

		if (min <= 0) {
			throw new ServiceException(GET_CLEAR_CD_GOLD_NO_CD, "获取消除扫荡的CD的金币");
		}

		return ((int) (min + FIVE_MIN - 1) / FIVE_MIN) * CLREAR_CD_COST_GOLD_EVERY_FIVE_MIN;
	}

	@Override
	public void speedUpSweep(String userId) {

		UserSweepInfo sweepInfo = userSweepInfoDao.getCurrentSweep(userId);
		boolean falg = checkComplete(sweepInfo);
		if (sweepInfo == null || falg) {
			throw new ServiceException(GET_CLEAR_CD_GOLD_NO_CD, "获取消除扫荡的CD的金币");
		}
		int min = DateUtils.getMinuteDiff(new Date(), sweepInfo.getEndTime());

		if (min <= 0) {
			throw new ServiceException(GET_CLEAR_CD_GOLD_NO_CD, "获取消除扫荡的CD的金币");
		}

		int needGold = ((int) (min + FIVE_MIN - 1) / FIVE_MIN) * CLREAR_CD_COST_GOLD_EVERY_FIVE_MIN;

		if (!userService.reduceGold(userId, needGold, ToolUseType.REDUCE_SPEED_UP_SWEEP)) {
			throw new ServiceException(SPEED_UP_SWEEP_GOLD_NOT_ENOUGH, "金币不足");
		}
		this.userSweepInfoDao.updateSweepComplete(userId, new Date());
	}

}
