package com.lodogame.game.dao.impl.cache;

import com.lodogame.game.dao.SystemEmpireDao;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.model.SystemEmpire;

public class SystemEmpireDaoCacheImpl extends BaseSystemCacheDao<SystemEmpire> implements SystemEmpireDao, ReloadAble {

@Override
public SystemEmpire getSystemEmpire(int floor) {
	return this.get(floor);
}
}