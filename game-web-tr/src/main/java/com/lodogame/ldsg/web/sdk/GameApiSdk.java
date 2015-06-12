package com.lodogame.ldsg.web.sdk;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.lodogame.ldsg.web.Config;
import com.lodogame.ldsg.web.constants.ServiceReturnCode;
import com.lodogame.ldsg.web.model.UserToken;
import com.lodogame.ldsg.web.util.Json;
import com.lodogame.ldsg.web.util.UrlRequestUtils;

public class GameApiSdk {

	private final static Logger LOG = Logger.getLogger(GameApiSdk.class);

	private final static String HTTP_URL_HEAD = "http://";
	private static String HOST = "lieguo.lodogame.com";

	private static GameApiSdk sdk;

	private Map<String, Integer> payMap;

	private GameApiSdk() {
	}

	public static GameApiSdk getInstance() {
		synchronized (GameApiSdk.class) {
			if (sdk == null) {
				sdk = new GameApiSdk();
			}
		}
		return sdk;
	}

	@SuppressWarnings("unchecked")
	public UserToken loadUserToken(String partnerUserId, String partnerId, String serverId, int port, String qn, Map<String, String> params) {
		String requestData = "去游戏服获取 userToken, 请求参数 - partnerUserId[" + partnerUserId + "] partnerId[" + partnerId + "] serverId[" + serverId + "] port[" + port + "] qn[" + qn + "]";
		LOG.debug(requestData);
		
		try {
			String url = HTTP_URL_HEAD + serverId + "." + HOST + ":" + port + "/gameApi/loadUserToken.do";

			String imei = params.get("imei");
			String mac = params.get("mac");
			String idfa = params.get("idfa");

			Map<String, String> paraMap = new HashMap<String, String>();
			paraMap.put("partnerUserId", partnerUserId);
			paraMap.put("partnerId", partnerId);
			paraMap.put("serverId", serverId);
			paraMap.put("qn", qn);
			paraMap.put("imei", imei);
			paraMap.put("mac", mac);
			paraMap.put("idfa", idfa);
			
			String jsonStr = UrlRequestUtils.execute(url, paraMap, UrlRequestUtils.Mode.GET);
			
			LOG.debug("去游戏服获取 userTokan, 返回结果" + jsonStr);
			
			Map<String, Object> ret = Json.toObject(jsonStr, Map.class);
			Map<String, String> map = (Map<String, String>) ret.get("userToken");
			UserToken tk = new UserToken();
			tk.setPartnerId(map.get("partnerId"));
			tk.setPartnerUserId(map.get("partnerUserId"));
			tk.setServerId(map.get("serverId"));
			tk.setToken(map.get("token"));
			tk.setUserId(map.get("userId"));
			return tk;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public boolean doPayment(String partnerId, String serverId, int port, String partnerUserId, int waresId, String channel, String orderId, BigDecimal amount, int gold, String userIp, String remark) {

		try {
			String url = HTTP_URL_HEAD + serverId + "." + HOST + ":" + port + "/gameApi/payment.do";
			Map<String, String> paraMap = new HashMap<String, String>();
			paraMap.put("partnerId", partnerId);
			paraMap.put("serverId", serverId);
			paraMap.put("partnerUserId", partnerUserId);
			paraMap.put("channel", channel);
			paraMap.put("orderId", orderId);
			paraMap.put("amount", String.valueOf(amount));
			paraMap.put("gold", String.valueOf(gold));
			paraMap.put("userIp", userIp);
			paraMap.put("remark", remark);
			paraMap.put("waresId", String.valueOf(waresId));
			String jsonStr = UrlRequestUtils.execute(url, paraMap, UrlRequestUtils.Mode.GET);
			LOG.info(jsonStr);
			Map<String, Object> retVal = Json.toObject(jsonStr, Map.class);
			int rc = Integer.parseInt(retVal.get("rc").toString());
			return rc == ServiceReturnCode.SUCCESS;

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return false;
		}

	}

	/**
	 * 获取套餐
	 * 
	 * @param serverId
	 * @param playerIds
	 * @param gold
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int getSystemGoldSetId(String serverId, String amount) {
		if (payMap != null && !payMap.isEmpty()) {
			return payMap.get(amount);
		}
		payMap = new HashMap<String, Integer>();
		try {
			String url = HTTP_URL_HEAD + serverId + "." + HOST + "/gameApi/getPaySettings.do";
			Map<String, String> paraMap = new HashMap<String, String>();
			String jsonStr = UrlRequestUtils.execute(url, paraMap, UrlRequestUtils.Mode.GET);
			Map<String, List<Map<String, Object>>> retVal = Json.toObject(jsonStr, Map.class);
			List<Map<String, Object>> list = retVal.get("list");
			for (Map<String, Object> item : list) {
				BigDecimal money = new BigDecimal((Double) item.get("money"));
				payMap.put(Double.toString(money.doubleValue()), (Integer) item.get("systemGoldSetId"));
			}

			return payMap.get(amount);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return 0;
		}
	}

	public static void main(String[] args) {
		System.out.println(new String("s1").contains("s"));
	}
}
