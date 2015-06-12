package com.lodogame.game.dao;

import com.lodogame.model.EnchantProperty;

public interface EnchantPropertyDao {
	EnchantProperty getEnchantProperty(int color, int type);
}
