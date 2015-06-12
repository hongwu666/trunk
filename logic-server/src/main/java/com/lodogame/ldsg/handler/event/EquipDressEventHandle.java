package com.lodogame.ldsg.handler.event;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.SystemEquipDao;
import com.lodogame.game.dao.UserEquipDao;
import com.lodogame.ldsg.checker.TaskChecker;
import com.lodogame.ldsg.constants.TaskTargetType;
import com.lodogame.ldsg.event.EquipDressEvent;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.service.TaskService;
import com.lodogame.model.SystemEquip;
import com.lodogame.model.UserEquip;

public class EquipDressEventHandle extends BaseEventHandle implements EventHandle {

	@Autowired
	private TaskService taskService;

	@Autowired
	private UserEquipDao userEquipDao;

	@Autowired
	private SystemEquipDao systemEquipDao;

	public boolean handle(Event event) {

		final String userId = event.getUserId();
		final String userHeroId = event.getString("userHeroId");

		taskService.updateTaskFinish(event.getUserId(), 1, new EventHandle() {

			@Override
			public boolean handle(Event event) {
				return false;
			}
		}, new TaskChecker() {

			public boolean isFinish(int systemTaskId, int taskTarget, Map<String, String> params) {
				if (taskTarget == TaskTargetType.EQUI_CHUAN) {

					int needColor = NumberUtils.toInt(params.get("color"));

					List<UserEquip> list = userEquipDao.getHeroEquipList(userId, userHeroId);		//weq ---->>>
					if (list.size() != 3) {
						return false;
					}
					for (UserEquip userEquip : list) {
						SystemEquip systemEquip = systemEquipDao.get(userEquip.getEquipId());
						if (systemEquip.getColor() != needColor) {
							return false;
						}
					}

					return true;

				}
				return false;
			}
		});
		return false;
	}

	public String getInterestedEvent() {
		return EquipDressEvent.class.getSimpleName();
	}

}
