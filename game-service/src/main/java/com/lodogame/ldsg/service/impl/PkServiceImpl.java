package com.lodogame.ldsg.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.jboss.netty.util.internal.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.PkAwardDao;
import com.lodogame.game.dao.RankScoreCfgDao;
import com.lodogame.game.dao.SystemVipLevelDao;
import com.lodogame.game.dao.UserDailyGainLogDao;
import com.lodogame.game.dao.UserDao;
import com.lodogame.game.dao.UserPkInfoDao;
import com.lodogame.game.utils.ScheduledUtils;
import com.lodogame.ldsg.bo.AwardDescBO;
import com.lodogame.ldsg.bo.BattleBO;
import com.lodogame.ldsg.bo.BattleHeroBO;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.DropDescBO;
import com.lodogame.ldsg.bo.PkPlayerBO;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.bo.UserPkInfoBO;
import com.lodogame.ldsg.constants.ActivityTargetType;
import com.lodogame.ldsg.constants.BattleType;
import com.lodogame.ldsg.constants.MailTarget;
import com.lodogame.ldsg.constants.PriceType;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.constants.ToolId;
import com.lodogame.ldsg.constants.ToolType;
import com.lodogame.ldsg.constants.ToolUseType;
import com.lodogame.ldsg.constants.UserDailyGainLogType;
import com.lodogame.ldsg.event.BaseEvent;
import com.lodogame.ldsg.event.BattleResponseEvent;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.PkEvent;
import com.lodogame.ldsg.event.PkRankEvent;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.helper.DropDescHelper;
import com.lodogame.ldsg.helper.HeroHelper;
import com.lodogame.ldsg.helper.LodoIDHelper;
import com.lodogame.ldsg.helper.PkHelper;
import com.lodogame.ldsg.service.ActivityTaskService;
import com.lodogame.ldsg.service.BattleService;
import com.lodogame.ldsg.service.DailyTaskService;
import com.lodogame.ldsg.service.EventService;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.MailService;
import com.lodogame.ldsg.service.PkService;
import com.lodogame.ldsg.service.PriceService;
import com.lodogame.ldsg.service.RobotService;
import com.lodogame.ldsg.service.ToolService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.ldsg.service.VipService;
import com.lodogame.model.IUser;
import com.lodogame.model.PkAward;
import com.lodogame.model.PkGiftDay;
import com.lodogame.model.RankScoreCfg;
import com.lodogame.model.RobotUser;
import com.lodogame.model.SystemDailyTask;
import com.lodogame.model.SystemHero;
import com.lodogame.model.SystemVipLevel;
import com.lodogame.model.User;
import com.lodogame.model.UserPkInfo;

public class PkServiceImpl implements PkService {

	private static final Logger logger = Logger.getLogger(PkServiceImpl.class);

	@Autowired
	private DailyTaskService dailyTaskService;

	@Autowired
	private UserPkInfoDao userPkInfoDao;

	@Autowired
	private UserService userService;

	@Autowired
	private BattleService battleService;

	@Autowired
	private PkAwardDao pkAwardDao;

	@Autowired
	private HeroService heroService;

	@Autowired
	private UserDao userDao;

	@Autowired
	private ToolService toolService;

	@Autowired
	private PriceService priceService;

	@Autowired
	private VipService vipServcie;

	@Autowired
	private EventService eventService;

	@Autowired
	private ActivityTaskService activityTaskService;

	@Autowired
	private RankScoreCfgDao rankScoreCfgDao;

	@Autowired
	private MailService mailService;

	@Autowired
	private RobotService robotService;

	@Autowired
	private SystemVipLevelDao systemVipLevelDao;

	@Autowired
	private UserDailyGainLogDao userDailyGainLogDao;

	private static boolean giftIng = false;

	private BlockingQueue<PkEvent> queue = new ArrayBlockingQueue<PkEvent>(1024);

	private static Map<String, List<Integer>> pkMaps = new ConcurrentHashMap<String, List<Integer>>();

	public void replace(String userId) {
		if (!userService.reduceCopper(userId, 100, ToolUseType.REDUCE_PK_REPLACE)) {
			throw new ServiceException(NO_MONEY, "金币不足");
		}
		pkMaps.remove(userId);
	}

	public void buyNum(String userId) {

		UserPkInfo info = userPkInfoDao.getByUserId(userId);

		int dailyBuyTimes = userDailyGainLogDao.getUserDailyGain(userId, UserDailyGainLogType.BUY_PK_TIMES);

		boolean buy = checkPkNum(userId, dailyBuyTimes);
		if (!buy) {
			throw new ServiceException(BUY_TIMES_LIMIT, "购买次数不足");
		}

		int price = priceService.getPrice(PriceType.PK_TIMES, dailyBuyTimes + 1);
		if (!userService.reduceGold(userId, price, ToolUseType.REDUCE_PK_BUY_TIMES)) {
			throw new ServiceException(NO_MONEY, "金币不足");
		}
		// 检查购买次数
		this.userDailyGainLogDao.addUserDailyGain(userId, UserDailyGainLogType.BUY_PK_TIMES, 1);
		// info.setBuyPkTimes(info.getBuyPkTimes() + 1);
		info.setPkTimes(info.getPkTimes() - 1);
		userPkInfoDao.update(info, null);
		return;

	}

	public boolean checkPkNum(String userId, int num) {
		User user = userService.get(userId);
		SystemVipLevel vip = systemVipLevelDao.get(user.getVipLevel());
		int max = vip.getPkBuyTimes();
		return max > num;
	}

	public void enter(String userId, EventHandle handle) {

		UserPkInfo userPkInfo = userPkInfoDao.getByUserId(userId);
		if (userPkInfo != null) {
			int pkNum = getPkTimes(userPkInfo);
			userPkInfo.setPkTimes(pkNum);
			Event event = new BaseEvent();
			event.setObject("userPkInfo", userPkInfo);
			handle.handle(event);
		} else {

			PkEvent event = new PkEvent(userId, 1, handle);
			addEvent(event);
		}

	}

	@Override
	public UserPkInfoBO getUserPkInfo(String userId) {

		UserPkInfo userPkInfo = userPkInfoDao.getByUserId(userId);
		if (userPkInfo != null) {
			return this.createUserPkInfoBo(userPkInfo);
		}

		return null;
	}

	private void addEvent(PkEvent event) {
		this.queue.add(event);
	}

	private PkEvent take() {
		try {
			return this.queue.take();
		} catch (InterruptedException ie) {
			throw new ServiceException(3000, ie.getMessage());
		}
	}

	/**
	 * 处理请求
	 * 
	 * @param event
	 */
	private void handle(PkEvent event) {

		if (event.getType() == PkEvent.EVENT_ENTER) {

			handleEnter(event);

		} else if (event.getType() == PkEvent.EVENT_FIGHT) {

			handleFight(event);
		}

	}

	/**
	 * 战斗
	 * 
	 * @param userId
	 * @param targtePid
	 * @param handle
	 */
	private void fight(final String userId, long targtePid, final EventHandle handle) {

		BattleBO attack = new BattleBO();

		User user = this.userService.get(userId);

		// 英雄列表
		List<BattleHeroBO> attackBO = this.heroService.getUserBattleHeroBOList(userId);
		attack.setUserLevel(user.getLevel());
		attack.setBattleHeroBOList(attackBO);

		final IUser defenseUser;
		List<BattleHeroBO> defenseBO;

		if (LodoIDHelper.isRobotLodoId(targtePid)) {
			defenseUser = this.robotService.getById(targtePid);
			defenseBO = this.robotService.getRobotUserBattleHeroBOList(defenseUser.getUserId());
		} else {
			defenseUser = userService.getByPlayerId(String.valueOf(targtePid));
			defenseBO = this.heroService.getUserBattleHeroBOList(defenseUser.getUserId());
		}
		BattleBO defense = new BattleBO();
		// 英雄列表
		defense.setBattleHeroBOList(defenseBO);
		defense.setUserLevel(defenseUser.getLevel());

		this.activityTaskService.updateActvityTask(userId, ActivityTargetType.PK, 1);

		battleService.fight(attack, defense, BattleType.PK, new EventHandle() {

			public boolean handle(Event event) {

				if (event instanceof BattleResponseEvent) {

					int flag = event.getInt("flag");

					CommonDropBO commonCropBO = handlePkEnd(userId, defenseUser.getUserId(), flag == 1);
					UserPkInfo userPkInfo = userPkInfoDao.getByUserId(userId);

					int pkTimes = 10;

					event.setObject("forcesDropBO", commonCropBO);
					event.setObject("bgid", 1);
					event.setObject("dun", defenseUser.getUsername());
					event.setObject("pkt", pkTimes - userPkInfo.getPkTimes());

					handle.handle(event);

				}
				return true;
			}
		});

	}

	/**
	 * 处理伦剑挑战结束
	 * 
	 * @param userId
	 * @param win
	 * @return
	 */
	private CommonDropBO handlePkEnd(String userId, String targetUserId, boolean win) {

		CommonDropBO commonDropBO = new CommonDropBO();

		UserPkInfo userPkInfo = this.userPkInfoDao.getByUserId(userId);
		UserPkInfo targetUserPkInfo = this.userPkInfoDao.getByUserId(targetUserId);

		int rank = userPkInfo.getRank();
		int newRank = rank;
		int rankUp = 0;
		commonDropBO.setLastFastRank(userPkInfo.getFastRank());

		userPkInfo.setPkTimes(userPkInfo.getPkTimes() + 1);

		// 给“挑战竞技场N次”的每日任务加上1次完成次数
		dailyTaskService.sendUpdateDailyTaskEvent(userId, SystemDailyTask.PK, 1);

		if (win) {

			int targetRank = targetUserPkInfo.getRank();
			if (rank > targetRank) {
				newRank = targetRank;
				userPkInfo.setRank(targetRank);
				targetUserPkInfo.setRank(rank);
				rankUp = rank - targetRank;
			}
			if (userPkInfo.getFastRank() == 0) {
				int yb = pkAwardDao.getUpGift(rank, userPkInfo.getRank());
				User u = userService.get(userPkInfo.getUserId());
				String toolIds = ToolType.GOLD + "," + ToolId.TOOL_GLOD_ID + "," + yb + "|";
				mailService.send("竞技场排名上升奖励", "您在竞技场中排名从" + rank + "名上升到" + userPkInfo.getRank() + "名，获得奖励" + yb + "钻石", toolIds, MailTarget.USERS, u.getLodoId() + "", null,
						new Date(), null);
				logger.warn("最佳名次刷新，发放奖励" + u.getUserId() + "=" + yb);
				commonDropBO.setGold(yb);
				userPkInfo.setFastRank(userPkInfo.getRank());
			}
			if (targetUserPkInfo.getFastRank() == 0) {
				targetUserPkInfo.setFastRank(targetUserPkInfo.getRank());
			}

			if (userPkInfo.getFastRank() > userPkInfo.getRank()) {
				// 更新最佳排名，
				int yb = pkAwardDao.getUpGift(userPkInfo.getFastRank(), userPkInfo.getRank());
				User u = userService.get(userPkInfo.getUserId());
				String toolIds = ToolType.GOLD + "," + ToolId.TOOL_GLOD_ID + "," + yb + "|";
				mailService.send("竞技场排名上升奖励", "您在竞技场中排名从" + userPkInfo.getFastRank() + "名上升到" + userPkInfo.getRank() + "名，获得奖励" + yb + "钻石", toolIds, MailTarget.USERS,
						u.getLodoId() + "", null, new Date(), null);
				logger.warn("最佳名次刷新，发放奖励" + u.getUserId() + "=" + yb);
				commonDropBO.setGold(yb);
				userPkInfo.setFastRank(userPkInfo.getRank());
			}
			if (targetUserPkInfo.getFastRank() > targetUserPkInfo.getRank()) {
				targetUserPkInfo.setFastRank(targetUserPkInfo.getRank());
			}

			pkMaps.remove(userId);
			pkMaps.remove(targetUserId);

			this.userPkInfoDao.update(userPkInfo, targetUserId);

			PkRankEvent event = new PkRankEvent(userPkInfo.getUserId(), targetRank);
			eventService.dispatchEvent(event);

			this.userPkInfoDao.update(targetUserPkInfo, targetUserPkInfo.getUserId());

		} else {
			this.userPkInfoDao.update(userPkInfo, null);
		}

		int score = win ? 10 : 5;
		this.userService.addReputation(userId, score, ToolUseType.ADD_PK_WIN);
		commonDropBO.setScore(score);
		commonDropBO.setRank(newRank);
		commonDropBO.setUprank(rankUp);

		return commonDropBO;
	}

	/**
	 * 处理挑战
	 * 
	 * @param pkEvent
	 */
	private void handleFight(PkEvent pkEvent) {

		String userId = pkEvent.getUserId();

		long targetPid = pkEvent.getInt("targetId");

		this.fight(userId, targetPid, pkEvent.getHandle());

	}

	/**
	 * 获取pk次数
	 * 
	 * @param userPkInfo
	 * @return
	 */
	private int getPkTimes(UserPkInfo userPkInfo) {

		Date now = new Date();

		int pkTimes = userPkInfo.getPkTimes();
		Date lastUpdatePkTime = userPkInfo.getUpdatePkTime();
		if (!DateUtils.isSameDay(now, lastUpdatePkTime)) {
			pkTimes = 0;
			userPkInfo.setPkTimes(pkTimes);
			userPkInfo.setUpdatePkTime(now);
			this.userPkInfoDao.update(userPkInfo, null);
		}

		return pkTimes;

	}

	/**
	 * 处理进入
	 * 
	 * @param event
	 */
	private void handleEnter(PkEvent pkEvent) {

		Date now = new Date();

		String userId = pkEvent.getUserId();

		User user = this.userService.get(userId);

		int lastRank = this.userPkInfoDao.getLastRank();
		UserPkInfo userPkInfo = new UserPkInfo();
		userPkInfo.setUserId(userId);
		userPkInfo.setUsername(user.getUsername());
		userPkInfo.setBuyPkTimes(0);
		userPkInfo.setLastBuyTime(now);
		userPkInfo.setPkTimes(0);
		userPkInfo.setLodoId(user.getLodoId());
		userPkInfo.setImgId(user.getImgId());
		userPkInfo.setRank(lastRank + 1);
		userPkInfo.setUpdatePkTime(now);
		userPkInfo.setLevel(user.getLevel());

		this.userPkInfoDao.add(userPkInfo);

		BaseEvent event = new BaseEvent();
		event.setObject("userPkInfo", userPkInfo);

		pkEvent.getHandle().handle(event);

	}

	@Override
	public UserPkInfoBO createUserPkInfoBo(UserPkInfo userPkInfo) {
		UserPkInfoBO userPkInfoBO = new UserPkInfoBO();

		userPkInfoBO.setImgId(userPkInfo.getImgId());

		int pkTimes = 10;
		userPkInfoBO.setPkTimes(pkTimes - userPkInfo.getPkTimes());

		int dailyBuyPkTimes = this.userDailyGainLogDao.getUserDailyGain(userPkInfo.getUserId(), UserDailyGainLogType.BUY_PK_TIMES);
		userPkInfoBO.setBuyPkTimes(dailyBuyPkTimes);
		int price = this.priceService.getPrice(PriceType.PK_TIMES, dailyBuyPkTimes + 1);
		userPkInfoBO.setBuyPkTimesMoney(price);

		userPkInfoBO.setPlayerId(userPkInfo.getLodoId());
		userPkInfoBO.setRank(userPkInfo.getRank());
		userPkInfoBO.setFastRank(userPkInfo.getFastRank());
		int score = 100;
		RankScoreCfg rankScoreCfg = rankScoreCfgDao.getByRank(userPkInfo.getRank());
		if (rankScoreCfg != null) {
			score = rankScoreCfg.getScore();
		}
		userPkInfoBO.setGainRepAmount(score);

		return userPkInfoBO;
	}

	@Override
	public List<PkPlayerBO> getChallengeList(int rank, String uid) {

		List<PkPlayerBO> list = new ArrayList<PkPlayerBO>();

		List<Integer> attackAbleList = pkMaps.get(uid);
		if (attackAbleList == null || attackAbleList.size() == 0) {
			attackAbleList = new ArrayList<Integer>();
			PkHelper.setAttackAbleList(attackAbleList, rank);
			pkMaps.put(uid, attackAbleList);
		}

		for (int r : attackAbleList) {

			UserPkInfo userPkInfo = this.userPkInfoDao.getByRank(r);
			if (userPkInfo == null) {
				continue;
			}
			if (userPkInfo.getUserId().equals(uid)) {
				logger.error("竞技场对手异常----------->user:" + uid + ",rank:" + rank + "--->VS" + "--->user-------------->" + userPkInfo.getUserId() + "-->rank:" + r);
				continue;
			}
			PkPlayerBO pkPlayerBO = this.createPkPlayerBO(userPkInfo);
			list.add(pkPlayerBO);
		}
		return list;
	}

	@Override
	public List<AwardDescBO> getAwardList() {

		List<AwardDescBO> boList = new ArrayList<AwardDescBO>();
		List<PkAward> list = pkAwardDao.getAll();
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				PkAward pkAward = list.get(i);
				AwardDescBO bo = new AwardDescBO();
				bo.setAwardId(pkAward.getAwardId());
				bo.setName(pkAward.getName());
				bo.setScore(pkAward.getScore());
				bo.setImgId(pkAward.getImgId());
				bo.setDescription(pkAward.getDescription());

				List<DropDescBO> dropDescBOList = DropDescHelper.parseDropTool(pkAward.getTools());
				if (!dropDescBOList.isEmpty()) {
					bo.setToolId(dropDescBOList.get(0).getToolId());
					bo.setToolType(dropDescBOList.get(0).getToolType());
				} else {
					bo.setToolId(0);
					bo.setToolType(0);
				}

				boList.add(bo);
			}
		}
		return boList;
	}

	private PkPlayerBO createPkPlayerBO(UserPkInfo userPkInfo) {

		PkPlayerBO pkPlayerBO = new PkPlayerBO();
		pkPlayerBO.setNickname(userPkInfo.getUsername());
		pkPlayerBO.setPlayerId(userPkInfo.getLodoId());
		pkPlayerBO.setRank(userPkInfo.getRank());

		if (LodoIDHelper.isRobotLodoId(userPkInfo.getLodoId())) {

			RobotUser robotUser = this.robotService.getById(userPkInfo.getLodoId());

			List<UserHeroBO> list = robotService.getRobotUserHeroBOList(userPkInfo.getUserId());
			UserHeroBO userHeroBO = list.get(0);
			SystemHero systemHero = this.heroService.getSysHero(userHeroBO.getSystemHeroId());
			pkPlayerBO.setImg(systemHero.getImgId());
			pkPlayerBO.setColor(systemHero.getHeroColor());
			pkPlayerBO.setLevel(userPkInfo.getLevel());

			pkPlayerBO.setAtt(robotUser.getCapability());
			
			

		} else {
			SystemHero systemHero = this.userService.getSystemHero(userPkInfo.getUserId());
			logger.debug("systemHeroId: " + systemHero.getSystemHeroId());
			pkPlayerBO.setImg(systemHero.getImgId());
			pkPlayerBO.setColor(systemHero.getHeroColor());
			pkPlayerBO.setLevel(userPkInfo.getLevel());
			List<UserHeroBO> hs = heroService.getUserHeroList(userPkInfo.getUserId(), 1);
			int v = HeroHelper.getCapability(hs);
			pkPlayerBO.setAtt(v);
		}
		return pkPlayerBO;

	}

	private void worker() {

		while (true) {

			try {

				PkEvent event = this.take();
				if (event != null) {
					handle(event);
				}

			} catch (Throwable t) {
				logger.error(t.getMessage(), t);
				try {
					Thread.sleep(1000 * 5);
				} catch (InterruptedException ie) {
					logger.debug(ie);
				}
			}
		}

	}

	@Override
	public CommonDropBO exchange(String userId, int exchangeId, int num) {

		if (num <= 0) {
			throw new ServiceException(ServiceReturnCode.FAILD, "数据异常，传了非正数");
		}

		PkAward pkAward = pkAwardDao.getById(exchangeId);

		int needScore = pkAward.getScore() * num;

		if (this.userService.reduceReputation(userId, needScore, ToolUseType.REDUCE_PK_EXCHANGE) == false) {
			throw new ServiceException(REPUTATION_NOT_ENOUGH, "声望不足");
		}

		CommonDropBO commonDropBO = this.toolService.give(userId, pkAward.getTools(), num, ToolUseType.ADD_PK_EXCHANGE);

		return commonDropBO;
	}

	@Override
	public void fight(String userId, long targetId, boolean useMoney, EventHandle handle) {
		if (giftIng) {
			throw new ServiceException(GIFTING, "昨日奖励结算中");
		}
		UserPkInfo userPkInfo = this.userPkInfoDao.getByUserId(userId);
		if (userPkInfo != null) {
			int pkTimes = this.getPkTimes(userPkInfo);
			int times = 10;
			if (pkTimes >= times) {
				throw new ServiceException(FIGHT_TIMES_LIMIT, "争霸挑战失败，挑战次数不足.userId[" + userId + "]");
			}
		}

		PkEvent event = new PkEvent(userId, PkEvent.EVENT_FIGHT, handle);
		event.setObject("targetId", targetId);
		this.addEvent(event);

		// 分发
		this.eventService.dispatchEvent(event);

	}

	public void init() {

		logger.info("开始线程 ");

		new Thread(new Runnable() {

			public void run() {
				worker();
			}

		}).start();

		ScheduledUtils.schelduleAtFixed(new CheckGiftSend(), TimeUnit.MINUTES, 1);

	}

	@Override
	public List<PkPlayerBO> getTenList() {
		List<PkPlayerBO> list = new ArrayList<PkPlayerBO>();

		for (int r : PkHelper.tenList) {

			UserPkInfo userPkInfo = this.userPkInfoDao.getByRank(r);
			if (userPkInfo == null) {
				continue;
			}

			PkPlayerBO pkPlayerBO = this.createPkPlayerBO(userPkInfo);
			list.add(pkPlayerBO);
		}

		return list;
	}

	class CheckGiftSend implements Runnable {

		public void run() {
			if (com.lodogame.game.utils.DateUtils.isThisTime(0, 2)) {
				try {
					giftIng = true;
					List<PkGiftDay> gift = pkAwardDao.getDayGift();
					// PkGiftDay lastGift = null;
					for (PkGiftDay temp : gift) {
						logger.debug("PkService.CheckGiftSend---rank--->" + temp.getRank() + "---gift--->yb:" + temp.getGiftYb() + " score:" + temp.getGiftScore());
						UserPkInfo info = userPkInfoDao.getByRank(temp.getRank());
						if (info != null) {

							if (LodoIDHelper.isRobotLodoId(info.getLodoId())) {
								continue;
							}

							User u = userService.get(info.getUserId());
							String toolIds = ToolType.GOLD + "," + ToolId.TOOL_GLOD_ID + "," + temp.getGiftYb() + "|";
							String toolIds2 = ToolType.REPUTATION + "," + ToolId.TOOL_REPUTATION_ID + "," + temp.getGiftScore() + "|";
							mailService.send("竞技场排名奖励", "您在竞技场获得了" + temp.getRank() + "名,获得了奖励", toolIds + toolIds2 + temp.getGift(), MailTarget.USERS, u.getLodoId() + "", null,
									new Date(), null);
						}

					}

				} catch (Exception ex) {
					logger.error("PkService.CheckGiftSend---ERROR--->" + ex.getMessage() + "--" + ex.getLocalizedMessage());
				} finally {
					giftIng = false;
					logger.error("PkService.CheckGiftSend---Over");
				}
			}
		}

	}
}
