package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.EmpireHistory;

public interface EmpireHistoryDao {
	/**
	 * 增加掠夺记录
	 * @param empireHistory
	 */
	public void addEmpireHistory(EmpireHistory empireHistory);
	/**
	 * 删除掠夺记录
	 * @param empireHistory
	 */
	public void deleteEmpireHistory(EmpireHistory empireHistory);
	/**
	 * 获得用户被掠夺历史记录
	 * @param empireEnemy
	 */
	public List<EmpireHistory> getEmpireHistory(String userId);
	
}
