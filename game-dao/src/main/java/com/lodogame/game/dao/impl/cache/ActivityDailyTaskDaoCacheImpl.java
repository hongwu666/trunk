package com.lodogame.game.dao.impl.cache;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.ActivityDailyTaskDao;
import com.lodogame.game.dao.BasePreloadAble;
import com.lodogame.game.dao.impl.mysql.ActivityDailyTaskDaoMysqlImpl;
import com.lodogame.model.DailyTask;

public class ActivityDailyTaskDaoCacheImpl extends BasePreloadAble implements ActivityDailyTaskDao {

	@Autowired
	private ActivityDailyTaskDaoMysqlImpl activityDailyTaskDaoMysqlImpl;

	private Map<Integer, DailyTask> taskCache = new TreeMap<Integer, DailyTask>();

	@PostConstruct
	protected void initData() {
		taskCache.clear();
		Collection<DailyTask> tlist = activityDailyTaskDaoMysqlImpl.getActivityDailyTaskList();
		for (DailyTask task : tlist) {
			taskCache.put(task.getId(), task);
		}
		
	}

	public void setActivityDailyTaskDaoMysqlImpl(
			ActivityDailyTaskDaoMysqlImpl activityDailyTaskDaoMysqlImpl) {
		this.activityDailyTaskDaoMysqlImpl = activityDailyTaskDaoMysqlImpl;
	}

	@Override
	public Collection<DailyTask> getActivityDailyTaskList() {
		return taskCache.values();
	}

	@Override
	public DailyTask getActivityDailyTask(int taskId) {
		return taskCache.get(taskId);
	}

}
