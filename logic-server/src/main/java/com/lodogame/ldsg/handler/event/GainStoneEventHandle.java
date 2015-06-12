package com.lodogame.ldsg.handler.event;

import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.SystemStoneDao;
import com.lodogame.ldsg.checker.TaskChecker;
import com.lodogame.ldsg.constants.TaskTargetType;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.GainStoneEvent;
import com.lodogame.ldsg.service.TaskService;
import com.lodogame.model.SystemStone;

public class GainStoneEventHandle extends BaseEventHandle implements EventHandle {

	@Autowired
	private TaskService taskService;

	@Autowired
	private SystemStoneDao systemStoneDao;

	@Override
	public boolean handle(Event event) {

		int stoneId = event.getInt("stoneId");

		final SystemStone systemStone = systemStoneDao.get(stoneId);

		taskService.updateTaskFinish(event.getUserId(), 1, new EventHandle() {

			@Override
			public boolean handle(Event event) {
				return false;
			}
		}, new TaskChecker() {

			public boolean isFinish(int systemTaskId, int taskTarget, Map<String, String> params) {
				if (taskTarget == TaskTargetType.STONE_GET) {
					int lv = NumberUtils.toInt(params.get("lv"));
					return lv == systemStone.getStoneLevel();
				}
				return false;
			}
		});

		return false;
	}

	@Override
	public String getInterestedEvent() {
		return GainStoneEvent.class.getSimpleName();
	}

}
