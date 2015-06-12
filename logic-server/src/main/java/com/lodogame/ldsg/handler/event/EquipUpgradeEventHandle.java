package com.lodogame.ldsg.handler.event;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.ldsg.event.EquipUpgradeEvent;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.service.DailyTaskService;
import com.lodogame.ldsg.service.EquipService;
import com.lodogame.ldsg.service.EventService;
import com.lodogame.ldsg.service.MessageService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.SystemDailyTask;
import com.lodogame.model.SystemEquip;
import com.lodogame.model.User;
import com.lodogame.model.UserEquip;

import flex.messaging.util.StringUtils;

public class EquipUpgradeEventHandle extends BaseEventHandle implements EventHandle {

	public static Logger logger = Logger.getLogger(EquipUpgradeEventHandle.class);

	@Autowired
	private EquipService equipService;

	@Autowired
	private DailyTaskService dailyTaskService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private EventService eventService;

	@Autowired
	private MessageService messageService;

	@Override
	public boolean handle(Event event) {

		String userId = event.getUserId();
		String userEquipId = event.getString("userEquipId");

		User user = this.userService.get(userId);

		UserEquip userEquip = this.equipService.getUserEquip(userId, userEquipId);
		if (userEquip == null) {
			return false;
		}

		SystemEquip systemEquip = equipService.getSysEquip(userEquip.getEquipId());
		if (systemEquip == null) {
			logger.error("系统装备不存在.userId[" + userId + "], systemEquip[" + systemEquip + "]");
			return false;
		}

		if (systemEquip.getColor() >= 3) {
			this.messageService.sendEquipUpgradeMsg(userId, user.getUsername(), systemEquip.getEquipName(), systemEquip.getColor());
		}

		if (!StringUtils.isEmpty(userEquip.getUserHeroId())) {
			eventService.addHeroPowerUpdateEvent(userId, userEquip.getUserHeroId());
		}
		dailyTaskService.sendUpdateDailyTaskEvent(userId, SystemDailyTask.ZHUANGBEIJINJIE, 1);
		return true;
	}

	@Override
	public String getInterestedEvent() {
		return EquipUpgradeEvent.class.getSimpleName();
	}

}
