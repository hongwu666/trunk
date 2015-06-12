package com.lodogame.game.dao.impl.cache;

import java.util.List;

import com.lodogame.game.dao.SystemHeroDao;
import com.lodogame.model.SystemHero;

public class SystemHeroDaoCacheImpl extends BaseSystemCacheDao<SystemHero> implements SystemHeroDao {

	public SystemHero get(Integer systemHeroId) {
		return super.get(String.valueOf(systemHeroId));
	}

	@Override
	public int getSystemHeroId(Integer heroId, Integer heroColor) {

		List<SystemHero> list = super.getList(String.valueOf(heroId));

		for (SystemHero hero : list) {
			if (hero.getHeroColor() == heroColor) {
				return hero.getSystemHeroId();
			}
		}

		return 0;
	}

}
