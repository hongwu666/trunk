package com.lodogame.ldsg.action;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.server.response.Response;
import com.lodogame.ldsg.bo.ActivityCopyBO;
import com.lodogame.ldsg.constants.ActivityId;
import com.lodogame.ldsg.handler.PushHandler;
import com.lodogame.ldsg.service.ActivityService;
import com.lodogame.ldsg.service.BlackRoomService;

public class BlackRoomAction extends LogicRequestAction {

	@Autowired
	private BlackRoomService blackRoomService;


	@Autowired
	private ActivityService activityService;
	
	@Autowired
	private PushHandler pushHandler;

	/**
	 * 进入密室
	 * 
	 * @return
	 */
	public Response enter() {
		String userId = getUid();
		int type = getInt("tp", 0);
		ActivityCopyBO activityCopyBO = blackRoomService.enter(userId, type);
		this.pushHandler.pushUser(userId);
		set("co", activityCopyBO);
		set("act",activityService.isActivityOpen(ActivityId.MOON_DAY)?1:0);
		return this.render();

	}
}
