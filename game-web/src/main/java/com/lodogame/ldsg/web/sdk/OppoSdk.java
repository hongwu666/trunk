package com.lodogame.ldsg.web.sdk;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class OppoSdk {
	private static final Logger logger = Logger.getLogger(OppoSdk.class);
	
	private static OppoSdk ins;

	private static Properties prop;

	private String partnerId;
	private String apiKey;
	private String appId;
	private String host;
	private String paymentKey;
	private String payCallback;
	
	public static OppoSdk instance() {
		synchronized (OppoSdk.class) {
			if (ins == null) {
				ins = new OppoSdk();
			}
		}

		return ins;
	}
	
	private OppoSdk() {
		loadSdkProperties();
	}

	public void reload(){
		loadSdkProperties();
	}
	
	private void loadSdkProperties() {
		try {
			prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("sdk.properties"));
			partnerId = prop.getProperty("OppoSdk.partnerId");
			appId = prop.getProperty("OppoSdk.appId");
			payCallback = prop.getProperty("OppoSdk.payCallback");
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Properties getProp() {
		return prop;
	}
	public String getPartnerId() {
		return partnerId;
	}
	public String getApiKey() {
		return apiKey;
	}
	public String getAppId() {
		return appId;
	}
	public String getHost() {
		return host;
	}
	public String getPaymentKey() {
		return paymentKey;
	}

	public String getPayCallback() {
		return payCallback;
	}

	public void setPayCallback(String payCallback) {
		this.payCallback = payCallback;
	}
}
