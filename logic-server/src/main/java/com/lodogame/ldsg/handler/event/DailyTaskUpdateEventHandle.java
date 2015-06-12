package com.lodogame.ldsg.handler.event;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.SystemDailyTaskDao;
import com.lodogame.ldsg.event.DailyTaskUpdateEvent;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.handler.PushHandler;
import com.lodogame.ldsg.service.DailyTaskService;

/**
*
* <br>==========================
* <br> 公司：木屋网络
* <br> 开发：onedear
* <br> 版本：1.0
* <br> 创建时间：Oct 29, 2014 2:48:57 PM
* <br>==========================
*/
public class DailyTaskUpdateEventHandle extends BaseEventHandle implements EventHandle {

	@Autowired
	private PushHandler pushHandler;

	@Autowired
	private DailyTaskService dailyTaskService;
	
	@Autowired
	private SystemDailyTaskDao systemDailyTaskDao;
	
	@Override
	public boolean handle(Event event) {
		String userId = event.getUserId();
		int taskId = event.getInt("taskId");
		int finishTimes = event.getInt("finishTimes");
		dailyTaskService.updateTask(userId, taskId, finishTimes);
		
		return true;
	}

	@Override
	public String getInterestedEvent() {
		return DailyTaskUpdateEvent.class.getSimpleName();
	}

}
