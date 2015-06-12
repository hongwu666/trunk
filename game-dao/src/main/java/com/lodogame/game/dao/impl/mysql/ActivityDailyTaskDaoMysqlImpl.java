package com.lodogame.game.dao.impl.mysql;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.ActivityDailyTaskDao;
import com.lodogame.model.DailyTask;

public class ActivityDailyTaskDaoMysqlImpl implements ActivityDailyTaskDao {

	@Autowired
	private Jdbc jdbc;

	String table = "activity_daily_task";

	@Override
	public Collection<DailyTask> getActivityDailyTaskList() {
		String sql = "SELECT * FROM " + table;
		return this.jdbc.getList(sql, DailyTask.class);
	}

	@Override
	public DailyTask getActivityDailyTask(int taskId) {
		String sql = "SELECT * FROM " + table + " WHERE id = ? ";
		SqlParameter sqlParameter = new SqlParameter();
		sqlParameter.setInt(taskId);
		return this.jdbc.get(sql, DailyTask.class, sqlParameter);
	}

}
