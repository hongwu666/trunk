package com.lodogame.ldsg.web.sdk;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import com.lodogame.ldsg.web.model.dangle.DanglePaymentObj;
import com.lodogame.ldsg.web.util.EncryptUtil;
import com.lodogame.ldsg.web.util.Json;
import com.lodogame.ldsg.web.util.UrlRequestUtils;

public class DangleSdk {

	private static final Logger logger = Logger.getLogger(DangleSdk.class);

	private static DangleSdk ins;

	private static Properties prop;

	private final static String PROTOCOL_HEAD = "http://";
	private String partnerId;
	private String apiKey;
	private String appId;
	private String host;
	private String paymentKey;

	public static DangleSdk instance() {
		synchronized (DangleSdk.class) {
			if (ins == null) {
				ins = new DangleSdk();
			}
		}

		return ins;
	}

	private DangleSdk() {
		loadSdkProperties();
	}

	public void reload() {
		loadSdkProperties();
	}

	private void loadSdkProperties() {
		try {
			prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("sdk.properties"));
			appId = prop.getProperty("DangleSdk.apiId");
			paymentKey = prop.getProperty("DangleSdk.payment_key");
			apiKey = prop.getProperty("DangleSdk.apiKey");
			partnerId = prop.getProperty("DangleSdk.partnerId");
			host = prop.getProperty("DangleSdk.host");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public String verifySession(String session, String uid) {
		String url = PROTOCOL_HEAD + host;
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", session);
		params.put("mid", uid);
		params.put("app_id", appId);
		String sign = "";
		try {
			sign = loginMakeSign(session, apiKey);
			params.put("sig", sign);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		String jsonStr = UrlRequestUtils.execute(url, params, UrlRequestUtils.Mode.GET);
		Map<String, Object> ret = Json.toObject(jsonStr, Map.class);
		if (ret != null && ret.containsKey("error_code") && ret.get("error_code").equals(0)) {
			return ret.get("memberId").toString();
		}
		return null;
	}

	public boolean checkPayCallbackSign(DanglePaymentObj cb) {

		try {
			String signString = "order=" + cb.getOrder() + "&money=" + cb.getMoney() + "&mid=" + cb.getMid() + "&time=" + cb.getTime() + "&result=" + cb.getResult() + "&ext="
					+ cb.getExt() + "&key=" + DangleSdk.instance().getPaymentKey();
			logger.info("当乐签名串：" + signString);
			if (StringUtils.isBlank(cb.getSignature())) {
				return false;
			}
			if (cb.getSignature().equals(EncryptUtil.getMD5(signString).toLowerCase())) {
				return true;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return false;
	}

	private String loginMakeSign(String token, String appKey) throws Exception {
		return EncryptUtil.getMD5(token + "|" + appKey);
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPaymentKey() {
		return paymentKey;
	}

	public void setPaymentKey(String paymentKey) {
		this.paymentKey = paymentKey;
	}

}
