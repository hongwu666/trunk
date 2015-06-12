package com.lodogame.ldsg.handler.event;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.ldsg.constants.ActivityTargetType;
import com.lodogame.ldsg.event.ArenaEvent;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.service.ActivityTaskService;
import com.lodogame.ldsg.service.MessageService;

public class ArenaEventHandle extends BaseEventHandle implements EventHandle {

	@Autowired
	private MessageService messageService;

	@Autowired
	private ActivityTaskService activityTaskService;

	@Override
	public boolean handle(Event event) {

		String userId = event.getUserId();

		activityTaskService.updateActvityTask(userId, ActivityTargetType.ARENA, 1);

		return true;
	}

	@Override
	public String getInterestedEvent() {
		return ArenaEvent.class.getSimpleName();
	}

}
