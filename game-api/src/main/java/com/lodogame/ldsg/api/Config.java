package com.lodogame.ldsg.api;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class Config {

	private static Config cfg;

	private String signKey;
	private int port;

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
		signKey = prop.getProperty("signKey", "test");
		port = NumberUtils.toInt(prop.getProperty("http.port", "80"));
	}

	public String getSignKey() {
		return signKey;
	}

	public void setSignKey(String signKey) {
		this.signKey = signKey;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}
