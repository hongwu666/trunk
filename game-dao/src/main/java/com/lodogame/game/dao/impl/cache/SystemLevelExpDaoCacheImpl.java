package com.lodogame.game.dao.impl.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.BasePreloadAble;
import com.lodogame.game.dao.SystemLevelExpDao;
import com.lodogame.game.dao.impl.mysql.SystemLevelExpDaoMysqlImpl;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.model.SystemLevelExp;

public class SystemLevelExpDaoCacheImpl extends BasePreloadAble implements SystemLevelExpDao, ReloadAble {

	private Map<Integer, SystemLevelExp> getHeroExpCache = new ConcurrentHashMap<Integer, SystemLevelExp>();

	private SystemLevelExpDaoMysqlImpl systemLevelExpDaoMysqlImpl;

	@Override
	public SystemLevelExp getHeroLevel(int exp) {
		SystemLevelExp result = null;
		for (SystemLevelExp keyExp : getHeroExpCache.values()) {
			if (result == null) {
				if (keyExp.getExp() <= exp) {
					result = keyExp;
				}
			} else {
				if (keyExp.getExp() <= exp && keyExp.getLevel() > result.getLevel()) {
					result = keyExp;
				}
			}
		}

		return result;
	}

	@Override
	public SystemLevelExp getHeroExp(int level) {
		return getHeroExpCache.get(level);
	}

	protected void initData() {

		getHeroExpCache.clear();
		List<SystemLevelExp> list = systemLevelExpDaoMysqlImpl.getSystemLevelExpList();
		for (SystemLevelExp systemLevleExp : list) {
			getHeroExpCache.put(systemLevleExp.getLevel(), systemLevleExp);
		}
	}
	@Override
	public int getTotalExp(int level) {
		List<SystemLevelExp> list=new ArrayList<SystemLevelExp>();
		for(int lv=1;lv<=level;lv++){
			list.add(getHeroExpCache.get(lv));
		}
		int total=0;
		for (SystemLevelExp systemLevelExp:list) {
			total+=systemLevelExp.getExp();
		}
		return total;
	}
	public void setSystemLevelExpDaoMysqlImpl(SystemLevelExpDaoMysqlImpl systemLevelExpDaoMysqlImpl) {
		this.systemLevelExpDaoMysqlImpl = systemLevelExpDaoMysqlImpl;
	}

}
