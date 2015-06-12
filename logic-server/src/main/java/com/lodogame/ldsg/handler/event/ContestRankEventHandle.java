package com.lodogame.ldsg.handler.event;

import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.ldsg.checker.TaskChecker;
import com.lodogame.ldsg.constants.TaskTargetType;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.ContestRankEvent;
import com.lodogame.ldsg.service.TaskService;

public class ContestRankEventHandle extends BaseEventHandle implements EventHandle {

	@Autowired
	private TaskService taskService;

	@Override
	public boolean handle(Event event) {

		final int rank = event.getInt("rank");
		taskService.updateTaskFinish(event.getUserId(), 1, new EventHandle() {

			@Override
			public boolean handle(Event event) {
				return false;
			}
		}, new TaskChecker() {

			@Override
			public boolean isFinish(int systemTaskId, int taskTarget, Map<String, String> params) {
				if (taskTarget == TaskTargetType.LEITAI_RANK) {
					int needRank = NumberUtils.toInt(params.get("ranking"));
					return rank <= needRank;
				}
				return false;
			}
		});
		return false;
	}

	@Override
	public String getInterestedEvent() {
		return ContestRankEvent.class.getSimpleName();
	}

}
