package com.lodogame.ldsg.handler.event;

import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.ldsg.checker.TaskChecker;
import com.lodogame.ldsg.constants.TaskTargetType;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.PkRankEvent;
import com.lodogame.ldsg.service.TaskService;

public class PkRankEventHandle extends BaseEventHandle implements EventHandle {
	@Autowired
	private TaskService taskService;

	public boolean handle(Event event) {

		final int rank = event.getInt("rank");
		taskService.updateTaskFinish(event.getUserId(), 1, new EventHandle() {

			public boolean handle(Event event) {
				return false;
			}
		}, new TaskChecker() {

			public boolean isFinish(int systemTaskId, int taskTarget, Map<String, String> params) {
				if (taskTarget == TaskTargetType.LUNJIAN_RANK) {
					int r = NumberUtils.toInt(params.get("ranking"));
					return r >= rank;
				}
				return false;
			}
		});

		return false;
	}

	public String getInterestedEvent() {
		return PkRankEvent.class.getSimpleName();
	}

}
