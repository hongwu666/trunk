package com.lodogame.ldsg.event;

/**
 * 事件接口
 * 
 * @author jacky
 * 
 */
public interface Event {

	public String getUserId();

	public Object getObject(String key);

	public String getString(String key);

	public Integer getInt(String key);

	public Long getLong(String key);

	public Double getDouble(String key);

	public Boolean getBoolean(String key);

	public Integer getInt(String key, int defaultVal);

	public void setObject(String key, Object value);
}
