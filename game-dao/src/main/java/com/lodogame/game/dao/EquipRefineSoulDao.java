package com.lodogame.game.dao;

import com.lodogame.model.EquipRefineSoul;

public interface EquipRefineSoulDao {

	EquipRefineSoul getEquipRefineSoul(String userEquipId,int equipId);
	
	boolean upEquipRefineSoul(String userEquipId,int equipId,int luck);
	
	boolean addEquipRefineSoul(EquipRefineSoul equipRefineSoul);
	
	void delEquipRefineSoul(String userEquipId,int equipId);
}
