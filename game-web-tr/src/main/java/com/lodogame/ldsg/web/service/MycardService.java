package com.lodogame.ldsg.web.service;

import java.util.Date;

public interface MycardService {
	/**
	 * 根据条件查询订单
	 * @param startDate
	 * @param endDate
	 * @param mycardNo
	 * @return
	 */
	public String getMycardOrders(Date startDate, Date endDate, String mycardNo);
}
