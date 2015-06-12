package com.lodogame.ldsg.handler.event;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.UserTotalGainLogDao;
import com.lodogame.ldsg.checker.TaskChecker;
import com.lodogame.ldsg.constants.TaskTargetType;
import com.lodogame.ldsg.constants.UserTotalGainLogType;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.PraiseEvent;
import com.lodogame.ldsg.service.TaskService;

public class PraiseEventHandle extends BaseEventHandle implements EventHandle {

	@Autowired
	private TaskService taskService;

	@Autowired
	private UserTotalGainLogDao userTotalGainLogDao;

	@Override
	public boolean handle(Event event) {

		String userId = event.getUserId();
		String praisedUserId = event.getString("praisedUserId");

		userTotalGainLogDao.addUserTotalGain(userId, UserTotalGainLogType.PRAISE, 1);
		userTotalGainLogDao.addUserTotalGain(praisedUserId, UserTotalGainLogType.BE_PRAISE, 1);

		taskService.updateTaskFinish(userId, 1, new EventHandle() {

			@Override
			public boolean handle(Event event) {
				return false;
			}
		}, new TaskChecker() {

			public boolean isFinish(int systemTaskId, int taskTarget, Map<String, String> params) {
				if (taskTarget == TaskTargetType.DZ) {
					return true;
				}
				return false;
			}
		});

		taskService.updateTaskFinish(praisedUserId, 1, new EventHandle() {

			@Override
			public boolean handle(Event event) {
				return false;
			}
		}, new TaskChecker() {

			public boolean isFinish(int systemTaskId, int taskTarget, Map<String, String> params) {
				if (taskTarget == TaskTargetType.BDZ) {
					return true;
				}
				return false;
			}
		});

		return false;
	}

	@Override
	public String getInterestedEvent() {
		return PraiseEvent.class.getSimpleName();
	}

}
