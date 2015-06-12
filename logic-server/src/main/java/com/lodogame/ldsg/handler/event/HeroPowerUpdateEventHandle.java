package com.lodogame.ldsg.handler.event;

import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.UserDao;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.checker.TaskChecker;
import com.lodogame.ldsg.constants.TaskTargetType;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.HeroPowerUpdateEvent;
import com.lodogame.ldsg.helper.HeroHelper;
import com.lodogame.ldsg.service.EventService;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.TaskService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.UserHeroInfo;

public class HeroPowerUpdateEventHandle extends BaseEventHandle implements EventHandle {

	@Autowired
	private TaskService taskService;

	@Autowired
	private HeroService heroService;

	@Autowired
	private UserService userService;

	@Autowired
	private EventService eventService;

	@Autowired
	private UserDao userDao;

	@Override
	public boolean handle(Event event) {

		String userId = event.getUserId();
		String userHeroId = event.getString("userHeroId");

		UserHeroBO userHeroBO = heroService.getUserHeroBO(userId, userHeroId);

		if (userHeroBO == null) {
			// 删除该武将
			return false;
		}

		final int cap = HeroHelper.getCapability(userHeroBO);

		taskService.updateTaskFinish(userId, 1, new EventHandle() {

			@Override
			public boolean handle(Event event) {
				return false;
			}
		}, new TaskChecker() {

			@Override
			public boolean isFinish(int systemTaskId, int taskTarget, Map<String, String> params) {

				if (taskTarget == TaskTargetType.CAPABILITY_SINGLE) {
					int needCap = NumberUtils.toInt(params.get("cap"));
					boolean isFinish = cap >= needCap;
					return isFinish;
				}
				return false;
			}
		});

		if (userHeroBO.getPos() > 0) {
			eventService.addUserPowerUpdateEvent(userId);
		}

		this.saveUserHeroPower(userHeroBO);

		return true;
	}

	private void saveUserHeroPower(UserHeroBO userHeroBO) {

		UserHeroInfo userHeroInfo = new UserHeroInfo();

		int cap = HeroHelper.getCapability(userHeroBO);

		userHeroInfo.setUserHeroId(userHeroBO.getUserHeroId());
		userHeroInfo.setUserId(userHeroBO.getUserId());
		userHeroInfo.setPower(cap);
		userHeroInfo.setHeroname(userHeroBO.getName());
		userHeroInfo.setUsername(userService.get(userHeroBO.getUserId()).getUsername());
		userHeroInfo.setVipLevel(userService.get(userHeroBO.getUserId()).getVipLevel());
		userHeroInfo.setSystemHeroId(userHeroBO.getSystemHeroId());
		userDao.updateHeroAtt(userHeroInfo);

	}

	@Override
	public String getInterestedEvent() {
		return HeroPowerUpdateEvent.class.getSimpleName();
	}

}
