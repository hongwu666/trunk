package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.UserDailyTask;

public interface UserDailyTaskDao {

	/**
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserDailyTask> getList(String userId);

	/**
	 * 更新用户任务完成次数，状态
	 * 
	 * @param userId
	 * @param systemTaskId
	 * @param finishTimes
	 * @param status
	 * @return
	 */
	public boolean update(String userId, int taskId, int finishTimes, int status);

	/**
	 * 增加任务
	 * 
	 * @param userTaskList
	 * @return
	 */
	public void add(List<UserDailyTask> userTaskList);

	/**
	 * 获取用户单个任务
	 * 
	 * @return
	 */
	public UserDailyTask get(String userId, int taskId);

}
