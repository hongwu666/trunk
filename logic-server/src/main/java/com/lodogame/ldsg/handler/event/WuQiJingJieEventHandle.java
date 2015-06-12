package com.lodogame.ldsg.handler.event;

import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.ldsg.checker.TaskChecker;
import com.lodogame.ldsg.constants.TaskTargetType;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.WuQiJingJieEvent;
import com.lodogame.ldsg.service.TaskService;

public class WuQiJingJieEventHandle extends BaseEventHandle implements EventHandle {
	@Autowired
	private TaskService taskService;

	@Override
	public boolean handle(Event event) {
		final int color = event.getInt("color");
		final int type = event.getInt("type");
		taskService.updateTaskFinish(event.getUserId(), 1, new EventHandle() {

			@Override
			public boolean handle(Event event) {
				return false;
			}
		}, new TaskChecker() {

			public boolean isFinish(int systemTaskId, int taskTarget, Map<String, String> params) {
				int needColor = NumberUtils.toInt(params.get("color"));
				if (taskTarget == TaskTargetType.ZHUANGBEI_JINJIE_WUQI && type == 1) {
					return needColor <= color;
				} else if (taskTarget == TaskTargetType.ZHUANGBEI_JINJIE_FANGJU && type == 2) {
					return needColor <= color;
				} else if (taskTarget == TaskTargetType.ZHUANGBEI_JINJIE_ZUOQI && type == 3) {
					return needColor <= color;
				}
				return false;
			}
		});
		return false;
	}

	@Override
	public String getInterestedEvent() {
		return WuQiJingJieEvent.class.getSimpleName();
	}

}
