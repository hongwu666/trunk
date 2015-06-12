package com.lodogame.ldsg.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.RankDao;
import com.lodogame.game.dao.SystemActivityDao;
import com.lodogame.game.dao.SystemForcesDao;
import com.lodogame.game.dao.SystemHeroDao;
import com.lodogame.game.dao.UserDailyGainLogDao;
import com.lodogame.game.dao.UserDao;
import com.lodogame.game.dao.UserPkInfoDao;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.ldsg.bo.HeroPowerRankBO;
import com.lodogame.ldsg.bo.RankBO;
import com.lodogame.ldsg.bo.UserLevelRankBO;
import com.lodogame.ldsg.bo.UserPowerRankBO;
import com.lodogame.ldsg.config.Config;
import com.lodogame.ldsg.constants.RankType;
import com.lodogame.ldsg.helper.LodoIDHelper;
import com.lodogame.ldsg.helper.PkHelper;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.PkService;
import com.lodogame.ldsg.service.RankService;
import com.lodogame.ldsg.service.RobotService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.OnlyoneWeekRank;
import com.lodogame.model.PayRankInfo;
import com.lodogame.model.ProcessRankInfo;
import com.lodogame.model.RobotUser;
import com.lodogame.model.StarRankInfo;
import com.lodogame.model.SystemActivity;
import com.lodogame.model.SystemForces;
import com.lodogame.model.SystemHero;
import com.lodogame.model.User;
import com.lodogame.model.UserDrawLog;
import com.lodogame.model.UserHeroInfo;
import com.lodogame.model.UserLevelRank;
import com.lodogame.model.UserPkInfo;
import com.lodogame.model.UserPowerInfo;
import com.lodogame.model.UserPowerRank;
import com.lodogame.model.VipRankInfo;

public class RankServiceImpl implements RankService {

	private static final Logger logger = Logger.getLogger(RankServiceImpl.class);

	private List<UserPowerRankBO> userPowerRankBOList = new ArrayList<UserPowerRankBO>();

	private List<HeroPowerRankBO> heroPowerRankBOList = new ArrayList<HeroPowerRankBO>();

	private List<UserLevelRankBO> userLevelRankBOList = new ArrayList<UserLevelRankBO>();
	// 剧情副本榜
	private List<RankBO> storyRankBOList = new ArrayList<RankBO>();
	// 精英副本榜
	private List<RankBO> eliteRankBOList = new ArrayList<RankBO>();
	// 副本星数榜
	private List<RankBO> ectypestarRankBOList = new ArrayList<RankBO>();
	// 资源星数榜
	private List<RankBO> resourcestarRankBOList = new ArrayList<RankBO>();
	// VIP等级榜
	private List<RankBO> vipRankBOList = new ArrayList<RankBO>();
	// 周充值榜
	private List<RankBO> payRankBOList = new ArrayList<RankBO>();
	// 竞技榜
	private List<RankBO> pkRankBOList = new ArrayList<RankBO>();
	// 千人斬周榜
	private List<RankBO> onlyoneRankBOList = new ArrayList<RankBO>();
	// 竞技榜
	private List<RankBO> drawRankBOList = new ArrayList<RankBO>();

	private Map<String, List> map = new HashMap<String, List>();
	@Autowired
	private SystemForcesDao systemForcesDao;

	@Autowired
	private RankDao rankDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private SystemActivityDao systemActivityDao;

	@Autowired
	private UserPkInfoDao userPkInfoDao;

	@Autowired
	private HeroService heroService;

	@Autowired
	private RobotService robotService;

	@Autowired
	private UserService userService;

	@Autowired
	private PkService pkService;

	@Autowired
	private SystemHeroDao systemHeroDao;

	@Autowired
	private UserDailyGainLogDao userDailyGainLogDao;

	private Date cacheTime = new Date();

	private Date date = new Date();

	public List<RankBO> getDrawRankList() {
		if (!this.drawRankBOList.isEmpty()) {
			return drawRankBOList;
		}
		if (map.containsKey("draw")) {
			return map.get("draw");
		}
		int rank = 1;
		SystemActivity activity = systemActivityDao.get(33);
		if (activity == null) {
			return null;
		}
		Date date = activity.getStartTime();
		List<UserDrawLog> list = this.rankDao.getDrawRank(200, date);
		for (UserDrawLog userDrawLog : list) {
			User user = userService.get(userDrawLog.getUserId());
			RankBO bo = new RankBO(userDrawLog.getUserId(), user.getUsername(), rank++, user.getVipLevel(), userDrawLog.getPoint() + "");
			drawRankBOList.add(bo);
		}
		return drawRankBOList;
	}

	public List<RankBO> getStoryRankList() {
		if (!this.storyRankBOList.isEmpty()) {
			return storyRankBOList;
		}
		int rank = 1;
		List<ProcessRankInfo> list = this.rankDao.getStoryRank(200);
		for (ProcessRankInfo processRankInfo : list) {
			StringBuffer vaule = new StringBuffer("");
			String[] str = String.valueOf(processRankInfo.getProce()).split("[.]");
			String chapter = str[0];
			List<SystemForces> listfForces = systemForcesDao.getSystemForcesByGroupId((Integer.valueOf(str[1])));
			String section = listfForces.get(listfForces.size() - 1).getForcesName();
			vaule.append("第").append(chapter).append("章-").append(section);
			User user = userService.get(processRankInfo.getUserId());
			RankBO bo = new RankBO(processRankInfo.getUserId(), user.getUsername(), rank++, user.getVipLevel(), vaule.toString());
			storyRankBOList.add(bo);
		}
		return storyRankBOList;
	}

	public List<RankBO> getEliteRankList() {
		if (!this.eliteRankBOList.isEmpty()) {
			return eliteRankBOList;
		}
		int rank = 1;
		List<ProcessRankInfo> list = this.rankDao.getEliteRank(200);
		for (ProcessRankInfo processRankInfo : list) {
			StringBuffer vaule = new StringBuffer("");
			String str[] = String.valueOf(processRankInfo.getProce()).split("[.]");
			String chapter = Integer.valueOf(str[0].substring(3, str[0].length())).toString();
			List<SystemForces> listfForces = systemForcesDao.getSystemForcesByGroupId((Integer.valueOf(str[1])));
			String section = listfForces.get(listfForces.size() - 1).getForcesName();
			vaule.append("第").append(chapter).append("章-").append(section);
			User user = userService.get(processRankInfo.getUserId());
			RankBO bo = new RankBO(processRankInfo.getUserId(), user.getUsername(), rank++, user.getVipLevel(), vaule.toString());
			eliteRankBOList.add(bo);
		}
		return eliteRankBOList;
	}

	public List<RankBO> getResourceStarRankList() {
		if (!this.resourcestarRankBOList.isEmpty()) {
			return resourcestarRankBOList;
		}

		int rank = 1;
		List<StarRankInfo> list = this.rankDao.getResourceStarRank(200);
		for (StarRankInfo starRankInfo : list) {
			User user = userService.get(starRankInfo.getUserId());
			RankBO bo = new RankBO(starRankInfo.getUserId(), user.getUsername(), rank++, user.getVipLevel(), String.valueOf(starRankInfo.getStar()));
			resourcestarRankBOList.add(bo);
		}

		return resourcestarRankBOList;
	}

	public List<RankBO> getEctypeStarRankList() {
		if (!this.ectypestarRankBOList.isEmpty()) {
			return ectypestarRankBOList;
		}

		int rank = 1;
		List<StarRankInfo> list = this.rankDao.getEctypeStarRank(200);
		for (StarRankInfo starRankInfo : list) {
			User user = userService.get(starRankInfo.getUserId());
			RankBO bo = new RankBO(starRankInfo.getUserId(), user.getUsername(), rank++, user.getVipLevel(), String.valueOf(starRankInfo.getStar()));
			ectypestarRankBOList.add(bo);
		}

		return ectypestarRankBOList;
	}

	public List<RankBO> getVipRankList() {
		if (!this.vipRankBOList.isEmpty()) {
			return vipRankBOList;
		}
		int rank = 1;
		List<VipRankInfo> list = this.rankDao.getVipRank(10);
		for (VipRankInfo vipRankInfo : list) {
			RankBO bo = new RankBO(vipRankInfo.getUserId(), vipRankInfo.getUsername(), rank++, vipRankInfo.getVipLevel(), userDao.getUserPower(vipRankInfo.getUserId()) + "");
			vipRankBOList.add(bo);
		}
		return vipRankBOList;
	}

	public List<RankBO> getPayRankList() {
		if (!this.payRankBOList.isEmpty()) {
			return payRankBOList;
		}

		int rank = 1;
		List<PayRankInfo> list = this.rankDao.getPayRank(10);
		for (PayRankInfo payRankInfo : list) {
			User user = userService.get(payRankInfo.getUserId());
			RankBO bo = new RankBO(payRankInfo.getUserId(), user.getUsername(), rank++, user.getVipLevel(), userDao.getUserPower(user.getUserId()) + "");
			payRankBOList.add(bo);
		}

		return payRankBOList;
	}

	public List<RankBO> getOnlyoneRankList() {
		if (!this.onlyoneRankBOList.isEmpty()) {
			return onlyoneRankBOList;
		}

		int rank = 1;
		List<OnlyoneWeekRank> list = this.rankDao.getOnlyoneRank(200);
		for (OnlyoneWeekRank onlyoneWeekRank : list) {
			User user = userService.get(onlyoneWeekRank.getUserId());
			RankBO bo = new RankBO(onlyoneWeekRank.getUserId(), user.getUsername(), rank++, user.getVipLevel(), String.valueOf((int) onlyoneWeekRank.getPoint()));
			onlyoneRankBOList.add(bo);
		}

		return onlyoneRankBOList;
	}

	@Override
	public List<UserPowerRankBO> getUserPowerRankList() {

		if (!this.userPowerRankBOList.isEmpty()) {
			return userPowerRankBOList;
		}

		int rank = 1;
		List<UserPowerInfo> list = this.rankDao.getUserPowerInfo(200);
		for (UserPowerInfo userPowerInfo : list) {
			User user = userService.get(userPowerInfo.getUserId());
			UserPowerRank userPowerRank = new UserPowerRank();
			userPowerRank.setUsername(user.getUsername());
			userPowerRank.setVipLevel(user.getVipLevel());
			userPowerRank.setPower(userPowerInfo.getPowers());
			userPowerRank.setUserId(userPowerInfo.getUserId());
			UserPowerRankBO bo = new UserPowerRankBO(userPowerRank);
			bo.setRank(rank++);
			userPowerRankBOList.add(bo);
		}

		return userPowerRankBOList;
	}

	@Override
	public List<UserLevelRankBO> getUserLevelRankList() {

		if (!this.userLevelRankBOList.isEmpty()) {
			return userLevelRankBOList;
		}

		int rank = 1;
		List<UserLevelRank> list = this.rankDao.getUserLevelRank(200);
		for (UserLevelRank userLevelRank : list) {
			User user = this.userService.get(userLevelRank.getUserId());
			if (user == null) {
				continue;
			}
			userLevelRank.setVipLevel(user.getVipLevel());
			userLevelRank.setUsername(user.getUsername());
			UserLevelRankBO bo = new UserLevelRankBO(userLevelRank);
			bo.setRank(rank++);
			userLevelRankBOList.add(bo);
		}

		return userLevelRankBOList;
	}

	public List<RankBO> getPkRankList() {
		if (!this.pkRankBOList.isEmpty()) {
			return pkRankBOList;
		}
		int rank = 1;
		for (int r : PkHelper.tenList) {
			UserPkInfo userPkInfo = this.userPkInfoDao.getByRank(r);
			if (userPkInfo == null) {
				continue;
			}

			int cap = 0;
			int vipLevel = 0;
			String username = userPkInfo.getUsername();

			if (LodoIDHelper.isRobotLodoId(userPkInfo.getLodoId())) {
				RobotUser robotUser = this.robotService.get(userPkInfo.getUserId());
				if (robotUser != null) {
					cap = robotUser.getCapability();
				}

			} else {
				User user = this.userService.get(userPkInfo.getUserId());
				cap = userDao.getUserPower(user.getUserId());
				vipLevel = user.getVipLevel();
			}

			RankBO bo = new RankBO(userPkInfo.getUserId(), username, rank++, vipLevel, cap + "");

			pkRankBOList.add(bo);
		}
		return pkRankBOList;
	}

	@Override
	public List<HeroPowerRankBO> getHeroPowerRankList() {

		if (!this.heroPowerRankBOList.isEmpty()) {
			return heroPowerRankBOList;
		}

		List<UserHeroInfo> list = this.rankDao.getUserHeroInfo(200);
		int rank = 1;
		for (UserHeroInfo userHeroInfo : list) {

			HeroPowerRankBO bo = new HeroPowerRankBO(userHeroInfo);
			bo.setRank(rank++);
			SystemHero systemHero = systemHeroDao.get(userHeroInfo.getSystemHeroId());
			if (systemHero == null) {
				logger.error("系统武将不存在.systemHeroId[" + userHeroInfo.getSystemHeroId() + "]");
				continue;
			}

			User user = this.userService.get(userHeroInfo.getUserId());
			if (user != null) {
				bo.setVipLevel(user.getVipLevel());
			}

			heroPowerRankBOList.add(bo);
		}
		return heroPowerRankBOList;
	}

	public void clearCache() {
		this.userLevelRankBOList.clear();
		this.userPowerRankBOList.clear();
		this.heroPowerRankBOList.clear();
		this.storyRankBOList.clear();
		this.ectypestarRankBOList.clear();
		this.eliteRankBOList.clear();
		this.resourcestarRankBOList.clear();
		this.vipRankBOList.clear();
		this.payRankBOList.clear();
		this.pkRankBOList.clear();
		this.drawRankBOList.clear();
		this.onlyoneRankBOList.clear();
	}

	private void work() {
		while (true) {

			try {

				Date now = new Date();
				// 每10分钟刷新
				if (cacheTime == null || DateUtils.add(cacheTime, Calendar.MINUTE, 10).before(now)) {

					clearCache();
					getHeroPowerRankList();
					getUserLevelRankList();
					getUserPowerRankList();
					getEctypeStarRankList();
					getEliteRankList();
					getPayRankList();
					getResourceStarRankList();
					getPkRankList();
					getStoryRankList();
					getVipRankList();
					getDrawRankList();
					getOnlyoneRankList();
					cacheTime = now;

				}

				// 每天清空资源星数榜
				if (date == null || !DateUtils.isSameDay(date, DateUtils.getDateAtMidnight())) {

					// 每周清空周充榜
					if (DateUtils.getDayOfWeek() == 1) {
						rankDao.cleanPayRank();
					}

					rankDao.cleanResourceStarRank();
					date = DateUtils.getDateAtMidnight();
				}

			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}

			try {
				Thread.sleep(1000 * 10);
			} catch (InterruptedException ie) {
				logger.error(ie.getMessage(), ie);
			}

		}

	}

	public void init() {

		if (!Config.ins().isGameServer()) {
			return;
		}

		new Thread(new Runnable() {

			public void run() {
				work();
			}

		}).start();

	}

	@Override
	public List<RankBO> getRankList(int type) {
		List<RankBO> list = new ArrayList<RankBO>();
		switch (RankType.getRankTypeByValue(type)) {
		case STORY:
			list = getStoryRankList();
			break;
		case ELITE:
			list = getEliteRankList();
			break;
		case ECTYPE_STAR:
			list = getEctypeStarRankList();
			break;
		case RESOURCE_STAR:
			list = getResourceStarRankList();
			break;
		case VIP:
			list = getVipRankList();
			break;
		case PAY_WEEK:
			list = getPayRankList();
			break;
		case ATHLETIC:
			list = getPkRankList();
			break;
		case ONLYONE:
			list = getOnlyoneRankList();
			break;
		case DRAW:
			list = getDrawRankList();
			break;
		default:
			break;
		}
		return list;
	}
}
