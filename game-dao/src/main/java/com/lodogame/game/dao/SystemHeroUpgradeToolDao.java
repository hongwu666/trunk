package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.SystemHeroUpgradeTool;

public interface SystemHeroUpgradeToolDao {

	public List<SystemHeroUpgradeTool> get(int nodeId);
}
