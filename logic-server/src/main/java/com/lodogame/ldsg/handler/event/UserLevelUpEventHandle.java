package com.lodogame.ldsg.handler.event;

import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.UserPkInfoDao;
import com.lodogame.ldsg.checker.TaskChecker;
import com.lodogame.ldsg.constants.TaskTargetType;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.UserLevelUpEvent;
import com.lodogame.ldsg.service.DailyTaskService;
import com.lodogame.ldsg.service.TaskService;
import com.lodogame.model.UserPkInfo;

public class UserLevelUpEventHandle extends BaseEventHandle implements EventHandle {

	@Autowired
	private UserPkInfoDao userPkInfoDao;

	@Autowired
	private TaskService taskService;

	@Autowired
	private DailyTaskService dailyTaskService;
	
	@Override
	public boolean handle(Event event) {

		updatePkLevel(event);
		updateUserLevelTask(event);

		return true;
	}

	/**
	 * 更新用户等级任务
	 */
	private void updateUserLevelTask(Event event) {
		String userId = event.getUserId();
		final int userLevel = event.getInt("userLevel");

		this.taskService.updateTaskFinish(userId, 1, new EventHandle() {
			@Override
			public boolean handle(Event event) {
				return false;
			}
		}, new TaskChecker() {

			@Override
			public boolean isFinish(int systemTaskId, int taskTarget, Map<String, String> params) {
				if (taskTarget == TaskTargetType.USER_LEVEL_TASK) {

					int needLevel = NumberUtils.toInt(params.get("lv"));
					boolean isFinish = userLevel >= needLevel;
					return isFinish;
				}

				return false;
			}

		});
		dailyTaskService.addTaskByLv(userId, userLevel);
	}

	/**
	 * 更新论剑的等级
	 */
	private void updatePkLevel(Event event) {
		String userId = event.getUserId();
		int level = event.getInt("userLevel");
		UserPkInfo userPkInfo = userPkInfoDao.getByUserId(userId);
		if (userPkInfo != null) {
			userPkInfo.setLevel(level);
			userPkInfoDao.update(userPkInfo, null);
		}
	}

	@Override
	public String getInterestedEvent() {
		return UserLevelUpEvent.class.getSimpleName();
	}
}
