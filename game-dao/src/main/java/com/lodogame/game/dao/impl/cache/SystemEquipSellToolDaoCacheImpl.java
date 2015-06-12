package com.lodogame.game.dao.impl.cache;

import com.lodogame.game.dao.SystemEquipSellToolDao;
import com.lodogame.model.SystemEquipSellTool;

public class SystemEquipSellToolDaoCacheImpl extends BaseSystemCacheDao<SystemEquipSellTool> implements SystemEquipSellToolDao {

	@Override
	public SystemEquipSellTool getByEquipColor(int equipColor) {
		return super.get(equipColor);
	}

}
