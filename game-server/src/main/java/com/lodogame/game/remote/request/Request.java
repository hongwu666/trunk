package com.lodogame.game.remote.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.lodogame.game.utils.json.Json;

public class Request {

	private static final Logger logger = Logger.getLogger(Request.class);

	public String id;

	public String action;

	public String method;

	public String callbackKey;

	public Map<String, Object> parameters = new HashMap<String, Object>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getCallbackKey() {
		return callbackKey;
	}

	public void setCallbackKey(String callbackKey) {
		this.callbackKey = callbackKey;
	}

	public void put(String key, Object value) {
		this.parameters.put(key, value);
	}

	public int get(String key, int defaultValue) {
		try {
			return Integer.parseInt(this.parameters.get(key).toString());
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public <T> List<T> getList(String key, Class<T> cls) {
		try {
			Object obj = this.parameters.get(key);
			return Json.toList(Json.toJson(obj), cls);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new ArrayList<T>();
	}

	public <T> T getObject(String key, Class<T> cls) {
		try {
			Object obj = this.parameters.get(key);
			return Json.toObject(Json.toJson(obj), cls);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public String get(String key, String defaultValue) {
		try {
			return this.parameters.get(key).toString();
		} catch (Exception e) {
			return defaultValue;
		}
	}

}
