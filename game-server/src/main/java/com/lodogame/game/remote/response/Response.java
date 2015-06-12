package com.lodogame.game.remote.response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.lodogame.game.utils.json.Json;

public class Response {

	private static final Logger logger = Logger.getLogger(Response.class);

	private String id;

	private int rc = 1000;

	private String message;

	private Map<String, Object> data = new HashMap<String, Object>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getRc() {
		return rc;
	}

	public void setRc(int rc) {
		this.rc = rc;
	}

	public void put(String key, Object value) {
		this.data.put(key, value);
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int get(String key, int defaultValue) {
		try {
			return Integer.parseInt(this.data.get(key).toString());
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public String get(String key, String defaultValue) {
		try {
			return this.data.get(key).toString();
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public Object getObject(String key) {
		try {
			Object obj = this.data.get(key);
			return obj;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public <T> T getObject(String key, Class<T> cls) {
		try {
			Object obj = this.data.get(key);
			return Json.toObject(Json.toJson(obj), cls);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public <T> List<T> getList(String key, Class<T> cls) {
		try {
			Object obj = this.data.get(key);
			return Json.toList(Json.toJson(obj), cls);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new ArrayList<T>();
	}
}
