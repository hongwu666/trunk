package com.lodogame.ldsg.handler.event;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.ldsg.constants.ActivityTargetType;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.TavernDrawEvent;
import com.lodogame.ldsg.service.ActivityTaskService;
import com.lodogame.ldsg.service.TavernService;

public class TavernDrawEventHandle extends BaseEventHandle implements EventHandle {

	@Autowired
	private TavernService tavernService;

	@Autowired
	private ActivityTaskService activityTaskService;

	@Override
	public boolean handle(Event event) {

		String userId = event.getUserId();
		int times = event.getInt("times");

		this.activityTaskService.updateActvityTask(userId, ActivityTargetType.COPPER_TAVERN_DRAW, times);

		return true;
	}

	@Override
	public String getInterestedEvent() {
		return TavernDrawEvent.class.getSimpleName();
	}

}
