package com.lodogame.ldsg.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.ConfigDataDao;
import com.lodogame.game.dao.SystemForcesDao;
import com.lodogame.game.dao.SystemForcesMonsterDao;
import com.lodogame.game.dao.SystemMonsterDao;
import com.lodogame.game.dao.SystemPriceDao;
import com.lodogame.game.dao.UserDailyGainLogDao;
import com.lodogame.game.dao.UserForcesDao;
import com.lodogame.ldsg.bo.BattleHeroBO;
import com.lodogame.ldsg.bo.UserForcesBO;
import com.lodogame.ldsg.constants.ForcesType;
import com.lodogame.ldsg.constants.PriceType;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.constants.ToolId;
import com.lodogame.ldsg.constants.ToolType;
import com.lodogame.ldsg.constants.ToolUseType;
import com.lodogame.ldsg.constants.UserDailyGainLogType;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.helper.BOHelper;
import com.lodogame.ldsg.service.ForcesService;
import com.lodogame.ldsg.service.PriceService;
import com.lodogame.ldsg.service.ToolService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.ldsg.service.VipService;
import com.lodogame.model.SystemForces;
import com.lodogame.model.SystemForcesMonster;
import com.lodogame.model.SystemMonster;
import com.lodogame.model.User;
import com.lodogame.model.UserForces;
import com.lodogame.model.UserForcesCount;

public class ForcesServiceImpl implements ForcesService {

	private static final Logger LOG = Logger.getLogger(ForcesServiceImpl.class);

	@Autowired
	private UserForcesDao userForcesDao;

	@Autowired
	private SystemForcesMonsterDao systemForcesMonsterDao;

	@Autowired
	private SystemMonsterDao systemMonsterDao;

	@Autowired
	private UserDailyGainLogDao userDailyGainLogDao;

	@Autowired
	private VipService vipService;

	@Autowired
	private PriceService priceService;

	@Autowired
	private UserService userService;

	@Autowired
	private ConfigDataDao configDataDao;

	@Autowired
	private ToolService toolService;

	@Autowired
	private SystemForcesDao systemForcesDao;

	@Autowired
	private SystemPriceDao systemPriceDao;

	public List<UserForcesBO> getUserForcesList(String userId, int sceneId) {

		List<UserForcesBO> userForcesBOList = new ArrayList<UserForcesBO>();

		List<UserForces> userForcesList = this.userForcesDao.getUserForcesList(userId, sceneId);

		for (UserForces userForces : userForcesList) {
			userForcesBOList.add(this.createUserForcesBO(userForces));
		}

		Map<Integer, Integer> result = this.userForcesDao.getUserResetTimes(userId);

		for (UserForcesBO userForcesBO : userForcesBOList) {
			int times = 0;
			if (result.containsKey(userForcesBO.getGroupId())) {
				times = result.get(userForcesBO.getGroupId());
			}

			int price = this.priceService.getPrice(PriceType.RESET_FORCES_TIMES, times + 1);
			userForcesBO.setResetMoney(price);
		}

		return userForcesBOList;
	}

	public List<BattleHeroBO> getForcesHeroBOList(int forcesId) {

		List<BattleHeroBO> battleHeroList = new ArrayList<BattleHeroBO>();

		List<SystemForcesMonster> forcesMonsterList = this.systemForcesMonsterDao.getForcesMonsterList(forcesId);

		Set<Integer> posList = new HashSet<Integer>();

		for (SystemForcesMonster systemForcesMonster : forcesMonsterList) {
			int monsterId = systemForcesMonster.getMonsterId();
			SystemMonster systemMonster = this.systemMonsterDao.get(monsterId);
			if (systemMonster == null) {
				LOG.error("异常数据，怪物部队的怪物不存在.forcesId[" + forcesId + "], monsterId[" + monsterId + "]");
				continue;
			}
			BattleHeroBO battleHeroBO = BOHelper.createBattleHeroBO(systemMonster);

			int pos = systemForcesMonster.getPos();

			// 容错代码
			if (posList.contains(pos)) {
				continue;
			} else {
				posList.add(pos);
			}

			battleHeroBO.setPos(pos);
			battleHeroBO.setImgageType(systemForcesMonster.getForcesType());

			battleHeroList.add(battleHeroBO);
		}

		return battleHeroList;
	}

	@Override
	public UserForcesBO getUserCurrentForces(String userId, int forcesType) {

		UserForces userForces = this.userForcesDao.getUserCurrentForces(userId, forcesType);
		if (userForces != null) {
			return createUserForcesBO(userForces);
		}

		return null;
	}

	private UserForcesBO createUserForcesBO(UserForces userForces) {

		if (!DateUtils.isSameDay(userForces.getUpdatedTime(), new Date())) {
			userForces.setTimes(0);
		}

		UserForcesBO userForcesBO = BOHelper.createUserForcesBO(userForces);

		return userForcesBO;
	}

	@Override
	public List<UserForcesCount> listOrderByForceCntDesc(int offset, int size) {
		return userForcesDao.listOrderByForceCntDesc(offset, size);
	}

	@Override
	public boolean resetForcesTimes(String userId, int groupId) {

		User user = this.userService.get(userId);
		int vipLevel = user.getVipLevel();

		int resetForcesTimesLimit = this.vipService.getResetForcesTimesLimit(vipLevel);
		int userDayResetForcesTimes = this.userForcesDao.getUserResetTimes(userId, groupId);

		if (userDayResetForcesTimes >= resetForcesTimesLimit) {// 次数不足
			String message = "重置副本失败，次数不足.userId[" + userId + "], resetForcesTimesLimit[" + resetForcesTimesLimit + "], userDayResetForcesTimes[" + userDayResetForcesTimes + "]";
			LOG.info(message);
			throw new ServiceException(RESET_FORCES_TIMES_TIMES_LIMIT, message);
		}

		int needMoney = priceService.getPrice(PriceType.RESET_FORCES_TIMES, userDayResetForcesTimes + 1);
		if (!this.userService.reduceGold(userId, needMoney, ToolUseType.REDUCE_RESET_FORCES_TIMES)) {
			String message = "重置副本失败，金币不足.userId[" + userId + "], needMoney[" + needMoney + "]";
			LOG.info(message);
			throw new ServiceException(RESET_FORCES_MONEY_NOT_ENOUGH, message);
		}

		// 保存
		this.userForcesDao.setUserResetTimes(userId, groupId, userDayResetForcesTimes + 1);

		return this.userForcesDao.resetForcesTimes(userId, groupId);

	}

	@Override
	public boolean passForcesBattle(String userId) {

		if (!this.toolService.reduceTool(userId, ToolType.MATERIAL, ToolId.TOOL_ID_PASS_BATTLE, 1, ToolUseType.REDUCE_PASS_FORCES_BATTLE)) {
			String message = "跳过战斗失败，材料不足.userId[" + userId + "], toolId[" + ToolId.TOOL_ID_PASS_BATTLE + "]";
			LOG.info(message);
			throw new ServiceException(RESET_FORCES_MONEY_NOT_ENOUGH, message);
		}

		return true;
	}

	@Override
	public Map<String, String> getForcesTimes(int type) {
		return null;
	}

	@Override
	public int updateForcesTime(int forcesId, int times) {
		return 0;
	}

}
