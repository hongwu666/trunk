package com.lodogame.ldsg.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.SystemEquipDao;
import com.lodogame.game.dao.SystemStoneDao;
import com.lodogame.game.dao.UserEquipDao;
import com.lodogame.game.dao.UserEquipStoneDao;
import com.lodogame.game.dao.UserStoneDao;
import com.lodogame.ldsg.bo.UserStoneBO;
import com.lodogame.ldsg.bo.UserToolBO;
import com.lodogame.ldsg.constants.ToolId;
import com.lodogame.ldsg.constants.ToolType;
import com.lodogame.ldsg.constants.ToolUseType;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.GainStoneEvent;
import com.lodogame.ldsg.event.StoneUpgradeEvent;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.service.EventService;
import com.lodogame.ldsg.service.StoneService;
import com.lodogame.ldsg.service.ToolService;
import com.lodogame.ldsg.service.UnSynLogService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.SystemEquip;
import com.lodogame.model.SystemStone;
import com.lodogame.model.User;
import com.lodogame.model.UserEquip;
import com.lodogame.model.UserEquipStone;
import com.lodogame.model.UserStone;

public class StoneServiceImpl implements StoneService {

	public static final int STONE_MAX_LEVEL = 8;

	public static final int UPGRADE_STONE_LIMIT_LEVEL = 50;

	@Autowired
	private UserStoneDao userStoneDao;

	@Autowired
	private ToolService toolService;

	@Autowired
	private SystemStoneDao systemStoneDao;

	@Autowired
	private EventService eventService;

	@Autowired
	private UserService userService;

	@Autowired
	private UserEquipStoneDao userEquipStoneDao;

	@Autowired
	private SystemEquipDao systemEquipDao;

	@Autowired
	private UserEquipDao userEquipDao;

	@Autowired
	private UnSynLogService unSynLogService;

	@Override
	public int enterStone(String userId) {
		UserToolBO userTool = toolService.getUserToolBO(userId, ToolId.TOOL_PROTECT);
		return userTool != null ? userTool.getToolNum() : 0;
	}

	@Override
	public Map<String, Object> upgradeStone(String userId, int isAuto, int isProdtect, int stoneId) {

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("sc", 1);
		User user = userService.get(userId);
		SystemStone stone = systemStoneDao.get(stoneId);

		UserStone userStone = userStoneDao.get(userId, stoneId);
		int userStoneNum = null != userStone ? userStone.getStoneNum() : 0;

		if (userStoneNum < 4) {
			throw new ServiceException(STONE_NOT_ENOUGH, "宝石不足");
		}
		if (stone.getStoneLevel() + 1 == 8) {
			if (user.getLevel() < UPGRADE_STONE_LIMIT_LEVEL) {
				throw new ServiceException(USER_LEVEL_NOT_ENOUGH, "等级需要到达50级才能合成8级宝石");
			}
		}
		if (stone.getStoneLevel() == STONE_MAX_LEVEL) {
			throw new ServiceException(STONE_LEVEL_IS_MAX, "宝石已是最高等级");
		}

		if (isProdtect == 1) {
			if (!toolService.reduceTool(userId, ToolType.MATERIAL, ToolId.TOOL_PROTECT, stone.getProtectNum(), ToolUseType.REDUCE_STONE)) {
				throw new ServiceException(PROTECT_NOT_ENOUGH, "保护符不足");
			}
		}
		int randomNum = RandomUtils.nextInt(10000);

		SystemStone newStone = systemStoneDao.get(stone.getUpgradStoneId());
		if (null == newStone) {
			throw new ServiceException(STONE_NOT_EXIST, "宝石不存在");
		}

		boolean succ = false;
		boolean isUpgrade = false;
		if (isUpgradeSucc(stone.getLowerNum(), stone.getUpperNum(), randomNum)) {// 成功
			isUpgrade = true;
			succ = this.reduceStone(userId, stoneId, 4, ToolUseType.REDUCE_STONE);
		} else {
			result.put("sc", 0);
			if (isProdtect != 1) {
				this.reduceStone(userId, stoneId, 1, ToolUseType.REDUCE_STONE);
			}
		}

		if (succ && isUpgrade) {
			this.addStone(userId, stone.getUpgradStoneId(), 1, ToolUseType.ADD_COMPOUND_STONE);

			Event event = new StoneUpgradeEvent(userId, stone.getUpgradStoneId());
			this.eventService.dispatchEvent(event);

			this.eventService.addUserPowerUpdateEvent(userId);

		}
		result.put("sid", newStone.getStoneId());

		return result;
	}

	@Override
	public boolean addStone(String userId, int stoneId, int stoneNum, int useType) {

		boolean success = this.userStoneDao.addUserStone(userId, stoneId, stoneNum);
		unSynLogService.toolLog(userId, ToolType.STONE, stoneId, stoneNum, useType, 1, "", true);

		GainStoneEvent e = new GainStoneEvent(userId, stoneId);
		eventService.dispatchEvent(e);

		return success;
	}

	@Override
	public Map<Integer, Integer> putPack(UserEquip userEquip) {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();

		List<UserEquipStone> list = userEquipStoneDao.getUserEquipStone(userEquip.getUserEquipId());
		if (list != null && list.size() > 0) {
			for (UserEquipStone userEquipStone : list) {
				//this.addStone(userEquip.getUserId(), userEquipStone.getStoneId(), 1, ToolUseType.ADD_SELL_EQUIP);
				int value = map.get(userEquipStone.getStoneId()) == null ? 0 : map.get(userEquipStone.getStoneId());
				map.put(userEquipStone.getStoneId(), value + 1);
			}
			this.userEquipStoneDao.delUserEquipStone(userEquip.getUserId());
		}

		return map;
	}

	@Override
	public boolean reduceStone(String userId, int stoneId, int stoneNum, int useType) {
		boolean success = this.userStoneDao.reduceUserStone(userId, stoneId, stoneNum);
		unSynLogService.toolLog(userId, ToolType.STONE, stoneId, stoneNum, useType, -1, "", true);
		return success;
	}

	private boolean isUpgradeSucc(int lowerNum, int upperNum, int randomNum) {
		return randomNum >= lowerNum && randomNum <= upperNum;
	}

	@Override
	public List<UserStoneBO> getUserStoneBos(String userId) {
		List<UserStoneBO> result = new ArrayList<UserStoneBO>();

		List<UserStone> list = this.userStoneDao.getUserStoneList(userId);
		if (list != null && list.size() > 0) {
			for (UserStone userStone : list) {
				if (userStone.getStoneNum() == 0) {
					continue;
				}
				UserStoneBO userStoneBO = new UserStoneBO();
				BeanUtils.copyProperties(userStone, userStoneBO);
				result.add(userStoneBO);
			}
		}

		return result;
	}

	@Override
	public void dressStone(String userId, String userEquipId, int stoneId, int pos) {

		checkDressStoneIsRight(userId, userEquipId, pos, stoneId);

		if (!this.reduceStone(userId, stoneId, 1, ToolUseType.REDUCE_DRESS_STONE)) {
			throw new ServiceException(DRESS_STONE_NOT_ENOUGH, "穿戴宝石,宝石不足");
		}

		UserEquipStone userEquipStone = new UserEquipStone();
		userEquipStone.setPos(pos);
		userEquipStone.setStoneId(stoneId);
		userEquipStone.setUserEquipId(userEquipId);
		userEquipStone.setUserId(userId);
		this.userEquipStoneDao.insertUserEquipStone(userEquipStone);

		this.eventService.addUserPowerUpdateEvent(userId);

	}

	private void checkDressStoneIsRight(String userId, String userEquipId, int pos, int stoneId) {

		if (this.userEquipStoneDao.getUserEquipStone(userEquipId, pos) != null) {
			throw new ServiceException(DRESS_STONE_HAS_STONE, "该装备该空位已有宝石");
		}

		UserStone userStone = userStoneDao.get(userId, stoneId);

		if (userStone == null) {
			throw new ServiceException(DRESS_STONE_NOT_ENOUGH, "穿戴宝石,宝石不足");
		}

		UserEquip userEquip = this.userEquipDao.get(userId, userEquipId);

		SystemEquip systemEquip = this.systemEquipDao.get(userEquip.getEquipId());

		SystemStone st = systemStoneDao.get(userStone.getStoneId());

		if (pos > systemEquip.getStoneHoleNum()) {
			throw new ServiceException(DRESS_STONE_HOLE_IS_NOT_ENOUGH, "穿戴宝石,孔位不足");
		}

		if (systemEquip.getColor() < st.getEquipColor()) {
			throw new ServiceException(NO_EQUIP_COLOR, "宝石颜色需求高于装备颜色");
		}
		SystemStone newStone = systemStoneDao.get(stoneId);
		List<UserEquipStone> stone = userEquipStoneDao.getUserEquipStone(userEquip.getUserEquipId());
		for (UserEquipStone temp : stone) {
			SystemStone tt = systemStoneDao.get(temp.getStoneId());
			if (tt.getStoneType() == newStone.getStoneType()) {
				throw new ServiceException(REPEAT_TYPE, "不能镶嵌相同类型的宝石");
			}
		}

	}

	@Override
	public Map<String, Object> undressStone(String userId, String userEquipId, int pos) {
		Map<String, Object> map = new HashMap<String, Object>();

		UserEquipStone userEquipStone = this.userEquipStoneDao.getUserEquipStone(userEquipId, pos);

		int stoneId = userEquipStone.getStoneId();

		if (this.userEquipStoneDao.delUserEquipStone(userEquipId, pos)) {
			this.addStone(userId, stoneId, 1, ToolUseType.ADD_UNDRESS_STONE);
		} else {
			throw new ServiceException(UNDRESS_STONE_HAS_STONE, "卸载宝石失败，该装备的该孔位没有安装宝石");
		}

		this.eventService.addUserPowerUpdateEvent(userId);

		map.put("sid", stoneId);
		return map;
	}

	@Override
	public int sellStone(List<String> info, String userId) {
		int money = 0;
		for (String temp : info) {
			String[] in = temp.split("[:]");
			int id = Integer.valueOf(in[0]);
			int num = Integer.valueOf(in[1]);
			UserStone st = userStoneDao.get(userId, id);
			if (st == null || st.getStoneNum() <= 0)
				continue;
			if (st.getStoneNum() < num) {
				num = st.getStoneNum();
			}
			SystemStone ss = systemStoneDao.get(id);
			reduceStone(userId, id, num, ToolUseType.ADD_STONE_SELL);
			int price = ss.getSellPrice() * num;
			userService.addMingwen(userId, price, ToolUseType.ADD_STONE_SELL);
			money += price;
		}
		return money;
	}
}
