package com.lodogame.ldsg.web.service.impl;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.ldsg.web.constants.OrderStatus;
import com.lodogame.ldsg.web.dao.PaymentOrderDao;
import com.lodogame.ldsg.web.exception.ServiceException;
import com.lodogame.ldsg.web.model.PaymentObj;
import com.lodogame.ldsg.web.model.PaymentOrder;
import com.lodogame.ldsg.web.model.UserToken;
import com.lodogame.ldsg.web.model.uc.PayCallbackResponse;
import com.lodogame.ldsg.web.model.uc.SidInfoResponse;
import com.lodogame.ldsg.web.sdk.GameApiSdk;
import com.lodogame.ldsg.web.sdk.UcSdk;
import com.lodogame.ldsg.web.service.BasePartnerService;
import com.lodogame.ldsg.web.service.PartnerService;
import com.lodogame.ldsg.web.service.ServerService;
import com.lodogame.ldsg.web.util.Json;

public class UcServiceImpl extends BasePartnerService {

	private final static Logger logger = Logger.getLogger(UcServiceImpl.class);

	@Autowired
	private PaymentOrderDao paymentOrderDao;

	@Autowired
	private ServerService serverService;

	@Override
	public UserToken login(String sessionId, String ext, String partnerId, String serverId, long timestamp, String sign, Map<String, String> params) {
		if (StringUtils.isBlank(sessionId) || StringUtils.isBlank(partnerId) || StringUtils.isBlank(serverId) || timestamp == 0) {
			throw new ServiceException(PartnerService.PARAM_ERROR, "参数不正确");
		}

		SidInfoResponse sidInfo = null;

		try {
			sidInfo = UcSdk.instance().sidInfo(sessionId);

			if (sidInfo != null && sidInfo.getState().getCode() == 1) {
				int port = serverService.getServerHttpPort(serverId);
				int closeReg = serverService.getServerCloseRegStatus(serverId);
				String partnerUserId = sidInfo.getData().getAccountId();

				logger.debug("uc xxx partneUserId:" + partnerUserId);
				return GameApiSdk.getInstance().loadUserToken(partnerUserId, partnerId, serverId, port, closeReg, "0", params);
			} else {
				if (sidInfo != null) {
					throw new ServiceException(PartnerService.LOGIN_VALID_FAILD, sidInfo.getState().getMsg());
				} else {
					throw new ServiceException(PartnerService.LOGIN_VALID_FAILD, "登录验证失败");
				}
			}

		} catch (ServiceException se) {
			throw se;
		}catch (Exception e) {
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
		PayCallbackResponse rsp = (PayCallbackResponse) paymentObj;
		if (!UcSdk.instance().checkPayCallbackSign(rsp)) {
			logger.error("签名不正确" + Json.toJson(rsp));
			return false;
		}

		PaymentOrder order = paymentOrderDao.get(rsp.getData().getCallbackInfo());

		if (order == null) {
			logger.error("订单为空：" + Json.toJson(rsp));
			return false;
		}

		if (order.getStatus() != OrderStatus.STATUS_NEW) {
			logger.error("订单已经完成" + Json.toJson(rsp));
			return true;
		}

		BigDecimal finishAmount = new BigDecimal(rsp.getData().getAmount());

		if (!order.getAmount().equals(finishAmount)) {
			logger.error("订单金额不符：" + Json.toJson(rsp));
			return false;
		}

		if (!rsp.getData().getOrderStatus().equals("S")) {
			logger.error("充值失败：" + Json.toJson(rsp));
			this.paymentOrderDao.updateStatus(order.getOrderId(), OrderStatus.STATUS_ERROR, rsp.getData().getOrderId(), finishAmount, rsp.getData().getFailedDesc());
			return false;
		}

		int gold = (int) (order.getAmount().doubleValue() * 10);
		String extInfo = Integer.toString(rsp.getData().getPayWay());
		// 更新订单状态
		if (this.paymentOrderDao.updateStatus(order.getOrderId(), OrderStatus.STATUS_FINISH, rsp.getData().getOrderId(), finishAmount, extInfo)) {
			int port = serverService.getServerHttpPort(order.getServerId());

			// 请求游戏服，发放游戏货币
			if (!GameApiSdk.getInstance().doPayment(order.getPartnerId(), order.getServerId(), port, order.getPartnerUserId(), order.getWaresId(), "", order.getOrderId(),
					finishAmount, gold, "", "")) {
				// 如果失败，要把订单置为未支付
				return this.paymentOrderDao.updateStatus(order.getOrderId(), OrderStatus.STATUS_NOT_PAY, rsp.getData().getOrderId(), finishAmount, extInfo);
			} else {
				logger.info("支付成功：" + Json.toJson(rsp));
				return true;
			}
		}

		logger.error("充值失败：" + Json.toJson(rsp));
		return false;
	}

	@Override
	public String getPayBackUrl() {
		return UcSdk.instance().getPaybackUrl();
	}

}
