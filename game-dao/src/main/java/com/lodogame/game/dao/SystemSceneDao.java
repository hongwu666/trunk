package com.lodogame.game.dao;

import com.lodogame.model.SystemScene;

public interface SystemSceneDao {

	/**
	 * 获取关卡
	 * 
	 * @param sceneId
	 * @return
	 */
	public SystemScene get(int sceneId);
}
