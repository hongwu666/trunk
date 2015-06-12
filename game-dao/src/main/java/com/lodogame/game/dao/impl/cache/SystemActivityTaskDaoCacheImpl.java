package com.lodogame.game.dao.impl.cache;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.lodogame.game.dao.BasePreloadAble;
import com.lodogame.game.dao.SystemActivityTaskDao;
import com.lodogame.game.dao.impl.mysql.SystemActivityTaskDaoMysqlImpl;
import com.lodogame.model.ActivityTask;
import com.lodogame.model.ActivityTaskReward;

public class SystemActivityTaskDaoCacheImpl extends BasePreloadAble implements SystemActivityTaskDao {

	private SystemActivityTaskDaoMysqlImpl systemActivityTaskDaoMysqlImpl;

	private Map<Integer, ActivityTaskReward> rewardCache = new TreeMap<Integer, ActivityTaskReward>();

	private Map<Integer, ActivityTask> taskCache = new TreeMap<Integer, ActivityTask>();

	@Override
	public Collection<ActivityTaskReward> getActivityRewardList() {
		return rewardCache.values();
	}

	@Override
	public ActivityTaskReward getActivityReward(int activityTaskRewardId) {
		return rewardCache.get(activityTaskRewardId);
	}

	@Override
	public Collection<ActivityTask> getActivityTaskList() {
		return taskCache.values();
	}

	@Override
	public ActivityTask getActivityTask(int activityTaskId) {
		return taskCache.get(activityTaskId);
	}

	@Override
	public ActivityTask getActivityTaskByTarget(int targetType) {

		for (ActivityTask activityTask : taskCache.values()) {
			if (activityTask.getTargetType() == targetType) {
				return activityTask;
			}
		}
		return null;
	}

	public void setSystemActivityTaskDaoMysqlImpl(SystemActivityTaskDaoMysqlImpl systemActivityTaskDaoMysqlImpl) {
		this.systemActivityTaskDaoMysqlImpl = systemActivityTaskDaoMysqlImpl;
	}

	@Override
	protected void initData() {

		rewardCache.clear();
		taskCache.clear();

		List<ActivityTask> tlist = this.systemActivityTaskDaoMysqlImpl.getActivityTaskList();
		for (ActivityTask activityTask : tlist) {
			taskCache.put(activityTask.getActivityTaskId(), activityTask);
		}

		List<ActivityTaskReward> rlist = this.systemActivityTaskDaoMysqlImpl.getActivityRewardList();
		for (ActivityTaskReward activityTaskReward : rlist) {
			rewardCache.put(activityTaskReward.getActivityTaskRewardId(), activityTaskReward);
		}
	}
}
