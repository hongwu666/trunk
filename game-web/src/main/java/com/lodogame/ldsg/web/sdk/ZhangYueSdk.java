package com.lodogame.ldsg.web.sdk;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.lodogame.ldsg.web.model.zhangyue.LoginResult;
import com.lodogame.ldsg.web.util.Json;
import com.lodogame.ldsg.web.util.UrlRequestUtils;

public class ZhangYueSdk {

	public static final String SIGN_ALGORITHMS = "SHA1WithRSA";
	private static final String ALGORITHM = "RSA";
	private static final String CHARSET = "UTF-8";

	private static final Logger logger = Logger.getLogger(ZhangYueSdk.class);

	private static ZhangYueSdk ins;

	private String payBackUrl = null;
	private String partnerId = null;
	private String appId = null;
	private String privateKey = null;
	private String checkSessionUr = null;
	private String md5Key = null;

	public static ZhangYueSdk instance() {
		synchronized (ZhangYueSdk.class) {
			if (ins == null) {
				ins = new ZhangYueSdk();
			}
		}
		return ins;
	}

	private ZhangYueSdk() {
		loadSdkProperties();
	}

	public void reload() {
		loadSdkProperties();
	}

	private void loadSdkProperties() {
		try {
			Properties prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("sdk.properties"));
			checkSessionUr = prop.getProperty("ZhangYueSdk.checkSessionUr");
			partnerId = prop.getProperty("ZhangYueSdk.partnerId");
			appId = prop.getProperty("ZhangYueSdk.appId");
			privateKey = prop.getProperty("ZhangYueSdk.privateKey");
			checkSessionUr = prop.getProperty("ZhangYueSdk.checkSessionUr");
			md5Key = prop.getProperty("ZhangYueSdk.md5Key");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean verify(String token, String partnerUserid) {

		Map<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("app_id", appId);
		paraMap.put("open_uid", partnerUserid);
		paraMap.put("access_token", token);
		paraMap.put("timestamp", String.valueOf(System.currentTimeMillis()));
		paraMap.put("sign_type", "RSA");
		paraMap.put("version", "1.0");

		String sign = Sign(paraMap);
		paraMap.put("sign", sign);

		for (String key : paraMap.keySet()) {
			try {
				paraMap.put(key, URLEncoder.encode(paraMap.get(key), CHARSET));
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}

		try {
			logger.info(Json.toJson(paraMap));
			String json = UrlRequestUtils.executeHttps(this.checkSessionUr, paraMap, "GET");
			LoginResult result = Json.toObject(json, LoginResult.class);

			if (result.getCode() != 0) {
				logger.info("掌阅登陆返回.code[" + result.getCode() + "],msg[" + result.getMsg() + "]");
			}
			return result.getCode() == 0;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}

	}

	/**
	 * 除去数组中的空值和签名参数
	 * 
	 * @param sArray
	 *            签名参数组
	 * 
	 */
	public static Map<String, String> paramFilter(Map<String, String> sArray) {

		Map<String, String> result = new HashMap<String, String>();

		if (sArray == null || sArray.size() <= 0) {
			return result;
		}

		for (String key : sArray.keySet()) {
			String value = sArray.get(key);
			if (value == null || value.equals("")) {
				continue;
			}
			result.put(key, value);
		}
		return result;
	}

	/**
	 * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
	 * 
	 * @param params
	 *            需要排序并参与字符拼接的参数组
	 * @return 拼接后字符串
	 */
	public static String createLinkString(Map<String, String> params) {
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		String prestr = "";
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);
			if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
				prestr = prestr + key + "=" + value;
			} else {
				prestr = prestr + key + "=" + value + "&";
			}
		}

		return prestr;
	}

	public String Sign(Map<String, String> data) {

		Map<String, String> map = paramFilter(data);
		String content = createLinkString(map);
		String sign = createSign(content, privateKey);// sign为生成签名
		return sign;
	}

	public static String createSign(String content, String privateKey) {
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
			KeyFactory keyf = KeyFactory.getInstance(ALGORITHM);
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);
			java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
			signature.initSign(priKey);
			signature.update(content.getBytes(CHARSET));
			byte[] signed = signature.sign();
			return Base64.encodeBase64String(signed);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getPayBackUrl() {
		return payBackUrl;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public String getCheckSessionUr() {
		return checkSessionUr;
	}

	public String getMd5Key() {
		return md5Key;
	}

}
