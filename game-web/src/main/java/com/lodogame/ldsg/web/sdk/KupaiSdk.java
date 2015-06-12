package com.lodogame.ldsg.web.sdk;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.lodogame.ldsg.web.model.anzhi.AnZhiPaymentObj;
import com.lodogame.ldsg.web.util.Json;
import com.lodogame.ldsg.web.util.UrlRequestUtils;
import com.lodogame.ldsg.web.util.UrlRequestUtils.Mode;

public class KupaiSdk {

	private static final Logger logger = Logger.getLogger(KupaiSdk.class);

	private static KupaiSdk ins;

	private String appId;
	private String partnerId;
	private String appKey;
	private String checkSessionUrl;
	private String payCallback;

	public static KupaiSdk instance() {

		synchronized (KupaiSdk.class) {
			if (ins == null) {
				ins = new KupaiSdk();
			}
		}

		return ins;
	}

	private KupaiSdk() {
		loadSdkProperties();
	}

	public void reload() {
		loadSdkProperties();
	}

	private void loadSdkProperties() {
		try {
			Properties prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("sdk.properties"));
			appId = prop.getProperty("KupaiSdk.appId");
			appKey = prop.getProperty("KupaiSdk.appKey");
			partnerId = prop.getProperty("KupaiSdk.partnerId");
			checkSessionUrl = prop.getProperty("KupaiSdk.checkSessionUrl");
			payCallback = prop.getProperty("KupaiSdk.payCallback");

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> verifySession(String session) {

		Map<String, String> params = new HashMap<String, String>();
		params.put("grant_type", "authorization_code");
		params.put("client_id", appId);
		params.put("client_secret", appKey);
		params.put("code", session);
		params.put("redirect_uri", appKey);

		logger.debug("grant_type:" + "authorization_code" + ",client_id:" + appId + ",client_secret:" + appKey + ",code:" + session + ",redirect_uri:" + appKey);

		String jsonStr = UrlRequestUtils.execute(checkSessionUrl, params, Mode.POST);
		logger.debug(jsonStr);

		Map<String, Object> jsonMap = Json.toObject(jsonStr, Map.class);
		if (jsonMap.containsKey("openid") && jsonMap.get("openid") != null) {
			return jsonMap;
		}
		return null;
	}

	public boolean checkPayCallbackSign(AnZhiPaymentObj data) {
		try {

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return false;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public String getPayCallback() {
		return payCallback;
	}

}
