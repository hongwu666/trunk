package com.lodogame.ldsg.web.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.lodogame.ldsg.web.bo.OrderBO;
import com.lodogame.ldsg.web.constants.OrderStatus;
import com.lodogame.ldsg.web.constants.ServiceReturnCode;
import com.lodogame.ldsg.web.exception.ServiceException;
import com.lodogame.ldsg.web.model.PaymentObj;
import com.lodogame.ldsg.web.model.PaymentOrder;
import com.lodogame.ldsg.web.model.PurchaseInfo;
import com.lodogame.ldsg.web.model.UserToken;
import com.lodogame.ldsg.web.model.baidu.BaiduPaymentObj;
import com.lodogame.ldsg.web.sdk.BaiduSdk;
import com.lodogame.ldsg.web.sdk.GameApiSdk;
import com.lodogame.ldsg.web.service.BasePartnerService;
import com.lodogame.ldsg.web.service.PartnerService;
import com.lodogame.ldsg.web.util.Json;

public class BaiduServiceImpl extends BasePartnerService {

	private Logger logger = Logger.getLogger(BaiduServiceImpl.class);

	@Override
	public UserToken login(String token, String ext, String partnerId, String serverId, long timestamp, String sign, Map<String, String> params) {
		if (StringUtils.isBlank(token) || StringUtils.isBlank(partnerId) || StringUtils.isBlank(serverId) || timestamp == 0 || StringUtils.isBlank(sign)) {
			throw new ServiceException(PartnerService.PARAM_ERROR, "参数不正确");
		}

		try {
			Map<String, String> map = Json.toObject(ext, HashMap.class);
			String uid = map.get("uid");
			boolean isLogin = BaiduSdk.instance().checkSession(token);

			if (isLogin) {
				int port = serverService.getServerHttpPort(serverId);
				int closeReg = serverService.getServerCloseRegStatus(serverId);
				return GameApiSdk.getInstance().loadUserToken(uid, partnerId, serverId, port, closeReg, "0", params);
			} else {
				throw new ServiceException(PartnerService.LOGIN_VALID_FAILD, "登录验证失败");
			}

		} catch (ServiceException se) {
			throw se;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(PartnerService.LOGIN_VALID_FAILD, "登录验证失败");
		}
	}

	@Override
	public boolean doPayment(PaymentObj paymentObj) {
		if (paymentObj == null) {
			logger.error("paymentObj为空");
			return false;
		}
		BaiduPaymentObj rsp = (BaiduPaymentObj) paymentObj;
		if (!BaiduSdk.instance().checkPayCallbackSign(rsp)) {
			logger.error("签名不正确" + Json.toJson(rsp));
			throw new ServiceException(ServiceReturnCode.FAILD, "ERROR_SIGN");
		}

		PaymentOrder order = paymentOrderDao.get(rsp.getCooperatorOrderSerial());

		if (order == null) {
			logger.error("订单为空：" + Json.toJson(rsp));
			return false;
		}

		if (order.getStatus() != OrderStatus.STATUS_NEW) {
			logger.error("订单已经完成" + Json.toJson(rsp));
			throw new ServiceException(ServiceReturnCode.FAILD, "ERROR_REPEAT");
		}

		BigDecimal finishAmount = rsp.getOrderMoney();

		if (order.getAmount().compareTo(finishAmount) != 0) {

			logger.error("订单金额不符：" + Json.toJson(rsp));
			logger.error("数据库订单" + Json.toJson(order));
			return false;
		}

		if (rsp.getOrderStatus() != 1) {
			logger.error("充值失败：" + Json.toJson(rsp));
			this.paymentOrderDao.updateStatus(order.getOrderId(), OrderStatus.STATUS_ERROR, rsp.getOrderSerial(), finishAmount, Integer.toString(rsp.getOrderStatus()));
			return false;
		}

		int gold = (int) (order.getAmount().doubleValue() * 10);
		String extInfo = rsp.getExtInfo();
		// 更新订单状态
		if (this.paymentOrderDao.updateStatus(order.getOrderId(), OrderStatus.STATUS_FINISH, rsp.getOrderSerial(), finishAmount, extInfo)) {
			int port = serverService.getServerHttpPort(order.getServerId());

			// 请求游戏服，发放游戏货币
			if (!GameApiSdk.getInstance().doPayment(order.getPartnerId(), order.getServerId(), port, order.getPartnerUserId(), order.getWaresId(), "", order.getOrderId(),
					finishAmount, gold, "", "")) {
				// 如果失败，要把订单置为未支付
				return this.paymentOrderDao.updateStatus(order.getOrderId(), OrderStatus.STATUS_NOT_PAY, rsp.getOrderSerial(), finishAmount, extInfo);
			} else {
				logger.info("支付成功：" + Json.toJson(rsp));
				return true;
			}
		}

		logger.error("充值失败：" + Json.toJson(rsp));
		return false;
	}

	@Override
	public OrderBO createOrder(PurchaseInfo purchaseInfo) {
		OrderBO info = super.createOrder(purchaseInfo);
		// 以分为单位
		info.setPrice(Integer.toString(new BigDecimal(100).multiply(new BigDecimal(info.getPrice())).intValue()));
		return info;
	}

	@Override
	public String getPayBackUrl() {
		return BaiduSdk.instance().getPaybackUrl();
	}

}
