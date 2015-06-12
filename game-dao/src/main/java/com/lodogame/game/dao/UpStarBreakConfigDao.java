package com.lodogame.game.dao;

import com.lodogame.model.UpstarBreakConfig;

public interface UpStarBreakConfigDao {

	public UpstarBreakConfig get(int baseStar, int nextStar);
}
