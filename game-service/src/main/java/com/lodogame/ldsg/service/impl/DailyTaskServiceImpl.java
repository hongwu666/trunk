package com.lodogame.ldsg.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.CommandDao;
import com.lodogame.game.dao.PaymentLogDao;
import com.lodogame.game.dao.SystemDailyTaskDao;
import com.lodogame.game.dao.UserDailyTaskDao;
import com.lodogame.game.dao.UserForcesDao;
import com.lodogame.game.dao.UserMonthlyCardDao;
import com.lodogame.game.dao.UserTotalGainLogDao;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.DropToolBO;
import com.lodogame.ldsg.bo.UserDailyTaskBO;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.constants.ToolUseType;
import com.lodogame.ldsg.event.DailyTaskUpdateEvent;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.helper.BOHelper;
import com.lodogame.ldsg.helper.DropToolHelper;
import com.lodogame.ldsg.service.DailyTaskService;
import com.lodogame.ldsg.service.EventService;
import com.lodogame.ldsg.service.ToolService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.SystemDailyTask;
import com.lodogame.model.UserDailyTask;
import com.lodogame.model.UserMonthlyCard;

/**
 * 
 * <br>=
 * ========================= <br>
 * 公司：木屋网络 <br>
 * 开发：onedear <br>
 * 版本：1.0 <br>
 * 创建时间：Oct 29, 2014 2:48:36 PM <br>=
 * =========================
 */
public class DailyTaskServiceImpl implements DailyTaskService {

	private static final Logger logger = LoggerFactory.getLogger(DailyTaskServiceImpl.class);

	@Autowired
	private UserMonthlyCardDao userMonthlyCardDao;

	@Autowired
	private UserDailyTaskDao userDailyTaskDao;

	@Autowired
	private SystemDailyTaskDao systemDailyTaskDao;

	@Autowired
	private EventService eventService;

	@Autowired
	private UserService userService;

	@Autowired
	private ToolService toolService;

	@Autowired
	private UserForcesDao userForcesDao;

	@Autowired
	private PaymentLogDao paymentLogDao;

	@Autowired
	private UserTotalGainLogDao userTotalGainLogDao;

	@Autowired
	private CommandDao commandDao;

	public List<UserDailyTask> addNewDailyTasks(String userId) {
		Collection<SystemDailyTask> list = systemDailyTaskDao.getList();
		List<SystemDailyTask> initList = new ArrayList<SystemDailyTask>();
		for (SystemDailyTask task : list) {
			if (task.getMinLv() <= 1) {
				initList.add(task);
			}
		}
		List<UserDailyTask> dailyTaskList = new ArrayList<UserDailyTask>(initList.size());
		for (SystemDailyTask task : initList) {
			dailyTaskList.add(createUserDailyTask(userId, task));
		}
		userDailyTaskDao.add(dailyTaskList);
		return dailyTaskList;
	}

	@Override
	public List<UserDailyTaskBO> getUserDailyTaskList(String userId) {
		List<UserDailyTaskBO> userTaskBOList = new ArrayList<UserDailyTaskBO>();

		List<UserDailyTask> userTaskList = userDailyTaskDao.getList(userId);

		for (UserDailyTask userTask : userTaskList) {

			SystemDailyTask activityTask = systemDailyTaskDao.get(userTask.getTaskId());
			if (activityTask == null) {
				continue;
			}

			UserDailyTaskBO taskBO = BOHelper.createUserDailyTaskBO(activityTask, userTask);

			if (userTask.getTaskId() == SystemDailyTask.LINGQUYUEKA) {
				UserMonthlyCard card = userMonthlyCardDao.get(userId);
				if (card != null && !card.isExpires()) {
					taskBO.setTaskDesc(taskBO.getTaskDesc() + " 剩余" + DateUtils.getDayDiff(new Date(), card.getDueTime()) + "天");
				}

			}

			userTaskBOList.add(taskBO);
		}

		return userTaskBOList;
	}

	@Override
	public void initDailyTask(String userId) {
		List<UserDailyTask> userTaskList = userDailyTaskDao.getList(userId);
		if (userTaskList == null || userTaskList.size() < 1) {
			userTaskList = addNewDailyTasks(userId);
		} else {
			for (UserDailyTask task : userTaskList) {
				if (task == null) {
					continue;
				}
				refreshUserDailyTask(task);
			}
		}
	}

	@Override
	public CommonDropBO receive(String userId, int taskId) {
		UserDailyTask userTask = userDailyTaskDao.get(userId, taskId);
		SystemDailyTask activityDailyTask = systemDailyTaskDao.get(taskId);
		if (userTask == null) {
			String message = "玩家没有此日常任务.领取失败, userId[{" + userId + "}], taskId[" + taskId + "]";
			logger.debug(message);
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}
		refreshUserDailyTask(userTask);
		if (taskId == 24 || taskId == 25) {// 领取体力的任务
			if (userTask.getStatus() == UserDailyTask.STATUS_FINISH) {
				String message = "已经领取过.无法领取, userId[" + userId + "], taskId[" + taskId + "], status[" + userTask.getStatus() + "]";
				throw new ServiceException(ServiceReturnCode.FAILD, message);
			}

		} else if (userTask.getStatus() != UserDailyTask.STATUS_FINISH_NO_REWARD) {
			String message = "玩家日常任务状态.无法领取, userId[" + userId + "], taskId[" + taskId + "], status[" + userTask.getStatus() + "]";
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

		updateTaskReceived(userId, taskId);

		CommonDropBO commonDropBO = this.dropTask(userId, activityDailyTask);

		this.userService.pushUser(userId);
		return commonDropBO;
	}

	@Override
	public UserDailyTaskBO get(String userId, int taskId) {
		UserDailyTask userTask = userDailyTaskDao.get(userId, taskId);
		SystemDailyTask activityDailyTask = systemDailyTaskDao.get(taskId);
		return BOHelper.createUserDailyTaskBO(activityDailyTask, userTask);
	}

	@Override
	public void updateTask(String userId, int taskId, int incr) {
		UserDailyTask userTask = userDailyTaskDao.get(userId, taskId);
		SystemDailyTask activityDailyTask = systemDailyTaskDao.get(taskId);
		refreshUserDailyTask(userTask);
		if (userTask == null || userTask.getFinishTimes() == activityDailyTask.getTargetValue()) {
			// 次数上限. 则不做更新跟推送 --邱志明
			return;
		}
		userTask.setFinishTimes(userTask.getFinishTimes() + incr);
		if (userTask.getFinishTimes() > activityDailyTask.getTargetValue()) {
			userTask.setFinishTimes(activityDailyTask.getTargetValue());
		}
		userTask.setUpdatedTime(System.currentTimeMillis());
		refreshUserDailyTask(userTask);
		userDailyTaskDao.update(userId, taskId, userTask.getFinishTimes(), userTask.getStatus());
	}

	private void updateTaskReceived(String userId, int taskId) {
		UserDailyTask userTask = userDailyTaskDao.get(userId, taskId);
		userTask.setStatus(UserDailyTask.STATUS_FINISH);
		userDailyTaskDao.update(userId, taskId, userTask.getFinishTimes(), userTask.getStatus());
	}

	private void refreshUserDailyTask(UserDailyTask userTask) {
		if (userTask == null)
			return;
		if (!DateUtils.isSameDay(new Date(userTask.getUpdatedTime()), new Date())) {
			userTask.setUpdatedTime(System.currentTimeMillis());
			userTask.setFinishTimes(0);
			userTask.setStatus(UserDailyTask.STATUS_UNFINISH);
		}
		SystemDailyTask activityTask = systemDailyTaskDao.get(userTask.getTaskId());
		if (activityTask == null) {
			return;
		}
		if (userTask.getStatus() == UserDailyTask.STATUS_UNFINISH && userTask.getFinishTimes() >= activityTask.getTargetValue()) {
			userTask.setStatus(UserDailyTask.STATUS_FINISH_NO_REWARD);
		}

		userDailyTaskDao.update(userTask.getUserId(), userTask.getTaskId(), userTask.getFinishTimes(), userTask.getStatus());
	}

	private CommonDropBO dropTask(String userId, SystemDailyTask dailyTask) {

		CommonDropBO commonDropBO = new CommonDropBO();
		// 给道具
		String toolIds = dailyTask.getRewards();

		List<DropToolBO> dropToolBOList = DropToolHelper.parseDropTool(toolIds);
		for (DropToolBO dropToolBO : dropToolBOList) {
			int toolType = dropToolBO.getToolType();
			int toolId = dropToolBO.getToolId();
			int toolNum = dropToolBO.getToolNum();
			List<DropToolBO> dropBOList = toolService.giveTools(userId, toolType, toolId, toolNum, ToolUseType.ADD_TASK_DROP);
			for (DropToolBO dropBO : dropBOList) {
				this.toolService.appendToDropBO(userId, commonDropBO, dropBO);
			}
		}
		return commonDropBO;
	}

	/**
	 * 玩家完成一次每日任务，会调用这个方法，更新这种每日任务的完成次数
	 */
	@Override
	public void sendUpdateDailyTaskEvent(String userId, int taskId, int incr) {

		DailyTaskUpdateEvent event = new DailyTaskUpdateEvent(userId, taskId, incr);
		eventService.dispatchEvent(event);

	}

	private List<SystemDailyTask> getTaskByLv(int lv) {
		List<SystemDailyTask> rlist = new ArrayList<SystemDailyTask>();
		for (SystemDailyTask task : systemDailyTaskDao.getList()) {
			if (task.getMinLv() == lv) {
				rlist.add(task);
			}
		}
		return rlist;

	}

	private UserDailyTask createUserDailyTask(String userId, SystemDailyTask task) {
		UserDailyTask utask = new UserDailyTask();
		utask.setFinishTimes(0);
		utask.setStatus(UserDailyTask.STATUS_UNFINISH);
		utask.setTaskId(task.getId());
		utask.setTaskType(task.getType());
		utask.setUpdatedTime(System.currentTimeMillis());
		utask.setUserId(userId);
		return utask;
	}

	@Override
	public void addTaskByLv(String userId, int lv) {
		List<SystemDailyTask> list = getTaskByLv(lv);
		if (list.size() < 1) {
			return;
		}

		List<UserDailyTask> ulist = new ArrayList<UserDailyTask>();
		for (SystemDailyTask task : list) {
			UserDailyTask existsObj = userDailyTaskDao.get(userId, task.getId());
			if (existsObj != null) {
				continue;
			}
			UserDailyTask utask = createUserDailyTask(userId, task);
			ulist.add(utask);
		}
		if (ulist.size() < 1) {
			return;
		}
		userDailyTaskDao.add(ulist);
	}

}
