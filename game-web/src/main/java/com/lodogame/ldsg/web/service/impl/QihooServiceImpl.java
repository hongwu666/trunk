package com.lodogame.ldsg.web.service.impl;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.ldsg.web.bo.OrderBO;
import com.lodogame.ldsg.web.constants.OrderStatus;
import com.lodogame.ldsg.web.dao.PaymentOrderDao;
import com.lodogame.ldsg.web.exception.ServiceException;
import com.lodogame.ldsg.web.model.PaymentObj;
import com.lodogame.ldsg.web.model.PaymentOrder;
import com.lodogame.ldsg.web.model.PurchaseInfo;
import com.lodogame.ldsg.web.model.UserToken;
import com.lodogame.ldsg.web.model.qihoo.PayCallbackObject;
import com.lodogame.ldsg.web.sdk.GameApiSdk;
import com.lodogame.ldsg.web.sdk.QihooSdk;
import com.lodogame.ldsg.web.service.BasePartnerService;
import com.lodogame.ldsg.web.service.PartnerService;
import com.lodogame.ldsg.web.service.ServerService;
import com.lodogame.ldsg.web.util.Json;

/**
 * 360SDK
 * 
 * @author Administrator
 * 
 */
public class QihooServiceImpl extends BasePartnerService {

	private static Logger logger = Logger.getLogger(QihooServiceImpl.class);

	@Autowired
	private PaymentOrderDao paymentOrderDao;

	@Autowired
	private ServerService serverService;

	@Override
	public UserToken login(String token, String ext, String partnerId, String serverId, long timestamp, String sign, Map<String, String> params) {
		if (StringUtils.isBlank(token) || StringUtils.isBlank(partnerId) || StringUtils.isBlank(serverId) || timestamp == 0) {
			throw new ServiceException(PartnerService.PARAM_ERROR, "参数不正确");
		}
		try {
			logger.info("qihoo sessionId:" + token);
			Map<String, String> tokenInfo = QihooSdk.instance().accessToken(token);
			logger.info("tokenInfo:" + Json.toJson(tokenInfo));
			if (tokenInfo != null && tokenInfo.containsKey("access_token") && !StringUtils.isBlank(tokenInfo.get("access_token"))) {
				String accessToken = tokenInfo.get("access_token");
				String refreshToken = tokenInfo.get("refresh_token");
				logger.info("accessToken:" + accessToken);
				logger.info("refreshToken:" + refreshToken);
				Map<String, String> userInfoMap = QihooSdk.instance().getUserInfo(tokenInfo.get("access_token"));
				logger.info("userInfoMap:" + userInfoMap);
				if (userInfoMap != null && userInfoMap.containsKey("id")) {
					int port = serverService.getServerHttpPort(serverId);
					int closeReg = serverService.getServerCloseRegStatus(serverId);
					UserToken userToken = GameApiSdk.getInstance().loadUserToken(userInfoMap.get("id"), partnerId, serverId, port, closeReg, "0", params);
					userToken.setPartnerToken(accessToken);
					userToken.setExtInfo(refreshToken);
					return userToken;
				}
			}

		} catch (ServiceException se) {
			throw se;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(PartnerService.LOGIN_VALID_FAILD, "登录验证失败");
		}

		throw new ServiceException(PartnerService.LOGIN_VALID_FAILD, "登录验证失败");
	}

	@Override
	public boolean doPayment(PaymentObj paymentObj) {
		if (paymentObj == null) {
			logger.error("paymentObj为空");
			return false;
		}
		PayCallbackObject cb = (PayCallbackObject) paymentObj;

		if (!QihooSdk.instance().checkPayCallbackSign(cb)) {
			logger.error("签名不正确" + Json.toJson(paymentObj));
			return false;
		}

		PaymentOrder order = paymentOrderDao.get(cb.getApp_order_id());

		logger.info("应用订单：" + Json.toJson(order));
		if (order == null) {
			logger.error("订单为空：" + Json.toJson(cb));
			return false;
		}

		if (order.getStatus() == OrderStatus.STATUS_FINISH) {
			logger.error("订单已经完成" + Json.toJson(cb));
			return true;
		}

		BigDecimal finishAmount = new BigDecimal(cb.getAmount()).divide(new BigDecimal(100));

		if (order.getAmount().compareTo(finishAmount) != 0) {
			logger.error("订单金额不符：" + Json.toJson(cb));
			return false;
		}

		if (!cb.getGateway_flag().equals("success")) {
			logger.error("充值失败：" + Json.toJson(cb));
			this.paymentOrderDao.updateStatus(order.getOrderId(), OrderStatus.STATUS_ERROR, cb.getOrder_id(), finishAmount, "");
			return false;
		}

		int gold = (int) (order.getAmount().doubleValue() * 10);
		String extInfo = cb.getApp_ext1();
		if (QihooSdk.instance().verifyPayment(cb)) {
			// 更新订单状态
			if (this.paymentOrderDao.updateStatus(order.getOrderId(), OrderStatus.STATUS_FINISH, cb.getOrder_id(), finishAmount, extInfo)) {
				int port = serverService.getServerHttpPort(order.getOrderId());
				// 请求游戏服，发放游戏货币
				if (!GameApiSdk.getInstance().doPayment(order.getPartnerId(), order.getServerId(), port, order.getPartnerUserId(), order.getWaresId(), "", order.getOrderId(),
						finishAmount, gold, "", "")) {
					// 如果失败，要把订单置为未支付
					this.paymentOrderDao.updateStatus(order.getOrderId(), OrderStatus.STATUS_NOT_PAY, cb.getOrder_id(), finishAmount, extInfo);
					logger.error("发货失败：" + Json.toJson(cb));
					return false;
				} else {
					logger.info("支付成功：" + Json.toJson(cb));
					return true;
				}
			}
		}
		this.paymentOrderDao.updateStatus(order.getOrderId(), OrderStatus.STATUS_ERROR, cb.getOrder_id(), finishAmount, "");
		logger.error("充值失败：" + Json.toJson(cb));
		return false;
	}

	@Override
	public OrderBO createOrder(PurchaseInfo purchaseInfo) {
		OrderBO info = super.createOrder(purchaseInfo);
		info.setNotifyUrl(QihooSdk.instance().getPayCallback());
		// 360以分为单位
		info.setPrice(Integer.toString(new BigDecimal(100).multiply(new BigDecimal(info.getPrice())).intValue()));
		return info;
	}

	@Override
	public String getPayBackUrl() {
		return QihooSdk.instance().getPayCallback();
	}

}
