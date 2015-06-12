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

public class GooGlePlayNewSdk {
	
	private static final Logger logger = Logger.getLogger(GooGlePlayNewSdk.class);
	
	private static GooGlePlayNewSdk ins;
	private static Properties prop;
	private String partnerId;
	private String appKey;

	public static GooGlePlayNewSdk instance() {
		synchronized (GooGlePlayNewSdk.class) {
			if (ins == null) {
				ins = new GooGlePlayNewSdk();
			}
		}
		return ins;
	}

	private GooGlePlayNewSdk() {
		loadSdkProperties();
	}

	public void reload(){
		loadSdkProperties();
	}
	
	private void loadSdkProperties() {
		try {
			prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("sdk.properties"));
			partnerId = prop.getProperty("GooGlePlayNewSdk.partnerId");
			appKey = prop.getProperty("GooGlePlayNewSdk.appKey");
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
