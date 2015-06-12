package com.lodogame.ldsg.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.ExDao;
import com.lodogame.game.dao.SystemHeroDao;
import com.lodogame.game.dao.SystemVipLevelDao;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.game.utils.ScheduledUtils;
import com.lodogame.ldsg.bo.BattleBO;
import com.lodogame.ldsg.bo.BattleHeroBO;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.DropToolBO;
import com.lodogame.ldsg.bo.ExpeditionShowBO;
import com.lodogame.ldsg.bo.ExpeditionStepBO;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.constants.BattleType;
import com.lodogame.ldsg.constants.ToolUseType;
import com.lodogame.ldsg.constants.UserPowerGetType;
import com.lodogame.ldsg.event.BattleResponseEvent;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.helper.BOHelper;
import com.lodogame.ldsg.helper.DropToolHelper;
import com.lodogame.ldsg.helper.HeroHelper;
import com.lodogame.ldsg.service.BattleService;
import com.lodogame.ldsg.service.DailyTaskService;
import com.lodogame.ldsg.service.ExpeditionService;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.RobotService;
import com.lodogame.ldsg.service.ToolService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.ExpeditionConfig;
import com.lodogame.model.ExpeditionNum;
import com.lodogame.model.IUser;
import com.lodogame.model.RobotUser;
import com.lodogame.model.SystemDailyTask;
import com.lodogame.model.SystemHero;
import com.lodogame.model.SystemVipLevel;
import com.lodogame.model.User;
import com.lodogame.model.UserExInfo;
import com.lodogame.model.UserExpeditionHero;
import com.lodogame.model.UserExpeditionVsTable;
import com.lodogame.model.UserPowerInfo;

public class ExpeditionServiceImpl implements ExpeditionService {

	/**
	 * '球队状态 0:开启 1:胜利 2:失败', '宝箱状态 2:get 1:open 0:close'
	 */

	public static final int BOX_OPEN = 2;
	public static final int BOX_CLOSE = 1;

	public static final int TEAM_OPEN = 0;
	public static final int TEAM_WIN = 1;
	public static final int TEAM_LOSS = 2;

	public static final int NEED_HERO_LEVEL = 20;

	@Autowired
	private DailyTaskService dailyTaskService;

	@Autowired
	private SystemHeroDao systemHeroDao;

	@Autowired
	private ToolService toolService;

	@Autowired
	private ExDao exDao;

	@Autowired
	private UserService userService;

	@Autowired
	private HeroService heroService;

	@Autowired
	private BattleService battleService;

	@Autowired
	private RobotService robotService;

	@Autowired
	private SystemVipLevelDao systemVipLevelDao;

	private static Logger logger = Logger.getLogger(ExpeditionService.class);

	public void init() {
		ScheduledUtils.schelduleAtFixed(new CheckClearNum(), TimeUnit.MINUTES, 1);
	}

	class CheckClearNum implements Runnable {
		public void run() {
			if (DateUtils.isThisTime(0, 2)) {
				exDao.clearNum();
				logger.error("clear over");
			}
		}
	}

	public void checkNewHero(UserExInfo info) {
		List<UserExpeditionHero> oldHero = getHeros(info, -1L);
		List<UserHeroBO> newHero = heroService.getUserHeroList(info.getUserId(), 0, NEED_HERO_LEVEL);
		if (oldHero.size() == newHero.size())
			return;
		Map<String, UserHeroBO> now = new HashMap<String, UserHeroBO>();
		for (UserHeroBO temp : newHero) {
			now.put(temp.getUserHeroId(), temp);
		}
		List<String> news = getOverLoad(oldHero, newHero);
		if (news.size() == 0)
			return;
		List<UserExpeditionHero> over = new ArrayList<UserExpeditionHero>();
		for (String temp : news) {
			UserHeroBO bo = now.get(temp);
			UserExpeditionHero hh = new UserExpeditionHero();
			BeanUtils.copyProperties(bo, hh);
			hh.setMaxLife(bo.getLife());
			hh.setLife(bo.getLife());
			hh.setAttack(bo.getPhysicalAttack());
			hh.setDefense(bo.getPhysicalDefense());
			hh.setExId(-1L);
			hh.setPos(0);
			over.add(hh);
		}
		exDao.insertHero(info.getUserId(), over);
		info.addNewMyHero(over);
	}

	public List<String> getOverLoad(List<UserExpeditionHero> oldHero, List<UserHeroBO> newHero) {
		List<String> list = new ArrayList<String>();
		Set<String> sets = new HashSet<String>();

		for (UserExpeditionHero temp : oldHero)
			sets.add(temp.getUserHeroId());

		for (UserHeroBO temp : newHero)
			if (sets.add(temp.getUserHeroId()))
				list.add(temp.getUserHeroId());

		return list;
	}

	public ExpeditionShowBO show(String userId) {
		UserExInfo info = exDao.getExInfo(userId);
		if (info.noHaveVs()) {
			// 没有第一个对手
			List<UserExpeditionHero> my = getMyHero(userId);
			UserExpeditionVsTable newt = getNextVsTable(info);
			List<UserExpeditionHero> heros = getNextHero(newt);
			exDao.insertHero(newt.getUserId(), heros);
			exDao.insertVs(newt);
			info.addNewVs(newt, heros);
			info.addMyHero(my);
		}
		checkNewHero(info);
		ExpeditionNum num = exDao.getNum(userId);
		List<UserExpeditionHero> hero = getHeros(info, info.getLastVsTable().getExId());
		int rand = RandomUtils.nextInt(hero.size());
		int shId = hero.get(rand).getSystemHeroId();
		SystemHero sy = systemHeroDao.get(shId);
		ExpeditionShowBO bo = BOHelper.getExShowBo(info, num, createBo(getHeros(info, -1L), true), sy.getModelId());
		return bo;
	}

	public UserExpeditionVsTable getNextVsTable(UserExInfo info) {
		long nextExId = exDao.getExId();
		UserExpeditionVsTable last = info.getLastVsTable();
		int nextIndex = 1;
		if (last != null) {
			nextIndex = last.getIndex() + 1;
		}
		UserExpeditionVsTable newVs = new UserExpeditionVsTable(info.getUserId(), nextIndex, nextExId, TEAM_OPEN, BOX_CLOSE);

		return newVs;
	}

	public List<UserExpeditionHero> getMyHero(String userId) {
		List<UserHeroBO> userHero = heroService.getUserHeroList(userId, 0, NEED_HERO_LEVEL);
		if (userHero.size() == 0) {
			throw new ServiceException(NO_HERO, "沒有20級英雄");
		}
		List<UserExpeditionHero> result = new ArrayList<UserExpeditionHero>();
		for (UserHeroBO temp : userHero) {
			UserExpeditionHero hh = new UserExpeditionHero();
			BeanUtils.copyProperties(temp, hh);
			hh.setMaxLife(temp.getLife());
			hh.setLife(temp.getLife());
			hh.setAttack(temp.getPhysicalAttack());
			hh.setDefense(temp.getPhysicalDefense());
			hh.setExId(-1L);
			result.add(hh);
		}
		exDao.insertHero(userId, result);
		return result;
	}

	public List<UserExpeditionHero> getNextHero(UserExpeditionVsTable vsTable) {

		int maxPower = userService.getUserMaxPower(vsTable.getUserId());
		int att = maxPower;
		List<UserHeroBO> list = heroService.getUserHeroList(vsTable.getUserId(), 1);
		int attB = HeroHelper.getCapability(list);
		if (att < attB) {
			att = attB;
		}
		List<UserExpeditionHero> result = null;
		ExpeditionConfig config = exDao.getLevelConfig(vsTable.getIndex());

		int min = att + (int) (att * config.getMin());
		int max = att + (int) (att * config.getMax());
		UserPowerInfo vsu = userService.getRandAttUser(vsTable.getUserId(), UserPowerGetType.EXPEDITION, min, max); // 战斗力等级区间符合的用户
		if (vsu != null) {
			List<UserHeroBO> heros = heroService.getUserHeroList(vsu.getUserId(), 1);
			if (heros.size() != 0)
				result = getExHeroList(vsTable, heros);
		}
		if (result == null) { // 没有符合的用户
			RobotUser rot = robotService.getRobotUser(min, max);
			if (rot != null) {
				List<UserHeroBO> heros = robotService.getRobotUserHeroBOList(rot.getUserId());
				if (heros.size() > 0) {
					result = getExHeroList(vsTable, heros);
					return result;
				}
			}
		}

		int in = 0;
		while (result == null && in < 100) {
			UserPowerInfo vs = new UserPowerInfo();
			UserPowerInfo info = userService.getRandUser(vsTable.getUserId(), min);

			int sub = 400 + 400 * (in / 10);

			if (info == null) {
				info = userService.getRandUser(vsTable.getUserId(), min - sub * in);
			}

			in++;

			if (info == null) {
				continue;
			}

			vs.setUserId(info.getUserId());//
			List<UserHeroBO> heros = heroService.getUserHeroList(vs.getUserId(), 1);
			if (heros.size() == 0)
				continue;
			result = getExHeroList(vsTable, heros);

		}
		return result;
	}

	private List<UserExpeditionHero> getExHeroList(UserExpeditionVsTable vs, List<UserHeroBO> list) {
		List<UserExpeditionHero> result = new ArrayList<UserExpeditionHero>();
		for (UserHeroBO temp : list) {
			UserExpeditionHero hh = new UserExpeditionHero();
			BeanUtils.copyProperties(temp, hh);
			hh.setMaxLife(temp.getLife());
			hh.setLife(temp.getLife());
			hh.setAttack(temp.getPhysicalAttack());
			hh.setDefense(temp.getPhysicalDefense());
			hh.setExId(vs.getExId());
			result.add(hh);
		}
		return result;
	}

	public void fight(final String userId, final long exId, final EventHandle eventHandle) {
		final UserExInfo info = exDao.getExInfo(userId);
		final List<UserExpeditionHero> heros = getHeros(info, exId);
		final List<UserExpeditionHero> myHeros = info.getMyHeroInPk();
		List<BattleHeroBO> att = heroService.getUserBattleHeroBOList(myHeros, true);
		List<BattleHeroBO> def = heroService.getUserBattleHeroBOList(heros, false);
		IUser a = userService.get(userId);
		IUser d = null;
		try {
			d = userService.get(heros.get(0).getUserId());
		} catch (Exception e) {
			d = robotService.get(heros.get(0).getUserId());
		}
		final String username = d.getUsername();

		BattleBO attack = new BattleBO();
		attack.setUserLevel(a.getLevel());
		attack.setBattleHeroBOList(att);

		BattleBO defense = new BattleBO();
		defense.setUserLevel(d.getLevel());
		defense.setBattleHeroBOList(def);

		battleService.fight(attack, defense, BattleType.EXPE, new EventHandle() {
			@SuppressWarnings("unchecked")
			@Override
			public boolean handle(Event event) {
				if (event instanceof BattleResponseEvent) {
					int flag = event.getInt("flag");
					Map<String, Integer> life = (Map<String, Integer>) event.getObject("life");
					if (life == null) {
						life = new HashMap<String, Integer>();
					}
					// 保存体力
					for (UserExpeditionHero temp : myHeros) {
						Integer power = life.get("L_a" + temp.getPos());
						Integer rage = life.get("M_a" + temp.getPos());
						if (power == null) {
							power = 0;
						}
						if (flag == 2) { // 平局都死
							temp.setLife(0);
						} else {

							if (power > 0) {
								int maxLife = temp.getMaxLife();
								power = power + (int) (maxLife * 0.15);
								if (power > maxLife) {
									power = maxLife;
								}
							}

							temp.setLife(power);
							temp.setRage(rage == null ? 0 : rage);
						}
						if (temp.getLife() <= 0) {
							temp.setPos(0);
						}
					}
					for (UserExpeditionHero temp : heros) {
						Integer power = life.get("L_d" + temp.getPos());
						Integer rage = life.get("M_d" + temp.getPos());
						if (power == null)
							power = 0;
						if (flag == 2) {// 平局都死
							temp.setLife(0);
						} else {
							temp.setLife(power);
							temp.setRage(rage == null ? 0 : rage);
						}
						if (temp.getLife() <= 0)
							temp.setPos(0);
					}

					exDao.updateHero(userId, myHeros);
					exDao.updateHero(userId, heros);

					event.setObject("myHero", myHeros);
					event.setObject("vsHero", heros);
					event.setObject("dun", username);
					if (flag == 1 || flag == 2) {// 平局算胜利
						// 修改宝箱状态
						UserExpeditionVsTable vs = info.getVsTable(exId);
						vs.setBoxStat(BOX_OPEN);
						vs.setStat(TEAM_WIN);
						exDao.updateVs(vs);
					}
					dailyTaskService.sendUpdateDailyTaskEvent(userId, SystemDailyTask.YUANZHENG, 1);
					eventHandle.handle(event);
				}
				return true;
			}
		});
	}

	public List<UserHeroBO> createBo(List<UserExpeditionHero> heros, boolean self) {
		List<UserHeroBO> bo = new ArrayList<UserHeroBO>();
		for (UserExpeditionHero temp : heros) {
			bo.add(heroService.createUserHeroBO(temp, self));
		}
		return bo;
	}

	private List<UserExpeditionHero> getHeros(UserExInfo info, long exId) {
		List<UserExpeditionHero> list = info.getHero(exId);
		if (list == null || list.size() <= 0) { // 没有重新插入,临时
			UserExpeditionVsTable table = info.getVsTable(exId);
			if (exId != -1L & table != null) {
				list = getNextHero(table);
				exDao.insertHero(info.getUserId(), list);
				info.addTempHero(exId, list);
				logger.error("null of expeditionHero service insert [" + info.getUserId() + " " + table.getIndex() + "];");
			}
		}
		return list;
	}

	public List<UserHeroBO> showHero(String userId, long exId) {
		UserExInfo info = exDao.getExInfo(userId);

		List<UserExpeditionHero> heros = getHeros(info, exId);

		if (heros == null) {
			return new ArrayList<UserHeroBO>();
		}

		Iterator<UserExpeditionHero> hero = heros.iterator();
		while (hero.hasNext()) {
			UserExpeditionHero ue = hero.next();
			if (ue.getPos() == 0) {
				hero.remove();
			}
		}
		return createBo(heros, false);
	}

	@Override
	public Map<Integer, Object> getGift(String userId, long exId) {
		Map<Integer, Object> map = new HashMap<Integer, Object>();
		UserExInfo info = exDao.getExInfo(userId);
		UserExpeditionVsTable vs = info.getVsTable(exId);
		CommonDropBO bo = new CommonDropBO();
		ExpeditionConfig config = exDao.getLevelConfig(vs.getIndex());
		List<DropToolBO> dropToolBOList = DropToolHelper.parseDropTool(config.getGift());

		if (vs.getBoxStat() == BOX_OPEN) {
			vs.setBoxStat(BOX_CLOSE);
			exDao.updateVs(vs);
			for (DropToolBO dropToolBO : dropToolBOList) {
				int toolType = dropToolBO.getToolType();
				int toolId = dropToolBO.getToolId();
				int toolNum = dropToolBO.getToolNum();

				List<DropToolBO> dropBOList = toolService.giveTools(userId, toolType, toolId, toolNum, ToolUseType.ADD_EXPEDITION);

				for (DropToolBO dropBO : dropBOList) {
					this.toolService.appendToDropBO(userId, bo, dropBO);
				}
			}
			if (vs.getIndex() < 15) {
				UserExpeditionVsTable newt = getNextVsTable(info);
				List<UserExpeditionHero> heros = getNextHero(newt);
				exDao.insertHero(newt.getUserId(), heros);
				exDao.insertVs(newt);
				info.addNewVs(newt, heros);
				ExpeditionStepBO bos = BOHelper.getExStepBo(newt);

				map.put(1, bos);
			} else {
				UserExpeditionVsTable newt = info.getLastVsTable();
				ExpeditionStepBO bos = BOHelper.getExStepBo(newt);

				map.put(1, bos);
			}
		} else {
			throw new ServiceException(BOX_NO_OPEN, "宝箱没有开启");
		}
		map.put(2, bo);
		return map;
	}

	@Override
	public void replace(String userId) {
		User user = userService.get(userId);
		SystemVipLevel vip = systemVipLevelDao.get(user.getVipLevel());
		int numCount = vip.getExpeditionResetTimes();

		UserExInfo info = exDao.getExInfo(userId);
		ExpeditionNum num = exDao.getNum(userId);
		if (num == null || num.getNum() < numCount) {
			if (num.getNum() >= 1) {
				if (!userService.reduceGold(userId, 30, ToolUseType.REDUCE_EXPEDITION)) {
					throw new ServiceException(NO_YB, "没钱");
				}
			}
			Set<Long> exIds = info.getAllExId();
			StringBuffer logs = new StringBuffer("replace expedition ex_id[");
			for (long temp : exIds) {
				logs.append(temp).append(",");
			}
			logs.append("];");
			logger.error(logs.toString());
			exDao.deleteHeroAll(userId, exIds);
			exDao.deleteVsAll(userId);
			exDao.clearCache(userId);
			num.setNum(num.getNum() + 1);
			exDao.updateNum(num);
		} else {
			throw new ServiceException(NO_REPLACE_NUM, "已经达到最大次数");
		}
	}

	@Override
	public void changePos(String userId, String hId1, String hId2, int pos1, int pos2) {
		UserExInfo info = exDao.getExInfo(userId);
		UserExpeditionHero hero1 = info.getMyHeroById(hId1);
		UserExpeditionHero hero2 = info.getMyHeroById(hId2);
		if ((hero1 != null && hero1.getLife() == 0) || (hero2 != null && hero2.getLife() == 0)) {
			throw new ServiceException(NO_LIFE, "死亡武将不能调整位置");
		}
		List<UserExpeditionHero> heros = new ArrayList<UserExpeditionHero>();

		if (hero1 != null) {
			hero1.setPos(pos1);
			heros.add(hero1);
		}
		if (hero2 != null) {
			hero2.setPos(pos2);
			heros.add(hero2);
		}
		exDao.updateHero(userId, heros);
	}
}