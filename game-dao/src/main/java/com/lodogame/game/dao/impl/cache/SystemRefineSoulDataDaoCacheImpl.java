package com.lodogame.game.dao.impl.cache;

import java.util.List;

import com.lodogame.game.dao.SystemRefineSoulDataDao;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.model.SystemRefineSoulData;

public class SystemRefineSoulDataDaoCacheImpl extends BaseSystemCacheDao<SystemRefineSoulData> implements SystemRefineSoulDataDao, ReloadAble {

	public List<SystemRefineSoulData> getByType(int equipType) {
		return super.getList(equipType + "");
	}

	public SystemRefineSoulData getByTypeSoul(int equipType, int soulType) {
		return super.get(equipType + "_" + soulType);
	}
}
