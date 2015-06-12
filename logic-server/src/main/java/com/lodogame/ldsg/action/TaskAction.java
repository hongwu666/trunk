package com.lodogame.ldsg.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.UserDailyTaskDao;
import com.lodogame.game.server.response.Response;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.UserDailyTaskBO;
import com.lodogame.ldsg.bo.UserTaskBO;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.TaskUpdateEvent;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.handler.PushHandler;
import com.lodogame.ldsg.service.DailyTaskService;
import com.lodogame.ldsg.service.TaskService;

/**
 * 任务相关action
 * 
 * @author jacky
 * 
 */

public class TaskAction extends LogicRequestAction {

	private static final Logger logger = LoggerFactory.getLogger(TaskAction.class);

	@Autowired
	private PushHandler pushHandler;

	@Autowired
	private TaskService taskService;

	@Autowired
	private UserDailyTaskDao userDailyTaskDao;

	@Autowired
	private DailyTaskService dailyTaskService;

	/**
	 * 创建角色
	 * 
	 * @return
	 */
	public Response loadTasks() {

		int status = (Integer) this.request.getParameter("st");

		logger.debug("获取用户任务列表.uid[" + getUid() + "]");

		List<UserTaskBO> userTaskBOList = this.taskService.getUserTaskList(getUid(), status);
		set("tl", userTaskBOList);

		return this.render();
	}

	public Response receive() {

		logger.debug("任务提交.uid[" + getUid() + "]");

		int taskId = getInt("tid", 0);

		UserTaskBO userTaskBO = taskService.get(getUid(), taskId);

		CommonDropBO commonDropBO = this.taskService.receive(getUid(), taskId, new EventHandle() {

			public boolean handle(Event event) {

				if (event instanceof TaskUpdateEvent) {

					int systemTaskId = event.getInt("systemTaskId");
					int flag = event.getInt("flag");
					pushHandler.pushTask(getUid(), systemTaskId, flag);
				}

				return true;
			}
		});

		set("hls", commonDropBO.getUserHeroBOList());
		set("eqs", commonDropBO.getUserEquipBOList());
		set("tls", commonDropBO.getUserToolBOList());
		set("tid", taskId);
		set("tt", userTaskBO.getTaskType());

		return this.render();
	}

	public Response loadDailyTasks() {
		logger.debug("获取用户日常任务列表.uid[" + getUid() + "]");
		List<UserDailyTaskBO> list = dailyTaskService.getUserDailyTaskList(getUid());
		set("tl", list);
		return this.render();
	}

	public Response dailyReward() {

		logger.debug("任务领取.uid[" + getUid() + "]");

		int taskId = getInt("tid", 0);
		if (taskId < 1) {
			logger.info("领取任务奖励id传递为空:");
			throw new ServiceException(ServiceReturnCode.FAILD, "领取任务奖励id传递为空");
		}
		UserDailyTaskBO bo = dailyTaskService.get(getUid(), taskId);

		CommonDropBO commonDropBO = this.dailyTaskService.receive(getUid(), taskId);

		set("hls", commonDropBO.getUserHeroBOList());
		set("eqs", commonDropBO.getUserEquipBOList());
		set("tls", commonDropBO.getUserToolBOList());
		set("tid", taskId);
		set("tt", bo.getTaskType());

		return this.render();
	}
}
