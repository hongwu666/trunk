/*
 * Powered By [rapid-framework]
 * Web Site: http://www.rapid-framework.org.cn
 * Google Code: http://code.google.com/p/rapid-framework/
 * Since 2008 - 2013
 */

package com.lodogame.game.dao;

import java.util.Date;

import com.lodogame.model.UserSweepInfo;


public interface UserSweepInfoDao{
	/**
	 * 增加扫荡信息，状态为0
	 * @param sweepInfo
	 * @return
	 */
	public boolean add(UserSweepInfo sweepInfo);
	
	/**
	 * 根据获取当前的sweep
	 * @param userId
	 * @return
	 */
	public UserSweepInfo getCurrentSweep(String userId);
	
	/**
	 * 更新扫荡为完成状态，状态为1
	 * @param userId
	 * @param date 
	 * @return
	 */
	public boolean updateSweepComplete(String userId, Date date);

	
}
