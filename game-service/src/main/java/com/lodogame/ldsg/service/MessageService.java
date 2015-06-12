package com.lodogame.ldsg.service;

public interface MessageService {

	/**
	 * 发送获得五星武将消息
	 * 
	 * @param userId
	 * @param username
	 * @param heroName
	 */

	/**
	 * 发送获取珍惜道具
	 * 
	 * @param userId
	 * @param username
	 * @param toolName
	 * @param place
	 * @param title
	 */
	public void sendGainToolMsg(String userId, String username, String toolName, String place, String title);

	public void sendGainHeroMsg(String userId, String username, String heroName, int star, String place);

	/**
	 * 发送系统消息 *
	 * 
	 * @param content
	 * @param
	 */
	public void sendSystemMsg(String content, String partnerIds);

	/**
	 * 发送系统消息 *
	 * 
	 * @param content
	 * @param
	 */
	public void sendSystemMsg(String content);

	/**
	 * vip等级
	 * 
	 * @param userId
	 * @param userName
	 * @param level
	 */
	public void sendVipLevelMsg(String userId, String username, int level);

	/**
	 * 武将进阶消息
	 * 
	 * @param userId
	 * @param username
	 * @param heroName
	 * @param color
	 */
	public void sendHeroUpgradeMsg(String userId, String username, String heroName, int color);

	/**
	 * 装备进阶消息
	 * 
	 * @param userId
	 * @param username
	 * @param equipName
	 * @param color
	 */
	public void sendEquipUpgradeMsg(String userId, String username, String equipName, int color);

	/**
	 * 宝石合成消息
	 * 
	 * @param userId
	 * @param username
	 * @param stone
	 * @param color
	 */
	public void sendStoneUpgradeMsg(String userId, String username, String stone, int color);

	/**
	 * 聚宝盆4倍广播
	 */
	public void sendTreasureMsg(String userName);

	/**
	 * 千人斩连杀消息
	 * 
	 * @param userId
	 * @param username
	 * @param count
	 */
	public void sendOnlyoneKillMsg(String userId, String username, int count, int num);

	/**
	 * 千人斩终止别人连杀消息
	 * 
	 * @param userId
	 * @param username
	 * @param usernameB
	 * @param count
	 */
	public void sendOnlyoneStopKillMsg(String userId, String username, String usernameB, int count, int honour);

	/**
	 * 跨服战胜利跑马灯
	 * 
	 * @param username
	 * @param title1
	 * @param title2
	 */
	public void sendContestWinMsg(String username, String title1, String title2);

}
