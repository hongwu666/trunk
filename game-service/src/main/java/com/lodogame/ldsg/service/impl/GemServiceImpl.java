package com.lodogame.ldsg.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.GemAltarDao;
import com.lodogame.game.dao.SystemStoneDao;
import com.lodogame.ldsg.bo.GemBO;
import com.lodogame.ldsg.constants.ToolUseType;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.helper.BOHelper;
import com.lodogame.ldsg.service.DailyTaskService;
import com.lodogame.ldsg.service.GemService;
import com.lodogame.ldsg.service.MessageService;
import com.lodogame.ldsg.service.StoneService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.GemAltarGroupsOpenConfig;
import com.lodogame.model.GemAltarOpenConfig;
import com.lodogame.model.GemAltarUserAutoSell;
import com.lodogame.model.GemAltarUserInfo;
import com.lodogame.model.GemAltarUserOpen;
import com.lodogame.model.GemAltarUserPack;
import com.lodogame.model.SystemDailyTask;
import com.lodogame.model.SystemStone;
import com.lodogame.model.User;
import com.lodogame.model.log.GemAltarUserSellLog;

public class GemServiceImpl implements GemService {

	@Autowired
	private DailyTaskService dailyTaskService;

	@Autowired
	private StoneService stoneService;

	@Autowired
	private SystemStoneDao systemStoneDao;

	@Autowired
	private GemAltarDao gemAltarDao;

	@Autowired
	private UserService userService;

	@Autowired
	private MessageService messageService;

	public void delAtuo(String userId, int level) {
		GemAltarUserInfo info = gemAltarDao.getUserInfo(userId);
		info.removeAuto(level);
		gemAltarDao.removeAuto(userId, level);
	}

	public void openAuto(String userId, int levels) {
		GemAltarUserInfo info = gemAltarDao.getUserInfo(userId);
		GemAltarUserAutoSell auto = new GemAltarUserAutoSell();
		auto.setLevels(levels);
		info.addAuto(auto);
		gemAltarDao.addAuto(userId, levels);
	}

	/** */
	public void getAllStone(String userId) {
		GemAltarUserInfo info = gemAltarDao.getUserInfo(userId);
		List<GemAltarUserPack> packs = info.getPack();
		for (GemAltarUserPack temp : packs) {
			stoneService.addStone(userId, temp.getStoneId(), 1, ToolUseType.ADD_GEM);
		}
		gemAltarDao.removeAllPack(userId);
		info.removeAllPack();
	}

	/** */
	public void getStone(String userId, int stoneId, long date) {
		Date d = new Date(date);
		GemAltarUserInfo info = gemAltarDao.getUserInfo(userId);
		GemAltarUserPack pack = info.getPack(stoneId, d);
		if (pack == null) {
			String msg = "没有奖励";
			throw new ServiceException(NO_GIFT, msg);
		}
		boolean b = info.removePack(pack);
		if (!b) {
			String msg = "没有奖励";
			throw new ServiceException(NO_GIFT, msg);
		}
		gemAltarDao.removePack(pack);
		stoneService.addStone(userId, stoneId, 1, ToolUseType.ADD_GEM);
	}

	/** */
	public GemBO show(String userId) {
		GemAltarUserInfo info = gemAltarDao.getUserInfo(userId);
		GemBO bo = BOHelper.getGemBO(info);
		return bo;
	}

	/** */
	public Map<Integer, Long> open(String userId, int groups) {
		Map<Integer, Long> maps = new HashMap<Integer, Long>();
		GemAltarUserInfo info = gemAltarDao.getUserInfo(userId);
		maps.put(3, -1L);
		if (groups != 1) {
			boolean b = info.haveGroup(groups);
			if (!b) {
				String msg = "对应祭坛未开启";
				throw new ServiceException(NO_GROUPS, msg);
			}
		}
		GemAltarGroupsOpenConfig config = gemAltarDao.getGroupsConfig(groups);

		if (!userService.reduceGold(userId, config.getConsume(), ToolUseType.REDUCE_GEM)) {
			String msg = "没钱";
			throw new ServiceException(NO_MONEY, msg);
		}

		info.removeGroup(groups);
		gemAltarDao.removeGroups(userId, groups);

		int stId = getStoneId(userId, groups);
		maps.put(1, (long) stId);
		SystemStone st = systemStoneDao.get(stId);
		if (info.haveAuto(st.getStoneLevel()) != null) {
			//stoneService.reduceStone(userId, stId, 1, ToolUseType.ADD_GEM);
			userService.addMingwen(userId, st.getSellPrice(), ToolUseType.ADD_GEM);
			maps.put(3, (long) st.getSellPrice());
			GemAltarUserSellLog log = new GemAltarUserSellLog();
			log.setCreateTime(new Date());
			log.setGold(st.getSellPrice());
			log.setStoneId(stId);
			log.setUserId(userId);
			info.addLog(log);
			gemAltarDao.addLog(log);
		} else {
			GemAltarUserPack p = new GemAltarUserPack(userId, stId, new Date());
			info.addPack(p);
			gemAltarDao.addPack(p);
			maps.put(4, p.getCreateTime().getTime());

		}
		if (openNextAltar(config)) {
			GemAltarUserOpen open = new GemAltarUserOpen(userId, groups + 1);
			info.addOpen(open);
			gemAltarDao.addGroups(open.getUserId(), open.getGroups());
			maps.put(2, (long) open.getGroups());
		}

		// 更新每日任务完成次数
		dailyTaskService.sendUpdateDailyTaskEvent(userId, SystemDailyTask.GEM, 1);
		return maps;
	}

	private boolean openNextAltar(GemAltarGroupsOpenConfig config) {
		int rand = RandomUtils.nextInt(100);
		return rand < config.getChance();
	}

	private int getStoneId(String userId, int groups) {
		List<GemAltarOpenConfig> open = gemAltarDao.getOpenConfig(groups);
		int num = RandomUtils.nextInt(100) + 1;
		int level = 1;
		for (GemAltarOpenConfig temp : open) {
			if (num <= temp.getChance()) {
				level = temp.getStoneLevel();
				break;
			}
		}

		SystemStone st = systemStoneDao.getRandomStone(level);

		if (level >= 5) {
			User user = this.userService.get(userId);
			String toolName;
			if (level == 5) {
				toolName = "非常极品的" + st.getStoneName();
			} else {
				toolName = "传说中的" + st.getStoneName();
			}
			messageService.sendGainToolMsg(userId, user.getUsername(), toolName, "宝石祭坛", "");
		}

		return st.getStoneId();
	}

	@Override
	public void toPo(String userId) {

		if (!userService.reduceGold(userId, 498, ToolUseType.REDUCE_GEM)) {
			throw new ServiceException(NO_MONEY, "金钱不足");
		}
		GemAltarUserInfo info = gemAltarDao.getUserInfo(userId);
		GemAltarUserOpen open = new GemAltarUserOpen(userId, 4);
		info.addOpen(open);
		gemAltarDao.addGroups(open.getUserId(), open.getGroups());
	}
}
