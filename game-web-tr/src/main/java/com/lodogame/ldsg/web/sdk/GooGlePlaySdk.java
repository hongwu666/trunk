package com.lodogame.ldsg.web.sdk;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.alibaba.fastjson.JSONObject;
import com.lodogame.ldsg.web.model.google.GooglePaymentObj;
import com.lodogame.ldsg.web.util.google.Security;

public class GooGlePlaySdk {
	
	private static final Logger logger = Logger.getLogger(GooGlePlaySdk.class);
	
	private static GooGlePlaySdk ins;
	private static Properties prop;
	private String partnerId;
	private String appKey;

	public static GooGlePlaySdk instance() {
		synchronized (GooGlePlaySdk.class) {
			if (ins == null) {
				ins = new GooGlePlaySdk();
			}
		}
		return ins;
	}

	private GooGlePlaySdk() {
		loadSdkProperties();
	}

	public void reload(){
		loadSdkProperties();
	}
	
	private void loadSdkProperties() {
		try {
			prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("sdk.properties"));
			partnerId = prop.getProperty("GooGlePlaySdk.partnerId");
			appKey = prop.getProperty("GooGlePlaySdk.appKey");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	public JSONObject checkPayCallbackSign(GooglePaymentObj data) {
		try {
			if(Security.verifyPurchase(getAppKey(),data.getJson(),data.getSignature())){
				return JSONObject.parseObject(data.getJson());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	public boolean verifySession(String session){
		if(StringUtils.isNotBlank(session)){
			return true;
		}
		return false;
	}
	
	public String getPartnerId() {
		return partnerId;
	}

	public String getAppKey() {
		return appKey;
	}
	
}
