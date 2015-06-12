package com.lodogame.ldsg.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.ChatLogDao;
import com.lodogame.game.dao.CommandDao;
import com.lodogame.game.dao.LogDao;
import com.lodogame.game.dao.LogOperatorDao;
import com.lodogame.game.dao.LogPoolDao;
import com.lodogame.ldsg.config.Config;
import com.lodogame.ldsg.service.UnSynLogService;
import com.lodogame.model.log.ChatLog;
import com.lodogame.model.log.CopperLog;
import com.lodogame.model.log.GoldLog;
import com.lodogame.model.log.HeroLog;
import com.lodogame.model.log.ToolLog;
import com.lodogame.model.log.UserLevelUpLog;

public class UnSynLogServiceImpl implements UnSynLogService {

	/**
	 * 插入内部日志库用到的dao
	 */
	@Autowired
	private CommandDao commandDao;

	@Autowired
	private LogPoolDao logPoolDao;

	@Autowired
	private ChatLogDao chatLogDao;

	@Autowired
	private LogDao logDao;

	@Override
	public void chatLog(String userId, int channel, String toUserName, String content) {

		ChatLog log = new ChatLog();

		log.setContent(content);
		log.setToUserName(toUserName);
		log.setChannel(channel);
		log.setCreatedTime(new Date());
		log.setUserId(userId);

		if (Config.ins().isGameServer()) {
			logPoolDao.add(log);
		} else {
			chatLogDao.addChatLog(log);
		}
	}

	@Override
	public void goldLog(String userId, int useType, int amount, int flag, boolean success, long beforeAmount, long afterAmount) {

		if (!success) {// 成功才记数据库
			return;
		}

		GoldLog log = new GoldLog();
		log.setUserId(userId);
		log.setUseType(useType);
		log.setAmount(amount);
		log.setFlag(flag);
		log.setBeforeAmount(beforeAmount);
		log.setAfterAmount(afterAmount);

		if (Config.ins().isGameServer()) {
			logPoolDao.add(log);
		} else {
			logDao.goldLog(userId, useType, amount, flag, success, beforeAmount, afterAmount);
		}

	}

	/**
	 * 插入外部日志库用到的dao
	 */
	@Autowired
	private LogOperatorDao logOperatorDaoRedisImpl;

	public void copperLog(String userId, int useType, int amount, int flag, boolean success) {

		if (!success) {// 成功才记数据库
			return;
		}

		CopperLog log = new CopperLog();
		log.setUserId(userId);
		log.setUseType(useType);
		log.setAmount(amount);
		log.setFlag(flag);

		if (Config.ins().isGameServer()) {
			logPoolDao.add(log);
		} else {
			logDao.copperLog(userId, useType, amount, flag, success);
		}

	}

	public void toolLog(String userId, int toolType, int toolId, int num, int useType, int flag, String extinfo, boolean success) {

		if (!success) {
			return;
		}

		ToolLog log = new ToolLog();
		log.setUserId(userId);
		log.setToolId(toolId);
		log.setToolType(toolType);
		log.setUseType(useType);
		log.setFlag(flag);
		log.setExtinfo(extinfo);
		log.setNum(num);

		if (Config.ins().isGameServer()) {
			logPoolDao.add(log);
		} else {
			logDao.toolLog(userId, toolType, toolId, num, useType, flag, extinfo, success);
		}

	}

	@Override
	public void heroLog(String userId, String userHeroId, int systemHeroId, int useType, int flag, int heroExp, int heroLevel) {

		HeroLog log = new HeroLog();
		log.setUserId(userId);
		log.setUserHeroId(userHeroId);
		log.setSystemHeroId(systemHeroId);
		log.setUseType(useType);
		log.setHeroExp(heroExp);
		log.setHeroLevel(heroLevel);
		log.setFlag(flag);

		if (Config.ins().isGameServer()) {
			logPoolDao.add(log);
		} else {
			logDao.heroLog(userId, userHeroId, systemHeroId, useType, flag, heroExp, heroLevel);
		}
	}

	@Override
	public void levelUpLog(String userId, int exp, int level) {

		UserLevelUpLog log = new UserLevelUpLog();
		log.setUserId(userId);
		log.setLevel(level);
		log.setExp(exp);
		if (Config.ins().isGameServer()) {
			logPoolDao.add(log);
		} else {
			logDao.levelUpLog(userId, level, exp);
		}
	}

}
