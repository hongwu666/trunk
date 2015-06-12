package com.lodogame.ldsg.web.sdk;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.lodogame.ldsg.web.model.huawei.HuaweiPaymentObj;
import com.lodogame.ldsg.web.util.Json;
import com.lodogame.ldsg.web.util.UrlRequestUtils;
import com.lodogame.ldsg.web.util.UrlRequestUtils.Mode;
import com.lodogame.ldsg.web.util.huawei.RSA;

public class HuaweiSdk {

	private static final Logger logger = Logger.getLogger(HuaweiSdk.class);

	private static HuaweiSdk ins;

	private static Properties prop;

	private String partnerId;
	private String publicKey;
	private String checkSessionUrl;

	public static HuaweiSdk instance() {
		synchronized (HuaweiSdk.class) {
			if (ins == null) {
				ins = new HuaweiSdk();
			}
		}

		return ins;
	}

	private HuaweiSdk() {
		loadSdkProperties();
	}

	public void reload() {
		loadSdkProperties();
	}

	private void loadSdkProperties() {
		try {
			prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("sdk.properties"));
			partnerId = prop.getProperty("HuaweiSdk.partnerId");
			publicKey = prop.getProperty("HuaweiSdk.publicKey");
			checkSessionUrl = prop.getProperty("HuaweiSdk.checkSessionUrl");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public boolean verifySession(String session, String uid, long timestamp) throws UnsupportedEncodingException {
		Map<String, String> params = new HashMap<String, String>();
		params.put("nsp_svc", "OpenUP.User.getInfo");
		params.put("nsp_ts", Long.toString(System.currentTimeMillis()));
		params.put("access_token", URLEncoder.encode(session, "utf-8"));
		String jsonStr = UrlRequestUtils.execute(checkSessionUrl, params, Mode.GET);
		Map<String, Object> jsonMap = Json.toObject(jsonStr, Map.class);
		if (jsonMap.containsKey("userID") && jsonMap.get("userID") != null && jsonMap.get("userID").equals(uid)) {
			return true;
		}
		return false;
	}

	public boolean checkPayCallbackSign(HuaweiPaymentObj data) {
		// 获取待签名字符串
		String content = RSA.getSignData(data.getParams());
		// 验签
		return RSA.doCheck(content, (String) data.getParams().get("sign"), publicKey);
	}

	public String getPartnerId() {
		return partnerId;
	}
}
