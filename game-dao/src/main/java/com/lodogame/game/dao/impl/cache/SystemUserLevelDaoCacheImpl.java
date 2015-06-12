package com.lodogame.game.dao.impl.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.BasePreloadAble;
import com.lodogame.game.dao.SystemUserLevelDao;
import com.lodogame.game.dao.impl.mysql.SystemUserLevelDaoMysqlImpl;
import com.lodogame.model.SystemUserLevel;

public class SystemUserLevelDaoCacheImpl extends BasePreloadAble implements SystemUserLevelDao {

	@Autowired
	private SystemUserLevelDaoMysqlImpl systemUserLevelDaoMysqlImpl;
	private Map<Integer, SystemUserLevel> getMap = new ConcurrentHashMap<Integer, SystemUserLevel>();

	public SystemUserLevel getUserLevel(long exp) {
		SystemUserLevel result = null;
		for (SystemUserLevel systemUserLevel : getMap.values()) {
			if (result == null) {
				if (systemUserLevel.getExp() <= exp) {
					result = systemUserLevel;
				}
			} else {
				if (systemUserLevel.getExp() <= exp && systemUserLevel.getUserLevel() > result.getUserLevel()) {
					result = systemUserLevel;
				}
			}
		}

		return result;
	}

	public SystemUserLevel get(int level) {
		return getMap.get(level);
	}

	public void add(SystemUserLevel systemUserLevel) {
		throw new NotImplementedException();
	}

	protected void initData() {
		getMap.clear();
		List<SystemUserLevel> list = systemUserLevelDaoMysqlImpl.getAll();
		for (SystemUserLevel systemUserLevel : list) {
			getMap.put(systemUserLevel.getUserLevel(), systemUserLevel);
		}

	}

}
