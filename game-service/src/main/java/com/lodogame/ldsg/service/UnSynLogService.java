package com.lodogame.ldsg.service;

/**
 * 异步存储日志service
 * 
 * @author foxwang
 * 
 */
public interface UnSynLogService {

	public void copperLog(String userId, int useType, int amount, int flag, boolean success);

	public void goldLog(String userId, int useType, int amount, int flag, boolean success, long beforeAmount, long afterAmount);

	public void toolLog(String userId, int toolType, int toolId, int num, int useType, int flag, String extinfo, boolean success);

	public void levelUpLog(String userId, int exp, int level);

	public void heroLog(String userId, String userHeroId, int systemHeroId, int useType, int flag, int heroExp, int heroLevel);

	public void chatLog(String userId, int channel, String toUserName, String content);

}
