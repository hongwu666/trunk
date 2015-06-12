package com.lodogame.game.dao.initcache;

/**
 * 登陆时初始化缓存的接口
 * 
 * @author foxwang
 * 
 */
public interface InitCacheOnLogin {

	// 初始化缓存
	public void initOnLogin(String userId) throws Exception;
}
