package com.lodogame.ldsg.handler.event;

import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.ldsg.checker.TaskChecker;
import com.lodogame.ldsg.constants.TaskTargetType;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.FriendAddEvent;
import com.lodogame.ldsg.service.TaskService;

public class FriendAddEventHandle  extends BaseEventHandle implements EventHandle {
	 @Autowired
	 private TaskService taskService;

	public boolean handle(Event event) {
		final int size = event.getInt("size");
		taskService.updateTaskFinish(event.getUserId(), 1, new EventHandle() {
			
			@Override
			public boolean handle(Event event) {
				return false;
			}
		}, new TaskChecker() {
			
			public boolean isFinish(int systemTaskId, int taskTarget, Map<String, String> params) {
				if(taskTarget==TaskTargetType.FRIEND_NUM){
					int num = NumberUtils.toInt(params.get("fnum"));
					return num <= size;
				}
				return false;
			}
		});
		return false;
	}

	public String getInterestedEvent() {
		return FriendAddEvent.class.getSimpleName();
	}

}
