package com.lodogame.ldsg.web.sdk;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.lodogame.ldsg.web.model.qihoo.PayCallbackObject;
import com.lodogame.ldsg.web.util.EncryptUtil;
import com.lodogame.ldsg.web.util.Json;
import com.lodogame.ldsg.web.util.UrlRequestUtils;
import com.lodogame.ldsg.web.util.UrlRequestUtils.Mode;

public class QihooSdk {
	private static Logger LOGGER = Logger.getLogger(QihooSdk.class);

	private Properties prop;

	private static QihooSdk qihooSdk;

	private final static String PROTOCOL_HEAD = "https://";
	private String host;
	private String appId;
	private String appKey;
	private String appSecret;
	private String payCallback;
	private String partnerId;

	public static QihooSdk instance() {
		synchronized (QihooSdk.class) {
			if (null == qihooSdk) {
				qihooSdk = new QihooSdk();
			}
		}
		return qihooSdk;
	}

	private QihooSdk() {
		loadSdkProperties();
	}

	public void reload() {
		loadSdkProperties();
	}

	public String getPayCallback() {
		return payCallback;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public void setPayCallback(String payCallback) {
		this.payCallback = payCallback;
	}

	private void loadSdkProperties() {
		try {
			prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("sdk.properties"));
			host = prop.getProperty("Qihoo.host");
			appId = prop.getProperty("Qihoo.cpId", "0");
			appKey = prop.getProperty("Qihoo.apiKey");
			appSecret = prop.getProperty("Qihoo.appSecret");
			payCallback = prop.getProperty("Qihoo.payBackUrl");
			partnerId = prop.getProperty("Qihoo.partnerId");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public Map<String, String> accessToken(String code) {
		String url = PROTOCOL_HEAD + host + "/oauth2/access_token";
		Map<String, String> param = new HashMap<String, String>();
		param.put("grant_type", "authorization_code");
		param.put("code", code);
		param.put("client_id", appKey);
		param.put("client_secret", appSecret);
		param.put("redirect_uri", "oob");

		String jsonStr = UrlRequestUtils.executeHttps(url, param, Mode.GET);

		Map<String, String> ret = Json.toObject(jsonStr, Map.class);

		return ret;
	}

	public Map<String, String> refreshToken(String accessToken, String refreshToken) {
		String url = PROTOCOL_HEAD + host + "/oauth2/access_token";
		Map<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("grant_type", "refresh_token");
		paraMap.put("refresh_token", refreshToken);
		paraMap.put("client_id", appKey);
		paraMap.put("client_secret", appSecret);
		paraMap.put("scope", "basic");
		String jsonStr = UrlRequestUtils.executeHttps(url, paraMap, Mode.GET);
		Map<String, String> ret = Json.toObject(jsonStr, Map.class);
		return ret;
	}

	@SuppressWarnings("unchecked")
	public boolean verifyPayment(PayCallbackObject cb) {
		String url = PROTOCOL_HEAD + host + "/pay/verify_mobile_notification.json";
		Map<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("product_id", cb.getProduct_id());
		paraMap.put("amount", cb.getAmount());
		paraMap.put("app_uid", cb.getApp_uid());
		paraMap.put("order_id", cb.getOrder_id());
		paraMap.put("app_order_id", cb.getApp_order_id());
		paraMap.put("sign_type", cb.getSign_type());
		paraMap.put("sign_return", cb.getSign_return());
		paraMap.put("app_key", cb.getApp_key());
		paraMap.put("client_id", appId);
		paraMap.put("client_secret", appSecret);
		String jsonStr = UrlRequestUtils.executeHttps(url, paraMap, Mode.GET);
		Map<String, String> ret = Json.toObject(jsonStr, Map.class);
		if (ret != null && ret.containsKey("ret") && ret.get("ret").equals("verified")) {
			return true;
		}
		return false;
	}

	public Map<String, String> getUserInfo(String accessToken) {
		String url = PROTOCOL_HEAD + host + "/user/me.json";
		Map<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("access_token", accessToken);
		// paraMap.put("sign", makeSign(paraMap));
		String jsonStr = UrlRequestUtils.executeHttps(url, paraMap, Mode.GET);
		Map<String, String> ret = Json.toObject(jsonStr, Map.class);
		return ret;
	}

	private String makeSign(Map<String, String> paraMap) {
		Set<String> keys = paraMap.keySet();
		TreeSet<String> treeSet = new TreeSet<String>();
		treeSet.addAll(keys);
		String signSrcStr = "";
		for (String key : treeSet) {
			String value = paraMap.get(key);
			if (!StringUtils.isBlank(value)) {
				signSrcStr += value + "#";
			}
		}
		signSrcStr += appSecret;

		return EncryptUtil.getMD5(signSrcStr);
	}

	public boolean checkPayCallbackSign(PayCallbackObject cb) {
		Map<String, String> map = Json.toObject(Json.toJson(cb), Map.class);
		map.remove("sign");
		map.remove("sign_return");
		String sign = makeSign(map);

		return sign.equalsIgnoreCase(cb.getSign());
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		QihooSdk qihooSdk = QihooSdk.instance();
		System.out.println(qihooSdk.accessToken("137693119186859575cf4dda7a2961d9e9c47c58eced12306d"));

	}
}
