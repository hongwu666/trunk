package com.lodogame.game.dao.impl.cache;

import java.util.List;

import com.lodogame.game.dao.SystemRobotHeroDao;
import com.lodogame.model.SystemRobotHero;

public class SystemRobotHeroDaoCacheImpl extends BaseSystemCacheDao<SystemRobotHero> implements SystemRobotHeroDao {

	@Override
	public List<SystemRobotHero> getList(int robotHeroId) {
		return super.getList(String.valueOf(robotHeroId));
	}

}
