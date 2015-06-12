package com.lodogame.game.dao;


import java.util.List;

import com.lodogame.model.SystemRefineSoulData;

public interface SystemRefineSoulDataDao {

	List<SystemRefineSoulData> getByType(int equipType);
	
	SystemRefineSoulData getByTypeSoul(int equipType,int soulType);
}
