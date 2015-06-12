package com.lodogame.ldsg.handler.event;

import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.factory.EventHandleFactory;

abstract class BaseEventHandle implements EventHandle {

	abstract public String getInterestedEvent();

	public void init() {
		EventHandleFactory.getInstance().register(getInterestedEvent(), this);
	}

}
