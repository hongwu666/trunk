package com.lodogame.ldsg.config;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class Config {

	private static Config cfg;

	private String signKey;
	private String account;
	private String password;
	private boolean isGameServer;
	private boolean isWorldServer;
	private boolean isDebug;
	private boolean isCloeseReg;

	private int initGold;
	private int initCopper;
	private int initMuhon;
	private int initExp;
	private int initLevel;
	private int initVip;

	// 是否混服
	private boolean isFixServer = false;

	private String serverCloseNotice;
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
		isGameServer = NumberUtils.toInt(prop.getProperty("server.isGameServer", "0")) == 1;
		isWorldServer = NumberUtils.toInt(prop.getProperty("server.isWorldServer", "0")) == 1;
		initGold = NumberUtils.toInt(prop.getProperty("user.initGold", "0"));
		initCopper = NumberUtils.toInt(prop.getProperty("user.initCopper", "1000"));
		serverCloseNotice = prop.getProperty("server.closeNotice", "");
		serverCloseNotice = new String(serverCloseNotice.getBytes("ISO8859-1"), "utf8");

		String debug = prop.getProperty("debug", "false");
		String closeReg = prop.getProperty("reg.close", "false");
		String fixServer = prop.getProperty("debug", "false");

		initExp = NumberUtils.toInt(prop.getProperty("user.initExp", "0"));
		initLevel = NumberUtils.toInt(prop.getProperty("user.initLevel", "1"));
		initMuhon = NumberUtils.toInt(prop.getProperty("user.initMuhon", "0"));
		initVip = NumberUtils.toInt(prop.getProperty("user.initVip", "0"));

		isDebug = debug.equals("true");
		isCloeseReg = closeReg.equals("true");
		isFixServer = fixServer.equals("true");
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

	public String getServerCloseNotice() {
		return serverCloseNotice;
	}

	public int getIsHK() {
		return isHK;
	}

	public void setIsHK(int isHK) {
		this.isHK = isHK;
	}

	public int getInitMuhon() {
		return initMuhon;
	}

	public int getInitExp() {
		return initExp;
	}

	public int getInitLevel() {
		return initLevel;
	}

	public int getInitVip() {
		return initVip;
	}

	public boolean isCloeseReg() {
		return isCloeseReg;
	}

	public void setCloeseReg(boolean isCloeseReg) {
		this.isCloeseReg = isCloeseReg;
	}

	public boolean isFixServer() {
		return isFixServer;
	}

	public boolean isWorldServer() {
		return isWorldServer;
	}

	public void setWorldServer(boolean isWorldServer) {
		this.isWorldServer = isWorldServer;
	}

}
