package com.lodogame.ldsg.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.lodogame.game.dao.CommandDao;
import com.lodogame.game.dao.OnlyOneDao;
import com.lodogame.game.dao.OnlyoneHourRankRewardDao;
import com.lodogame.game.dao.OnlyoneJoinRewardDao;
import com.lodogame.game.dao.OnlyoneTimesRewardDao;
import com.lodogame.game.dao.OnlyoneWeekRankRewardDao;
import com.lodogame.game.dao.UserDailyGainLogDao;
import com.lodogame.game.dao.UserDao;
import com.lodogame.game.dao.UserHeroDao;
import com.lodogame.game.dao.UserOnlyOneHeroDao;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.ldsg.bo.BattleBO;
import com.lodogame.ldsg.bo.BattleHeroBO;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.OnlyOneRankBO;
import com.lodogame.ldsg.bo.OnlyOneRegBO;
import com.lodogame.ldsg.bo.OnlyOneRewardBO;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.config.Config;
import com.lodogame.ldsg.constants.BattleType;
import com.lodogame.ldsg.constants.CommandType;
import com.lodogame.ldsg.constants.InitDefine;
import com.lodogame.ldsg.constants.MailTarget;
import com.lodogame.ldsg.constants.OnlyOneRegStatus;
import com.lodogame.ldsg.constants.OnlyOneStatus;
import com.lodogame.ldsg.constants.OnlyOneUserStatus;
import com.lodogame.ldsg.constants.OnlyoneRewardStatus;
import com.lodogame.ldsg.constants.OnlyoneRewardType;
import com.lodogame.ldsg.constants.Priority;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.constants.ToolUseType;
import com.lodogame.ldsg.constants.UserDailyGainLogType;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.helper.DropToolHelper;
import com.lodogame.ldsg.helper.HeroHelper;
import com.lodogame.ldsg.helper.OnlyOneHelper;
import com.lodogame.ldsg.service.BattleService;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.MailService;
import com.lodogame.ldsg.service.MessageService;
import com.lodogame.ldsg.service.OnlyOneService;
import com.lodogame.ldsg.service.RobotService;
import com.lodogame.ldsg.service.ToolService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.Command;
import com.lodogame.model.OnlyoneHourRankReward;
import com.lodogame.model.OnlyoneJoinReward;
import com.lodogame.model.OnlyoneUserReg;
import com.lodogame.model.OnlyoneUserReward;
import com.lodogame.model.OnlyoneWeekRank;
import com.lodogame.model.OnlyoneWeekRankReward;
import com.lodogame.model.RobotUser;
import com.lodogame.model.User;
import com.lodogame.model.UserHero;
import com.lodogame.model.UserOnlyOneHero;

public class OnlyOneServiceImpl implements OnlyOneService {

	private static final Logger logger = Logger.getLogger(OnlyOneServiceImpl.class);

	/**
	 * 结算时间列表
	 */
	private List<Date> cutOffTimeList = new ArrayList<Date>();

	@Autowired
	private OnlyOneDao onlyOneDao;

	/**
	 * 是否并发处理
	 */
	private boolean concurrency = true;

	@Autowired
	private BattleService battleService;

	@Autowired
	private HeroService heroService;

	@Autowired
	private MailService mailService;

	@Autowired
	private UserService userService;

	@Autowired
	private RobotService robotService;

	@Autowired
	private ToolService toolService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private UserDao userDao;

	@Autowired
	private UserHeroDao userHeroDao;

	@Autowired
	private CommandDao commandDao;

	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;

	@Autowired
	private OnlyoneHourRankRewardDao onlyoneHourRankRewardDao;

	@Autowired
	private OnlyoneWeekRankRewardDao onlyoneWeekRankRewardDao;

	@Autowired
	private OnlyoneTimesRewardDao onlyoneTimesRewardDao;

	@Autowired
	private OnlyoneJoinRewardDao onlyoneJoinRewardDao;

	@Autowired
	private UserDailyGainLogDao userDailyGainLogDao;

	@Autowired
	private UserOnlyOneHeroDao userOnlyOneHeroDao;

	private boolean started = false;

	/**
	 * 测试用的用户ID
	 */
	private List<String> userIds = null;

	/**
	 * 是否暂停
	 */
	private boolean pause = false;

	/**
	 * 开始时间
	 */
	private Date startTime;

	/**
	 * 结束时间
	 */
	private Date endTime;

	/**
	 * 状态
	 */
	private int status = OnlyOneStatus.NOT_STARTED;

	/**
	 * 获取状态描述
	 * 
	 * @param status
	 */
	public String getStatusDesc(int status) {

		switch (status) {
		case OnlyOneStatus.NOT_STARTED:
			return "活动未开始(" + status + ")";
		case OnlyOneStatus.STARTED:
			return "活动进行中(" + status + ")";
		case OnlyOneStatus.END:
			return "活动已经结束(" + status + ")";
		default:
			return "未知状态(" + status + ")";
		}

	}

	@Override
	public boolean enter(String userId) {

		Date now = new Date();

		logger.debug("用户进入千人斩活动.userId[" + userId + "]");

		OnlyoneUserReg onlyOneReg = this.onlyOneDao.getByUserId(userId);

		if (onlyOneReg == null) {

			User user = this.userService.get(userId);

			onlyOneReg = new OnlyoneUserReg();
			onlyOneReg.setVigour(InitDefine.VIGOUR_MAX);
			onlyOneReg.setAddVigourTime(now);
			onlyOneReg.setCreatedTime(now);
			onlyOneReg.setStatus(OnlyOneRegStatus.STATUS_END);
			onlyOneReg.setUsername(user.getUsername());
			onlyOneReg.setUserId(userId);
		}

		this.onlyOneDao.add(onlyOneReg);

		return false;
	}

	/**
	 * 推送战报
	 * 
	 * @param userId
	 */
	private void pushBattle(String userId, Map<String, String> params) {

		params.put("userId", userId);

		Command command = new Command();
		command.setCommand(CommandType.COMMAND_ONLYONE_PUSH_BATTLE);
		command.setType(CommandType.PUSH_USER);
		command.setPriority(Priority.HIGH);
		command.setParams(params);

		commandDao.add(command);
	}

	/**
	 * 推送战斗
	 * 
	 * @param attackUserId
	 * @param attackUsername
	 * @param defenseUserId
	 * @param defenseUsername
	 * @param copper
	 * @param falg
	 * @param report
	 */
	public void pushBattle(OnlyoneUserReg onlyOneRegA, OnlyoneUserReg onlyOneRegB, int pointA, int pointB, int flag, String report) {

		Map<String, String> params = new HashMap<String, String>();
		params.put("attackUserId", onlyOneRegA.getUserId());
		params.put("attackUsername", onlyOneRegA.getUsername());
		params.put("defenseUserId", onlyOneRegB.getUserId());
		params.put("defenseUsername", onlyOneRegB.getUsername());
		params.put("pointA", String.valueOf(pointA));
		params.put("pointB", String.valueOf(pointB));
		params.put("flag", String.valueOf(flag));
		params.put("report", report);

		// 推战斗给防守方
		if (onlyOneRegB.getIsRobot() == 0) {
			params.put("point", String.valueOf(pointB));
			this.pushBattle(onlyOneRegB.getUserId(), params);
		}

		params.put("point", String.valueOf(pointA));
		this.pushBattle(onlyOneRegA.getUserId(), params);

	}

	@Override
	public boolean quit(String userId) {

		OnlyoneUserReg onlyOneReg = this.onlyOneDao.getByUserId(userId);

		if (onlyOneReg == null) {
			return false;
		}

		onlyOneReg.setStatus(OnlyOneRegStatus.STATUS_QUIT);

		this.onlyOneDao.add(onlyOneReg);

		return true;
	}

	/**
	 * 活动开始
	 */
	private void start() {

		this.switchStatus(OnlyOneStatus.STARTED);

		this.onlyOneDao.cleanData();

		int day = DateUtils.getDayOfWeek();
		if (day == 1) {
			this.onlyOneDao.cleanWeekRank();
		}

		this.initCutOffTime();
	}

	private void switchStatus(int status) {
		this.status = status;
		this.onlyOneDao.setStatus(status);
	}

	/**
	 * 初始化结算时间
	 */
	private void initCutOffTime() {

		Date now = new Date();

		this.cutOffTimeList.clear();
		Date startTime = DateUtils.str2Date(DateUtils.getDate() + " 10:30:00");

		if (now.before(startTime)) {
			cutOffTimeList.add(startTime);
		}
		for (int i = 1; i <= 30; i++) {
			Date time = DateUtils.add(startTime, Calendar.MINUTE, 30 * i);
			if (time.after(now) && time.before(DateUtils.add(this.getEndTime(), Calendar.MINUTE, 1))) {
				cutOffTimeList.add(time);
			}
		}
	}

	/**
	 * 结算
	 */
	private void cutOff() {

		Date now = new Date();

		Iterator<Date> iterator = this.cutOffTimeList.iterator();

		while (iterator.hasNext()) {

			Date date = iterator.next();
			if (date.before(now)) {
				boolean moveToWeek = OnlyOneHelper.isSpCut(date);
				try {
					this._cutOff(moveToWeek);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
				iterator.remove();
			}

		}
	}

	/**
	 * 周结算
	 */
	private void weekCutOff() {

		int date = DateUtils.getDayOfWeek();
		if (date != 6) {
			return;
		}

		if (this.onlyOneDao.isWeekCutOff()) {
			return;
		}

		this.onlyOneDao.addWeekCutOffLog();

		this._weekCutOff();

	}

	private void _weekCutOff() {

		Date now = new Date();

		List<OnlyoneWeekRank> list = this.onlyOneDao.getWeekRank();
		int rank = 1;
		for (OnlyoneWeekRank onlyoneWeekRank : list) {

			String userId = onlyoneWeekRank.getUserId();
			OnlyoneWeekRankReward reward = this.onlyoneWeekRankRewardDao.get(rank);

			if (reward == null) {
				logger.info("该名次没有奖励.userId[" + userId + "], rank[" + rank + "]");
				continue;
			}

			OnlyoneUserReward onlyoneUserReward = new OnlyoneUserReward();
			onlyoneUserReward.setCreatedTime(now);
			onlyoneUserReward.setRank(rank);
			onlyoneUserReward.setRewards(reward.getToolIds());
			onlyoneUserReward.setStatus(OnlyoneRewardStatus.NEW);
			onlyoneUserReward.setType(OnlyoneRewardType.WEEK_RANK);
			onlyoneUserReward.setUpdatedTime(now);
			onlyoneUserReward.setUserId(userId);

			rank += 1;

			this.onlyOneDao.addReward(onlyoneUserReward);
		}
	}

	/**
	 * 半小时结算
	 */
	private void _cutOff(boolean moveToWeek) {

		Collection<OnlyoneUserReg> list = this.onlyOneDao.getRegList();
		List<OnlyoneUserReg> l = new ArrayList<OnlyoneUserReg>();
		for (OnlyoneUserReg reg : list) {
			l.add((OnlyoneUserReg) reg.clone());
		}

		Collections.sort(l, new Comparator<OnlyoneUserReg>() {

			@Override
			public int compare(OnlyoneUserReg o1, OnlyoneUserReg o2) {
				if (o1.getPoint() >= o2.getPoint()) {
					return -1;
				} else {
					return 1;
				}
			}

		});

		Date now = new Date();

		int rank = 1;
		for (OnlyoneUserReg reg : l) {

			String userId = reg.getUserId();
			String username = reg.getUsername();

			OnlyoneUserReg onlyOneReg = this.onlyOneDao.getByUserId(userId);

			User user = this.userService.get(userId);

			if (moveToWeek) {
				// 把积分加到周
				this.onlyOneDao.addWeekPoint(userId, username, onlyOneReg.getPoint());
				onlyOneReg.setPointCount(0);
			}

			onlyOneReg.setPoint(onlyOneReg.getPoint() * 0.3);
			this.onlyOneDao.add(onlyOneReg);

			OnlyoneHourRankReward onlyoneHourRankReward = this.onlyoneHourRankRewardDao.get(rank);

			if (onlyoneHourRankReward == null) {
				continue;
			}

			String rewards = OnlyOneHelper.getHourRankReward(onlyoneHourRankReward, user.getLevel(), false);

			OnlyoneUserReward onlyoneUserReward = new OnlyoneUserReward();

			onlyoneUserReward.setUserId(userId);
			onlyoneUserReward.setRank(rank);
			onlyoneUserReward.setRewards(rewards);
			onlyoneUserReward.setStatus(OnlyoneRewardStatus.NEW);
			onlyoneUserReward.setType(OnlyoneRewardType.HOUR_RANK);
			onlyoneUserReward.setRound(OnlyOneHelper.getRound(this.getStartTime(), this.getEndTime()) - 1);
			onlyoneUserReward.setUpdatedTime(now);
			onlyoneUserReward.setCreatedTime(now);

			this.onlyOneDao.addReward(onlyoneUserReward);

			rank += 1;

		}

	}

	/**
	 * 活动结束(推送奖励)
	 */
	private void end() {

		this.switchStatus(OnlyOneStatus.END);

	}

	public boolean startMatcher(String userId) {

		OnlyoneUserReg onlyOneReg = this.onlyOneDao.getByUserId(userId);

		if (onlyOneReg == null) {
			return false;
		}

		Date now = new Date();

		if (onlyOneReg.getEndCdTime() != null && onlyOneReg.getEndCdTime().after(now)) {
			// return false;
		}

		if (onlyOneReg.getStatus() != OnlyOneRegStatus.STATUS_WAIT) {// 已经是等待了则不管

			if (onlyOneReg.getVigour() < 1) {
				String message = "参与匹配失败，精力不足.userId[" + userId + "]";
				throw new ServiceException(VIGOUR_NOT_ENOUGH, message);
			}

			if (onlyOneReg.getVigour() == InitDefine.VIGOUR_MAX) {
				onlyOneReg.setAddVigourTime(now);
			}

			onlyOneReg.setVigour(onlyOneReg.getVigour() - 1);
			onlyOneReg.setStatus(OnlyOneRegStatus.STATUS_WAIT);
			onlyOneReg.setEndCdTime(DateUtils.add(new Date(), Calendar.SECOND, 5));
		}

		this.onlyOneDao.add(onlyOneReg);

		return true;
	}

	public Date getStartTime() {
		if (startTime == null) {
			if (Config.ins().isDebug()) {
				startTime = new Date();
			} else {
				startTime = DateUtils.str2Date(DateUtils.getDate() + " 10:00:00");	
			}
		}
		int date = DateUtils.getDayOfWeek();
		if (date == 6|| date ==7) {	
			startTime = DateUtils.str2Date(DateUtils.getDate(DateUtils.addDays(new Date(),1)) +" 10:00:00");
		}
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		if (endTime == null) {
			if (Config.ins().isDebug()) {
				endTime = DateUtils.add(new Date(), Calendar.MINUTE, 60 * 2);
			} else {
				endTime = DateUtils.str2Date(DateUtils.getDate() + " 21:00:00");
			}

		}
		int date = DateUtils.getDayOfWeek();
		if (date == 6|| date ==7) {	
			endTime = DateUtils.str2Date(DateUtils.getDate(DateUtils.addDays(new Date(),1)) +" 21:00:00");
		}
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * 状态监控方法
	 */
	private void statusCheck() {

		Date now = new Date();

		// logger.debug("状态切换线程.status[" + getStatusDesc(this.status) + "]");

		if (this.status == OnlyOneStatus.NOT_STARTED) {

			if (now.after(this.getStartTime())) {
				logger.debug("活动开始");
				this.start();

			}

		} else if (this.status == OnlyOneStatus.STARTED) {

			if (now.after(this.getEndTime())) {
				logger.debug("活动结束");
				this.end();
			}

		} else if (this.status == OnlyOneStatus.END) {

			// 已经不是同一天了，到了第二天
			if (!DateUtils.isSameDay(now, this.getEndTime())) {

				logger.debug("进入第二天");
				this.switchStatus(OnlyOneStatus.NOT_STARTED);
				this.startTime = DateUtils.addDays(this.getStartTime(), 1);
				this.endTime = DateUtils.addDays(this.getEndTime(), 1);
			}

		}

	}

	/**
	 * 主工作方法
	 */
	private int execute() {

		if (this.status != OnlyOneStatus.STARTED) {
			logger.debug("活动未开始");
			return 5000;
		}

		if (this.pause == true) {// 暂停
			logger.debug("活动暂停");
			return 5000;
		}

		this.handle();

		return 1000;

	}

	/**
	 * 处理比赛
	 * 
	 * @param winCount
	 */
	private void handle() {

		if (this.concurrency) {

			Runnable task = new Runnable() {

				@Override
				public void run() {
					_handle();
				}
			};

			taskExecutor.execute(task);

		} else {

			_handle();

		}
	}

	private void _handle() {

		List<OnlyoneUserReg> onlyOneRegList = this.onlyOneDao.getRegList(OnlyOneRegStatus.STATUS_WAIT);

		// logger.debug("当前队列人数.size[" + onlyOneRegList.size() + "]");

		if (onlyOneRegList.isEmpty()) {
			return;
		}

		for (int i = 0; i < onlyOneRegList.size(); i++) {

			OnlyoneUserReg onlyOneRegA = onlyOneRegList.get(i);

			if (!this.isCanMatcher(onlyOneRegA)) {
				continue;
			}

			onlyOneRegA.setStatus(OnlyOneRegStatus.STATUS_MATCH);

			OnlyoneUserReg onlyOneRegB = this.matcher(onlyOneRegList, i);
			if (onlyOneRegB != null) {
				// 比赛处理
				onlyOneRegA.setStatus(OnlyOneRegStatus.STATUS_DOING);

				onlyOneRegB.setStatus(OnlyOneRegStatus.STATUS_DOING);

				this.handleMatch(onlyOneRegA, onlyOneRegB);
			} else {

				Date date = DateUtils.add(onlyOneRegA.getEndCdTime(), Calendar.SECOND, 5);
				Date now = new Date();
				if (date.before(now)) {

					// 没有则打机器人
					logger.debug("区配机器人.userId[" + onlyOneRegA.getUserId() + "]");
					onlyOneRegB = new OnlyoneUserReg();
					RobotUser robotUser = this.getRobotUser(onlyOneRegA.getUserId());
					if (robotUser != null) {
						onlyOneRegB.setIsRobot(1);
						onlyOneRegB.setUserId(robotUser.getUserId());
						onlyOneRegB.setUsername(robotUser.getUsername());

						this.handleMatch(onlyOneRegA, onlyOneRegB);
					} else {
						onlyOneRegA.setStatus(OnlyOneRegStatus.STATUS_WAIT);
					}

				} else {
					onlyOneRegA.setStatus(OnlyOneRegStatus.STATUS_WAIT);
				}
			}

		}
	}

	private RobotUser getRobotUser(String userId) {

		List<UserHeroBO> list = heroService.getUserHeroList(userId, 1);
		int capability = HeroHelper.getCapability(list);
		for (int i = 0; i < 7; i++) {
			RobotUser robotUser = this.robotService.getRobotUser((int) (capability * (0.7 - i * 0.1)), capability);
			if (robotUser != null) {
				return robotUser;
			}
		}
		return null;

	}

	/**
	 * 匹配
	 * 
	 * @param onlyOneRegList
	 * @param startIndex
	 * @param winCount
	 * @return
	 */
	private OnlyoneUserReg matcher(List<OnlyoneUserReg> onlyOneRegList, int startIndex) {

		OnlyoneUserReg onlyOneRegB = null;

		// 从后往前匹配
		for (int j = startIndex; j < onlyOneRegList.size(); j++) {

			onlyOneRegB = onlyOneRegList.get(j);

			if (this.isCanMatcher(onlyOneRegB)) {
				onlyOneRegB.setStatus(OnlyOneRegStatus.STATUS_DOING);
				return onlyOneRegB;
			}
		}

		return null;
	}

	/**
	 * 是否可以匹配
	 * 
	 * @param onlyOneReg
	 * @return
	 */
	private boolean isCanMatcher(OnlyoneUserReg onlyOneReg) {

		if (onlyOneReg.getStatus() != OnlyOneRegStatus.STATUS_WAIT) {
			logger.debug("用户不是等待状态.userId[" + onlyOneReg.getUserId() + "], username[" + onlyOneReg.getUsername() + "], status[" + onlyOneReg.getStatus() + "]");
			return false;
		}

		return true;
	}

	/**
	 * 处理千人斩分组比赛
	 * 
	 * @param onlyOneRegA
	 * @param onlyOneRegB
	 */
	private void handleMatch(final OnlyoneUserReg onlyOneRegA, final OnlyoneUserReg onlyOneRegB) {

		if (this.concurrency) {

			Runnable task = new Runnable() {

				@Override
				public void run() {
					_handleMatch(onlyOneRegA, onlyOneRegB);
				}
			};

			taskExecutor.execute(task);

		} else {
			_handleMatch(onlyOneRegA, onlyOneRegB);
		}
	}

	/**
	 * 
	 * @param onlyOneRegA
	 * @param onlyOneRegB
	 */
	private void _handleMatchEnd(OnlyoneUserReg onlyOneRegA, OnlyoneUserReg onlyOneRegB, int flag, String report) {

		onlyOneRegA.setStatus(OnlyOneRegStatus.STATUS_END);
		onlyOneRegB.setStatus(OnlyOneRegStatus.STATUS_END);

		int winTimesA = onlyOneRegA.getWinTimes();
		int winTimesB = onlyOneRegB.getWinTimes();

		int pointA = 0;
		int pointB = 0;

		// 进攻方赢
		if (flag == 1) {

			pointA = this.updateWin(onlyOneRegA);
			pointB = this.updateLose(onlyOneRegB);

			if (winTimesB >= 5) {
				this.userService.addHonour(onlyOneRegA.getUserId(), 30, ToolUseType.ADD_ONLYONE_STOP_SKILL);
				this.messageService.sendOnlyoneStopKillMsg(onlyOneRegA.getUserId(), onlyOneRegA.getUsername(), onlyOneRegB.getUsername(), winTimesB, 30);
			}

		} else {

			pointB = this.updateWin(onlyOneRegB);
			pointA = this.updateLose(onlyOneRegA);

			if (winTimesA >= 5) {
				this.userService.addHonour(onlyOneRegB.getUserId(), 30, ToolUseType.ADD_ONLYONE_STOP_SKILL);
				this.messageService.sendOnlyoneStopKillMsg(onlyOneRegB.getUserId(), onlyOneRegB.getUsername(), onlyOneRegA.getUsername(), winTimesA, 30);
			}

		}

		// 推送战斗
		this.pushBattle(onlyOneRegA, onlyOneRegB, pointA, pointB, flag, report);

	}

	private void sendWinTimesReward(String userId, int times) {

		String title = "千人斩奖励";
		String content = "请领取" + times + "连杀奖励";
		String toolIds = OnlyOneHelper.getWinReward(times);

		User user = this.userService.get(userId);
		if (user == null) {
			return;
		}
		String userLodoIds = user.getLodoId() + ",";

		this.mailService.send(title, content, toolIds, MailTarget.USERS, userLodoIds, null, null, null);
	}

	/**
	 * 更新为胜利
	 * 
	 * @param arenReg
	 * @param lifeMap
	 */
	private int updateWin(OnlyoneUserReg onlyOneReg) {

		if (onlyOneReg.getIsRobot() == 1) {
			return 0;
		}

		int winCount = onlyOneReg.getWinCount();
		int winTimes = onlyOneReg.getWinTimes();
		int totalCount = onlyOneReg.getTotalCount();
		int pointCount = onlyOneReg.getPointCount();
		double point = onlyOneReg.getPoint();

		winCount += 1;
		winTimes += 1;
		totalCount += 1;
		pointCount += 1;

		int gainPoint = this.onlyoneTimesRewardDao.getPoint(pointCount);
		if (OnlyOneHelper.isDouble()) {
			gainPoint = gainPoint * 2;
		}
		if (winTimes >= 10) {
			gainPoint = gainPoint * 2;
		}

		point += gainPoint;

		onlyOneReg.setWinCount(winCount);
		onlyOneReg.setWinTimes(winTimes);
		onlyOneReg.setTotalCount(totalCount);
		onlyOneReg.setPointCount(pointCount);
		onlyOneReg.setPoint(point);

		String userId = onlyOneReg.getUserId();

		// 参与奖
		this.sendJoinReward(userId, totalCount);

		this.onlyOneDao.add(onlyOneReg);

		if (winTimes == 5 || winTimes == 8 || winTimes == 10 || winTimes == 30 || winTimes == 50 || winTimes == 100) {
			if (winTimes == 30 || winTimes == 50 || winTimes == 100) {
				int gainType = getDailyGainLimitType(winTimes);
				int userDailyGaiin = this.userDailyGainLogDao.getUserDailyGain(userId, gainType);
				if (userDailyGaiin == 0) {
					int num = 2;
					if (winTimes == 100) {
						num = 4;
					}
					this.userDailyGainLogDao.addUserDailyGain(userId, gainType, 1);
					this.messageService.sendOnlyoneKillMsg(onlyOneReg.getUserId(), onlyOneReg.getUsername(), winTimes, num);
					sendWinTimesReward(onlyOneReg.getUserId(), winTimes);
				}
			} else {
				this.messageService.sendOnlyoneKillMsg(onlyOneReg.getUserId(), onlyOneReg.getUsername(), winTimes, 0);
			}
		}

		return gainPoint;

	}

	private int getDailyGainLimitType(int times) {
		switch (times) {
		case 30:
			return UserDailyGainLogType.ONLY_ONE_30_TIMES_REWARD;
		case 50:
			return UserDailyGainLogType.ONLY_ONE_50_TIMES_REWARD;
		case 100:
			return UserDailyGainLogType.ONLY_ONE_100_TIMES_REWARD;
		default:
			return 0;
		}
	}

	/**
	 * 发送参与奖
	 * 
	 * @param userId
	 * @param totalCount
	 */
	private void sendJoinReward(String userId, int totalCount) {

		User user = this.userService.get(userId);
		Date now = new Date();

		OnlyoneJoinReward onlyoneJoinReward = this.onlyoneJoinRewardDao.get(totalCount);
		if (onlyoneJoinReward != null) {

			String rewards = OnlyOneHelper.getJoinReward(onlyoneJoinReward, user.getLevel(), false);

			OnlyoneUserReward onlyoneUserReward = new OnlyoneUserReward();

			onlyoneUserReward.setUserId(userId);
			onlyoneUserReward.setRank(0);
			onlyoneUserReward.setTimes(totalCount);
			onlyoneUserReward.setRewards(rewards);
			onlyoneUserReward.setStatus(OnlyoneRewardStatus.NEW);
			onlyoneUserReward.setType(OnlyoneRewardType.TIMES_REWARD);
			onlyoneUserReward.setUpdatedTime(now);
			onlyoneUserReward.setCreatedTime(now);

			this.onlyOneDao.addReward(onlyoneUserReward);
		}
	}

	/**
	 * 更新为失败
	 * 
	 * @param onlyOneReg
	 */
	private int updateLose(OnlyoneUserReg onlyOneReg) {

		if (onlyOneReg.getIsRobot() == 1) {
			return 0;
		}

		int totalCount = onlyOneReg.getTotalCount();
		int pointCount = onlyOneReg.getPointCount();
		double point = onlyOneReg.getPoint();

		totalCount += 1;
		pointCount += 1;

		int gainPoint = this.onlyoneTimesRewardDao.getPoint(pointCount);
		gainPoint = gainPoint / 3;
		if (OnlyOneHelper.isDouble()) {
			gainPoint = gainPoint * 2;
		}
		point += gainPoint;

		onlyOneReg.setWinTimes(0);
		onlyOneReg.setTotalCount(totalCount);
		onlyOneReg.setPointCount(pointCount);
		onlyOneReg.setPoint(point);

		String userId = onlyOneReg.getUserId();

		sendJoinReward(userId, totalCount);

		this.onlyOneDao.add(onlyOneReg);

		this.onlyOneDao.deleteOnlyoneUserHero(userId);

		return gainPoint;

	}

	private void _handleMatch(final OnlyoneUserReg onlyOneRegA, final OnlyoneUserReg onlyOneRegB) {

		// 进攻方信息
		final BattleBO attack = new BattleBO();

		List<BattleHeroBO> attackHeroBOList = this.heroService.getUserOnlyOneBattleHeroBOList(onlyOneRegA.getUserId());

		if (attackHeroBOList.isEmpty()) {
			// 未布阵容默认上阵英雄列表
			checkHero(onlyOneRegA.getUserId(), new ArrayList<UserOnlyOneHero>());
			attackHeroBOList = this.heroService.getUserOnlyOneBattleHeroBOList(onlyOneRegA.getUserId());
		}
		attack.setBattleHeroBOList(attackHeroBOList);

		// 防守方信息
		final BattleBO defense = new BattleBO();

		if (onlyOneRegB.getIsRobot() == 0) {
			List<BattleHeroBO> defenseHeroBOList = this.heroService.getUserOnlyOneBattleHeroBOList(onlyOneRegB.getUserId());

			if (defenseHeroBOList.isEmpty()) {
				// 未布阵容默认上阵英雄列表
				checkHero(onlyOneRegB.getUserId(), new ArrayList<UserOnlyOneHero>());
				defenseHeroBOList = this.heroService.getUserOnlyOneBattleHeroBOList(onlyOneRegB.getUserId());
			}

			defense.setBattleHeroBOList(defenseHeroBOList);
		} else {
			List<BattleHeroBO> defenseHeroBOList = this.robotService.getRobotUserBattleHeroBOList(onlyOneRegB.getUserId());
			defense.setBattleHeroBOList(defenseHeroBOList);
		}

		battleService.fight(attack, defense, BattleType.ONLY_ONE, new EventHandle() {

			@Override
			public boolean handle(Event event) {

				int flag = event.getInt("flag");
				String report = event.getString("report");
				@SuppressWarnings("unchecked")
				Map<String, Integer> lifeMap = (Map<String, Integer>) event.getObject("life");
				if (lifeMap == null) {
					lifeMap = new HashMap<String, Integer>();
				}
				// 进攻方赢
				if (flag == 1) {
					// 保存残血
					for (BattleHeroBO bo : attack.getBattleHeroBOList()) {
						Integer life = lifeMap.get("L_a" + bo.getPos());
						Integer morale = lifeMap.get("M_a" + bo.getPos());
						if (life == null) {
							life = 0;
						}
						if (morale == null) {
							morale = 0;
						}

						UserOnlyOneHero userOnlyOneHero = new UserOnlyOneHero();
						userOnlyOneHero.setUserId(onlyOneRegA.getUserId());
						userOnlyOneHero.setUserHeroId(bo.getUserHeroId());
						userOnlyOneHero.setPos(bo.getPos());
						userOnlyOneHero.setHeroId(heroService.getHeroId(bo.getSystemHeroId()));
						userOnlyOneHero.setLife(life);
						userOnlyOneHero.setMorale(morale);
						userOnlyOneHeroDao.updateHero(onlyOneRegA.getUserId(), userOnlyOneHero);

					}
					// 失败敌方所有英雄满血复活
					if (onlyOneRegB.getIsRobot() == 0) {
						userOnlyOneHeroDao.delteHero(onlyOneRegB.getUserId());
						// 未布阵容默认上阵英雄列表
						checkHero(onlyOneRegB.getUserId(), new ArrayList<UserOnlyOneHero>());
					}
				} else {

					if (onlyOneRegB.getIsRobot() == 0) {
						for (BattleHeroBO bo : defense.getBattleHeroBOList()) {
							Integer life = lifeMap.get("L_d" + bo.getPos());
							Integer morale = lifeMap.get("M_d" + bo.getPos());
							if (life == null) {
								life = 0;
							}
							if (morale == null) {
								morale = 0;
							}

							UserOnlyOneHero userOnlyOneHero = new UserOnlyOneHero();
							userOnlyOneHero.setUserId(onlyOneRegB.getUserId());
							userOnlyOneHero.setUserHeroId(bo.getUserHeroId());
							userOnlyOneHero.setPos(bo.getPos());
							userOnlyOneHero.setLife(life);
							userOnlyOneHero.setHeroId(heroService.getHeroId(bo.getSystemHeroId()));
							userOnlyOneHero.setMorale(morale);
							userOnlyOneHeroDao.updateHero(onlyOneRegB.getUserId(), userOnlyOneHero);

						}
					}
					// 失败己方所有英雄满血复活
					userOnlyOneHeroDao.delteHero(onlyOneRegA.getUserId());
					// 未布阵容默认上阵英雄列表
					checkHero(onlyOneRegA.getUserId(), new ArrayList<UserOnlyOneHero>());
				}

				// 处理比赛结束
				_handleMatchEnd(onlyOneRegA, onlyOneRegB, flag, report);

				return true;
			}
		});

	}

	@Override
	public List<OnlyOneRewardBO> getRewardList(String userId) {

		List<OnlyOneRewardBO> list = new ArrayList<OnlyOneRewardBO>();

		List<OnlyoneUserReward> l = this.onlyOneDao.getRewardList(userId);
		for (OnlyoneUserReward reward : l) {
			OnlyOneRewardBO bo = new OnlyOneRewardBO();
			bo.setDropToolBOList(DropToolHelper.parseDropTool(reward.getRewards()));
			bo.setId(reward.getId());
			bo.setType(reward.getType());
			bo.setTitle(OnlyOneHelper.getRewardTitle(reward));
			bo.setDate(DateUtils.getDateStr(reward.getCreatedTime().getTime(), "MM月dd日 HH:mm"));

			list.add(bo);

		}
		return list;

	}

	@Override
	public CommonDropBO receive(String userId, int id) {

		OnlyoneUserReward onlyoneUserReward = this.onlyOneDao.getReward(userId, id);
		if (onlyoneUserReward == null) {
			String message = "领取千人斩奖励失败，奖励不存在.userId[" + userId + "], id[" + id + "]";
			logger.error(message);
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

		if (onlyoneUserReward.getStatus() != OnlyoneRewardStatus.NEW) {
			String message = "领取千人斩奖励失败，奖励已经领取过.userId[" + userId + "], id[" + id + "]";
			logger.error(message);
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

		if (!this.onlyOneDao.updateReward(userId, id, OnlyoneRewardStatus.RECIVED)) {
			String message = "领取千人斩奖励失败，奖励已经领取过.userId[" + userId + "], id[" + id + "]";
			logger.error(message);
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

		int useType = 0;
		if (onlyoneUserReward.getType() == OnlyoneRewardType.WEEK_RANK) {
			useType = ToolUseType.ADD_ONLYONE_WEEK_RANK;
		} else if (onlyoneUserReward.getType() == OnlyoneRewardType.HOUR_RANK) {
			useType = ToolUseType.ADD_ONLYONE_HOUR_RANK;
		} else if (onlyoneUserReward.getType() == OnlyoneRewardType.TIMES_REWARD) {
			useType = ToolUseType.ADD_ONLYONE_TIMES;
		}

		CommonDropBO commonDropBO = this.toolService.give(userId, onlyoneUserReward.getRewards(), useType);

		return commonDropBO;
	}

	public void init() {

		if (started) {
			return;
		}

		if (!Config.ins().isGameServer()) {
			return;
		}

		this.status = this.onlyOneDao.getStatus();
		if (this.status != OnlyOneStatus.NOT_STARTED) {
			this.initCutOffTime();
			this.onlyOneDao.loadData();
		}

		started = true;

		// 主工作线程
		new Thread(new Runnable() {

			public void run() {
				while (true) {

					int sleepTimes = 1000;

					try {
						sleepTimes = execute();
					} catch (Throwable t) {

						logger.error(t.getMessage(), t);

					}

					if (sleepTimes > 0) {

						try {
							// logger.debug("主工作线程进入休眠.times[" + sleepTimes +
							// "]");
							Thread.sleep(sleepTimes);
							// logger.debug("主工作线程休眼醒来.times[" + sleepTimes +
							// "]");
						} catch (InterruptedException ie) {
							// logger.error(ie.getMessage(), ie);
						}
					}

				}
			}

		}).start();

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

		// 半小时结算线程
		new Thread(new Runnable() {

			public void run() {
				while (true) {
					try {
						cutOff();
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

		// 周结算线程
		new Thread(new Runnable() {

			public void run() {
				while (true) {
					try {
						weekCutOff();
					} catch (Throwable t) {
						logger.error(t.getMessage(), t);
					}

					try {
						Thread.sleep(1000 * 30);
					} catch (InterruptedException ie) {
						logger.error(ie.getMessage(), ie);
					}

				}
			}

		}).start();

		if (Config.ins().isDebug()) {
			// test();
		}

	}

	private void test() {

		// 状态切换线程
		new Thread(new Runnable() {

			public void run() {

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

		}).start();
	}

	private List<String> getTestUserIds() {
		if (userIds == null) {
			userIds = new ArrayList<String>();
			List<User> userList = this.userDao.listOrderByLevelDesc(0, 100);
			for (User user : userList) {
				userIds.add(user.getUserId());
			}
		}

		return userIds;
	}

	private void _test() throws InterruptedException {

		int init = 0;

		List<String> userIds = this.getTestUserIds();

		while (true) {

			if (this.status == OnlyOneStatus.STARTED) {

				if (init == 0) {

					for (String userId : userIds) {
						this.enter(userId);
					}

					init = 1;
				}

				for (String userId : userIds) {
					this.startMatcher(userId);
				}
			}

			Thread.sleep(5000);
		}

	}

	@Override
	public List<OnlyOneRankBO> getRankList(String userId, boolean self) {

		List<OnlyoneUserReg> onlyOnyRankList = this.onlyOneDao.getRankList();

		int rank = 1;
		for (OnlyoneUserReg reg : onlyOnyRankList) {
			reg.setRank(rank++);
		}

		List<OnlyoneUserReg> list = OnlyOneHelper.getRank(onlyOnyRankList, userId, self);

		List<OnlyOneRankBO> onlyOneRankBOList = new ArrayList<OnlyOneRankBO>();

		for (OnlyoneUserReg onlyOneReg : list) {

			OnlyOneRankBO onlyOneRankBO = new OnlyOneRankBO();
			onlyOneRankBO.setUsername(onlyOneReg.getUsername());
			onlyOneRankBO.setPoint((int) onlyOneReg.getPoint());
			onlyOneRankBO.setRank(onlyOneReg.getRank());

			onlyOneRankBOList.add(onlyOneRankBO);
		}

		return onlyOneRankBOList;
	}

	@Override
	public List<OnlyOneRankBO> getWinRankList() {

		List<OnlyoneUserReg> onlyOnyRankList = this.onlyOneDao.getWinRankList();

		int rank = 1;

		List<OnlyOneRankBO> onlyOneRankBOList = new ArrayList<OnlyOneRankBO>();

		for (OnlyoneUserReg onlyOneReg : onlyOnyRankList) {

			OnlyOneRankBO onlyOneRankBO = new OnlyOneRankBO();
			onlyOneRankBO.setUsername(onlyOneReg.getUsername());
			onlyOneRankBO.setWin(onlyOneReg.getWinTimes());
			onlyOneRankBO.setRank(rank++);

			onlyOneRankBOList.add(onlyOneRankBO);
		}

		return onlyOneRankBOList;

	}

	@Override
	public int getRank(String userId) {
		int rank = 1;
		List<OnlyoneUserReg> onlyOnyRankList = this.onlyOneDao.getRankList();
		for (OnlyoneUserReg reg : onlyOnyRankList) {
			if (StringUtils.equals(reg.getUserId(), userId)) {
				return rank;
			}
			rank += 1;
		}
		return rank;
	}

	@Override
	public OnlyOneRegBO getRegBO(String userId) {

		OnlyoneUserReg onlyOneReg = this.getByUserId(userId);
		if (onlyOneReg != null) {

			User user = this.userService.get(userId);

			OnlyOneRegBO onlyOneRegBO = new OnlyOneRegBO();
			onlyOneRegBO.setPoint((int) onlyOneReg.getPoint());
			onlyOneRegBO.setTotalCount(onlyOneReg.getTotalCount() + 1);
			onlyOneRegBO.setVigour(onlyOneReg.getVigour());

			long diff = System.currentTimeMillis() - onlyOneReg.getAddVigourTime().getTime();
			if (diff < 0) {
				diff = 0;
			}

			onlyOneRegBO.setVigourAddSecond(300 - (int) (diff / 1000));

			int rank = this.getRank(userId);
			onlyOneRegBO.setRank(rank);

			int point = this.onlyoneTimesRewardDao.getPoint(onlyOneReg.getPointCount() + 1);
			OnlyoneHourRankReward onlyoneHourRankReward = this.onlyoneHourRankRewardDao.get(rank);
			OnlyoneJoinReward onlyoneJoinReward = this.onlyoneJoinRewardDao.getNextReward(onlyOneReg.getTotalCount());

			if (OnlyOneHelper.isDouble()) {
				point = point * 2;
			}
			if (onlyOneReg.getWinTimes() + 1 >= 10) {
				point = point * 2;
			}

			onlyOneRegBO.setText1(OnlyOneHelper.getText1(point));
			onlyOneRegBO.setText2(OnlyOneHelper.getText2(this.cutOffTimeList));
			onlyOneRegBO.setText3(OnlyOneHelper.getHourRankReward(onlyoneHourRankReward, user.getLevel(), true));
			onlyOneRegBO.setText4(String.valueOf(onlyoneJoinReward.getTimes()));
			if (onlyOneReg.getTotalCount() > onlyoneJoinReward.getTimes()) {
				onlyOneRegBO.setText5("");
			} else {
				onlyOneRegBO.setText5(OnlyOneHelper.getJoinReward(onlyoneJoinReward, user.getLevel(), true));
			}
			onlyOneRegBO.setText6(OnlyOneHelper.getText6());

			onlyOneRegBO.setNextTime(OnlyOneHelper.getNextTime(this.getStartTime(), this.getEndTime(), this.cutOffTimeList));
			onlyOneRegBO.setMulti(OnlyOneHelper.getMutil(onlyOneReg.getPointCount() + 1, onlyOneReg.getWinTimes() + 1));
			onlyOneRegBO.setRound(OnlyOneHelper.getRound(this.getStartTime(), this.getEndTime()));

			Date now = new Date();

			int userStatus = OnlyOneUserStatus.OK;
			if (DateUtils.getDayOfWeek()==6||DateUtils.getDayOfWeek()==7||now.before(this.getStartTime()) || now.after(this.getEndTime())) {
				userStatus = OnlyOneUserStatus.NOT_START;
			} else if (onlyOneReg.getStatus() == OnlyOneRegStatus.STATUS_WAIT) {
				userStatus = OnlyOneUserStatus.IS_WAITING;
			}
			onlyOneRegBO.setStatus(userStatus);

			List<OnlyoneUserReward> list = this.onlyOneDao.getRewardList(userId);
			if (!list.isEmpty()) {
				onlyOneRegBO.setReward(1);
			}

			return onlyOneRegBO;
		}

		return null;
	}

	@Override
	public OnlyoneUserReg getByUserId(String userId) {

		OnlyoneUserReg reg = this.onlyOneDao.getByUserId(userId);
		if (reg == null) {
			return null;
		}

		Date now = new Date();// 当前时间
		Date addVigourTime = reg.getAddVigourTime();// 上次添加精力的时间
		long timestamp1 = now.getTime();
		long timestamp2 = addVigourTime.getTime();
		long sub = timestamp1 - timestamp2;
		if (sub > InitDefine.VIGOUR_ADD_INTERVAL) {// 时间到了

			Date newVigourAddTime;
			int addVigour;
			int newVigour;

			if (reg.getVigour() >= InitDefine.VIGOUR_MAX) {// 如果精力已经超过上限
				addVigour = 0;
				newVigour = reg.getVigour();
				newVigourAddTime = new Date();
			} else {
				addVigour = (int) (sub / InitDefine.VIGOUR_ADD_INTERVAL);// 可以加多少精力
				newVigour = reg.getVigour() + addVigour;
				if (newVigour > InitDefine.VIGOUR_MAX) {
					newVigour = InitDefine.VIGOUR_MAX;
					addVigour = InitDefine.VIGOUR_MAX - reg.getVigour();
					newVigourAddTime = new Date();
				} else {
					long leaveTimes = addVigour * InitDefine.VIGOUR_ADD_INTERVAL - sub;// 要把余下的时间给用户算回去
					newVigourAddTime = DateUtils.add(now, Calendar.MILLISECOND, (int) leaveTimes);
				}
			}

			reg.setVigour(newVigour);
			reg.setAddVigourTime(newVigourAddTime);
		}

		return reg;
	}

	@Override
	public void execute(String cmd) {
		if (StringUtils.equalsIgnoreCase(cmd, "PAUSE")) {
			this.pause = true;
		} else if (this.pause || StringUtils.equalsIgnoreCase(cmd, "CONTINUE")) {
			this.pause = false;
		}
	}

	@Override
	public List<UserHeroBO> showHero(String userId) {
		List<UserHeroBO> list = new ArrayList<UserHeroBO>();
		List<UserOnlyOneHero> userOnlyOneList = userOnlyOneHeroDao.getHeros(userId);

		checkHero(userId, userOnlyOneList);
		List<String> idList = new ArrayList<String>();
		for (UserOnlyOneHero userOnlyOneHero : userOnlyOneList) {
			UserHeroBO userHero = heroService.getUserHeroBO(userId, userOnlyOneHero.getUserHeroId());
			// 武将已删除
			if (userHero == null) {
				idList.add(userOnlyOneHero.getUserHeroId());
				continue;
			}
			userHero.setMaxLife(userHero.getLife());
			userHero.setLife(userOnlyOneHero.getLife());
			userHero.setPos(userOnlyOneHero.getPos());
			list.add(userHero);
		}
		if (!idList.isEmpty()) {
			userOnlyOneHeroDao.delteHero(userId, idList);
		}
		return list;
	}

	public void checkHero(String userId, List<UserOnlyOneHero> userOnlyOneList) {
		List<UserOnlyOneHero> list = getHeros(userId);
		List<UserOnlyOneHero> addlist = new ArrayList<UserOnlyOneHero>();

		List<String> userOnlyOneIds = new ArrayList<String>();
		if (userOnlyOneList.isEmpty()) {
			userOnlyOneList.addAll(list);
			userOnlyOneHeroDao.insertHero(userId, userOnlyOneList);
		} else {
			for (UserOnlyOneHero userOnlyOneHero : userOnlyOneList) {
				userOnlyOneIds.add(userOnlyOneHero.getUserHeroId());
			}
			for (UserOnlyOneHero userOnlyOneHero : list) {
				if (!userOnlyOneIds.contains(userOnlyOneHero.getUserHeroId())) {
					userOnlyOneHero.setPos(0);
					userOnlyOneList.add(userOnlyOneHero);
					addlist.add(userOnlyOneHero);
				}
			}
			if (!addlist.isEmpty()) {
				userOnlyOneHeroDao.insertHero(userId, addlist);
			}
		}
	}

	/**
	 * 千人斩根据条件筛选上阵英雄
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserOnlyOneHero> getHeros(String userId) {
		List<UserOnlyOneHero> list = new ArrayList<UserOnlyOneHero>();
		List<UserHero> ulist = userHeroDao.getUserHeroList(userId);
		for (UserHero userHero : ulist) {
			UserOnlyOneHero userOnlyOneHero = new UserOnlyOneHero();
			UserHeroBO userHeroBO = heroService.getUserHeroBO(userId, userHero.getUserHeroId());
			BeanUtils.copyProperties(userHeroBO, userOnlyOneHero);
			list.add(userOnlyOneHero);
		}
		return list;
	}

	@Override
	public void changePos(String userId, String h1, String h2, int pos1, int pos2) {
		UserOnlyOneHero hero1 = userOnlyOneHeroDao.getHero(userId, h1);
		UserOnlyOneHero hero2 = userOnlyOneHeroDao.getHero(userId, h2);
		if ((hero1 != null && hero1.getLife() <= 0) || (hero2 != null && hero2.getLife() <= 0)) {
			throw new ServiceException(DEAN_HERO, "死亡武将不能调整位置");
		}
		UserHero userHero1 = heroService.get(userId, h1);
		int heroId = heroService.getSysHero(userHero1.getSystemHeroId()).getHeroId();
		List<UserOnlyOneHero> l = userOnlyOneHeroDao.getPosHeros(userId);
		if (pos1 != 0 && pos2 == 0 && l.size() <= 1) {
			throw new ServiceException(ONE_HERO, "最后一个武将不能下阵");
		}
		for (UserOnlyOneHero userOnlyOneHero : l) {
			if (userOnlyOneHero.getHeroId() == heroId && userOnlyOneHero.getPos() != pos1 && userOnlyOneHero.getPos() != pos2) {
				throw new ServiceException(SIMPLE_HERO, "相同武将不能上阵");
			}
		}
		if (hero1 != null) {
			hero1.setPos(pos2);
			userOnlyOneHeroDao.updateHeroPos(userId, hero1);
		}
		if (hero2 != null) {
			hero2.setPos(pos1);
			userOnlyOneHeroDao.updateHeroPos(userId, hero2);
		}

	}
}
