package com.lodogame.ldsg.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.ConfigDataDao;
import com.lodogame.game.dao.SystemEquipDao;
import com.lodogame.game.dao.SystemFragmentDao;
import com.lodogame.game.dao.SystemHeroDao;
import com.lodogame.game.dao.SystemHeroSkillDao;
import com.lodogame.game.dao.SystemHeroUpgradeAddPropDao;
import com.lodogame.game.dao.SystemHeroUpgradeDao;
import com.lodogame.game.dao.SystemHeroUpgradeToolDao;
import com.lodogame.game.dao.SystemLevelExpDao;
import com.lodogame.game.dao.SystemToolDao;
import com.lodogame.game.dao.SystemUserLevelDao;
import com.lodogame.game.dao.SystemVipLevelDao;
import com.lodogame.game.dao.UpStarBreakConfigDao;
import com.lodogame.game.dao.UpStarHeroConfigDao;
import com.lodogame.game.dao.UpStarValueConfigDao;
import com.lodogame.game.dao.UserEquipDao;
import com.lodogame.game.dao.UserHeroDao;
import com.lodogame.game.dao.UserOnlyOneHeroDao;
import com.lodogame.game.dao.UserToolDao;
import com.lodogame.ldsg.bo.BattleHeroBO;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.DropToolBO;
import com.lodogame.ldsg.bo.MergeSkillBO;
import com.lodogame.ldsg.bo.UserEquipBO;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.bo.UserToolBO;
import com.lodogame.ldsg.constants.ConfigKey;
import com.lodogame.ldsg.constants.InitDefine;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.constants.ToolId;
import com.lodogame.ldsg.constants.ToolType;
import com.lodogame.ldsg.constants.ToolUseType;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.HeroAdvanceEvent;
import com.lodogame.ldsg.event.HeroUpdateEvent;
import com.lodogame.ldsg.event.HeroUpgradeEvent;
import com.lodogame.ldsg.event.XiaShiLevelEvent;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.helper.BOHelper;
import com.lodogame.ldsg.helper.HeroHelper;
import com.lodogame.ldsg.ret.SellHeroRet;
import com.lodogame.ldsg.service.DailyTaskService;
import com.lodogame.ldsg.service.EquipService;
import com.lodogame.ldsg.service.EventService;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.MeridianService;
import com.lodogame.ldsg.service.RobotService;
import com.lodogame.ldsg.service.ToolService;
import com.lodogame.ldsg.service.UnSynLogService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.ContestUserHero;
import com.lodogame.model.SystemDailyTask;
import com.lodogame.model.SystemEquip;
import com.lodogame.model.SystemFragment;
import com.lodogame.model.SystemHero;
import com.lodogame.model.SystemHeroSkill;
import com.lodogame.model.SystemHeroUpgrade;
import com.lodogame.model.SystemHeroUpgradeAddProp;
import com.lodogame.model.SystemHeroUpgradeTool;
import com.lodogame.model.SystemLevelExp;
import com.lodogame.model.SystemTool;
import com.lodogame.model.SystemUserLevel;
import com.lodogame.model.SystemVipLevel;
import com.lodogame.model.UpstarBreakConfig;
import com.lodogame.model.UpstarHeroConfig;
import com.lodogame.model.UpstarValueConfig;
import com.lodogame.model.User;
import com.lodogame.model.UserEquip;
import com.lodogame.model.UserExpeditionHero;
import com.lodogame.model.UserHero;
import com.lodogame.model.UserOnlyOneHero;
import com.lodogame.model.UserTool;

public class HeroServiceImpl implements HeroService {

	private static final Logger LOG = Logger.getLogger(HeroServiceImpl.class);

	private static final int HERO_ALIVE = 0;
	public static final int NO = 0;
	public static final int UP_LEVEl = 1;
	public static final int UP_STAR = 2;
	/**
	 * 用元宝点亮节点
	 */
	private static final int USE_GOLD_LIGHT_NODE = 2;

	@Autowired
	private SystemFragmentDao systemFragmentDao;

	@Autowired
	private SystemToolDao systemToolDao;

	@Autowired
	private SystemHeroUpgradeAddPropDao systemHeroUpgradeAddPropDao;

	@Autowired
	private SystemHeroUpgradeToolDao systemHeroUpgradeToolDao;

	@Autowired
	private UserToolDao userToolDao;

	@Autowired
	private UserHeroDao userHeroDao;

	@Autowired
	private UserOnlyOneHeroDao userOnlyOneHeroDao;

	@Autowired
	private UserHeroDao userHeroDaoMysqlImpl;

	@Autowired
	private SystemHeroDao systemHeroDao;

	@Autowired
	private UserEquipDao userEquipDao;

	@Autowired
	private SystemHeroUpgradeDao systemHeroUpgradeDao;

	@Autowired
	private SystemLevelExpDao systemLevelExpDao;

	@Autowired
	private UserService userService;

	@Autowired
	private SystemUserLevelDao systemUserLevelDao;

	@Autowired
	private EquipService equipService;

	@Autowired
	private EventService eventService;

	@Autowired
	private SystemHeroSkillDao systemHeroSkillDao;

	@Autowired
	private ToolService toolService;

	@Autowired
	private SystemEquipDao systemEquipDao;

	@Autowired
	private UnSynLogService unSynLogService;

	@Autowired
	private ConfigDataDao configDataDao;

	@Autowired
	private MeridianService meridianService;

	@Autowired
	private RobotService robotService;

	@Autowired
	private SystemVipLevelDao systemVipLevelDao;

	@Autowired
	private UpStarBreakConfigDao upStarBreakConfigDao;

	@Autowired
	private UpStarHeroConfigDao upStarHeroConfigDao;

	@Autowired
	private UpStarValueConfigDao upStarValueConfigDao;

	@Autowired
	private DailyTaskService dailyTaskService;

	@Override
	public List<UserHeroBO> getUserHeroList(String userId, int type) {

		List<UserHeroBO> userHeroBOList = new ArrayList<UserHeroBO>();

		List<UserHero> userHeroList = this.userHeroDao.getUserHeroList(userId);

		if (userHeroList == null || userHeroList.size() == 0) {
			return robotService.getRobotUserHeroBOList(userId);
		}

		for (UserHero userHero : userHeroList) {

			if ((userHero.getPos() > 0) || type == 0) {
				UserHeroBO userHeroBO = this.createUserHeroBO(userHero);
				userHeroBOList.add(userHeroBO);
			}
		}

		return userHeroBOList;
	}

	@Override
	public List<UserHeroBO> getUserHeroList(String userId, int type, int grade) {

		List<UserHeroBO> userHeroBOList = new ArrayList<UserHeroBO>();

		List<UserHero> userHeroList = this.userHeroDao.getUserHeroList(userId);

		for (UserHero userHero : userHeroList) {
			if (userHero.getHeroLevel() < grade) {
				continue;
			}
			if ((userHero.getPos() > 0) || type == 0) {
				UserHeroBO userHeroBO = this.createUserHeroBO(userHero);
				userHeroBOList.add(userHeroBO);
			}
		}

		return userHeroBOList;
	}

	@Override
	public List<UserHeroBO> createUserHeroBOList(String userId, List<DropToolBO> boList) {
		// TODO Auto-generated method stub
		return null;
	}

	public UserHeroBO getUserHeroBO(String userId, String userHeroId) {
		UserHero userHero = this.userHeroDao.get(userId, userHeroId);
		if (userHero != null) {
			UserHeroBO userHeroBO = this.createUserHeroBO(userHero);
			return userHeroBO;
		}
		return null;
	}

	/**
	 * 判断上场武将个数有没有超过限制
	 * 
	 * @param userId
	 */
	private void checkBattleHeroLimit(String userId) {

		User user = this.userService.get(userId);
		SystemUserLevel systemUserLevel = this.systemUserLevelDao.get(user.getLevel());
		if (systemUserLevel == null) {
			throw new ServiceException(ServiceReturnCode.FAILD, "数据异常，武将等级配置数据不存在.level[" + user.getLevel() + "]");
		}

		// 用户当前上场武将个数
		int userBattleCount = this.userHeroDao.getBattleHeroCount(userId);

		// 人数已经达到上阵限制
		if (userBattleCount >= systemUserLevel.getBattleNum()) {
			throw new ServiceException(CHANGE_POS_HERO_NUM_LIMIT, "布阵失败，上场武将个数超过限制.userBattleCount[" + userBattleCount + "]");
		}

	}

	/**
	 * 判断在阵上的武将是不是至少有一个
	 * 
	 * @param userId
	 */
	private void checkBattleHeroIsZero(String userId) {

		// 用户当前上场武将个数
		int userBattleCount = this.userHeroDao.getBattleHeroCount(userId);

		// 人数已经达到上阵限制
		if (userBattleCount <= 1) {
			throw new ServiceException(CHANGE_POS_HERO_NUM_IS_ZERO, "武将下阵失败，至少有一个武将在阵上.userBattleCount[" + userBattleCount + "]");
		}
	}

	/**
	 * 判断有没有相同的武将在阵上
	 * 
	 * @param userId
	 * @param userHeroId
	 */
	public void checkSameHeroOnEmbattle(String userId, String userHeroId, int pos) {

		UserHeroBO userHeroBO = this.getUserHeroBO(userId, userHeroId);

		List<UserHeroBO> userHeroBOList = this.getUserHeroList(userId, 1);

		for (UserHeroBO bo : userHeroBOList) {
			if (!userHeroBO.getUserHeroId().equals(bo.getUserHeroId()) && userHeroBO.getHeroId() == bo.getHeroId() && bo.getPos() != pos) {// 如果是同一人物开将，且不是换掉它，则是不允许的(如:同一个张飞只允许上一个)
				String message = "开将布阵失败，已经有相同的开将在阵上.userHeroId[" + userHeroId + "], userHeroId2[" + bo.getUserHeroId() + "]";
				throw new ServiceException(CHANGE_POS_SAME_HERO_EXIST, message);
			}
		}

	}

	public boolean changePos(String userId, String userHeroId, int pos, EventHandle handle) {

		LOG.debug("修改武将战斗站位.userId[" + userId + "], userHeroId[" + userHeroId + "], pos[" + pos + "]");

		UserHero userHero = this.get(userId, userHeroId);

		int oldPos = userHero.getPos();

		UserHero targetUserHero = null;
		if (pos > 0) {// 上阵
			targetUserHero = this.userHeroDaoMysqlImpl.getUserHeroByPos(userId, pos);
			if (oldPos == 0 && targetUserHero == null) {// 原来不在阵 上，判断人数有没有超
				this.checkBattleHeroLimit(userId);
			}

			// 判断是不是有相同的武将在阵上
			this.checkSameHeroOnEmbattle(userId, userHeroId, pos);

		} else {// 下阵，一定要最少有一个武将在阵上
			this.checkBattleHeroIsZero(userId);
		}
		// 修改的位置等于自己的位置的话,直接返回true
		if (oldPos == pos) {
			return true;
		}

		boolean success = this.userHeroDao.changePos(userId, userHeroId, pos);
		if (!success) {
			return success;
		}

		if (targetUserHero != null) {

			if (oldPos > 0) {// 如果武将原来是阵上的，则是两个人换位置
				this.userHeroDao.changePos(userId, targetUserHero.getUserHeroId(), oldPos);
			} else {
				this.userHeroDao.changePos(userId, targetUserHero.getUserHeroId(), 0);
				// 用户战斗力发生改变
				this.eventService.addUserPowerUpdateEvent(userId);
			}
			HeroUpdateEvent targetHeroUpdateEvent = new HeroUpdateEvent(userId, targetUserHero.getUserHeroId());
			handle.handle(targetHeroUpdateEvent);
		}

		HeroUpdateEvent heroUpdateEvent = new HeroUpdateEvent(userId, userHeroId);
		handle.handle(heroUpdateEvent);

		return success;
	}

	public UserHeroBO upgradePre(String userId, String userHeroId) {

		UserHero userHero = this.get(userId, userHeroId);

		int systemHeroId = userHero.getSystemHeroId();

		SystemHeroUpgrade systemHeroUpgrade = this.systemHeroUpgradeDao.get(systemHeroId);
		if (systemHeroUpgrade != null) {
			userHero.setSystemHeroId(systemHeroUpgrade.getUpgradeHeroId());
		}

		return this.createUserHeroBO(userHero);
	}

	public UserHeroBO upgrade(String userId, String userHeroId) {

		UserHero userHero = userHeroDao.get(userId, userHeroId);
		SystemHero systemHero = systemHeroDao.get(userHero.getSystemHeroId());
		if (userHero.getHeroLevel() < systemHero.getUpgradeLevel()) {
			String message = "武将进阶，武将等级不足 userHeroId[" + userHeroId + "]";
			throw new ServiceException(HeroService.UPGRADE_HERO_LEVEL_NOT_ENOUGH, message);
		}
		SystemHero sy = systemHeroDao.get(userHero.getSystemHeroId());
		int heroColor = sy.getHeroColor();
		int carrer = sy.getCareer();
		List<String> nodes = HeroHelper.getAllNodesByHeroColor(heroColor, carrer);
		if (userHero.isAllUpgradeNodesLighted(nodes) == false) {
			String message = "还有节点没有点亮，不可以升级userHeroId[" + userHeroId + "] nodes[" + userHero.getUpgradeNode() + "]";
			throw new ServiceException(HeroService.UPGRADE_NOT_ALL_NODES_LIGHTED, message);
		}
		int nextColorSystemHeroId = getNextColorSystemHero(userHero.getSystemHeroId());
		userHeroDao.update(userId, userHeroId, nextColorSystemHeroId, userHero.getHeroLevel(), userHero.getHeroExp());

		Event event = new HeroUpgradeEvent(userId, userHeroId);
		this.eventService.dispatchEvent(event);

		// 在升一级后，要将这一级点亮过的节点清除
		userHeroDao.updateUpgradeNode(userId, userHeroId, "");
		userHero = userHeroDao.get(userId, userHeroId);
		return createUserHeroBO(userHero);
	}

	/**
	 * 获取下一个品质的武将的系统武将id
	 */
	private int getNextColorSystemHero(int systemHeroId) {
		return this.systemHeroUpgradeDao.get(systemHeroId).getUpgradeHeroId();
	}

	@Override
	public CommonDropBO sell(String userId, List<String> userHeroIdList, EventHandle handle) {

		if (userHeroIdList == null || userHeroIdList.size() == 0) {
			String message = "数据异常，至少选择一名武将.userId[" + userId + "]";
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

		// 可删除验证
		List<UserHero> userHeroList = new ArrayList<UserHero>();
		for (String userHeroId : userHeroIdList) {
			UserHero userHero = this.checkDeleteAble(userId, userHeroId);
			userHeroList.add(userHero);
		}

		// 出售
		int sellCopper = 0;
		int sellMuhon = 0;
		int jiangshanOrder = 0;
		int mingwen = 0;
		int soul = 0;
		List<UserHero> beSellList = new ArrayList<UserHero>();
		for (String userHeroId : userHeroIdList) {
			SellHeroRet sellHeroRet = this.getSellInfo(userId, userHeroId, beSellList);
			sellCopper += sellHeroRet.getCopper();
			sellMuhon += sellHeroRet.getMuhon();
			jiangshanOrder += sellHeroRet.getJiangshanOrder();
			mingwen += sellHeroRet.getMingwen();
			soul += sellHeroRet.getSoul();
		}

		int rowCount = this.delete(userId, userHeroList, ToolUseType.REDUCE_SELL_HERO);

		List<UserToolBO> toolBOList = new ArrayList<UserToolBO>();
		if (rowCount == userHeroIdList.size()) {
			toolBOList = addSellProfit(userId, userHeroList, new SellHeroRet(sellCopper, sellMuhon, jiangshanOrder, mingwen, soul));

		} else {
			String message = "数据异常，出售武将失败.userId[" + userId + "], rowCount[" + rowCount + "], userHeroIdList.size[" + userHeroIdList.size() + "]";
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

		CommonDropBO dr = new CommonDropBO();
		dr.setUserToolBOList(toolBOList);

		return dr;
	}

	/**
	 * 给用户增加出售武将获得到东西
	 */
	private List<UserToolBO> addSellProfit(String userId, List<UserHero> userheros, SellHeroRet sellHeroRet) {
		this.userService.addCopper(userId, sellHeroRet.getCopper(), ToolUseType.ADD_SELL_HERO);
		this.userService.addMuhon(userId, sellHeroRet.getMuhon(), ToolUseType.ADD_SELL_HERO);
		this.userService.addSoul(userId, sellHeroRet.getSoul(), ToolUseType.ADD_SELL_HERO);
		this.userService.addMingwen(userId, sellHeroRet.getMingwen(), ToolUseType.ADD_SELL_HERO);
		this.toolService.addTool(userId, ToolType.MATERIAL, ToolId.JIANGSHAN_ORDER, sellHeroRet.getJiangshanOrder(), ToolUseType.ADD_SELL_HERO);

		Map<Integer, UserToolBO> lightNodeConsumeTools = calLightNodeConsumeTools(userheros);

		List<UserToolBO> toolBOList = toolService.giveTools(userId, lightNodeConsumeTools);

		UserToolBO toolJiangshanOrderBO = new UserToolBO(userId, ToolId.JIANGSHAN_ORDER, sellHeroRet.getJiangshanOrder(), ToolType.MATERIAL);
		if (sellHeroRet.getJiangshanOrder() != 0) {
			toolBOList.add(toolJiangshanOrderBO);
		}
		UserToolBO toolSellCopperBO = new UserToolBO(userId, ToolId.TOOL_COPPER_ID, sellHeroRet.getCopper(), ToolType.COPPER);
		if (sellHeroRet.getCopper() != 0) {
			toolBOList.add(toolSellCopperBO);
		}
		UserToolBO toolSellMuhonBO = new UserToolBO(userId, ToolType.MUHON, sellHeroRet.getMuhon(), ToolType.MUHON);
		if (sellHeroRet.getMuhon() != 0) {
			toolBOList.add(toolSellMuhonBO);
		}
		UserToolBO toolSellMingWenBO = new UserToolBO(userId, ToolType.MINGWEN, sellHeroRet.getMingwen(), ToolType.MINGWEN);
		if (sellHeroRet.getMingwen() != 0) {
			toolBOList.add(toolSellMingWenBO);
		}
		UserToolBO toolSellSoulBO = new UserToolBO(userId, ToolType.SOUL, sellHeroRet.getSoul(), ToolType.SOUL);
		if (sellHeroRet.getSoul() != 0) {
			toolBOList.add(toolSellSoulBO);
		}
		return toolBOList;

	}

	/**
	 * 计算某个武将点亮节点时消耗过的道具，在出售武将时将这些道具返回给用户
	 */
	private List<UserToolBO> calLightNodeConsumeTools(UserHero userHero) {
		Map<Integer, UserToolBO> toolBOs = new HashMap<Integer, UserToolBO>();
		List<SystemHeroUpgradeTool> upgradeTool = new ArrayList<SystemHeroUpgradeTool>();

		String[] lightedNodes = getLightedNodes(userHero);
		for (String node : lightedNodes) {
			List<SystemHeroUpgradeTool> tools = new ArrayList<SystemHeroUpgradeTool>();
			try {
				tools = systemHeroUpgradeToolDao.get(Integer.valueOf(node));
			} catch (Exception e) {
				e.printStackTrace();
				LOG.error("分解武将错误，upgradeNode[" + userHero.getUpgradeNode() + "], 节点[" + node + "]");
			}
			upgradeTool.addAll(tools);
		}

		for (SystemHeroUpgradeTool tool : upgradeTool) {
			UserToolBO userToolBO = new UserToolBO(userHero.getUserId(), tool.getToolId(), tool.getToolNum(), tool.getToolType());
			BOHelper.addUserToolBOToMap(userToolBO, toolBOs);
		}

		List<UserToolBO> list = new ArrayList<UserToolBO>();
		Iterator<Entry<Integer, UserToolBO>> iterator = toolBOs.entrySet().iterator();
		while (iterator.hasNext()) {
			UserToolBO toolBO = iterator.next().getValue();
			list.add(toolBO);
		}

		return list;
	}

	private Map<Integer, UserToolBO> calLightNodeConsumeTools(List<UserHero> userHeros) {
		Map<Integer, UserToolBO> toolBOs = new HashMap<Integer, UserToolBO>();

		for (UserHero userHero : userHeros) {
			List<UserToolBO> toolBOList = calLightNodeConsumeTools(userHero);

			for (UserToolBO bo : toolBOList) {
				UserToolBO userToolBO = toolBOs.get(bo.getToolId());
				if (userToolBO != null) {
					userToolBO.setToolNum(bo.getToolNum() + userToolBO.getToolNum());
				} else {
					userToolBO = new UserToolBO(userHero.getUserId(), bo.getToolId(), bo.getToolNum(), bo.getToolType());
					toolBOs.put(bo.getToolId(), userToolBO);
				}
			}
		}

		return toolBOs;
	}

	/**
	 * 获取某个武将所有点亮过的节点
	 */
	private String[] getLightedNodes(UserHero userHero) {
		String upgradeNode = userHero.getUpgradeNode();
		SystemHero systemHero = systemHeroDao.get(userHero.getSystemHeroId());
		if (systemHero == null) {
			LOG.error("系统武将不存在.systemHeroId[" + userHero.getSystemHeroId() + "]");
			return new String[] {};
		}

		int heroColor = systemHero.getHeroColor();

		if (StringUtils.isEmpty(upgradeNode) && heroColor == 0) {
			return new String[0];
		}

		// 该武将下一阶段的品质
		int nextColor = 0;
		if (StringUtils.isEmpty(upgradeNode) == false) {
			// 如果这个武将正在点亮第一个阶段的节点，例如已经点亮了 101,102,103 则表示他即将要进阶的品质是1，即 nextColor
			// 值为1
			nextColor = Integer.valueOf(String.valueOf(upgradeNode.trim().charAt(0)));
		} else {
			// 如果这个武将一开始就是高品质武将，例如品质是2，则他下一阶段的品质是3
			nextColor = heroColor + 1;
		}

		// 如果 nextColor 为1，说明这个武将一开始的品质是0，即他一定点亮过若干个节点
		if (nextColor == 1) {
			return upgradeNode.split(",");
		}

		int career = systemHero.getCareer();

		StringBuffer buff = new StringBuffer();
		for (int i = 1; i < nextColor; i++) {
			buff.append(i + "" + career + "1,");
			buff.append(i + "" + career + "2,");
			buff.append(i + "" + career + "3,");
			buff.append(i + "" + career + "4,");
			buff.append(i + "" + career + "5,");
			buff.append(i + "" + career + "6,");
		}

		return buff.append(upgradeNode).toString().split(",");
	}

	public int delete(String userId, List<UserHero> userHeroList, int useType) {

		List<String> userHeroIdList = new ArrayList<String>();
		for (UserHero userHero : userHeroList) {
			userHeroIdList.add(userHero.getUserHeroId());

			List<UserEquip> list = this.userEquipDao.getHeroEquipList(userId, userHero.getUserHeroId());
			if (list != null && !list.isEmpty()) {
				for (UserEquip userEquip : list) {
					this.userEquipDao.updateEquipHero(userId, userEquip.getUserEquipId(), null);
				}
			}

			this.unSynLogService.heroLog(userId, userHero.getUserHeroId(), userHero.getSystemHeroId(), useType, -1, userHero.getHeroExp(), userHero.getHeroLevel());
		}

		int count = 0;

		count = this.userHeroDao.delete(userId, userHeroIdList);

		return count;
	}

	private SellHeroRet getSellInfo(String userId, String userHeroId, List<UserHero> beSellList) {

		UserHero userHero = this.get(userId, userHeroId);

		if (userHero == null) {
			return new SellHeroRet(0, 0, 0, 0, 0);
		}

		SystemHero systemHero = this.systemHeroDao.get(userHero.getSystemHeroId());
		if (systemHero == null) {
			return new SellHeroRet(0, 0, 0, 0, 0);
		}

		int level = userHero.getHeroLevel();

		int copper = 0;

		// int muhon = HeroHelper.getSellMuhon(level, systemHero.getHeroStar());
		int muhon = (int) (getAdvanceTotalNeedMuhon(level) * 0.9);
		int jiangshanOrder = systemHero.getJiangshanOrder();
		int mingwen = meridianService.getSellMingwen(userId, userHeroId);
		int soul = meridianService.getSellSoul(userId, userHeroId);

		int meridianMuhon = meridianService.getSellMuhon(userId, userHeroId);

		if (beSellList != null) {
			beSellList.add(userHero);
		}
		return new SellHeroRet(copper, muhon + meridianMuhon, jiangshanOrder, mingwen, soul);

	}

	/**
	 * 判断武将是否可以删除(没有上阵，没有装备，没有兵符)
	 * 
	 * @param userId
	 */
	private UserHero checkDeleteAble(String userId, String userHeroId) {

		UserHero userHero = this.get(userId, userHeroId);

		if (!StringUtils.equalsIgnoreCase(userHero.getUserId(), userId)) {
			String message = "武将不可删除,武将所有人不符.userId[" + userId + "], userHeroId[" + userHeroId + "]";
			LOG.info(message);
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

		if (userHero.getPos() > 0) {
			String message = "武将不可删除,武将在阵上.userId[" + userId + "], userHeroId[" + userHeroId + "]";
			LOG.info(message);
			throw new ServiceException(DELETE_HERO_IS_IN_EMBATTLE, message);
		}

		if (userHero.getLockStatus() > 0) {
			String message = "武将不可删除,所消耗的武将已被锁定.userId[" + userId + "]";
			LOG.info(message);
			throw new ServiceException(HERO_HAS_LOCKED, message);
		}

		return userHero;
	}

	/**
	 * 获取系统武将
	 * 
	 * @param systemHeroId
	 * @return
	 */
	public SystemHero getSysHero(int systemHeroId) {

		SystemHero systemHero = this.systemHeroDao.get(systemHeroId);
		if (systemHero == null) {
			String message = "获取系统武将失败,系统武将不存在.systemHeroId[" + systemHeroId + "]";
			LOG.error(message);
			throw new ServiceException(HERO_NOT_EXIST, message);
		}
		return systemHero;
	}

	public UserHeroBO createUserHeroBO(UserExpeditionHero hero, boolean self) {
		UserHero userHero = new UserHero();
		userHero.setUserHeroId(hero.getUserHeroId());
		userHero.setUserId(hero.getUserId());
		userHero.setSystemHeroId(hero.getSystemHeroId());
		userHero.setHeroExp(hero.getHeroExp());
		userHero.setHeroLevel(hero.getHeroLevel());
		userHero.setPos(hero.getPos());
		userHero.setBloodSacrificeStage(hero.getBloodSacrificeStage());
		userHero.setLockStatus(hero.getLockStatus());
		userHero.setDeifyNodeLevel(hero.getDeifyNodeLevel());
		userHero.setUpgradeNode(hero.getUpgradeNode() == null ? "" : hero.getUpgradeNode());
		userHero.setStarLevel(hero.getStarLevel());
		userHero.setStarExp(hero.getStarExp());
		// BeanUtils.copyProperties(hero, userHero);
		UserHeroBO bo = createUserHeroBO(userHero);
		int maxLife = hero.getMaxLife();

		int addLife = 0;

		if (maxLife == 0) {
			maxLife = bo.getLife();
		} else {
			if (self) {
				int newMaxLife = bo.getLife();
				if (newMaxLife > maxLife) {
					addLife = newMaxLife - maxLife;
					maxLife = newMaxLife;
				}

			}
		}

		int life = hero.getLife();
		if (life > 0 && addLife > 0) {
			life = life + addLife;
		}
		if (life > maxLife) {
			life = maxLife;
		}

		bo.setMaxLife(maxLife);
		bo.setLife(life);
		bo.setRage(hero.getRage());

		if (hero.getAttack() > 0) {
			// 不是自己,则用快照
			if (!self) {
				bo.setPhysicalAttack(hero.getAttack());
			}
		}
		if (hero.getDefense() > 0) {
			if (!self) {
				bo.setPhysicalDefense(hero.getDefense());
			}
		}

		return bo;
	}

	public UserHeroBO createUserHeroBO(UserHero userHero) {

		UserHeroBO userHeroBO = new UserHeroBO();
		BeanUtils.copyProperties(userHero, userHeroBO);

		int level = userHero.getHeroLevel();

		SystemHero systemHero = this.systemHeroDao.get(userHero.getSystemHeroId());

		if (systemHero != null) {
			userHeroBO.setCanUpgrade(systemHero.getCanUpgrade());
			userHeroBO.setUpgradeLevel(systemHero.getUpgradeLevel());
			userHeroBO.setName(systemHero.getHeroName());
			userHeroBO.setImgId(systemHero.getImgId());
			userHeroBO.setCardId(systemHero.getCardId());
			// 技能
			userHeroBO.setPlan(systemHero.getPlan());
			userHeroBO.setNormalPlan(systemHero.getNormalPlan());
			userHeroBO.setSkill1(systemHero.getSkill1());
			userHeroBO.setSkill2(systemHero.getSkill2());
			userHeroBO.setSkill3(systemHero.getSkill3());
			userHeroBO.setSkill4(systemHero.getSkill4());
			// 职业
			userHeroBO.setCareer(systemHero.getCareer());
			userHeroBO.setHeroId(systemHero.getHeroId());
			userHeroBO.setSellCopper(0);
			userHeroBO.setSellExploits((int) (getAdvanceTotalNeedMuhon(level) * 0.9));
			userHeroBO.setSellJiangshanOrder(systemHero.getJiangshanOrder());

			userHeroBO.setStarPoint(systemHero.getStarPoint());

			// （初始值+成长值*等级）/100
			int attack = systemHero.getAttackInit() + (systemHero.getAttackGrowth() * level);
			int life = systemHero.getLifeInit() + (systemHero.getLifeGrowth() * level);
			int defense = systemHero.getDefenseInit() + (systemHero.getDefenseGrowth() * level);

			life = life == 0 ? 1 : life;
			defense = defense == 0 ? 1 : defense;
			attack = attack == 0 ? 1 : attack;

			userHeroBO.setLife(life);
			userHeroBO.setPhysicalDefense(defense);
			userHeroBO.setPhysicalAttack(attack);

		}

		// 计算进阶后加成
		String[] upgradeNodes = getLightedNodes(userHero);

		for (String node : upgradeNodes) {
			SystemHeroUpgradeAddProp prop = systemHeroUpgradeAddPropDao.get(Integer.valueOf(node));
			userHeroBO.addUpgradeProp(prop);
		}

		// 经脉
		int[] v = meridianService.getAddVal(userHero);
		userHeroBO.addMeridian(v);

		// 星级加成
		int starLevel = userHero.getStarLevel();
		UpstarHeroConfig h = this.upStarHeroConfigDao.get(systemHero.getHeroId());
		if (starLevel < h.getInitialLevel()) {
			LOG.error("脏数据.userId[" + userHero.getUserId() + "], userHeroId[" + userHero.getUserHeroId() + "]");
			starLevel = h.getInitialLevel();
		}

		UpstarValueConfig config = this.upStarValueConfigDao.get(starLevel, systemHero.getCareer());
		UpstarValueConfig next = this.upStarValueConfigDao.get(starLevel + 2, systemHero.getCareer()); // 取下两阶

		UpstarValueConfig baseConfig = this.upStarValueConfigDao.get(h.getInitialLevel(), systemHero.getCareer());
		if (config != null) {
			int addAtt = config.getAttAdd() - baseConfig.getAttAdd();
			int addDef = config.getDefAdd() - baseConfig.getDefAdd();
			int addLife = config.getPowerAdd() - baseConfig.getPowerAdd();
			userHeroBO.setPhysicalAttack(userHeroBO.getPhysicalAttack() + addAtt);
			userHeroBO.setPhysicalDefense(userHeroBO.getPhysicalDefense() + addDef);
			userHeroBO.setLife(userHeroBO.getLife() + addLife);
			if (next != null) {
				int nextAtt = next.getAttAdd() - config.getAttAdd();
				int nextDef = next.getDefAdd() - config.getDefAdd();
				int nextPower = next.getPowerAdd() - config.getPowerAdd();

				userHeroBO.setStarNextAtt((int) (nextAtt * next.getRatio() / 1000.0 + userHeroBO.getPhysicalAttack() * (next.getRatio() - config.getRatio()) / 1000));
				userHeroBO.setStarNextDef((int) (nextDef * next.getRatio() / 1000.0 + userHeroBO.getPhysicalDefense() * (next.getRatio() - config.getRatio()) / 1000));
				userHeroBO.setStarNextPower((int) (nextPower * next.getRatio() / 1000.0 + userHeroBO.getLife() * (next.getRatio() - config.getRatio()) / 1000));
			}

			userHeroBO.setPhysicalAttack((int) (userHeroBO.getPhysicalAttack() * config.getRatio() / 1000.0));
			userHeroBO.setPhysicalDefense((int) (userHeroBO.getPhysicalDefense() * config.getRatio() / 1000.0));
			userHeroBO.setLife((int) (userHeroBO.getLife() * config.getRatio() / 1000.0));

			userHeroBO.setPhysicalAttack(userHeroBO.getPhysicalAttack() + baseConfig.getAttBase());
			userHeroBO.setPhysicalDefense(userHeroBO.getPhysicalDefense() + baseConfig.getDefBase());
			userHeroBO.setLife(userHeroBO.getLife() + baseConfig.getPowerBase());

		} else {
			LOG.error("武将星级配置不存在.strtLevel[" + userHero.getStarLevel() + "], career[" + systemHero.getCareer() + "]");
		}

		// 计算装备的加成
		List<UserEquipBO> userEquipBOList = this.equipService.getUserHeroEquipList(userHero.getUserId(), userHero.getUserHeroId());

		float[] addRatio = new float[9];
		for (UserEquipBO userEquipBO : userEquipBOList) {
			HeroHelper.addRatio(addRatio, userEquipBO.getAddRatio());
		}

		for (UserEquipBO userEquipBO : userEquipBOList) {

			SystemEquip systemEquip = this.systemEquipDao.get(userEquipBO.getEquipId());

			userEquipBO.calcTotalValue(systemEquip, addRatio);

			userHeroBO.addEqui(userEquipBO.getAddVal());
			userHeroBO.addRefineEqui(userEquipBO.getAddRefineVal());
			userHeroBO.addEnchantEqui(userEquipBO.getAddEnchantVal());
			userHeroBO.setLife(userHeroBO.getLife() + userEquipBO.getLife());
			userHeroBO.setPhysicalAttack(userHeroBO.getPhysicalAttack() + userEquipBO.getAttack());
			userHeroBO.setPhysicalDefense(userHeroBO.getPhysicalDefense() + userEquipBO.getDefense());
		}

		addGroupSkillProperty(userHeroBO);
		addEquipSkillProerty(userHeroBO);
		userHeroBO.setMaxLife(userHeroBO.getLife());

		return userHeroBO;
	}

	/**
	 * 添加英雄的缘分属性
	 */
	public void addGroupSkillProperty(UserHeroBO userHeroBO) {
		if (userHeroBO.getPos() == 0) {
			return;
		}
		List<UserHero> list = this.userHeroDao.getUserHeroList(userHeroBO.getUserId());

		Map<Integer, UserHero> map = new HashMap<Integer, UserHero>();
		for (UserHero userHero : list) {
			SystemHero systemHero = this.systemHeroDao.get(userHero.getSystemHeroId());
			if (userHero.getPos() > 0) {
				map.put(systemHero.getHeroId(), userHero);
			}
		}

		SystemHero systemHero = this.systemHeroDao.get(userHeroBO.getSystemHeroId());
		List<SystemHeroSkill> heroSkillList = this.systemHeroSkillDao.getHeroSkillList(systemHero.getHeroId());
		for (SystemHeroSkill systemHeroSkill : heroSkillList) {

			if (systemHeroSkill.getEquipId() > 0) {
				continue;
			}
			List<Integer> needHeroIdList = new ArrayList<Integer>();
			needHeroIdList.add(systemHeroSkill.getNeedHeroId1());
			needHeroIdList.add(systemHeroSkill.getNeedHeroId2());
			needHeroIdList.add(systemHeroSkill.getNeedHeroId3());
			needHeroIdList.add(systemHeroSkill.getNeedHeroId4());
			needHeroIdList.add(systemHeroSkill.getNeedHeroId5());

			boolean checkPass = true;

			for (int needHeroId : needHeroIdList) {

				if (needHeroId > 0) {

					if (!map.containsKey(needHeroId)) {
						checkPass = false;
						break;
					}
				}
			}
			if (checkPass) {
				userHeroBO.addGroupSkillProerty(systemHeroSkill.getType1(), systemHeroSkill.getValue1());
				userHeroBO.addGroupSkillProerty(systemHeroSkill.getType2(), systemHeroSkill.getValue2());
			}
		}
	}

	/**
	 * 添加装备缘分属性
	 * 
	 * @param hero
	 */
	private void addEquipSkillProerty(UserHeroBO userHeroBO) {
		List<UserEquipBO> list = equipService.getUserHeroEquipList(userHeroBO.getUserId(), userHeroBO.getUserHeroId());

		List<Integer> it = new ArrayList<Integer>();
		for (UserEquipBO t : list) {
			SystemEquip se = equipService.getSysEquip(t.getEquipId());
			it.add(se.getPredestinedId());
		}
		SystemHero systemHero = this.systemHeroDao.get(userHeroBO.getSystemHeroId());
		List<SystemHeroSkill> heroSkillList = this.systemHeroSkillDao.getHeroSkillList(systemHero.getHeroId());
		for (SystemHeroSkill skill : heroSkillList) {
			int equiId = skill.getEquipId();
			if (equiId == 0)
				continue;

			if (it.contains(equiId)) {
				if (skill.getEquipIdType() == SystemHeroSkill.EQUIP_SKILL_TYPE_BEIDONG) {
					userHeroBO.addGroupSkillProerty(skill.getType1(), skill.getValue1());
					userHeroBO.addGroupSkillProerty(skill.getType2(), skill.getValue2());
					LOG.debug("添加装备缘分技能.skillId[" + skill.getSkillId() + "]");
				}
			}
		}

	}

	public UserHero get(String userId, String userHeroId) {
		UserHero userHero = this.userHeroDao.get(userId, userHeroId);
		if (userHero == null) {
			String message = "获取用户武将失败,用户武将不存在.userHeroId[" + userHeroId + "]";
			LOG.error(message);
			throw new ServiceException(HERO_NOT_EXIST, message);
		}
		return userHero;
	}

	public List<BattleHeroBO> getUserBattleHeroBOList(String userId) {

		List<BattleHeroBO> battleHeroBOList = new ArrayList<BattleHeroBO>();

		List<UserHeroBO> userHeroBOList = this.getUserHeroList(userId, 2);

		// 阵上武将ID列表
		Map<Integer, UserHeroBO> battleHeroIdMap = new HashMap<Integer, UserHeroBO>();

		for (UserHeroBO userHeroBO : userHeroBOList) {
			BattleHeroBO battleHeroBO = BOHelper.createBattleHeroBO(userHeroBO);
			battleHeroBOList.add(battleHeroBO);

			SystemHero systemHero = this.getSysHero(userHeroBO.getSystemHeroId());
			battleHeroIdMap.put(systemHero.getHeroId(), userHeroBO);

			if (userHeroBO.getSuitSkillId() > 0) {
				battleHeroBO.getSkillList().add(userHeroBO.getSuitSkillId());
			}
		}

		addGroupSkill(battleHeroBOList, battleHeroIdMap);
		equipPredestinedSkill(battleHeroBOList, userId);
		return battleHeroBOList;
	}

	public List<BattleHeroBO> getUserOnlyOneBattleHeroBOList(String userId) {

		List<BattleHeroBO> battleHeroBOList = new ArrayList<BattleHeroBO>();

		List<UserOnlyOneHero> userOnlyOneHeros = this.userOnlyOneHeroDao.getPosHeros(userId);
		List<UserHeroBO> userHeroBOList = new ArrayList<UserHeroBO>();
		for (UserOnlyOneHero userOnlyOneHero : userOnlyOneHeros) {
			UserHeroBO userHeroBO = getUserHeroBO(userId, userOnlyOneHero.getUserHeroId());
			if (userHeroBO == null) {
				continue;
			}
			userHeroBO.setPos(userOnlyOneHero.getPos());
			userHeroBO.setLife(userOnlyOneHero.getLife());
			userHeroBO.setRage(userOnlyOneHero.getMorale());
			userHeroBOList.add(userHeroBO);
		}
		// 阵上武将ID列表
		Map<Integer, UserHeroBO> battleHeroIdMap = new HashMap<Integer, UserHeroBO>();

		Set<Integer> posSet = new HashSet<Integer>();

		for (UserHeroBO userHeroBO : userHeroBOList) {

			if (posSet.contains(userHeroBO.getPos())) {
				LOG.error("用户千人斩阵容数据异常.userId[" + userId + "]");
				continue;
			} else {
				posSet.add(userHeroBO.getPos());
			}

			BattleHeroBO battleHeroBO = BOHelper.createBattleHeroBO(userHeroBO);
			battleHeroBOList.add(battleHeroBO);
			SystemHero systemHero = this.getSysHero(userHeroBO.getSystemHeroId());
			battleHeroIdMap.put(systemHero.getHeroId(), userHeroBO);
			if (userHeroBO.getSuitSkillId() > 0) {
				battleHeroBO.getSkillList().add(userHeroBO.getSuitSkillId());
			}
		}

		addGroupSkill(battleHeroBOList, battleHeroIdMap);
		equipPredestinedSkill(battleHeroBOList, userId);
		return battleHeroBOList;
	}

	public List<BattleHeroBO> getUserArenaBattleHeroBOList(List<UserHeroBO> userHeroBOList, String userId) {

		List<BattleHeroBO> battleHeroBOList = new ArrayList<BattleHeroBO>();

		// 阵上武将ID列表
		Map<Integer, UserHeroBO> battleHeroIdMap = new HashMap<Integer, UserHeroBO>();

		for (UserHeroBO userHeroBO : userHeroBOList) {
			BattleHeroBO battleHeroBO = BOHelper.createBattleHeroBO(userHeroBO);
			battleHeroBOList.add(battleHeroBO);

			SystemHero systemHero = this.getSysHero(userHeroBO.getSystemHeroId());
			battleHeroIdMap.put(systemHero.getHeroId(), userHeroBO);

			if (userHeroBO.getSuitSkillId() > 0) {
				battleHeroBO.getSkillList().add(userHeroBO.getSuitSkillId());
			}
		}

		addGroupSkill(battleHeroBOList, battleHeroIdMap);
		equipPredestinedSkill(battleHeroBOList, userId);
		return battleHeroBOList;
	}

	public List<BattleHeroBO> getUserBattleHeroBOList(List<ContestUserHero> list) {

		List<BattleHeroBO> battleHeroBOList = new ArrayList<BattleHeroBO>();

		List<UserHeroBO> userHeroBOList = new ArrayList<UserHeroBO>();
		for (ContestUserHero contestUserHero : list) {

			UserHeroBO userHeroBO = new UserHeroBO();

			SystemHero systemHero = this.getSysHero(contestUserHero.getSystemHeroId());

			userHeroBO.setCanUpgrade(systemHero.getCanUpgrade());
			userHeroBO.setUpgradeLevel(systemHero.getUpgradeLevel());
			userHeroBO.setName(systemHero.getHeroName());
			userHeroBO.setImgId(systemHero.getImgId());
			userHeroBO.setCardId(systemHero.getCardId());
			// 技能
			userHeroBO.setPlan(systemHero.getPlan());
			userHeroBO.setNormalPlan(systemHero.getNormalPlan());
			userHeroBO.setSkill1(systemHero.getSkill1());
			userHeroBO.setSkill2(systemHero.getSkill2());
			userHeroBO.setSkill3(systemHero.getSkill3());
			userHeroBO.setSkill4(systemHero.getSkill4());
			// 职业
			userHeroBO.setCareer(systemHero.getCareer());
			userHeroBO.setHeroId(systemHero.getHeroId());

			userHeroBO.setBogey(contestUserHero.getBogey());
			userHeroBO.setCrit(contestUserHero.getCrit());
			userHeroBO.setDodge(contestUserHero.getDodge());
			userHeroBO.setHeroLevel(contestUserHero.getLevel());
			userHeroBO.setHit(contestUserHero.getHit());
			userHeroBO.setLife(contestUserHero.getLife());
			userHeroBO.setParry(contestUserHero.getParry());
			userHeroBO.setPhysicalAttack(contestUserHero.getAttack());
			userHeroBO.setPhysicalDefense(contestUserHero.getDefense());
			userHeroBO.setSystemHeroId(contestUserHero.getSystemHeroId());
			userHeroBO.setPos(contestUserHero.getPos());
			userHeroBO.setToughness(contestUserHero.getToughness());

			userHeroBOList.add(userHeroBO);
		}

		// 阵上武将ID列表
		Map<Integer, UserHeroBO> battleHeroIdMap = new HashMap<Integer, UserHeroBO>();

		for (UserHeroBO userHeroBO : userHeroBOList) {
			BattleHeroBO battleHeroBO = BOHelper.createBattleHeroBO(userHeroBO);
			battleHeroBOList.add(battleHeroBO);

			SystemHero systemHero = this.getSysHero(userHeroBO.getSystemHeroId());
			battleHeroIdMap.put(systemHero.getHeroId(), userHeroBO);

			if (userHeroBO.getSuitSkillId() > 0) {
				battleHeroBO.getSkillList().add(userHeroBO.getSuitSkillId());
			}
		}

		addGroupSkill(battleHeroBOList, battleHeroIdMap);
		if (list.size() > 0) {
			equipPredestinedSkill(battleHeroBOList, list.get(0).getUserId());
		}
		return battleHeroBOList;
	}

	public List<BattleHeroBO> getUserBattleHeroBOList(List<UserExpeditionHero> hero, boolean self) {
		List<BattleHeroBO> battleHeroBOList = new ArrayList<BattleHeroBO>();

		List<UserHeroBO> userHeroBOList = new ArrayList<UserHeroBO>();
		for (UserExpeditionHero temp : hero) {
			userHeroBOList.add(createUserHeroBO(temp, self));
		}

		// 阵上武将ID列表
		Map<Integer, UserHeroBO> battleHeroIdMap = new HashMap<Integer, UserHeroBO>();

		for (UserHeroBO userHeroBO : userHeroBOList) {
			BattleHeroBO battleHeroBO = BOHelper.createBattleHeroBO(userHeroBO);
			battleHeroBOList.add(battleHeroBO);

			SystemHero systemHero = this.getSysHero(userHeroBO.getSystemHeroId());
			battleHeroIdMap.put(systemHero.getHeroId(), userHeroBO);

			if (userHeroBO.getSuitSkillId() > 0) {
				battleHeroBO.getSkillList().add(userHeroBO.getSuitSkillId());
			}
		}

		addGroupSkill(battleHeroBOList, battleHeroIdMap);
		if (hero.size() > 0) {
			equipPredestinedSkill(battleHeroBOList, hero.get(0).getUserId());
		}
		return battleHeroBOList;
	}

	/**
	 * 装备缘分技能
	 * 
	 * @param hero
	 */
	private void equipPredestinedSkill(List<BattleHeroBO> hero, String userId) {
		for (BattleHeroBO battleHero : hero) {
			String userHeroId = battleHero.getUserHeroId();
			List<UserEquipBO> list = equipService.getUserHeroEquipList(userId, userHeroId);

			List<Integer> it = new ArrayList<Integer>();
			for (UserEquipBO t : list) {
				SystemEquip se = equipService.getSysEquip(t.getEquipId());
				it.add(se.getPredestinedId());
			}

			SystemHero systemHero = this.systemHeroDao.get(battleHero.getSystemHeroId());
			List<SystemHeroSkill> heroSkillList = this.systemHeroSkillDao.getHeroSkillList(systemHero.getHeroId());
			for (SystemHeroSkill skill : heroSkillList) {
				int equiId = skill.getEquipId();
				if (equiId == 0)
					continue;

				if (it.contains(equiId)) {
					if (skill.getEquipIdType() == SystemHeroSkill.EQUIP_SKILL_TYPE_BEIDONG) {
						battleHero.getSkillList().add(skill.getSkillId());
					} else {
						battleHero.setPlan(skill.getSkillId());
					}
					LOG.debug("添加装备缘分技能.skillId[" + skill.getSkillId() + "]");
				}
			}
		}
	}

	private void addGroupSkill(List<BattleHeroBO> battleHeroBOList, Map<Integer, UserHeroBO> battleHeroIdMap) {
		// 设置组合技
		for (BattleHeroBO battleHeroBO : battleHeroBOList) {

			SystemHero systemHero = this.systemHeroDao.get(battleHeroBO.getSystemHeroId());
			List<SystemHeroSkill> heroSkillList = this.systemHeroSkillDao.getHeroSkillList(systemHero.getHeroId());
			for (SystemHeroSkill systemHeroSkill : heroSkillList) {

				if (systemHeroSkill.getEquipId() > 0) {
					continue;
				}

				MergeSkillBO mergeSkillBO = new MergeSkillBO();

				List<Integer> posList = new ArrayList<Integer>();

				List<Integer> needHeroIdList = new ArrayList<Integer>();
				needHeroIdList.add(systemHeroSkill.getNeedHeroId1());
				needHeroIdList.add(systemHeroSkill.getNeedHeroId2());
				needHeroIdList.add(systemHeroSkill.getNeedHeroId3());
				needHeroIdList.add(systemHeroSkill.getNeedHeroId4());
				needHeroIdList.add(systemHeroSkill.getNeedHeroId5());

				boolean checkPass = true;

				for (int needHeroId : needHeroIdList) {

					if (needHeroId > 0) {

						if (!battleHeroIdMap.containsKey(needHeroId)) {
							checkPass = false;
							break;
						} else {
							posList.add(battleHeroIdMap.get(needHeroId).getPos());
						}
					}
				}

				if (!checkPass) {
					continue;
				}

				mergeSkillBO.setPosList(posList);
				mergeSkillBO.setSkillId(systemHeroSkill.getSkillId());

				LOG.debug("添加组合技.heroId[" + systemHeroSkill.getHeroId() + "], skillId[" + systemHeroSkill.getSkillId() + "]");
				if (systemHeroSkill.getEquipIdType() == SystemHeroSkill.EQUIP_SKILL_TYPE_BEIDONG) {
					battleHeroBO.addMergeSkill(mergeSkillBO);
				} else {
					battleHeroBO.setPlan(mergeSkillBO.getSkillId());
				}
			}
		}
	}

	public boolean addUserHero(String userId, String userHeroId, final int systemHeroId, int pos, int useType) {

		Date now = new Date();

		SystemHero systemHero = this.getSysHero(systemHeroId);

		UpstarHeroConfig h = this.upStarHeroConfigDao.get(systemHero.getHeroId());

		UserHero userHero = new UserHero();
		userHero.setCreatedTime(now);
		userHero.setHeroExp(0);
		userHero.setHeroLevel(1);
		userHero.setSystemHeroId(systemHeroId);
		userHero.setPos(pos);
		userHero.setUpdatedTime(now);
		userHero.setUserId(userId);
		userHero.setUserHeroId(userHeroId);
		userHero.setStarLevel(h.getInitialLevel());
		boolean success = this.userHeroDao.addUserHero(userHero);
		addHeroStarEvent(userId, systemHeroId);
		unSynLogService.heroLog(userId, userHeroId, systemHeroId, useType, 1, 0, 1);

		return success;
	}

	@Override
	public void amendEmbattle(String userId, Map<Integer, String> posMap) {

		if (posMap.isEmpty()) {// 空阵法，不做处理
			return;
		}

		// 服务端上阵武将列表
		List<UserHeroBO> userHeroBOList = this.getUserHeroList(userId, 1);

		// 需要更新的武将列表
		Map<String, Integer> upateUserHeroIdMap = new HashMap<String, Integer>();

		// 当前服务端的阵法
		Map<Integer, String> serverPosMap = new HashMap<Integer, String>();

		// 看下当前服务端的阵法中有没有要下阵的
		for (UserHeroBO userHeroBO : userHeroBOList) {
			String userHeroId = userHeroBO.getUserHeroId();

			serverPosMap.put(userHeroBO.getPos(), userHeroId);

			if (!posMap.values().contains(userHeroId)) {// 有人下阵，不允许
				return;
			}
		}

		// 看下有没有要新上阵的
		for (Map.Entry<Integer, String> entry : posMap.entrySet()) {
			Integer pos = entry.getKey();
			String userHeroId = entry.getValue();

			if (!serverPosMap.values().contains(userHeroId)) {
				// 有新人上阵，不允许
				return;
			}

			if (serverPosMap.containsKey(pos) && userHeroId.equalsIgnoreCase(serverPosMap.get(pos))) {// 传上来的版本和服务端的一至
				continue;
			}

			upateUserHeroIdMap.put(userHeroId, pos);
		}

		if (upateUserHeroIdMap.isEmpty()) {
			LOG.debug("阵法一致，不做处理");
			return;
		}

		for (Map.Entry<String, Integer> entry : upateUserHeroIdMap.entrySet()) {
			String userHeroId = entry.getKey();
			int pos = entry.getValue();
			LOG.debug("修正用户阵法.userId[" + userId + "], userHeroId[" + userHeroId + "], pos[" + pos + "]");
			this.userHeroDao.changePos(userId, userHeroId, pos);
		}

	}

	@Override
	public boolean addUserHero(String userId, Map<String, Integer> heroIdMap, int useType) {

		List<UserHero> userHeroList = new ArrayList<UserHero>();

		for (Map.Entry<String, Integer> entry : heroIdMap.entrySet()) {

			final int systemHeroId = entry.getValue();
			String userHeroId = entry.getKey();
			Date now = new Date();

			SystemHero systemHero = this.getSysHero(systemHeroId);

			UserHero userHero = new UserHero();

			UpstarHeroConfig h = this.upStarHeroConfigDao.get(systemHero.getHeroId());

			userHero.setCreatedTime(now);
			userHero.setHeroExp(0);
			userHero.setHeroLevel(1);
			userHero.setSystemHeroId(systemHeroId);
			userHero.setPos(0);
			userHero.setUpdatedTime(now);
			userHero.setUserId(userId);
			userHero.setUserHeroId(userHeroId);
			userHero.setStarLevel(h.getInitialLevel());
			userHeroList.add(userHero);

			addHeroStarEvent(userId, systemHeroId);

			this.unSynLogService.heroLog(userId, userHeroId, systemHeroId, useType, 1, 0, 1);

		}

		boolean success = this.userHeroDao.addUserHero(userHeroList);

		return success;

	}

	private void addHeroStarEvent(String userId, final int systemHeroId) {
		int heroStar = getSysHero(systemHeroId).getHeroStar();
		eventService.addHeroStarEvent(userId, heroStar);
	}

	@Override
	public int getUserHeroCount(String userId) {
		return this.userHeroDao.getUserHeroCount(userId);
	}

	@Override
	public int getHeroId(int systemHeroId) {
		return systemHeroDao.get(systemHeroId).getHeroId();
	}

	@Override
	public void retrieve(String uid, String userHeroId) {
		userHeroDao.updateHeroStatus(uid, userHeroId, HERO_ALIVE);
	}

	@Override
	public UserHeroBO getUserHeroByPos(String userId, int pos) {
		UserHero userHero = this.userHeroDao.getUserHeroByPos(userId, pos);
		return createUserHeroBO(userHero);
	}

	@Override
	public Map<String, Object> lightNode(String userId, String userHeroId, int nodeId, int type) {
		checkIsNodeLighted(userId, userHeroId, nodeId);

		List<UserToolBO> toolBOs = tryReduceTool(userId, nodeId, type);

		updateHeroUpgradeNode(userId, userHeroId, nodeId);

		UserHero userHero = userHeroDao.get(userId, userHeroId);
		Map<String, Object> rt = new HashMap<String, Object>();
		rt.put("hero", createUserHeroBO(userHero));
		rt.put("tls", toolBOs);

		dailyTaskService.sendUpdateDailyTaskEvent(userId, SystemDailyTask.WUJIANGJINJIE, 1);

		return rt;
	}

	private void updateHeroUpgradeNode(String userId, String userHeroId, int nodeId) {
		UserHero userHero = userHeroDao.get(userId, userHeroId);
		String nodes = userHero.getUpgradeNode();
		if (StringUtils.isEmpty(nodes)) {
			nodes = String.valueOf(nodeId);
		} else {
			nodes += "," + String.valueOf(nodeId);

		}
		userHeroDao.updateUpgradeNode(userId, userHeroId, nodes);
	}

	private List<UserToolBO> tryReduceTool(String userId, int nodeId, int type) {
		User user = userService.get(userId);

		List<SystemHeroUpgradeTool> tools = systemHeroUpgradeToolDao.get(nodeId);

		int needGoldNum = checkLightNodeToolEnough(nodeId, user, tools, type);

		List<UserToolBO> userToolBOs = reduceLightNodeNeedTools(userId, tools, needGoldNum, type);

		return userToolBOs;

	}

	private List<UserToolBO> reduceLightNodeNeedTools(String userId, List<SystemHeroUpgradeTool> tools, int needGoldNum, int type) {
		List<UserToolBO> userToolBOs = new ArrayList<UserToolBO>();

		for (SystemHeroUpgradeTool tool : tools) {

			int toolId = tool.getToolId();
			int needToolNum = tool.getToolNum();

			UserTool userTool = userToolDao.get(userId, toolId);
			if (userTool == null) {
				needToolNum = 0;
			} else {
				int userToolNum = userTool.getToolNum();
				needToolNum = userToolNum < needToolNum ? userToolNum : needToolNum;
			}

			UserToolBO toolBO = new UserToolBO();
			toolBO.setToolId(toolId);
			toolBO.setToolNum(needToolNum);
			userToolBOs.add(toolBO);

			if (tool.getToolType() == ToolType.COPPER) {
				userService.reduceCopper(userId, needToolNum, ToolUseType.REDUCE_HERO_UPGRADE);
			} else {
				toolService.reduceTool(userId, tool.getToolType(), toolId, needToolNum, ToolUseType.REDUCE_HERO_UPGRADE);
			}
		}

		if (type == USE_GOLD_LIGHT_NODE) {
			if (!userService.reduceGold(userId, needGoldNum, ToolUseType.REDUCE_HERO_UPGRADE)) {
				String message = "元宝点亮失败，用户元宝不足 userId[" + userId + "]";
				throw new ServiceException(HeroService.UPGRADE_GOLD_NOT_ENOUGH, message);
			}

		}

		return userToolBOs;
	}

	private int checkLightNodeToolEnough(int nodeId, User user, List<SystemHeroUpgradeTool> tools, int type) {
		int needGoldNum = 0;
		SystemVipLevel vip = systemVipLevelDao.get(user.getVipLevel());
		if (vip.getOpenGoldUpgrade() == 0 && type == 2) {
			throw new ServiceException(HeroService.NO_VIP, "不是vip");
		}
		String userId = user.getUserId();
		for (SystemHeroUpgradeTool tool : tools) {
			int toolId = tool.getToolId();
			int toolNum = tool.getToolNum();

			int userToolNum = userToolDao.getUserToolNum(userId, toolId);

			if (tool.getToolType() == ToolType.COPPER) {
				if (user.getCopper() < toolNum) {
					String message = "进阶失败，银币不足 userId[" + userId + "] toolId[" + toolId + "]";
					throw new ServiceException(HeroService.UPGRADE_COPPER_NOT_ENOUGH, message);
				}
			} else if (userToolNum < toolNum) {

				if (type != USE_GOLD_LIGHT_NODE) {
					String message = "进阶失败，道具不足 userId[" + userId + "] toolId[" + toolId + "]";
					throw new ServiceException(HeroService.UPGRADE_TOOL_NOT_ENOUGH, message);
				} else {
					SystemTool systemTool = systemToolDao.get(toolId);
					needGoldNum += (toolNum - userToolNum) * systemTool.getGoldMerge();
					long userGoldNum = user.getGoldNum();
					if (userGoldNum < needGoldNum) {
						String message = "元宝点亮失败，用户元宝不足 userId[" + userId + "]";
						throw new ServiceException(HeroService.UPGRADE_GOLD_NOT_ENOUGH, message);
					}
				}
			}
		}
		return needGoldNum;
	}

	private void checkIsNodeLighted(String userId, String userHeroId, int nodeId) {
		UserHero userHero = userHeroDao.get(userId, userHeroId);
		String upgradeNode = userHero.getUpgradeNode();
		upgradeNode = upgradeNode == null ? "" : upgradeNode;
		String[] nodes = upgradeNode.split(",");

		String nodeToCheck = String.valueOf(nodeId);
		for (String node : nodes) {
			if (nodeToCheck.equals(node)) {
				String message = "节点已经点亮过 userId[" + userId + "] userHeroId[" + userHeroId + "] nodeId[" + nodeId + "]";
				throw new ServiceException(HeroService.UPGRADE_NODE_ALREADY_LIGHTED, message);
			}
		}
	}

	@Override
	public Map<String, Object> advance(String uid, int type, String userHeroId) {
		User user = userService.get(uid);
		UserHero userHero = userHeroDao.get(uid, userHeroId);
		int heroLevel = userHero.getHeroLevel();

		int needExploitsNum = getAdvanceNeedMuhon(heroLevel);

		SystemUserLevel systemUserLevel = this.systemUserLevelDao.get(user.getLevel());

		if (type == 2) {

			heroLevel += 1;

			if (needExploitsNum > user.getMuhon()) {
				String message = "玩家武魂不足 uid[" + uid + "]";
				throw new ServiceException(HeroService.ADVANCE_EXPLOITS_NOT_ENOUGH, message);
			}

			if (heroLevel > systemUserLevel.getHeroLevelMax()) {
				String message = "武将等级不可以超过用户等级 userHeroId[" + userHeroId + "]";
				throw new ServiceException(HeroService.ADVANCE_CANNOT_BEYOND_USER_LEVEL, message);
			}

			SystemLevelExp heroExp = systemLevelExpDao.getHeroExp(heroLevel);

			userHeroDao.updateExpLevel(uid, userHeroId, heroExp.getExp(), heroExp.getLevel());

			if (!userService.reduceMuhon(uid, needExploitsNum, ToolUseType.REDUCE_ADVANCE)) {
				String message = "玩家武魂不足 uid[" + uid + "]";
				throw new ServiceException(HeroService.ADVANCE_EXPLOITS_NOT_ENOUGH, message);
			}

			userHero = userHeroDao.get(uid, userHeroId);

			Event event = new HeroAdvanceEvent(uid, userHeroId, 1, heroLevel);
			this.eventService.dispatchEvent(event);
		}

		int level = userHero.getHeroLevel();

		UserHeroBO previousHeroBO = createUserHeroBO(userHero);
		UserHeroBO afterHeroBO = createUserHeroBOOfNextLevel(userHero);

		XiaShiLevelEvent e = new XiaShiLevelEvent(userHero.getUserId(), level);
		eventService.dispatchEvent(e);

		Map<String, Object> rt = new HashMap<String, Object>();
		int needMuhon = getAdvanceNeedMuhon(heroLevel);
		rt.put("mnum", needMuhon);
		// rt.put("snum", (int) (needMuhon * 0.5));
		rt.put("snum", 0);
		rt.put("bho", previousHeroBO);
		rt.put("aho", afterHeroBO);

		return rt;
	}

	public UserHeroBO createUserHeroBOOfNextLevel(UserHero userHero) {

		int maxLevel = configDataDao.getInt(ConfigKey.USER_MAX_LEVEL, InitDefine.DEFAULT_MAX_LEFEL);
		if (userHero.getHeroLevel() >= maxLevel) {
			return createUserHeroBO(userHero);
		}

		int heroLevel = userHero.getHeroLevel();
		SystemLevelExp heroExp = systemLevelExpDao.getHeroExp(heroLevel + 1);

		userHero.setHeroLevel(heroExp.getLevel());
		userHero.setHeroExp(heroExp.getExp());

		UserHeroBO afterHeroBO = createUserHeroBO(userHero);

		return afterHeroBO;
	}

	/**
	 * 获取升级总共消耗的奶酪数量
	 */
	public int getAdvanceTotalNeedMuhon(int heroLevel) {
		int maxLevel = configDataDao.getInt(ConfigKey.USER_MAX_LEVEL, InitDefine.DEFAULT_MAX_LEFEL);
		if (heroLevel >= maxLevel) {
			heroLevel = maxLevel;
		}

		return systemLevelExpDao.getTotalExp(heroLevel);
	}

	/**
	 * 获取升级需要的功勋数量
	 */
	public int getAdvanceNeedMuhon(int heroLevel) {

		int maxLevel = configDataDao.getInt(ConfigKey.USER_MAX_LEVEL, InitDefine.DEFAULT_MAX_LEFEL);
		if (heroLevel >= maxLevel) {
			return 0;
		}
		SystemLevelExp afterLevelExp = systemLevelExpDao.getHeroExp(heroLevel + 1);

		return afterLevelExp.getExp();
	}

	@Override
	public Map<String, Object> autoAdvance(String uid, String userHeroId) {

		User user = userService.get(uid);
		UserHero userHero = userHeroDao.get(uid, userHeroId);
		int heroLevel = userHero.getHeroLevel();

		SystemUserLevel systemUserLevel = this.systemUserLevelDao.get(user.getLevel());
		int maxLevel = systemUserLevel.getHeroLevelMax();
		int addLevel = 0;
		int stopResult = 0;
		int totalNeedMuhon = 0;

		int i = 0;
		while (i < 200) {

			i++;

			if (heroLevel + addLevel >= maxLevel) {
				stopResult = ADVANCE_CANNOT_BEYOND_USER_LEVEL;
				break;
			}

			int needMuhon = getAdvanceNeedMuhon(heroLevel + addLevel);

			if (totalNeedMuhon + needMuhon > user.getMuhon()) {
				stopResult = ADVANCE_EXPLOITS_NOT_ENOUGH;
				break;
			}

			totalNeedMuhon += needMuhon;

			addLevel++;
		}

		if (addLevel > 0) {
			heroLevel += addLevel;
			SystemLevelExp heroExp = systemLevelExpDao.getHeroExp(heroLevel);
			if (userService.reduceMuhon(uid, totalNeedMuhon, ToolUseType.REDUCE_ADVANCE)) {
				userHeroDao.updateExpLevel(uid, userHeroId, heroExp.getExp(), heroExp.getLevel());
			}

			HeroAdvanceEvent event = new HeroAdvanceEvent(uid, userHeroId, addLevel, heroLevel);
			eventService.dispatchEvent(event);

		} else {
			throw new ServiceException(stopResult, "武将一健升级失败");
		}

		userHero = userHeroDao.get(uid, userHeroId);

		UserHeroBO previousHeroBO = createUserHeroBO(userHero);
		UserHeroBO afterHeroBO = createUserHeroBOOfNextLevel(userHero);

		Map<String, Object> rt = new HashMap<String, Object>();
		// rt.put("mnum", totalNeedMuhon);
		// rt.put("snum", totalNeedCopper);
		int needMuhon = getAdvanceNeedMuhon(heroLevel);
		rt.put("mnum", needMuhon);
		rt.put("snum", (int) (needMuhon * 0.5));
		rt.put("bho", previousHeroBO);
		rt.put("aho", afterHeroBO);

		return rt;
	}

	@Override
	public CommonDropBO merge(String uid, int fragmentId) {
		UserTool userTool = userToolDao.get(uid, fragmentId);
		SystemFragment fragment = systemFragmentDao.getByFragmentId(fragmentId);
		// 对应的万能碎片
		// TODO
		SystemFragment systemAllFragment = systemFragmentDao.getByStar(fragment.getStar(), fragment.getMergedToolType());
		UserTool userToolAll = null;
		if (null != systemAllFragment) {
			userToolAll = userToolDao.get(uid, systemAllFragment.getToolId());
		}
		int[] need = checkFragmentsEnough(userTool, fragment, userToolAll);
		if (fragment.getMergedToolType() == ToolType.HERO) {
			boolean success = toolService.reduceTool(uid, ToolType.FRAGMENT, fragmentId, need[0], ToolUseType.REDUCE_HERO_MERGE);
			boolean successAll = true;
			if (need[1] != 0) {
				successAll = toolService.reduceTool(uid, ToolType.FRAGMENT, systemAllFragment.getToolId(), need[1], ToolUseType.REDUCE_HERO_MERGE);
			}
			if (success && successAll) {
				List<DropToolBO> tools = toolService.giveTools(uid, ToolType.HERO, fragment.getMergedToolId(), fragment.getMergedToolNum(), ToolUseType.ADD_HERO_MERGE);
				return toolService.appendToDropBO(uid, tools);
			}
		} else if (fragment.getMergedToolType() == ToolType.EQUIP) {
			boolean success = toolService.reduceTool(uid, ToolType.FRAGMENT, fragmentId, need[0], ToolUseType.REDUCE_EQUIP_MERGE);
			boolean successAll = true;
			if (need[1] != 0) {
				successAll = toolService.reduceTool(uid, ToolType.FRAGMENT, systemAllFragment.getToolId(), need[1], ToolUseType.REDUCE_EQUIP_MERGE);
			}
			if (success && successAll) {
				List<DropToolBO> tools = toolService.giveTools(uid, ToolType.EQUIP, fragment.getMergedToolId(), fragment.getMergedToolNum(), ToolUseType.ADD_EQUIP_MERGE);
				return toolService.appendToDropBO(uid, tools);
			}
		}

		return null;
	}

	private int[] checkFragmentsEnough(UserTool userTool, SystemFragment systemFragment, UserTool userToolAll) {
		int[] need = new int[2];
		if (userTool == null) {
			String message = "武将碎片和成数量不足";
			throw new ServiceException(HeroService.MERGE_FRAGMENT_NOT_ENOUGH, message);
		}

		int userFragmentNum = userTool.getToolNum();
		int needFragmentNum = systemFragment.getNeedFragmentNum();
		int allFragmentNum = 0;
		if (null != userToolAll) {
			allFragmentNum = userToolAll.getToolNum();
		}

		if (userFragmentNum + allFragmentNum < needFragmentNum) {
			String message = "武将碎片和成数量不足, toolNum[" + userFragmentNum + "]";
			throw new ServiceException(HeroService.MERGE_FRAGMENT_NOT_ENOUGH, message);
		}
		if (userFragmentNum >= needFragmentNum) {
			need[0] = needFragmentNum;
			need[1] = 0;
		} else {
			need[0] = userFragmentNum;
			need[1] = needFragmentNum - userFragmentNum;
		}
		return need;
	}

	public CommonDropBO eatHero(String userId, String userHeroId, List<String> eatHeroId) {
		int exp = 0;

		UserHero hero = userHeroDao.get(userId, userHeroId);
		SystemHero systemHero = systemHeroDao.get(hero.getSystemHeroId());
		UpstarValueConfig val = this.upStarValueConfigDao.get(hero.getStarLevel(), systemHero.getCareer());
		UpstarValueConfig valNext = this.upStarValueConfigDao.get(hero.getStarLevel() + 1, systemHero.getCareer());
		if (val.getEvent() != UP_LEVEl) {
			throw new ServiceException(HeroService.UPSTAR_NO, "当前不能升级");
		}
		UpstarHeroConfig heroc = this.upStarHeroConfigDao.get(systemHero.getHeroId());

		if (heroc == null) {
			String message = "武将的升星配置不存在.userId[" + userId + "], heroId[" + systemHero.getHeroId() + "]";
			LOG.error(message);
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

		if (val.getStarLevel() >= heroc.getMaxLevel()) {
			throw new ServiceException(HeroService.NO_UPSTAR, "已经到该武将最大级别");
		}
		List<UserHero> delList = new ArrayList<UserHero>();
		// 出售
		int sellCopper = 0;
		int sellMuhon = 0;
		int jiangshanOrder = 0;// 吞噬无灵魂
		int mingwen = 0;
		int soul = 0;

		List<UserHero> beSellList = new ArrayList<UserHero>();
		for (String heroId : eatHeroId) {
			UserHero eat = checkDeleteAble(userId, heroId);
			if (eat != null) {
				SellHeroRet sellHeroRet = this.getSellInfo(userId, heroId, beSellList);
				sellCopper += sellHeroRet.getCopper();
				sellMuhon += sellHeroRet.getMuhon();
				mingwen += sellHeroRet.getMingwen();
				soul += sellHeroRet.getSoul();
				SystemHero sh = systemHeroDao.get(eat.getSystemHeroId());
				exp += sh.getStarPoint();
				delList.add(eat);
			}
		}

		if (delList.size() > 0)
			delete(userId, delList, ToolUseType.REDUCE_HERO_EAT);

		// 加经验
		hero.setStarExp(hero.getStarExp() + exp);
		if (hero.getStarExp() >= val.getExp()) { // 升级
			hero.setStarLevel(hero.getStarLevel() + 1);
			if (valNext != null && valNext.getEvent() == UP_STAR) {
				hero.setNewexp(hero.getStarExp() - val.getExp());
				hero.setStarExp(0);
			} else {
				hero.setStarExp(hero.getStarExp() - val.getExp());
			}
		}
		userHeroDao.updateHeroStar(userId, userHeroId, hero.getStarLevel(), hero.getStarExp(), hero.getNewexp());
		// 添加获得的金币，奶酪，材料
		List<UserToolBO> toolBOList = new ArrayList<UserToolBO>();
		toolBOList = addSellProfit(userId, delList, new SellHeroRet(sellCopper, sellMuhon, jiangshanOrder, mingwen, soul));
		CommonDropBO dr = new CommonDropBO();
		dr.setUserToolBOList(toolBOList);
		return dr;
	}

	public void eatFragment(String userId, String userHeroId, List<Integer> ids) {
		int exp = 0;

		UserHero hero = userHeroDao.get(userId, userHeroId);
		SystemHero systemHero = systemHeroDao.get(hero.getSystemHeroId());
		UpstarValueConfig val = this.upStarValueConfigDao.get(hero.getStarLevel(), systemHero.getCareer());
		UpstarValueConfig valNext = this.upStarValueConfigDao.get(hero.getStarLevel() + 1, systemHero.getCareer());
		if (val.getEvent() != UP_LEVEl) {
			throw new ServiceException(HeroService.UPSTAR_NO, "当前不能升级");
		}
		UpstarHeroConfig heroc = this.upStarHeroConfigDao.get(systemHero.getHeroId());
		if (val.getStarLevel() >= heroc.getMaxLevel()) {
			throw new ServiceException(HeroService.NO_UPSTAR, "已经到该武将最大级别");
		}
		for (Integer temp : ids) {
			UserTool userTool = userToolDao.get(userId, temp);
			SystemFragment fragment = systemFragmentDao.getByFragmentId(temp);
			if (userTool == null || userTool.getToolNum() <= 0 || fragment.getType() == 1) {
				continue;
			}
			toolService.reduceTool(userId, ToolType.FRAGMENT, userTool.getToolId(), userTool.getToolNum(), ToolUseType.REDUCE_HERO_EAT);
			exp += (fragment.getEatExp() * userTool.getToolNum());
		}
		if (exp <= 0) {
			return;
		}
		hero.setStarExp(hero.getStarExp() + exp);
		if (hero.getStarExp() >= val.getExp()) { // 升级
			hero.setStarLevel(hero.getStarLevel() + 1);
			if (valNext != null && valNext.getEvent() == UP_STAR) {
				hero.setNewexp(hero.getStarExp() - val.getExp());
				hero.setStarExp(0);
			} else {
				hero.setStarExp(hero.getStarExp() - val.getExp());
			}
		}
		userHeroDao.updateHeroStar(userId, userHeroId, hero.getStarLevel(), hero.getStarExp(), hero.getNewexp());
	}

	public void heroBreak(String userId, String userHeroId, List<String> eatHeroId) {
		UserHero hero = userHeroDao.get(userId, userHeroId);
		SystemHero systemHero = systemHeroDao.get(hero.getSystemHeroId());
		UpstarValueConfig val = this.upStarValueConfigDao.get(hero.getStarLevel(), systemHero.getCareer());
		if (val.getEvent() != UP_STAR) {
			throw new ServiceException(HeroService.UPSTAR_NO, "当前不能突破");
		}
		UpstarHeroConfig heroc = this.upStarHeroConfigDao.get(systemHero.getHeroId());
		if (val.getStarLevel() >= heroc.getMaxLevel()) {
			throw new ServiceException(HeroService.NO_UPSTAR, "已经到该武将最大级别");
		}

		List<UserHero> delList = new ArrayList<UserHero>();

		SystemHero sh = systemHeroDao.get(hero.getSystemHeroId());

		List<UserHero> userHeroList = new ArrayList<UserHero>();

		for (String heroId : eatHeroId) {
			UserHero eat = checkDeleteAble(userId, heroId);
			userHeroList.add(eat);
			if (eat != null) {
				SystemHero eatSh = systemHeroDao.get(eat.getSystemHeroId());
				if (sh.getHeroId() != eatSh.getHeroId()) {
					throw new ServiceException(HeroService.UPSTAR_NO, "不是相同武将，不能升星");
				}
				delList.add(eat);
			}
		}

		if (delList.size() > 0) {

			int rowCount = this.delete(userId, userHeroList, ToolUseType.REDUCE_HERO_BREAK);
			if (rowCount != userHeroList.size()) {
				throw new ServiceException(ServiceReturnCode.FAILD, "数据异常");
			}

			hero.setStarExp(hero.getStarExp() + delList.size());
			UpstarBreakConfig bval = this.upStarBreakConfigDao.get(heroc.getInitialLevel(), hero.getStarLevel());
			if (hero.getStarExp() >= bval.getNum()) {
				hero.setStarLevel(hero.getStarLevel() + 1);
				hero.setStarExp(hero.getNewexp());
			}
			userHeroDao.updateHeroStar(userId, userHeroId, hero.getStarLevel(), hero.getStarExp(), 0);
		}
	}
}
