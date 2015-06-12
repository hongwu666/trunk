package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.RefineSoulCondition;
import com.lodogame.model.SystemEquip;

public interface SystemEquipDao {

	/**
	 * 
	 * @return
	 */
	public List<SystemEquip> getList();

	/**
	 * 获取系统装备
	 * 
	 * @param equipId
	 * @return
	 */
	public SystemEquip get(int equipId);
	/**
	 * 根据缘分装备和品质获得equip
	 * @param equipId
	 * @param color
	 * @return
	 */
	public SystemEquip get(int predestinedId,int color);

	/**
	 * 获得炼魂装备列表
	 * 
	 * @return
	 */
	public List<SystemEquip> getRefineSoul(RefineSoulCondition refineSoulCondition);

}
