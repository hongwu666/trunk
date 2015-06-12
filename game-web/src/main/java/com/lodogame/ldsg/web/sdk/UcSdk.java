package com.lodogame.ldsg.web.sdk;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.lodogame.ldsg.web.helper.HttpHelper;
import com.lodogame.ldsg.web.model.uc.PayCallbackResponse;
import com.lodogame.ldsg.web.model.uc.SidInfoResponse;
import com.lodogame.ldsg.web.model.uc.UcGame;
import com.lodogame.ldsg.web.util.EncryptUtil;
import com.lodogame.ldsg.web.util.Json;

public class UcSdk {
	private static final Logger logger = Logger.getLogger(UcSdk.class);

	// private static ObjectMapper objectMapper = new ObjectMapper();

	private static UcSdk ins;

	private static Properties prop;

	private final static String PROTOCOL_HEAD = "http://";
	private String partnerId;
	private String host;
	private UcGame ucGame;
	private String apiKey;
	private String paybackUrl;

	public static UcSdk instance() {
		synchronized (UcSdk.class) {
			if (ins == null) {
				ins = new UcSdk();
			}
		}
		return ins;
	}

	private UcSdk() {
		loadSdkProperties();
	}

	public void reload() {
		loadSdkProperties();
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public String getPaybackUrl() {
		return paybackUrl;
	}

	public void setPaybackUrl(String paybackUrl) {
		this.paybackUrl = paybackUrl;
	}

	private void loadSdkProperties() {
		try {
			prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("sdk.properties"));
			host = prop.getProperty("UcSdk.host");
			ucGame = new UcGame();
			ucGame.setCpId(Integer.valueOf(prop.getProperty("UcSdk.cpId", "0")));
			ucGame.setGameId(Integer.valueOf(prop.getProperty("UcSdk.gameId", "0")));
			apiKey = prop.getProperty("UcSdk.apiKey");
			partnerId = prop.getProperty("UcSdk.partnerId");
			paybackUrl = prop.getProperty("UcSdk.payBackUrl");
			ucGame.setChannelId(prop.getProperty("UcSdk.channelId", ""));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 验证SID信息
	 * 
	 * @param sid
	 * @param serverId
	 * @param channelId
	 * @return
	 * @throws Exception
	 */
	public SidInfoResponse sidInfo(String sid) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", Long.toString(System.currentTimeMillis()));// 当前系统时间
		params.put("service", "ucid.user.sidInfo");
		ucGame.setServerId(0);
		// ucGame.setChannelId(channelId);
		params.put("game", ucGame);

		Map<String, String> data = new HashMap<String, String>();
		data.put("sid", sid);
		params.put("data", data);
		params.put("encrypt", "md5");

		String signSource = ucGame.getCpId() + "sid=" + sid + apiKey;

		String sign = EncryptUtil.getMD5(signSource);
		params.put("sign", sign.toLowerCase());

		String jsonStr = HttpHelper.doPost(PROTOCOL_HEAD + host + "/cp/account.verifySession", Json.toJson(params));

		logger.info("UcSdk.sidInfo result:" + jsonStr);

		return Json.toObject(jsonStr, SidInfoResponse.class);
	}

	public boolean checkPayCallbackSign(PayCallbackResponse rsp) {

		logger.debug("[sign]" + rsp.getSign());
		logger.debug("[orderId]" + rsp.getData().getOrderId());
		logger.debug("[gameId]" + rsp.getData().getGameId());
		logger.debug("[accountId]" + rsp.getData().getAccountId());
		logger.debug("[creator]" + rsp.getData().getCreator());
		logger.debug("[payWay]" + rsp.getData().getPayWay());
		logger.debug("[amount]" + rsp.getData().getAmount());
		logger.debug("[callbackInfo]" + rsp.getData().getCallbackInfo());
		logger.debug("[orderStatus]" + rsp.getData().getOrderStatus());
		logger.debug("[failedDesc]" + rsp.getData().getFailedDesc());
		logger.debug("[cpOrderId]" + rsp.getData().getCpOrderId());

		String signSource = "accountId=" + rsp.getData().getAccountId() + "amount=" + rsp.getData().getAmount() + "callbackInfo=" + rsp.getData().getCallbackInfo() + "cpOrderId="
				+ rsp.getData().getCpOrderId() + "creator=" + rsp.getData().getCreator() + "failedDesc=" + rsp.getData().getFailedDesc() + "gameId=" + rsp.getData().getGameId() + "orderId="
				+ rsp.getData().getOrderId() + "orderStatus=" + rsp.getData().getOrderStatus() + "payWay=" + rsp.getData().getPayWay() + this.apiKey;

		String sign = EncryptUtil.getMD5(signSource);

		logger.debug("[签名原文]" + signSource);
		logger.debug("[签名结果]" + sign);

		if (sign.toLowerCase().equals(rsp.getSign().toLowerCase())) {
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		UcSdk ucSdk = UcSdk.instance();
		try {
			ucSdk.sidInfo("sst1game643cbb072e41480cbedb76d453d3b782165258");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
