package com.lodogame.ldsg.handler.event;

import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.ldsg.checker.TaskChecker;
import com.lodogame.ldsg.constants.TaskTargetType;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.HeroStarEvent;
import com.lodogame.ldsg.service.TaskService;

public class HeroStarEventHandle extends BaseEventHandle implements EventHandle {

	@Autowired
	private TaskService taskService;
	
	@Override
	public boolean handle(Event event) {
		String userId = event.getUserId();
		final int heroStar = event.getInt("star");
		
		taskService.updateTaskFinish(userId, 1, new EventHandle() {
			
			@Override
			public boolean handle(Event event) {
				return false;
			}
		}, new TaskChecker() {
			
			@Override
			public boolean isFinish(int systemTaskId, int taskTarget, Map<String, String> params) {
				 if (taskTarget == TaskTargetType.GAIN_HERO) {
					
					 int needStar = NumberUtils.toInt(params.get("star"));
					 boolean isFinish = (heroStar == needStar);
					 return isFinish;
				 }
				 return false;
			}
		});
		
		return true;
	}

	@Override
	public String getInterestedEvent() {
		return HeroStarEvent.class.getSimpleName();
	}

}
