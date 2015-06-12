package com.lodogame.ldsg.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.SystemVipLevelDao;
import com.lodogame.game.dao.TreasureDao;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.ldsg.bo.TreasureGiftBo;
import com.lodogame.ldsg.bo.TreasurePriceBo;
import com.lodogame.ldsg.bo.TreasureReturnBO;
import com.lodogame.ldsg.bo.TreasureShowBO;
import com.lodogame.ldsg.constants.ToolUseType;
import com.lodogame.ldsg.constants.TreasureConstant;
import com.lodogame.ldsg.event.TreasureEvent;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.helper.BOHelper;
import com.lodogame.ldsg.service.DailyTaskService;
import com.lodogame.ldsg.service.EventService;
import com.lodogame.ldsg.service.TreasureService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.SystemDailyTask;
import com.lodogame.model.SystemVipLevel;
import com.lodogame.model.TreasureConfigInfo;
import com.lodogame.model.TreasureUserInfo;
import com.lodogame.model.User;

public class TreasureServiceImpl implements TreasureService {
	private static final Logger logger = Logger.getLogger(TreasureServiceImpl.class);

	@Autowired
	private DailyTaskService dailyTaskService;

	@Autowired
	private TreasureDao treasureDao;

	@Autowired
	private EventService eventServcie;

	@Autowired
	private UserService userService;

	@Autowired
	private SystemVipLevelDao systemVipLevelDao;

	public TreasureReturnBO open(String userId, int type) {

		User user = userService.get(userId);
		TreasureUserInfo info = find(treasureDao.getUserInfo(userId, DateUtils.getDate()), type);
		SystemVipLevel vip = systemVipLevelDao.get(user.getVipLevel());

		int yb = getNeedMoney(info == null ? 1 : info.getTreasureNum());
		if (!userService.reduceGold(userId, yb, ToolUseType.REDUCE_TREASURE)) {
			String message = "元宝不足";
			throw new ServiceException(TreasureService.YB_NOT_ENOUGH, message);
		}
		if (info != null && info.getTreasureNum() >= vip.getBuyTreasureTimesLimit()) {
			String message = "剩余次数不足";
			throw new ServiceException(TreasureService.NUM_NOT_ENOUGH, message);
		}

		if (info == null) {
			info = TreasureUserInfo.create(userId, type);
			info.setDate(DateUtils.getDate());
			treasureDao.insertUserInfo(info);
		}

		int v = getGift(userId, type);

		int ts = getN(info, user);
		int jl = 0;
		switch (type) {
		case TreasureConstant.TYPE_TB:
			jl = v * ts;
			userService.addCopper(userId, jl, ToolUseType.ADD_TREASURE);
			break;
		case TreasureConstant.TYPE_WH:
			jl = v * ts;
			userService.addMuhon(userId, jl, ToolUseType.ADD_TREASURE);
			break;
		case TreasureConstant.TYPE_HL:
			jl = v * ts;
			userService.addMingwen(userId, jl, ToolUseType.ADD_TREASURE);
			break;
		case TreasureConstant.TYPE_JY:
			jl = v * ts;
			userService.addSoul(userId, jl, ToolUseType.ADD_TREASURE);
			break;
		default:
			logger.error("--------------->聚宝盆类型不正确 type=[" + type + "]");
			break;
		}
		info.setTreasureNum(info.getTreasureNum() + 1);
		treasureDao.updateUserInfo(info);
		int p = getPrice(userId, type);
		TreasureReturnBO ret = BOHelper.createTreasureReturnBO(jl, (int) ts, getGift(userId, type), p);
		// 4倍广播
		if (ts >= 4.0f) {
			TreasureEvent event = new TreasureEvent(user.getUsername());
			eventServcie.dispatchEvent(event);
		}

		// 更新每日任务
		dailyTaskService.sendUpdateDailyTaskEvent(userId, SystemDailyTask.TREASURE, 1);

		return ret;
	}

	public TreasureGiftBo getGift(String userId) {
		return BOHelper.createTreasureGiftBo(getGift(userId, TreasureConstant.TYPE_TB), getGift(userId, TreasureConstant.TYPE_WH), getGift(userId, TreasureConstant.TYPE_HL),
				getGift(userId, TreasureConstant.TYPE_JY));
	}

	private int getPrice(String userId, int type) {
		TreasureUserInfo info = find(treasureDao.getUserInfo(userId, DateUtils.getDate()), type);
		int yb = getNeedMoney(info == null ? 1 : info.getTreasureNum());
		return yb;
	}

	public TreasurePriceBo getPriceBo(String userId) {
		return BOHelper.createTreasurePriceBo(getPrice(userId, TreasureConstant.TYPE_TB), getPrice(userId, TreasureConstant.TYPE_WH), getPrice(userId, TreasureConstant.TYPE_HL),
				getPrice(userId, TreasureConstant.TYPE_JY));
	}

	public int getGift(String userId, int type) {
		User user = userService.get(userId);
		int num = 0;
		TreasureUserInfo info = find(treasureDao.getUserInfo(userId, DateUtils.getDate()), type);
		if (info != null) {
			num = info.getTreasureNum();
		}
		TreasureConfigInfo config = treasureDao.getConfigByGrade(num + 1);
		if (type == TreasureConstant.TYPE_HL) {
			return config.getHl();
		}
		int g = user.getLevel() - 1;
		int v = (int) (config.getV(type) * (1 + (g > 40 ? 40 : g) * 0.04f));
		return v;
	}

	public List<TreasureShowBO> show(String userId) {
		List<TreasureUserInfo> info = treasureDao.getUserInfo(userId, DateUtils.getDate(new Date()));
		List<TreasureShowBO> bo = new ArrayList<TreasureShowBO>();
		for (TreasureUserInfo temp : info) {
			bo.add(BOHelper.createTreasureShowBO(temp));
		}
		return bo;
	}

	private TreasureUserInfo find(List<TreasureUserInfo> list, int type) {
		for (TreasureUserInfo temp : list) {
			if (temp.getTreasureType() == type) {
				return temp;
			}
		}
		return null;
	}

	/**
	 * 所需价格
	 * 
	 * @param info
	 * @return
	 */
	private int getNeedMoney(int num) {
		TreasureConfigInfo config = treasureDao.getConfigByGrade(num + 1);
		return config.getPrice();
	}

	/**
	 * 计算倍数
	 * 
	 * @param info
	 * @param user
	 * @return
	 */
	private int getN(TreasureUserInfo info, User user) {
		int n = RandomUtils.nextInt(100);
		if (n < 60) {
			return 1;
		} else if (n < 60 + 25) {
			return 2;
		} else
			return 4;
	}
}
