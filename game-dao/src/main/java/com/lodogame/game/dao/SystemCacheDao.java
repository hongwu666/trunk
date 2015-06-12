package com.lodogame.game.dao;

import java.util.List;

public interface SystemCacheDao<T> {

	public T get(String objKey);

	public T get(int objKey);

	public List<T> getList();

	public List<T> getList(String listKey);

}
