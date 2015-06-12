package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.SystemRefineSoulMap;

public interface SystemRefineSoulMapDao {
	
	List<SystemRefineSoulMap> getByMapId(int mapId);

}
