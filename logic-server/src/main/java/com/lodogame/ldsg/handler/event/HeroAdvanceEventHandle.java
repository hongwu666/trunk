package com.lodogame.ldsg.handler.event;

import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.ldsg.checker.TaskChecker;
import com.lodogame.ldsg.constants.ActivityTargetType;
import com.lodogame.ldsg.constants.TaskTargetType;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.HeroAdvanceEvent;
import com.lodogame.ldsg.service.ActivityTaskService;
import com.lodogame.ldsg.service.DailyTaskService;
import com.lodogame.ldsg.service.EventService;
import com.lodogame.ldsg.service.TaskService;
import com.lodogame.model.SystemDailyTask;

public class HeroAdvanceEventHandle extends BaseEventHandle implements EventHandle {

	public static Logger logger = Logger.getLogger(HeroAdvanceEventHandle.class);

	@Autowired
	private ActivityTaskService activityTaskService;

	@Autowired
	private EventService eventService;

	@Autowired
	private DailyTaskService dailyTaskService;

	@Autowired
	private TaskService taskService;

	@Override
	public boolean handle(Event event) {

		String userId = event.getUserId();
		String userHeroId = event.getString("userHeroId");
		final int heroLevel = event.getInt("heroLevel");
		int advanceTimes = event.getInt("advanceTimes");

		activityTaskService.updateActvityTask(userId, ActivityTargetType.DEVOUR_HERO, advanceTimes);

		eventService.addHeroPowerUpdateEvent(userId, userHeroId);

		dailyTaskService.sendUpdateDailyTaskEvent(userId, SystemDailyTask.WUJIANGSHENGJI, advanceTimes);

		taskService.updateTaskFinish(userId, 1, new EventHandle() {

			@Override
			public boolean handle(Event event) {
				return false;
			}
		}, new TaskChecker() {

			@Override
			public boolean isFinish(int systemTaskId, int taskTarget, Map<String, String> params) {
				if (taskTarget == TaskTargetType.HERO_LEVEL) {
					int needLevel = NumberUtils.toInt(params.get("lv"));
					boolean isFinish = heroLevel >= needLevel;
					return isFinish;
				}
				return false;
			}
		});

		return true;
	}

	@Override
	public String getInterestedEvent() {
		return HeroAdvanceEvent.class.getSimpleName();
	}
}
