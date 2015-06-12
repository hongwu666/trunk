package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.UserArenaRecordLog;

public interface UserArenaRecordLogDao {
	
	public boolean add(UserArenaRecordLog userArenaRecordLog);
	
	/**
	 * 判断一个用户是不是用户的仇人
	 * @param userId
	 * @param enemyId 仇人ID
	 * @return
	 */
	public boolean isEnemy(String userId,String enemyId);
	
	/**
	 * 删除仇人
	 * @return
	 */
	public boolean deleteRevenge(String usreId,String enemyId);
	
	/**
	 * 拿挑战记录
	 * <li>以日期倒序排列</li>
	 * @param userId
	 * @param itemNum 条数
	 * @return
	 */
	public List<UserArenaRecordLog> getListByAttackUserId(String userId,int itemNum);
	
	/**
	 * 清空记录
	 * <li>一个礼拜清一次</li>
	 * @return
	 */
	public boolean clearRecord();
}
