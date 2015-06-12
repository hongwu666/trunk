package com.lodogame.ldsg.web.dao;

import com.lodogame.ldsg.web.model.mol.SystemMOLCardPayment;

public interface SystemMOLCardPaymentDao {

	public SystemMOLCardPayment get(String waresId);

	public SystemMOLCardPayment get(String currencyCode, double amount);

}
