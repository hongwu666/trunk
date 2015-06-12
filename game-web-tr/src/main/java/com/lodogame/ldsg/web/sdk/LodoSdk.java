package com.lodogame.ldsg.web.sdk;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.lodogame.ldsg.web.model.lodo.LodoboGameData;
import com.lodogame.ldsg.web.model.lodo.LodoReturnData;
import com.lodogame.ldsg.web.model.lodo.LodoGameInfo;
import com.lodogame.ldsg.web.util.Json;
import com.lodogame.ldsg.web.util.MD5;
import com.lodogame.ldsg.web.util.UrlRequestUtils;


public class LodoSdk {
	
	private static final Logger LOGGER = Logger.getLogger(LodoSdk.class);

	private static final String SUCCESS = "1";

	private static LodoSdk ins;
	private Properties prop;
	private LodoGameInfo game;
	private String apikey;
	private String verifyUrl;
	private String service;
	private String payBackUrl;
	private String partnerId;
	
	public static LodoSdk instance() {
		synchronized (LodoSdk.class) {
			if (ins == null) {
				ins = new LodoSdk();
			}
		}
		return ins;
	}
	
	private LodoSdk() {
		loadSdkProperties();
	}

	public void reload() {
		loadSdkProperties();
	}

	private void loadSdkProperties() {
		try {
			prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("sdk.properties"));
			
			String cpid = prop.getProperty("LodoSdk.cpId");
			String gameid = prop.getProperty("LodoSdk.gameId");
			String serverid = prop.getProperty("LodoSdk.serverid");
			game = new LodoGameInfo(cpid, serverid, gameid);
			
			apikey = prop.getProperty("LodoSdk.apiKey");
			verifyUrl = prop.getProperty("LodoSdk.verifyUrl");
			service = prop.getProperty("LodoSdk.service");
			
			payBackUrl = prop.getProperty("LodoSdk.payBackUrl");
			partnerId = prop.getProperty("LodoSdk.partnerId");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public String verifySession(String session) {

		Map<String, Object> params = getParams(session);
		String jsonParam = Json.toJson(params);

		LOGGER.debug("验证用户 - 请求参数" + jsonParam);
		
		String response = UrlRequestUtils.executeHttpByJson(verifyUrl, jsonParam);

		LOGGER.info("验证用户, 返回结果" + response);
		
		LodoReturnData returnData = Json.toObject(response, LodoReturnData.class);
		if (returnData != null) {
			if (returnData.getState().getCode().equals(SUCCESS)) {
				return returnData.getData().getUserid();
			}
		}
		
		return null;
	}
	
	
	public String verifySessionToFormalSDK(String session) {
		Map<String, Object> params = getParamsToFormalSdk(session);
		String jsonParam = Json.toJson(params);
		LOGGER.info("去正式 SDK 验证用户 - 请求参数" + jsonParam);
		
		String url = "http://ucenter.lodogame.com/loginverify";
		
		String response = UrlRequestUtils.executeHttpByJson(url, jsonParam);
		
		LOGGER.info("正式 SDK 验证用户, 返回结果" + response);

		LodoReturnData returnData = Json.toObject(response, LodoReturnData.class);
		if (returnData != null) {
			if (returnData.getState().getCode().equals(SUCCESS)) {
				return returnData.getData().getUserid();
			}
		}
		
		return null;
	}

	private Map<String, Object> getParams(String session) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", new Date().getTime());

		LodoboGameData data = new LodoboGameData(session);
		params.put("data", data);
		
		params.put("game", game);
		params.put("sign", getSign(game, data.getSid()));
		
		return params;
	}
	
	private Map<String, Object> getParamsToFormalSdk(String session) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", new Date().getTime());
		LodoboGameData data = new LodoboGameData(session);
		
		String cpid = "1000";
		String gameid = "10000";
		String serverid = "0";
		
		LodoGameInfo tmp = new LodoGameInfo(cpid, serverid, gameid);
		params.put("data", data);
		params.put("game", tmp);
		params.put("sign", getSignToFormalSDK(tmp, data.getSid()));
		
		return params;
	}
	
	private String getSign(LodoGameInfo game, String sessionId) {
		String sign =  game.getCpid() + "sid=" + sessionId + apikey;
		return MD5.MD5Encode(sign);
	}
	
	private String getSignToFormalSDK(LodoGameInfo game, String sessionId) {
		String key = "f0a97500aa76416981f6d6e580d6167e";
		String sign =  game.getCpid() + "sid=" + sessionId + key;
		return MD5.MD5Encode(sign);
	}

	public Properties getProp() {
		return prop;
	}

	public void setProp(Properties prop) {
		this.prop = prop;
	}

	public LodoGameInfo getGame() {
		return game;
	}

	public void setGame(LodoGameInfo game) {
		this.game = game;
	}

	public String getApikey() {
		return apikey;
	}

	public void setApikey(String apikey) {
		this.apikey = apikey;
	}

	public String getUrl() {
		return verifyUrl;
	}

	public void setUrl(String url) {
		this.verifyUrl = url;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getPayBackUrl() {
		return payBackUrl;
	}

	public void setPayBackUrl(String payBackUrl) {
		this.payBackUrl = payBackUrl;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

}
