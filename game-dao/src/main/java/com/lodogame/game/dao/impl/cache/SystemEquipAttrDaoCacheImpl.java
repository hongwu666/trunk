package com.lodogame.game.dao.impl.cache;

import com.lodogame.game.dao.SystemEquipAttrDao;
import com.lodogame.model.SystemEquipAttr;

public class SystemEquipAttrDaoCacheImpl extends BaseSystemCacheDao<SystemEquipAttr> implements SystemEquipAttrDao {

	@Override
	public SystemEquipAttr getEquipAttr(int equipId, int equipLevel) {
		return super.get(equipId + "_" + equipLevel);
	}

}
