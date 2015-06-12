package com.lodogame.ldsg.web.model;

import java.io.Serializable;

public class GameServer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 服务器ID
	 */
	private String serverId;

	/**
	 * 服务器名称
	 */
	private String serverName;

	/**
	 * 服务器端口号
	 */
	private int serverPort;

	/**
	 * http 端口
	 */
	private int httpPort;

	/**
	 * 是否关闭注册
	 */
	private int closeReg;

	/**
	 * 服务器状态
	 */
	private int status;

	/**
	 * 开放时间
	 */
	private long openTime;

	public long getOpenTime() {
		return openTime;
	}

	public void setOpenTime(long openTime) {
		this.openTime = openTime;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public int getHttpPort() {
		return httpPort;
	}

	public void setHttpPort(int httpPort) {
		this.httpPort = httpPort;
	}

	public int getCloseReg() {
		return closeReg;
	}

	public void setCloseReg(int closeReg) {
		this.closeReg = closeReg;
	}

}
