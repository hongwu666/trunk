package com.lodogame.ldsg.web.sdk;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.lodogame.ldsg.web.model.diyibo.DiyiboGameData;
import com.lodogame.ldsg.web.model.diyibo.DiyiboGameInfo;
import com.lodogame.ldsg.web.model.diyibo.DiyiboReturnData;
import com.lodogame.ldsg.web.util.Json;
import com.lodogame.ldsg.web.util.MD5;
import com.lodogame.ldsg.web.util.UrlRequestUtils;

public class DiyiboSdk {

	private static final Logger LOGGER = Logger.getLogger(DiyiboSdk.class);

	private static final String SUCCESS = "1";

	private static DiyiboSdk ins;
	private Properties prop;
	private DiyiboGameInfo game;
	private String apikey;
	private String url;
	private String service;
	private String payBackUrl;
	private String partnerId;

	public static DiyiboSdk instance() {
		synchronized (DiyiboSdk.class) {
			if (ins == null) {
				ins = new DiyiboSdk();
			}
		}
		return ins;
	}

	private DiyiboSdk() {
		loadSdkProperties();
	}

	public void reload() {
		loadSdkProperties();
	}

	private void loadSdkProperties() {
		try {
			prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("sdk.properties"));

			String cpid = prop.getProperty("DiyiboSdk.cpid");
			String gameid = prop.getProperty("DiyiboSdk.gameid");
			String serverid = prop.getProperty("DiyiboSdk.serverid");
			game = new DiyiboGameInfo(cpid, serverid, gameid);

			apikey = prop.getProperty("DiyiboSdk.apikey");
			url = prop.getProperty("DiyiboSdk.url");
			service = prop.getProperty("DiyiboSdk.service");

			payBackUrl = prop.getProperty("DiyiboSdk.payBackUrl");
			partnerId = prop.getProperty("DiyoboSdk.partnerId");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public String verifySession(String session) {

		Map<String, Object> params = getParams(session);
		String jsonParam = Json.toJson(params);

		LOGGER.info("请求“第一波”服武器，验证用户。参数 " + jsonParam);

		String response = UrlRequestUtils.executeHttpByJson(url, jsonParam);

		LOGGER.info("第一波验证用户, 返回 " + response);

		DiyiboReturnData returnData = Json.toObject(response, DiyiboReturnData.class);
		if (returnData != null) {
			if (returnData.getState().getCode().equals(SUCCESS)) {
				return returnData.getData().getUserid();
			}
		}

		return null;
	}

	private Map<String, Object> getParams(String session) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", String.valueOf(new Date().getTime()));

		DiyiboGameData data = new DiyiboGameData(session);
		params.put("data", data);

		params.put("game", game);
		params.put("sign", getSign(game, data.getSid()));

		return params;
	}

	private String getSign(DiyiboGameInfo game, String sessionId) {
		String sign = game.getCpid() + "sid=" + sessionId + apikey;
		return MD5.MD5Encode(sign);
	}

	public Properties getProp() {
		return prop;
	}

	public void setProp(Properties prop) {
		this.prop = prop;
	}

	public DiyiboGameInfo getGame() {
		return game;
	}

	public void setGame(DiyiboGameInfo game) {
		this.game = game;
	}

	public String getApikey() {
		return apikey;
	}

	public void setApikey(String apikey) {
		this.apikey = apikey;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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
