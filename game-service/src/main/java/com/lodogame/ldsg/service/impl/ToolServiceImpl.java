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

import com.lodogame.game.dao.ConfigDataDao;
import com.lodogame.game.dao.SystemStoneDao;
import com.lodogame.game.dao.SystemToolDao;
import com.lodogame.game.dao.SystemToolDropDao;
import com.lodogame.game.dao.UserExtinfoDao;
import com.lodogame.game.dao.UserStoneDao;
import com.lodogame.game.dao.UserToolDao;
import com.lodogame.game.utils.IDGenerator;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.DropDescBO;
import com.lodogame.ldsg.bo.DropToolBO;
import com.lodogame.ldsg.bo.UserEquipBO;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.bo.UserStoneBO;
import com.lodogame.ldsg.bo.UserToolBO;
import com.lodogame.ldsg.constants.ConfigKey;
import com.lodogame.ldsg.constants.InitDefine;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.constants.ToolType;
import com.lodogame.ldsg.constants.ToolUseType;
import com.lodogame.ldsg.event.DropHeroEvent;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.ToolDropEvent;
import com.lodogame.ldsg.event.ToolUpdateEvent;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.helper.BOHelper;
import com.lodogame.ldsg.helper.DropDescHelper;
import com.lodogame.ldsg.helper.ToolHelper;
import com.lodogame.ldsg.helper.VIPHelper;
import com.lodogame.ldsg.service.EquipService;
import com.lodogame.ldsg.service.EventService;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.MessageService;
import com.lodogame.ldsg.service.StoneService;
import com.lodogame.ldsg.service.ToolService;
import com.lodogame.ldsg.service.UnSynLogService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.SystemHero;
import com.lodogame.model.SystemTool;
import com.lodogame.model.SystemToolDrop;
import com.lodogame.model.User;
import com.lodogame.model.UserTool;

public class ToolServiceImpl implements ToolService {

	private static final Logger logger = Logger.getLogger(ToolServiceImpl.class);

	@Autowired
	private UserToolDao userToolDao;

	@Autowired
	private UserService userService;

	@Autowired
	private EquipService equipService;

	@Autowired
	private StoneService stoneService;

	@Autowired
	private HeroService heroService;

	@Autowired
	private SystemToolDao systemToolDao;

	@Autowired
	private UserExtinfoDao userExtinfoDao;

	@Autowired
	private SystemToolDropDao systemToolDropDao;

	@Autowired
	private UnSynLogService unSynLogService;

	@Autowired
	private ConfigDataDao configDataDao;

	@Autowired
	private EventService eventServcie;

	@Autowired
	private MessageService messageService;

	@Autowired
	private UserStoneDao userStoneDao;

	@Autowired
	private SystemStoneDao systemStoneDao;

	public boolean addTool(String userId, int toolType, int toolId, int toolNum, int useType) {

		ToolDropEvent event = new ToolDropEvent(userId, toolId, useType, 0);
		eventServcie.dispatchEvent(event);

		logger.debug("增加道具.userId[" + userId + "], toolId[" + toolId + "] toolNum[" + toolNum + "], useType[" + useType + "]");
		boolean success = false;
		try {
			UserTool userTool = this.userToolDao.get(userId, toolId);
			if (userTool != null) {
				success = this.userToolDao.addUserTool(userId, toolId, toolNum);
			} else {
				Date now = new Date();
				userTool = new UserTool();
				userTool.setUserId(userId);
				userTool.setToolType(toolType);
				userTool.setToolId(toolId);
				userTool.setToolNum(toolNum);
				userTool.setCreatedTime(now);
				userTool.setUpdatedTime(now);
				success = this.userToolDao.add(userTool);
			}
		} finally {
			unSynLogService.toolLog(userId, toolType, toolId, toolNum, useType, 1, "", success);
		}

		return success;
	}

	public boolean reduceTool(String userId, int toolType, int toolId, int toolNum, int useType) {
		logger.debug("消费道具.userId[" + userId + "], toolId[" + toolId + "] toolNum[" + toolNum + "], useType[" + useType + "]");

		boolean success = this.userToolDao.reduceUserTool(userId, toolId, toolNum);

		unSynLogService.toolLog(userId, toolType, toolId, toolNum, useType, -1, "", success);

		return success;
	}

	@Override
	public List<UserToolBO> getUserToolList(String userId) {

		List<UserToolBO> userToolBOList = new ArrayList<UserToolBO>();

		List<UserTool> userToolList = this.userToolDao.getList(userId);
		for (UserTool userTool : userToolList) {
			UserToolBO userToolBO = BOHelper.createUsetToolBO(userTool);
			userToolBOList.add(userToolBO);
		}

		return userToolBOList;
	}

	@Override
	public CommonDropBO give(String userId, List<DropDescBO> dropDescBOList, int useType) {

		CommonDropBO commonDropBO = new CommonDropBO();

		for (DropDescBO dropDescBO : dropDescBOList) {

			List<DropToolBO> dropToolBOList = this.giveTools(userId, dropDescBO.getToolType(), dropDescBO.getToolId(), dropDescBO.getToolNum(), useType);
			for (DropToolBO dropToolBO : dropToolBOList) {
				this.appendToDropBO(userId, commonDropBO, dropToolBO);
			}

		}

		return commonDropBO;

	}

	@Override
	public CommonDropBO give(String userId, DropDescBO dropDescBO, int useType) {

		List<DropDescBO> dropTools = new ArrayList<DropDescBO>();
		dropTools.add(dropDescBO);

		return this.give(userId, dropTools, useType);
	}

	@Override
	public CommonDropBO give(String userId, String toolIds, int useType) {

		List<DropDescBO> dropTools = DropDescHelper.parseDropTool(toolIds);

		return this.give(userId, dropTools, useType);
	}

	@Override
	public CommonDropBO give(String userId, int toolType, int toolId, int toolNum, int useType) {
		List<DropDescBO> dropTools = new ArrayList<DropDescBO>();
		dropTools.add(new DropDescBO(toolType, toolId, toolNum));
		return this.give(userId, dropTools, useType);
	}

	@Override
	public CommonDropBO give(String userId, String toolIds, int num, int useType) {

		List<DropDescBO> dropTools = DropDescHelper.parseDropTool(toolIds);
		for (DropDescBO bo : dropTools) {
			bo.setToolNum(bo.getToolNum() * num);
		}

		return this.give(userId, dropTools, useType);
	}

	@Override
	public UserToolBO getUserToolBO(String userId, int toolId) {

		UserTool userTool = this.userToolDao.get(userId, toolId);
		if (userTool != null) {
			return BOHelper.createUsetToolBO(userTool);
		}
		return null;
	}

	private List<DropToolBO> giveHeros(String userId, int toolType, int toolId, int toolNum, int useType) {
		Map<String, Integer> heroIdMap = new HashMap<String, Integer>();
		List<DropToolBO> boList = new ArrayList<DropToolBO>();
		for (int i = 0; i < toolNum; i++) {
			String userHeroId = IDGenerator.getID();
			heroIdMap.put(userHeroId, toolId);
			DropToolBO dropToolBO = new DropToolBO(toolType, toolId, userHeroId);
			boList.add(dropToolBO);
		}
		heroService.addUserHero(userId, heroIdMap, useType);
		return boList;
	}

	private List<DropToolBO> giveEquips(String userId, int toolType, int toolId, int toolNum, int useType) {
		Map<String, Integer> equipIdMap = new HashMap<String, Integer>();
		List<DropToolBO> boList = new ArrayList<DropToolBO>();
		for (int i = 0; i < toolNum; i++) {
			String userEquipId = IDGenerator.getID();
			equipIdMap.put(userEquipId, toolId);
			DropToolBO dropToolBO = new DropToolBO(toolType, toolId, userEquipId);
			boList.add(dropToolBO);
		}
		equipService.addUserEquips(userId, equipIdMap, useType);
		return boList;
	}

	@Override
	public List<DropToolBO> giveTools(String userId, int toolType, int toolId, int toolNum, int useType) {

		List<DropToolBO> boList = new ArrayList<DropToolBO>();

		DropToolBO dropToolBO = null;

		if (toolType == ToolType.COPPER) {
			this.userService.addCopper(userId, toolNum, useType);
		} else if (toolType == ToolType.EXP) {
			// 当用户等级大于或等于指定等级，则不再增长经验
			User user = userService.get(userId);
			int level = configDataDao.getInt(ConfigKey.USER_MAX_LEVEL, InitDefine.DEFAULT_MAX_LEFEL);
			if (user.getLevel() < level) {
				this.userService.addExp(userId, toolNum, useType);
			} else {
				toolNum = 0;
			}
			// this.userService.addExp(userId, toolNum, useType);
		} else if (toolType == ToolType.GOLD) {
			User user = userService.get(userId);
			this.userService.addGold(userId, toolNum, useType, user.getLevel());
		} else if (toolType == ToolType.HERO) {
			boList = giveHeros(userId, toolType, toolId, toolNum, useType);
		} else if (toolType == ToolType.EQUIP) {
			boList = giveEquips(userId, toolType, toolId, toolNum, useType);
		} else if (toolType == ToolType.POWER) {
			this.userService.addPower(userId, toolNum, useType, null);
		} else if (this.isTool(toolType)) {
			this.addTool(userId, toolType, toolId, toolNum, useType);
		} else if (toolType == ToolType.HERO_BAG) {
			this.userExtinfoDao.updateHeroMax(userId, toolNum);
		} else if (toolType == ToolType.EQUIP_BAG) {
			this.userExtinfoDao.updateEquipMax(userId, toolNum);
		} else if (toolType == ToolType.VIP_EXP) {
			int vipLevel = VIPHelper.getVipCardLevel(toolId);
			this.userService.assignVipLevel(userId, vipLevel, false);
		} else if (toolType == ToolType.REPUTATION) {
			this.userService.addReputation(userId, toolNum, useType);
		} else if (toolType == ToolType.STONE) {
			stoneService.addStone(userId, toolId, toolNum, useType);
		} else if (toolType == ToolType.MUHON) {
			this.userService.addMuhon(userId, toolNum, useType);
		} else if (toolType == ToolType.SOUL) {
			this.userService.addSoul(userId, toolNum, useType);
		} else if (toolType == ToolType.COIN) {
			this.userService.addCoin(userId, toolNum, useType);
		} else if (toolType == ToolType.MINGWEN) {
			this.userService.addMingwen(userId, toolNum, useType);
		} else if (toolType == ToolType.HONOUR)
			this.userService.addHonour(userId, toolNum, useType);
		else {
			logger.warn("未知掉落道具类型.toolType[" + toolType + "]");
		}

		if (dropToolBO == null && (boList == null || boList.size() == 0)) {
			dropToolBO = new DropToolBO(toolType, toolId, toolNum);
			boList.add(dropToolBO);
		}

		return boList;

	}

	@Override
	public CommonDropBO appendToDropBO(String userId, List<DropToolBO> dropToolBOs) {
		CommonDropBO dr = new CommonDropBO();
		for (DropToolBO dropToolBO : dropToolBOs) {
			appendToDropBO(userId, dr, dropToolBO);
		}
		return dr;
	}

	public void appendToDropBO(String userId, CommonDropBO commonDropBO, DropToolBO dropToolBO) {

		int toolId = dropToolBO.getToolId();
		int toolType = dropToolBO.getToolType();
		int toolNum = dropToolBO.getToolNum();

		switch (toolType) {
		case ToolType.HERO:
			UserHeroBO userHeroBO = this.heroService.getUserHeroBO(userId, dropToolBO.getValue());
			commonDropBO.addHero(userHeroBO);
			break;
		case ToolType.EQUIP:
			UserEquipBO userEquipBO = this.equipService.getUserEquipBO(userId, dropToolBO.getValue());
			commonDropBO.addEquip(userEquipBO);
			break;
		case ToolType.GOLD:
			commonDropBO.setGold(commonDropBO.getGold() + toolNum);
			break;
		case ToolType.COPPER:
			commonDropBO.setCopper(commonDropBO.getCopper() + toolNum);
			break;
		case ToolType.EXP:
			commonDropBO.setExp(commonDropBO.getExp() + toolNum);
			break;
		case ToolType.HERO_BAG:
			commonDropBO.setHeroBag(commonDropBO.getHeroBag() + toolNum);
			break;
		case ToolType.EQUIP_BAG:
			commonDropBO.setEquipBag(commonDropBO.getEquipBag() + toolNum);
			break;
		case ToolType.POWER:
			commonDropBO.setPower(commonDropBO.getPower() + toolNum);
			break;
		case ToolType.MUHON:
			commonDropBO.setMuhon(commonDropBO.getMuhon() + toolNum);
			break;
		case ToolType.MINGWEN:
			commonDropBO.setMingwen(commonDropBO.getMingwen() + toolNum);
			break;
		case ToolType.SOUL:
			commonDropBO.setSoul(commonDropBO.getSoul() + toolNum);
			break;
		case ToolType.COIN:
			commonDropBO.setCoin(commonDropBO.getCoin() + toolNum);
			break;
		case ToolType.HONOUR:
			commonDropBO.setHonour(commonDropBO.getHonour() + toolNum);
			break;
		case ToolType.VIP_EXP:
			int vipLevel = VIPHelper.getVipCardLevel(toolId);
			commonDropBO.setVipLevel(vipLevel);
			break;
		case ToolType.STONE:
			UserStoneBO userStoneBO = new UserStoneBO();
			userStoneBO.setStoneId(toolId);
			userStoneBO.setStoneNum(toolNum);
			commonDropBO.addStone(userStoneBO);
			break;
		default:
			if (this.isTool(toolType)) {

				List<UserToolBO> userToolBOList = commonDropBO.getUserToolBOList();
				boolean add = true;
				for (UserToolBO toolBO : userToolBOList) {
					if (toolBO.getToolId() == toolId && toolBO.getToolType() == toolType) {
						toolBO.setToolNum(toolBO.getToolNum() + toolNum);
						add = false;
						break;
					}
				}

				if (add) {
					UserToolBO userToolBO = new UserToolBO();
					userToolBO.setToolId(toolId);
					userToolBO.setToolNum(toolNum);
					userToolBO.setToolType(toolType);
					commonDropBO.addTool(userToolBO);
				}

			} else {
				logger.warn("未知道具类型.toolType[" + toolType + "]");
			}
			break;
		}

	}

	/**
	 * 是不是道具
	 * 
	 * @param toolType
	 * @return
	 */
	private boolean isTool(int toolType) {

		if (toolType == ToolType.MATERIAL || toolType == ToolType.GIFT_BOX || toolType == ToolType.FRAGMENT) {
			return true;
		}

		if (toolType == ToolType.SKILL_BOOK || toolType == ToolType.DIREC_EGG || toolType == ToolType.GIFT_BOX_NEED_KEY || toolType == ToolType.HORN) {
			return true;
		}

		return false;
	}

	@Override
	public CommonDropBO openGiftBox(String userId, int toolId, EventHandle handle) {
		UserTool userTool = this.userToolDao.get(userId, toolId);
		if (userTool == null || userTool.getToolNum() <= 0) {
			String message = "打开宝箱失败，用户道具不足.userId[" + userId + "], toolId[" + toolId + "]";
			logger.error(message);
			throw new ServiceException(ERROR_OPEN_GIFT_BOX_NOT_ENOUGH, message);
		}

		SystemTool systemTool = systemToolDao.get(toolId);

		// 需要钥匙的宝箱，需验证是否有钥匙
		if (userTool.getToolType() == ToolType.GIFT_BOX_NEED_KEY) {
			UserTool userLooseTool = this.userToolDao.get(userId, systemTool.getLooseToolId());
			if (userLooseTool == null || userLooseTool.getToolNum() <= 0) {
				String message = "打开宝箱失败，钥匙不足.userId[" + userId + "], toolId[" + toolId + "]";
				logger.error(message);
				throw new ServiceException(ERROR_OPEN_GIFT_BOX_HAS_NOT_KEY, message);
			}

		}

		if (userTool.getToolType() != ToolType.GIFT_BOX && userTool.getToolType() != ToolType.DIREC_EGG && userTool.getToolType() != ToolType.GIFT_BOX_NEED_KEY) {
			String message = "打开宝箱失败，这不是一个箱箱.userId[" + userId + "], toolId[" + toolId + "]";
			logger.error(message);
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

		CommonDropBO commonDropBO = new CommonDropBO();

		boolean success = this.reduceTool(userId, ToolType.GIFT_BOX, toolId, 1, ToolUseType.REDUCE_OPEN_GIFT_BOX);
		if (!success) {
			String message = "打开宝箱失败，用户道具不足.userId[" + userId + "], toolId[" + toolId + "]";
			logger.error(message);
			throw new ServiceException(ERROR_OPEN_GIFT_BOX_NOT_ENOUGH, message);
		}

		// 如果需要需要的宝箱，要把钥匙删掉
		if (userTool.getToolType() == ToolType.GIFT_BOX_NEED_KEY) {
			boolean successKey = this.reduceTool(userId, ToolType.MATERIAL, systemTool.getLooseToolId(), 1, ToolUseType.REDUCE_OPEN_GIFT_BOX);
			if (!successKey) {
				String message = "打开宝箱失败，钥匙不足.userId[" + userId + "], toolId[" + toolId + "]";
				logger.error(message);
				throw new ServiceException(ERROR_OPEN_GIFT_BOX_HAS_NOT_KEY, message);
			}
		}

		ToolUpdateEvent toolUpdateEvent = new ToolUpdateEvent(userId);
		handle.handle(toolUpdateEvent);

		int rand = RandomUtils.nextInt(10000) + 1;

		// 处理定向蛋
		if (systemTool.getType() == ToolType.DIREC_EGG) {
			rand = ToolHelper.direcEggGetRand(this.userExtinfoDao.get(userId));
		}

		User user = this.userService.get(userId);

		List<SystemToolDrop> systemToolDropList = this.systemToolDropDao.getSystemToolDropList(toolId);

		for (SystemToolDrop systemToolDrop : systemToolDropList) {

			if (systemToolDrop.getLowerNum() <= rand && rand <= systemToolDrop.getUpperNum()) {
				List<DropToolBO> dropToolBOList = this.giveTools(userId, systemToolDrop.getDropToolType(), systemToolDrop.getDropToolId(), systemToolDrop.getDropToolNum(),
						ToolUseType.ADD_OPEN_GIFT_BOX);

				for (DropToolBO dropToolBO : dropToolBOList) {

					this.appendToDropBO(userId, commonDropBO, dropToolBO);

					if (dropToolBO.getToolType() == ToolType.HERO) {

						SystemHero systemHero = this.heroService.getSysHero(dropToolBO.getToolId());

						DropHeroEvent toolDropEvent = new DropHeroEvent(userId, user.getUsername(), systemHero.getHeroName(), systemHero.getHeroStar(), systemTool.getName());
						eventServcie.dispatchEvent(toolDropEvent);

					}
				}
			}
		}

		return commonDropBO;
	}

	@Override
	public CommonDropBO checkOpenBoxLooseTool(String userId, int toolId) {
		SystemTool systemTool = systemToolDao.get(toolId);
		CommonDropBO commonDropBO = new CommonDropBO();
		if (systemTool != null && systemTool.getLooseToolId() > 0) {
			DropToolBO dropToolBO = new DropToolBO(ToolType.MATERIAL, systemTool.getLooseToolId(), 1);
			this.appendToDropBO(userId, commonDropBO, dropToolBO);
		}

		return commonDropBO;

	}

	@Override
	public List<UserToolBO> giveTools(String userId, Map<Integer, UserToolBO> toolBOs) {
		List<UserToolBO> toolBOList = new ArrayList<UserToolBO>();
		for (Entry<Integer, UserToolBO> entry : toolBOs.entrySet()) {
			UserToolBO toolBO = entry.getValue();
			addTool(userId, toolBO.getToolType(), toolBO.getToolId(), toolBO.getToolNum(), ToolUseType.ADD_SELL_HERO);
			toolBOList.add(toolBO);
		}
		return toolBOList;

	}

}
