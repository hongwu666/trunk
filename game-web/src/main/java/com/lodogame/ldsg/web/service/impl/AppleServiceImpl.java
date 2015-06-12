package com.lodogame.ldsg.web.service.impl;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.ldsg.web.constants.OrderStatus;
import com.lodogame.ldsg.web.exception.ServiceException;
import com.lodogame.ldsg.web.model.PaymentObj;
import com.lodogame.ldsg.web.model.PaymentOrder;
import com.lodogame.ldsg.web.model.UserToken;
import com.lodogame.ldsg.web.model.apple.ApplePaymentObj;
import com.lodogame.ldsg.web.sdk.AppleSdk;
import com.lodogame.ldsg.web.sdk.DiyiboSdk;
import com.lodogame.ldsg.web.sdk.GameApiSdk;
import com.lodogame.ldsg.web.service.BasePartnerService;
import com.lodogame.ldsg.web.service.PartnerService;
import com.lodogame.ldsg.web.service.ServerService;
import com.lodogame.ldsg.web.util.Json;

public class AppleServiceImpl extends BasePartnerService {
	private static final Logger logger = Logger.getLogger(AppleServiceImpl.class);

	@Autowired
	private ServerService serverService;

	@Override
	public UserToken login(String sessionId, String ext, String partnerId, String serverId, long timestamp, String sign, Map<String, String> params) {
		if (StringUtils.isBlank(sessionId) || StringUtils.isBlank(partnerId) || StringUtils.isBlank(serverId) || timestamp == 0 || StringUtils.isBlank(sign)) {
			throw new ServiceException(PartnerService.PARAM_ERROR, "参数不正确");
		}

		// checkSign(sessionId, partnerId, serverId, timestamp, sign);

		String uid = DiyiboSdk.instance().verifySession(sessionId);
		int port = serverService.getServerHttpPort(serverId);
		int closeReg = serverService.getServerCloseRegStatus(serverId);

		if (uid != null) {
			UserToken userToken = GameApiSdk.getInstance().loadUserToken(uid, partnerId, serverId, port, closeReg, "0", params);
			return userToken;
		}

		throw new ServiceException(PartnerService.LOGIN_VALID_FAILD, "登录验证失败");
	}

	@Override
	public boolean doPayment(PaymentObj paymentObj) {
		if (paymentObj == null) {
			logger.error("paymentObj为空");
			return false;
		}
		ApplePaymentObj cb = (ApplePaymentObj) paymentObj;
		logger.info("game id:" + cb.getGameOrderId());
		PaymentOrder order = paymentOrderDao.get(cb.getGameOrderId());
		if (order == null) {
			logger.error("订单为空：" + Json.toJson(cb));
			return false;
		}
		BigDecimal finishAmount = order.getAmount();
		int n = 0;
		while (!AppleSdk.instance().checkPayCallbackSign(cb)) {
			if (n >= 5) {
				logger.error("apple验证失败" + Json.toJson(paymentObj));
				this.paymentOrderDao.updateStatus(cb.getGameOrderId(), OrderStatus.STATUS_ERROR, cb.getAppleOrderId(), finishAmount, cb.getReceipt());
				return false;
			}
			n++;
		}
		if (!this.paymentOrderDao.countOrderByExtInfo(cb.getReceipt())) {
			logger.error("订单已经完成" + Json.toJson(cb));
			return true;
		}
		logger.info("应用订单：" + Json.toJson(order));
		if (order.getStatus() == OrderStatus.STATUS_FINISH) {
			logger.error("订单已经完成" + Json.toJson(cb));
			return true;
		}
		if (order.getAmount().compareTo(finishAmount) != 0) {
			logger.error("订单金额不符：" + Json.toJson(cb));
			return false;
		}
		int gold = (int) (order.getAmount().doubleValue() * 10);
		// 更新订单状态
		if (this.paymentOrderDao.updateStatus(order.getOrderId(), OrderStatus.STATUS_FINISH, cb.getAppleOrderId(), finishAmount, cb.getReceipt())) {

			// 请求游戏服，发放游戏货币
			int port = serverService.getServerHttpPort(order.getServerId());
			if (!GameApiSdk.getInstance().doPayment(order.getPartnerId(), order.getServerId(), port, order.getPartnerUserId(), order.getWaresId(), "", order.getOrderId(),
					finishAmount, gold, "", "")) {
				// 如果失败，要把订单置为未支付
				this.paymentOrderDao.updateStatus(order.getOrderId(), OrderStatus.STATUS_NOT_PAY, cb.getAppleOrderId(), finishAmount, cb.getReceipt());
				logger.error("发货失败：" + Json.toJson(cb));
				return false;
			} else {
				logger.info("支付成功：" + Json.toJson(cb));
				return true;
			}
		}
		this.paymentOrderDao.updateStatus(order.getOrderId(), OrderStatus.STATUS_ERROR, cb.getAppleOrderId(), finishAmount, cb.getReceipt());
		logger.error("充值失败：" + Json.toJson(cb));
		return false;
	}

	@Override
	public String getPayBackUrl() {
		return AppleSdk.instance().getPayBackUrl();
	}
}
