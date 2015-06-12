package com.lodogame.ldsg.web.service;

import java.io.IOException;
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
import com.lodogame.ldsg.web.constants.WebApiStatus;
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
import com.lodogame.ldsg.web.model.PaymentOrder;
import com.lodogame.ldsg.web.model.PurchaseInfo;
import com.lodogame.ldsg.web.model.ServerStatus;
import com.lodogame.ldsg.web.util.EncryptUtil;
import com.lodogame.ldsg.web.util.IDGenerator;

public abstract class BasePartnerService implements PartnerService {

	private static final Logger LOG = Logger.getLogger(BasePartnerService.class);

	@Autowired
	protected ServerStatusDao serverStatusDao;

	@Autowired
	protected PackageInfoDao packageInfoDao;

	@Autowired
	protected PackageExtinfoDao packageExtinfoDao;

	@Autowired
	protected PackageSettingsDao packageSettingsDao;

	@Autowired
	protected ActiveCodeDao activeCodeDao;

	@Autowired
	protected PaymentOrderDao paymentOrderDao;

	@Autowired
	protected ServerService serverService;

	@Override
	public PackageInfoBO checkVersion(String version, String fr, String mac, String partnerId, String ip) {

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
			throw new ServiceException(WebApiStatus.PARAM_ERROR, "版本参数错误");
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
			if (apkPackage != null && needUpgradeApk(apkBigVer, apkSmallVer, apkPackage.getVersion())) {
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

		bo.setVersion(resPackage.getVersion());
		if (isSupportVersion) {
			bo.setVersion(resPackage.getVersion());
			bo.setResUrl(resPackage.getUpgradeUrl());
		} else {
			bo.setVersion(resPackage.getVersion());
			bo.setResUrl(resPackage.getFullUrl());
		}

		return bo;
	}

	private boolean isWhite(String partnerId, String ip) {
		return this.serverStatusDao.isWhiteIp(partnerId, ip);
	}

	/**
	 * 比较APK版本号，是否需要升级APK
	 * 
	 * @param apkBigVer
	 * @param apkSmallVer
	 * @param fullVer
	 * @return
	 */
	protected boolean needUpgradeApk(int apkBigVer, int apkSmallVer, String fullVer) {
		String[] verArr = fullVer.split("\\.");
		int apkBigVerTmp = Integer.parseInt(verArr[0]);
		int apkSmallVerTmp = Integer.parseInt(verArr[1]);
		return apkBigVer < apkBigVerTmp || (apkBigVer == apkBigVerTmp && apkSmallVer < apkSmallVerTmp);
	}

	/**
	 * 比较资源包版本号
	 */
	protected boolean needUpgradeRes(int resBigVer, int resSmallVer, String fullVer) {
		String[] verArr = fullVer.split("\\.");
		int resBigVerTmp = Integer.parseInt(verArr[2]);
		int resSmallVerTmp = Integer.parseInt(verArr[3]);
		return resBigVer < resBigVerTmp || (resBigVer == resBigVerTmp && resSmallVer < resSmallVerTmp);
	}

	protected void checkVersionExpire(String version, String partnerId) {

		PackageExtinfo info = packageExtinfoDao.getByVersion(version, partnerId);
		if (info != null && info.getIsExpire() == 1) {
			PackageSettings settings = packageSettingsDao.get();
			throw new ServiceException(WebApiService.VERSION_EXPIRE, settings.getExpirePackageMessage());
		}
	}

	/**
	 * 判断能否进游戏
	 */
	protected void checkServerOpen(String imei, String mac, String partnerId, String ip) {

		ServerStatus serverStatus = this.serverStatusDao.getServerStatus(partnerId);

		if (serverStatus != null && serverStatus.getStatus() == 1) {

			LOG.info("imei[" + imei + "], " + "mac[" + mac + "], partnerId[" + partnerId + "], ip[" + ip + "]");

			if (this.activeCodeDao.isBlackImei(imei, partnerId)) {
				return;
			}

			if (this.activeCodeDao.isBlackImei(mac, partnerId)) {
				return;
			}

			if (this.serverStatusDao.isWhiteIp(partnerId, ip)) {
				return;
			}

			throw new ServiceException(WebApiService.SERVER_CLOSE, Config.ins().getServerCloseNotice());
		}
	}

	protected void checkSign(String token, String partnerId, String serverId, long timestamp, String sign) {
		String serverSign = EncryptUtil.getMD5((token + partnerId + serverId + timestamp + Config.ins().getSignKey()));
		if (!serverSign.toLowerCase().equals(sign.toLowerCase())) {
			throw new ServiceException(WebApiService.SIGN_CHECK_ERROR, "签名校验失败");
		}
	}

	public OrderBO createOrder(PurchaseInfo order) {

		if (StringUtils.isEmpty(order.getQn())) {
			order.setQn(this.getQN());
		}

		String orderId = IDGenerator.getID();
		PaymentOrder paymentOrder = new PaymentOrder(orderId, order, OrderStatus.STATUS_NEW);

		boolean success = this.paymentOrderDao.add(paymentOrder);
		if (!success) {
			throw new ServiceException(ServiceReturnCode.FAILD, "订单创建失败");
		}

		return new OrderBO(paymentOrder, order);

	}

	private String getQN() {
		String qn;
		qn = "9000001";
		try {
			Properties prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("client.properties"));
			qn = prop.getProperty("qn");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return qn;
	}

}
