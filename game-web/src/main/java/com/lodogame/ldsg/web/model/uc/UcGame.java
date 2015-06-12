package com.lodogame.ldsg.web.model.uc;

public class UcGame {
	//cp编号
	private int cpId = 0;
	
	//游戏编号
	private int gameId = 0;
	
	// 游戏发行id
	private String channelId = "";
	
	// 游戏服务器id
	int serverId = 0;

	public int getCpId() {
		return cpId;
	}

	public void setCpId(int cpId) {
		this.cpId = cpId;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}
	
	
}
