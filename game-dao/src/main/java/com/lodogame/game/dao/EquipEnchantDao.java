package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.EquipEnchant;

public interface EquipEnchantDao {

	public List<EquipEnchant> getEquipEnchantList(String userId);

	/**
	 * 获取装备点炼属性
	 * 
	 * @param userEquipId
	 * @return
	 */
	public EquipEnchant getEquipEnchant(String userId, String userEquipId);

	/**
	 * 更新用户装备点炼属性
	 * 
	 * @param equipEnchant
	 * @return
	 */
	public boolean updateEquipEnchant(EquipEnchant equipEnchant);

	/**
	 * 修改点炼属性
	 * 
	 * @param curProperty
	 * @return
	 */
	public boolean updateEnchantProperty(String userId, String userEquipId, String curProperty);
}
