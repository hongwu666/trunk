package com.lodogame.ldsg.handler.event;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.UserPkInfoDao;
import com.lodogame.ldsg.constants.ActivityTargetType;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.PkFightEvent;
import com.lodogame.ldsg.service.ActivityTaskService;
import com.lodogame.ldsg.service.MessageService;
import com.lodogame.ldsg.service.PkService;
import com.lodogame.ldsg.service.UserService;

public class PkFightEventHandle extends BaseEventHandle implements EventHandle {

	@Autowired
	private MessageService messageService;

	@Autowired
	private PkService pkService;

	@Autowired
	private UserService userService;

	@Autowired
	private ActivityTaskService activityTaskService;

	@Autowired
	private UserPkInfoDao userPkInfoDao;

	@Override
	public boolean handle(Event event) {

		String userId = event.getUserId();

		this.activityTaskService.updateActvityTask(userId, ActivityTargetType.PK, 1);

		return true;
	}

	@Override
	public String getInterestedEvent() {
		return PkFightEvent.class.getSimpleName();
	}

}
