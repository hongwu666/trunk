package com.lodogame.ldsg.handler.event;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.ldsg.checker.TaskChecker;
import com.lodogame.ldsg.constants.TaskTargetType;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.ContestBetEvent;
import com.lodogame.ldsg.service.TaskService;

public class ContestBetEventHandle extends BaseEventHandle implements EventHandle {

	@Autowired
	private TaskService taskService;

	@Override
	public boolean handle(Event event) {
		taskService.updateTaskFinish(event.getUserId(), 1, new EventHandle() {

			@Override
			public boolean handle(Event event) {
				return false;
			}
		}, new TaskChecker() {

			public boolean isFinish(int systemTaskId, int taskTarget, Map<String, String> params) {
				if (taskTarget == TaskTargetType.LEITA_ZHUWEI_ZHENGQUE) {
					return true;
				}
				return false;
			}
		});
		return false;
	}

	@Override
	public String getInterestedEvent() {
		return ContestBetEvent.class.getSimpleName();
	}

}
