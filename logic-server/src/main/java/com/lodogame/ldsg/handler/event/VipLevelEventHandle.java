package com.lodogame.ldsg.handler.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.ldsg.checker.TaskChecker;
import com.lodogame.ldsg.constants.TaskTargetType;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.VipLevelEvent;
import com.lodogame.ldsg.service.MessageService;
import com.lodogame.ldsg.service.TaskService;

/**
 * 
 * @author Candon
 * 
 */
public class VipLevelEventHandle extends BaseEventHandle implements EventHandle {

	@Autowired
	private TaskService taskService;

	@Autowired
	private MessageService messageService;

	@Override
	public boolean handle(Event event) {
		String userId = event.getUserId();
		final int vipLevel = event.getInt("vipLevel");

		final List<Integer> finishTaskIds = new ArrayList<Integer>();
		taskService.updateTaskFinish(userId, 1, new EventHandle() {

			@Override
			public boolean handle(Event event) {
				return false;
			}
		}, new TaskChecker() {

			@Override
			public boolean isFinish(int systemTaskId, int taskTarget, Map<String, String> params) {
				if (taskTarget == TaskTargetType.VIP_LEVEL_TASK) {
					int needVipLevel = NumberUtils.toInt(params.get("vl"));
					boolean isFinish = vipLevel >= needVipLevel;

					if (isFinish) {
						if (finishTaskIds.contains(systemTaskId)) {
							return false;
						} else {
							finishTaskIds.add(systemTaskId);
						}
					}
					return isFinish;
				}

				if (taskTarget == TaskTargetType.PAYMENT) {
					return true;
				}

				return false;
			}
		});

		return true;
	}

	@Override
	public String getInterestedEvent() {
		return VipLevelEvent.class.getSimpleName();
	}

}
