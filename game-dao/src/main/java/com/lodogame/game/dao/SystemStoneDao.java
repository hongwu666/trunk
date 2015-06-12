package com.lodogame.game.dao;

import com.lodogame.model.SystemStone;

public interface SystemStoneDao {

	public SystemStone get(int stoneId);

	public SystemStone getRandomStone(int level);
}
