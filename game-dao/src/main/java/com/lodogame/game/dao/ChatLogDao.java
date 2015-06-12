package com.lodogame.game.dao;

import com.lodogame.model.log.ChatLog;



public interface ChatLogDao {

	/**
	 * 增加聊天记录
	 * 
	 * @param userId
	 * @return
	 */
	public boolean addChatLog(ChatLog chatLog);

}
