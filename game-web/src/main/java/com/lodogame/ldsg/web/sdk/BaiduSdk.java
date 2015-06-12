package com.lodogame.ldsg.web.sdk;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.lodogame.ldsg.web.model.baidu.BaiduPaymentObj;
import com.lodogame.ldsg.web.util.EncryptUtil;
import com.lodogame.ldsg.web.util.Json;
import com.lodogame.ldsg.web.util.UrlRequestUtils;

public class BaiduSdk {
	private static final Logger logger = Logger.getLogger(BaiduSdk.class);

	private static BaiduSdk ins;

	private static Properties prop;
	private String partnerId;
	private String appId;
	private String appKey;
	private String appSecret;
	private String checkSessionUrl;
	private String paybackUrl;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public void setPaybackUrl(String paybackUrl) {
		this.paybackUrl = paybackUrl;
	}

	public static BaiduSdk instance() {
		synchronized (BaiduSdk.class) {
			if (ins == null) {
				ins = new BaiduSdk();
			}
		}

		return ins;
	}

	private BaiduSdk() {
		loadSdkProperties();
	}

	public void reload() {
		loadSdkProperties();
	}

	private void loadSdkProperties() {
		try {
			prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("sdk.properties"));
			appId = prop.getProperty("BaiduSdk.appId");
			appKey = prop.getProperty("BaiduSdk.appKey");
			appSecret = prop.getProperty("BaiduSdk.appSecret");
			partnerId = prop.getProperty("BaiduSdk.partnerId");
			checkSessionUrl = prop.getProperty("BaiduSdk.checkSession");
			paybackUrl = prop.getProperty("BaiduSdk.payBackUrl");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 验证SID信息
	 * 
	 * @param sid
	 * @param serverId
	 * @param channelId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Boolean checkSession(String sid) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("AppID", appId);// 当前系统时间
		params.put("appkey", appKey);
		params.put("AccessToken", sid);
		// params.put("uid", uid);

		String sign = EncryptUtil.getMD5(appId + sid + appSecret);

		params.put("Sign", sign.toLowerCase());

		String body = Json.toJson(params);

		logger.info("BaiduDuokuSdk.checkSession body:" + body);

		String jsonStr = UrlRequestUtils.execute(checkSessionUrl, params, UrlRequestUtils.Mode.GET);

		logger.info("BaiduDuokuSdk.checkSession result:" + jsonStr);

		Map<String, Object> result = Json.toObject(jsonStr, HashMap.class);

		if (result == null || result.isEmpty() || !result.containsKey("ResultCode")) {
			return false;
		}

		return (Integer) result.get("ResultCode") == 1;

	}

	public boolean checkPayCallbackSign(BaiduPaymentObj paymentObj) {
		String signStr = paymentObj.getAppId() + paymentObj.getOrderSerial() + paymentObj.getCooperatorOrderSerial() + paymentObj.getContent() + appSecret;
		String sign = EncryptUtil.getMD5(signStr);
		if (sign.equalsIgnoreCase(paymentObj.getSign())) {
			return true;
		}
		return false;
	}

	public static void main(String[] args) throws Exception {
		String json = "{\"id\":1376815057712,\"state\":{\"code\":10,\"msg\":\"无效的请求数据,校验签名失败\"},\"data\":\"\"}";
		// SidInfoResponse s = Json.toObject(json, SidInfoResponse.class);
		// System.out.println(s);
	}

	public String getPartnerId() {
		return partnerId;
	}

	public String getPaybackUrl() {
		return paybackUrl;
	}
}
