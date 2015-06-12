package com.lodogame.ldsg.handler.event;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.UserDao;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.checker.TaskChecker;
import com.lodogame.ldsg.constants.TaskTargetType;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.UserPowerUpdateEvent;
import com.lodogame.ldsg.helper.HeroHelper;
import com.lodogame.ldsg.service.ExpeditionService;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.TaskService;
import com.lodogame.ldsg.service.UserService;

public class UserPowerUpdateEventHandle extends BaseEventHandle implements EventHandle {

	@Autowired
	private TaskService taskService;

	@Autowired
	private HeroService heroService;

	@Autowired
	private ExpeditionService expeditionService;

	@Autowired
	private UserService userService;

	@Autowired
	private UserDao userDao;

	@Override
	public boolean handle(Event event) {

		String userId = event.getUserId();

		List<UserHeroBO> userHeroBOList = heroService.getUserHeroList(userId, 1);

		final int cap = HeroHelper.getCapability(userHeroBOList);

		if (cap > 0) {
			userDao.updateUserPower(userId, cap);
		}

		taskService.updateTaskFinish(userId, 1, new EventHandle() {

			@Override
			public boolean handle(Event event) {
				return false;
			}

		}, new TaskChecker() {

			@Override
			public boolean isFinish(int systemTaskId, int taskTarget, Map<String, String> params) {

				if (taskTarget == TaskTargetType.CAPABILITY_TOTAL) {
					int needCap = NumberUtils.toInt(params.get("cap"));
					boolean isFinish = cap >= needCap;
					return isFinish;
				}
				return false;
			}
		});

		return true;
	}

	@Override
	public String getInterestedEvent() {
		return UserPowerUpdateEvent.class.getSimpleName();
	}

}
