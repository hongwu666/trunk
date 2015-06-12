package com.lodogame.ldsg.handler.event;

import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.SystemStoneDao;
import com.lodogame.ldsg.checker.TaskChecker;
import com.lodogame.ldsg.constants.TaskTargetType;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.StoneUpgradeEvent;
import com.lodogame.ldsg.service.MessageService;
import com.lodogame.ldsg.service.TaskService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.SystemStone;
import com.lodogame.model.User;

public class StoneUpgradeEventHandle extends BaseEventHandle implements EventHandle {

	public static Logger logger = Logger.getLogger(StoneUpgradeEventHandle.class);

	@Autowired
	private UserService userService;

	@Autowired
	private SystemStoneDao systemStoneDao;

	@Autowired
	private MessageService messageService;

	@Autowired
	private TaskService taskService;

	@Override
	public boolean handle(Event event) {

		String userId = event.getUserId();
		int stoneId = event.getInt("stoneId");

		User user = this.userService.get(userId);

		final SystemStone systemStone = systemStoneDao.get(stoneId);
		if (systemStone == null) {
			logger.error("系统宝石不存在.userId[" + userId + "], stoneId[" + stoneId + "]");
			return false;
		}

		String stoneType = "";
		if (systemStone.getStoneType() == 1) {
			stoneType = "攻击";
		} else if (systemStone.getStoneType() == 2) {
			stoneType = "防御";
		} else {
			stoneType = "生命";
		}

		if (systemStone.getStoneColor() > 3) {
			this.messageService.sendStoneUpgradeMsg(userId, user.getUsername(), stoneType, systemStone.getStoneColor());
		}

		taskService.updateTaskFinish(event.getUserId(), 1, new EventHandle() {

			@Override
			public boolean handle(Event event) {
				return false;
			}
		}, new TaskChecker() {

			public boolean isFinish(int systemTaskId, int taskTarget, Map<String, String> params) {
				int need = NumberUtils.toInt(params.get("lv"));
				if (taskTarget == TaskTargetType.BAOSHI_JINJIE) {
					return need == systemStone.getStoneLevel();
				}
				return false;
			}
		});

		return true;
	}

	@Override
	public String getInterestedEvent() {
		return StoneUpgradeEvent.class.getSimpleName();
	}

}
