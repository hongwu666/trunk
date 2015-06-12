package com.lodogame.ldsg.web.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.ldsg.web.dao.SystemMOLCardPaymentDao;
import com.lodogame.ldsg.web.model.mol.SystemMOLCardPayment;

public class SystemMOLCardPaymentDaoCacheImpl implements SystemMOLCardPaymentDao {

	Map<String, SystemMOLCardPayment> cache = new ConcurrentHashMap<String, SystemMOLCardPayment>();
	List<SystemMOLCardPayment> cacheList = new ArrayList<SystemMOLCardPayment>();
	
	private SystemMOLCardPaymentMysqlImpl systemMOLCardPaymentMysqlImpl;
	
	public void setSystemMOLCardPaymentMysqlImpl(SystemMOLCardPaymentMysqlImpl systemMOLCardPaymentMysqlImpl) {
		this.systemMOLCardPaymentMysqlImpl = systemMOLCardPaymentMysqlImpl;
	}

	@Override
	public SystemMOLCardPayment get(String waresId) {
		return cache.get(waresId);
	}
	
	private void initCache() {
		cacheList = systemMOLCardPaymentMysqlImpl.getAll();
		for (SystemMOLCardPayment payment : cacheList) {
			cache.put(payment.getWaresId(), payment);
		}
	}
	
	public void init() {
		initCache();
	}

	@Override
	public SystemMOLCardPayment get(String currencyCode, double amount) {
		for (SystemMOLCardPayment payment : cacheList) {
			if (currencyCode.equalsIgnoreCase(payment.getCurrencyCode()) && amount == payment.getAmount().doubleValue()) {
				return payment;
			}
		}
		return null;
	}
}
