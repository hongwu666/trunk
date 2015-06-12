package com.lodogame.ldsg.service;

public interface VipService {

	/**
	 * 获取购买体力上限
	 * 
	 * @param vipLevel
	 * @return
	 */
	public int getBuyPowerLimit(int vipLevel);

	/**
	 * 聚宝盆购买次数
	 * 
	 * @param vipLevel
	 * @return
	 */
	public int getCopyBuyTime(int vipLevel);

	/**
	 * 重置精英副本次数
	 * 
	 * @param vipLevel
	 * @return
	 */
	public int getResetForcesTimesLimit(int vipLevel);

	/**
	 * 神密商店刷新次数
	 * 
	 * @param vipLevel
	 * @return
	 */
	public int getRefreshMysteryMallTimes(int vipLevel);

	/**
	 * 竞技场刷新次数
	 * 
	 * @param vipLevel
	 * @return
	 */
	public int getRefreshPkMallTimes(int vipLevel);

	/**
	 * 远征重置次数
	 * 
	 * @param vipLevel
	 * @return
	 */
	public int getExpeditionResetTimes(int vipLevel);

}
