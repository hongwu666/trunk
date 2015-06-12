package com.lodogame.ldsg.web.service.impl;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.ldsg.web.bo.OrderBO;
import com.lodogame.ldsg.web.constants.OrderStatus;
import com.lodogame.ldsg.web.constants.ServiceReturnCode;
import com.lodogame.ldsg.web.exception.ServiceException;
import com.lodogame.ldsg.web.model.PaymentObj;
import com.lodogame.ldsg.web.model.PaymentOrder;
import com.lodogame.ldsg.web.model.PurchaseInfo;
import com.lodogame.ldsg.web.model.UserToken;
import com.lodogame.ldsg.web.model.oppo.OppoGetUserResult;
import com.lodogame.ldsg.web.model.oppo.OppoPaymentObj;
import com.lodogame.ldsg.web.sdk.GameApiSdk;
import com.lodogame.ldsg.web.sdk.OppoSdk;
import com.lodogame.ldsg.web.service.BasePartnerService;
import com.lodogame.ldsg.web.service.PartnerService;
import com.lodogame.ldsg.web.service.ServerService;
import com.lodogame.ldsg.web.util.Json;
import com.nearme.oauth.log.NearMeException;
import com.nearme.oauth.model.AccessToken;
import com.nearme.oauth.open.AccountAgent;

public class OppoServiceImpl extends BasePartnerService {

	@Autowired
	private ServerService serverService;

	private final static Logger logger = Logger.getLogger(OppoServiceImpl.class);

	@Override
	public UserToken login(String token, String ext, String partnerId, String serverId, long timestamp, String sign, Map<String, String> params) {
		if (StringUtils.isBlank(token) || StringUtils.isBlank(ext) || StringUtils.isBlank(partnerId) || StringUtils.isBlank(serverId) || timestamp == 0) {
			throw new ServiceException(PartnerService.PARAM_ERROR, "参数不正确");
		}

		logger.debug("sessionId[" + token + "] partnerId[" + partnerId + "] serverId[" + serverId + "] ext[" + ext + "]");

		String oauthToken = token.split("&")[0].split("=")[1];
		String oauthTokenSecret = token.split("&")[1].split("=")[1];
		AccessToken tokens = new AccessToken(oauthToken, oauthTokenSecret);

		try {
			String gcUserInfo = AccountAgent.getInstance().getGCUserInfo(tokens);

			logger.debug("userInfo[" + gcUserInfo + "]");

			if (StringUtils.isEmpty(gcUserInfo) == true) {
				throw new ServiceException(PartnerService.LOGIN_VALID_FAILD, "登录验证失败");
			}

			gcUserInfo = gcUserInfo.replace("BriefUser", "briefUser");
			OppoGetUserResult result = Json.toObject(gcUserInfo, OppoGetUserResult.class);

			String partnerUserId = result.getBriefUser().getId();

			int port = serverService.getServerHttpPort(serverId);
			int closeReg = serverService.getServerCloseRegStatus(serverId);
			return GameApiSdk.getInstance().loadUserToken(partnerUserId, partnerId, serverId, port, closeReg, "0", params);

		} catch (ServiceException se) {
			throw se;
		}catch (NearMeException e) {
			throw new ServiceException(PartnerService.LOGIN_VALID_FAILD, "登录验证失败");
		}
	}

	@Override
	public boolean doPayment(PaymentObj paymentObj) {
		if (paymentObj == null) {
			logger.error("paymentObj为空");
			return false;
		}

		OppoPaymentObj oppoPaymentObj = (OppoPaymentObj) paymentObj;
		PaymentOrder paymentOrder = paymentOrderDao.get(oppoPaymentObj.getCpOrderId());
		if (paymentOrder == null) {
			logger.error("订单为空" + Json.toJson(oppoPaymentObj));
			return false;
		}

		if (paymentOrder.getStatus() != OrderStatus.STATUS_NEW) {
			logger.error("订单已经完成" + Json.toJson(oppoPaymentObj));
			throw new ServiceException(ServiceReturnCode.FAILD, "ERROR_REPEAT");
		}

		int price = oppoPaymentObj.getPrice();
		int count = oppoPaymentObj.getCount();

		// 以分为单位
		BigDecimal finishAmount = new BigDecimal(price * count / 100);

		if (paymentOrder.getAmount().compareTo(finishAmount) != 0) {
			logger.error("订单金额不符：" + Json.toJson(oppoPaymentObj));
			logger.error("数据库订单" + Json.toJson(paymentOrder));

			return false;
		}

		int gold = (int) (paymentOrder.getAmount().doubleValue() * 10);
		if (paymentOrderDao.updateStatus(paymentOrder.getOrderId(), OrderStatus.STATUS_FINISH, oppoPaymentObj.getSdkOrderId(), finishAmount, oppoPaymentObj.getExtInfo())) {
			int port = serverService.getServerHttpPort(paymentOrder.getServerId());
			// 请求游戏服，发放游戏货币
			if (!GameApiSdk.getInstance().doPayment(paymentOrder.getPartnerId(), paymentOrder.getServerId(), port, paymentOrder.getPartnerUserId(), paymentOrder.getWaresId(), "",
					paymentOrder.getOrderId(), finishAmount, gold, "", "")) {
				// 如果失败，要把订单置为未支付
				this.paymentOrderDao.updateStatus(paymentOrder.getOrderId(), OrderStatus.STATUS_NOT_PAY, oppoPaymentObj.getSdkOrderId(), finishAmount, oppoPaymentObj.getExtInfo());
				logger.error("发货失败：" + Json.toJson(oppoPaymentObj));
				return false;
			} else {
				logger.info("支付成功：" + Json.toJson(oppoPaymentObj));
				return true;
			}
		}

		return false;
	}

	@Override
	public OrderBO createOrder(PurchaseInfo purchaseInfo) {
		OrderBO info = super.createOrder(purchaseInfo);
		info.setNotifyUrl(OppoSdk.instance().getPayCallback());
		info.setPrice(Integer.toString(new BigDecimal(100).multiply(new BigDecimal(info.getPrice())).intValue()));
		return info;
	}

	@Override
	public String getPayBackUrl() {
		return OppoSdk.instance().getPayCallback();
	}
}
