package com.lodogame.ldsg.handler.event;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.ldsg.event.DropHeroEvent;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.service.MessageService;

public class DropHeroEventHandle extends BaseEventHandle implements EventHandle {

	@Autowired
	private MessageService messageService;

	@Override
	public boolean handle(Event event) {

		String userId = event.getString("userId");
		String username = event.getString("username");
		String heroName = event.getString("heroName");
		int heroStar = event.getInt("heroStar");
		String toolName = event.getString("toolName");

		if (heroStar >= 5) {
			messageService.sendGainHeroMsg(userId, username, heroName, heroStar, toolName);
		}

		return true;
	}

	@Override
	public String getInterestedEvent() {
		return DropHeroEvent.class.getSimpleName();
	}

}
