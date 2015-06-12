package com.lodogame.ldsg.handler.event;

import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.ldsg.checker.TaskChecker;
import com.lodogame.ldsg.constants.TaskTargetType;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.MuhonUpdateEvent;
import com.lodogame.ldsg.service.TaskService;

public class MuhonUpdateEventHandle extends BaseEventHandle implements EventHandle {

	@Autowired
	private TaskService taskService;
	
	@Override
	public boolean handle(Event event) {
		String userId = event.getUserId();
		final int muhonNum = event.getInt("muhon");
		
		taskService.updateTaskFinish(userId, 1 , new EventHandle() {
			
			@Override
			public boolean handle(Event event) {
				return false;
			}
		}, new TaskChecker() {
			
			@Override
			public boolean isFinish(int systemTaskId, int taskTarget, Map<String, String> params) {
				if (taskTarget == TaskTargetType.MUHON_NUM_TASK) {
					int needNum = NumberUtils.toInt(params.get("muhon"));
					boolean isFinish = muhonNum >= needNum;
					return isFinish;
				}
				return false;
			}
		});
		
		return true;
	}

	@Override
	public String getInterestedEvent() {
		return MuhonUpdateEvent.class.getSimpleName();
	}

}
