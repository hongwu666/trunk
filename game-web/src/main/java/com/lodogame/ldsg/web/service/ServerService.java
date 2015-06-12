package com.lodogame.ldsg.web.service;

import java.util.List;

import com.lodogame.ldsg.web.model.GameServer;

public interface ServerService {

	/**
	 * 获取partner 服务器列表
	 * 
	 * @param partnerId
	 * @param imei
	 * @param ifr
	 * @param ip
	 * @return
	 */
	public List<GameServer> getServerList(String partnerId, String imei, String ip);

	/**
	 * 获取服务器http端口
	 * 
	 * @param serverId
	 * @return
	 */
	public int getServerHttpPort(String serverId);

	/**
	 * 获取服务器开放注册的状态
	 * 
	 * @param serverId
	 * @return
	 */
	public int getServerCloseRegStatus(String serverId);

	/**
	 * 清除缓存
	 * 
	 * @return
	 */
	public boolean cleanServerMap();

}
