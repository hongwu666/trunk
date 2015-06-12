package com.lodogame.game.dao.impl.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lodogame.game.dao.UserDailyTaskDao;
import com.lodogame.game.dao.clearcache.ClearCacheOnLoginOut;
import com.lodogame.game.dao.impl.mysql.UserDailyTaskDaoMysqlImpl;
import com.lodogame.model.UserDailyTask;

/**
*
* <br>==========================
* <br> 公司：木屋网络
* <br> 开发：onedear
* <br> 版本：1.0
* <br> 创建时间：Oct 29, 2014 2:50:11 PM
* <br>==========================
*/
public class UserDailyTaskDaoCacheImpl implements UserDailyTaskDao, ClearCacheOnLoginOut {

	@Autowired
	private UserDailyTaskDaoMysqlImpl userDailyTaskDaoMysqlImpl;

	private Map<String, List<UserDailyTask>> userDailyTaskMap = new ConcurrentHashMap<String, List<UserDailyTask>>();

	public void setUserDailyTaskDaoMysqlImpl(
			UserDailyTaskDaoMysqlImpl userDailyTaskDaoMysqlImpl) {
		this.userDailyTaskDaoMysqlImpl = userDailyTaskDaoMysqlImpl;
	}

	@Override
	public boolean update(String userId, int taskId, int finishTimes, int status) {
		userDailyTaskDaoMysqlImpl.update(userId, taskId, finishTimes, status);
		UserDailyTask userTask = get(userId, taskId);
		userTask.setFinishTimes(finishTimes);
		userTask.setStatus(status);
		userTask.setUpdatedTime(System.currentTimeMillis());
		return true;
	}

	@Override
	public void clearOnLoginOut(String userId) throws Exception {
		userDailyTaskMap.remove(userId);
	}

	@Override
	public List<UserDailyTask> getList(String userId) {
		if (userDailyTaskMap.containsKey(userId)) {
			return userDailyTaskMap.get(userId);
		}
		List<UserDailyTask> list = userDailyTaskDaoMysqlImpl.getList(userId);
		userDailyTaskMap.put(userId, list);
		return list;
	}

	@Override
	public void add(List<UserDailyTask> userTaskList) {
		String userId = userTaskList.get(0).getUserId();
		userDailyTaskDaoMysqlImpl.add(userTaskList);
		List<UserDailyTask> list = getList(userId);
		list.addAll(userTaskList);
	}

	@Override
	public UserDailyTask get(String userId, int taskId) {
		List<UserDailyTask> list = getList(userId);
		for (UserDailyTask userDailyTask: list) {
			if (userDailyTask.getTaskId() == taskId) {
				return userDailyTask;
			}
		}
		return null;
	}
	public void init() {

	}
}
