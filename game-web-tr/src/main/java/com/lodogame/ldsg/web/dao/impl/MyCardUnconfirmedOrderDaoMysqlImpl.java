package com.lodogame.ldsg.web.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.ldsg.web.model.mycard.MycardUnconfirmedOrder;

public class MyCardUnconfirmedOrderDaoMysqlImpl {

	@Autowired
	private Jdbc jdbc;
	
	private static final String TABLE = "mycard_unconfirmed_order";

	public boolean add(MycardUnconfirmedOrder unconfirmedOrder) {
		return this.jdbc.insert(unconfirmedOrder) > 0;
				
	}

	public List<MycardUnconfirmedOrder> getAllUnConfirmedOrders() {
		String sql = "SELECT * FROM " + TABLE + " WHERE status = 1";
		
		return this.jdbc.getList(sql, MycardUnconfirmedOrder.class);
	}

	public boolean update(String mycardOrderId, int status) {
		String sql = "UPDATE " + TABLE + " SET status = ? WHERE mycard_order_id = ? limit 1";
		SqlParameter param = new SqlParameter();
		param.setInt(status);
		param.setString(mycardOrderId);
		
		return this.jdbc.update(sql, param) > 0;
	}
	
	
}
