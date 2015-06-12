package com.lodogame.ldsg.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.RobotDao;
import com.lodogame.game.dao.SystemHeroDao;
import com.lodogame.game.dao.SystemRobotHeroDao;
import com.lodogame.game.dao.SystemRobotRuleDao;
import com.lodogame.game.dao.SystemRobotUserDao;
import com.lodogame.game.dao.UserPkInfoDao;
import com.lodogame.game.utils.IDGenerator;
import com.lodogame.ldsg.bo.BattleHeroBO;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.config.Config;
import com.lodogame.ldsg.factory.NameFactory;
import com.lodogame.ldsg.helper.BOHelper;
import com.lodogame.ldsg.helper.HeroHelper;
import com.lodogame.ldsg.service.RobotService;
import com.lodogame.model.RobotUser;
import com.lodogame.model.RobotUserHero;
import com.lodogame.model.SystemHero;
import com.lodogame.model.SystemRobotHero;
import com.lodogame.model.SystemRobotRule;
import com.lodogame.model.SystemRobotUser;
import com.lodogame.model.UserPkInfo;

public class RobotServiceImpl implements RobotService {

	private static final Logger logger = Logger.getLogger(RobotServiceImpl.class);

	@Autowired
	private RobotDao robotDao;

	@Autowired
	private SystemRobotUserDao systemRobotUserDao;

	@Autowired
	private SystemRobotHeroDao systemRobotHeroDao;

	@Autowired
	private SystemRobotRuleDao systemRobotRuleDao;

	@Autowired
	private SystemHeroDao systemHeroDao;

	@Autowired
	private UserPkInfoDao userPkInfoDao;

	@Override
	public RobotUser getRobotUser(int minCapability, int maxCapability) {
		for (int i = 0; i < 5; i++) {
			List<RobotUser> robotUserList = robotDao.getByCapability(minCapability - 500 * i, maxCapability + 500 * i);
			if (!robotUserList.isEmpty()) {
				return robotUserList.get(RandomUtils.nextInt(robotUserList.size()));
			}
		}
		return null;
	}

	@Override
	public RobotUser get(String userId) {
		return robotDao.getUser(userId);
	}

	@Override
	public RobotUser getById(long lodoId) {
		return robotDao.getUser(lodoId);
	}

	@Override
	public RobotUser getByName(String username) {
		return robotDao.getByName(username);
	}

	@Override
	public List<UserHeroBO> getRobotUserHeroBOList(String userId) {

		RobotUser robotUser = this.robotDao.getUser(userId);

		List<UserHeroBO> list = new ArrayList<UserHeroBO>();
		List<RobotUserHero> l = this.robotDao.getRobotUserHero(userId);
		for (RobotUserHero robotUserHero : l) {
			UserHeroBO userHeroBO = new UserHeroBO();

			SystemHero systemHero = this.systemHeroDao.get(robotUserHero.getSystemHeroId());

			userHeroBO.setSystemHeroId(robotUserHero.getSystemHeroId());
			userHeroBO.setLife(robotUserHero.getLife());
			userHeroBO.setPhysicalAttack(robotUserHero.getAttack());
			userHeroBO.setPhysicalDefense(robotUserHero.getDefense());
			userHeroBO.setPlan(systemHero.getPlan());
			userHeroBO.setCareer(systemHero.getCareer());
			userHeroBO.setNormalPlan(systemHero.getNormalPlan());
			userHeroBO.setSkill1(systemHero.getSkill1());
			userHeroBO.setSkill2(systemHero.getSkill2());
			userHeroBO.setSkill3(systemHero.getSkill3());
			userHeroBO.setSkill4(systemHero.getSkill4());
			userHeroBO.setStar(systemHero.getHeroStar());
			userHeroBO.setPos(robotUserHero.getPos());
			userHeroBO.setHeroLevel(robotUser.getLevel());
			userHeroBO.setUserHeroId(robotUserHero.getUserHeroId());
			userHeroBO.setUserId(robotUserHero.getUserId());
			userHeroBO.setName(systemHero.getHeroName());

			list.add(userHeroBO);
		}
		return list;
	}

	@Override
	public List<BattleHeroBO> getRobotUserBattleHeroBOList(String userId) {

		List<UserHeroBO> list = this.getRobotUserHeroBOList(userId);
		List<BattleHeroBO> l = new ArrayList<BattleHeroBO>();
		for (UserHeroBO bo : list) {
			BattleHeroBO battleHeroBO = BOHelper.createBattleHeroBO(bo);
			l.add(battleHeroBO);
		}

		return l;
	}

	public void init() {

		if (!Config.ins().isGameServer()) {
			return;
		}

		logger.debug("开始初始化数机器人数据");

		createRobot();

	}

	public void createRobot() {

		int count = robotDao.count();

		boolean reCreate = false;

		if (count > 0) {
			return;
		}

		List<SystemRobotRule> systemRobotRuleList = systemRobotRuleDao.getList();

		int rank = 1;
		Date now = new Date();

		int lodoId = this.robotDao.getMaxLodoId();
		if (lodoId == 0) {
			lodoId = 1000000000;
		} else {
			lodoId += 1;
		}

		List<UserPkInfo> pkInfoList = new ArrayList<UserPkInfo>();
		List<RobotUser> robotUserList = new ArrayList<RobotUser>();
		List<RobotUserHero> heroList = new ArrayList<RobotUserHero>();

		for (SystemRobotRule systemRobotRule : systemRobotRuleList) {

			int num = systemRobotRule.getNum();
			int level = systemRobotRule.getLevel();
			SystemRobotUser systemRobotUser = systemRobotUserDao.get(level);

			Map<Integer, List<SystemRobotHero>> heroListMap = new HashMap<Integer, List<SystemRobotHero>>();
			if (systemRobotUser.getHero1() > 0) {
				heroListMap.put(1, systemRobotHeroDao.getList(systemRobotUser.getHero1()));
			}
			if (systemRobotUser.getHero2() > 0) {
				heroListMap.put(2, systemRobotHeroDao.getList(systemRobotUser.getHero2()));
			}
			if (systemRobotUser.getHero3() > 0) {
				heroListMap.put(3, systemRobotHeroDao.getList(systemRobotUser.getHero3()));
			}
			if (systemRobotUser.getHero4() > 0) {
				heroListMap.put(4, systemRobotHeroDao.getList(systemRobotUser.getHero4()));
			}
			if (systemRobotUser.getHero5() > 0) {
				heroListMap.put(5, systemRobotHeroDao.getList(systemRobotUser.getHero5()));
			}
			if (systemRobotUser.getHero6() > 0) {
				heroListMap.put(6, systemRobotHeroDao.getList(systemRobotUser.getHero6()));
			}

			for (int i = 0; i < num; i++) {

				String userId = IDGenerator.getID();

				List<RobotUserHero> robotUserHeroList = new ArrayList<RobotUserHero>();
				for (Entry<Integer, List<SystemRobotHero>> entry : heroListMap.entrySet()) {

					int pos = entry.getKey();
					List<SystemRobotHero> list = entry.getValue();
					if (list.isEmpty()) {
						continue;
					}
					SystemRobotHero systemRobotHero = list.get(RandomUtils.nextInt(list.size()));

					RobotUserHero robotUserHero = new RobotUserHero();
					robotUserHero.setAttack(systemRobotHero.getAttackRatio() * level + RandomUtils.nextInt(10));
					robotUserHero.setDefense(systemRobotHero.getDefenseRatio() * level + +RandomUtils.nextInt(10));
					robotUserHero.setLife(systemRobotHero.getLifeRatio() * level + +RandomUtils.nextInt(10));
					robotUserHero.setPos(pos);
					robotUserHero.setSystemHeroId(systemRobotHero.getSystemHeroId());
					robotUserHero.setUserId(userId);
					robotUserHero.setUserHeroId(IDGenerator.getID());

					robotUserHeroList.add(robotUserHero);

				}

				int capability = HeroHelper.getCapabilityByRobotUserHeroList(robotUserHeroList);

				int imgId = 999901;

				String username = NameFactory.getInstance().getName();

				RobotUser robotUser = new RobotUser();
				robotUser.setCapability(capability);
				robotUser.setImgId(imgId);
				robotUser.setUsername(username);
				robotUser.setLevel(level);
				robotUser.setUserId(userId);
				robotUser.setLodoId(lodoId);

				lodoId += 1;

				robotUserList.add(robotUser);
				heroList.addAll(robotUserHeroList);

				if (systemRobotRule.getRankUpper() != -1 && reCreate == false) {

					UserPkInfo userPkInfo = new UserPkInfo();
					userPkInfo.setImgId(imgId);
					userPkInfo.setLastBuyTime(now);
					userPkInfo.setLevel(level);
					userPkInfo.setLodoId(robotUser.getLodoId());
					userPkInfo.setPkTimes(0);
					userPkInfo.setRank(rank);
					userPkInfo.setUpdatePkTime(now);
					userPkInfo.setUserId(userId);
					userPkInfo.setUsername(username);

					rank += 1;

					// this.userPkInfoDao.add(userPkInfo);
					pkInfoList.add(userPkInfo);

				}

				if (robotUserList.size() > 300) {
					robotDao.insertUser(robotUserList);
					robotUserList.clear();
				}

				if (heroList.size() > 300) {
					robotDao.insert(heroList);
					heroList.clear();
				}

				if (pkInfoList.size() > 300) {
					this.userPkInfoDao.add(pkInfoList);
					pkInfoList.clear();
				}

			}

		}

		if (robotUserList.size() > 0) {
			robotDao.insertUser(robotUserList);
		}

		if (heroList.size() > 0) {
			robotDao.insert(heroList);
		}

		if (pkInfoList.size() > 0) {
			this.userPkInfoDao.add(pkInfoList);
		}

	}
}
