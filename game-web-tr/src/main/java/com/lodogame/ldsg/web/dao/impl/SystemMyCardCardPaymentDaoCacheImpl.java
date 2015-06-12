package com.lodogame.ldsg.web.dao.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.ldsg.web.dao.SystemMyCardCardPaymentDao;
import com.lodogame.ldsg.web.model.mycard.SystemMyCardCardPayment;

public class SystemMyCardCardPaymentDaoCacheImpl implements SystemMyCardCardPaymentDao {

	private SystemMyCardPaymentDaoMysqlImpl systemMyCardPaymentDaoMysqlImpl;
	public void setSystemMyCardPaymentDaoMysqlImpl(SystemMyCardPaymentDaoMysqlImpl systemMyCardPaymentDaoMysqlImpl) {
		this.systemMyCardPaymentDaoMysqlImpl = systemMyCardPaymentDaoMysqlImpl;
	}
	
	private Map<String, SystemMyCardCardPayment> cache = new ConcurrentHashMap<String, SystemMyCardCardPayment>();

	@Override
	public SystemMyCardCardPayment get(String waresId) {
		SystemMyCardCardPayment payment = cache.get(waresId);
		
		return payment;
	}
	
	public void init() {
		initCache();
	}
	
	private void initCache() {
		List<SystemMyCardCardPayment> list = systemMyCardPaymentDaoMysqlImpl.getAll();
		
		for (SystemMyCardCardPayment payment : list) {
			cache.put(payment.getWaresId(), payment);
		}
	}
}
