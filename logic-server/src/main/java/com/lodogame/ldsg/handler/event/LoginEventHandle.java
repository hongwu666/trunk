package com.lodogame.ldsg.handler.event;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.SystemUserLevelDao;
import com.lodogame.game.dao.UserDao;
import com.lodogame.game.dao.UserMonthlyCardDao;
import com.lodogame.ldsg.constants.ActivityTargetType;
import com.lodogame.ldsg.constants.ToolUseType;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.LoginEvent;
import com.lodogame.ldsg.service.ActivityTaskService;
import com.lodogame.ldsg.service.DailyTaskService;
import com.lodogame.ldsg.service.EventService;
import com.lodogame.ldsg.service.MessageService;
import com.lodogame.ldsg.service.PkService;
import com.lodogame.ldsg.service.TaskService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.ldsg.service.VipService;
import com.lodogame.model.SystemDailyTask;
import com.lodogame.model.SystemUserLevel;
import com.lodogame.model.User;
import com.lodogame.model.UserMonthlyCard;

public class LoginEventHandle extends BaseEventHandle implements EventHandle {

	private static final Logger logger = Logger.getLogger(LoginEventHandle.class);

	@Autowired
	private UserService userService;

	@Autowired
	private PkService pkService;

	@Autowired
	private VipService vipService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private UserDao userDao;

	@Autowired
	private SystemUserLevelDao systemUserLevelDao;

	@Autowired
	private ActivityTaskService activityTaskService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private DailyTaskService dailyTaskService;

	@Autowired
	private UserMonthlyCardDao userMonthlyCardDao;

	@Autowired
	private EventService eventService;

	@Override
	public boolean handle(Event event) {

		String userId = event.getUserId();

		logger.debug("用户登陆.userId[" + userId + "]");

		// 设置在线
		this.userDao.setOnline(userId, true);

		// 活跃度任务
		this.activityTaskService.updateActvityTask(userId, ActivityTargetType.LOGIN, 1);

		User user = this.userService.get(userId);
		SystemUserLevel systemUserLevel = this.systemUserLevelDao.get(user.getLevel());
		int maxPower = systemUserLevel.getPowerMax();

		if (user.getPower() >= maxPower) {
			this.userService.addPower(userId, 0, user.getPower(), ToolUseType.ADD_AUTO_RESUME, new Date());
		}

		this.taskService.initTask(userId);
		this.dailyTaskService.initDailyTask(userId);

		// 如果是月卡任务，判断现在时间是否在到期时间之前，如果是，将完成次数置为1，并更新状态为“完成未领取”

		UserMonthlyCard card = userMonthlyCardDao.get(userId);
		if (card != null && !card.isExpires()) {
			dailyTaskService.updateTask(userId, SystemDailyTask.LINGQUYUEKA, 1);
		}
		
		eventService.addUserPowerUpdateEvent(userId);

		return true;
	}

	@Override
	public String getInterestedEvent() {
		return LoginEvent.class.getSimpleName();
	}

}
