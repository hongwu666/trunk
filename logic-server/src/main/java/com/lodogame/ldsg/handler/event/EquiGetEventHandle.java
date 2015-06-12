package com.lodogame.ldsg.handler.event;

import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.ldsg.checker.TaskChecker;
import com.lodogame.ldsg.constants.TaskTargetType;
import com.lodogame.ldsg.event.EquiGetEvent;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.service.TaskService;

public class EquiGetEventHandle extends BaseEventHandle implements EventHandle {

	@Autowired
	private TaskService taskService;

	@Override
	public boolean handle(Event event) {
		final int colors = event.getInt("color");
		taskService.updateTaskFinish(event.getUserId(), 1, new EventHandle() {
			
			@Override
			public boolean handle(Event event) {
				// TODO Auto-generated method stub
				return false;
			}
		}, new TaskChecker() {
			
			@Override
			public boolean isFinish(int systemTaskId, int taskTarget, Map<String, String> params) {
				if(taskTarget==TaskTargetType.EQUI_GET){
					return true;
				}else if(taskTarget == TaskTargetType.EQUI_GET_COLOR){
					int color = NumberUtils.toInt(params.get("color"));
					return color==colors;
				}
				return false;
			}
		});
		return false;
	}

	@Override
	public String getInterestedEvent() {
		return EquiGetEvent.class.getSimpleName();
	}

}
