package com.lodogame.ldsg.web.dao.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.ldsg.web.dao.PaymentOrderDao;
import com.lodogame.ldsg.web.model.PaymentOrder;
import com.mysql.jdbc.StringUtils;

public class PaymentOrderDaoMysqlImpl implements PaymentOrderDao {

	private final static String table = "payment_order";

	private final static String columns = "*";

	@Autowired
	private Jdbc jdbc;

	@Override
	public boolean add(PaymentOrder paymentOrder) {
		return this.jdbc.insert(paymentOrder) > 0;
	}

	@Override
	public PaymentOrder getLastOrder(String gameId, String partnerId) {

		String sql = "SELECT " + columns + " FROM " + table + " WHERE game_id = ? AND partner_id = ? ORDER BY seq DESC LIMIT 1 ";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(gameId);
		parameter.setString(partnerId);

		return this.jdbc.get(sql, PaymentOrder.class, parameter);
	}

	@Override
	public PaymentOrder get(String orderId) {

		String sql = "SELECT " + columns + " FROM " + table + " WHERE order_id = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(orderId);

		return this.jdbc.get(sql, PaymentOrder.class, parameter);
	}

	@Override
	public boolean updateStatus(String orderId, int status, String partnerOrderId, BigDecimal finishAmount, String extInfo) {

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(status);
		parameter.setObject(finishAmount);
		
		String sql = "UPDATE " + table + " SET status = ?, finish_amount = ?, updated_time = now() ";
		if (!StringUtils.isNullOrEmpty(partnerOrderId)) {
			sql += " , partner_order_id = ? ";
			parameter.setString(partnerOrderId);
		}
		
		if (!StringUtils.isNullOrEmpty(extInfo)) {
			sql += " , ext_info=?";
			parameter.setString(extInfo);
		}
		sql += "WHERE order_id = ? ";

		parameter.setString(orderId);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean countOrderByExtInfo(String extInfo) {
		SqlParameter parameter = new SqlParameter();
		String sql = "select count(*) from payment_order where partner_id = 2001 and status = 1 and ext_info = ?";
		parameter.setString(extInfo);
		return this.jdbc.getInt(sql, parameter) == 0;
	}

	@Override
	public List<PaymentOrder> getPaymentList(int status) {

		String sql = "SELECT " + columns + " FROM " + table + " WHERE status = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(status);

		return this.jdbc.getList(sql, PaymentOrder.class, parameter);
	}

}
