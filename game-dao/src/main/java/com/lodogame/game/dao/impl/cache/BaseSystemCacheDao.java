package com.lodogame.game.dao.impl.cache;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.game.dao.BasePreloadAble;
import com.lodogame.game.dao.SystemCacheDao;
import com.lodogame.model.SystemModel;

public class BaseSystemCacheDao<T extends SystemModel> extends BasePreloadAble implements SystemCacheDao<T> {

	private List<T> list;

	private Map<String, List<T>> listCache;

	private Map<String, T> objCache;

	@Autowired
	private Jdbc jdbc;

	private Class<T> entityClass;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public BaseSystemCacheDao() {
		Type genType = getClass().getGenericSuperclass();
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		entityClass = (Class) params[0];
	}

	@Override
	public T get(String objKey) {
		if (objCache != null) {
			return objCache.get(objKey);
		}
		return null;
	}

	@Override
	public T get(int objKey) {
		return this.get(String.valueOf(objKey));
	}

	@Override
	public List<T> getList() {
		return list;
	}

	@Override
	public List<T> getList(String listKey) {
		if (listCache != null && listCache.containsKey(listKey)) {
			return listCache.get(listKey);
		}
		return new ArrayList<T>();
	}

	private List<T> getAll() {

		String table = className2TableName(entityClass.getSimpleName());
		String sql = "SELECT * FROM " + table;
		return this.jdbc.getList(sql, entityClass);

	}

	private String className2TableName(String className) {

		char[] chs = className.toCharArray();

		StringBuffer tableName = new StringBuffer();
		tableName.append(chs[0]);
		for (int i = 1; i < chs.length; i++) {
			byte bt = (byte) chs[i];
			if (bt >= 65 && bt <= 90) {
				tableName.append("_");
				tableName.append(chs[i]);
			} else {
				tableName.append(chs[i]);
			}
		}

		return tableName.toString().toLowerCase();

	}

	@Override
	protected void initData() {

		if (listCache != null) {
			listCache.clear();
		}

		if (objCache != null) {
			objCache.clear();
		}

		list = this.getAll();

		for (T model : list) {

			String objKey = model.getObjKey();
			String listKey = model.getListeKey();

			if (StringUtils.isNotEmpty(listKey)) {
				if (listCache == null) {
					listCache = new ConcurrentHashMap<String, List<T>>();
				}
				List<T> l = listCache.get(listKey);
				if (l == null) {
					l = new ArrayList<T>();
				}
				l.add(model);
				listCache.put(listKey, l);
			}

			if (StringUtils.isNotEmpty(objKey)) {
				if (objCache == null) {
					objCache = new ConcurrentHashMap<String, T>();
				}
				objCache.put(objKey, model);
			}

		}

	}
}
