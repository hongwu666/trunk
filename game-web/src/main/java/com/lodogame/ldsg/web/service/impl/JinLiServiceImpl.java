package com.lodogame.ldsg.web.service.impl;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.CharEncoding;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.ldsg.web.bo.OrderBO;
import com.lodogame.ldsg.web.constants.OrderStatus;
import com.lodogame.ldsg.web.exception.ServiceException;
import com.lodogame.ldsg.web.model.PaymentObj;
import com.lodogame.ldsg.web.model.PaymentOrder;
import com.lodogame.ldsg.web.model.PurchaseInfo;
import com.lodogame.ldsg.web.model.UserToken;
import com.lodogame.ldsg.web.model.jinli.JinLiPaymentObj;
import com.lodogame.ldsg.web.sdk.GameApiSdk;
import com.lodogame.ldsg.web.sdk.JinLiSdk;
import com.lodogame.ldsg.web.service.BasePartnerService;
import com.lodogame.ldsg.web.service.PartnerService;
import com.lodogame.ldsg.web.service.ServerService;
import com.lodogame.ldsg.web.util.Json;
import com.lodogame.ldsg.web.util.jinli.PayUtil;
import com.lodogame.ldsg.web.util.jinli.RSASignature;

public class JinLiServiceImpl extends BasePartnerService {
	private static final Logger logger = Logger.getLogger(JinLiServiceImpl.class);

	@Autowired
	private ServerService serverService;

	@Override
	public UserToken login(String token, String ext, String partnerId, String serverId, long timestamp, String sign, Map<String, String> params) {
		if (StringUtils.isBlank(token) || StringUtils.isBlank(partnerId) || StringUtils.isBlank(serverId) || timestamp == 0) {
			throw new ServiceException(PartnerService.PARAM_ERROR, "参数不正确");
		}

		try {
			int port = serverService.getServerHttpPort(serverId);
			int closeReg = serverService.getServerCloseRegStatus(serverId);
			String partnerUserId = JinLiSdk.instance().verify(token);
			if (!StringUtils.isEmpty(partnerUserId)) {
				UserToken userToken = GameApiSdk.getInstance().loadUserToken(partnerUserId, partnerId, serverId, port, closeReg, "0", params);
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
	public OrderBO createOrder(PurchaseInfo order) {

		String apiKey = JinLiSdk.instance().getApiKey();
		String privateKey = JinLiSdk.instance().getPrivateKey();
		String notifyUrl = JinLiSdk.instance().getPayBackUrl();

		OrderBO orderBO = super.createOrder(order);

		PayUtil.createOrder(orderBO.getGameOrderId(), order.getPartnerUserId(), order.getTradeName(), apiKey, privateKey, order.getAmount(), orderBO.getTime(), notifyUrl);

		return orderBO;
	}

	@Override
	public boolean doPayment(PaymentObj paymentObj) {

		if (paymentObj == null) {
			logger.error("paymentObj为空");
			return false;
		}

		JinLiPaymentObj cb = (JinLiPaymentObj) paymentObj;

		boolean isValid = false;
		try {
			isValid = RSASignature.doCheck(cb.getContent(), cb.getSign(), JinLiSdk.instance().getPublicKey(), CharEncoding.UTF_8);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}

		if (!isValid) {
			logger.error("签名验证失败：" + Json.toJson(cb));
			return false;
		}

		PaymentOrder order = paymentOrderDao.get(cb.getOrderId());

		logger.info("应用订单：" + Json.toJson(order));
		if (order == null) {
			logger.error("订单为空：" + Json.toJson(cb));
			return false;
		}

		if (order.getStatus() == OrderStatus.STATUS_FINISH) {
			logger.error("订单已经完成" + Json.toJson(cb));
			return true;
		}

		// 以分为单位
		BigDecimal finishAmount = cb.getOrderAmount();
		if (order.getAmount().compareTo(finishAmount) != 0) {
			logger.error("订单金额不符：" + Json.toJson(cb));
			return false;
		}

		int gold = (int) (order.getAmount().doubleValue() * 10);
		// 更新订单状态
		if (this.paymentOrderDao.updateStatus(order.getOrderId(), OrderStatus.STATUS_FINISH, cb.getOrderId(), finishAmount, null)) {
			int port = serverService.getServerHttpPort(order.getServerId());
			// 请求游戏服，发放游戏货币
			if (!GameApiSdk.getInstance().doPayment(order.getPartnerId(), order.getServerId(), port, order.getPartnerUserId(), order.getWaresId(), "", order.getOrderId(),
					finishAmount, gold, "", "")) {
				// 如果失败，要把订单置为未支付
				this.paymentOrderDao.updateStatus(order.getOrderId(), OrderStatus.STATUS_NOT_PAY, cb.getOrderId(), finishAmount, null);
				logger.error("发货失败：" + Json.toJson(cb));
				return false;
			} else {
				logger.info("支付成功：" + Json.toJson(cb));
				return true;
			}
		}
		this.paymentOrderDao.updateStatus(order.getOrderId(), OrderStatus.STATUS_ERROR, cb.getOrderId(), finishAmount, "");
		logger.error("充值失败：" + Json.toJson(cb));
		return false;
	}

	@Override
	public String getPayBackUrl() {
		return JinLiSdk.instance().getPayBackUrl();
	}

}
