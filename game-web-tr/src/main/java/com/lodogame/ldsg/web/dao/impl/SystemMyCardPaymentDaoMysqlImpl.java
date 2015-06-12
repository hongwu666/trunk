package com.lodogame.ldsg.web.dao.impl;

import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.ldsg.web.dao.SystemMyCardCardPaymentDao;
import com.lodogame.ldsg.web.model.mycard.SystemMyCardCardPayment;

public class SystemMyCardPaymentDaoMysqlImpl implements SystemMyCardCardPaymentDao {

	@Autowired
	private Jdbc jdbc;
	
	public List<SystemMyCardCardPayment> getAll() {
		String sql = "SELECT * FROM system_mycard_card_payment";
		return this.jdbc.getList(sql, SystemMyCardCardPayment.class);
	}

	@Override
	public SystemMyCardCardPayment get(String waresId) {
		throw new NotImplementedException();
	}

}
