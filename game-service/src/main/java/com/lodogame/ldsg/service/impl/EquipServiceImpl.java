package com.lodogame.ldsg.service.impl;

import java.util.ArrayList;
import java.util.Collections;
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

import com.lodogame.game.dao.EnchantPropertyDao;
import com.lodogame.game.dao.EquipEnchantDao;
import com.lodogame.game.dao.EquipRefineDao;
import com.lodogame.game.dao.EquipRefineSoulDao;
import com.lodogame.game.dao.SkillDataDao;
import com.lodogame.game.dao.SystemEquipAttrDao;
import com.lodogame.game.dao.SystemEquipDao;
import com.lodogame.game.dao.SystemEquipRefineDao;
import com.lodogame.game.dao.SystemEquipSellToolDao;
import com.lodogame.game.dao.SystemEquipUpgradeDao;
import com.lodogame.game.dao.SystemEquipUpgradeToolDao;
import com.lodogame.game.dao.SystemRefineSoulDataDao;
import com.lodogame.game.dao.SystemRefineSoulMapDao;
import com.lodogame.game.dao.SystemStoneDao;
import com.lodogame.game.dao.SystemToolDao;
import com.lodogame.game.dao.UserEquipDao;
import com.lodogame.game.dao.UserEquipStoneDao;
import com.lodogame.game.dao.UserHeroDao;
import com.lodogame.game.dao.UserToolDao;
import com.lodogame.game.utils.json.Json;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.DropDescBO;
import com.lodogame.ldsg.bo.DropToolBO;
import com.lodogame.ldsg.bo.EnchantProty;
import com.lodogame.ldsg.bo.EquipEnchantBO;
import com.lodogame.ldsg.bo.EquipRefineBO;
import com.lodogame.ldsg.bo.EquipRefineSoulBO;
import com.lodogame.ldsg.bo.Property;
import com.lodogame.ldsg.bo.UserEquipBO;
import com.lodogame.ldsg.bo.UserEquipStoneBO;
import com.lodogame.ldsg.bo.UserToolBO;
import com.lodogame.ldsg.constants.ActivityTargetType;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.constants.ToolId;
import com.lodogame.ldsg.constants.ToolType;
import com.lodogame.ldsg.constants.ToolUseType;
import com.lodogame.ldsg.event.CopperUpdateEvent;
import com.lodogame.ldsg.event.EquiGetEvent;
import com.lodogame.ldsg.event.EquipDressEvent;
import com.lodogame.ldsg.event.EquipUpdateEvent;
import com.lodogame.ldsg.event.EquipUpgradeEvent;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.HeroUpdateEvent;
import com.lodogame.ldsg.event.QiangHuaEvent;
import com.lodogame.ldsg.event.WuQiJingJieEvent;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.helper.DropDescHelper;
import com.lodogame.ldsg.service.ActivityTaskService;
import com.lodogame.ldsg.service.DailyTaskService;
import com.lodogame.ldsg.service.EquipService;
import com.lodogame.ldsg.service.EventService;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.StoneService;
import com.lodogame.ldsg.service.ToolService;
import com.lodogame.ldsg.service.UnSynLogService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.EnchantProperty;
import com.lodogame.model.EquipEnchant;
import com.lodogame.model.EquipRefine;
import com.lodogame.model.EquipRefineSoul;
import com.lodogame.model.RefineCondition;
import com.lodogame.model.SkillData;
import com.lodogame.model.SystemDailyTask;
import com.lodogame.model.SystemEquip;
import com.lodogame.model.SystemEquipAttr;
import com.lodogame.model.SystemEquipRefine;
import com.lodogame.model.SystemEquipSellTool;
import com.lodogame.model.SystemEquipUpgrade;
import com.lodogame.model.SystemEquipUpgradeTool;
import com.lodogame.model.SystemHero;
import com.lodogame.model.SystemRefineSoulData;
import com.lodogame.model.SystemRefineSoulMap;
import com.lodogame.model.SystemStone;
import com.lodogame.model.SystemTool;
import com.lodogame.model.User;
import com.lodogame.model.UserEquip;
import com.lodogame.model.UserEquipStone;
import com.lodogame.model.UserHero;

public class EquipServiceImpl implements EquipService {

	private static final Logger LOG = Logger.getLogger(EquipServiceImpl.class);

	@Autowired
	private DailyTaskService dailyTaskService;

	@Autowired
	private SystemEquipSellToolDao systemEquipSellToolDao;

	@Autowired
	private SystemToolDao systemToolDao;

	@Autowired
	private UserEquipDao userEquipDao;

	@Autowired
	private SystemEquipDao systemEquipDao;

	@Autowired
	private SystemEquipRefineDao systemEquipRefineDao;

	@Autowired
	private EquipRefineSoulDao equipRefineSoulDao;

	@Autowired
	private SystemRefineSoulDataDao systemRefineSoulDataDao;

	@Autowired
	private SystemRefineSoulMapDao systemRefineSoulMapDao;

	@Autowired
	private UserService userService;

	@Autowired
	private SystemEquipAttrDao systemEquipAttrDao;

	@Autowired
	private SystemEquipUpgradeDao systemEquipUpgradeDao;

	@Autowired
	private SystemEquipUpgradeToolDao systemEquipUpgradeToolDao;

	@Autowired
	private UserToolDao userToolDao;

	@Autowired
	private SkillDataDao skillDataDao;

	@Autowired
	private EnchantPropertyDao enchantPropertyDao;

	@Autowired
	private HeroService heroService;

	@Autowired
	private EventService eventService;

	@Autowired
	private EquipRefineDao equipRefineDao;

	@Autowired
	private ToolService toolService;

	@Autowired
	private ActivityTaskService activityTaskService;

	@Autowired
	private UnSynLogService unSynLogService;

	@Autowired
	private StoneService stoneService;

	@Autowired
	private UserEquipStoneDao userEquipStoneDao;

	@Autowired
	private SystemStoneDao systemStoneDao;

	@Autowired
	private EquipEnchantDao equipEnchantDao;

	@Autowired
	private UserHeroDao userHeroDao;

	public List<UserEquipBO> getUserEquipList(String userId) {
		return this.getUserEquipList(userId, true);
	}

	public List<UserEquipBO> getUserEquipList(String userId, boolean all) {

		List<UserEquipBO> userEquipBOList = new ArrayList<UserEquipBO>();

		List<UserEquip> userEquipList = this.userEquipDao.getUserEquipList(userId);
		for (UserEquip userEquip : userEquipList) {
			if (!all && StringUtils.isEmpty(userEquip.getUserHeroId())) {
				continue;
			}
			UserEquipBO userEquipBO = this.createEquipBO(userEquip);
			userEquipBOList.add(userEquipBO);
		}

		return userEquipBOList;

	}

	@Override
	public List<UserEquipBO> getUserHeroEquipList(String userId, String userHeroId) {

		List<UserEquip> userEquipList = this.userEquipDao.getHeroEquipList(userId, userHeroId);
		Map<Integer, UserEquipBO> eqs = new HashMap<Integer, UserEquipBO>();
		for (UserEquip userEquip : userEquipList) {
			UserEquipBO userEquipBO = this.createEquipBO(userEquip);
			SystemEquip systemEquip = systemEquipDao.get(userEquip.getEquipId());
			if (systemEquip == null) {
				LOG.error("系统状态不存在.equipId[" + userEquip.getEquipId() + "]");
				continue;
			}
			eqs.put(systemEquip.getEquipType(), userEquipBO);
		}

		return new ArrayList<UserEquipBO>(eqs.values());

	}

	/**
	 * 创建用户装备BO
	 * 
	 * @param userEquip
	 * @return
	 */
	private UserEquipBO createEquipBO(UserEquip userEquip) {

		UserEquipBO userEquipBO = new UserEquipBO();

		String userId = userEquip.getUserId();

		SystemEquip systemEquip = this.systemEquipDao.get(userEquip.getEquipId());

		int equipLevel = userEquip.getEquipLevel();

		userEquipBO.setEquipId(userEquip.getEquipId());
		userEquipBO.setEquipLevel(equipLevel);
		userEquipBO.setUserEquipId(userEquip.getUserEquipId());
		userEquipBO.setUserHeroId(userEquip.getUserHeroId());

		if (systemEquip == null) {
			return userEquipBO;
		}

		SystemEquipAttr systemEquipAttr = this.systemEquipAttrDao.getEquipAttr(systemEquip.getEquipStar(), equipLevel);
		if (systemEquipAttr == null) {
			return userEquipBO;
		}

		userEquipBO.setStoneHoleNum(systemEquip.getStoneHoleNum());
		userEquipBO.setEquipName(systemEquip.getEquipName());
		userEquipBO.setEquipType(systemEquip.getEquipType());

		int initAttack = systemEquip.getAttackInit() + (int) (systemEquip.getAttackGrowth() * equipLevel) + systemEquip.getAttackRatio();
		int initDefense = systemEquip.getDefenseInit() + (int) (systemEquip.getDefenseGrowth() * equipLevel) + systemEquip.getDefenseRatio();
		int initLife = systemEquip.getLifeInit() + (int) (systemEquip.getLifeGrowth() * equipLevel) + systemEquip.getLifeRatio();

		userEquipBO.setInitialLife(initLife);
		userEquipBO.setInitialAttack(initAttack);
		userEquipBO.setInitialPhysicsDefense(initDefense);
		userEquipBO.setPrice(systemEquipAttr.getRecyclePrice());

		List<UserEquipStone> userEquipStoneList = this.userEquipStoneDao.getUserEquipStone(userEquip.getUserEquipId());

		List<UserEquipStoneBO> userEquipStoneBOList = new ArrayList<UserEquipStoneBO>();
		for (UserEquipStone userEquipStone : userEquipStoneList) {
			UserEquipStoneBO userEquipStoneBO = new UserEquipStoneBO();
			userEquipStoneBO.setPos(userEquipStone.getPos());
			userEquipStoneBO.setStoneId(userEquipStone.getStoneId());
			userEquipStoneBOList.add(userEquipStoneBO);

			SystemStone systemStone = this.systemStoneDao.get(userEquipStone.getStoneId());

			if (systemStone == null) {
				continue;
			}

			userEquipBO.addVal(systemStone.getStoneType(), systemStone.getStoneValue(), systemStone.getStoneValuePercent());

		}
		userEquipBO.setUserEquipStoneBOList(userEquipStoneBOList);

		// 添加精炼属性
		addRefineProperty(userId, userEquipBO, userEquip, systemEquip);
		// 添加点化属性
		addEnchantProperty(userId, userEquipBO, userEquip.getUserEquipId());
		return userEquipBO;
	}

	public void addEnchantProperty(String userId, UserEquipBO userEquipBO, String userEquipId) {
		EquipEnchant equipEnchant = this.equipEnchantDao.getEquipEnchant(userId, userEquipId);
		if (equipEnchant == null) {
			return;
		}
		String curPro = equipEnchant.getCurProperty();
		String[] cStr = curPro.split(",");
		List<EnchantProty> curProVal = new ArrayList<EnchantProty>();
		for (String str : cStr) {
			String[] enStr = str.split(":");
			if (enStr.length > 1) {
				EnchantProty enchantProty = new EnchantProty(Integer.parseInt(enStr[0]), Integer.parseInt(enStr[2]));
				enchantProty.setColor(Integer.parseInt(enStr[1]));
				curProVal.add(enchantProty);
			}
		}
		userEquipBO.setAddEnchantVal(curProVal);
	}

	/**
	 * 添加精炼属性
	 * 
	 * @param userEquipBO
	 * @param userEquip
	 * @param systemEquip
	 */
	public void addRefineProperty(String userId, UserEquipBO userEquipBO, UserEquip userEquip, SystemEquip systemEquip) {
		// 添加精炼属性
		List<Property> list = new ArrayList<Property>();
		List<EquipRefine> listr = checkRefinePoint(userEquip.getUserId(), userEquip.getUserEquipId());
		for (EquipRefine equipRefine : listr) {

			RefineCondition condition = new RefineCondition(equipRefine, systemEquip);
			SystemEquipRefine systemEquipRefine = systemEquipRefineDao.getSystemEquipRefine(condition);
			if (systemEquipRefine == null) {
				continue;
			}
			Property p = new Property(systemEquipRefine.getProType(), systemEquipRefine.getProValue());
			list.add(p);
		}
		userEquipBO.setAddRefineVal(list);
	}

	public boolean updateEquipHero(String userId, String userEquipId, String userHeroId, int equipType, EventHandle handle) {

		UserEquip userEquip = this.userEquipDao.get(userId, userEquipId);

		if (userEquip == null) {
			throw new ServiceException(ServiceReturnCode.FAILD, "穿载装备出错，用户装备不存在.userEquipId[" + userEquipId + "]");
		}

		if (!StringUtils.equalsIgnoreCase(userId, userEquip.getUserId())) {
			throw new ServiceException(ServiceReturnCode.FAILD, "穿载装备出错，装备所有者不符.userEquipId[" + userEquipId + "], userId[" + userId + "], userEquip.userId[" + userEquip.getUserId() + "]");
		}

		if (StringUtils.isEmpty(userHeroId)) {

			boolean success = this.userEquipDao.updateEquipHero(userId, userEquipId, "");

			if (StringUtils.isNotEmpty(userEquip.getUserHeroId())) {
				HeroUpdateEvent heroUpdateEvent = new HeroUpdateEvent(userId, userEquip.getUserHeroId());
				handle.handle(heroUpdateEvent);
			}

			return success;
		}

		UserHero userHero = this.heroService.get(userId, userHeroId);

		SystemHero systemHero = this.heroService.getSysHero(userHero.getSystemHeroId());
		SystemEquip systemEquip = this.getSysEquip(userEquip.getEquipId());
		if (systemEquip.getCareer() != 100 && systemEquip.getCareer() != systemHero.getCareer()) {
			String message = "穿载装备出错，武将职业不符.hero.career[" + systemHero.getCareer() + "],equip.career[" + systemEquip.getCareer() + "]";
			LOG.warn(message);
			throw new ServiceException(INSTALL_EQUIP_CAREER_ERROR, message);
		}

		UserEquip oldUserEquip = null;

		// 拿到武将当前身上的装备，看有没有替换的
		List<UserEquip> heroEquipList = this.userEquipDao.getHeroEquipList(userId, userHeroId);
		for (UserEquip heroEquip : heroEquipList) {
			int equipId = heroEquip.getEquipId();
			SystemEquip systemEquip2 = this.systemEquipDao.get(equipId);
			if (systemEquip2.getEquipType() == equipType) {
				oldUserEquip = heroEquip;
				break;
			}
		}

		String oldEquipHeroID = userEquip.getUserHeroId();

		boolean success = this.userEquipDao.updateEquipHero(userId, userEquipId, userHeroId);

		userEquip.setUserHeroId(userHeroId);

		if (success && oldUserEquip != null) {
			this.userEquipDao.updateEquipHero(userId, oldUserEquip.getUserEquipId(), null);
			oldUserEquip.setUserHeroId(null);

			EquipUpdateEvent equipUpdateEvent = new EquipUpdateEvent(userId, oldUserEquip);
			handle.handle(equipUpdateEvent);
		}

		// 武将的战斗力发生改变
		this.eventService.addHeroPowerUpdateEvent(userId, userHeroId);

		// 穿戴装备武将更新
		HeroUpdateEvent heroUpdateEvent = new HeroUpdateEvent(userId, userHeroId);
		handle.handle(heroUpdateEvent);

		// 原来的穿戴武将
		if (StringUtils.isNotEmpty(oldEquipHeroID)) {
			// 被卸下装备武将更新
			HeroUpdateEvent heroUpdateOffEvent = new HeroUpdateEvent(userId, oldEquipHeroID);
			handle.handle(heroUpdateOffEvent);
		}

		EquipDressEvent e = new EquipDressEvent(userId, userHeroId);
		eventService.dispatchEvent(e);

		return true;
	}

	/**
	 * 暂时不用，用的时候需要确认最大升级等级
	 */
	@Override
	public int autoUpgrade(String userId, String userEquipId, List<Integer> addLevelList) {

		// 一键强化停下来的原因
		int stopResult = 0;

		LOG.debug("装备一键强化.userId[" + userId + "], userEquipId[" + userEquipId + "]");
		UserEquip userEquip = this.getUserEquip(userId, userEquipId);
		SystemEquip systemEquip = this.getSysEquip(userEquip.getEquipId());
		User user = this.userService.get(userId);

		int totalNeedCopper = 0;
		int totalAddLevel = 0;

		int maxLevel = user.getLevel() * 2;

		int i = 0;
		while (i < 200) {
			i += 1;

			if (userEquip.getEquipLevel() >= maxLevel) {
				stopResult = UPGRADE_EQUIP_LEVEL_OVER_USER_LEVEL;
				break;
			}

			SystemEquipAttr systemEquipAttr = this.systemEquipAttrDao.getEquipAttr(systemEquip.getEquipStar(), userEquip.getEquipLevel());

			if (systemEquipAttr == null) {
				LOG.error("装备星级配置不存在.starLevel[" + systemEquip.getEquipStar() + "], level[" + userEquip.getEquipLevel() + "]");
			}

			int needCopper = systemEquipAttr.getUpgradePrice();
			if (totalNeedCopper + needCopper > user.getCopper()) {
				stopResult = UPGRADE_EQUIP_COPPER_NOT_ENOUGH;
				break;
			}

			// 上升等级
			int addLevel = 1;

			if (addLevel + userEquip.getEquipLevel() > maxLevel) {
				addLevel = maxLevel - userEquip.getEquipLevel();
			}

			userEquip.setEquipLevel(userEquip.getEquipLevel() + addLevel);

			totalAddLevel += addLevel;
			totalNeedCopper += needCopper;

			addLevelList.add(addLevel);

		}

		if (totalAddLevel > 0) {
			if (this.userService.reduceCopper(userId, totalNeedCopper, ToolUseType.REDUCE_ADUO_UPGRADE_EQUIP)) {
				this.userEquipDao.updateEquipLevel(userId, userEquipId, totalAddLevel, maxLevel);

				QiangHuaEvent e = new QiangHuaEvent(userId, systemEquipDao.get(userEquip.getEquipId()).getEquipType(), userEquip.getEquipLevel());
				eventService.dispatchEvent(e);

			}
		} else {
			throw new ServiceException(stopResult, "不能强化");
		}

		if (addLevelList.size() > 0) {
			// 活跃度奖励
			activityTaskService.updateActvityTask(userId, ActivityTargetType.UPDATE_EQUIP, addLevelList.size());
		}

		// 给“装备强化 N 次”每日任务添加1次完成次数
		dailyTaskService.sendUpdateDailyTaskEvent(userId, SystemDailyTask.EQUIP_MERGE, totalAddLevel);
		return stopResult;
	}

	public boolean upgrade(String userId, String userEquipId, EventHandle handle) {

		LOG.debug("装备强化.userId[" + userId + "], userEquipId[" + userEquipId + "]");
		UserEquip userEquip = this.getUserEquip(userId, userEquipId);
		SystemEquip systemEquip = this.getSysEquip(userEquip.getEquipId());
		User user = this.userService.get(userId);
		if (userEquip.getEquipLevel() >= user.getLevel() * 2) {
			throw new ServiceException(UPGRADE_EQUIP_LEVEL_OVER_USER_LEVEL, "装备强化失败.装备等级超过用户等级.userId[" + userId + "], userEquipId[" + userEquipId + "]");
		}

		// 不是vip需要根据概率是否增加强化等级
		SystemEquipAttr systemEquipAttr = this.systemEquipAttrDao.getEquipAttr(systemEquip.getEquipStar(), userEquip.getEquipLevel());

		int needCopper = systemEquipAttr.getUpgradePrice();

		boolean success = this.userService.reduceCopper(userId, needCopper, ToolUseType.REDUCE_UPGRADE_EQUIP);
		// 先扣除钱，再更新装备
		if (success) {

			// 活跃度奖励
			activityTaskService.updateActvityTask(userId, ActivityTargetType.UPDATE_EQUIP, 1);

			if (this.userEquipDao.updateEquipLevel(userId, userEquipId, 1, user.getLevel() * 2)) {

				QiangHuaEvent e = new QiangHuaEvent(userId, systemEquipDao.get(userEquip.getEquipId()).getEquipType(), userEquip.getEquipLevel() + 1);
				eventService.dispatchEvent(e);

				// 装备更新事件
				EquipUpdateEvent equipUpdateEvent = new EquipUpdateEvent(userId, userEquipId);

				if (!StringUtils.isBlank(userEquip.getUserHeroId())) {
					// 武将更新事件
					HeroUpdateEvent heroUpdateEvent = new HeroUpdateEvent(userId, userEquip.getUserHeroId());
					handle.handle(heroUpdateEvent);
				}

				handle.handle(equipUpdateEvent);

			} else {
				throw new ServiceException(ServiceReturnCode.FAILD, "装备强化失败,数据异常. userId[" + userId + "], userEquipId[" + userEquipId + "], userEquip.userHeroId[" + userEquip.getUserHeroId() + "]");
			}

		} else {
			throw new ServiceException(UPGRADE_EQUIP_COPPER_NOT_ENOUGH, "装备强化失败.用户银币不足.userId[" + userId + "], userEquipId[" + userEquipId + "]");
		}

		this.eventService.addUserPowerUpdateEvent(userId);

		// 给“装备强化 N 次”每日任务添加1次完成次数
		dailyTaskService.sendUpdateDailyTaskEvent(userId, SystemDailyTask.EQUIP_MERGE, 1);

		return success;
	}

	public CommonDropBO sell(String userId, List<String> userEquipIdList, EventHandle handle) {

		int amount = 0;
		// Map<Integer, UserToolBO> toolBOs = new HashMap<Integer,
		// UserToolBO>();
		// 装备列表
		List<UserEquip> equipMentList = new ArrayList<UserEquip>();
		for (String userEquipId : userEquipIdList) {
			amount += this.checkIsSellAble(userId, userEquipId, equipMentList);
		}

		// 先删除装备，后给银币
		boolean success = this.userEquipDao.delete(userId, userEquipIdList);

		// 日志
		unSynLogService.toolLog(userId, ToolType.EQUIP, 0, userEquipIdList.size(), ToolUseType.REDUCE_SELL_EQUIP, -1, StringUtils.join(userEquipIdList, ","), success);

		CommonDropBO dr = new CommonDropBO();

		if (success) {
			if (this.userService.addCopper(userId, amount, ToolUseType.ADD_SELL_EQUIP)) {
				CopperUpdateEvent event = new CopperUpdateEvent(userId, amount);
				handle.handle(event);
			} else {
				String message = "出售装备后增加银币异常.userId[" + userId + "], userEquipId[" + StringUtils.join(userEquipIdList, ",") + "], amount[" + amount + "]";
				throw new ServiceException(ServiceReturnCode.FAILD, message);
			}

			StringBuffer addTools = new StringBuffer();
			Map<Integer, Integer> allmap = new HashMap<Integer, Integer>();
			for (UserEquip userEquip : equipMentList) {
				addTools.append(getEquipSellTools(userId, userEquip.getEquipId()));
				// 有宝石自动放入背包
				if (hasStone(userEquip)) {
					Map<Integer, Integer> eqmap = stoneService.putPack(userEquip);
					for (Integer key : eqmap.keySet()) {
						int value = (allmap.get(key) == null ? 0 : allmap.get(key)) + eqmap.get(key);
						allmap.put(key, value);
					}
				}
			}
			// 宝石额外给客户端
			for (Integer stoneId : allmap.keySet()) {
				addTools.append(ToolType.STONE).append(",").append(stoneId).append(",").append(allmap.get(stoneId)).append("|");
			}
			dr = toolService.give(userId, addTools.toString(), ToolUseType.ADD_SELL_EQUIP);
		}

		return dr;
	}

	public boolean hasStone(UserEquip userEquip) {
		List<UserEquipStone> list = this.userEquipStoneDao.getUserEquipStone(userEquip.getUserEquipId());
		return list != null && list.size() > 0;
	}

	private String getEquipSellTools(String userId, int systemEquipId) {
		SystemEquip systemEquip = systemEquipDao.get(systemEquipId);
		if (systemEquip.getSuitId() == 0) {
			SystemEquipSellTool tools = systemEquipSellToolDao.getByEquipColor(systemEquip.getColor());
			if (tools != null) {
				return tools.getTools();
			}
		}
		return "";
	}

	/**
	 * 判断装备是否可以出售
	 * 
	 * @param userId
	 * @param userEquip
	 */
	private int checkIsSellAble(String userId, String userEquipId, List<UserEquip> equipMentList) {

		UserEquip userEquip = this.getUserEquip(userId, userEquipId);

		if (userEquip == null) {
			String message = "出售装备异常装备不存在.userId[" + userId + "], userEquipId[" + userEquipId + "]";
			LOG.error(message);
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

		// 所有人验证
		this.checkIsOwner(userId, userEquip);

		if (!StringUtils.isBlank(userEquip.getUserHeroId())) {
			throw new ServiceException(SELL_EQUIP_IS_INSTALLED, "装备出售失败,装备已经穿戴. userId[" + userId + "], userEquipId[" + userEquipId + "], userEquip.userHeroId[" + userEquip.getUserHeroId() + "]");
		}

		SystemEquip systemEquip = this.systemEquipDao.get(userEquip.getEquipId());

		SystemEquipAttr systemEquipAttr = this.systemEquipAttrDao.getEquipAttr(systemEquip.getEquipStar(), userEquip.getEquipLevel());

		int price = systemEquipAttr.getRecyclePrice();
		equipMentList.add(userEquip);
		return price;
	}

	/**
	 * 装备所有人验证
	 * 
	 * @param userId
	 * @param userEquip
	 */
	private void checkIsOwner(String userId, UserEquip userEquip) {

		if (!StringUtils.equalsIgnoreCase(userEquip.getUserId(), userId)) {
			throw new ServiceException(ServiceReturnCode.FAILD, "装备所有者验证失败,装备所有人不符. userId[" + userId + "], userEquipId[" + userEquip.getUserEquipId() + "], userEquip.userId[" + userEquip.getUserId()
					+ "]");
		}
	}

	public UserEquip getUserEquip(String userId, String userEquipId) {
		UserEquip userEquip = this.userEquipDao.get(userId, userEquipId);
		if (userEquip == null) {
			throw new ServiceException(ServiceReturnCode.FAILD, "装备获取失败,装备不存在. userEquipId[" + userEquipId + "]");
		}

		return userEquip;
	}

	public SystemEquip getSysEquip(int equipId) {
		SystemEquip systemEquip = this.systemEquipDao.get(equipId);
		if (systemEquip == null) {
			String message = "系统装备获取失败,装备不存在. equipId[" + equipId + "]";
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

		return systemEquip;
	}

	public UserEquipBO getUserEquipBO(String userId, String userEquipId) {
		UserEquip userEquip = this.getUserEquip(userId, userEquipId);
		return this.createEquipBO(userEquip);
	}

	public boolean addUserEquip(String userId, String userEquipId, final int equipId, int useType) {

		UserEquip userEquip = new UserEquip();
		userEquip.setEquipId(equipId);
		userEquip.setEquipLevel(1);
		userEquip.setUserEquipId(userEquipId);
		userEquip.setUserId(userId);
		userEquip.setCreatedTime(new Date());
		userEquip.setUpdatedTime(new Date());

		boolean success = this.userEquipDao.add(userEquip);
		try {
			SystemEquip ep = systemEquipDao.get(equipId);
			EquiGetEvent e = new EquiGetEvent(userId, ep.getColor());
			eventService.dispatchEvent(e);
		} catch (Exception e) {
			LOG.error("cheng jiu err" + this.getClass().getName());
		}
		unSynLogService.toolLog(userId, ToolType.EQUIP, equipId, 1, useType, 1, userEquipId, success);
		return success;
	}

	@Override
	public boolean addUserEquips(String userId, Map<String, Integer> equipIdMap, int useType) {
		List<UserEquip> userEquipList = new ArrayList<UserEquip>();

		Set<Integer> gainSystemEquipIdSet = new HashSet<Integer>();

		for (Map.Entry<String, Integer> entry : equipIdMap.entrySet()) {

			final int systemEquipId = entry.getValue();
			String userEquipId = entry.getKey();

			Date now = new Date();

			UserEquip userEquip = new UserEquip();
			userEquip.setEquipId(systemEquipId);
			userEquip.setUserEquipId(userEquipId);
			userEquip.setCreatedTime(now);
			userEquip.setUpdatedTime(now);
			userEquip.setUserId(userId);
			userEquip.setEquipLevel(1);

			gainSystemEquipIdSet.add(systemEquipId);

			userEquipList.add(userEquip);
			try {
				SystemEquip ep = systemEquipDao.get(systemEquipId);
				EquiGetEvent e = new EquiGetEvent(userId, ep.getColor());
				eventService.dispatchEvent(e);
			} catch (Exception e) {
				LOG.error("cheng jiu err" + this.getClass().getName());
			}
		}

		boolean success = this.userEquipDao.addEquips(userEquipList);

		unSynLogService.toolLog(userId, ToolType.EQUIP, 0, equipIdMap.size(), useType, 1, Json.toJson(equipIdMap), success);

		return success;
	}

	@Override
	public Map<String, Object> upgradePre(String userId, String userEquipId) {

		UserEquipBO userEquipBO = null;
		int status = 1;

		LOG.debug("装备强化预览 .userId[" + userId + "], userEquipId[" + userEquipId + "]");
		UserEquip userEquip = this.getUserEquip(userId, userEquipId);

		SystemEquip systemEqup = this.systemEquipDao.get(userEquip.getEquipId());

		SystemEquipAttr systemEquipAttr = this.systemEquipAttrDao.getEquipAttr(systemEqup.getEquipStar(), userEquip.getEquipLevel());

		int needCopper = systemEquipAttr.getUpgradePrice();

		User user = this.userService.get(userId);
		if (user.getCopper() < needCopper) {
			status = -1;// 银币不足
		} else if (userEquip.getEquipLevel() >= user.getLevel() * 2) {
			status = -2;// 等级不足
		}

		userEquip.setEquipLevel(userEquip.getEquipLevel() + 1);
		userEquipBO = this.createEquipBO(userEquip);

		Map<String, Object> retVal = new HashMap<String, Object>();

		retVal.put("userEquipBO", userEquipBO);
		retVal.put("status", status);
		retVal.put("needCopper", needCopper);

		return retVal;
	}

	@Override
	public Map<String, Object> mergeEquip(String userId, String userEquipId, boolean useMoney, EventHandle handle) {

		Map<String, Object> map = new HashMap<String, Object>();

		UserEquip userEquip = this.getUserEquip(userId, userEquipId);

		int equipId = userEquip.getEquipId();

		SystemEquip systemEquip = this.getSysEquip(equipId);

		if (userEquip.getEquipLevel() < systemEquip.getAdvanceLevel()) {
			String message = "装备合成异常，装备等级不够.userId[" + userId + "], userEquipId[" + userEquipId + "]";
			throw new ServiceException(MERGE_EQUIP_NOT_ENOUGH_LEVEL, message);
		}

		SystemEquipUpgrade systemEquipUpgrade = this.systemEquipUpgradeDao.get(equipId);
		if (systemEquipUpgrade == null) {
			String message = "装备合成异常，装备合成配置不存在.userId[" + userId + "], userEquipId[" + userEquipId + "]";
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

		List<SystemEquipUpgradeTool> toolList = this.systemEquipUpgradeToolDao.getList(equipId);

		List<DropToolBO> dropToolBOList = new ArrayList<DropToolBO>();
		for (SystemEquipUpgradeTool systemEquipUpgradeTool : toolList) {
			dropToolBOList.add(new DropToolBO(systemEquipUpgradeTool.getToolType(), systemEquipUpgradeTool.getToolId(), systemEquipUpgradeTool.getToolNum()));
		}

		map.put("tr", dropToolBOList);
		if (!useMoney) {// 不是金币进阶
			this.checkToolEnough(userId, toolList);// 材料是否够
		}

		if (useMoney) {
			User user = this.userService.get(userId);
			if (user.getVipLevel() < 5) {
				String message = "装备合成异常(金币),vip等级不足.userId[" + userId + "], vipLevel[" + user.getVipLevel() + "]";
				throw new ServiceException(MERGE_EQUIP_NOT_ENOUGH_VIP, message);
			}
			int needMoney = calEquipUpgradeNeedGold(userId, systemEquip.getEquipId());

			if (needMoney != 0 && !this.userService.reduceGold(userId, needMoney, ToolUseType.REDUCE_GOLD_MERGE_EQUIP)) {
				String message = "装备合成异常(金币)，用户金币不足.userId[" + userId + "], needMoney[" + needMoney + "]";
				throw new ServiceException(MERGE_EQUIP_NOT_ENOUGH_GOLD, message);
			}
		}

		for (SystemEquipUpgradeTool tool : toolList) {
			int toolId = tool.getToolId();
			int needToolNum = tool.getToolNum();
			int userToolNum = userToolDao.getUserToolNum(userId, toolId);
			int reduceToolNum = userToolNum > needToolNum ? needToolNum : userToolNum;
			if (reduceToolNum > 0) {
				boolean success = this.toolService.reduceTool(userId, ToolType.MATERIAL, toolId, reduceToolNum, ToolUseType.REDUCE_MERGE_EQUIP);
				if (!success) {
					String message = "装备进阶失败，扣材料失败.userId[" + userId + "], toolId[" + toolId + "], toolNum[" + reduceToolNum + "]";
					LOG.warn(message);
					throw new ServiceException(MERGE_EQUIP_NOT_ENOUGH_TOOL, message);
				}
			}
		}

		boolean success = this.userEquipDao.updateEquipId(userId, userEquipId, systemEquipUpgrade.getUpgradeEquipId());
		int needMoney = calEquipUpgradeNeedGold(userId, systemEquipUpgrade.getUpgradeEquipId());

		map.put("ng", needMoney);
		if (!success) {
			String message = "装备合成异常，更新装备系统ID失败.userId[" + userId + "], userEquipId[" + userEquipId + "]";
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		} else {
			try {
				SystemEquip pp = systemEquipDao.get(systemEquipUpgrade.getUpgradeEquipId());
				WuQiJingJieEvent e = new WuQiJingJieEvent(userId, pp.getColor(), pp.getEquipType());

				EquiGetEvent e2 = new EquiGetEvent(userId, pp.getColor());
				eventService.dispatchEvent(e);
				eventService.dispatchEvent(e2);
			} catch (Exception e) {
				LOG.error("cheng jiu err" + this.getClass().getName());
			}
			// 装备更新事件
			EquipUpdateEvent equipUpdateEvent = new EquipUpdateEvent(userId, userEquipId);

			if (!StringUtils.isBlank(userEquip.getUserHeroId())) {
				// 武将更新事件
				HeroUpdateEvent heroUpdateEvent = new HeroUpdateEvent(userId, userEquip.getUserHeroId());
				handle.handle(heroUpdateEvent);
			}

			handle.handle(equipUpdateEvent);

			Event event = new EquipUpgradeEvent(userId, userEquipId);
			this.eventService.dispatchEvent(event);

		}

		return map;

	}

	@Override
	public UserEquipBO mergeEquipPre(String userId, String userEquipId) {

		UserEquip userEquip = this.getUserEquip(userId, userEquipId);

		int equipId = userEquip.getEquipId();

		SystemEquipUpgrade systemEquipUpgrade = this.systemEquipUpgradeDao.get(equipId);
		if (systemEquipUpgrade == null) {
			return null;
		}

		userEquip.setEquipId(systemEquipUpgrade.getUpgradeEquipId());

		return this.createEquipBO(userEquip);

	}

	/**
	 * 装备升级时，如果需要的道具不足，计算需要消耗多少元宝进行元宝升级
	 */
	@Override
	public int calEquipUpgradeNeedGold(String userId, int systemEquipId) {
		List<SystemEquipUpgradeTool> needToolList = systemEquipUpgradeToolDao.getList(systemEquipId);
		int needGoldNum = 0;

		for (SystemEquipUpgradeTool tool : needToolList) {

			int toolId = tool.getToolId();
			int toolNum = tool.getToolNum();

			int userToolNum = this.userToolDao.getUserToolNum(userId, toolId);
			if (userToolNum < toolNum) {
				SystemTool systemTool = systemToolDao.get(toolId);
				needGoldNum += (toolNum - userToolNum) * systemTool.getGoldMerge();
			}
		}

		return needGoldNum;
	}

	/**
	 * 判断用户装备是否足够
	 * 
	 * @param userId
	 * @param toolList
	 */
	public void checkToolEnough(String userId, List<SystemEquipUpgradeTool> toolList) {

		for (SystemEquipUpgradeTool systemEquipUpgradeTool : toolList) {

			int toolId = systemEquipUpgradeTool.getToolId();
			int toolNum = systemEquipUpgradeTool.getToolNum();

			int userToolNum = this.userToolDao.getUserToolNum(userId, toolId);

			if (userToolNum < toolNum) {
				String message = "装备进阶失败，材料不足.userId[" + userId + "], toolId[" + toolId + "], toolNum[" + toolNum + "], userToolNum[" + userToolNum + "]";
				throw new ServiceException(MERGE_EQUIP_NOT_ENOUGH_TOOL, message);
			}

		}

	}

	@Override
	public int getUserEquipCount(String userId) {
		return this.userEquipDao.getUserEquipCount(userId);
	}

	@Override
	public List<UserEquip> getUserEquipList(String userId, int equipId) {
		return userEquipDao.getUserEquipList(userId, equipId);
	}

	@Override
	public UserEquip getLowestLevel(List<UserEquip> userEquipList) {
		UserEquip minUseEquip = userEquipList.get(0);
		int minEquipLevel = minUseEquip.getEquipLevel();

		for (UserEquip userEquip : userEquipList) {
			int equipLevel = userEquip.getEquipLevel();
			if (equipLevel < minEquipLevel) {
				minEquipLevel = equipLevel;
				minUseEquip = userEquip;
			}
		}
		return minUseEquip;
	}

	@Override
	public List<UserEquipBO> createUserEquipBOList(String userId, List<DropToolBO> boList) {
		List<UserEquipBO> userEquipBoList = new ArrayList<UserEquipBO>();

		for (DropToolBO bo : boList) {
			UserEquip userEquip = getUserEquip(userId, bo.getValue());
			UserEquipBO userEquipBo = createEquipBO(userEquip);
			userEquipBoList.add(userEquipBo);
		}
		return userEquipBoList;
	}

	@Override
	public List<EquipRefineBO> refinePre(String userId, String userEquipId) {

		List<EquipRefine> list = checkRefinePoint(userId, userEquipId);

		List<EquipRefineBO> bolist = new ArrayList<EquipRefineBO>();

		for (EquipRefine equipRefine : list) {

			SystemEquip systemEquip = this.getSysEquip(equipRefine.getEquipId());

			RefineCondition condition = new RefineCondition(equipRefine, systemEquip);
			SystemEquipRefine systemEquipRefine = systemEquipRefineDao.getSystemEquipRefine(condition);
			EquipRefineBO equipRefineBO = new EquipRefineBO(systemEquipRefine);
			equipRefineBO.setPower(equipRefineBO.getProValue());
			// 下一级
			condition.setRefineLevel(condition.getRefineLevel() + 1);
			SystemEquipRefine systemEquipRefineNex = systemEquipRefineDao.getSystemEquipRefine(condition);
			EquipRefineBO equipRefineBONex = new EquipRefineBO(systemEquipRefineNex);
			equipRefineBO.setPowerNex(equipRefineBONex.getProValue());
			equipRefineBO.setProNexValue(equipRefineBONex.getProValue());
			bolist.add(equipRefineBO);
		}
		return bolist;

	}

	/**
	 * 获得装备精炼点
	 * 
	 * @param userId
	 * @param userEquipId
	 * @param type
	 * @return
	 */
	public EquipRefine getRefinePoint(String userId, String userEquipId, int type) {
		EquipRefine equipRefine = equipRefineDao.getEquipRefine(userId, userEquipId, type);
		if (equipRefine == null) {

			UserEquip userEquip = this.userEquipDao.get(userId, userEquipId);

			equipRefine = new EquipRefine();
			equipRefine.setRefineLevel(1);
			equipRefine.setUserId(userId);
			equipRefine.setRefinePoint(type);
			equipRefine.setEquipId(userEquip.getEquipId());
			equipRefine.setUserEquipId(userEquipId);
			equipRefine.setCreatedTime(new Date());
			equipRefineDao.addEquipRefine(equipRefine);
		}

		return equipRefine;
	}

	/**
	 * 核对开启精炼点
	 * 
	 * @param userId
	 * @param userEquipId
	 * @return
	 */
	public List<EquipRefine> checkRefinePoint(String userId, String userEquipId) {
		List<EquipRefine> list = new ArrayList<EquipRefine>();
		UserEquip equip = userEquipDao.get(userId, userEquipId);
		SystemEquip systemEquip = systemEquipDao.get(equip.getEquipId());
		int pointNum = 0;
		if (isMagic(systemEquip)) {
			pointNum = 3;
		} else if (systemEquip.getEquipStar() >= 4) {
			pointNum = 2;
		} else if (systemEquip.getEquipStar() >= 3) {
			pointNum = 1;
		}
		if (list.size() < pointNum) {
			for (int i = list.size() + 1; i <= pointNum; i++) {
				EquipRefine equipRefine = equipRefineDao.getEquipRefine(userId, userEquipId, i);
				if (equipRefine == null) {
					equipRefine = new EquipRefine();
					equipRefine.setRefineLevel(1);
					equipRefine.setUserId(userId);
					equipRefine.setRefinePoint(i);
					equipRefine.setEquipId(systemEquip.getEquipId());
					equipRefine.setUserEquipId(userEquipId);
				}
				list.add(equipRefine);
			}
		}
		return list;
	}

	@Override
	public List<UserToolBO> refine(String userId, String userEquipId, int type) {

		List<DropDescBO> list = checkRefineCondition(userId, userEquipId, type);

		// 扣錢
		List<UserToolBO> userToolBOs = new ArrayList<UserToolBO>();
		for (DropDescBO bo : list) {
			UserToolBO toolBO = new UserToolBO();
			if (bo.getToolType() == 2) {
				if (!userService.reduceCopper(userId, bo.getToolNum(), ToolUseType.REDUCE_REFINE_SOUL_COST)) {
					String message = "金币不足.userId[" + userId + "]";
					throw new ServiceException(REFINE_NOT_ENOUGH_COPPER, message);
				}
			} else {
				if (!toolService.reduceTool(userId, bo.getToolType(), bo.getToolId(), bo.getToolNum(), ToolUseType.REDUCE_REFINE_COST)) {
					String message = "精炼消耗不足.userId[" + userId + "]";
					throw new ServiceException(REFINE_NOT_ENOUGH_TOOL, message);
				}
				toolBO.setToolId(bo.getToolId());
				toolBO.setToolNum(bo.getToolNum());
				userToolBOs.add(toolBO);
			}
		}

		// 更新精炼等级
		EquipRefine equipRefine = getRefinePoint(userId, userEquipId, type);
		equipRefine.setRefineLevel(equipRefine.getRefineLevel() + 1);
		equipRefineDao.updateEquipRefine(equipRefine);
		return userToolBOs;
	}

	/**
	 * 核对精炼条件
	 * 
	 * @param userId
	 * @param userEquipId
	 * @param type
	 */
	public List<DropDescBO> checkRefineCondition(String userId, String userEquipId, int type) {

		EquipRefine equipRefine = getRefinePoint(userId, userEquipId, type);

		UserEquip userEquip = this.userEquipDao.get(userId, userEquipId);
		if (userEquip == null) {
			return new ArrayList<DropDescBO>();
		}
		SystemEquip systemEquip = this.systemEquipDao.get(userEquip.getEquipId());

		if (!StringUtils.isEmpty(userEquip.getUserHeroId())) {

			UserHero userHero = this.userHeroDao.get(userId, userEquip.getUserHeroId());
			if (userHero != null) {
				SystemHero systemHero = this.heroService.getSysHero(userHero.getSystemHeroId());
				if (systemEquip.getCareer() != 100 && systemEquip.getCareer() != systemHero.getCareer()) {
					String message = "装备与英雄职业不匹配，无法精炼";
					throw new ServiceException(REFINE_CAREER_ERROR, message);
				}
			}

		}

		RefineCondition condition = new RefineCondition(equipRefine, systemEquip);
		SystemEquipRefine systemEquipRefine = systemEquipRefineDao.getSystemEquipRefine(condition);

		// 三星以上装备才能精炼
		if (systemEquip.getEquipStar() < 3) {
			String message = "装备星级不足.userId[" + userId + "],]";
			throw new ServiceException(REFINE_NOT_ENOUGH_STAR_LEVEL, message);
		}
		// 精炼等级已满
		if (isMaxLevel(systemEquipRefine)) {
			String message = "精炼等级已满[" + userId + "]]";
			throw new ServiceException(REFINE_MAX_LEVEL, message);
		}
		List<DropDescBO> list = DropDescHelper.parseDropTool(systemEquipRefine.getCost());
		// 精炼石不足
		for (DropDescBO bo : list) {
			if (bo.getToolType() == 2) {
				User user = userService.get(userId);
				if (user.getCopper() < bo.getToolNum()) {
					String message = "金币不足.userId[" + userId + "]";
					throw new ServiceException(REFINE_NOT_ENOUGH_COPPER, message);
				}
				continue;
			}
			if (bo.getToolNum() > userToolDao.getUserToolNum(userId, bo.getToolId())) {
				String message = "精炼消耗不足.userId[" + userId + "]";
				throw new ServiceException(REFINE_NOT_ENOUGH_TOOL, message);
			}
		}
		return list;
	}

	public boolean isMaxLevel(SystemEquipRefine systemEquipRefine) {
		return StringUtils.isEmpty(systemEquipRefine.getCost());

	}

	@Override
	public List<EquipRefineSoulBO> refineSoulPre(String userId, String userEquipId) {
		List<EquipRefineSoulBO> list = new ArrayList<EquipRefineSoulBO>();
		UserEquip userEquip = userEquipDao.get(userId, userEquipId);
		SystemEquip systemEquip = systemEquipDao.get(userEquip.getEquipId());
		// 四星以上装备才能炼魂
		if (systemEquip.getEquipStar() <= 4) {
			return null;
		}
		// 已经是神兵
		if (isMagic(systemEquip)) {
			return null;
		}

		List<SystemRefineSoulData> listDate = systemRefineSoulDataDao.getByType(systemEquip.getEquipType());
		if (listDate == null || listDate.size() == 0) {
			return null;
		}

		for (SystemRefineSoulData data : listDate) {
			List<DropDescBO> colist = DropDescHelper.parseDropTool(data.getCost());
			// 随机炼魂
			if (data.getRefineSoulType() == 0) {
				EquipRefineSoul equipSoul = getEquipRefineSoul(userId, userEquipId, 0);
				EquipRefineSoulBO bo = new EquipRefineSoulBO();
				bo.setEquipId(0);
				for (DropDescBO ddb : colist) {
					if (ddb.getToolType() == 2) {
						bo.setCoin(ddb.getToolNum());
					} else {
						bo.setCost(ddb.getToolNum());
					}
				}
				bo.setValue(0);
				bo.setLuck(equipSoul.getLuck());
				list.add(bo);
			} else {
				List<SystemRefineSoulMap> simpleCareerList = getSimpleCareer(systemEquip, data.getMapId());
				if (simpleCareerList.size() == 0) {
					String message = "该职业无缘分装备[" + systemEquip.getCareer() + "],]";
					throw new ServiceException(REFINE_SOUL_NOT_PRE_EQUIP, message);
				}
				for (SystemRefineSoulMap soulMap : simpleCareerList) {
					EquipRefineSoul equipSoul = getEquipRefineSoul(userId, userEquipId, soulMap.getEquipId());
					EquipRefineSoulBO bo = new EquipRefineSoulBO();
					bo.setEquipId(soulMap.getEquipId());
					for (DropDescBO ddb : colist) {
						if (ddb.getToolType() == 2) {
							bo.setCoin(ddb.getToolNum());
						} else {
							bo.setCost(ddb.getToolNum());
						}
					}
					int preId = soulMap.getEquipId();
					SystemEquip sysEquip = systemEquipDao.get(preId, systemEquip.getColor());
					if (sysEquip == null) {
						String message = "装备炼魂失败.preId[" + preId + "],]";
						throw new ServiceException(REFINE_SOUL_NOT_EQUIP, message);
					}
					int eqId = sysEquip.getEquipId();
					bo.setValue(getEquipProperty(eqId, userEquip.getEquipLevel()));
					bo.setLuck(equipSoul.getLuck());
					list.add(bo);
				}
			}
		}
		return list;
	}

	/**
	 * 筛选同职业的
	 * 
	 * @param systemEquip
	 * @param mapId
	 * @return
	 */
	public List<SystemRefineSoulMap> getSimpleCareer(SystemEquip systemEquip, int mapId) {
		List<SystemRefineSoulMap> slist = systemRefineSoulMapDao.getByMapId(mapId);
		List<SystemRefineSoulMap> list = new ArrayList<SystemRefineSoulMap>();
		for (SystemRefineSoulMap smap : slist) {
			if (systemEquipDao.get(smap.getEquipId()).getCareer() == systemEquip.getCareer()) {
				list.add(smap);
			}
		}
		return list;
	}

	public boolean isMagic(SystemEquip systemEquip) {
		return systemEquip.getPredestinedHeroId() != 0;
	}

	public int getEquipProperty(int equipId, int level) {
		SystemEquip systemEquip = systemEquipDao.get(equipId);
		int initAttack = systemEquip.getAttackInit() + (int) (systemEquip.getAttackGrowth() * level) + systemEquip.getAttackRatio();
		int initDefense = systemEquip.getDefenseInit() + (int) (systemEquip.getDefenseGrowth() * level) + systemEquip.getDefenseRatio();
		int initLife = systemEquip.getLifeInit() + (int) (systemEquip.getLifeGrowth() * level) + systemEquip.getLifeRatio();
		switch (systemEquip.getEquipType()) {
		case 1:
			return initAttack;
		case 2:
			return initDefense;

		case 3:
			return initLife;

		default:
			break;
		}
		return 0;
	}

	public EquipRefineSoul getEquipRefineSoul(String userId, String userEquipId, int equipId) {
		// 没有炼魂记录则插入
		EquipRefineSoul equipSoul = equipRefineSoulDao.getEquipRefineSoul(userEquipId, equipId);
		if (equipSoul == null) {
			EquipRefineSoul equipRefineSoul = new EquipRefineSoul();
			equipRefineSoul.setUserId(userId);
			equipRefineSoul.setEquipId(equipId);
			equipRefineSoul.setLuck(0);
			equipRefineSoul.setUserEquipId(userEquipId);
			equipRefineSoul.setCreatedTime(new Date());
			equipRefineSoulDao.addEquipRefineSoul(equipRefineSoul);
			equipSoul = equipRefineSoul;
		}
		return equipSoul;
	}

	@Override
	public EquipRefineSoulBO refineSoul(String userId, String userEquipId, int predestinedId) {

		List<DropDescBO> list = checkRefineSoulCondition(userId, userEquipId, predestinedId);

		// 扣除消耗
		for (DropDescBO bo : list) {
			if (bo.getToolType() == 2) {
				if (!userService.reduceCopper(userId, bo.getToolNum(), ToolUseType.REDUCE_REFINE_SOUL_COST)) {
					String message = "金币不足.userId[" + userId + "]]";
					throw new ServiceException(REFINE_SOUL_NOT_ENOUGH_COPPER, message);
				}
			} else {
				if (!toolService.reduceTool(userId, bo.getToolType(), bo.getToolId(), bo.getToolNum(), ToolUseType.REDUCE_REFINE_SOUL_COST)) {
					String message = "精炼消耗不足.userId[" + userId + "]]";
					throw new ServiceException(REFINE_SOUL_NOT_ENOUGH_TOOL, message);
				}
			}
		}

		EquipRefineSoul equipSoul = getEquipRefineSoul(userId, userEquipId, predestinedId);
		UserEquip userEquip = userEquipDao.get(userId, userEquipId);
		SystemEquip systemEquip = systemEquipDao.get(userEquip.getEquipId());
		SystemRefineSoulData soulData = systemRefineSoulDataDao.getByTypeSoul(systemEquip.getEquipType(), predestinedId == 0 ? 0 : 1);
		int addLuck = soulData.getAddLuck();
		// 计算是否成功
		int chancea = addLuck + equipSoul.getLuck() >= soulData.getMinLuck() ? 50 : 0;
		int chanceb = (addLuck + equipSoul.getLuck() - soulData.getMinLuck()) / addLuck * soulData.getChance();
		boolean success = RandomUtils.nextInt(100) < chancea + chanceb;
		EquipRefineSoulBO bo = new EquipRefineSoulBO();

		if (success) {
			// 随机炼魂
			if (predestinedId == 0) {
				Map<Integer, Integer> randomMap = new HashMap<Integer, Integer>();
				int weight = 0;
				List<SystemRefineSoulMap> mapList = getSimpleCareer(systemEquip, soulData.getMapId());
				for (SystemRefineSoulMap map : mapList) {
					weight += map.getWeight();
					randomMap.put(weight, map.getEquipId());
				}
				int preId = getEquipByWeight(randomMap);
				SystemEquip sysEquip = systemEquipDao.get(preId, systemEquip.getColor());
				if (sysEquip == null) {
					String message = "装备炼魂失败.preId[" + preId + "],]";
					throw new ServiceException(REFINE_SOUL_NOT_EQUIP, message);
				}
				int eqId = sysEquip.getEquipId();
				bo.setEquipId(eqId);
				userEquipDao.updateEquipId(userId, userEquipId, eqId);
			} else {
				SystemEquip sysEquip = systemEquipDao.get(predestinedId, systemEquip.getColor());
				if (sysEquip == null) {
					String message = "装备炼魂失败.preId[" + predestinedId + "],]";
					throw new ServiceException(REFINE_SOUL_NOT_EQUIP, message);
				}
				int equipId = sysEquip.getEquipId();
				bo.setEquipId(equipId);
				userEquipDao.updateEquipId(userId, userEquipId, equipId);
			}
			bo.setLuck(0);
			equipRefineSoulDao.delEquipRefineSoul(userEquipId, predestinedId);
		} else {
			equipRefineSoulDao.upEquipRefineSoul(userEquipId, predestinedId, addLuck);
			bo.setEquipId(0);
			bo.setLuck(addLuck + equipSoul.getLuck());
		}
		return bo;
	}

	/**
	 * 核对炼魂条件
	 * 
	 * @param userId
	 * @param userEquipId
	 * @param type
	 */
	public List<DropDescBO> checkRefineSoulCondition(String userId, String userEquipId, int equipId) {
		// 获得系统炼魂数据信息
		UserEquip userEquip = userEquipDao.get(userId, userEquipId);
		SystemEquip systemEquip = systemEquipDao.get(userEquip.getEquipId());

		SystemRefineSoulData soulData = systemRefineSoulDataDao.getByTypeSoul(systemEquip.getEquipType(), equipId == 0 ? 0 : 1);
		// 四星以上装备才能炼魂
		if (systemEquip.getEquipStar() <= 4) {
			String message = "装备星级不足.userId[" + userId + "],]";
			throw new ServiceException(REFINE_SOUL_NOT_ENOUGH_STAR_LEVEL, message);
		}
		// 已经是神兵
		if (isMagic(systemEquip)) {
			String message = "神兵不能炼魂.userId[" + userId + "],]";
			throw new ServiceException(REFINE_SOUL_MAX_LEVEL, message);
		}

		List<DropDescBO> list = DropDescHelper.parseDropTool(soulData.getCost());
		// 炼魂石不足
		for (DropDescBO bo : list) {
			if (bo.getToolType() == 2) {
				User user = userService.get(userId);
				if (user.getCopper() < bo.getToolNum()) {
					String message = "金币不足.userId[" + userId + "]]";
					throw new ServiceException(REFINE_SOUL_NOT_ENOUGH_COPPER, message);
				}
			} else {
				if (bo.getToolNum() > userToolDao.getUserToolNum(userId, bo.getToolId())) {
					String message = "精炼消耗不足.userId[" + userId + "]]";
					throw new ServiceException(REFINE_SOUL_NOT_ENOUGH_TOOL, message);
				}
			}
		}
		return list;
	}

	/**
	 * 根据权重获得equipid
	 * 
	 * @param randomMap
	 * @return
	 */
	public int getEquipByWeight(Map<Integer, Integer> randomMap) {
		List<Integer> list = new ArrayList<Integer>();
		for (Integer weight : randomMap.keySet()) {
			list.add(weight);
		}
		int random = RandomUtils.nextInt(Collections.max(list));
		Collections.sort(list);
		for (Integer weight : list) {
			if (random <= weight) {
				return randomMap.get(weight);
			}
		}
		return 0;
	}

	@Override
	public EquipEnchantBO enchantPre(String userId, String userEquipId) {
		UserEquip userEquip = getUserEquip(userId, userEquipId);
		SystemEquip systemEquip = getSysEquip(userEquip.getEquipId());
		User user = userService.get(userId);
		checkEnchantOpen(user, userEquipId, systemEquip);
		EquipEnchant equipEnchant = this.equipEnchantDao.getEquipEnchant(userId, userEquipId);
		EquipEnchantBO bo = new EquipEnchantBO(equipEnchant);
		SkillData skillData = this.skillDataDao.getSkillData(user.getSkill());
		bo.setCoin(skillData.getCoin());
		bo.setCost(skillData.getCrystal());

		return bo;
	}

	public void checkEnchantOpen(User user, String userEquipId, SystemEquip systemEquip) {
		// 用户等级不足45级
		if (user.getLevel() < 45) {
			String message = "用户等级不足45级.userId[" + user.getUserId() + "]";
			throw new ServiceException(ENCHANT_EQUIP_NOT_ENOUGH_LEVEL, message);
		}
		// 装备星级不足3级
		if (systemEquip.getEquipStar() < 3) {
			String message = "装备星级不足3级.userEquipId[" + userEquipId + "]";
			throw new ServiceException(ENCHANT_EQUIP_NOT_ENOUGH_STAR, message);
		}
	}

	/**
	 * 在最大值和最小值间随机一个数
	 * 
	 * @param lowwer
	 * @param upper
	 * @return
	 */
	public int randNum(int lowwer, int upper) {
		return RandomUtils.nextInt(upper - lowwer + 1) + lowwer;
	}

	@Override
	public EquipEnchantBO enchant(String userId, String userEquipId) {
		User user = userService.get(userId);
		UserEquip userEquip = getUserEquip(userId, userEquipId);
		SystemEquip systemEquip = getSysEquip(userEquip.getEquipId());

		checkEnchantOpen(user, userEquipId, systemEquip);

		int career = systemEquip.getCareer();
		SkillData skillData = skillDataDao.getSkillData(user.getSkill());
		// 扣除消耗
		int coin = skillData.getCoin();
		int crystal = skillData.getCrystal();

		int toolNum = this.userToolDao.getUserToolNum(userId, ToolId.TOOL_ID_CRYSTAL);
		if (toolNum < crystal) {
			String message = "水晶不足.userId[" + userId + "]";
			throw new ServiceException(ENCHANT_EQUIP_NOT_ENOUGH_CRYSTAL, message);
		}

		// 扣金币
		if (!this.userService.reduceCopper(userId, coin, ToolUseType.ENCHANT_EQUIP_COST)) {
			String message = "金币不足.userId[" + userId + "]";
			throw new ServiceException(ENCHANT_EQUIP_NOT_ENOUGH_COPPER, message);
		}
		// 扣水晶
		if (!toolService.reduceTool(userId, ToolType.MATERIAL, ToolId.TOOL_ID_CRYSTAL, crystal, ToolUseType.ENCHANT_EQUIP_COST)) {
			String message = "水晶不足.userId[" + userId + "]";
			throw new ServiceException(ENCHANT_EQUIP_NOT_ENOUGH_CRYSTAL, message);
		}

		// 增加熟练度
		this.userService.addSkill(userId, crystal, ToolUseType.ADD_ENCHANT_EQUIP);

		// 增加属性
		// 获得属性条数
		StringBuffer sb = new StringBuffer();
		int proNum = randNum(skillData.getPropertyNumLow(), skillData.getPropertyNumUp());
		for (int i = 0; i < proNum; i++) {
			// 获得属性类型
			int type = randNum(0, 8);
			// 获得属性品质
			int color = randNum(skillData.getPropertyColorLow(), skillData.getPropertyColorUp());

			EnchantProperty enchantProperty = enchantPropertyDao.getEnchantProperty(color, type);
			// 点化获得的属性值
			int value = getEnchantValue(enchantProperty, career);
			sb.append(type).append(":").append(color).append(":").append(value).append(",");
		}
		EquipEnchant equipEnchant = this.equipEnchantDao.getEquipEnchant(userId, userEquipId);
		if (equipEnchant == null) {
			equipEnchant = new EquipEnchant(userEquipId, userId, userEquip.getEquipId(), "", sb.toString());
		} else {
			equipEnchant.setEnProperty(sb.toString());
		}
		equipEnchantDao.updateEquipEnchant(equipEnchant);

		return enchantPre(userId, userEquipId);
	}

	/**
	 * 根据装备职业获得点化属性值
	 * 
	 * @param enchantProperty
	 * @param career
	 * @return
	 */
	public int getEnchantValue(EnchantProperty enchantProperty, int career) {
		switch (career) {
		case 0:
			return enchantProperty.getTank();

		case 1:
			return enchantProperty.getHurt();
		case 2:
			return enchantProperty.getMilk();

		case 3:
			return enchantProperty.getMilkTemple();

		default:
			return 0;

		}
	}

	@Override
	public EquipEnchantBO save(String userId, String userEquipId) {
		EquipEnchant equipEnchant = this.equipEnchantDao.getEquipEnchant(userId, userEquipId);
		if (equipEnchant == null) {
			String message = "没有点化属性.userEquipId[" + userEquipId + "]";
			throw new ServiceException(ENCHANT_NOT_EQUIP, message);
		}
		String enPro = equipEnchant.getEnProperty();
		// 没有点化属性
		if (StringUtils.isEmpty(enPro)) {
			String message = "没有点化属性.userEquipId[" + userEquipId + "]";
			throw new ServiceException(ENCHANT_NOT_EQUIP, message);
		}
		this.equipEnchantDao.updateEnchantProperty(userId, userEquipId, enPro);
		return enchantPre(userId, userEquipId);
	}
}
