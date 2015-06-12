package com.lodogame.ldsg.web.service.impl;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.ldsg.web.bo.OrderBO;
import com.lodogame.ldsg.web.constants.OrderStatus;
import com.lodogame.ldsg.web.dao.DbCacheDao;
import com.lodogame.ldsg.web.exception.ServiceException;
import com.lodogame.ldsg.web.model.PaymentObj;
import com.lodogame.ldsg.web.model.PaymentOrder;
import com.lodogame.ldsg.web.model.PurchaseInfo;
import com.lodogame.ldsg.web.model.UserToken;
import com.lodogame.ldsg.web.model.anzhi.AnZhiPaymentObj;
import com.lodogame.ldsg.web.sdk.AnZhiSdk;
import com.lodogame.ldsg.web.sdk.GameApiSdk;
import com.lodogame.ldsg.web.sdk.KupaiSdk;
import com.lodogame.ldsg.web.service.BasePartnerService;
import com.lodogame.ldsg.web.service.PartnerService;
import com.lodogame.ldsg.web.service.ServerService;
import com.lodogame.ldsg.web.util.Json;

public class KupaiServiceImpl extends BasePartnerService {
	private static final Logger logger = Logger.getLogger(KupaiServiceImpl.class);

	@Autowired
	private ServerService serverService;

	@Autowired
	private DbCacheDao dbCacheDao;

	@Override
	public UserToken login(String token, String ext, String partnerId, String serverId, long timestamp, String sign, Map<String, String> params) {
		if (StringUtils.isBlank(token) || StringUtils.isBlank(partnerId) || StringUtils.isBlank(serverId) || timestamp == 0) {
			throw new ServiceException(PartnerService.PARAM_ERROR, "参数不正确");
		}

		// checkSign(token, partnerId, serverId, timestamp, sign);

		try {
			int port = serverService.getServerHttpPort(serverId);
			int closeReg = serverService.getServerCloseRegStatus(serverId);
			Map<String, Object> retValue = KupaiSdk.instance().verifySession(token);
			if (retValue != null) {
				String partnerUserId = retValue.get("openid").toString();
				String accessToken = retValue.get("access_token").toString();
				dbCacheDao.replace("kupai_" + partnerUserId, accessToken);
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
	public boolean doPayment(PaymentObj paymentObj) {
		if (paymentObj == null) {
			logger.error("paymentObj为空");
			return false;
		}

		AnZhiPaymentObj cb = (AnZhiPaymentObj) paymentObj;

		if (!AnZhiSdk.instance().checkPayCallbackSign(cb)) {
			logger.error("签名不正确" + Json.toJson(paymentObj));
			return false;
		}
		logger.info("game id:" + cb.getAppInfo());
		PaymentOrder order = paymentOrderDao.get(cb.getAppInfo());

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
		BigDecimal finishAmount = new BigDecimal(cb.getOrderAmount()).divide(new BigDecimal(100));
		if (order.getAmount().compareTo(finishAmount) != 0) {
			logger.error("订单金额不符：" + Json.toJson(cb));
			return false;
		}

		if (!cb.getCode().equals("1")) {
			logger.error("充值失败：" + Json.toJson(cb));
			this.paymentOrderDao.updateStatus(order.getOrderId(), OrderStatus.STATUS_ERROR, cb.getOrderId(), finishAmount, cb.getMemo());
			return false;
		}

		int gold = (int) (order.getAmount().doubleValue() * 10);
		// 更新订单状态
		if (this.paymentOrderDao.updateStatus(order.getOrderId(), OrderStatus.STATUS_FINISH, cb.getOrderId(), finishAmount, cb.getMemo())) {
			int port = serverService.getServerHttpPort(order.getServerId());
			// 请求游戏服，发放游戏货币
			if (!GameApiSdk.getInstance().doPayment(order.getPartnerId(), order.getServerId(), port, order.getPartnerUserId(), order.getWaresId(), "", order.getOrderId(),
					finishAmount, gold, "", "")) {
				// 如果失败，要把订单置为未支付
				this.paymentOrderDao.updateStatus(order.getOrderId(), OrderStatus.STATUS_NOT_PAY, cb.getOrderId(), finishAmount, cb.getMemo());
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
	public OrderBO createOrder(PurchaseInfo order) {

		OrderBO orderBo = super.createOrder(order);
		String accessToken = this.dbCacheDao.get("kupai_" + order.getPartnerUserId());
		orderBo.setPrivateInfo(accessToken);
		return orderBo;
	}

	@Override
	public String getPayBackUrl() {
		return KupaiSdk.instance().getPayCallback();
	}

}
