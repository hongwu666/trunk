package com.lodogame.ldsg.handler.event;

import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.ldsg.checker.TaskChecker;
import com.lodogame.ldsg.constants.TaskTargetType;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.HeroUpgradeEvent;
import com.lodogame.ldsg.service.ActivityTaskService;
import com.lodogame.ldsg.service.DailyTaskService;
import com.lodogame.ldsg.service.EventService;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.MessageService;
import com.lodogame.ldsg.service.TaskService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.SystemHero;
import com.lodogame.model.User;
import com.lodogame.model.UserHero;

public class HeroUpgradeEventHandle extends BaseEventHandle implements EventHandle {

	public static Logger logger = Logger.getLogger(HeroUpgradeEventHandle.class);

	@Autowired
	private ActivityTaskService activityTaskService;

	@Autowired
	private HeroService heroService;

	@Autowired
	private UserService userService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private EventService eventService;

	@Autowired
	private DailyTaskService dailyTaskService;

	@Override
	public boolean handle(Event event) {

		String userId = event.getUserId();

		User user = this.userService.get(userId);

		String userHeroId = event.getString("userHeroId");

		UserHero userHero = this.heroService.get(userId, userHeroId);
		if (userHero == null) {
			return false;
		}

		SystemHero systemHero = heroService.getSysHero(userHero.getSystemHeroId());
		if (systemHero == null) {
			logger.error("系统武将不存在.userId[" + userId + "], systemHeroId[" + userHero.getSystemHeroId() + "]");
			return false;
		}

		final int color = systemHero.getHeroColor();
		if (color >= 3) {
			messageService.sendHeroUpgradeMsg(userId, user.getUsername(), systemHero.getHeroName(), color);
		}

		taskService.updateTaskFinish(userId, 1, new EventHandle() {

			@Override
			public boolean handle(Event event) {
				return false;
			}
		}, new TaskChecker() {

			@Override
			public boolean isFinish(int systemTaskId, int taskTarget, Map<String, String> params) {

				if (taskTarget == TaskTargetType.HERO_ADVANCE) {
					int needColor = NumberUtils.toInt(params.get("color"));
					boolean isFinish = color == needColor;
					return isFinish;
				}

				return false;
			}
		});

		eventService.addHeroPowerUpdateEvent(userId, userHeroId);

		return true;
	}

	@Override
	public String getInterestedEvent() {
		return HeroUpgradeEvent.class.getSimpleName();
	}

}
