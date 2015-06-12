package com.lodogame.game.dao.impl.mysql;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserSceneDao;
import com.lodogame.game.utils.TableUtils;
import com.lodogame.model.UserScene;

public class UserSceneDaoMysqlImpl implements UserSceneDao {

	public final static String columns = " * ";

	@Autowired
	private Jdbc jdbc;

	public List<UserScene> getUserSceneList(String userId) {
		String table = TableUtils.getUserSceneTable(userId);

		String sql = "SELECT " + columns + " FROM " + table + " WHERE user_id = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);

		return this.jdbc.getList(sql, UserScene.class, parameter);
	}

	public int add(UserScene userScene) {
		String userId = userScene.getUserId();
		String table = TableUtils.getUserSceneTable(userId);
		return this.jdbc.insert(table, userScene);

	}

	@Override
	public boolean updateScenePassed(String userId, int sceneId) {
		String table = TableUtils.getUserSceneTable(userId);

		Date date = new Date();
		String sql = "update " + table + " set pass_flag = 1, updated_time = ? where user_id = ? and scene_id = ?";
		SqlParameter params = new SqlParameter();
		params.setObject(date);
		params.setString(userId);
		params.setInt(sceneId);
		return this.jdbc.update(sql, params) == 1;
	}

	@Override
	public UserScene getUserSceneBySceneId(String userId, int sceneId) {
		String table = TableUtils.getUserSceneTable(userId);

		String sql = "SELECT " + columns + " FROM " + table + " WHERE user_id = ? AND scene_id = ? LIMIT 1";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(sceneId);

		return this.jdbc.get(sql, UserScene.class, parameter);
	}

	@Override
	public boolean updateSceneOneStarReward(String userId, int sceneId, int state) {
		String table = TableUtils.getUserSceneTable(userId);

		String sql = "UPDATE " + table + " SET one_star_reward = ? WHERE user_id = ? and scene_id = ?";
		SqlParameter params = new SqlParameter();
		params.setInt(state);
		params.setString(userId);
		params.setInt(sceneId);
		return this.jdbc.update(sql, params) == 1;
	}

	@Override
	public boolean updateSceneTwoStarReward(String userId, int sceneId, int state) {
		String table = TableUtils.getUserSceneTable(userId);

		String sql = "UPDATE " + table + " SET two_star_reward = ? WHERE user_id = ? and scene_id = ?";
		SqlParameter params = new SqlParameter();
		params.setInt(state);
		params.setString(userId);
		params.setInt(sceneId);
		return this.jdbc.update(sql, params) == 1;
	}

	@Override
	public boolean updateSceneThirdStarReward(String userId, int sceneId, int state) {
		String table = TableUtils.getUserSceneTable(userId);

		String sql = "UPDATE " + table + " SET third_star_reward = ? WHERE user_id = ? and scene_id = ?";
		SqlParameter params = new SqlParameter();
		params.setInt(state);
		params.setString(userId);
		params.setInt(sceneId);
		return this.jdbc.update(sql, params) == 1;
	}

}
