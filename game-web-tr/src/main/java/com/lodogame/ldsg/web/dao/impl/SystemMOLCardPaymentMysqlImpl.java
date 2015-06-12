package com.lodogame.ldsg.web.dao.impl;

import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.ldsg.web.dao.SystemMOLCardPaymentDao;
import com.lodogame.ldsg.web.model.mol.SystemMOLCardPayment;

public class SystemMOLCardPaymentMysqlImpl implements SystemMOLCardPaymentDao {

	@Autowired
	private Jdbc jdbc;
	
	public List<SystemMOLCardPayment> getAll() {
		String sql = "SELECT * FROM system_mol_card_payment";
		return this.jdbc.getList(sql, SystemMOLCardPayment.class);
	}
	
	@Override
	public SystemMOLCardPayment get(String waresId) {
		throw new NotImplementedException();
	}

	@Override
	public SystemMOLCardPayment get(String currencyCode, double amount) {
		String sql = "SELECT * FROM system_mol_card_payment WHERE currency_code = ? AND amount = ?";
		SqlParameter param = new SqlParameter();
		param.setString(currencyCode);
		param.setDouble(amount);
		
		return this.jdbc.get(sql, SystemMOLCardPayment.class, param);
	}

}
