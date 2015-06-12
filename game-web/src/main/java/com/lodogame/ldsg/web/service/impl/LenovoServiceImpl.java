package com.lodogame.ldsg.web.service.impl;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.ldsg.web.bo.OrderBO;
import com.lodogame.ldsg.web.constants.OrderStatus;
import com.lodogame.ldsg.web.exception.ServiceException;
import com.lodogame.ldsg.web.model.PaymentObj;
import com.lodogame.ldsg.web.model.PaymentOrder;
import com.lodogame.ldsg.web.model.PurchaseInfo;
import com.lodogame.ldsg.web.model.UserToken;
import com.lodogame.ldsg.web.model.lenovo.TransData;
import com.lodogame.ldsg.web.model.lenovo.TransSyncTemp;
import com.lodogame.ldsg.web.sdk.GameApiSdk;
import com.lodogame.ldsg.web.sdk.LenovoSdk;
import com.lodogame.ldsg.web.service.BasePartnerService;
import com.lodogame.ldsg.web.service.PartnerService;
import com.lodogame.ldsg.web.service.ServerService;
import com.lodogame.ldsg.web.util.Json;

public class LenovoServiceImpl extends BasePartnerService {

	private static final Logger logger = Logger.getLogger(LenovoServiceImpl.class);

	@Autowired
	private ServerService serverService;

	@Override
	public UserToken login(String sessionId, String ext, String partnerId, String serverId, long timestamp, String sign, Map<String, String> params) {
		if (StringUtils.isBlank(sessionId) || StringUtils.isBlank(partnerId) || StringUtils.isBlank(serverId) || timestamp == 0) {
			throw new ServiceException(PartnerService.PARAM_ERROR, "参数不正确");
		}

		Map<String, String> account = null;
		try {
			account = LenovoSdk.instance().getAccount(sessionId);

			if (account != null && account.containsKey("accountId") && !StringUtils.isBlank(account.get("accountId"))) {
				String accountId = account.get("accountId");
				int port = serverService.getServerHttpPort(serverId);
				int closeReg = serverService.getServerCloseRegStatus(serverId);
				UserToken userToken = GameApiSdk.getInstance().loadUserToken(accountId, partnerId, serverId, port, closeReg, "0", params);
				return userToken;
			}

		} catch (ServiceException se) {
			throw se;
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(PartnerService.LOGIN_VALID_FAILD, "登录验证失败");
		}

		throw new ServiceException(PartnerService.LOGIN_VALID_FAILD, "登录验证失败");
	}

	@Override
	public boolean doPayment(PaymentObj paymentObj) {
		TransData data = (TransData) paymentObj;
		String jsonData = data.getTransData();
		if (LenovoSdk.instance().validSign(jsonData, data.getSign())) {
			logger.error("签名不正确" + jsonData);
		}

		TransSyncTemp cb = Json.toObject(jsonData, TransSyncTemp.class);

		PaymentOrder order = paymentOrderDao.get(cb.getExorderno());

		logger.info("应用订单：" + Json.toJson(order));
		if (order == null) {
			logger.error("订单为空：" + Json.toJson(cb));
			return false;
		}

		if (order.getStatus() == OrderStatus.STATUS_FINISH) {
			logger.error("订单已经完成" + Json.toJson(cb));
			return true;
		}

		BigDecimal finishAmount = new BigDecimal(cb.getMoney()).divide(new BigDecimal(100));

		if (order.getAmount().compareTo(finishAmount) != 0) {
			logger.error("订单金额不符：" + Json.toJson(cb));
			return false;
		}

		if (!cb.getResult().equals(0)) {
			logger.error("充值失败：" + Json.toJson(cb));
			this.paymentOrderDao.updateStatus(order.getOrderId(), OrderStatus.STATUS_ERROR, cb.getTransid(), finishAmount, "");
			return false;
		}

		int gold = (int) (order.getAmount().doubleValue() * 10);
		String extInfo = cb.getCpprivate();
		// 更新订单状态
		if (this.paymentOrderDao.updateStatus(order.getOrderId(), OrderStatus.STATUS_FINISH, cb.getTransid(), finishAmount, "")) {
			int port = serverService.getServerHttpPort(order.getServerId());
			// 请求游戏服，发放游戏货币
			if (!GameApiSdk.getInstance().doPayment(order.getPartnerId(), order.getServerId(), port, order.getPartnerUserId(), order.getWaresId(), "", order.getOrderId(),
					finishAmount, gold, "", "")) {
				// 如果失败，要把订单置为未支付
				this.paymentOrderDao.updateStatus(order.getOrderId(), OrderStatus.STATUS_NOT_PAY, cb.getTransid(), finishAmount, extInfo);
				logger.error("发货失败：" + Json.toJson(cb));
				return false;
			} else {
				logger.info("支付成功：" + Json.toJson(cb));
				return true;
			}
		}
		this.paymentOrderDao.updateStatus(order.getOrderId(), OrderStatus.STATUS_ERROR, cb.getTransid(), finishAmount, "");
		logger.error("充值失败：" + Json.toJson(cb));
		return false;
	}

	@Override
	public OrderBO createOrder(PurchaseInfo purchaseInfo) {
		OrderBO info = super.createOrder(purchaseInfo);
		info.setNotifyUrl(LenovoSdk.instance().getPayCallback());
		// 360以分为单位
		info.setPrice(Integer.toString(new BigDecimal(100).multiply(new BigDecimal(info.getPrice())).intValue()));
		return info;
	}

	@Override
	public String getPayBackUrl() {
		return LenovoSdk.instance().getPayCallback();
	}
}
