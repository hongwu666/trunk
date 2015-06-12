package com.lodogame.ldsg.web;

import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class Config {

	private static Config cfg;

	private String signKey;
	private String account;
	private String password;
	private boolean isGameServer;
	private boolean isDebug;
	private boolean openArena;
	private int initGold;
	private int initCopper;
	private boolean isServerClose;
	private String serverCloseNotice;
	private String getServerListUrl;
	private int isHK;

	private Properties prop;

	public static Config ins() {
		synchronized (Config.class) {
			if (cfg == null) {
				cfg = new Config();
			}
		}
		return cfg;
	}

	private Config() {
		try {
			initConfig();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void initConfig() throws IOException {
		prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("webconfig.properties"));
		isHK = Integer.valueOf(prop.getProperty("server.isHK", "0"));
		signKey = prop.getProperty("signKey", "test");
		account = prop.getProperty("account", "webadmin");
		password = prop.getProperty("password", "webadmin");
		serverCloseNotice = prop.getProperty("server.closeNotice", "");
		serverCloseNotice = new String(serverCloseNotice.getBytes("ISO8859-1"), "utf8");
		getServerListUrl = prop.getProperty("getServerListUrl", "http://wapi.lieguo.lodogame.com:8088/webApi/getServerList.do");

		String debug = prop.getProperty("debug", "false");
		isDebug = debug.equals("true");

		openArena = "true".equals(prop.getProperty("openArena", "true"));
	}

	public String getSignKey() {
		return signKey;
	}

	public void setSignKey(String signKey) {
		this.signKey = signKey;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isGameServer() {
		return isGameServer;
	}

	public boolean isDebug() {
		return isDebug;
	}

	public void setDebug(boolean isDebug) {
		this.isDebug = isDebug;
	}

	public int getInitGold() {
		return initGold;
	}

	public int getInitCopper() {
		return initCopper;
	}

	public boolean isServerClose() {
		return isServerClose;
	}

	public String getServerCloseNotice() {
		return serverCloseNotice;
	}

	public boolean isOpenArena() {
		return openArena;
	}

	public void setOpenArena(boolean openArena) {
		this.openArena = openArena;
	}

	public int getIsHK() {
		return isHK;
	}

	public void setIsHK(int isHK) {
		this.isHK = isHK;
	}

	public String getGetServerListUrl() {
		return getServerListUrl;
	}

}
