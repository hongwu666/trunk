package com.lodogame.game.dao;

import java.util.Collection;

import com.lodogame.model.ActivityTask;
import com.lodogame.model.ActivityTaskReward;

public interface SystemActivityTaskDao {

	public Collection<ActivityTaskReward> getActivityRewardList();

	public ActivityTaskReward getActivityReward(int activityTaskRewardId);

	public Collection<ActivityTask> getActivityTaskList();

	public ActivityTask getActivityTask(int activityTaskId);

	public ActivityTask getActivityTaskByTarget(int targetType);

}
