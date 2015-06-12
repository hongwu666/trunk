package com.lodogame.ldsg.handler;

import java.util.Map;

public interface OnlyOnePusHandler {

	/**
	 * 推送用户信息
	 * 
	 * @param userId
	 */
	public void pushUserInfo(String userId);

	/**
	 * 推送战报
	 * 
	 * @param userId
	 * @param params
	 */
	public void pushBattle(String userId, Map<String, String> params);

}
