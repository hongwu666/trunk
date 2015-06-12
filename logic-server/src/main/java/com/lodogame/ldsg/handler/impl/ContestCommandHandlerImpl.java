package com.lodogame.ldsg.handler.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.CommandDao;
import com.lodogame.game.dao.UserDao;
import com.lodogame.game.utils.json.Json;
import com.lodogame.ldsg.constants.CommandType;
import com.lodogame.ldsg.handler.CommandHandler;
import com.lodogame.ldsg.handler.CommandHandlerFactory;
import com.lodogame.ldsg.handler.ContestPushHandler;
import com.lodogame.ldsg.service.ContestService;
import com.lodogame.model.Command;
import com.lodogame.model.ContestBattleReport;

public class ContestCommandHandlerImpl implements CommandHandler {

	private static final Logger logger = Logger.getLogger(ContestCommandHandlerImpl.class);

	@Autowired
	private ContestPushHandler contestPushHandler;

	@Autowired
	private ContestService contestService;

	@Autowired
	private UserDao userDao;

	@Autowired
	private CommandDao commandDao;

	@Override
	public void handle(Command command) {

		int comm = command.getCommand();
		int type = command.getType();
		Map<String, String> params = command.getParams();

		switch (comm) {
		case CommandType.COMMAND_CONTEST_PUSH_BATTLE:
			handleContestPushBattle(type, params);
			break;
		case CommandType.COMMAND_CONTEST_PUSH_STATUS:
			handleContestPushStatus(type, params);
			break;
		default:
			break;
		}
	}

	private void handleContestPushBattle(int type, Map<String, String> params) {

		String userId = params.get("userId");

		logger.debug("推送战报.userId[" + userId + "]");

		String s = params.get("report");
		ContestBattleReport report = Json.toObject(s, ContestBattleReport.class);
		this.contestPushHandler.pushBattle(userId, report);
	}

	private void handleContestPushStatus(int type, Map<String, String> params) {

		int status = Integer.parseInt(params.get("status"));

		if (type == CommandType.PUSH_ALL) {

			logger.debug("开始处理推送擂台赛状态改变消息");

			Set<String> onlineUserIdList = this.userDao.getOnlineUserIdList();
			for (String userId : onlineUserIdList) {

				Map<String, String> m = new HashMap<String, String>(1);
				m.put("userId", userId);
				m.put("status", String.valueOf(status));

				Command command = new Command();
				command.setCommand(CommandType.COMMAND_CONTEST_PUSH_STATUS);
				command.setType(CommandType.PUSH_USER);
				command.setParams(m);

				commandDao.add(command);
			}
		} else {
			String userId = params.get("userId");
			this.contestPushHandler.pushStatus(userId, status);
		}
	}

	@Override
	public void init() {
		CommandHandlerFactory.getInstance().register(5, this);
	}

}
