package com.lodogame.ldsg.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.SystemVipLevelDao;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.ldsg.service.VipService;
import com.lodogame.model.SystemVipLevel;

public class VipServiceImpl implements VipService {

	@Autowired
	private SystemVipLevelDao systemVipLevelDao;

	@Autowired
	private UserService userService;

	@Override
	public int getBuyPowerLimit(int vipLevel) {
		SystemVipLevel systemVipLevel = this.systemVipLevelDao.get(vipLevel);
		return systemVipLevel.getBuyPowerLimit();
	}

	@Override
	public int getCopyBuyTime(int vipLevel) {
		SystemVipLevel systemVipLevel = this.systemVipLevelDao.get(vipLevel);
		return systemVipLevel.getBuyTreasureTimesLimit();
	}

	@Override
	public int getResetForcesTimesLimit(int vipLevel) {
		SystemVipLevel systemVipLevel = this.systemVipLevelDao.get(vipLevel);
		return systemVipLevel.getResetForcesTimes();
	}

	@Override
	public int getRefreshMysteryMallTimes(int vipLevel) {
		SystemVipLevel systemVipLevel = this.systemVipLevelDao.get(vipLevel);
		return systemVipLevel.getRefreshMysteryMallTimes();
	}

	@Override
	public int getRefreshPkMallTimes(int vipLevel) {
		SystemVipLevel systemVipLevel = this.systemVipLevelDao.get(vipLevel);
		return systemVipLevel.getRefreshPkMallTimes();
	}

	@Override
	public int getExpeditionResetTimes(int vipLevel) {
		SystemVipLevel systemVipLevel = this.systemVipLevelDao.get(vipLevel);
		return systemVipLevel.getExpeditionResetTimes();
	}

}
