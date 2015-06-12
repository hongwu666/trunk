package com.lodogame.ldsg.world.service.impl;

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

import com.alibaba.fastjson.JSON;
import com.lodogame.game.remote.callback.Callback;
import com.lodogame.game.remote.constants.StatusConstants;
import com.lodogame.game.remote.handle.RemoteCallHandle;
import com.lodogame.game.remote.request.Request;
import com.lodogame.game.remote.response.Response;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.game.utils.IDGenerator;
import com.lodogame.ldsg.bo.BattleBO;
import com.lodogame.ldsg.bo.ContestHistoryBO;
import com.lodogame.ldsg.bo.ContestScheduleBO;
import com.lodogame.ldsg.bo.ContestTopUserBO;
import com.lodogame.ldsg.bo.ContestUserBO;
import com.lodogame.ldsg.bo.ContestUserHeroBO;
import com.lodogame.ldsg.bo.ContestWorldPairBO;
import com.lodogame.ldsg.constants.BattleType;
import com.lodogame.ldsg.constants.ContestWorldStatus;
import com.lodogame.ldsg.event.BaseEvent;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.helper.ContestHelper;
import com.lodogame.ldsg.helper.WorldContestHelper;
import com.lodogame.ldsg.service.BattleService;
import com.lodogame.ldsg.world.dao.ContestDao;
import com.lodogame.ldsg.world.model.ContestUserReady;
import com.lodogame.ldsg.world.model.ServerRegList;
import com.lodogame.ldsg.world.service.ContestService;
import com.lodogame.model.ContestBattleReport;
import com.lodogame.model.ContestHistory;
import com.lodogame.model.ContestInfo;
import com.lodogame.model.ContestTopUser;
import com.lodogame.model.ContestWorldUser;

public class ContestServiceImpl implements ContestService {

	public static boolean debug = false;

	@Autowired
	private ContestDao contestDao;

	private static final Logger logger = Logger.getLogger(ContestServiceImpl.class);

	/**
	 * 比赛场数
	 */
	private int matchCount;

	/**
	 * 已经完成比赛场数
	 */
	private int finishMatchCount;

	/**
	 * 获取战斗信息回调次数
	 */
	private int getBattleInfoCallbackCount;

	private boolean started = false;

	/**
	 * 下个阶段开始时间
	 */
	private Date nextStatusTime = null;

	/**
	 * 状态
	 */
	private ContestWorldStatus status = ContestWorldStatus.NOT_START;

	/**
	 * 胜利记录
	 */
	private Map<String, Map<Integer, Integer>> winState = new HashMap<String, Map<Integer, Integer>>();

	/**
	 * 比赛要开始的走马灯
	 */
	private static String MATCH_START_MSG = "全服巅峰战今晚20:00准时开战，谁将问鼎巅峰王者，让巅峰战火告诉我们答案！全服巅峰战期间，全体在线玩家将会获得100钻石，为你们的王者加油吧！";

	/**
	 * 比 赛结束走马灯
	 */
	private static String MATCH_END_MSG = "还有${min}分钟，全服巅峰最终战即将开幕，我们已经热血沸腾了！";

	/**
	 * 比 赛结束要发奖励走马灯
	 */
	private static String MATCH_END_REWARD_MSG = "本次巅峰战即将落下战幕，请各位玩家保持在线，以便领取战场福利！";

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
	private RemoteCallHandle remoteCallHandle;

	private void statusCheck() {

		// 消息发送奖励
		checkSendMsg();

		Date now = new Date();

		if (now.after(this.getNextStatusTime())) {

			if (status.isMatchStatus()) {// 比赛回合的话要等比赛完

				if (this.matchCount == 0) {
					switchStatus();
				} else {
					logger.debug("比赛进行中,剩余比赛场数[" + this.matchCount + "], 已经完成比赛场数[" + this.finishMatchCount + "], 获取战斗信息回调次数[" + this.getBattleInfoCallbackCount + "]");
				}

			} else {
				switchStatus();
			}

		} else {
			logger.debug("[" + Thread.currentThread().getName() + "].当前状态[" + this.status.getDesc() + "], 当前时间[" + DateUtils.getDate3(now) + "], 状态切换时间["
					+ DateUtils.getDate3(this.getNextStatusTime()) + "]");
		}

	}

	@Override
	public Map<String, Object> enter(String userId) {

		Map<String, Object> data = new HashMap<String, Object>();

		ContestWorldUser contestUser = this.contestDao.get(userId);
		if (contestUser != null && this.status != ContestWorldStatus.NOT_START) {

		}

		int round = WorldContestHelper.getClientRound(status);

		data.put("status", WorldContestHelper.toClientStatus(status).getValue());
		data.put("round", round);
		data.put("nst", this.getNextStatusTime().getTime());

		// 1. 正常 2 没有报名资格 3.已经被淘态
		int st = 1;
		if (contestUser == null) {
			st = 2;
		} else if (contestUser.getDeadRound() != 100) {
			st = 3;
		}

		data.put("st", st);

		if (st == 1 && this.status != ContestWorldStatus.NOT_START) {
			data.put("wininfo", getWinInfo(userId, round));
			data.put("uinfo", this.getTargetInfo(userId));
		}

		return data;
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

	@Override
	public void getHeros(String userId, final EventHandle handle) {

		ContestWorldUser user = this.contestDao.get(userId);
		if (user == null) {

			BaseEvent event = new BaseEvent();
			handle.handle(event);

		} else {

			Request request = new Request();
			request.setAction("ContestRemote");
			request.setMethod("getHeros");
			request.put("uid", userId);

			this.remoteCallHandle.call(user.getServerId(), request, new Callback() {

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

	/**
	 * 获取下一阶段开始的时间
	 * 
	 * @return
	 */
	public Date getNextStatusTime() {

		if (nextStatusTime == null) {
			if (debug) {
				nextStatusTime = DateUtils.add(new Date(), Calendar.MINUTE, 5);
				new Date();
			} else {
				nextStatusTime = new Date(DateUtils.getTimeByDayOfWeek("7 20:00"));
			}
		}

		return nextStatusTime;
	}

	/**
	 * 进行一轮比赛
	 */
	private void runMatch() {

		int round = WorldContestHelper.getRound(status);
		int index = WorldContestHelper.getIndex(status);
		if (round <= 0 || round > 5) {
			logger.debug("错误的比赛轮次.Round[" + round + "]");
			return;
		}

		List<ContestWorldUser> l = contestDao.getList();
		// 获取比赛赛程
		Map<String, ContestWorldPairBO> matchSchedule = WorldContestHelper.getSchedule(l, round);

		this.matchCount = matchSchedule.size();
		this.finishMatchCount = 0;
		this.getBattleInfoCallbackCount = 0;

		logger.info("共有比场数[" + this.matchCount + "]");

		if (this.matchCount > 0) {
			// 执行赛程比赛
			this.runSchedule(round, index, matchSchedule);
		}

	}

	private void runSchedule(int round, int index, Map<String, ContestWorldPairBO> matchSchedule) {

		// 开始比赛
		for (Map.Entry<String, ContestWorldPairBO> entry : matchSchedule.entrySet()) {

			ContestWorldPairBO contestPairBO = entry.getValue();

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
	private void runOneSchedule(final int round, final int matchIndex, final ContestWorldPairBO contestPairBO) {

		final String attackUserID = contestPairBO.getUserA().getUserId();
		final String attackUsername = contestPairBO.getUserA().getUsername();
		final int attackWin = contestPairBO.getUserA().getRoundWin();

		final String defenderUserID = contestPairBO.getUserB().getUserId();
		final String defenderUsername = contestPairBO.getUserB().getUsername();
		final int defenderWin = contestPairBO.getUserB().getRoundWin();

		int rand = RandomUtils.nextInt(100);
		if (rand == 50) {
			rand = 51;
		}
		final int attackRand = rand;
		final int defenderRand = 100 - rand;

		// 两支队都有才比赛，不然的话就是轮空
		if (!contestPairBO.isFullPair()) {
			synchronized (matchLock) {
				if (contestPairBO.getUserA().getIsEmpty() == 1) {// 如果没有A队，那么被淘汰的是A队，不然是B队被淘态
					setWin(contestPairBO.getUserA().getUserId(), matchIndex, round, false, null, null, 0, defenderWin + 1, attackRand);
					setWin(contestPairBO.getUserB().getUserId(), matchIndex, round, true, null, null, 0, attackWin, defenderRand);
				} else {
					setWin(contestPairBO.getUserA().getUserId(), matchIndex, round, true, null, null, 0, defenderWin, attackRand);
					setWin(contestPairBO.getUserB().getUserId(), matchIndex, round, false, null, null, 0, attackWin + 1, defenderRand);
				}
				matchCount -= 1;
				finishMatchCount += 1;
			}
			return;
		}

		this.figth(attackUserID, defenderUserID, 1, new EventHandle() {

			@Override
			public boolean handle(Event event) {

				synchronized (matchLock) {
					matchCount -= 1;
					finishMatchCount += 1;
				}

				int emptyType = event.getInt("emptyType", -1);

				if (emptyType == 1) {

					setWin(attackUserID, matchIndex, round, false, defenderUserID, defenderUsername, 0, defenderWin, attackRand);
					setWin(defenderUserID, matchIndex, round, false, attackUserID, attackUsername, 0, attackWin, defenderRand);

				} else {
					int flag = event.getInt("flag");
					String report = event.getString("report");

					int attackPower = event.getInt("attackPower");
					int defensePower = event.getInt("defensePower");

					if (flag == 1) {// 进攻方赢
						setWin(attackUserID, matchIndex, round, true, defenderUserID, defenderUsername, defensePower, defenderWin, attackRand);
						setWin(defenderUserID, matchIndex, round, false, attackUserID, attackUsername, attackPower, attackWin + 1, defenderRand);
					} else {
						setWin(attackUserID, matchIndex, round, false, defenderUserID, defenderUsername, defensePower, defenderWin + 1, attackRand);
						setWin(defenderUserID, matchIndex, round, true, attackUserID, attackUsername, attackPower, attackWin, defenderRand);

					}

					saveReport(attackUserID, defenderUserID, flag, contestPairBO.getBaseCode(), report, round);

				}

				return false;

			}
		});

	}

	/**
	 * 保存战报
	 * 
	 * @param attackUserId
	 * @param defenseUserId
	 * @param flag
	 * @param baseCode
	 * @param report
	 */
	private void saveReport(final String attackUserId, final String defenseUserId, int flag, String baseCode, String report, int round) {

		ContestWorldUser attackUser = this.contestDao.get(attackUserId);
		ContestWorldUser defenseUser = this.contestDao.get(defenseUserId);

		ContestBattleReport contestBattleReport = new ContestBattleReport();
		contestBattleReport.setAttackUserId(attackUserId);
		contestBattleReport.setAttackUsername(attackUser.getUsername());
		contestBattleReport.setDefenseUserId(defenseUserId);
		contestBattleReport.setDefenseUsername(defenseUser.getUsername());
		contestBattleReport.setBaseCode(baseCode);
		contestBattleReport.setCreatedTime(new Date());
		contestBattleReport.setFlag(flag);
		contestBattleReport.setReport(report);

		Request req = new Request();
		req.setAction("ContestRemote");
		req.setMethod("pushBattle");
		req.put("uid", attackUserId);
		req.put("attackUserId", contestBattleReport.getAttackUserId());
		req.put("attackUsername", contestBattleReport.getAttackUsername());
		req.put("defenseUserId", contestBattleReport.getDefenseUserId());
		req.put("defenseUsername", contestBattleReport.getDefenseUsername());
		req.put("baseCode", contestBattleReport.getBaseCode());
		req.put("flag", contestBattleReport.getFlag());
		req.put("report", contestBattleReport.getReport());

		req.put("winInfo", JSON.toJSON(this.getWinInfo(attackUserId, round)));
		// 推送战斗
		this.remoteCallHandle.call(attackUser.getServerId(), req, new Callback() {

			@Override
			public void handle(Response resp) {
				logger.debug("推送战斗返回.userId[" + attackUserId + "], rc[" + resp.getRc() + "]");
			}
		});

		req.put("uid", defenseUserId);
		req.put("winInfo", JSON.toJSON(this.getWinInfo(defenseUserId, round)));

		// 推送战斗
		this.remoteCallHandle.call(defenseUser.getServerId(), req, new Callback() {

			@Override
			public void handle(Response resp) {
				logger.debug("推送战斗返回.userId[" + defenseUserId + "], rc[" + resp.getRc() + "]");
			}
		});

		this.contestDao.addReport(contestBattleReport);

	}

	@Override
	public ContestUserBO getTargetInfo(String userId) {

		ContestWorldUser contestUser = this.contestDao.get(userId);

		if (contestUser != null && contestUser.getDeadRound() == 100) {

			Map<String, String> info = ContestHelper.getBaseCode(contestUser.getInd(), WorldContestHelper.getClientRound(status));
			String baseCode = info.get("code");
			String tag = info.get("tag");

			List<ContestWorldUser> list = this.contestDao.getList();
			for (ContestWorldUser cu : list) {

				if (cu.getDeadRound() != 100) {
					continue;
				}

				Map<String, String> i = ContestHelper.getBaseCode(cu.getInd(), WorldContestHelper.getClientRound(status));
				String b = i.get("code");
				String t = i.get("tag");

				if (StringUtils.equals(baseCode, b) && !StringUtils.equals(tag, t)) {

					if (cu.getIsEmpty() == 0) {
						ContestUserBO bo = new ContestUserBO();
						bo.setImgId(cu.getImgId());
						bo.setLevel(cu.getLevel());
						bo.setName(cu.getUsername());
						bo.setServerId(cu.getServerId());
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

	/**
	 * 胜利场数+1
	 * 
	 * @param userId
	 * @param matchIndex
	 *            (比赛场次)
	 * @param round
	 *            (轮次)
	 */
	private void setWin(String userId, int matchIndex, int round, boolean win, String targetUserId, String targetUsername, int targetPower, int targetWin, int rand) {

		Map<Integer, Integer> m = this.getWinInfo(userId, round);

		ContestWorldUser contestUser = this.contestDao.get(userId);

		int roundWin = contestUser.getRoundWin();
		int deadRound = contestUser.getDeadRound();

		if (win) {
			contestUser.setRoundWin(contestUser.getRoundWin() + 1);
			roundWin += 1;
			m.put(matchIndex, 1);
		} else {
			m.put(matchIndex, 2);
		}

		if (matchIndex == 3) {

			if (roundWin < targetWin) {
				deadRound = round;
			} else if (roundWin == targetWin) {
				if (rand < 50) {
					deadRound = round;
				}
			}

			if (targetUserId != null) {

				ContestHistory history = new ContestHistory();
				history.setUserId(userId);
				history.setTargetUsername(targetUsername);
				history.setTargetUserId(targetUserId);
				history.setRound(round);
				history.setWinCount(contestUser.getRoundWin());
				history.setCreatedTime(new Date());
				history.setTargetPower(targetPower);
				this.contestDao.replace(history);

			}

			// 重置回合胜利场数
			contestUser.setRoundWin(0);

			if (deadRound == 100 && contestUser.getIsEmpty() == 0) {// 晋级
				Map<String, String> map = ContestHelper.getMsg(2, round);
				this.sendMsg(contestUser.getUsername(), map.get("title1"), map.get("title2"));
			}

			this.cleanFinishTeam();
		}

		contestUser.setDeadRound(deadRound);
		this.contestDao.replace(contestUser);
	}

	private void cleanFinishTeam() {

		List<ServerRegList> l = this.contestDao.getServerRegList();

		for (ServerRegList server : l) {

			final String serverId = server.getServerId();

			Request request = new Request();
			request.setAction("ContestRemote");
			request.setMethod("cleanFinishTeam");

			this.remoteCallHandle.call(serverId, request, new Callback() {

				@Override
				public void handle(Response resp) {
					if (resp.getRc() != StatusConstants.OK) {
						logger.error(resp.getMessage());
					}
				}
			});

		}
	}

	/**
	 * 战斗
	 * 
	 * @param userIdA
	 * @param userIdB
	 * @param type
	 * @param handle
	 */
	private void figth(String userIdA, String userIdB, int type, final EventHandle handle) {

		ContestWorldUser contestUserA = this.contestDao.get(userIdA);

		final ContestWorldUser contestUserB = this.contestDao.get(userIdB);

		this.getBattleBO(contestUserA.getServerId(), contestUserA.getUserId(), new Callback() {

			@Override
			public void handle(Response resp) {

				getBattleInfoCallbackCount += 1;

				final BattleBO battleBOA = resp.getObject("battleBO", BattleBO.class);

				getBattleBO(contestUserB.getServerId(), contestUserB.getUserId(), new Callback() {

					@Override
					public void handle(Response resp) {

						getBattleInfoCallbackCount += 1;

						BattleBO battleBOB = resp.getObject("battleBO", BattleBO.class);

						if (battleBOA.getBattleHeroBOList().isEmpty() && battleBOB.getBattleHeroBOList().isEmpty()) {
							int emptyType = 1;
							BaseEvent event = new BaseEvent();
							event.setObject("emptyType", emptyType);
							handle.handle(event);
							return;
						} else {
							battleService.fight(battleBOA, battleBOB, BattleType.CONTEST, new EventHandle() {

								@Override
								public boolean handle(Event event) {
									event.setObject("attackPower", battleBOA.getExtValue());
									event.setObject("defensePower", battleBOA.getExtValue());
									handle.handle(event);
									return false;
								}
							});
						}

					}
				});

			}
		});

	}

	/**
	 * 获 取战斗数据
	 * 
	 * @param userId
	 * @param callback
	 */
	private void getBattleBO(String serverId, String userId, Callback callback) {

		Request request = new Request();
		request.setAction("ContestRemote");
		request.setMethod("getBattleBO");

		request.put("userId", userId);

		remoteCallHandle.call(serverId, request, callback);

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

		// 发奖励
		this.sendReward();

		//
		this.initMsgSet();

		this.contestDao.truncateTopUser();

		List<ContestWorldUser> list = this.contestDao.getList();
		for (ContestWorldUser user : list) {

			if (user.getIsEmpty() == 1) {
				continue;
			}

			if (user.getDeadRound() >= 3) {

				ContestTopUser contestTopUser = new ContestTopUser();
				contestTopUser.setCreatedTime(new Date());
				contestTopUser.setDeadRound(user.getDeadRound());
				contestTopUser.setImgId(user.getImgId());
				contestTopUser.setLevel(user.getLevel());
				contestTopUser.setServerId(user.getServerId());
				contestTopUser.setUserId(user.getUserId());
				contestTopUser.setUsername(user.getUsername());
				contestTopUser.setModelId(user.getModelId());
				contestTopUser.setVip(user.getVip());

				this.contestDao.replace(contestTopUser);

			}

		}

	}

	/**
	 * 初始化赛程
	 */
	private void initRegList() {

		// 清空上次的表,备份上次的表
		this.contestDao.truncateContestUser();

		// 根据千人斩周排行弄
		Date now = new Date();

		List<ContestUserReady> list = this.contestDao.getReadyList();
		for (ContestUserReady contestUserReady : list) {
			ContestWorldUser contestUser = new ContestWorldUser();
			contestUser.setCreatedTime(now);
			contestUser.setDeadRound(100);
			contestUser.setInd(0);
			contestUser.setIsEmpty(0);
			contestUser.setRoundWin(0);
			contestUser.setUserId(contestUserReady.getUserId());
			contestUser.setUsername(contestUserReady.getUsername());
			contestUser.setServerId(contestUserReady.getServerId());
			contestUser.setImgId(contestUserReady.getImgId());
			contestUser.setLevel(contestUserReady.getLevel());
			contestUser.setVip(contestUserReady.getVip());
			contestUser.setModelId(contestUserReady.getModelId());
			this.contestDao.replace(contestUser);
		}

		// 把报名表删掉
		this.contestDao.truncateContestUserReady();

	}

	@Override
	public boolean saveContestReadyUser(String serverId, List<ContestUserReady> list) {

		for (ContestUserReady contestUserReady : list) {
			contestUserReady.setServerId(serverId);
			this.contestDao.replace(contestUserReady);
		}

		return true;
	}

	/**
	 * 安排赛程
	 */
	private void scheduling() {

		List<Integer> indList = new ArrayList<Integer>();
		for (int i = 0; i <= 31; i++) {
			indList.add(i);
		}

		List<ContestWorldUser> list = this.contestDao.getList();

		WorldContestHelper.scheduling(list, indList);

		Set<Integer> set = new HashSet<Integer>();
		for (ContestWorldUser contestUser : list) {
			set.add(contestUser.getInd());
		}

		// 更新回数据库
		for (ContestWorldUser contestUser : list) {
			this.contestDao.replace(contestUser);
		}

		Date now = new Date();

		for (int i = 0; i < 32; i++) {
			if (!set.contains(i)) {
				ContestWorldUser contestUser = new ContestWorldUser();
				contestUser.setCreatedTime(now);
				contestUser.setDeadRound(100);
				contestUser.setInd(i);
				contestUser.setIsEmpty(1);
				contestUser.setRoundWin(0);
				contestUser.setUserId(IDGenerator.getID());
				contestUser.setUsername("轮空");
				contestUser.setServerId("world");
				this.contestDao.replace(contestUser);
			}
		}

	}

	private void checkSendMsg() {

		Date now = new Date();

		if (msgSet.contains(1)) {

			String s1 = DateUtils.getDate2(new Date(DateUtils.getTimeByDayOfWeek("7 12:00")));
			String s2 = DateUtils.getDate2(now);
			if (StringUtils.equals(s1, s2)) {
				this.sendMsg("", MATCH_START_MSG, "");
				msgSet.remove(1);
			}

		}

		if (msgSet.contains(2)) {

			String s1 = DateUtils.getDate2(new Date(DateUtils.getTimeByDayOfWeek("7 18:00")));
			String s2 = DateUtils.getDate2(now);
			if (StringUtils.equals(s1, s2)) {
				this.sendMsg("", MATCH_START_MSG, "");
				msgSet.remove(2);
			}
		}

		if (msgSet.contains(3)) {

			String s1 = DateUtils.getDate2(new Date(DateUtils.getTimeByDayOfWeek("7 19:45")));
			String s2 = DateUtils.getDate2(now);
			if (StringUtils.equals(s1, s2)) {
				this.sendMsg("", MATCH_START_MSG, "");
				msgSet.remove(3);
			}

		}

		if (msgSet.contains(4)) {

			if (this.status == ContestWorldStatus.MATCH_READY_2_1) {

				long time1 = this.getNextStatusTime().getTime();
				if (time1 - now.getTime() <= 5 * 60 * 1000) {
					this.sendMsg("", MATCH_END_MSG.replace("${min}", "5"), "");
					msgSet.remove(4);
				}

			}

		}

		if (msgSet.contains(5)) {

			if (this.status == ContestWorldStatus.MATCH_READY_2_1) {

				long time1 = this.getNextStatusTime().getTime();
				if (time1 - now.getTime() <= 3 * 60 * 1000) {
					this.sendMsg("", MATCH_END_MSG.replace("${min}", "3"), "");
					msgSet.remove(5);
				}

			}
		}

		if (msgSet.contains(6)) {

			if (this.status == ContestWorldStatus.MATCH_READY_2_3) {

				long time1 = this.getNextStatusTime().getTime();
				if (time1 - now.getTime() <= 5 * 60 * 1000) {
					this.sendMsg("", MATCH_END_REWARD_MSG, "");
					msgSet.remove(6);
				}

			}
		}

	}

	/**
	 * 发送在线奖励
	 */
	private void sendOnlineReward() {

		List<ServerRegList> l = this.contestDao.getServerRegList();

		for (ServerRegList server : l) {

			final String serverId = server.getServerId();

			Request request = new Request();
			request.setAction("ContestRemote");
			request.setMethod("sendOnlineReward");

			this.remoteCallHandle.call(serverId, request, new Callback() {

				@Override
				public void handle(Response resp) {

				}
			});

		}

	}

	private void sendMsg(String username, String title1, String title2) {

		List<ServerRegList> l = this.contestDao.getServerRegList();

		for (ServerRegList server : l) {

			final String serverId = server.getServerId();

			Request request = new Request();
			request.setAction("ContestRemote");
			request.setMethod("sendMsg");
			request.put("username", username);
			request.put("title1", title1);
			request.put("title2", title2);

			this.remoteCallHandle.call(serverId, request, new Callback() {

				@Override
				public void handle(Response resp) {

				}
			});

		}

	}

	public void switchStatus() {

		Date now = new Date();

		if (this.status.isNextIsEnd()) {// 倒数一场比赛

			this.status = ContestWorldStatus.END;

			this.nextStatusTime = WorldContestHelper.getNextDayStartTime();

			this.end();

		} else {

			if (this.status == ContestWorldStatus.NOT_START) {// 没开始，则开始
				this.status = ContestWorldStatus.toStatus(this.status.getValue() + 1);
				this.nextStatusTime = DateUtils.add(now, Calendar.MILLISECOND, WorldContestHelper.getNextStatusTime(status));
				this.start();
			} else if (this.status == ContestWorldStatus.ARRAY) {

				this.status = ContestWorldStatus.toStatus(this.status.getValue() + 1);
				this.nextStatusTime = DateUtils.add(now, Calendar.MILLISECOND, WorldContestHelper.getNextStatusTime(status));
				// 排定赛程
				scheduling();

			} else if (status.isMatchReadyStatus()) {// 是比赛准备状态
				this.status = ContestWorldStatus.toStatus(this.status.getValue() + 1);
				this.nextStatusTime = DateUtils.add(now, Calendar.MILLISECOND, WorldContestHelper.getNextStatusTime(status));
				this.runMatch();

			} else if (this.status == ContestWorldStatus.END) {
				this.status = ContestWorldStatus.NOT_START;
				this.nextStatusTime = DateUtils.add(now, Calendar.MILLISECOND, WorldContestHelper.getNextStatusTime(status));
			} else {
				this.status = ContestWorldStatus.toStatus(this.status.getValue() + 1);
				this.nextStatusTime = DateUtils.add(now, Calendar.MILLISECOND, WorldContestHelper.getNextStatusTime(status));
			}

		}

		if (this.status.isNeedPushStatus()) {
			pushStatus(WorldContestHelper.toClientStatus(status).getValue());
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
	 * 推送战斗状态
	 */
	private void pushStatus(int clientStatus) {

		List<ServerRegList> l = this.contestDao.getServerRegList();

		for (ServerRegList server : l) {

			final String serverId = server.getServerId();

			Request request = new Request();
			request.setAction("ContestRemote");
			request.setMethod("notifyStatus");
			request.put("status", clientStatus);

			this.remoteCallHandle.call(serverId, request, new Callback() {

				@Override
				public void handle(Response resp) {
					logger.debug("通知跨服状态变化.serverId[" + serverId + "], status[" + status + "]");
				}
			});

		}
	}

	@Override
	public List<ContestScheduleBO> getScheduleList() {

		List<ContestScheduleBO> l = new ArrayList<ContestScheduleBO>();
		List<ContestWorldUser> list = this.contestDao.getList();

		for (ContestWorldUser u : list) {

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
	public List<ContestHistoryBO> getHistorys(String userId) {

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
			this.status = ContestWorldStatus.toStatus(contestInfo.getStatus());
		}
	}

	@Override
	public List<ContestTopUserBO> getContestTopUserList() {

		List<ContestTopUser> l = this.contestDao.getContestTopUserList();
		List<ContestTopUserBO> list = new ArrayList<ContestTopUserBO>();

		for (ContestTopUser contestTopUser : l) {
			ContestTopUserBO bo = new ContestTopUserBO();

			bo.setImgId(contestTopUser.getImgId());
			bo.setServerId(contestTopUser.getServerId());
			bo.setUsername(contestTopUser.getUsername());
			bo.setUserId(contestTopUser.getUserId());
			bo.setModelId(contestTopUser.getModelId());

			list.add(bo);
		}

		return list;
	}

	@Override
	public int getStatus() {
		return this.status.getValue();
	}

	/**
	 * 发放奖励
	 */
	private void sendReward() {

		// 在线的奖励
		this.sendOnlineReward();

		List<ContestWorldUser> list = this.contestDao.getList();
		for (ContestWorldUser contestUser : list) {

			if (contestUser.getIsEmpty() == 1) {
				continue;
			}

			int deadRound = contestUser.getDeadRound();
			int rank = ContestHelper.deadRound2Rank(deadRound);

			Request req = new Request();
			req.setAction("ContestRemote");
			req.setMethod("sendReward");
			req.put("userId", contestUser.getUserId());
			req.put("rank", rank);

			this.remoteCallHandle.call(contestUser.getServerId(), req, new Callback() {

				@Override
				public void handle(Response resp) {
					if (resp.getRc() != StatusConstants.OK) {
						logger.error(resp.getMessage());
					}
				}
			});
		}

	}

	/**
	 * 初始化要发的消息
	 */
	private void initMsgSet() {

		// 初始化要发的消息
		msgSet.add(1);// 12点
		msgSet.add(2);// 18点
		msgSet.add(3);// 19:45
		msgSet.add(4);// 决赛开始前5
		msgSet.add(5);// 决赛开始前3
		msgSet.add(6);// 完成前5
	}

	public void init() {

		if (this.started) {
			return;
		}

		this.started = true;

		// 初始化要发的消息
		initMsgSet();

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

	}
}
