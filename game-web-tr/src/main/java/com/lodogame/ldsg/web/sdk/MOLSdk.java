package com.lodogame.ldsg.web.sdk;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.lodogame.ldsg.web.dao.SystemMOLCardPaymentDao;

public class MOLSdk {
	private static final Logger LOGGER = Logger.getLogger(MOLSdk.class);

	@Autowired
	private static SystemMOLCardPaymentDao systemMOLCardPaymentDao;
	
	private String partnerId;
	private String appKey;
	private String payBackUrl;

	private Properties prop;
	private static MOLSdk ins;
	
	public static MOLSdk instance()  {
		synchronized (MOLSdk.class) {
			if (ins == null) {
				ins = new MOLSdk();
			}
		}
		
		return ins;
	}
	
	private MOLSdk() {
		loadSdkProperties();
	}
	
	private void loadSdkProperties() {
		try {
			prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("sdk.properties"));
			partnerId = prop.getProperty("MOLSdk.partnerId");
			appKey = prop.getProperty("MOLSdk.appKey");
			payBackUrl = prop.getProperty("MOLSdk.payBackUrl");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String getPartnerId() {
		return partnerId;
	}

	public String getAppKey() {
		return appKey;
	}

	public String getPaybackUrl() {
		return payBackUrl;
	}
	
	public static String payRequest(String url, String content) {
		HttpURLConnection conn = null;
		String result = "";
		try {
			
			URL u = new URL(url);
			conn = (HttpURLConnection) u.openConnection();
			
			conn.setRequestMethod("POST");
			conn.setUseCaches(false);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			
			conn.setConnectTimeout(15000);
			conn.setReadTimeout(15000);
			
			conn.setInstanceFollowRedirects(true);
			//conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
//			conn.setRequestProperty("Content-Type","multipart/form-data");		
			
			conn.connect();		

			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			dos.writeBytes(content);
			dos.flush();
			dos.close();
			int responseCode = conn.getResponseCode();
			
			if (responseCode == 200) {
				InputStreamReader in = new InputStreamReader(
						conn.getInputStream());
				BufferedReader br = new BufferedReader(in);
				String line = null;
				while ((line = br.readLine()) != null) {
					result += line;
				}
			} else {
				result = "NetworkError";
			}
		} catch (MalformedURLException e) {
			result = "NetworkError";
			// System.out.println("MalformedURLException!!!!!");
			// e.printStackTrace();
		} catch (IOException e) {
			result = "NetworkError";
			// System.out.println("IOException!!!!");
			// e.printStackTrace();
		} catch (Exception e) {
			result = "NetworkError";
			// System.out.println("Exception!!!!!");
			// e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		
		return result;

	}
}
