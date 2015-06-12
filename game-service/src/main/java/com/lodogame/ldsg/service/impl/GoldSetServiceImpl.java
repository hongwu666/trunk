package com.lodogame.ldsg.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.PaymentLogDao;
import com.lodogame.game.dao.SystemActivityDao;
import com.lodogame.game.dao.SystemGoldSetDao;
import com.lodogame.ldsg.bo.SystemGoldSetBO;
import com.lodogame.ldsg.constants.ActivityId;
import com.lodogame.ldsg.helper.BOHelper;
import com.lodogame.ldsg.service.ActivityService;
import com.lodogame.ldsg.service.GoldSetService;
import com.lodogame.model.SystemActivity;
import com.lodogame.model.SystemGoldSet;

public class GoldSetServiceImpl implements GoldSetService {

	@Autowired
	private SystemGoldSetDao systemGoldSetDao;

	@Autowired
	private PaymentLogDao paymentLogDao;

	@Autowired
	private ActivityService activityService;

	@Autowired
	private SystemActivityDao systemActivityDao;

	@Override
	public List<SystemGoldSetBO> getGoldSetList(String userId) {

		List<SystemGoldSet> systemGoldSetList = this.systemGoldSetDao.getAll();

		List<SystemGoldSetBO> systemGoldSetBOList = new ArrayList<SystemGoldSetBO>();

		boolean isDoubleOpen = this.activityService.isActivityOpen(ActivityId.FIRST_PAY_DOUBLE);
		SystemActivity systemActivity = null;
		if (isDoubleOpen) {
			systemActivity = this.systemActivityDao.get(ActivityId.FIRST_PAY_DOUBLE);
		}

		for (SystemGoldSet systemGoldSet : systemGoldSetList) {

			BigDecimal amount = systemGoldSet.getMoney();
			SystemGoldSetBO bo = BOHelper.createSystemGoldSetBO(systemGoldSet);

			if (isDoubleOpen) {

				if (systemGoldSet.isMonthlyCard()) {
					bo.setIsDouble(0);
				} else {
					boolean isAmount = paymentLogDao.isByAmount(userId, amount.doubleValue(), systemActivity.getStartTime(), systemActivity.getEndTime());
					if (isAmount) {
						bo.setIsDouble(0);
					} else {
						bo.setIsDouble(1);
					}
				}
			} else if (bo.getIsDouble() == 1) {
				boolean isAmount = paymentLogDao.isByAmount(userId, amount.doubleValue());
				if (isAmount) {
					bo.setIsDouble(0);
				}
			}

			systemGoldSetBOList.add(bo);
		}

		return systemGoldSetBOList;
	}

	@Override
	public List<SystemGoldSetBO> getGoldSetList() {

		List<SystemGoldSet> systemGoldSetList = this.systemGoldSetDao.getAll();

		List<SystemGoldSetBO> systemGoldSetBOList = new ArrayList<SystemGoldSetBO>();

		for (SystemGoldSet systemGoldSet : systemGoldSetList) {

			systemGoldSetBOList.add(BOHelper.createSystemGoldSetBO(systemGoldSet));
		}

		return systemGoldSetBOList;
	}

	@Override
	public SystemGoldSet getByPayAmount(double amount) {
		// 普通套餐
		return this.systemGoldSetDao.getByPayAmount(1, amount);
	}

	@Override
	public SystemGoldSet getMaxGoldSet() {
		// TODO Auto-generated method stub
		return null;
	}

}
