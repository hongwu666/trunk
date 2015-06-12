package com.lodogame.ldsg.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.CommandDao;
import com.lodogame.game.utils.Constant;
import com.lodogame.game.utils.IllegalWordUtills;
import com.lodogame.ldsg.constants.CommandType;
import com.lodogame.ldsg.constants.InitDefine;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.service.ChatService;
import com.lodogame.ldsg.service.UnSynLogService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.Command;
import com.lodogame.model.User;

public class ChatServiceImpl implements ChatService {

	private static final Logger logger = Logger.getLogger(ChatServiceImpl.class);

	private Map<String, Long> lastChatMap = new ConcurrentHashMap<String, Long>();

	@Autowired
	private UserService userService;

	@Autowired
	private CommandDao commandDao;

	@Autowired
	private UnSynLogService unSynLogService;

	@Override
	public String sendMessage(String userId, int channel, String toUserName, String content) {

		if (channel == 2) {
			// 私聊的对象是否正确
			chenckToUserId(toUserName);
		} /*
		 * else { User user = this.userService.get(userId); if
		 * (user.getVipLevel() < 1) { throw new
		 * ServiceException(VIP_LEVEL_NOT_ENOUGH, "世界聊天需要vip等级1开放.userId[" +
		 * userId + "]"); } }
		 */

		long now = System.currentTimeMillis();
		Long lastChat = lastChatMap.get(userId);
		if (lastChat != null) {
			long diff = now - lastChat;
			logger.debug("diff:[" + diff + "]");
			if (diff <= 20 * 1000) {
				throw new ServiceException(CHAT_SEND_TOOL_FAST, "说话太快了.userId[" + userId + "]");
			}
		}

		// 发送的消息是否正确
		checkMessageIsRight(content);
		lastChatMap.put(userId, now);

		// 检查发送消息的用户是否合法
		chenckUser(userId);
		String toUserId = null;
		// 发送消息
		if (channel == 1) {
			this.pushAllUserMessage(userId, content);
		} else {
			toUserId = this.pushUserMessage(userId, toUserName, content);
		}

		// 保存日志
		unSynLogService.chatLog(userId, channel, toUserName, content);

		return toUserId;
	}

	// 推送私聊消息
	public String pushUserMessage(String userId, String toUserName, String content) {

		User toUser = this.userService.getUserByUserName(toUserName);

		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", toUser.getUserId());
		params.put("content", content);
		params.put("uid", userId);

		Command command = new Command();
		command.setCommand(CommandType.COMMAND_CHAT_PLIVATE);
		command.setType(CommandType.PUSH_USER);
		command.setParams(params);

		commandDao.add(command);
		return toUser.getUserId();
	}

	// 推送世界消息
	public void pushAllUserMessage(String userId, String content) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", userId);
		params.put("content", content);

		Command command = new Command();
		command.setCommand(CommandType.COMMAND_CHAT_ALL);
		command.setType(CommandType.PUSH_ALL);
		command.setParams(params);

		commandDao.add(command);
	}

	private void chenckUser(String userId) {
		// 是否被禁言
		User user = this.userService.get(userId);
		if (user.getBannedChatTime() != null) {
			Date now = new Date();// 当前时间
			long timestamp1 = now.getTime();
			Date timestamp2 = user.getBannedChatTime();
			long sub = timestamp1 - timestamp2.getTime();
			if (sub < InitDefine.BANNED_TO_POST_TIME_INTERVAL) {
				throw new ServiceException(CHAT_USER_IS_BANNED, "发送消息失败,该用户正在禁言期间内");
			}
		}
	}

	private void checkMessageIsRight(String content) {

		if (content.length() == 0) {
			throw new ServiceException(CHAT_MESSAGE_IS_NULL, "消息长度非法.content[" + content + "]");
		} else if (Constant.getBytes(content) > 50) {
			throw new ServiceException(CHAT_MESSAGE_TOO_LONG, "消息长度非法.content[" + content + "]");
		} else if (IllegalWordUtills.hasIllegalWords(content)) {
			throw new ServiceException(CHAT_HAS_CONTAIN_ILLEGAL_WORDS, "消息包含非法文字.username[" + content + "]");
		}

	}

	public void chenckToUserId(String toUserName) {

		User user = this.userService.getUserByUserName(toUserName);

		if (user == null) {
			throw new ServiceException(CHAT_TO_USER_NOT_EXIST, "发送消息失败，该用户不存在.toUserName[" + toUserName + "]");
		}

		if (!this.userService.isOnline(user.getUserId())) {
			throw new ServiceException(CHAT_TO_USER_NOT_ONLINE, "发送消息失败，该用户在线.toUserName[" + toUserName + "]");
		}

	}

	@Override
	public void bannedToPost(String userId) {

		this.userService.bannedToPost(userId);

	}

}
