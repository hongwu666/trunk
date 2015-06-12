package com.lodogame.ldsg.handler.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.ldsg.constants.CommandType;
import com.lodogame.ldsg.handler.CommandHandler;
import com.lodogame.ldsg.handler.CommandHandlerFactory;
import com.lodogame.ldsg.handler.OnlyOnePusHandler;
import com.lodogame.ldsg.handler.PushHandler;
import com.lodogame.ldsg.service.OnlyOneService;
import com.lodogame.model.Command;

public class OnlyOneCommandHandlerImpl implements CommandHandler {

	@Autowired
	private OnlyOnePusHandler onlyOnePusHandler;

	@Autowired
	private OnlyOneService onlyOneService;

	@Autowired
	private PushHandler pushHandler;

	@Override
	public void handle(final Command command) {

		int comm = command.getCommand();
		int type = command.getType();
		Map<String, String> params = command.getParams();

		switch (comm) {

		case CommandType.COMMAND_ONLYONE_PUSH_USER_INFO:
			handlePushUserInfo(type, params);
			break;
		case CommandType.COMMAND_ONLYONE_PUSH_BATTLE:
			handlePushBattle(type, params);
			break;
		case CommandType.COMMAND_ONLYONE_PAUSE:
			handlePause(type, params);
			break;

		default:
			break;

		}

	}

	private void handlePushBattle(int type, Map<String, String> params) {
		String userId = params.get("userId");
		this.onlyOnePusHandler.pushBattle(userId, params);
	}

	private void handlePause(int type, Map<String, String> params) {
		String cmd = params.get("CMD");
		this.onlyOneService.execute(cmd);
	}

	private void handlePushUserInfo(int type, Map<String, String> params) {
		String userId = params.get("userId");
		this.onlyOnePusHandler.pushUserInfo(userId);
	}

	@Override
	public void init() {
		CommandHandlerFactory.getInstance().register(4, this);
	}

}
