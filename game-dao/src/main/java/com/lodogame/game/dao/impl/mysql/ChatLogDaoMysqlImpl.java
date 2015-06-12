package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.game.dao.ChatLogDao;
import com.lodogame.model.log.ChatLog;

public class ChatLogDaoMysqlImpl implements ChatLogDao {

	@Autowired
	private Jdbc jdbcLog;

	public final static String table = "chat_log";

	@Override
	public boolean addChatLog(ChatLog chatLog) {
		return this.jdbcLog.insert(chatLog) > 0;
	}

}
