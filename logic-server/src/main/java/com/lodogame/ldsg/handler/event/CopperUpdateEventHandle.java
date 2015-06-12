package com.lodogame.ldsg.handler.event;

import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.ldsg.checker.TaskChecker;
import com.lodogame.ldsg.constants.TaskTargetType;
import com.lodogame.ldsg.event.CopperUpdateEvent;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.service.TaskService;

public class CopperUpdateEventHandle extends BaseEventHandle implements EventHandle {

	@Autowired
	private TaskService taskService;

	@Override
	public boolean handle(Event event) {
		String userId = event.getUserId();
		final int copperNum = event.getInt("copper");

		taskService.updateTaskFinish(userId, 1, new EventHandle() {

			@Override
			public boolean handle(Event event) {
				return false;
			}
		}, new TaskChecker() {

			@Override
			public boolean isFinish(int systemTaskId, int taskTarget, Map<String, String> params) {

				if (taskTarget == TaskTargetType.COPPER_NUM_TASK) {
					int needCopperNum = NumberUtils.toInt(params.get("copper"));
					boolean isFinish = copperNum >= needCopperNum;
					return isFinish;
				}
				return false;
			}
		});

		return true;
	}

	@Override
	public String getInterestedEvent() {
		return CopperUpdateEvent.class.getSimpleName();
	}

}
