package com.lodogame.ldsg.handler.event;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.TreasureEvent;
import com.lodogame.ldsg.service.MessageService;

public class TreasureEventHandler  extends BaseEventHandle implements EventHandle {

	@Autowired
	private MessageService messageService;
	
	public boolean handle(Event event) {
		String name = event.getString("name");
		messageService.sendTreasureMsg(name);
		return true;
	}

	@Override
	public String getInterestedEvent() {
		return TreasureEvent.class.getSimpleName();
	}
	
}
