package com.lodogame.ldsg.handler.event;

import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.ldsg.checker.TaskChecker;
import com.lodogame.ldsg.constants.TaskTargetType;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.QiangHuaEvent;
import com.lodogame.ldsg.service.TaskService;

public class QiangHuaEventHandle extends BaseEventHandle implements EventHandle {

	@Autowired
	private TaskService taskService;

	@Override
	public boolean handle(Event event) {
		final int type = event.getInt("type");
		final int level = event.getInt("level");
		taskService.updateTaskFinish(event.getUserId(), 1, new EventHandle() {

			@Override
			public boolean handle(Event event) {
				// TODO Auto-generated method stub
				return false;
			}
		}, new TaskChecker() {

			@Override
			public boolean isFinish(int systemTaskId, int taskTarget, Map<String, String> params) {
				if (taskTarget == TaskTargetType.ZHUANGBEI_QIANGHUA_WUQI && type == 1) {
					int needLevel = NumberUtils.toInt(params.get("lv"));
					return needLevel <= level;
				} else if (taskTarget == TaskTargetType.ZHUANGBEI_QIANGHUA_FANGJU && type == 2) {
					int needLevel = NumberUtils.toInt(params.get("lv"));
					return needLevel <= level;
				} else if (taskTarget == TaskTargetType.ZHUANGBEI_QIANGHUA_ZUOQI && type == 3) {
					int needLevel = NumberUtils.toInt(params.get("lv"));
					return needLevel <= level;
				}
				return false;
			}
		});
		return false;
	}

	@Override
	public String getInterestedEvent() {
		return QiangHuaEvent.class.getSimpleName();
	}

}
