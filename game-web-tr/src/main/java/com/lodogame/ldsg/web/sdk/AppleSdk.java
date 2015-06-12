package com.lodogame.ldsg.web.sdk;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.lodogame.ldsg.web.model.apple.ApplePaymentObj;
import com.lodogame.ldsg.web.util.UrlRequestUtils;

public class AppleSdk {

	private static final Logger logger = Logger.getLogger(AppleSdk.class);

	private static AppleSdk ins;

	private static Properties prop;

	private String partnerId;

	private String host;

	private String testHost;

	private String testVersion;

	private String payBackUrl;

	private ExecutorService executorService;

	private final static String PROTOCOL_HEAD = "https://";

	public static AppleSdk instance() {
		synchronized (AppleSdk.class) {
			if (ins == null) {
				ins = new AppleSdk();
			}
		}

		return ins;
	}

	private AppleSdk() {
		loadSdkProperties();
	}

	public void reload() {
		loadSdkProperties();
	}

	private void loadSdkProperties() {
		try {
			prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("sdk.properties"));
			partnerId = prop.getProperty("AppleSdk.partnerId");
			host = prop.getProperty("AppleSdk.host");
			payBackUrl = prop.getProperty("AppleSdk.payBackUrl");

			testVersion = prop.getProperty("AppleSdk.testVersion");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean checkPayCallbackSign(ApplePaymentObj cb) {
		try {

			String url = PROTOCOL_HEAD + host;

			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("receipt-data", cb.getReceipt());
			String jsonStr = UrlRequestUtils.executeHttpsByJson(url, jsonObject);
			logger.info("checkPayCallbackSign:" + jsonStr);
			JSONObject jObject = JSONObject.parseObject(jsonStr);
			if (jObject != null && "0".equals(jObject.getString("status"))) {
				JSONObject receipt = jObject.getJSONObject("receipt");
				if ("com.yxcq.fanting.apps".equals(receipt.getString("bid"))) {
					return true;
				}
			}

			url = PROTOCOL_HEAD + host;

			jsonObject = new JsonObject();
			jsonObject.addProperty("receipt-data", cb.getReceipt());
			jsonStr = UrlRequestUtils.executeHttpsByJson(url, jsonObject);
			logger.info("checkPayCallbackSign:" + jsonStr);
			jObject = JSONObject.parseObject(jsonStr);
			if (jObject != null && "0".equals(jObject.getString("status"))) {
				JSONObject receipt = jObject.getJSONObject("receipt");
				if ("com.yxcq.fanting.apps".equals(receipt.getString("bid"))) {
					return true;
				}
			}

			return false;
		} catch (Exception e) {
			logger.error("apple verify error!", e);
		}
		return false;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public String getTestHost() {
		return testHost;
	}

	public ExecutorService getExecutorService() {
		return executorService;
	}

	static class DefaultThreadFactory implements ThreadFactory {
		static final AtomicInteger poolNumber = new AtomicInteger(1);
		final ThreadGroup group;
		final AtomicInteger threadNumber = new AtomicInteger(1);
		final String namePrefix;

		DefaultThreadFactory() {
			SecurityManager s = System.getSecurityManager();
			group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
			namePrefix = "appleverifyReceipt-" + poolNumber.getAndIncrement() + "-thread-";
		}

		public Thread newThread(Runnable r) {
			Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
			if (t.isDaemon())
				t.setDaemon(false);
			if (t.getPriority() != Thread.NORM_PRIORITY)
				t.setPriority(Thread.NORM_PRIORITY);
			return t;
		}
	}

	private static class TrustAnyTrustManager implements X509TrustManager

	{

		public void checkClientTrusted(X509Certificate[] chain, String

		authType) throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] chain, String

		authType) throws CertificateException {
		}

		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[] {};
		}
	}

	private static class TrustAnyHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}

	public String getPayBackUrl() {
		return payBackUrl;
	}

}
