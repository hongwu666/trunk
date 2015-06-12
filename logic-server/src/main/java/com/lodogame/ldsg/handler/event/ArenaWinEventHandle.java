package com.lodogame.ldsg.handler.event;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.UserTotalGainLogDao;
import com.lodogame.ldsg.checker.TaskChecker;
import com.lodogame.ldsg.constants.TaskTargetType;
import com.lodogame.ldsg.constants.UserTotalGainLogType;
import com.lodogame.ldsg.event.ArenaWinEvent;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.service.TaskService;

public class ArenaWinEventHandle extends BaseEventHandle implements EventHandle {

	@Autowired
	private TaskService taskService;

	@Autowired
	private UserTotalGainLogDao userTotalGainLogDao;

	public boolean handle(Event event) {

		String userId = event.getUserId();

		userTotalGainLogDao.addUserTotalGain(userId, UserTotalGainLogType.AREAN_WIN, 1);

		taskService.updateTaskFinish(event.getUserId(), 1, new EventHandle() {

			public boolean handle(Event event) {
				return false;
			}
		}, new TaskChecker() {

			@Override
			public boolean isFinish(int systemTaskId, int taskTarget, Map<String, String> params) {
				if (taskTarget == TaskTargetType.ARENA_WIN) {
					return true;
				}
				return false;
			}
		});
		return false;
	}

	public String getInterestedEvent() {
		return ArenaWinEvent.class.getSimpleName();
	}
}
