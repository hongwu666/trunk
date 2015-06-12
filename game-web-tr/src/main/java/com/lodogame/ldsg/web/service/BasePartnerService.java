package com.lodogame.ldsg.web.service;

import java.io.IOException;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.lodogame.ldsg.web.Config;
import com.lodogame.ldsg.web.bo.OrderBO;
import com.lodogame.ldsg.web.bo.PackageInfoBO;
import com.lodogame.ldsg.web.constants.OrderStatus;
import com.lodogame.ldsg.web.constants.ServiceReturnCode;
import com.lodogame.ldsg.web.dao.ActiveCodeDao;
import com.lodogame.ldsg.web.dao.PackageExtinfoDao;
import com.lodogame.ldsg.web.dao.PackageInfoDao;
import com.lodogame.ldsg.web.dao.PackageSettingsDao;
import com.lodogame.ldsg.web.dao.PaymentOrderDao;
import com.lodogame.ldsg.web.dao.ServerStatusDao;
import com.lodogame.ldsg.web.exception.ServiceException;
import com.lodogame.ldsg.web.model.PackageExtinfo;
import com.lodogame.ldsg.web.model.PackageInfo;
import com.lodogame.ldsg.web.model.PackageSettings;
import com.lodogame.ldsg.web.model.Payment;
import com.lodogame.ldsg.web.model.PaymentOrder;
import com.lodogame.ldsg.web.model.PurchaseInfo;
import com.lodogame.ldsg.web.model.ServerStatus;
import com.lodogame.ldsg.web.model.UserToken;
import com.lodogame.ldsg.web.sdk.GameApiSdk;
import com.lodogame.ldsg.web.util.EncryptUtil;
import com.lodogame.ldsg.web.util.IDGenerator;
import com.lodogame.ldsg.web.util.Json;

public class BasePartnerService implements PartnerService {

	private static final Logger LOGGER = Logger
			.getLogger(BasePartnerService.class);

	@Autowired
	protected PackageSettingsDao packageSettingsDao;

	@Autowired
	protected PaymentOrderDao paymentOrderDao;

	@Autowired
	private ActiveCodeDao activeCodeDao;

	@Autowired
	private PackageInfoDao packageInfoDao;

	@Autowired
	private ServerStatusDao serverStatusDao;

	@Autowired
	private PackageExtinfoDao packageExtinfoDao;

	@Autowired
	protected ServerService serverService;

	@Override
	public boolean isActive(String uuid, String partnerId) {

		if (StringUtils.isBlank(uuid) || StringUtils.isBlank(partnerId)) {
			throw new ServiceException(WebApiService.PARAM_ERROR, "参数不正确");
		}

		return this.activeCodeDao.isActive(uuid, partnerId);
	}

	@Override
	public boolean active(String uuid, String code, String partnerId) {

		if (StringUtils.isBlank(uuid)) {
			throw new ServiceException(WebApiService.PARAM_ERROR, "参数不正确");
		}

		return this.activeCodeDao.active(uuid, code, partnerId);
	}

	protected void checkSign(String token, String partnerId, String serverId,
			long timestamp, String sign) {
		String serverSign = EncryptUtil.getMD5((token + partnerId + serverId
				+ timestamp + Config.ins().getSignKey()));
		System.out.println(serverSign.toLowerCase());
		System.out.println(sign.toLowerCase());
		if (!serverSign.toLowerCase().equals(sign.toLowerCase())) {
			throw new ServiceException(WebApiService.SIGN_CHECK_ERROR, "签名校验失败");
		}
	}

	/**
	 * 判断能否进游戏
	 * 
	 * @param imei
	 */
	protected void checkServerOpen(String imei, String mac, String partnerId,
			String ip) {

		ServerStatus serverStatus = this.serverStatusDao
				.getServerStatus(partnerId);

		if (serverStatus != null && serverStatus.getStatus() == 1) {

			LOGGER.info("imei[" + imei + "], " + "mac[" + mac + "], partnerId["
					+ partnerId + "], ip[" + ip + "]");

			if (this.activeCodeDao.isBlackImei(imei, partnerId)) {
				return;
			}

			if (this.activeCodeDao.isBlackImei(mac, partnerId)) {
				return;
			}

			if (this.serverStatusDao.isWhiteIp(partnerId, ip)) {
				return;
			}

			throw new ServiceException(WebApiService.SERVER_CLOSE, Config.ins()
					.getServerCloseNotice());
		}
	}

	protected void checkVersionExpire(String version, String partnerId) {

		PackageExtinfo info = packageExtinfoDao
				.getByVersion(version, partnerId);
		if (info != null && info.getIsExpire() == 1) {
			PackageSettings settings = packageSettingsDao.get();
			throw new ServiceException(WebApiService.VERSION_EXPIRE,
					settings.getExpirePackageMessage());
		}
	}

	@Override
	public PackageInfoBO checkVersion(String version, String fr, String mac,
			String partnerId, String ip) {

		checkServerOpen(fr, mac, partnerId, ip);
		checkVersionExpire(version, partnerId);

		int apkBigVer = 0;
		int apkSmallVer = 0;
		int resBigVer = 0;
		int resSmallVer = 0;
		// 版本参数是否合法
		boolean versionInvalid = false;
		if (version != null && !StringUtils.isBlank(version)) {
			String[] verArr = version.split("\\.");
			if (verArr.length == 4) {
				apkBigVer = Integer.parseInt(verArr[0]);
				apkSmallVer = Integer.parseInt(verArr[1]);
				resBigVer = Integer.parseInt(verArr[2]);
				resSmallVer = Integer.parseInt(verArr[3]);
			} else {
				versionInvalid = true;
			}
		} else {
			versionInvalid = true;
		}

		if (versionInvalid) {
			throw new ServiceException(PARAM_ERROR, "版本参数错误");
		}

		PackageInfo apkPackage = packageInfoDao.getLast(0, partnerId);
		PackageInfo resPackage = packageInfoDao.getLast(1, partnerId);
		PackageInfoBO bo = new PackageInfoBO();
		bo.setVersion("");
		bo.setApkUrl("");
		bo.setResUrl("");
		bo.setDescribe("");

		if (apkPackage == null && resPackage == null) {
			return bo;
		}

		// 检测APK包升级
		if (apkPackage != null) {
			if (apkPackage.getIsTest() == 1) {
				// 用户不在灰度测试范围内，则获取普通的最后一个包
				if (!isWhite(partnerId, ip)) {
					apkPackage = packageInfoDao.getLastByTest(0, 0, partnerId);
				}
			}

			// 版本小于当前版本，表示APK包为旧的，则直接返回APK包内容
			if (apkPackage != null
					&& needUpgradeApk(apkBigVer, apkSmallVer,
							apkPackage.getVersion())) {
				bo.setVersion(apkPackage.getVersion());
				bo.setApkUrl(apkPackage.getFullUrl());
				bo.setDescribe(apkPackage.getDescription());
				return bo;
			}
		}

		// 检测资源包升级
		if (resPackage != null && resPackage.getIsTest() == 1) {
			// 用户不在灰度测试范围内，则获取普通的最后一个包
			if (!isWhite(partnerId, ip)) {
				resPackage = packageInfoDao.getLastByTest(1, 0, partnerId);
			}
		}

		if (resPackage == null) {
			return bo;
		}

		// 资源版本大于当前版本，无需升级，则直接返回
		if (!needUpgradeRes(resBigVer, resSmallVer, resPackage.getVersion())) {
			bo.setVersion(resPackage.getVersion());
			return bo;
		}

		String versions = resPackage.getVersions();

		// 如果兼容版本为空，则直接返回完整包URL
		if (StringUtils.isBlank(versions)) {
			bo.setVersion(resPackage.getVersion());
			bo.setResUrl(resPackage.getFullUrl());
			return bo;
		}

		// 判断是否为兼容包
		String[] versionArr = versions.split(",");
		boolean isSupportVersion = false;
		for (int i = 0; i < versionArr.length; i++) {
			if (versionArr[i].equals(version)) {
				isSupportVersion = true;
				break;
			}
		}

		if (isSupportVersion) {
			bo.setVersion(resPackage.getVersion());
			bo.setResUrl(resPackage.getUpgradeUrl());
		} else {
			bo.setVersion(resPackage.getVersion());
			bo.setResUrl(resPackage.getFullUrl());
		}

		return bo;
	}

	/**
	 * 比较APK版本号，是否需要升级APK
	 * 
	 * @param apkBigVer
	 * @param apkSmallVer
	 * @param fullVer
	 * @return
	 */
	protected boolean needUpgradeApk(int apkBigVer, int apkSmallVer,
			String fullVer) {
		String[] verArr = fullVer.split("\\.");
		int apkBigVerTmp = Integer.parseInt(verArr[0]);
		int apkSmallVerTmp = Integer.parseInt(verArr[1]);
		return apkBigVer < apkBigVerTmp
				|| (apkBigVer == apkBigVerTmp && apkSmallVer < apkSmallVerTmp);
	}

	/**
	 * 比较资源包版本号
	 * 
	 * @param resBigVer
	 * @param resSmallVer
	 * @param fullVer
	 * @return
	 */
	protected boolean needUpgradeRes(int resBigVer, int resSmallVer,
			String fullVer) {
		String[] verArr = fullVer.split("\\.");
		int resBigVerTmp = Integer.parseInt(verArr[2]);
		int resSmallVerTmp = Integer.parseInt(verArr[3]);
		return resBigVer < resBigVerTmp
				|| (resBigVer == resBigVerTmp && resSmallVer < resSmallVerTmp);
	}

	public OrderBO createOrder(PurchaseInfo order) {
		LOGGER.info("create order params:" + Json.toJson(order));
		if (StringUtils.isEmpty(order.getQn())) {
			order.setQn(this.getQN());
		}

		String orderId = IDGenerator.getID();
		PaymentOrder paymentOrder = new PaymentOrder(orderId, order,
				OrderStatus.STATUS_NEW);

		boolean success = this.paymentOrderDao.add(paymentOrder);
		if (!success) {
			throw new ServiceException(ServiceReturnCode.FAILD, "订单创建失败");
		}

		OrderBO orderBO = new OrderBO(paymentOrder, order);

		orderBO.setNotifyUrl(getPayBackUrl());
		return orderBO;

	}

	public String createOrderStr(PurchaseInfo purchaseInfo) {
		return null;
	}

	private boolean isWhite(String partnerId, String ip) {
		return this.serverStatusDao.isWhiteIp(partnerId, ip);
	}

	public String getQN() {
		String qn;
		qn = "9000001";
		try {
			Properties prop = PropertiesLoaderUtils
					.loadProperties(new ClassPathResource("client.properties"));
			qn = prop.getProperty("qn");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return qn;
	}

	@Override
	public boolean doPayment(Payment payment) {
		if (payment == null) {
			LOGGER.error("payment 为空");
			return false;
		}

		if (payment.isPaymentDataVerified() == false) {
			this.paymentOrderDao.updateStatus(payment.getGameOrderId(),
					OrderStatus.STATUS_ERROR, payment.getPartnerOrderId(),
					new BigDecimal(0), "payment data check faild.");
			LOGGER.error("支付数据验证失败");
			return false;
		}

		PaymentOrder order = paymentOrderDao.get(payment.getGameOrderId());

		LOGGER.info("游戏中订单信息 " + order.printPaymentOrderInfo());

		if (order.isOrderFinished() == true) {
			LOGGER.error("订单已经完成, 支付渠道订单信息 " + payment.printPaymentInfo());
			return false;
		}

		String gameOrderId = order.getOrderId();
		String partnerOrderId = payment.getPartnerOrderId();
		BigDecimal finishAmount;

		try {
			finishAmount = payment.getFinishAmount(order.getAmount());
		} catch (Exception e) {
			LOGGER.error("游戏中订单和支付渠道的订单金额不符, 支付渠道订单信息"
					+ payment.printPaymentInfo());
			return false;
		}

		int gold = payment.getGoldNum(order);
		String partnerUserId = order.getPartnerUserId();
		int waresId = payment.getGameWaresId(order);
		String channel = payment.getChannel();
		String extInfo = payment.getExtInfo();

		// 更新订单状态
		if (this.paymentOrderDao.updateStatus(gameOrderId,
				PaymentOrder.STATUS_FINISH, partnerOrderId, finishAmount,
				extInfo)) {
			int port = serverService.getServerHttpPort(order.getServerId());

			// 请求游戏服，发放游戏货币
			if (!GameApiSdk.getInstance().doPayment(order.getPartnerId(),
					order.getServerId(), port, partnerUserId, waresId, channel,
					gameOrderId, finishAmount, gold, "", "")) {

				// 如果失败，要把订单置为未支付
				this.paymentOrderDao.updateStatus(gameOrderId,
						PaymentOrder.STATUS_NOT_PAY, partnerOrderId,
						finishAmount, extInfo);
				LOGGER.error("向游戏服中发放游戏货币失败, 支付渠道订单信息 "
						+ payment.printPaymentInfo());
				return false;
			} else {
				LOGGER.info("支付成功, 支付渠道订单信息" + payment.printPaymentInfo());
				return true;
			}
		}

		this.paymentOrderDao.updateStatus(gameOrderId,
				PaymentOrder.STATUS_ERROR, partnerOrderId, finishAmount,
				extInfo);
		LOGGER.error("充值失败, 支付渠道订单信息 " + payment.printPaymentInfo());

		return false;
	}

	@Override
	public UserToken login(String sessionId, String partnerId, String serverId,
			long timestamp, String sign, Map<String, String> params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean doPayment(String orderId, String partnerUserId,
			boolean success, String partnerOrderId, BigDecimal finishAmount,
			String reqInfo) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getPayBackUrl() {
		// TODO Auto-generated method stub
		return null;
	}

}
