package com.lodogame.ldsg.event;

import java.util.HashMap;
import java.util.Map;

public class BaseEvent implements Event {

	protected Map<String, Object> data = new HashMap<String, Object>();

	protected String userId;

	public String getUserId() {
		return this.userId;
	}

	public void setObject(String key, Object value) {
		this.data.put(key, value);
	}

	public Object getObject(String key) {
		return this.data.get(key);
	}

	public String getString(String key) {
		Object obj = this.data.get(key);
		if (obj != null) {
			return obj.toString();
		}
		return null;
	}

	@Override
	public Boolean getBoolean(String key) {
		return (Boolean) (this.data.get(key));
	}

	@Override
	public Long getLong(String key) {
		return Long.parseLong(getString(key));
	}

	public Integer getInt(String key) {
		return Integer.parseInt(getString(key));
	}

	public Double getDouble(String key) {
		return Double.parseDouble(getString(key));
	}

	public Integer getInt(String key, int defaultVal) {
		try {
			return Integer.parseInt(getString(key));
		} catch (Exception e) {
			return defaultVal;
		}
	}

}
