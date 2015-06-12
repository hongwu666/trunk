package com.lodogame.ldsg.web.dao.impl;

import java.math.BigDecimal;
import java.util.Date;
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
		parameter.setString(extInfo);

		String sql = "UPDATE " + table + " SET status = ?, finish_amount = ?, ext_info = ?, updated_time = now() ";
		if (!StringUtils.isNullOrEmpty(partnerOrderId)) {
			sql += " , partner_order_id = ? ";
			parameter.setString(partnerOrderId);
		}
		sql += "WHERE order_id = ? ";

		parameter.setString(orderId);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean countOrderByExtInfo(String extInfo) {
		// TODO Auto-generated method stub
		SqlParameter parameter = new SqlParameter();
		String sql = "select count(*) from `payment_order` p where p.`partner_id` = 2001 and p.`status` = 1 and p.`partner_order_id` = ?";
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

	@Override
	public List<PaymentOrder> getMyCardNotifyOrders(String extinfo) {
		String sql = "SELECT * FROM " + table + " WHERE status = 1 and ext_info =?";
		SqlParameter param = new SqlParameter();
		param.setString(extinfo);
		return this.jdbc.getList(sql, PaymentOrder.class, param);
	}

	@Override
	public boolean updateExtInfo(String gameOrderId, String extInfo) {
		String sql = "UPDATE " + table + " WHERE order_id = ? and ext_info = ?";
		SqlParameter param = new SqlParameter();
		param.setString(gameOrderId);
		param.setString(extInfo);
		return this.jdbc.update(sql, param) > 0;
	}

	@Override
	public List<PaymentOrder> getMycardSuccessByDate(Date startDate, Date endDate) {
		String sql = "select * from " + table + " where created_time >= ? and created_time <= ? and qn = 'mycard' and status = 1 and ext_info <> ''";
		SqlParameter param = new SqlParameter();
		param.setDate(startDate);
		param.setDate(endDate);
		return jdbc.getList(sql, PaymentOrder.class, param);
	}

	@Override
	public List<PaymentOrder> getMycardSuccessByExtinfo(String mycardNo) {
		String sql = "select * from " + table + " where ext_info = ? and qn = 'mycard' and status = 1";
		SqlParameter param = new SqlParameter();
		param.setString(mycardNo);
		return jdbc.getList(sql, PaymentOrder.class, param);
	}

}
