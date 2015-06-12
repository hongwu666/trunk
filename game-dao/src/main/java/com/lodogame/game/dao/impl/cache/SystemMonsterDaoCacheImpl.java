package com.lodogame.game.dao.impl.cache;

import com.lodogame.game.dao.SystemMonsterDao;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.model.SystemMonster;

public class SystemMonsterDaoCacheImpl extends BaseSystemCacheDao<SystemMonster> implements SystemMonsterDao, ReloadAble {

	@Override
	public SystemMonster get(int monsterId) {
		return super.get(String.valueOf(monsterId));
	}

}
