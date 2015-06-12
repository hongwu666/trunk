package com.lodogame.ldsg.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.SystemPriceDao;
import com.lodogame.ldsg.service.PriceService;
import com.lodogame.model.SystemPrice;

public class PriceServiceImpl implements PriceService {

	@Autowired
	private SystemPriceDao systemPriceDao;

	@Override
	public int getPrice(int type, int times) {

		SystemPrice systemPrice = this.systemPriceDao.get(type, times);
		if (systemPrice == null) {
			List<SystemPrice> list = this.systemPriceDao.getList(type);
			for (SystemPrice s : list) {
				if (systemPrice == null || systemPrice.getAmount() < s.getAmount()) {
					systemPrice = s;
				}
			}
		}

		return systemPrice.getAmount();
	}
}
