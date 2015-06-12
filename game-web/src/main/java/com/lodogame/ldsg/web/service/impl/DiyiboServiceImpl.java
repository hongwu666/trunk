package com.lodogame.ldsg.web.service.impl;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.lodogame.ldsg.web.constants.OrderStatus;
import com.lodogame.ldsg.web.exception.ServiceException;
import com.lodogame.ldsg.web.model.PaymentObj;
import com.lodogame.ldsg.web.model.PaymentOrder;
import com.lodogame.ldsg.web.model.UserToken;
import com.lodogame.ldsg.web.sdk.DiyiboSdk;
import com.lodogame.ldsg.web.sdk.GameApiSdk;
import com.lodogame.ldsg.web.service.BasePartnerService;
import com.lodogame.ldsg.web.service.PartnerService;

public class DiyiboServiceImpl extends BasePartnerService {

	public static Logger LOG = Logger.getLogger(DiyiboServiceImpl.class);

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

	public boolean doPayment(String orderId, String partnerUserId, boolean success, String partnerOrderId, BigDecimal finishAmount) {

		// 第一波回调时传回来的金额是以 “分”为单位，要转成以 “元” 为单位
		finishAmount = finishAmount.divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);
		LOG.info("向第一波支付成功金额：" + finishAmount + " 元");

		PaymentOrder paymentOrder = this.paymentOrderDao.get(orderId);
		if (paymentOrder == null) {
			LOG.error("订单支付回调失败，订单不存在.orderId[" + orderId + "]");
			return false;
		}

		if (paymentOrder.getStatus() == OrderStatus.STATUS_FINISH) {
			LOG.error("订单支付回调成功，订单已经处理过.orderId[" + orderId + "]");
			return true;
		}

		if (paymentOrder.getStatus() != OrderStatus.STATUS_NEW) {
			LOG.error("订单支付回调失败，订单不在可修改状态.orderId[" + orderId + "]");
			return false;
		}

		String extInfo = "";

		if (success) {// 成功
			int gold = (int) (finishAmount.doubleValue() * 10);

			// 更新订单状态
			if (this.paymentOrderDao.updateStatus(orderId, OrderStatus.STATUS_FINISH, partnerOrderId, finishAmount, extInfo)) {
				int port = serverService.getServerHttpPort(paymentOrder.getServerId());
				// 请求游戏服，发放游戏货币
				if (!GameApiSdk.getInstance().doPayment(paymentOrder.getPartnerId(), paymentOrder.getServerId(), port, partnerUserId, paymentOrder.getWaresId(), "", orderId,
						finishAmount, gold, "", "")) {
					// 如果失败，要把订单置为未支付
					this.paymentOrderDao.updateStatus(orderId, OrderStatus.STATUS_NOT_PAY, partnerOrderId, finishAmount, extInfo);
					return false;
				}
			}

		} else {
			this.paymentOrderDao.updateStatus(orderId, OrderStatus.STATUS_ERROR, partnerOrderId, finishAmount, extInfo);
		}

		return true;
	}

	@Override
	public boolean doPayment(PaymentObj paymentObj) {

		return false;
	}

	@Override
	public String getPayBackUrl() {
		return DiyiboSdk.instance().getPayBackUrl();
	}
}
