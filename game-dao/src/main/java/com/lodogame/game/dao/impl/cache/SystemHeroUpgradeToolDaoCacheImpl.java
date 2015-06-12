package com.lodogame.game.dao.impl.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.SystemHeroUpgradeToolDao;
import com.lodogame.game.dao.impl.mysql.SystemHeroUpgradeToolDaoMysqlImpl;
import com.lodogame.game.dao.reload.ReloadAbleBase;
import com.lodogame.model.SystemHeroUpgradeTool;

public class SystemHeroUpgradeToolDaoCacheImpl extends ReloadAbleBase implements SystemHeroUpgradeToolDao {

	private SystemHeroUpgradeToolDaoMysqlImpl systemHeroUpgradeToolDaoMysqlImpl;

	/**
	 * key 是 node id
	 */
	private Map<Integer, List<SystemHeroUpgradeTool>> cache = new ConcurrentHashMap<Integer, List<SystemHeroUpgradeTool>>();

	@Override
	public List<SystemHeroUpgradeTool> get(int nodeId) {
		List<SystemHeroUpgradeTool> list = cache.get(nodeId);
		if (list == null) {
			list = systemHeroUpgradeToolDaoMysqlImpl.get(nodeId);
			if (list != null) {
				cache.put(nodeId, list);
			}
		}
		return list;
	}

	@Override
	public void reload() {
		cache.clear();
	}

	public void setSystemHeroUpgradeToolDaoMysqlImpl(SystemHeroUpgradeToolDaoMysqlImpl systemHeroUpgradeToolDaoMysqlImpl) {
		this.systemHeroUpgradeToolDaoMysqlImpl = systemHeroUpgradeToolDaoMysqlImpl;
	}
}
