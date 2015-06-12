package com.lodogame.ldsg.handler.event;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.UserDao;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.LogoutEvent;
import com.lodogame.ldsg.service.EventService;
import com.lodogame.ldsg.service.MessageService;
import com.lodogame.ldsg.service.UserService;

public class LogoutEventHandle extends BaseEventHandle implements EventHandle {

	private static final Logger logger = Logger.getLogger(LoginEventHandle.class);

	@Autowired
	private UserService userService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private EventService eventService;

	@Autowired
	private UserDao userDao;

	@Override
	public boolean handle(Event event) {

		String userId = event.getUserId();

		logger.debug("用户退出登陆.userId[" + userId + "]");

		// 设置在线
		this.userDao.setOnline(userId, false);

		eventService.addUserPowerUpdateEvent(userId);

		return true;
	}

	@Override
	public String getInterestedEvent() {
		return LogoutEvent.class.getSimpleName();
	}

}
