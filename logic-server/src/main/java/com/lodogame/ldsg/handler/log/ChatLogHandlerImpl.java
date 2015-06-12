package com.lodogame.ldsg.handler.log;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.ChatLogDao;
import com.lodogame.ldsg.handler.LogHandle;
import com.lodogame.ldsg.handler.LogHandlerFactory;
import com.lodogame.model.log.ChatLog;
import com.lodogame.model.log.ILog;

public class ChatLogHandlerImpl implements LogHandle {

	@Autowired
	private ChatLogDao chatLogDao;

	@Override
	public void handle(ILog log) {
		ChatLog chatLog = (ChatLog) log;
		chatLogDao.addChatLog(chatLog);
	}

	@Override
	public void init() {
		LogHandlerFactory.getInstance().register(ChatLog.class, this);
	}

}
