package com.lodogame.game.dao.impl.cache;

import java.util.List;

import com.lodogame.game.dao.SystemForcesMonsterDao;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.model.SystemForcesMonster;

public class SystemForcesMonsterDaoCacheImpl extends BaseSystemCacheDao<SystemForcesMonster> implements SystemForcesMonsterDao, ReloadAble {

	public List<SystemForcesMonster> getForcesMonsterList(int forcesId) {
		return super.getList(String.valueOf(forcesId));
	}
}
