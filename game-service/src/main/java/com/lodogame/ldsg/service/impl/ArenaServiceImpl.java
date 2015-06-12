package com.lodogame.ldsg.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.SystemArenaGiftDao;
import com.lodogame.game.dao.SystemGiftbagDao;
import com.lodogame.game.dao.UserArenaInfoDao;
import com.lodogame.game.dao.UserArenaRecordLogDao;
import com.lodogame.game.dao.UserArenaRewardLogDao;
import com.lodogame.game.dao.UserDao;
import com.lodogame.game.dao.UserMapperDao;
import com.lodogame.game.dao.UserVersusLogDao;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.ldsg.bo.ArenaDefiantBO;
import com.lodogame.ldsg.bo.ArenaRankRewardBo;
import com.lodogame.ldsg.bo.ArenaScoreRankBo;
import com.lodogame.ldsg.bo.ArenaWinGiftBo;
import com.lodogame.ldsg.bo.BattleBO;
import com.lodogame.ldsg.bo.BattleHeroBO;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.DropDescBO;
import com.lodogame.ldsg.bo.UserArenaBo;
import com.lodogame.ldsg.bo.UserArenaRecordBo;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.constants.BattleType;
import com.lodogame.ldsg.constants.ToolUseType;
import com.lodogame.ldsg.constants.UserPowerGetType;
import com.lodogame.ldsg.event.ArenaEvent;
import com.lodogame.ldsg.event.ArenaWinEvent;
import com.lodogame.ldsg.event.BattleResponseEvent;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.helper.DropDescHelper;
import com.lodogame.ldsg.helper.HeroHelper;
import com.lodogame.ldsg.service.ArenaService;
import com.lodogame.ldsg.service.BattleService;
import com.lodogame.ldsg.service.DailyTaskService;
import com.lodogame.ldsg.service.EventService;
import com.lodogame.ldsg.service.ExpeditionService;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.MailService;
import com.lodogame.ldsg.service.RobotService;
import com.lodogame.ldsg.service.ToolService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.GiftbagDropTool;
import com.lodogame.model.IUser;
import com.lodogame.model.RobotUser;
import com.lodogame.model.SystemArenaGift;
import com.lodogame.model.SystemDailyTask;
import com.lodogame.model.SystemHero;
import com.lodogame.model.User;
import com.lodogame.model.UserArenaHero;
import com.lodogame.model.UserArenaInfo;
import com.lodogame.model.UserArenaRecordLog;
import com.lodogame.model.UserArenaRewardLog;
import com.lodogame.model.UserArenaSeriesGift;
import com.lodogame.model.UserArenaSeriesGiftInfo;
import com.lodogame.model.UserPowerInfo;
import com.lodogame.model.UserVersusLog;

public class ArenaServiceImpl implements ArenaService {

	private static final Logger logger = Logger.getLogger(ArenaServiceImpl.class);
	private Date lastClearTime = new Date();
	private int periodGroupId = 0;
	private final static int RECORD_NUM = 10;
	private final static int DAY_PK_TIMES = 3;

	private static final Map<Integer, Integer> winCountMap = new HashMap<Integer, Integer>();
	private static Map<String, String> arenaUserId = new HashMap<String, String>();
	static {
		winCountMap.put(3, 6040);
		winCountMap.put(5, 6041);
		winCountMap.put(8, 6042);
	}

	@Autowired
	private UserArenaInfoDao userArenaInfoDao;

	@Autowired
	private UserArenaRecordLogDao userArenaRecordLogDao;

	@Autowired
	private UserArenaRewardLogDao userArenaRewardLogDao;

	@Autowired
	private UserService userService;

	@Autowired
	private ToolService toolService;

	@Autowired
	private HeroService heroService;

	@Autowired
	private EventService eventService;

	@Autowired
	private UserDao userDao;

	@Autowired
	private BattleService battleService;

	@Autowired
	private SystemArenaGiftDao systemArenaGiftDao;

	@Autowired
	private SystemGiftbagDao systemGiftbagDao;

	@Autowired
	private MailService mailService;

	@Autowired
	private UserMapperDao userMapperDao;

	@Autowired
	DailyTaskService dailyTaskService;

	@Autowired
	private ExpeditionService expeditionService;

	@Autowired
	private RobotService robotService;

	@Autowired
	private UserVersusLogDao userVersusLogDao;

	@Override
	public Map<String, Object> enter(String userId) {

		Map<String, Object> map = new HashMap<String, Object>();

		UserArenaInfo userArenaInfo = this.userArenaInfoDao.get(userId);
		User user = this.userService.get(userId);
		if (null == userArenaInfo) {
			userArenaInfo = new UserArenaInfo();
			userArenaInfo.setPkNum(0);
			userArenaInfo.setRefreshNum(0);
			userArenaInfo.setScore(0);
			userArenaInfo.setUserId(userId);
			userArenaInfo.setUserLevel(user.getLevel());
			userArenaInfo.setUserName(user.getUsername());
			userArenaInfo.setWinCount(0);

			this.userArenaInfoDao.update(userArenaInfo);
		}

		ArenaDefiantBO bb = getArenaDefiantBOs(userArenaInfo, getRank(userId), 2);
		List<UserHeroBO> hs = heroService.getUserHeroList(bb.getUserId(), 1);
		int v = HeroHelper.getCapability(hs);
		bb.setAtt(v);
		List<ArenaDefiantBO> list = new ArrayList<ArenaDefiantBO>();
		list.add(bb);
		map.put("pls", list);
		map.put("bn", userArenaInfo.getBuyNum());
		map.put("wls", createArenaWinGiftBo());
		map.put("rls", getArenaRankRewardBos());
		return map;
	}

	private List<ArenaWinGiftBo> createArenaWinGiftBo() {
		List<ArenaWinGiftBo> list = new ArrayList<ArenaWinGiftBo>();

		for (Integer key : winCountMap.keySet()) {
			ArenaWinGiftBo winGiftBo = new ArenaWinGiftBo();
			int value = winCountMap.get(key);
			winGiftBo.setGiftId(value);
			winGiftBo.setWinCount(key);
			list.add(winGiftBo);
		}
		return list;
	}

	private List<ArenaRankRewardBo> getArenaRankRewardBos() {
		List<SystemArenaGift> systemArenaGifts = this.systemArenaGiftDao.getList(getRandom());
		List<ArenaRankRewardBo> list = new ArrayList<ArenaRankRewardBo>();
		if (null != systemArenaGifts && systemArenaGifts.size() > 0) {
			for (SystemArenaGift systemArenaGift : systemArenaGifts) {
				ArenaRankRewardBo arenaRankRewardBo = new ArenaRankRewardBo();
				arenaRankRewardBo.setGiftId(systemArenaGift.getGiftId());
				if (systemArenaGift.getLowerRank() <= 10) {
					arenaRankRewardBo.setRank(systemArenaGift.getLowerRank() + "");
				} else {
					arenaRankRewardBo.setRank(systemArenaGift.getLowerRank() + "-" + systemArenaGift.getUpperRank());
				}
				list.add(arenaRankRewardBo);

			}
		}
		return list;
	}

	public int getRank(String userId) {
		UserArenaInfo userArenaInfo = this.userArenaInfoDao.get(userId);
		if (userArenaInfo.getScore() == 0) {
			return 0;
		}
		return this.userArenaInfoDao.getRank(userId);
	}

	/*
	 * private int getLimit(int wincount) { int limit = 50; switch (wincount) {
	 * case 0: case 1: case 2: limit = 50; break; case 3: case 4: case 5: limit
	 * = 40; break; case 6: case 7: limit = 30; break; default: break; } return
	 * limit; }
	 */

	/**
	 * @param userArenaInfo
	 * @param rank
	 * @param 起因
	 *            1:刷新 2:获取 3:胜利后刷新
	 * @return
	 */
	private ArenaDefiantBO getArenaDefiantBOs(UserArenaInfo userArenaInfo, int rank, int event) {

		Set<ArenaDefiantBO> list = new HashSet<ArenaDefiantBO>();
		if (arenaUserId.containsKey(userArenaInfo.getUserId())) {
			return getArenaDefiantBOsByIds(userArenaInfo.getUserId());
		}

		int att = 0;

		int maxPower = userDao.getUserMaxPower(userArenaInfo.getUserId());
		List<UserHeroBO> hero = heroService.getUserHeroList(userArenaInfo.getUserId(), 1);
		att = HeroHelper.getCapability(hero);
		if (maxPower > att) {
			att = maxPower;
		}

		int[] need = getNeedAtt(att, userArenaInfo.getWinNum() + 1);
		String vsUser = null;
		UserPowerInfo uid = userService.getRandAttUser(userArenaInfo.getUserId(), UserPowerGetType.ARENA, need[0], need[1]);

		boolean robot = false;
		if (uid == null) { // 没有用户符合
			RobotUser rot = robotService.getRobotUser(need[0], need[1]);
			if (rot == null) {// 没有机器人
				uid = userService.getRandUser(userArenaInfo.getUserId(), 0);
				vsUser = uid.getUserId();
			} else {
				vsUser = rot.getUserId();
				robot = true;
			}
		} else {

			vsUser = uid.getUserId();

			UserVersusLog userVersusLog = new UserVersusLog();
			userVersusLog.setUserId(userArenaInfo.getUserId());
			userVersusLog.setType(1);
			userVersusLog.setTargetUserId(vsUser);
			userVersusLog.setDate(DateUtils.getDate());
			userVersusLog.setCreatedTime(new Date());
			userVersusLogDao.add(userVersusLog);

		}

		List<UserHeroBO> hs = heroService.getUserHeroList(vsUser, 1);
		int v = HeroHelper.getCapability(hs);

		Date now = new Date();

		if (!robot) {
			List<UserArenaHero> userArenaHeroList = new ArrayList<UserArenaHero>();
			for (UserHeroBO bo : hs) {
				UserArenaHero userArenaHero = new UserArenaHero();
				userArenaHero.setUserHeroId(bo.getUserHeroId());
				userArenaHero.setUserId(userArenaInfo.getUserId());
				userArenaHero.setCreatedTime(now);
				userArenaHero.setDefense(bo.getPhysicalDefense());
				userArenaHero.setLife(bo.getLife());
				userArenaHero.setAttack(bo.getPhysicalAttack());
				userArenaHero.setPos(bo.getPos());
				userArenaHero.setLevel(bo.getHeroLevel());
				userArenaHero.setStar(bo.getStar());
				userArenaHero.setSystemHeroId(bo.getSystemHeroId());
				userArenaHeroList.add(userArenaHero);

			}
			this.userArenaInfoDao.insertArenaHero(userArenaHeroList);
		}

		add(list, vsUser, userArenaInfo, 1, robot);
		ArenaDefiantBO arenaDefiantBO = list.iterator().next();
		arenaDefiantBO.setAtt(v);

		arenaUserId.put(userArenaInfo.getUserId(), arenaDefiantBO.getUserId());
		return arenaDefiantBO;
	}

	private int[] getNeedAtt(int att, int winNum) {
		int[] result = new int[] { att, att };
		int[] fw = new int[] { 95, 110 };
		switch (winNum) {
		case 1:
			fw = new int[] { 80, 90 };
			break;
		case 2:
			fw = new int[] { 90, 95 };
			break;
		case 3:
			fw = new int[] { 90, 100 };
			break;
		}
		result[0] = (int) (att * (fw[0] / 100f));
		result[1] = (int) (att * (fw[1] / 100f));
		return result;
	}

	private void add(Set<ArenaDefiantBO> list, String listA, UserArenaInfo userArenaInfo, int num, boolean robot) {
		UserArenaInfo temp1 = getUserArenaInfo(listA, robot);
		ArenaDefiantBO arenaDefiantBO = createArenaDefiantBO(getUserArenaInfo(temp1.getUserId(), robot), userArenaInfo.getUserId(), robot);
		arenaDefiantBO.setRobot(robot);
		list.add(arenaDefiantBO);
	}

	private ArenaDefiantBO getArenaDefiantBOsByIds(String userId) {
		ArenaDefiantBO arenaDefiantBO = createArenaDefiantBO(getUserArenaInfo(arenaUserId.get(userId)), userId);
		return arenaDefiantBO;
	}

	private UserArenaInfo getUserArenaInfo(String userId) {
		return this.getUserArenaInfo(userId, false);
	}

	private UserArenaInfo getUserArenaInfo(String userId, boolean robot) {
		UserArenaInfo userArenaInfo = this.userArenaInfoDao.get(userId);
		IUser user = this.robotService.get(userId);
		if (user == null) {
			user = this.userService.get(userId);
		}
		if (null == userArenaInfo) {
			userArenaInfo = new UserArenaInfo();
			userArenaInfo.setPkNum(0);
			userArenaInfo.setRefreshNum(0);
			userArenaInfo.setScore(0);
			userArenaInfo.setUserId(userId);
			userArenaInfo.setUserLevel(user.getLevel());
			userArenaInfo.setUserName(user.getUsername());
			userArenaInfo.setWinCount(0);
		}
		return userArenaInfo;
	}

	private ArenaDefiantBO createArenaDefiantBO(UserArenaInfo userArenaInfo, String userId) {
		return this.createArenaDefiantBO(userArenaInfo, userId, false);
	}

	private ArenaDefiantBO createArenaDefiantBO(UserArenaInfo userArenaInfo, String userId, boolean robot) {

		ArenaDefiantBO arenaDefiantBO = new ArenaDefiantBO();
		IUser user = robotService.get(userArenaInfo.getUserId());
		if (user == null) {
			user = this.userService.get(userArenaInfo.getUserId());
		}

		boolean isEnemy = this.userArenaRecordLogDao.isEnemy(userId, userArenaInfo.getUserId());

		arenaDefiantBO.setIsEnemy(isEnemy ? 1 : 0);
		arenaDefiantBO.setLevel(userArenaInfo.getUserLevel());
		arenaDefiantBO.setUserId(userArenaInfo.getUserId());
		SystemHero systemHero = this.userService.getSystemHero(userArenaInfo.getUserId());
		arenaDefiantBO.setUserImgId(systemHero.getImgId());
		arenaDefiantBO.setUsrename(userArenaInfo.getUserName());
		arenaDefiantBO.setPlayId(user.getLodoId());
		arenaDefiantBO.setBuyNum(userArenaInfo.getBuyNum());
		return arenaDefiantBO;
	}

	private int getRandom() {
		if (periodGroupId == 0) {
			int count = this.systemArenaGiftDao.getGroupItemCount();
			periodGroupId = RandomUtils.nextInt(count) + 1;
		}
		return periodGroupId;
	}

	@Override
	public UserArenaBo get(String userId) {
		UserArenaBo userArenaBo = new UserArenaBo();
		UserArenaInfo userArenaInfo = getUserArenaInfo(userId);
		userArenaBo.setRefreshNum(userArenaInfo.getRefreshNum() > 0 ? 0 : 1);
		userArenaBo.setPkNum(userArenaInfo.getPkNum() >= DAY_PK_TIMES ? 0 : DAY_PK_TIMES - userArenaInfo.getPkNum());
		if (userArenaInfo.getScore() == 0) {
			userArenaBo.setRank(0);
		} else {
			userArenaBo.setRank(getRank(userId));
		}
		userArenaBo.setNeedGold(userArenaInfo.getPkNum() >= DAY_PK_TIMES ? getReduceGoldNum(userArenaInfo.getPkNum() + 1 - DAY_PK_TIMES) : 0);
		userArenaBo.setScore(userArenaInfo.getScore());
		userArenaBo.setWinCount(userArenaInfo.getWinCount());
		userArenaBo.setThreeRewardStatus(isReward(userId, 3));
		userArenaBo.setFiveRewardStatus(isReward(userId, 5));
		userArenaBo.setEightRewardStatus(isReward(userId, 8));
		userArenaBo.setMaxWinCount(userArenaInfo.getMaxWinCount());
		userArenaBo.setWinNum(userArenaInfo.getWinNum());
		return userArenaBo;
	}

	private int isReward(String userId, int type) {
		UserArenaRewardLog userArenaRewardLog = this.userArenaRewardLogDao.get(userId, type);
		if (null != userArenaRewardLog) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public List<ArenaScoreRankBo> getArenaScoreRankBos() {
		List<ArenaScoreRankBo> result = new ArrayList<ArenaScoreRankBo>();
		List<String> list = this.userArenaInfoDao.getRangeRankTen();
		if (null != list && list.size() > 0) {
			int rank = 1;
			for (String userId : list) {
				UserArenaInfo userArenaInfo = getUserArenaInfo(userId);

				List<UserHeroBO> userHeroList = this.heroService.getUserHeroList(userId, 1);

				ArenaScoreRankBo arenaScoreRankBo = new ArenaScoreRankBo();
				arenaScoreRankBo.setLevel(userArenaInfo.getUserLevel());
				arenaScoreRankBo.setSystemHeroId(userHeroList.get(0).getSystemHeroId());
				arenaScoreRankBo.setUsername(userArenaInfo.getUserName());
				arenaScoreRankBo.setWinCount(userArenaInfo.getMaxWinCount());
				result.add(arenaScoreRankBo);
			}

			Comparator<ArenaScoreRankBo> comparator = new Comparator<ArenaScoreRankBo>() {

				@Override
				public int compare(ArenaScoreRankBo o1, ArenaScoreRankBo o2) {
					return o2.getWinCount() - o1.getWinCount();
				}
			};

			Collections.sort(result, comparator);

			for (ArenaScoreRankBo bo : result) {
				bo.setRank(rank);
				rank++;
			}
		}
		return result;
	}

	@Override
	public List<UserArenaRecordBo> getArenaRecordBos(String userId) {
		List<UserArenaRecordBo> list = new ArrayList<UserArenaRecordBo>();
		List<UserArenaRecordLog> userArenaRecordLogs = this.userArenaRecordLogDao.getListByAttackUserId(userId, RECORD_NUM);
		if (null != userArenaRecordLogs && userArenaRecordLogs.size() > 0) {
			for (UserArenaRecordLog uarl : userArenaRecordLogs) {
				UserArenaRecordBo userArenaRecordBo = new UserArenaRecordBo();
				if (uarl.getAttackUserId().equals(userId)) {
					userArenaRecordBo.setUsername(uarl.getUsername());
					userArenaRecordBo.setDefiant(1);
					userArenaRecordBo.setResult(uarl.getResult());
				} else {
					User user = this.userService.get(uarl.getAttackUserId());
					userArenaRecordBo.setUsername(user.getUsername());
					userArenaRecordBo.setDefiant(0);
					userArenaRecordBo.setResult(uarl.getResult() == 0 ? 1 : 0);
				}
				userArenaRecordBo.setTime(DateUtils.getDate2(uarl.getCreatedTime()));

				list.add(userArenaRecordBo);
			}
		}
		return list;
	}

	@Override
	public List<ArenaDefiantBO> refresh(String userId) {
		UserArenaInfo userArenaInfo = getUserArenaInfo(userId);
		// if (userArenaInfo.getRefreshNum() >= 3) {
		if (!userService.reduceGold(userId, 5, ToolUseType.REDUCE_ARENA_REFRESH)) {
			throw new ServiceException(GOLD_NOT_ENOUGH, "元宝不足");
		}
		// }
		arenaUserId.remove(userId);
		userArenaInfo.setRefreshNum(userArenaInfo.getRefreshNum() + 1);
		this.userArenaInfoDao.update(userArenaInfo);
		ArenaDefiantBO bb = getArenaDefiantBOs(userArenaInfo, getRank(userId), 1);
		bb.setBuyNum(userArenaInfo.getBuyNum());

		List<ArenaDefiantBO> list = new ArrayList<ArenaDefiantBO>();
		list.add(bb);

		return list;
	}

	private void checkPkNum(UserArenaInfo userArenaInfo) {
		if (userArenaInfo.getPkNum() >= DAY_PK_TIMES) {
			int needBuyCount = userArenaInfo.getPkNum() + 1 - DAY_PK_TIMES;
			if (!userService.reduceGold(userArenaInfo.getUserId(), getReduceGoldNum(needBuyCount), ToolUseType.REDUCE_ARENA_DEFIANT)) {
				throw new ServiceException(GOLD_NOT_ENOUGH, "元宝不足");
			}
		}
	}

	private int getReduceGoldNum(int pkNum) {
		double num = pkNum;
		int temp = (int) (Math.ceil(num / 2)) * 20;
		return temp > 100 ? 100 : temp;
	}

	@Override
	public List<UserHeroBO> getArenaUserHeroBOList(String userId) {

		List<UserHeroBO> list = new ArrayList<UserHeroBO>();
		List<UserArenaHero> l = this.userArenaInfoDao.getUserArenaHero(userId);
		for (UserArenaHero userArenaHero : l) {
			UserHeroBO userHeroBO = new UserHeroBO();

			SystemHero systemHero = this.heroService.getSysHero(userArenaHero.getSystemHeroId());

			userHeroBO.setSystemHeroId(userArenaHero.getSystemHeroId());
			userHeroBO.setLife(userArenaHero.getLife());
			userHeroBO.setPhysicalAttack(userArenaHero.getAttack());
			userHeroBO.setPhysicalDefense(userArenaHero.getDefense());
			userHeroBO.setPlan(systemHero.getPlan());
			userHeroBO.setCareer(systemHero.getCareer());
			userHeroBO.setNormalPlan(systemHero.getNormalPlan());
			userHeroBO.setSkill1(systemHero.getSkill1());
			userHeroBO.setSkill2(systemHero.getSkill2());
			userHeroBO.setSkill3(systemHero.getSkill3());
			userHeroBO.setSkill4(systemHero.getSkill4());
			userHeroBO.setStar(systemHero.getHeroStar());
			userHeroBO.setPos(userArenaHero.getPos());
			userHeroBO.setHeroLevel(userArenaHero.getLevel());
			userHeroBO.setUserHeroId(userArenaHero.getUserHeroId());
			userHeroBO.setUserId(userArenaHero.getUserId());
			userHeroBO.setName(systemHero.getHeroName());

			list.add(userHeroBO);
		}
		return list;
	}

	@Override
	public boolean battle(final String attackUserId, long targetPid, final EventHandle handle) {
		final UserArenaInfo userArenaInfo = getUserArenaInfo(attackUserId);
		// 检查挑战次数
		checkPkNum(userArenaInfo);
		final User user = userService.get(attackUserId);
		BattleBO attack = new BattleBO();
		// 英雄列表
		List<BattleHeroBO> attackBO = this.heroService.getUserBattleHeroBOList(attackUserId);
		attack.setUserLevel(user.getLevel());
		attack.setBattleHeroBOList(attackBO);

		IUser defenseUser = null;
		boolean robot = false;
		try {
			defenseUser = userService.getByPlayerId(Long.toString(targetPid));
		} catch (Exception e) {
			robot = true;
			defenseUser = robotService.getById(targetPid);
		}
		BattleBO defense = new BattleBO();
		// 英雄列表
		List<BattleHeroBO> defenseBO = null;
		if (!robot) {
			List<UserHeroBO> userHeroBOList = this.getArenaUserHeroBOList(attackUserId);
			defenseBO = this.heroService.getUserArenaBattleHeroBOList(userHeroBOList, defenseUser.getUserId());
		}
		if (defenseBO == null || defenseBO.isEmpty()) {
			defenseBO = this.heroService.getUserBattleHeroBOList(defenseUser.getUserId());
		}

		defense.setBattleHeroBOList(defenseBO);
		defense.setUserLevel(defenseUser.getLevel());

		userArenaInfo.setPkNum(userArenaInfo.getPkNum() + 1);

		final boolean isRevenge = this.userArenaRecordLogDao.isEnemy(attackUserId, defenseUser.getUserId());

		if (isRevenge) {// 复仇
			this.userArenaRecordLogDao.deleteRevenge(attackUserId, defenseUser.getUserId());
		}

		ArenaEvent event = new ArenaEvent(attackUserId);
		eventService.dispatchEvent(event);

		dailyTaskService.sendUpdateDailyTaskEvent(attackUserId, SystemDailyTask.ARENA, 1);

		_battle(userArenaInfo, defenseUser, isRevenge, attack, defense, BattleType.ARENA, handle);

		return true;
	}

	private void _battle(final UserArenaInfo userArenaInfo, final IUser defenseUser, final Boolean isRevenge, BattleBO attack, BattleBO defense, int type, final EventHandle eventHandle) {
		battleService.fight(attack, defense, BattleType.ARENA, new EventHandle() {
			public boolean handle(Event event) {
				boolean isWin = false;
				if (event instanceof BattleResponseEvent) {
					UserArenaRecordLog userArenaRecordLog = new UserArenaRecordLog();
					userArenaRecordLog.setAttackUserId(userArenaInfo.getUserId());
					userArenaRecordLog.setCreatedTime(new Date());
					userArenaRecordLog.setDefenseUserId(defenseUser.getUserId());
					userArenaRecordLog.setIsRevenge(0);
					userArenaRecordLog.setUsername(defenseUser.getUsername());
					int score = 0;
					int flag = event.getInt("flag");
					if (flag == 1) {// 挑战打赢了
						userArenaInfo.setWinCount(userArenaInfo.getWinCount() + 1);
						if (userArenaInfo.getMaxWinCount() < userArenaInfo.getWinCount()) {
							userArenaInfo.setMaxWinCount(userArenaInfo.getWinCount());
						}
						userArenaInfo.setWinNum(userArenaInfo.getWinNum() + 1);
						userArenaRecordLog.setResult(1);
						score = isRevenge ? 10 * 2 : 10;
						isWin = true;
						arenaUserId.remove(userArenaInfo.getUserId());
						event.setObject("pls", getArenaDefiantBOs(userArenaInfo, getRank(userArenaInfo.getUserId()), 3));
						ArenaWinEvent arenaWinEvent = new ArenaWinEvent(userArenaInfo.getUserId());
						eventService.dispatchEvent(arenaWinEvent);
						userService.addSoul(userArenaInfo.getUserId(), 150, ToolUseType.ADD_SOUL_ARENA);
					} else {
						userArenaInfo.setWinCount(0);
						score = 5;
						userArenaRecordLog.setResult(0);
					}
					userArenaRecordLogDao.add(userArenaRecordLog);
					userArenaInfo.setScore(userArenaInfo.getScore() + score);

					userArenaInfoDao.update(userArenaInfo);
					userArenaInfoDao.setScore(userArenaInfo.getUserId(), userArenaInfo.getScore());

					// 是否有连胜礼包
					if (userArenaInfo.getWinCount() == 3) {
						event.setObject("iw", 3);
					} else if (userArenaInfo.getWinCount() == 5) {
						event.setObject("iw", 5);
					} else if (userArenaInfo.getWinNum() == 8) {
						event.setObject("iw", 8);
					}
					event.setObject("iw3", isReward(userArenaInfo.getUserId(), 3));
					event.setObject("iw5", isReward(userArenaInfo.getUserId(), 5));
					event.setObject("iw8", isReward(userArenaInfo.getUserId(), 8));
					// CommonDropBO dropBO =
					// crateCommonDropBO(userArenaInfo.getUserId(), winCount,
					// isWin, isReward);
					checkGift(userArenaInfo);
					event.setObject("uaf", get(userArenaInfo.getUserId()));
					// event.setObject("forcesDropBO", dropBO);
					event.setObject("bgid", 1);
					event.setObject("dun", defenseUser.getUsername());
					// dailyTaskService.sendUpdateDailyTaskEvent(userArenaInfo.getUserId(),
					// SystemDailyTask.PK, 1);
					eventHandle.handle(event);
				}
				return true;
			}
		});
	}

	static int[] giftCount = new int[] { 3, 5, 8 };

	static Map<String, List<Integer>> his = new ConcurrentHashMap<String, List<Integer>>();

	public static final int DAY_GIFT = -1;

	public void checkGift(UserArenaInfo info) {
		UserArenaSeriesGiftInfo gifts = userArenaInfoDao.getSeriesGifts(info.getUserId());
		for (int temp : giftCount) {
			if (temp == 8) { // 8次非连胜，临时改
				if (userArenaRewardLogDao.get(info.getUserId(), temp) == null) {
					if (info.getWinNum() >= temp) {
						UserArenaRewardLog userArenaRewardLog = new UserArenaRewardLog();
						userArenaRewardLog.setRewardType(temp);
						userArenaRewardLog.setUserId(info.getUserId());
						this.userArenaRewardLogDao.add(userArenaRewardLog);
						if (gifts.have(temp)) {
							UserArenaSeriesGift g = gifts.get(temp);
							g.addNum(1);
							userArenaInfoDao.updateSeriesGift(g);
						} else {
							UserArenaSeriesGift g = new UserArenaSeriesGift(info.getUserId(), temp, 1);
							gifts.add(g);
							userArenaInfoDao.insertSeriesGift(g);
						}
					}
				}
			} else if (temp <= info.getWinCount()) {
				if (userArenaRewardLogDao.get(info.getUserId(), temp) == null) {
					UserArenaRewardLog userArenaRewardLog = new UserArenaRewardLog();
					userArenaRewardLog.setRewardType(temp);
					userArenaRewardLog.setUserId(info.getUserId());
					this.userArenaRewardLogDao.add(userArenaRewardLog);
					if (gifts.have(temp)) {
						UserArenaSeriesGift g = gifts.get(temp);
						g.addNum(1);
						userArenaInfoDao.updateSeriesGift(g);
					} else {
						UserArenaSeriesGift g = new UserArenaSeriesGift(info.getUserId(), temp, 1);
						gifts.add(g);
						userArenaInfoDao.insertSeriesGift(g);
					}
				}
			}
		}
	}

	public List<UserArenaSeriesGift> showGift(String userId) {
		UserArenaSeriesGiftInfo info = userArenaInfoDao.getSeriesGifts(userId);
		return info.getList();
	}

	public CommonDropBO crateCommonDropBO(String userId, int winCount, boolean isWin, boolean isReward) {
		CommonDropBO commonDropBO = null;
		UserArenaSeriesGiftInfo info = userArenaInfoDao.getSeriesGifts(userId);
		UserArenaSeriesGift g = info.get(winCount);
		if (g == null || g.getNum() <= 0) {
			return commonDropBO;
		}
		g.addNum(-1);
		userArenaInfoDao.updateSeriesGift(g);
		if (winCount > 0) {
			List<GiftbagDropTool> gift = this.systemGiftbagDao.getGiftbagDropToolList(winCountMap.get(winCount));
			List<DropDescBO> dropDescBOList = DropDescHelper.toDropDesc(gift);
			commonDropBO = this.toolService.give(userId, dropDescBOList, ToolUseType.ARENA_CONTINUOUS_COUNT);
		} else {
			int randomNum = getRandom();
			SystemArenaGift systemArenaGift = this.systemArenaGiftDao.get(randomNum, Math.abs(winCount));
			List<GiftbagDropTool> gift = this.systemGiftbagDao.getGiftbagDropToolList(systemArenaGift.getGiftId());
			List<DropDescBO> dropDescBOList = DropDescHelper.toDropDesc(gift);
			commonDropBO = this.toolService.give(userId, dropDescBOList, ToolUseType.ARENA_CONTINUOUS_COUNT);
		}
		return commonDropBO;
	}

	public void init() {
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						/**
						 * 每天零点 1. 清零连胜纪录 2. 重置连胜礼包状态 3. 重置剩余挑战次数 4. 重置免费刷新次数
						 * 
						 */
						if (!DateUtils.isSameDay(lastClearTime, new Date())) {
							/*
							 * if (
							 * !userArenaInfoDao.isSendReward(DateUtils.getDate
							 * ())) { periodGroupId = 0; logger.info("每晚凌晨零点[" +
							 * DateUtils.getTime() + "]");
							 * userArenaInfoDao.addSendReward
							 * (DateUtils.getDate()); sendWeekReward();
							 * userArenaInfoDao.backup();// 备份用户信息表
							 * clearArenaRecord(); clearArenaScore(); }
							 */
							List<ArenaScoreRankBo> rank = getArenaScoreRankBos();
							logger.info("每天零点 1. 清零连胜纪录 2. 重置连胜礼包状态 3. 重置剩余挑战次数 4. 重置免费刷新次数[" + DateUtils.getTime() + "]");
							lastClearTime = new Date();
							userArenaRewardLogDao.clear();
							userArenaInfoDao.clear();
							userArenaInfoDao.clearGift();
							// 清空连胜礼包记录

						}
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

		}).start();
	}

	@Override
	public int buyNum(String userId, int num) {
		UserArenaInfo userArenaInfo = getUserArenaInfo(userId);
		int size = userArenaInfo.getBuyNum();
		int price = (int) (18 * (Math.pow(2, size)));
		if (!userService.reduceGold(userId, price > 288 ? 288 : price, ToolUseType.REDUCE_ARENA_BUY_NUM)) {
			throw new ServiceException(GOLD_NOT_ENOUGH, "元宝不足");
		}
		userArenaInfo.setPkNum(userArenaInfo.getPkNum() - num);
		userArenaInfo.setBuyNum(userArenaInfo.getBuyNum() + 1);
		userArenaInfoDao.update(userArenaInfo);
		int v = userArenaInfo.getPkNum() >= DAY_PK_TIMES ? 0 : DAY_PK_TIMES - userArenaInfo.getPkNum();
		return v;
	}
}
