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

import com.lodogame.game.dao.CommandDao;
import com.lodogame.game.dao.ContestDao;
import com.lodogame.game.dao.RankDao;
import com.lodogame.game.dao.SystemGiftbagDao;
import com.lodogame.game.dao.SystemToolDropDao;
import com.lodogame.game.dao.UserDao;
import com.lodogame.game.remote.callback.Callback;
import com.lodogame.game.remote.handle.RemoteCallHandle;
import com.lodogame.game.remote.request.Request;
import com.lodogame.game.remote.response.Response;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.game.utils.IDGenerator;
import com.lodogame.game.utils.json.Json;
import com.lodogame.ldsg.bo.BattleBO;
import com.lodogame.ldsg.bo.BattleHeroBO;
import com.lodogame.ldsg.bo.ContestHistoryBO;
import com.lodogame.ldsg.bo.ContestPairBO;
import com.lodogame.ldsg.bo.ContestScheduleBO;
import com.lodogame.ldsg.bo.ContestTopUserBO;
import com.lodogame.ldsg.bo.ContestUserBO;
import com.lodogame.ldsg.bo.ContestUserHeroBO;
import com.lodogame.ldsg.bo.MergeSkillBO;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.config.Config;
import com.lodogame.ldsg.constants.BattleType;
import com.lodogame.ldsg.constants.CommandType;
import com.lodogame.ldsg.constants.ContestStatus;
import com.lodogame.ldsg.constants.ContestWorldStatus;
import com.lodogame.ldsg.constants.MailTarget;
import com.lodogame.ldsg.event.BaseEvent;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.helper.ContestHelper;
import com.lodogame.ldsg.helper.HeroHelper;
import com.lodogame.ldsg.helper.ToolHelper;
import com.lodogame.ldsg.helper.WorldContestHelper;
import com.lodogame.ldsg.service.BattleService;
import com.lodogame.ldsg.service.ContestService;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.MailService;
import com.lodogame.ldsg.service.MessageService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.Command;
import com.lodogame.model.ContestBattleReport;
import com.lodogame.model.ContestHistory;
import com.lodogame.model.ContestInfo;
import com.lodogame.model.ContestReward;
import com.lodogame.model.ContestUser;
import com.lodogame.model.ContestUserHero;
import com.lodogame.model.OnlyoneWeekRank;
import com.lodogame.model.SystemHero;
import com.lodogame.model.SystemToolDrop;
import com.lodogame.model.User;
import com.lodogame.model.log.ContestRewardLog;

public class ContestServiceImpl implements ContestService {

	@Autowired
	private ContestDao contestDao;

	private static final Logger logger = Logger.getLogger(ContestServiceImpl.class);

	/**
	 * 当前是本服比赛
	 */
	public static final int RUNNING_LOCAL = 1;

	/**
	 * 当前是跨服比赛
	 */
	public static final int RUNNING_WORLD = 2;

	boolean started = false;

	/**
	 * 比赛场数
	 */
	private int matchCount;

	/**
	 * 已完成比赛场数
	 */
	private int finishMatchCount;

	/**
	 * 下个阶段开始时间
	 */
	private Date nextStatusTime = null;

	private int runningType = RUNNING_LOCAL;

	/**
	 * 本服的状态
	 */
	private ContestStatus status = ContestStatus.NOT_START;

	/**
	 * 跨服的状态
	 */
	private ContestWorldStatus worldStatus = ContestWorldStatus.NOT_START;

	/**
	 * 比赛要开始的走马灯
	 */
	private static String MATCH_START_MSG = "尊敬的各位英雄，本服巅峰战将在20:00拉开战幕，请获得参赛资格的英雄准时进入！";

	private static String MATCH_END_MSG = "还有${min}分钟，本服巅峰最终战即将开幕，为了最后的荣誉，战斗吧！";

	/**
	 * 记录哪些队已经出过场了
	 */
	private Map<String, Set<Integer>> finishTeams = new HashMap<String, Set<Integer>>();

	/**
	 * 胜利记录
	 */
	private Map<String, Map<Integer, Integer>> winState = new HashMap<String, Map<Integer, Integer>>();

	/**
	 * 参加跨服战需要的最小武将等级
	 */
	private static final int MIN_HERO_LEVEL = 1;

	/**
	 * 最后发走马为相的时间
	 */
	private Set<Integer> msgSet = new HashSet<Integer>();

	/**
	 * 比赛锁
	 */
	private Object matchLock = new Object();

	@Autowired
	private BattleService battleService;

	@Autowired
	private UserService userService;

	@Autowired
	private HeroService heroService;

	@Autowired
	private MailService mailService;

	@Autowired
	private UserDao userDao;

	@Autowired
	private SystemGiftbagDao systemGiftbagDao;

	@Autowired
	private MessageService messageService;

	@Autowired
	private RankDao rankDao;

	@Autowired
	private CommandDao commandDao;

	@Autowired
	private RemoteCallHandle remoteCallHandle;

	@Autowired
	private SystemToolDropDao systemToolDropDao;

	@Override
	public void enter(String userId, final EventHandle handle) {

		if (runningType == RUNNING_LOCAL) {
			enterLocal(userId, handle);
		} else {
			enterRemote(userId, handle);
		}

	}

	private void enterLocal(String userId, EventHandle handle) {

		ContestUser contestUser = this.contestDao.get(userId);
		if (contestUser != null && this.status != ContestStatus.NOT_START) {

			// 如果还没布好阵
			if (contestUser.getArrayFinish() == 0) {
				this.updateContestUserHero(userId);
			}
		}

		int round = ContestHelper.getClientRound(status);

		BaseEvent event = new BaseEvent();
		event.setObject("status", ContestHelper.toClientStatus(status).getValue());
		event.setObject("round", round);
		event.setObject("rt", runningType);
		event.setObject("nst", this.getNextStatusTime().getTime());

		// 1. 正常 2 没有报名资格 3.已经被淘态
		int st = 1;
		if (contestUser == null) {
			st = 2;
		} else if (contestUser.getDeadRound() != 100) {
			st = 3;
		}

		event.setObject("st", st);

		if (st == 1 && this.status != ContestStatus.NOT_START) {

			event.setObject("arrfn", contestUser.getArrayFinish());
			event.setObject("carr", contestUser.getSelectTeam());
			event.setObject("enarr", getEnableArray(this.finishTeams.get(userId)));
			event.setObject("wininfo", getWinInfo(userId, round));
			event.setObject("uinfo", this.getTargetInfo(userId));

			event.setObject("hls", getContestUserHeroBOList(userId));

		}

		handle.handle(event);

	}

	private List<Integer> getEnableArray(Set<Integer> finishArray) {
		List<Integer> enableTeam = new ArrayList<Integer>();
		for (int i = 1; i <= 3; i++) {
			if (finishArray == null || !finishArray.contains(i)) {
				enableTeam.add(i);
			}
		}
		return enableTeam;
	}

	private void enterRemote(final String userId, final EventHandle handle) {

		Request request = new Request();
		request.setAction("ContestRemote");
		request.setMethod("enter");
		request.put("uid", userId);

		final ContestUser contestUser = this.contestDao.get(userId);

		this.remoteCallHandle.call(request, new Callback() {

			@Override
			public void handle(Response resp) {

				BaseEvent event = new BaseEvent();

				int st = resp.get("st", 0);

				event.setObject("st", st);
				event.setObject("status", resp.getObject("status"));
				event.setObject("round", resp.getObject("round"));
				event.setObject("rt", runningType);
				event.setObject("nst", resp.getObject("nst"));

				if (st == 1) {
					event.setObject("arrfn", contestUser.getArrayFinish());
					event.setObject("carr", contestUser.getSelectTeam());
					event.setObject("enarr", getEnableArray(finishTeams.get(userId)));
					event.setObject("wininfo", resp.getObject("wininfo"));
					event.setObject("uinfo", resp.getObject("uinfo", ContestUserBO.class));

					event.setObject("hls", getContestUserHeroBOList(userId));
				}

				handle.handle(event);
			}
		});

	}

	@Override
	public void getTargetInfo(String userId, final EventHandle handle) {

		ContestUserBO contestUserBO = null;

		if (runningType == RUNNING_LOCAL) {
			BaseEvent event = new BaseEvent();
			contestUserBO = this.getTargetInfo(userId);
			event.setObject("uinfo", contestUserBO);
			handle.handle(event);
		} else {

			Request request = new Request();
			request.setAction("ContestRemote");
			request.setMethod("getTargetInfo");
			request.put("uid", userId);

			this.remoteCallHandle.call(request, new Callback() {

				@Override
				public void handle(Response resp) {

					BaseEvent event = new BaseEvent();
					event.setObject("uinfo", resp.getObject("uinfo", ContestUserBO.class));
					handle.handle(event);
				}
			});

		}
	}

	@Override
	public void getTopUserList(final EventHandle handle) {

		Request request = new Request();
		request.setAction("ContestRemote");
		request.setMethod("getTopUserList");

		this.remoteCallHandle.call(request, new Callback() {

			@Override
			public void handle(Response resp) {

				BaseEvent event = new BaseEvent();
				event.setObject("list", resp.getList("list", ContestTopUserBO.class));
				handle.handle(event);
			}
		});

	}

	private List<ContestScheduleBO> getScheduleList() {

		List<ContestScheduleBO> l = new ArrayList<ContestScheduleBO>();
		List<ContestUser> list = this.contestDao.getList();

		for (ContestUser u : list) {

			ContestScheduleBO bo = new ContestScheduleBO();
			bo.setDeadRound(u.getDeadRound());
			bo.setInd(u.getInd());

			if (u.getIsEmpty() == 1) {
				bo.setUserId("");
				bo.setUsername("");
			} else {
				bo.setUsername(u.getUsername());
				bo.setUserId(u.getUserId());
			}

			l.add(bo);
		}

		return l;
	}

	@Override
	public void getScheduleList(final EventHandle handle) {

		if (runningType == RUNNING_LOCAL) {
			BaseEvent event = new BaseEvent();
			event.setObject("rt", runningType);
			event.setObject("round", ContestHelper.getClientRound(status));
			event.setObject("list", this.getScheduleList());
			handle.handle(event);
		} else {

			Request request = new Request();
			request.setAction("ContestRemote");
			request.setMethod("getScheduleList");

			this.remoteCallHandle.call(request, new Callback() {

				@Override
				public void handle(Response resp) {

					BaseEvent event = new BaseEvent();
					event.setObject("rt", runningType);
					event.setObject("round", WorldContestHelper.getClientRound(worldStatus));
					event.setObject("list", resp.getList("list", ContestScheduleBO.class));
					handle.handle(event);
				}
			});
		}
	}

	@Override
	public void getHeros(String userId, final EventHandle handle) {

		if (runningType == RUNNING_LOCAL) {
			BaseEvent event = new BaseEvent();

			ContestUser contestUser = this.contestDao.get(userId);
			if (contestUser != null) {
				event.setObject("lv", contestUser.getLevel());
				event.setObject("vip", contestUser.getVip());
				event.setObject("name", contestUser.getUsername());
			}

			event.setObject("list", this.getHeros(userId));
			handle.handle(event);
		} else {

			Request request = new Request();
			request.setAction("ContestRemote");
			request.setMethod("getHeros");
			request.put("uid", userId);

			this.remoteCallHandle.call(request, new Callback() {

				@Override
				public void handle(Response resp) {

					BaseEvent event = new BaseEvent();
					event.setObject("lv", resp.get("lv", 0));
					event.setObject("vip", resp.get("vip", 0));
					event.setObject("name", resp.get("name", ""));
					event.setObject("list", resp.getList("list", ContestUserHeroBO.class));
					handle.handle(event);
				}
			});
		}
	}

	@Override
	public List<ContestUserHeroBO> getHeros(String userId) {
		List<ContestUserHeroBO> l = this.getContestUserHeroBOList(userId);
		List<ContestUserHeroBO> ll = new ArrayList<ContestUserHeroBO>();
		for (ContestUserHeroBO bo : l) {
			if (bo.getPos() > 0) {
				ll.add(bo);
			}
		}

		return ll;
	}

	@Override
	public void getHistorys(String userId, final EventHandle handle) {

		if (runningType == RUNNING_LOCAL) {
			BaseEvent event = new BaseEvent();
			event.setObject("list", this.getHistorys(userId));
			handle.handle(event);
		} else {

			Request request = new Request();
			request.setAction("ContestRemote");
			request.setMethod("getHistorys");
			request.put("uid", userId);

			this.remoteCallHandle.call(request, new Callback() {

				@Override
				public void handle(Response resp) {

					BaseEvent event = new BaseEvent();
					event.setObject("list", resp.getList("list", ContestHistoryBO.class));
					handle.handle(event);
				}
			});
		}
	}

	private List<ContestHistoryBO> getHistorys(String userId) {

		List<ContestHistoryBO> l = new ArrayList<ContestHistoryBO>();

		List<ContestHistory> list = this.contestDao.getContestHistory(userId);
		for (ContestHistory history : list) {

			ContestHistoryBO bo = new ContestHistoryBO();

			bo.setFlag(history.getWinCount() >= 2 ? 1 : 2);
			bo.setPower(history.getTargetPower());
			bo.setTargetUsername(history.getTargetUsername());
			bo.setTitle1(ContestHelper.getTitle1(history.getRound(), history.getWinCount() >= 2));
			bo.setTitle2(ContestHelper.getTitle2(history.getWinCount()));

			l.add(bo);
		}

		return l;
	}

	@Override
	public List<ContestUserHeroBO> getContestUserHeroBOList(String userId) {

		List<ContestUserHero> list = this.contestDao.getHeroList(userId);
		List<ContestUserHeroBO> boList = new ArrayList<ContestUserHeroBO>();

		for (ContestUserHero hero : list) {

			ContestUserHeroBO bo = new ContestUserHeroBO();

			bo.setLevel(hero.getLevel());
			bo.setPos(hero.getPos());
			bo.setTeam(hero.getTeam());
			bo.setSystemHeroId(hero.getSystemHeroId());
			bo.setUserHeroId(hero.getUserHeroId());
			bo.setAttack(hero.getAttack());
			bo.setDefense(hero.getDefense());
			bo.setLife(hero.getLife());

			boList.add(bo);
		}

		return boList;
	}

	/**
	 * 初始化或者更新用户武将列表
	 * 
	 * @param userId
	 */
	public void updateContestUserHero(String userId) {

		List<UserHeroBO> list = this.heroService.getUserHeroList(userId, 0, MIN_HERO_LEVEL);
		int count = 0;
		for (UserHeroBO userHeroBO : list) {
			ContestUserHero contestUserHero = this.contestDao.getContestUserHero(userHeroBO.getUserHeroId());
			if (contestUserHero == null) {
				contestUserHero = new ContestUserHero();
			}
			copyProperty(userHeroBO, contestUserHero);
			this.contestDao.replace(contestUserHero);

			count += 1;
			if (testArrayFinish == true && count >= 18) {
				break;
			}
		}
	}

	/**
	 * 复制属性
	 * 
	 * @param userHeroBO
	 * @param contestUserHero
	 */
	private void copyProperty(UserHeroBO userHeroBO, ContestUserHero contestUserHero) {
		contestUserHero.setAttack(userHeroBO.getPhysicalAttack());
		contestUserHero.setBogey(userHeroBO.getBogey());
		contestUserHero.setCreatedTime(new Date());
		contestUserHero.setCrit(userHeroBO.getCrit());
		contestUserHero.setDefense(userHeroBO.getPhysicalDefense());
		contestUserHero.setDodge(userHeroBO.getDodge());
		contestUserHero.setHit(userHeroBO.getHit());
		contestUserHero.setLevel(userHeroBO.getHeroLevel());
		contestUserHero.setLife(userHeroBO.getLife());
		contestUserHero.setParry(userHeroBO.getParry());
		contestUserHero.setStarLevel(userHeroBO.getStarLevel());
		contestUserHero.setSystemHeroId(userHeroBO.getSystemHeroId());
		contestUserHero.setToughness(userHeroBO.getToughness());
		contestUserHero.setUserHeroId(userHeroBO.getUserHeroId());
		contestUserHero.setUserId(userHeroBO.getUserId());
	}

	@Override
	public boolean savePos(String userId, Map<String, Integer> m1, Map<String, Integer> m2, Map<String, Integer> m3) {

		List<ContestUserHero> list = this.contestDao.getHeroList(userId);

		for (ContestUserHero hero : list) {

			if (m1.containsKey(hero.getUserHeroId())) {
				hero.setTeam(1);
				hero.setPos(m1.get(hero.getUserHeroId()));
			} else if (m2.containsKey(hero.getUserHeroId())) {
				hero.setTeam(2);
				hero.setPos(m2.get(hero.getUserHeroId()));
			} else if (m3.containsKey(hero.getUserHeroId())) {
				hero.setTeam(3);
				hero.setPos(m3.get(hero.getUserHeroId()));
			} else {
				hero.setTeam(0);
				hero.setPos(0);
			}
			this.contestDao.updateHeroPosAndTeam(hero.getUserHeroId(), hero.getTeam(), hero.getPos());
		}

		ContestUser contestUser = this.contestDao.get(userId);
		if (contestUser != null) {
			this.contestDao.updateArrayFinish(userId);
		} else {
			logger.error("没有用户的报名信息.userId[" + userId + "]");
		}

		return true;
	}

	@Override
	public boolean selectTeam(String userId, int team) {

		this.contestDao.updateSelectTeam(userId, team);

		return true;
	}

	private void statusCheck() {

		Date now = new Date();

		// 消息发送
		checkSendMsg();

		if (now.after(this.getNextStatusTime())) {

			if (status.isMatchStatus()) {// 比赛回合的话要等比赛完

				if (this.matchCount == 0) {
					switchStatus();
				} else {
					logger.debug("[" + Thread.currentThread().getName() + "]比赛进行中,剩余比赛场数[" + this.matchCount + "],已完成比赛场数[" + this.finishMatchCount + "]");
				}

			} else {
				switchStatus();
			}

		}

		logger.debug("[" + Thread.currentThread().getName() + "]当前状态[" + this.status.getDesc() + "], 当前时间[" + DateUtils.getDate3(now) + "], 状态切换时间[" + DateUtils.getDate3(this.getNextStatusTime())
				+ "]");

	}

	/**
	 * 获取下一阶段开始的时间
	 * 
	 * @return
	 */
	public Date getNextStatusTime() {

		if (nextStatusTime == null) {
			if (Config.ins().isDebug()) {
				nextStatusTime = DateUtils.add(new Date(), Calendar.MINUTE, 0);
				new Date();
			} else {
				nextStatusTime = new Date(DateUtils.getTimeByDayOfWeek("6 20:00"));
			}
		}

		return nextStatusTime;
	}

	/**
	 * 进行一轮比赛
	 */
	private void runMatch() {

		int round = ContestHelper.getRound(status);
		int index = ContestHelper.getIndex(status);
		if (round <= 0 || round > 5) {
			logger.debug("错误的比赛轮次.Round[" + round + "]");
			return;
		}

		List<ContestUser> l = contestDao.getList();
		// 获取比赛赛程
		Map<String, ContestPairBO> matchSchedule = ContestHelper.getSchedule(l, round);

		this.matchCount = matchSchedule.size();
		this.finishMatchCount = 0;

		logger.info("共有比场数[" + this.matchCount + "]");

		if (this.matchCount > 0) {
			// 执行赛程比赛
			this.runSchedule(round, index, matchSchedule);
		}

	}

	private void runSchedule(int round, int index, Map<String, ContestPairBO> matchSchedule) {

		// 开始比赛
		for (Map.Entry<String, ContestPairBO> entry : matchSchedule.entrySet()) {

			ContestPairBO contestPairBO = entry.getValue();

			// 进行一场比赛
			this.runOneSchedule(round, index, contestPairBO);

		}

	}

	/**
	 * 进行一场比赛
	 * 
	 * @param round
	 * @param contestPairBO
	 */
	private void runOneSchedule(final int round, final int matchIndex, final ContestPairBO contestPairBO) {

		final String attackUserID = contestPairBO.getUserA().getUserId();
		final int attackWin = contestPairBO.getUserA().getRoundWin();
		final String attackUsername = contestPairBO.getUserA().getUsername();

		final String defenderUserID = contestPairBO.getUserB().getUserId();
		final int defenderWin = contestPairBO.getUserB().getRoundWin();
		final String defenderUsername = contestPairBO.getUserB().getUsername();

		int rand = RandomUtils.nextInt(100);
		if (rand == 50) {
			rand = 51;
		}
		final int attackRand = rand;
		final int defenderRand = 100 - rand;

		// 两支队都有才比赛，不然的话就是轮空
		if (!contestPairBO.isFullPair()) {
			synchronized (matchLock) {
				matchCount -= 1;
				finishMatchCount += 1;
			}

			if (contestPairBO.getUserA().getIsEmpty() == 1) {// 如果没有A队，那么被淘汰的是A队，不然是B队被淘态
				setWin(contestPairBO.getUserA().getUserId(), matchIndex, round, false, null, null, defenderWin + 1, attackRand);
				setWin(contestPairBO.getUserB().getUserId(), matchIndex, round, true, null, null, attackWin, defenderRand);
			} else {
				setWin(contestPairBO.getUserA().getUserId(), matchIndex, round, true, null, null, defenderWin, attackRand);
				setWin(contestPairBO.getUserB().getUserId(), matchIndex, round, false, null, null, attackWin + 1, defenderRand);
			}

			return;
		}

		boolean force = false;
		if (matchIndex == 3 && attackWin == 0 && defenderWin == 0) {
			force = true;
		}

		this.figth(attackUserID, defenderUserID, force, BattleType.CONTEST, new EventHandle() {

			@Override
			public boolean handle(Event event) {

				synchronized (matchLock) {
					matchCount -= 1;
					finishMatchCount += 1;
				}

				int emptyType = event.getInt("emptyType", -1);

				if (emptyType == 1) {
					setWin(attackUserID, matchIndex, round, false, defenderUserID, defenderUsername, defenderWin, attackRand);
					setWin(defenderUserID, matchIndex, round, false, attackUserID, attackUsername, attackWin, defenderRand);
				} else {
					int flag = event.getInt("flag");
					String report = event.getString("report");
					if (flag == 1) {// 进攻方赢
						setWin(attackUserID, matchIndex, round, true, defenderUserID, defenderUsername, defenderWin, attackRand);
						setWin(defenderUserID, matchIndex, round, false, attackUserID, attackUsername, attackWin + 1, defenderRand);
					} else {
						setWin(attackUserID, matchIndex, round, false, defenderUserID, defenderUsername, defenderWin + 1, attackRand);
						setWin(defenderUserID, matchIndex, round, true, attackUserID, attackUsername, attackWin, defenderRand);
					}

					saveReport(attackUserID, defenderUserID, flag, contestPairBO.getBaseCode(), report, round);

				}

				return false;

			}
		});

	}

	/**
	 * 胜利场数+1
	 * 
	 * @param userId
	 * @param matchIndex
	 *            (比赛场次)
	 * @param round
	 *            (轮次)
	 */
	private void setWin(String userId, int matchIndex, int round, boolean win, String targetUserId, String targetUsername, int targetWinCount, int rand) {

		ContestUser contestUser = this.contestDao.get(userId);

		Map<Integer, Integer> m = this.getWinInfo(userId, round);

		int roundWin = contestUser.getRoundWin();
		int deadRound = contestUser.getDeadRound();

		if (win) {
			roundWin = contestUser.getRoundWin() + 1;
			m.put(matchIndex, 1);
		} else {
			m.put(matchIndex, 2);
		}

		if (matchIndex == 3) {
			if (roundWin < targetWinCount) {
				deadRound = round;
			} else if (roundWin == targetWinCount) {
				if (rand < 50) {
					deadRound = round;
				}
			}

			if (targetUserId != null) {

				List<ContestUserHeroBO> heroList = this.getHeros(targetUserId);
				int targetPower = HeroHelper.getCapabilityByContestHeroBO(heroList);

				ContestHistory history = new ContestHistory();
				history.setUserId(userId);
				history.setTargetUsername(targetUsername);
				history.setTargetUserId(targetUserId);
				history.setRound(round);
				history.setWinCount(roundWin);
				history.setCreatedTime(new Date());
				history.setTargetPower(targetPower);
				this.contestDao.replace(history);

			}

			// 重置回合胜利场数
			roundWin = 0;

			// 出过场的队伍也重置
			this.finishTeams.clear();
			this.winState.clear();

			if (deadRound == 100 && contestUser.getIsEmpty() == 0) {// 晋级
				Map<String, String> map = ContestHelper.getMsg(RUNNING_LOCAL, round);
				this.sendMsg(contestUser.getUsername(), map.get("title1"), map.get("title2"));
			}
		}

		this.contestDao.updateDeadRound(userId, roundWin, deadRound);
	}

	@Override
	public void sendMsg(String username, String title1, String title2) {
		this.messageService.sendContestWinMsg(username, title1, title2);
	}

	/**
	 * 战斗
	 * 
	 * @param userIdA
	 * @param userIdB
	 * @param type
	 * @param handle
	 */
	private void figth(String userIdA, String userIdB, boolean force, int type, EventHandle handle) {

		BattleBO attack = new BattleBO();
		// 英雄列表
		User userA = this.userService.get(userIdA);
		List<BattleHeroBO> attackBO = this.getArray(userIdA, false);

		attack.setUserLevel(userA.getLevel());
		attack.setBattleHeroBOList(attackBO);

		final User defenseUser = userService.get(userIdB);
		BattleBO defense = new BattleBO();
		// 英雄列表
		List<BattleHeroBO> defenseBO = this.getArray(userIdB, false);

		defense.setBattleHeroBOList(defenseBO);
		defense.setUserLevel(defenseUser.getLevel());

		// 强制打一场
		if (attack.getBattleHeroBOList().isEmpty() && defense.getBattleHeroBOList().isEmpty()) {

			if (force) {

				attackBO = this.getArray(userIdA, true);
				attack.setBattleHeroBOList(attackBO);

				defenseBO = this.getArray(userIdB, true);
				defense.setBattleHeroBOList(defenseBO);

			} else {
				int emptyType = 1;
				BaseEvent event = new BaseEvent();
				event.setObject("emptyType", emptyType);
				handle.handle(event);
				return;
			}

		}

		battleService.fight(attack, defense, type, handle);

	}

	@Override
	public BattleBO getBattleBO(String userId, boolean force) {

		BattleBO battleBO = new BattleBO();
		// 英雄列表
		List<BattleHeroBO> l = this.getArray(userId, force);

		for (BattleHeroBO bo : l) {
			if (bo.getMergeSkillList() == null) {
				bo.setMergeSkillList(new ArrayList<MergeSkillBO>());
			}
			if (bo.getUserHeroId() == null) {
				bo.setUserHeroId("");
			}
		}

		List<ContestUserHeroBO> list = this.getContestUserHeroBOList(userId);
		int power = HeroHelper.getCapabilityByContestHeroBO(list);
		battleBO.setExtValue(power);

		battleBO.setBattleHeroBOList(l);

		return battleBO;
	}

	private List<BattleHeroBO> getArray(String userId, boolean force) {

		ContestUser contestUser = this.contestDao.get(userId);

		int selectTeam = contestUser.getSelectTeam();

		// 强制一个队
		if (force && selectTeam == 0) {
			selectTeam = 1;
		}

		if (selectTeam > 0) {

			contestUser.setSelectTeam(0);

			Set<Integer> set;
			if (this.finishTeams.containsKey(userId)) {
				set = this.finishTeams.get(userId);
			} else {
				set = new HashSet<Integer>();
			}
			set.add(selectTeam);
			this.finishTeams.put(userId, set);

			contestDao.updateSelectTeam(userId, 0);

			List<ContestUserHero> l = this.contestDao.getContestUserHeroList(userId, selectTeam);
			return this.heroService.getUserBattleHeroBOList(l);
		}

		return new ArrayList<BattleHeroBO>();

	}

	@Override
	public void sendOnlineReward() {

		Set<String> userIds = this.userDao.getOnlineUserIdList();
		for (String userId : userIds) {

			String title = "巅峰战场奖励";
			String content = "巅峰战期间您作为在线玩家获得奖励，多谢您的参与！";

			User user = this.userService.get(userId);
			if (user == null) {
				continue;
			}

			String userLodoIds = user.getLodoId() + ",";

			this.mailService.send(title, content, "1,1,100", MailTarget.USERS, userLodoIds, null, null, null);

		}
	}

	/**
	 * 开始
	 */
	public void start() {

		// 初始化报名列表
		initRegList();

		this.winState.clear();

	}

	/**
	 * 结束
	 */
	private void end() {

		// 发放奖励
		this.sendReward();

		// 上报
		this.reportContestUser();

		// 初始化下次要发的消息
		initMsgSet();
	}

	/**
	 * 初始化要发的消息
	 */
	private void initMsgSet() {

		// 初始化要发的消息
		msgSet.add(1);// 12点
		msgSet.add(2);// 18点
		msgSet.add(3);// 19:45
		msgSet.add(4);// 8强开始前5
		msgSet.add(5);// 8强开始前3
	}

	private void checkSendMsg() {

		Date now = new Date();

		if (msgSet.contains(1)) {

			String s1 = DateUtils.getDate2(new Date(DateUtils.getTimeByDayOfWeek("6 12:00")));
			String s2 = DateUtils.getDate2(now);
			if (StringUtils.equals(s1, s2)) {
				this.sendMsg("", MATCH_START_MSG, "");
				msgSet.remove(1);
			}

		}

		if (msgSet.contains(2)) {

			String s1 = DateUtils.getDate2(new Date(DateUtils.getTimeByDayOfWeek("6 18:00")));
			String s2 = DateUtils.getDate2(now);
			if (StringUtils.equals(s1, s2)) {
				this.sendMsg("", MATCH_START_MSG, "");
				msgSet.remove(2);
			}
		}

		if (msgSet.contains(3)) {

			String s1 = DateUtils.getDate2(new Date(DateUtils.getTimeByDayOfWeek("6 19:45")));
			String s2 = DateUtils.getDate2(now);
			if (StringUtils.equals(s1, s2)) {
				this.sendMsg("", MATCH_START_MSG, "");
				msgSet.remove(3);
			}

		}

		if (msgSet.contains(4)) {

			if (this.status == ContestStatus.MATCH_READY_8_1) {

				long time1 = this.getNextStatusTime().getTime();
				if (time1 - now.getTime() <= 5 * 60 * 1000) {
					this.sendMsg("", MATCH_END_MSG.replace("${min}", "5"), "");
					msgSet.remove(4);
				}

			}

		}

		if (msgSet.contains(5)) {

			if (this.status == ContestStatus.MATCH_READY_8_1) {

				long time1 = this.getNextStatusTime().getTime();
				if (time1 - now.getTime() <= 3 * 60 * 1000) {
					this.sendMsg("", MATCH_END_MSG.replace("${min}", "3"), "");
					msgSet.remove(4);
				}

			}
		}

	}

	/**
	 * 发放奖励
	 */
	private void sendReward() {

		List<ContestUser> list = this.contestDao.getList();
		for (ContestUser contestUser : list) {

			if (contestUser.getIsEmpty() == 1) {
				continue;
			}

			int deadRound = contestUser.getDeadRound();
			int rank = ContestHelper.deadRound2Rank(deadRound);
			sendReward(RUNNING_LOCAL, contestUser.getUserId(), rank);
		}

	}

	private void sendReward(int type, String userId, int rank) {

		ContestReward contestReward = this.contestDao.getReward(type, rank);

		if (contestReward != null) {

			String title = "巅峰战场奖励";

			String content = ContestHelper.getMailContent(type, rank);

			List<SystemToolDrop> list = this.systemToolDropDao.getSystemToolDropList(contestReward.getGiftbagId());

			String toolIds = ToolHelper.dropList2ToolIds(list);
			User user = this.userService.get(userId);
			if (user == null) {
				return;
			}

			String userLodoIds = user.getLodoId() + ",";

			this.mailService.send(title, content, toolIds, MailTarget.USERS, userLodoIds, null, null, null);

			ContestRewardLog contestRewardLog = new ContestRewardLog();
			contestRewardLog.setUserId(userId);
			contestRewardLog.setWeek(DateUtils.getWeekOfYear());
			contestRewardLog.setCreatedTime(new Date());
			contestRewardLog.setGiftBagId(contestReward.getGiftbagId());

			this.contestDao.add(contestRewardLog);
		}
	}

	/**
	 * 上报进决赛的队伍到跨服务器
	 */
	private void reportContestUser() {

		logger.debug("上报进决赛的队伍到跨服服务器");

		Request request = new Request();
		request.setAction("ContestRemote");
		request.setMethod("reportContestUser");

		List<ContestUser> list = new ArrayList<ContestUser>();

		List<ContestUser> l = this.contestDao.getList();
		for (ContestUser contestUser : l) {
			if (contestUser.getDeadRound() == 100 && contestUser.getIsEmpty() == 0) {
				list.add(contestUser);
			}
		}

		request.put("list", list);

		remoteCallHandle.call(request, new Callback() {

			@Override
			public void handle(Response resp) {
				logger.debug("上报服务器返回[" + resp.getRc() + "]");
			}
		});
	}

	/**
	 * 初始化赛程
	 */
	private void initRegList() {

		// 清空上次的表,备份上次的表
		this.contestDao.truncateContestUser();

		// 根据千人斩周排行弄
		Date now = new Date();

		List<OnlyoneWeekRank> list = rankDao.getOnlyoneRank(32);
		for (OnlyoneWeekRank rank : list) {
			ContestUser contestUser = new ContestUser();

			User user = this.userService.get(rank.getUserId());

			contestUser.setArrayFinish(0);
			contestUser.setCreatedTime(now);
			contestUser.setDeadRound(100);
			contestUser.setInd(0);
			contestUser.setIsEmpty(0);
			contestUser.setSelectTeam(0);
			contestUser.setRoundWin(0);
			contestUser.setUserId(rank.getUserId());
			contestUser.setUsername(rank.getUsername());
			contestUser.setLevel(user.getLevel());
			SystemHero systemHero = this.userService.getSystemHero(user.getUserId());
			contestUser.setImgId(systemHero != null ? systemHero.getImgId() : 0);
			contestUser.setModelId(systemHero.getModelId());
			contestUser.setVip(user.getVipLevel());
			this.contestDao.replace(contestUser);

		}

	}

	/**
	 * 安排赛程
	 */
	private void scheduling() {

		List<Integer> indList = new ArrayList<Integer>();
		for (int i = 0; i <= 31; i++) {
			indList.add(i);
		}

		// 删除没有布阵的队伍
		this.contestDao.deleteNotArrayTeam();

		List<ContestUser> list = this.contestDao.getList();

		ContestHelper.scheduling(list, indList);

		Set<Integer> set = new HashSet<Integer>();
		for (ContestUser contestUser : list) {
			set.add(contestUser.getInd());
		}

		// 更新回数据库
		for (ContestUser contestUser : list) {
			this.contestDao.replace(contestUser);
		}

		Date now = new Date();

		for (int i = 0; i < 32; i++) {
			if (!set.contains(i)) {
				ContestUser contestUser = new ContestUser();
				contestUser.setArrayFinish(0);
				contestUser.setCreatedTime(now);
				contestUser.setDeadRound(100);
				contestUser.setInd(i);
				contestUser.setIsEmpty(1);
				contestUser.setSelectTeam(0);
				contestUser.setRoundWin(0);
				contestUser.setUserId(IDGenerator.getID());
				contestUser.setUsername("轮空");
				this.contestDao.replace(contestUser);
			}
		}

	}

	public void switchStatus() {

		Date now = new Date();

		if (this.status == ContestStatus.MATCH_8_3) {// 倒数一场比赛

			this.status = ContestStatus.END;

			this.nextStatusTime = ContestHelper.getNextDayStartTime();

			this.end();

		} else {

			if (this.status == ContestStatus.NOT_START) {// 没开始，则开始
				this.status = ContestStatus.toStatus(this.status.getValue() + 1);
				this.nextStatusTime = DateUtils.add(now, Calendar.MILLISECOND, ContestHelper.getNextStatusTime(status));
				this.start();
			} else if (this.status == ContestStatus.ARRAY) {
				this.status = ContestStatus.toStatus(this.status.getValue() + 1);
				this.nextStatusTime = DateUtils.add(now, Calendar.MILLISECOND, ContestHelper.getNextStatusTime(status));
				// 排定赛程
				this.scheduling();

			} else if (status.isMatchReadyStatus()) {// 是比赛准备状态
				this.status = ContestStatus.toStatus(this.status.getValue() + 1);
				this.nextStatusTime = DateUtils.add(now, Calendar.MILLISECOND, ContestHelper.getNextStatusTime(status));
				this.runMatch();

			} else if (this.status == ContestStatus.END) {
				this.status = ContestStatus.NOT_START;
				this.nextStatusTime = DateUtils.add(now, Calendar.MILLISECOND, ContestHelper.getNextStatusTime(status));
			} else {
				this.status = ContestStatus.toStatus(this.status.getValue() + 1);
				this.nextStatusTime = DateUtils.add(now, Calendar.MILLISECOND, ContestHelper.getNextStatusTime(status));
			}

		}

		// 推送给客户端
		if (status.isMatchReadyStatus()) {
			pushStatus(ContestHelper.toClientStatus(status).getValue());
		}

		int week = DateUtils.getWeekOfYear();
		ContestInfo contestInfo = this.contestDao.getContestStatus(week);
		if (contestInfo != null) {
			contestInfo.setNextStatusTime(this.getNextStatusTime());
			contestInfo.setStatus(status.getValue());
			this.contestDao.saveContestStatus(contestInfo);
		}

	}

	/**
	 * 推送状态
	 */
	@Override
	public void pushStatus(int clientStatus) {

		Command command = new Command();
		command.setCommand(CommandType.COMMAND_CONTEST_PUSH_STATUS);
		command.setType(CommandType.PUSH_ALL);

		Map<String, String> params = new HashMap<String, String>();
		params.put("status", String.valueOf(clientStatus));

		command.setParams(params);

		commandDao.add(command);

	}

	/**
	 * 设置开始时间
	 */
	public void setStartTime() {

		int week = DateUtils.getWeekOfYear();
		ContestInfo contestInfo = this.contestDao.getContestStatus(week);
		if (contestInfo == null) {
			contestInfo = new ContestInfo();
			contestInfo.setWeek(week);
			contestInfo.setCreatedTime(new Date());
			contestInfo.setNextStatusTime(this.getNextStatusTime());
			contestInfo.setStatus(this.status.getValue());
			this.contestDao.saveContestStatus(contestInfo);
		} else {
			this.nextStatusTime = contestInfo.getNextStatusTime();
			this.status = ContestStatus.toStatus(contestInfo.getStatus());
		}
	}

	@Override
	public ContestUserBO getTargetInfo(String userId) {

		ContestUser contestUser = this.contestDao.get(userId);

		if (contestUser != null && contestUser.getDeadRound() == 100) {

			Map<String, String> info = ContestHelper.getBaseCode(contestUser.getInd(), ContestHelper.getClientRound(status));
			String baseCode = info.get("code");
			String tag = info.get("tag");

			List<ContestUser> list = this.contestDao.getList();
			for (ContestUser cu : list) {

				if (cu.getDeadRound() != 100) {
					continue;
				}

				Map<String, String> i = ContestHelper.getBaseCode(cu.getInd(), ContestHelper.getClientRound(status));
				String b = i.get("code");
				String t = i.get("tag");

				if (StringUtils.equals(baseCode, b) && !StringUtils.equals(tag, t)) {

					if (cu.getIsEmpty() == 0) {
						ContestUserBO bo = new ContestUserBO();
						bo.setImgId(cu.getImgId());
						bo.setLevel(cu.getLevel());
						bo.setName(cu.getUsername());
						bo.setServerId("本服");
						bo.setUserId(cu.getUserId());
						return bo;

					} else {
						return null;
					}
				}
			}

		}

		return null;
	}

	private void regToWorldServer() {

		Request request = new Request();
		request.setAction("ContestRemote");
		request.setMethod("reg");

		remoteCallHandle.call(request, new Callback() {

			@Override
			public void handle(Response resp) {
				logger.debug("注册跨服服务器返回[" + resp.getRc() + "]");
			}
		});
	}

	private void worldStatusSync() {

		Request request = new Request();
		request.setAction("ContestRemote");
		request.setMethod("getStatus");

		logger.debug("本地状态.[" + status.getDesc() + "], 跨服状态[" + worldStatus.getDesc() + "], runningType[" + runningType + "]");

		remoteCallHandle.call(request, new Callback() {

			@Override
			public void handle(Response resp) {

				worldStatus = ContestWorldStatus.toStatus(resp.get("status", 0));

				if (worldStatus != ContestWorldStatus.NOT_START) {
					runningType = RUNNING_WORLD;
				} else {
					runningType = RUNNING_LOCAL;
				}

			}
		});

	}

	@Override
	public boolean worldStart() {

		// 状态从完成变成未开始
		if (this.status == ContestStatus.END) {
			this.status = ContestStatus.NOT_START;
			int week = DateUtils.getWeekOfYear();
			ContestInfo contestInfo = this.contestDao.getContestStatus(week);
			if (contestInfo != null) {
				contestInfo.setNextStatusTime(this.getNextStatusTime());
				contestInfo.setStatus(status.getValue());
				this.contestDao.saveContestStatus(contestInfo);
			}

			// 清空武将表，让用户重新布阵
			this.contestDao.truncateContestHero();
			this.contestDao.setArrayNotFinish();

		} else {
			logger.error("跨服比赛开始时，游戏服状态为非结束状态,状态异常.status[" + this.status.getValue() + "]");
		}

		return true;
	}

	private Map<Integer, Integer> getWinInfo(String userId, int round) {

		String key = userId + "-" + round;

		Map<Integer, Integer> m = null;
		if (this.winState.containsKey(key)) {
			m = this.winState.get(key);
		} else {
			m = new HashMap<Integer, Integer>();
			m.put(1, 0);
			m.put(2, 0);
			m.put(3, 0);
			this.winState.put(key, m);
		}
		return m;
	}

	private void saveReport(String attackUserId, String defenseUserId, int flag, String baseCode, String report, int round) {

		User attackUser = this.userService.get(attackUserId);
		User defenseUser = this.userService.get(defenseUserId);

		ContestBattleReport contestBattleReport = new ContestBattleReport();
		contestBattleReport.setAttackUserId(attackUserId);
		contestBattleReport.setAttackUsername(attackUser.getUsername());
		contestBattleReport.setDefenseUserId(defenseUserId);
		contestBattleReport.setDefenseUsername(defenseUser.getUsername());
		contestBattleReport.setBaseCode(baseCode);
		contestBattleReport.setCreatedTime(new Date());
		contestBattleReport.setFlag(flag);
		contestBattleReport.setReport(report);

		contestBattleReport.setWinInfo(Json.toJson(getWinInfo(attackUserId, round)));

		this.contestDao.addReport(contestBattleReport);

		Command command = new Command();
		command.setCommand(CommandType.COMMAND_CONTEST_PUSH_BATTLE);
		command.setType(CommandType.PUSH_USER);

		Map<String, String> params = new HashMap<String, String>();
		params.put("report", Json.toJson(contestBattleReport));

		params.put("userId", attackUserId);

		command.setParams(params);
		commandDao.add(command);

		params.put("userId", defenseUserId);
		contestBattleReport.setWinInfo(Json.toJson(getWinInfo(defenseUserId, round)));
		params.put("report", Json.toJson(contestBattleReport));

		commandDao.add(command);

	}

	private boolean testArrayFinish = false;

	private void _test() {

		if (this.status == ContestStatus.ARRAY) {

			testArrayFinish = true;

			List<ContestUser> list = this.contestDao.getList();
			for (ContestUser contestUser : list) {

				if (contestUser.getArrayFinish() == 0) {

					this.updateContestUserHero(contestUser.getUserId());

					List<ContestUserHero> l = this.contestDao.getHeroList(contestUser.getUserId());

					int team = 1;
					int pos = 1;
					for (ContestUserHero hero : l) {

						this.contestDao.updateHeroPosAndTeam(hero.getUserHeroId(), team, pos);

						team += 1;
						if (team > 3) {
							team = 1;
							pos += 1;
						}

						if (pos > 6) {
							break;
						}

					}

					this.contestDao.updateArrayFinish(contestUser.getUserId());

				}

			}

		}

		if (this.status.isMatchReadyStatus()) {

			List<ContestUser> list = this.contestDao.getList();
			for (ContestUser contestUser : list) {

				if (contestUser.getSelectTeam() == 0) {

					int matchIndex = ContestHelper.getClientIndex(status);

					this.contestDao.updateSelectTeam(contestUser.getUserId(), matchIndex);

				}

			}
		}

	}

	/**
	 * 测试线程
	 */
	private void test() {

		// 世界服状态同步
		new Thread(new Runnable() {

			public void run() {
				while (true) {
					try {
						_test();
					} catch (Throwable t) {
						logger.error(t.getMessage(), t);
					}

					try {
						Thread.sleep(1000);
					} catch (InterruptedException ie) {
						logger.error(ie.getMessage(), ie);
					}

				}
			}

		}).start();

	}

	@Override
	public void cleanFinishTeam() {
		this.finishTeams.clear();
	}

	@Override
	public void sendReward(String userId, int rand) {
		this.sendReward(RUNNING_WORLD, userId, rand);
	}

	synchronized public void init() {

		if (!Config.ins().isGameServer()) {
			return;
		}

		if (this.started) {
			return;
		}

		this.started = true;

		// 初始化要发的消息
		initMsgSet();

		// 在跨服服务器注册
		regToWorldServer();

		// 设置开始时间
		setStartTime();

		// 状态切换线程
		new Thread(new Runnable() {

			public void run() {
				while (true) {
					try {
						statusCheck();
					} catch (Throwable t) {
						logger.error(t.getMessage(), t);
					}

					try {
						Thread.sleep(1000);
					} catch (InterruptedException ie) {
						logger.error(ie.getMessage(), ie);
					}

				}
			}

		}).start();

		// 世界服状态同步
		new Thread(new Runnable() {

			public void run() {
				while (true) {
					try {
						worldStatusSync();
					} catch (Throwable t) {
						logger.error(t.getMessage(), t);
					}

					try {
						Thread.sleep(1000);
					} catch (InterruptedException ie) {
						logger.error(ie.getMessage(), ie);
					}

				}
			}

		}).start();

		// 测试线程
		test();

	}
}
