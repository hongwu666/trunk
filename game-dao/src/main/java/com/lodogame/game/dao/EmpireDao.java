package com.lodogame.game.dao;

import java.util.List;
import java.util.Set;

import com.lodogame.model.Empire;
import com.lodogame.model.EmpireEnemy;
import com.lodogame.model.EmpireGain;
import com.lodogame.model.EmpireNum;

public interface EmpireDao {
	/**
	 * 获得用户帝国的次数
	 * @param empireEnemy
	 */
	public EmpireNum getEmpireNum(String userId);
	/**
	 * 更新用户帝国的次数
	 * @param empireEnemy
	 */
	public void updateEmpireNum(String userId,int num);
	/**
	 * 清空用户帝国的次数
	 * @param empireEnemy
	 */
	public void clearEmpireNum();
	
	/**
	 * 添加帝国敌人
	 * @param empireEnemy
	 */
	public void addEmpireEnemy(EmpireEnemy empireEnemy);
	/**
	 * 删除帝国敌人
	 * @param userIdA
	 * @return
	 */
	public boolean deleteEmpireEnemy(String userIdA);
	/**
	 * 查询用户的帝国敌人
	 * @param userIdA
	 * @return
	 */
	public List<String> getEmpireEnemy(String userIdA);
	/**
	 * 通过层数位置删除宝库
	 * @param floor
	 * @param pos
	 * @return
	 */
	public boolean deleteEmpire(String userId,int floor,int pos);
	/**
	 * 添加宝库
	 * @param empireList
	 * @return
	 */ 
	public boolean addEmpire(List<Empire> empireList);
	/**
	 * 通过层数位置查询宝库
	 * @param floor
	 * @param pos
	 * @return
	 */
	public List<Empire> getEmpireHero(int floor,int pos);
	/**
	 * 通过层数位置查询宝库
	 * @param floor
	 * @param pos
	 * @return
	 */
	public Empire getEmpireUser(int floor,int pos);
	/**
	 * 得到floor层数userId
	 * @param floor
	 * @param pos
	 * @return
	 */
	public Empire getEmpireUser(int floor,String userId);
	/**
	 * 得到floor层数userId
	 * @param floor
	 * @param pos
	 * @return
	 */
	public List<Empire> getEmpireHero(int floor,String userId);
	
	/**
	 * 通过层数查询宝库
	 * @param floor
	 * @param pos
	 * @return
	 */
	public List<Empire> getEmpire(int floor);
	/**
	 * 获得一层中的用户
	 * @param floor
	 * @return
	 */
	public Set<String> getEmpireUser(int floor);
	/**
	 * 查出用户占矿的信息
	 * @param userId
	 * @return
	 */
	public List<Integer> getEmpireFloor(String userId);
	/**
	 * 获得所有占领宝库用户信息
	 * @return
	 */
	public List<Empire> getEmpireAll();
	/**
	 * 更新下一次结算时间
	 * @return
	 */
	public void updateNextCount(int floor,int pos);
	
	/**
	 * 添加收益
	 * @param empireList
	 * @return
	 */ 
	public boolean addEmpireGain(List<EmpireGain> empireGainList);
	/**
	 * 查询位置收益
	 * @param empireList
	 * @return
	 */ 
	public List<EmpireGain> getEmpireGain(String userId,int floor,int pos);
	/**
	 * 通过层数位置删除宝库收益
	 * @param floor
	 * @param pos
	 * @return
	 */
	public boolean deleteEmpireGain(String userId,int floor,int pos);

}
