package com.lodogame.game.dao.impl.redis;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lodogame.game.dao.UserSceneDao;
import com.lodogame.game.dao.daobase.redis.RedisMapBase;
import com.lodogame.game.utils.RedisKey;
import com.lodogame.model.UserScene;

public class UserSceneDaoRedisImpl extends RedisMapBase<UserScene> implements UserSceneDao {
	/**
	 * 初始化
	 * 
	 * @param userId
	 * @param sceneList
	 */
	public void initUserSceneList(String userId, List<UserScene> sceneList) {
		Map<String, UserScene> map = new HashMap<String, UserScene>();
		for (UserScene userScene : sceneList) {
			map.put(userScene.getSceneId() + "", userScene);
		}
		this.initEntry(userId, map);
	}

	@Override
	public List<UserScene> getUserSceneList(String userId) {
		return this.getAllEntryValue(userId);
	}

	@Override
	public int add(UserScene userScene) {
		// 添加的时候要先查看是否存在该缓存 否则会发生错误
		if (this.existUserId(userScene.getUserId())) {
			this.updateEntryEntry(userScene.getUserId(), userScene.getSceneId() + "", userScene);
		}
		return 1;
	}

	@Override
	public boolean updateScenePassed(String userId, int sceneId) {
		UserScene scene = this.getEntryEntry(userId, sceneId + "");
		if (scene != null) {
			Date date = new Date();
			scene.setPassFlag(1);
			scene.setUpdatedTime(date);
			this.updateEntryEntry(userId, sceneId + "", scene);
			return true;
		}
		return false;
	}

	@Override
	public String getPreKey() {
		return RedisKey.getUserSceneKeyPre();
	}

	@Override
	public UserScene getUserSceneBySceneId(String userId, int sceneId) {
		return this.getEntryEntry(userId, sceneId + "");
	}

	@Override
	public boolean updateSceneOneStarReward(String userId, int sceneId, int state) {
		UserScene scene = this.getEntryEntry(userId, sceneId + "");
		if (scene != null) {
			scene.setOneStarReward(state);
			this.updateEntryEntry(userId, sceneId + "", scene);
			return true;
		}
		return false;
	}

	@Override
	public boolean updateSceneTwoStarReward(String userId, int sceneId, int state) {
		UserScene scene = this.getEntryEntry(userId, sceneId + "");
		if (scene != null) {
			scene.setTwoStarReward(state);
			this.updateEntryEntry(userId, sceneId + "", scene);
			return true;
		}
		return false;
	}

	@Override
	public boolean updateSceneThirdStarReward(String userId, int sceneId, int state) {
		UserScene scene = this.getEntryEntry(userId, sceneId + "");
		if (scene != null) {
			scene.setThirdStarReward(state);
			this.updateEntryEntry(userId, sceneId + "", scene);
			return true;
		}
		return false;
	}

}
