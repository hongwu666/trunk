hpackage com.lodogame.ldsg.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.ForcesDropToolDao;
import com.lodogame.game.dao.SystemBlackRoomConfigDao;
import com.lodogame.game.dao.SystemForcesDao;
import com.lodogame.game.dao.SystemForcesMonsterDao;
import com.lodogame.game.dao.SystemMonsterDao;
import com.lodogame.game.dao.SystemSceneDao;
import com.lodogame.game.dao.UserBlackRoomLogDao;
import com.lodogame.ldsg.bo.ActivityCopyBO;
import com.lodogame.ldsg.bo.BattleBO;
import com.lodogame.ldsg.bo.BattleHeroBO;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.DropToolBO;
import com.lodogame.ldsg.constants.ActivityId;
import com.lodogame.ldsg.constants.BattleType;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.constants.ToolUseType;
import com.lodogame.ldsg.event.BattleResponseEvent;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.service.ActivityService;
import com.lodogame.ldsg.service.BattleService;
import com.lodogame.ldsg.service.BlackRoomService;
import com.lodogame.ldsg.service.ForcesService;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.ToolService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.ldsg.service.VipService;
import com.lodogame.model.ForcesDropTool;
import com.lodogame.model.SystemBlackRoomConfig;
import com.lodogame.model.SystemForces;
import com.lodogame.model.SystemForcesMonster;
import com.lodogame.model.SystemHero;
import com.lodogame.model.SystemMonster;
import com.lodogame.model.SystemScene;
import com.lodogame.model.User;
import com.lodogame.model.UserBlackRoomLog;

public class BlackRoomServiceImpl implements BlackRoomService {

	@Autowired
	private SystemBlackRoomConfigDao systemBlackRoomConfigDao;

	@Autowired
	private UserBlackRoomLogDao userBlackRoomLogDao;

	@Autowired
	private UserService userService;

	@Autowired
	private HeroService heroService;

	@Autowired
	private ForcesService forcesService;

	@Autowired
	private BattleService battleService;

	@Autowired
	private SystemForcesDao systemForcesDao;

	@Autowired
	private ForcesDropToolDao forcesDropToolDao;

	@Autowired
	private ToolService toolService;

	@Autowired
	private VipService vipService;

	@Autowired
	private SystemForcesMonsterDao systemForcesMonsterDao;

	@Autowired
	private SystemMonsterDao systemMonsterDao;

	@Autowired
	private SystemSceneDao systemSceneDao;

	@Autowired
	private ActivityService activityService;

	private static final Logger LOG = Logger.getLogger(BlackRoomServiceImpl.class);

	@Override
	public void fight(final String userId, final int type, final EventHandle handle) {

		BattleBO attack = new BattleBO();

		User user = this.userService.get(userId);
		final int userLevel = user.getLevel();

		final UserBlackRoomLog userBlackRoomLog = this.getUserBlackRoolInfo(userId, type);
		int times = userBlackRoomLog.getTime();
		final int nextTimes = times + 1;

		if (nextTimes > 9) {
			throw new ServiceException(FIGHT_TIMES_LIMIT, "挑战修炼密室失败，已经达到了最大次数.userId[" + userId + "],Time[" + nextTimes + "]");
		}

		SystemBlackRoomConfig systemBlackRoomConfig = this.systemBlackRoomConfigDao.getBlackRoomConfigByTime(nextTimes);
		int needMoney = 0;

		if (nextTimes > 3) {// 第四次开始收钱

			needMoney = systemBlackRoomConfig.getCostGold();

			int buyTime = this.vipService.getCopyBuyTime(user.getVipLevel());
			if (nextTimes - 3 > buyTime) {
				throw new ServiceException(FIGHT_VIP_LEVEL_NOT_ENOUGH, "购买密室挑战次数失败，VIP等级不够.userId[" + userId + "],Time[" + nextTimes + "]");
			}

			if (!this.userService.reduceGold(userId, needMoney, ToolUseType.REDUCE_BUY_BLACK_ROOM_TIME)) {
				String message = "购买密室挑战次数出错，用户金币不足.userId[" + userId + "], needMoney[" + needMoney + "]";
				throw new ServiceException(FIGHT_GOLD_NOT_ENOUGH, message);
			}
		}

		// 英雄列表
		List<BattleHeroBO> attackBO = this.heroService.getUserBattleHeroBOList(userId);
		attack.setUserLevel(user.getLevel());
		attack.setBattleHeroBOList(attackBO);

		BattleBO defense = new BattleBO();

		int forcesId = 0;
		if (type == BattleType.BLACK_ROOM) {
			forcesId = systemBlackRoomConfig.getBlackRoomForcesId();
		} else if (type == BattleType.TREASURY) {
			forcesId = systemBlackRoomConfig.getTreasuryForcesId();
		}
		final SystemForces systemForces = this.systemForcesDao.get(forcesId);
		final SystemScene systemScene = this.systemSceneDao.get(systemForces.getSceneId());

		List<BattleHeroBO> battleHeroBOList = this.forcesService.getForcesHeroBOList(forcesId);
		defense.setBattleHeroBOList(battleHeroBOList);

		final int coseMoney = needMoney;

		battleService.fight(attack, defense, type, new EventHandle() {

			public boolean handle(Event battleResponse) {

				if (battleResponse instanceof BattleResponseEvent) {

					int flag = battleResponse.getInt("flag");

					processFightResult(systemForces, userId, flag, type, battleResponse);

					if (flag == 1) {// 赢了改次数
						userBlackRoomLog.setTime(nextTimes);
					} else if (coseMoney > 0) {
						userService.addGold(userId, coseMoney, ToolUseType.ADD_BLACK_ROOM_RETURN_GOLD, userLevel);
					}

					battleResponse.setObject("bgid", systemScene.getImgId());

					// 更新用户信息
					userBlackRoomLogDao.updateUserBlackRoomLog(userBlackRoomLog);

					handle.handle(battleResponse);
				}
				return true;
			}
		});

	}

	protected void processFightResult(SystemForces systemForces, String userId, int flag, int type, Event battleResponse) {

		if (flag == 1) {
			List<ForcesDropTool> forcesDropToolList = this.forcesDropToolDao.getForcesGroupDropToolList(systemForces.getGroupId());
			List<DropToolBO> dropToolBOList = null;
			CommonDropBO forcesDropBO = new CommonDropBO();

			boolean moonIsOpen = activityService.isActivityOpen(ActivityId.MOON_DAY);

			for (ForcesDropTool forcesDropTool : forcesDropToolList) {
				int toolType = forcesDropTool.getToolType();
				int toolId = forcesDropTool.getToolId();
				int toolNum = forcesDropTool.getToolNum();

				// 中秋双倍
				if (moonIsOpen) {
					toolNum *= 2;
				}

				dropToolBOList = toolService.giveTools(userId, toolType, toolId, toolNum, ToolUseType.ADD_FORCES);
				for (DropToolBO dropToolBO : dropToolBOList) {
					this.toolService.appendToDropBO(userId, forcesDropBO, dropToolBO);
				}
				battleResponse.setObject("forcesDropBO", forcesDropBO);
			}
		}

		battleResponse.setObject("co", this.enter(userId, type));
		battleResponse.setObject("dun", systemForces.getForcesName());

	}

	private UserBlackRoomLog getUserBlackRoolInfo(String userId, int type) {

		UserBlackRoomLog userBlackRoomLog = this.userBlackRoomLogDao.getUserBlackRoomLog(userId, type);
		if (userBlackRoomLog == null) {
			userBlackRoomLog = new UserBlackRoomLog();
			userBlackRoomLog.setType(type);
			userBlackRoomLog.setUserId(userId);
			userBlackRoomLog.setTime(0);
			userBlackRoomLog.setUpdateTime(new Date());
			this.userBlackRoomLogDao.addUserBlackRoomLog(userBlackRoomLog);
			return userBlackRoomLog;
		}

		if (!DateUtils.isSameDay(userBlackRoomLog.getUpdateTime(), new Date())) {
			userBlackRoomLog.setTime(0);
			userBlackRoomLog.setUpdateTime(new Date());
		}

		return userBlackRoomLog;

	}

	@Override
	public ActivityCopyBO enter(String userId, int type) {
		User user = this.userService.get(userId);
		ActivityCopyBO activityCopyBO = new ActivityCopyBO();
		activityCopyBO.setType(type);
		activityCopyBO.setFreeTime(3);
		int challengeTime = 0;
		int buyTime = this.vipService.getCopyBuyTime(user.getVipLevel());
		UserBlackRoomLog userBlackRoomLog = this.userBlackRoomLogDao.getUserBlackRoomLog(userId, type);
		if (userBlackRoomLog == null) {
			userBlackRoomLog = new UserBlackRoomLog();
			userBlackRoomLog.setUserId(userId);
			userBlackRoomLog.setTime(0);
			userBlackRoomLog.setType(type);
			userBlackRoomLog.setUpdateTime(new Date());
			this.userBlackRoomLogDao.addUserBlackRoomLog(userBlackRoomLog);
			activityCopyBO.setChallengeTime(0);
			activityCopyBO.setChallengeBuyTime(buyTime);
		}
		if (!DateUtils.isSameDay(userBlackRoomLog.getUpdateTime(), new Date())) {
			activityCopyBO.setChallengeTime(0);
			activityCopyBO.setChallengeBuyTime(buyTime);
		} else {
			challengeTime = userBlackRoomLog.getTime();
			activityCopyBO.setChallengeBuyTime(userBlackRoomLog.getTime() - 3 < 0 ? buyTime : buyTime + 3 - userBlackRoomLog.getTime());
		}

		SystemBlackRoomConfig systemBlackRoomConfig = this.systemBlackRoomConfigDao.getBlackRoomConfigByTime(challengeTime + 1);
		if (challengeTime > 0) {
			activityCopyBO.setChallengeTime(challengeTime);
		}
		if (systemBlackRoomConfig == null) {
			return activityCopyBO;
		}

		int forcesId = 0;
		if (type == BattleType.BLACK_ROOM) {
			forcesId = systemBlackRoomConfig.getBlackRoomForcesId();
			// 取部队的第一个怪的模型
			List<SystemForcesMonster> forcesMonsterList = this.systemForcesMonsterDao.getForcesMonsterList(forcesId);
			if (forcesMonsterList == null) {
				String message = "异常数据，怪物部队的怪物不存在.forcesId[" + forcesId + "]";
				LOG.error(message);
				throw new ServiceException(ServiceReturnCode.FAILD, message);
			}
			int monsterId = forcesMonsterList.get(0).getMonsterId();
			SystemMonster systemMonster = this.systemMonsterDao.get(monsterId);
			SystemHero systemHero = this.heroService.getSysHero(systemMonster.getHeroModel());
			activityCopyBO.setModelId(systemHero.getModelId());

		} else if (type == BattleType.TREASURY) {
			forcesId = systemBlackRoomConfig.getTreasuryForcesId();
			activityCopyBO.setColor(systemBlackRoomConfig.getColor());

		}

		SystemForces systemForces = this.systemForcesDao.get(forcesId);
		activityCopyBO.setForceName(systemForces.getForcesName());
		List<ForcesDropTool> forcesDropToolList = this.forcesDropToolDao.getForcesGroupDropToolList(systemForces.getGroupId());
		if (forcesDropToolList == null) {
			String message = "异常数据，该部队掉落只能掉落武魂或是银币.forcesId[" + forcesId + "]";
			LOG.error(message);
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}
		activityCopyBO.setToolNum(forcesDropToolList.get(0).getToolNum());
		activityCopyBO.setForceId(forcesId);
		activityCopyBO.setColor(systemBlackRoomConfig.getColor());
		activityCopyBO.setGold(systemBlackRoomConfig.getCostGold());

		return activityCopyBO;
	}

}
