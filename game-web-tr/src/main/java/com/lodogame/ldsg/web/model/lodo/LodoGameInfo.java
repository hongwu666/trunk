package com.lodogame.ldsg.web.model.lodo;

public class LodoGameInfo {

	private String cpid;
	private String serverid;
	private String gameid;

	public LodoGameInfo() {
		
	}
	
	public LodoGameInfo(String cpid, String serverid, String gameid) {
		this.cpid = cpid;
		this.serverid = serverid;
		this.gameid = gameid;
	}

	public String getCpid() {
		return cpid;
	}

	public void setCpid(String cpid) {
		this.cpid = cpid;
	}

	public String getServerid() {
		return serverid;
	}

	public void setServerid(String serverid) {
		this.serverid = serverid;
	}

	public String getGameid() {
		return gameid;
	}

	public void setGameid(String gameid) {
		this.gameid = gameid;
	}
}
