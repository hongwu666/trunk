package com.lodogame.game.dao.initcache;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * 缓存清理管理器
 * 
 * @author foxwang
 * 
 */
public class InitCacheManager {

	private List<InitCacheOnLogin> cacheList = new ArrayList<InitCacheOnLogin>();
	private static final Logger LOG = Logger.getLogger(InitCacheManager.class);
	private static InitCacheManager cacheManager;

	private InitCacheManager() {
	}

	public static InitCacheManager getInstance() {
		if (cacheManager == null) {
			cacheManager = new InitCacheManager();
		}
		return cacheManager;
	}

	/**
	 * 添加缓存
	 * 
	 * @param cache
	 */
	public void addCache(InitCacheOnLogin cache) {
		cacheList.add(cache);
	}

	/**
	 * 初始化缓存
	 */
	public void executeUserLogin(String userId) {
		for (InitCacheOnLogin initCacheOnLogin : cacheList) {
			try {
				LOG.debug("初始化cache[" + initCacheOnLogin.getClass().getSimpleName() + "]");
				initCacheOnLogin.initOnLogin(userId);
			} catch (Exception e) {
				LOG.error(e);
			}
		}
	}
}
