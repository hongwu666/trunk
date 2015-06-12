package com.lodogame.ldsg.web.sdk;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.lodogame.ldsg.web.Config;
import com.lodogame.ldsg.web.model.GameServer;
import com.lodogame.ldsg.web.util.Json;
import com.lodogame.ldsg.web.util.UrlRequestUtils;

public class AdminApiSdk {

	private final static Logger LOG = Logger.getLogger(GameApiSdk.class);

	private static AdminApiSdk sdk;

	private AdminApiSdk() {
	}

	public static AdminApiSdk getInstance() {
		synchronized (AdminApiSdk.class) {
			if (sdk == null) {
				sdk = new AdminApiSdk();
			}
		}
		return sdk;
	}

	@SuppressWarnings("unchecked")
	public List<GameServer> loadServers(String partnerId) {
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("partnerId", partnerId);
			String jsonStr = UrlRequestUtils.execute(Config.ins().getGetServerListUrl(), params, UrlRequestUtils.Mode.GET);
			Map<String, String> map = Json.toObject(jsonStr, Map.class);
			String serverJson = map.get("servers");
			List<GameServer> ret = Json.toList(serverJson, GameServer.class);
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(), e);
			return null;
		}
	}
	
	public static void main(String[] args){
		AdminApiSdk apiSdk = AdminApiSdk.getInstance();
		List<GameServer> ret = apiSdk.loadServers("1002");
//		System.out.println(apiSdk.loadServers("1002"));
	}

}
