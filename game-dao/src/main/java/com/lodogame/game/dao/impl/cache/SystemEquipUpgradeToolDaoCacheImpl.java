package com.lodogame.game.dao.impl.cache;

import java.util.List;

import com.lodogame.game.dao.SystemEquipUpgradeToolDao;
import com.lodogame.model.SystemEquipUpgradeTool;

public class SystemEquipUpgradeToolDaoCacheImpl extends BaseSystemCacheDao<SystemEquipUpgradeTool> implements SystemEquipUpgradeToolDao {

	@Override
	public List<SystemEquipUpgradeTool> getList(int equipId) {
		return super.getList(String.valueOf(equipId));
	}

}
