package com.lodogame.ldsg.web.sdk.mycard;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.lodogame.ldsg.web.dao.impl.SystemMyCardPaymentDaoMysqlImpl;
import com.lodogame.ldsg.web.model.mycard.SystemMyCardCardPayment;
import com.lodogame.ldsg.web.util.EncryptUtil;
import com.lodogame.ldsg.web.util.Json;
import com.lodogame.ldsg.web.util.UrlRequestUtils;

public class MyCardSdk {

	private static Logger LOGGER = Logger.getLogger(MyCardSdk.class); 
	
	private Properties prop;
	private static MyCardSdk ins;
	
	private String partnerId;
	private String facId;
	private String securityKey;
	private String sendConfirmUrl;
	private String authUrl;
	private String serverToServerPayUrl;
	
	/**
	 * 在获取授权码时，用户计算签名的 key。共有两个 key 用于计算签名，这个 key 由 MyCard 提供
	 */
	private String authKeyMyCard;
	
	/**
	 * 在获取授权码时，用户计算签名的 key。共有两个 key 用于计算签名，这个 key 由 CP 方提供
	 */
	private String authKeyCP;
	
	public static MyCardSdk instance() {
		synchronized (MyCardSdk.class) { 
			if (ins == null) {
				ins = new MyCardSdk();
			}
		}
		
		return ins;
	}
	
	private MyCardSdk() {
		loadSdkProperties();
	}
	
	private void loadSdkProperties() {
		try {
			prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("sdk.properties"));
			partnerId = prop.getProperty("MyCardSdk.partnerId");
			facId = prop.getProperty("MyCardSdk.facId");
			securityKey = prop.getProperty("MyCardSdk.securityKey");
			sendConfirmUrl = prop.getProperty("MyCardSdk.sendConfirmUrl");
			authUrl = prop.getProperty("MyCardSdk.authUrl");
			authKeyMyCard = prop.getProperty("MyCardSdk.authKeyMyCard");
			authKeyCP = prop.getProperty("MyCardSdk.authKeyCP");
			serverToServerPayUrl = prop.getProperty("MyCardSdk.serverToServerPayUrl");
					
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String getSecurityKey() {
		return securityKey;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public String getSendConfirmUrl() {
		return sendConfirmUrl;
	}

	public String getFacId() {
		return facId;
	}

	public String getAuthKeyMyCard() {
		return authKeyMyCard;
	}

	public String getAuthKeyCP() {
		return authKeyCP;
	}
	
	public String getServerToServerPayUrl() {
		return serverToServerPayUrl;
	}

	public String auth(String gameOrderId) {
		Map<String, String> map = new HashMap<String, String>();
		
		String strToComputeSign = authKeyMyCard + facId + gameOrderId + authKeyCP;
		String sign = EncryptUtil.getSHA256(strToComputeSign);
		
		LOGGER.debug("MyCard 获取授权码 - 计算签名使用的字符串[" + strToComputeSign + "] 计算得到的签名[" + sign + "]");
		
		map.put("facId", this.facId);
		map.put("facTradeSeq", gameOrderId);
		map.put("hash", sign);
		
		String jsonStr = UrlRequestUtils.execute(this.authUrl, map, UrlRequestUtils.Mode.GET);
		
		LOGGER.info("MyCard 获取授权码返回结果[" + jsonStr + "]");
		
		return jsonStr;
	}
	

}
