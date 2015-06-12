package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserDailyTaskDao;
import com.lodogame.model.UserDailyTask;

public class UserDailyTaskDaoMysqlImpl implements UserDailyTaskDao {

	/**
	 * 字段列表
	 */
	public final static String columns = "*";
	@Autowired
	private Jdbc jdbc;

	String table = "user_daily_task";

	public boolean update(String userId, int taskId, int finishTimes, int status) {
		String sql = "UPDATE " + table + " SET finish_times = ? , status = ?, updated_time = ?  WHERE user_id = ? AND task_id = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(finishTimes);
		parameter.setInt(status);
		parameter.setLong(System.currentTimeMillis());
		parameter.setString(userId);
		parameter.setInt(taskId);

		return this.jdbc.update(sql, parameter) > 0;
	}

	public UserDailyTask get(String userId, int taskId) {
		SqlParameter parameter = new SqlParameter();

		String sql = "SELECT " + columns + " FROM " + table + " WHERE user_id = ? AND task_id = ? ";
		parameter.setString(userId);

		return this.jdbc.get(sql, UserDailyTask.class, parameter);
	}

	@Override
	public List<UserDailyTask> getList(String userId) {
		SqlParameter parameter = new SqlParameter();

		String sql = "SELECT " + columns + " FROM " + table + " WHERE user_id = ?";
		parameter.setString(userId);

		return this.jdbc.getList(sql, UserDailyTask.class, parameter);
	}

	@Override
	public void add(List<UserDailyTask> userTaskList) {
		this.jdbc.insert(table, userTaskList);

	}
}
