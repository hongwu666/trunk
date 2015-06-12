package com.lodogame.ldsg.handler.event;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.SystemForcesDao;
import com.lodogame.game.dao.UserForcesDao;
import com.lodogame.game.dao.UserSceneDao;
import com.lodogame.ldsg.checker.TaskChecker;
import com.lodogame.ldsg.constants.ActivityTargetType;
import com.lodogame.ldsg.constants.ForcesType;
import com.lodogame.ldsg.constants.TaskTargetType;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.ForcesEvent;
import com.lodogame.ldsg.service.ActivityTaskService;
import com.lodogame.ldsg.service.DailyTaskService;
import com.lodogame.ldsg.service.TaskService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.SystemDailyTask;
import com.lodogame.model.SystemForces;
import com.lodogame.model.UserForces;
import com.lodogame.model.UserScene;

public class ForcesEventHandle extends BaseEventHandle implements EventHandle {

	@Autowired
	private UserService userService;

	@Autowired
	private ActivityTaskService activityTaskService;

	@Autowired
	private SystemForcesDao systemForcesDao;

	@Autowired
	private TaskService taskService;

	@Autowired
	private UserSceneDao userSceneDao;

	@Autowired
	private UserForcesDao userForcesDao;

	@Autowired
	private DailyTaskService dailyTaskService;

	@Override
	public boolean handle(Event event) {

		final String userId = event.getUserId();
		int forcesId = event.getInt("forcesId");
		int times = event.getInt("times");

		final SystemForces systemForces = systemForcesDao.get(forcesId);

		if (systemForces.getForcesType() == ForcesType.FORCES_TYPE_NORMAL) {
			activityTaskService.updateActvityTask(userId, ActivityTargetType.NORMAL_FORCES, times);
		} else if (systemForces.getForcesType() == ForcesType.FORCES_TYPE_ELITE) {
			activityTaskService.updateActvityTask(userId, ActivityTargetType.ELITE_FORCES, times);
		}

		if (systemForces.isLastForceInTheGroup()) {
			dailyTaskService.sendUpdateDailyTaskEvent(userId, SystemDailyTask.RENYIFUBN, times);
			if (systemForces.getForcesType() == 2) {// 精英副本
				dailyTaskService.sendUpdateDailyTaskEvent(userId, SystemDailyTask.JINGYINGFUBEN, times);
			}
		}

		taskService.updateTaskFinish(event.getUserId(), 1, new EventHandle() {

			@Override
			public boolean handle(Event event) {
				return false;
			}
		}, new TaskChecker() {

			public boolean isFinish(int systemTaskId, int taskTarget, Map<String, String> params) {

				UserScene userScene = userSceneDao.getUserSceneBySceneId(userId, systemForces.getSceneId());

				if (userScene == null) {
					return false;
				}

				if (taskTarget == TaskTargetType.FULL_STAR_SCENE_PASS) {

					int sceneId = NumberUtils.toInt(params.get("id"));
					if (sceneId != systemForces.getSceneId()) {
						return false;
					}

					if (userScene.getPassFlag() == 0) {
						return false;
					}

					List<UserForces> list = userForcesDao.getUserForcesList(userId, systemForces.getSceneId());
					for (UserForces userForces : list) {
						if (userForces.getPassStar() != 3) {
							return false;
						}
					}

					return true;

				} else if (taskTarget == TaskTargetType.SCENE_PASS) {
					int sceneId = NumberUtils.toInt(params.get("id"));
					if (sceneId != systemForces.getSceneId()) {
						return false;
					}
					if (userScene.getPassFlag() == 1) {
						return true;
					}
				}
				return false;
			}
		});

		return true;

	}

	@Override
	public String getInterestedEvent() {
		return ForcesEvent.class.getSimpleName();
	}

}
