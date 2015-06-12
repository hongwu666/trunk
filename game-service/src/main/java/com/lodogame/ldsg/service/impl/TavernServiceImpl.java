package com.lodogame.ldsg.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.ConfigDataDao;
import com.lodogame.game.dao.RuntimeDataDao;
import com.lodogame.game.dao.TavernDropToolDao;
import com.lodogame.game.dao.TavernGroupRateDao;
import com.lodogame.game.dao.TavernRewardDao;
import com.lodogame.game.dao.UserDailyGainLogDao;
import com.lodogame.game.dao.UserExtinfoDao;
import com.lodogame.game.dao.UserTavernDao;
import com.lodogame.game.dao.UserTavernLogDao;
import com.lodogame.game.utils.IDGenerator;
import com.lodogame.game.utils.JedisUtils;
import com.lodogame.game.utils.RedisKey;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.UserTavernBO;
import com.lodogame.ldsg.constants.ConfigKey;
import com.lodogame.ldsg.constants.InitDefine;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.constants.TavernConstant;
import com.lodogame.ldsg.constants.ToolUseType;
import com.lodogame.ldsg.constants.UserDailyGainLogType;
import com.lodogame.ldsg.event.DropHeroEvent;
import com.lodogame.ldsg.event.TavernDrawEvent;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.helper.RollHalper;
import com.lodogame.ldsg.helper.TavernHelper;
import com.lodogame.ldsg.ret.TavernDrawRet;
import com.lodogame.ldsg.service.ActivityTaskService;
import com.lodogame.ldsg.service.EventService;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.TavernService;
import com.lodogame.ldsg.service.ToolService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.RuntimeData;
import com.lodogame.model.SystemHero;
import com.lodogame.model.TavernAmendDropTool;
import com.lodogame.model.TavernDropTool;
import com.lodogame.model.TavernGroupRate;
import com.lodogame.model.TavernReward;
import com.lodogame.model.User;
import com.lodogame.model.UserExtinfo;
import com.lodogame.model.UserTavern;

public class TavernServiceImpl implements TavernService {

	@Autowired
	private TavernDropToolDao tavernDropToolDao;
	@Autowired
	private TavernGroupRateDao tavernGroupRateDao;

	@Autowired
	private UserTavernDao userTavernDao;

	@Autowired
	private UserService userService;

	@Autowired
	private HeroService heroService;

	@Autowired
	private UserExtinfoDao userExtinfoDao;

	@Autowired
	private UserTavernLogDao userTavernLogDao;

	@Autowired
	private ConfigDataDao configDataDao;

	@Autowired
	private RuntimeDataDao runtimeDataDao;

	@Autowired
	private TavernRewardDao tavernRewardDao;

	@Autowired
	private ActivityTaskService activityTaskService;

	@Autowired
	private EventService eventServcie;

	@Autowired
	private ToolService toolService;

	@Autowired
	private UserDailyGainLogDao userDailyGainLogDao;

	/**
	 * 获取冷却时间
	 * 
	 * @param type
	 * @return
	 */

	public TavernDrawRet draw(String userId, int type, int times) {

		this.checkHeroBag(userId);
		User user = userService.get(userId);
		int dayTimes = 0;
		if (type == TavernConstant.DRAW_TYPE_1) {
			dayTimes = userDailyGainLogDao.getUserDailyGain(userId, UserDailyGainLogType.TAVERN_TYPE_1);
			if (dayTimes >= 3) {
				throw new ServiceException(DRAW_NOT_ENOUGH_TIMES, "抽将失败，次数不足[" + userId + "]");
			}
		}

		// 用户的抽奖信息
		UserTavern userTavern = this.getUserTavern(userId, type);
		// 判断是否要花金币
		boolean isNeedMoney = this.isNeedMoney(userTavern, type, times);
		// 扣钱(银币或者金币)
		this.costMoney(userId, type, times, isNeedMoney);
		// 是否首次抽将
		int drawTimes = userTavern.getTotalTimes();

		// 计算掉落(正常掉掉落)
		List<TavernDropTool> tavernDropTools =  new ArrayList<TavernDropTool>();

		// 计算掉落(修正掉落)
		List<TavernAmendDropTool> tavernAmendDropTools = this.tavernDropToolDao.getTavernAmendDropToolList(1);

		// 计算掉落
		boolean amend = false;
		int amendTime = getAmendTimesLimit(type);
		if (amendTime > 0 && (userTavern.getAmendTimes() > 0 && (userTavern.getAmendTimes() + 1) % amendTime == 0)) {
			amend = true;
		}

		// 根据类型获得分组id
		List<TavernGroupRate> list = this.tavernGroupRateDao.getList(type);
		for (int i = 0; i < times; i++) {

			int rand = RandomUtils.nextInt(10000) + 1;
			TavernGroupRate tavernGroup = null;
			for (TavernGroupRate tavernGroupRate : list) {
				if (tavernGroupRate.getLowerNum() <= rand && tavernGroupRate.getUpperNum() >= rand) {
					tavernGroup = tavernGroupRate;
				}
			}
			if (tavernGroup == null) {
				return null;
			}
			int groupId = tavernGroup.getGroupId();

			// 计算掉落(正常掉掉落)
			List<TavernDropTool> tavernDropTool = this.tavernDropToolDao.getTavernDropToolList(groupId);

			tavernDropTool = TavernHelper.upListByVip(user.getVipLevel(), tavernDropTool);

			TavernDropTool tavernDrop = (TavernDropTool) RollHalper.roll(tavernDropTool);
			tavernDropTools.add(tavernDrop);
		}
		List<TavernDropTool> tavernDropToolList = TavernHelper.draw(type, tavernDropTools, tavernAmendDropTools, times, drawTimes, amend);

		boolean resetAmend = false;
		if (times == 1 && tavernDropToolList.get(0).getGroupId() == 5) {
			resetAmend = true;
		}

		this.updateUserTavern(userId, type, times, userTavern, isNeedMoney, resetAmend);

		// 获取掉落武将
		Map<String, Integer> heroIdMap = pickDrawTool(userId, tavernDropToolList);

		UserTavernBO userTavernBO = new UserTavernBO();
		int amendTimes = userTavern.getAmendTimes();
		if (amendTime > 0 && times == 1) {
			amendTimes = amendTimes + 1;
		}

		if (resetAmend) {
			amendTimes = 0;
		}

		userTavernBO.setAmendTimes(12 - ((amendTimes % 12)));
		userTavernBO.setTimes(3 - dayTimes - 1);
		userTavernBO.setType(type);
		userTavernBO.setCoolTime(getCoolTime(userTavern, isNeedMoney, type));

		CommonDropBO commonDropBO = null;
		if (type == TavernConstant.DRAW_TYPE_2) {
			TavernReward tavernReward = this.getTavernReward(times);
			if (tavernReward != null) {
				commonDropBO = this.toolService.give(userId, tavernReward.getToolType(), tavernReward.getToolId(), tavernReward.getToolNum(), ToolUseType.ADD_TAVERN_REWARD);
			}
		}
		commonDropBO = commonDropBO == null ? new CommonDropBO() : commonDropBO;

		// 抽将事件
		TavernDrawEvent tavernDrawEvent = new TavernDrawEvent(userId, type, isNeedMoney, times);
		eventServcie.dispatchEvent(tavernDrawEvent);

		if (type == TavernConstant.DRAW_TYPE_1) {
			userDailyGainLogDao.addUserDailyGain(userId, UserDailyGainLogType.TAVERN_TYPE_1, 1);
		}

		return new TavernDrawRet(new ArrayList<String>(heroIdMap.keySet()), userTavernBO, commonDropBO);
	}

	/**
	 * 获取抽将奖励
	 * 
	 * @param times
	 * @return
	 */
	private TavernReward getTavernReward(int times) {

		int type = times == 1 ? 1 : 2;
		List<TavernReward> list = this.tavernRewardDao.getList(type);
		int rand = RandomUtils.nextInt(10000);
		for (TavernReward tavernReward : list) {
			if (tavernReward.getLowerNum() <= rand && rand <= tavernReward.getUpperNum()) {
				return tavernReward;
			}
		}
		return null;

	}

	private long getCoolTime(UserTavern userTavern, boolean isNeedMoney, int type) {
		if (isNeedMoney) {
			return userTavern.getUpdatedTime().getTime() + TavernHelper.getCoolTimeInterval(type);
		} else {
			return System.currentTimeMillis() + TavernHelper.getCoolTimeInterval(type);
		}

	}

	/**
	 * 获取修正值上限
	 * 
	 * @param type
	 * @return
	 */
	private int getAmendTimesLimit(int type) {

		int amendLimitTimes = 0;

		switch (type) {
		case TavernConstant.DRAW_TYPE_1:
			amendLimitTimes = this.configDataDao.getInt(ConfigKey.TAVERN_AMEND_TIMES_1 + type, 0);
			break;
		case TavernConstant.DRAW_TYPE_2:
			amendLimitTimes = this.configDataDao.getInt(ConfigKey.TAVERN_AMEND_TIMES_2 + type, 12);
			break;
		case TavernConstant.DRAW_TYPE_3:
			amendLimitTimes = this.configDataDao.getInt(ConfigKey.TAVERN_AMEND_TIMES_3 + type, 40);
			break;
		}

		return amendLimitTimes;
	}

	/**
	 * 获取当前掉落池的游标
	 * 
	 * @param type
	 * @return
	 */
	private long getTavernDropToolInd(int type, int times) {

		String key = RedisKey.getTavernDropPoolIndKey(type);
		if (!JedisUtils.exists(key)) {
			RuntimeData runtimeData = this.runtimeDataDao.get(key);
			long totalTimes = 0;
			if (runtimeData != null) {
				totalTimes = runtimeData.getValue();
				JedisUtils.incrBy(key, totalTimes);
			} else {
				runtimeData = new RuntimeData();
				runtimeData.setCreatedTime(new Date());
				runtimeData.setValue(0);
				runtimeData.setValueKey(key);
				this.runtimeDataDao.add(runtimeData);
			}
		}

		long value = JedisUtils.incrBy(key, times);

		// 更新到数据库，防止数据丢失，可以移到其它地方做
		this.runtimeDataDao.set(key, value);

		return value;
	}

	/**
	 * 获取用户抽奖记录信息
	 * 
	 * @param userId
	 * @param type
	 * @return
	 */
	@Override
	public UserTavern getUserTavern(String userId, int type) {

		UserTavern userTavern = this.userTavernDao.get(userId, type);

		if (userTavern == null) {

			Date now = new Date();
			userTavern = new UserTavern();
			userTavern.setType(type);
			userTavern.setUserId(userId);
			userTavern.setCreatedTime(now);
			userTavern.setUpdatedTime(now);
			userTavern.setTotalTimes(0);
			userTavern.setAmendTimes(0);
			userTavern.setHadUsedMoney(0);
			this.userTavernDao.add(userTavern);
		}

		return userTavern;
	}

	/**
	 * 判断当前武将背包是否已经满意了
	 * 
	 * @param userId
	 */
	private void checkHeroBag(String userId) {
		int userHeroCount = this.heroService.getUserHeroCount(userId);

		UserExtinfo userExtinfo = this.userExtinfoDao.get(userId);
		int bagMax = InitDefine.HERO_BAG_INIT;
		if (userExtinfo != null) {
			bagMax = userExtinfo.getHeroMax();
		}

		if (userHeroCount + 1 > bagMax) {
			throw new ServiceException(ServiceReturnCode.USER_HERO_BAG_LIMIT_EXCEED, "抽奖失败,武将背包超过上限.userId[" + userId + "]");
		}
	}

	private void costMoney(String userId, int type, int times, boolean isNeedMoney) {

		// 如果需要花钱
		if (isNeedMoney) {

			// 用金币
			int money = TavernHelper.getCostMoney(type, times);
			if (!this.userService.reduceGold(userId, money, ToolUseType.REDUCE_TAVERN_DRAW)) {
				String message = "抽奖失败，金币不足.userId[" + userId + "], type[" + type + "]";
				throw new ServiceException(DRAW_NOT_ENOUGH_GOLD, message);
			}

		}

	}

	private Map<String, Integer> pickDrawTool(String userId, List<TavernDropTool> tavernDropToolList) {
		Map<String, Integer> heroIdMap = new HashMap<String, Integer>();
		for (TavernDropTool tavernDropTool : tavernDropToolList) {

			int systemHeroId = tavernDropTool.getSystemHeroId();

			String userHeroId = IDGenerator.getID();
			heroIdMap.put(userHeroId, systemHeroId);

			SystemHero systemHero = this.heroService.getSysHero(systemHeroId);
			// 英雄星数大于等于5星,发送跑马灯
			if (systemHero.getHeroStar() >= 5) {
				User user = this.userService.get(userId);
				DropHeroEvent toolDropEvent = new DropHeroEvent(userId, user.getUsername(), systemHero.getHeroName(), systemHero.getHeroStar(), "酒馆中");
				eventServcie.dispatchEvent(toolDropEvent);
			}

		}

		this.heroService.addUserHero(userId, heroIdMap, ToolUseType.ADD_TAVERN_DROP_HERO);
		return heroIdMap;
	}

	/**
	 * 更新用户抽奖信息
	 * 
	 * @param userId
	 * @param type
	 * @param times
	 * @param userTavern
	 * @param isNeedMoney
	 * @param resetAmend
	 * @return
	 */
	private void updateUserTavern(String userId, int type, int times, UserTavern userTavern, boolean isNeedMoney, boolean resetAmend) {

		Date updatedTime = null;
		if (isNeedMoney == false) {
			updatedTime = new Date();
		}

		// 修改抽奖记录
		int totalTimes = userTavern.getTotalTimes() + times;

		int amendTimes = userTavern.getAmendTimes();
		if (times == 1) {
			amendTimes = userTavern.getAmendTimes() + 1;
		}

		if (resetAmend) {
			amendTimes = 0;
		}

		this.userTavernDao.updateTavernInfo(userId, type, updatedTime, totalTimes, amendTimes, 0);

	}

	/**
	 * 是否需要花费金币
	 * 
	 * @param userTavern
	 * @return
	 */
	private boolean isNeedMoney(UserTavern userTavern, int type, int times) {

		boolean costMoney = false;

		if (userTavern == null) {
			return false;
		}

		if (type == TavernConstant.DRAW_TYPE_1) {
			if (isCoolTimeFinished(userTavern)) {
				costMoney = false;
			} else {
				costMoney = true;
			}
		} else {

			if (times > 1) {// 不是1次的肯定要钱
				return true;
			}

			if (isCoolTimeFinished(userTavern)) {
				costMoney = false;
			} else {
				costMoney = true;
			}
		}

		return costMoney;
	}

	@Override
	public boolean isCoolTimeFinished(UserTavern userTavern) {
		long coolTimeInterval = TavernHelper.getCoolTimeInterval(userTavern.getType());

		if (userTavern != null) {
			Date updateTime = userTavern.getUpdatedTime();
			Date now = new Date();
			if (updateTime.getTime() + coolTimeInterval > now.getTime()) {
				// 冷却时间未到
				return false;
			}

		}
		return true;
	}

	@Override
	public List<UserTavernBO> getTavernCDInfo(String userId) {
		List<UserTavern> list = userTavernDao.getList(userId);

		Map<Integer, UserTavern> tavernMap = new HashMap<Integer, UserTavern>();
		for (UserTavern userTavern : list) {
			tavernMap.put(userTavern.getType(), userTavern);
		}

		List<UserTavernBO> boList = new ArrayList<UserTavernBO>();
		for (int i = 0; i < 3; i++) {
			UserTavernBO bo = new UserTavernBO();

			if (tavernMap.containsKey(i)) {
				UserTavern userTavern = tavernMap.get(i);
				bo.setType(userTavern.getType());
				long coolTimeInterval = TavernHelper.getCoolTimeInterval(userTavern.getType());
				bo.setCoolTime(userTavern.getUpdatedTime().getTime() + coolTimeInterval);
				bo.setNeedGold(TavernHelper.getCostMoney(userTavern.getType(), 1));
				boList.add(bo);
				// type = 3 增加tips
				if (userTavern.getType() == TavernConstant.DRAW_TYPE_2) {
					bo.setAmendTimes(12 - (userTavern.getAmendTimes() % 12));
				}

				// 今天抽了多少次
				if (userTavern.getType() == TavernConstant.DRAW_TYPE_1) {
					int remainTimes = 3 - userDailyGainLogDao.getUserDailyGain(userId, UserDailyGainLogType.TAVERN_TYPE_1);
					if (remainTimes < 0) {
						remainTimes = 0;
					}
					bo.setTimes(remainTimes);
				}

			} else {
				bo.setType(i);
				bo.setCoolTime(System.currentTimeMillis());
				bo.setNeedGold(TavernHelper.getCostMoney(i, 1));

				boList.add(bo);
			}
		}
		return boList;
	}
}
