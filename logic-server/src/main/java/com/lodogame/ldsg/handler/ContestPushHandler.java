package com.lodogame.ldsg.handler;

import com.lodogame.model.ContestBattleReport;

public interface ContestPushHandler {

	/**
	 * 推送战斗
	 * 
	 * @param userId
	 * @param rf
	 * @param report
	 */
	public void pushBattle(String userId, ContestBattleReport report);

	/**
	 * 推送跨服战状态
	 * 
	 * @param userId
	 */
	public void pushStatus(String userId, int status);

}
