package com.lodogame.ldsg.web.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.ldsg.web.dao.ActiveCodeDao;
import com.lodogame.ldsg.web.dao.ServerStatusDao;
import com.lodogame.ldsg.web.model.GameServer;
import com.lodogame.ldsg.web.sdk.AdminApiSdk;
import com.lodogame.ldsg.web.service.ServerService;

public class ServerServiceImpl implements ServerService {

	public static Logger LOG = Logger.getLogger(ServerServiceImpl.class);

	private Map<String, List<GameServer>> partnerServerMap = new HashMap<String, List<GameServer>>();

	private Map<String, GameServer> serverMap = new HashMap<String, GameServer>();

	@Autowired
	private ActiveCodeDao activeCodeDao;

	@Autowired
	private ServerStatusDao serverStatusDao;

	@Override
	public int getServerHttpPort(String serverId) {

		GameServer gameServer = serverMap.get(serverId);
		if (gameServer != null) {
			return gameServer.getHttpPort();
		}

		return 8088;
	}

	@Override
	public int getServerCloseRegStatus(String serverId) {

		GameServer gameServer = serverMap.get(serverId);
		if (gameServer != null) {
			return gameServer.getCloseReg();
		}

		return -1;
	}

	@Override
	public boolean cleanServerMap() {
		partnerServerMap.clear();
		serverMap.clear();
		return true;
	}

	@Override
	public List<GameServer> getServerList(String partnerId, String imei, String ip) {

		List<GameServer> serverList = partnerServerMap.get(partnerId);

		if (serverList == null || serverList.size() == 0) {
			serverList = AdminApiSdk.getInstance().loadServers(partnerId);
			if (serverList == null) {
				serverList = new ArrayList<GameServer>();
			} else {
				for (GameServer server : serverList) {
					serverMap.put(server.getServerId(), server);
				}
			}
			LOG.debug("serverList size:" + serverList.size());
			partnerServerMap.put(partnerId, serverList);
		}

		if (serverList != null && serverList.size() > 0) {
			return serverLiset(serverList, partnerId, imei, ip);

		}

		return serverList;
	}

	private List<GameServer> serverLiset(List<GameServer> serverList, String partnerId, String imei, String ip) {

		boolean isSpceImei = false;
		if (imei != null && activeCodeDao.isBlackImei(imei, partnerId)) {
			isSpceImei = true;
		}

		if (this.serverStatusDao.isWhiteIp(partnerId, ip)) {
			isSpceImei = true;
		}

		// 处理IMEI白名单
		List<GameServer> serverListForSpecImei = new ArrayList<GameServer>(serverList.size());
		for (GameServer gs : serverList) {

			// IMEI白名单中的GameServer状态都需要为2
			int status = gs.getStatus();
			if (status == 100) {
				if (isSpceImei) {
					status = 2;
				} else {
					continue;
				}
			}

			String ind = gs.getServerId().replaceAll("[a-zA-Z]+", "");

			GameServer gameServerForSpecImei = new GameServer();
			gameServerForSpecImei.setServerId(gs.getServerId());
			gameServerForSpecImei.setServerName(ind + "-" + gs.getServerName());
			gameServerForSpecImei.setServerPort(gs.getServerPort());
			gameServerForSpecImei.setStatus(status);
			gameServerForSpecImei.setOpenTime(gs.getOpenTime());

			serverListForSpecImei.add(gameServerForSpecImei);

		}

		Collections.sort(serverListForSpecImei, new Comparator<GameServer>() {

			@Override
			public int compare(GameServer o1, GameServer o2) {
				if (o1.getOpenTime() > o2.getOpenTime()) {
					return -1;
				} else if (o1.getOpenTime() < o2.getOpenTime()) {
					return 1;
				}

				return 0;
			}

		});

		return serverListForSpecImei;

	}
}
