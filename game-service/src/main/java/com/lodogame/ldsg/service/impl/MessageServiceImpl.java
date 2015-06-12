package com.lodogame.ldsg.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.CommandDao;
import com.lodogame.ldsg.constants.CommandType;
import com.lodogame.ldsg.constants.MessageType;
import com.lodogame.ldsg.constants.Priority;
import com.lodogame.ldsg.helper.ColorHelper;
import com.lodogame.ldsg.service.MessageService;
import com.lodogame.ldsg.service.ToolService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.Command;

public class MessageServiceImpl implements MessageService {

	@Autowired
	private CommandDao commandDao;

	@Autowired
	private ToolService toolService;

	@Autowired
	private UserService userService;

	@Override
	public void sendGainHeroMsg(String userId, String username, String heroName, int star, String place) {

		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", userId);
		params.put("username", username);
		params.put("heroName", heroName);
		params.put("place", place);
		params.put("star", String.valueOf(star));
		params.put("msgType", String.valueOf(MessageType.MESSAGE_TYPE_GAIN_HERO));
		sendMsg(params, Priority.MESSAGE);
	}

	private void sendMsg(Map<String, String> params, int priority) {

		Command command = new Command();
		command.setCommand(CommandType.COMMAND_PUSH_MESSAGE);
		command.setType(CommandType.PUSH_ALL);
		command.setParams(params);
		command.setPriority(priority);

		commandDao.add(command);

	}

	@Override
	public void sendGainToolMsg(String userId, String username, String toolName, String place, String title) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", userId);
		params.put("username", username);
		params.put("toolName", toolName);
		params.put("title", title);
		params.put("place", place);
		params.put("msgType", String.valueOf(MessageType.MESSAGE_TYPE_GAIN_TOOL));
		sendMsg(params, Priority.MESSAGE);
	}

	@Override
	public void sendSystemMsg(String content) {
		this.sendSystemMsg(content, "all");
	}

	@Override
	public void sendVipLevelMsg(String userId, String username, int level) {

		Map<String, String> params = new HashMap<String, String>();

		params.put("userId", userId);
		params.put("username", username);
		params.put("level", String.valueOf(level));
		params.put("msgType", String.valueOf(MessageType.MESSAGE_TYPE_VIP_LEVEL));

		sendMsg(params, Priority.MESSAGE);

	}

	@Override
	public void sendOnlyoneKillMsg(String userId, String username, int count, int num) {

		Map<String, String> params = new HashMap<String, String>();

		String msgType;
		if (count == 10) {
			msgType = MessageType.MESSAGE_TYPE_ONLYONE_KILL_10;
		} else if (count == 30 || count == 50 || count == 100) {
			msgType = MessageType.MESSAGE_TYPE_ONLYONE_KILL_30;
		} else {
			msgType = MessageType.MESSAGE_TYPE_ONLYONE_KILL_5;
		}

		params.put("userId", userId);
		params.put("username", username);
		params.put("count", String.valueOf(count));
		params.put("num", String.valueOf(num));
		params.put("msgType", msgType);

		sendMsg(params, Priority.MESSAGE);

	}

	@Override
	public void sendOnlyoneStopKillMsg(String userId, String username, String usernameB, int count, int honour) {

		Map<String, String> params = new HashMap<String, String>();

		params.put("userId", userId);
		params.put("username", username);
		params.put("usernameb", usernameB);
		params.put("count", String.valueOf(count));
		params.put("honour", String.valueOf(honour));
		params.put("msgType", MessageType.MESSAGE_TYPE_ONLYONE_STOP_SKILL);

		sendMsg(params, Priority.MESSAGE);

	}

	@Override
	public void sendSystemMsg(String content, String partnerIds) {

		Map<String, String> params = new HashMap<String, String>();
		params.put("content", content);
		params.put("partnerIds", partnerIds);
		params.put("msgType", String.valueOf(MessageType.MESSAGE_TYPE_SYSTEM_MSG));
		sendMsg(params, Priority.HIGH);

	}

	@Override
	public void sendHeroUpgradeMsg(String userId, String username, String heroName, int color) {

		Map<String, String> params = new HashMap<String, String>();

		params.put("userId", userId);
		params.put("username", username);
		params.put("heroName", heroName);
		params.put("color", ColorHelper.getColorName(color));
		params.put("msgType", MessageType.MESSAGE_TYPE_HERO_UPGRADE);

		sendMsg(params, Priority.MESSAGE);

	}

	@Override
	public void sendEquipUpgradeMsg(String userId, String username, String equipName, int color) {

		Map<String, String> params = new HashMap<String, String>();

		params.put("userId", userId);
		params.put("username", username);
		params.put("equipName", equipName);
		params.put("color", ColorHelper.getColorName(color));
		params.put("msgType", MessageType.MESSAGE_TYPE_EQUIP_UPGRADE);

		sendMsg(params, Priority.MESSAGE);

	}

	@Override
	public void sendStoneUpgradeMsg(String userId, String username, String stoneType, int color) {

		Map<String, String> params = new HashMap<String, String>();

		params.put("userId", userId);
		params.put("username", username);
		params.put("stoneType", stoneType);
		params.put("color", ColorHelper.getColorName(color));
		params.put("msgType", MessageType.MESSAGE_TYPE_STONE_UPGRADE);

		sendMsg(params, Priority.MESSAGE);
	}

	@Override
	public void sendContestWinMsg(String username, String title1, String title2) {

		Map<String, String> params = new HashMap<String, String>();

		params.put("username", username);
		params.put("title1", title1);
		params.put("title2", title2);
		params.put("msgType", MessageType.MESSAGE_TYPE_CONTEST);

		sendMsg(params, Priority.MESSAGE);
	}

	public void sendTreasureMsg(String userName) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", userName);
		params.put("msgType", MessageType.MESSAGE_TYPE_TREASURE);

		sendMsg(params, Priority.MESSAGE);
	}

}
