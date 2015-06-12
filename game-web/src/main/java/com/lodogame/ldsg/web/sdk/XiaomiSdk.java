package com.lodogame.ldsg.web.sdk;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import com.lodogame.ldsg.web.model.xiaomi.XiaomiPaymentObj;
import com.lodogame.ldsg.web.util.HmacSHA1Encryption;
import com.lodogame.ldsg.web.util.Json;
import com.lodogame.ldsg.web.util.UrlRequestUtils;

public class XiaomiSdk {
	
	private static final Logger logger = Logger.getLogger(BaiduSdk.class);
	
	private static XiaomiSdk ins;

	private static Properties prop;

	private final static String PROTOCOL_HEAD = "http://";
	private String partnerId;
	private String appSecret;
	private String appId;
	private String host;

	public static XiaomiSdk instance() {
		synchronized (XiaomiSdk.class) {
			if (ins == null) {
				ins = new XiaomiSdk();
			}
		}

		return ins;
	}

	private XiaomiSdk() {
		loadSdkProperties();
	}

	public void reload(){
		loadSdkProperties();
	}
	
	private void loadSdkProperties() {
		try {
			prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("sdk.properties"));
			appId = prop.getProperty("XiaomiSdk.appId");
			appSecret = prop.getProperty("XiaomiSdk.appSecret");
			partnerId = prop.getProperty("XiaomiSdk.partnerId");
			host = prop.getProperty("XiaomiSdk.host");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public boolean verifySession(String session, String uid){
		String url = PROTOCOL_HEAD + host + "/api/biz/service/verifySession.do";
		Map<String, String> params = new HashMap<String, String>();
		params.put("session", session);
		params.put("uid", uid);
		params.put("appId", appId);
		String sign = "";
		try {
			sign = makeSign(params);
			params.put("signature", sign);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		String jsonStr = UrlRequestUtils.execute(url, params, UrlRequestUtils.Mode.GET);
		Map<String, Object> ret = Json.toObject(jsonStr, Map.class);
		if(ret != null && ret.containsKey("errcode") && ret.get("errcode").equals(200)){
			return true;
		}
		return false;
	}
	

	@SuppressWarnings("unchecked")
	public boolean checkPayCallbackSign(XiaomiPaymentObj cb) {
		String json = Json.toJson(cb);
		Map<String, Object> map = Json.toObject(json, Map.class);
		try {
			String sign = makeSign(map);
			return sign.equalsIgnoreCase(cb.getSignature());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return false;
	}

	private String makeSign(Map<String, ?> paraMap) throws Exception{
		Set<String> keys = paraMap.keySet();
		TreeSet<String> treeSet = new TreeSet<String>();
		treeSet.addAll(keys);
		String signSrcStr = "";
		for(String key : treeSet){
			if(key.equals("signature")){
				continue;
			}
			Object value = paraMap.get(key);
			if(!StringUtils.isBlank(signSrcStr)){
				signSrcStr += "&";
			}
			signSrcStr += key + "=" + (value == null ? "" : value);
		}
		
		
		return HmacSHA1Encryption.HmacSHA1Encrypt(signSrcStr, appSecret);
	}
	
	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}
}
