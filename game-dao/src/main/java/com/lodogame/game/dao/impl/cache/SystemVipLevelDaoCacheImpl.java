package com.lodogame.game.dao.impl.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.NotImplementedException;

import com.lodogame.game.dao.BasePreloadAble;
import com.lodogame.game.dao.SystemVipLevelDao;
import com.lodogame.game.dao.impl.mysql.SystemVipLevelDaoMysqlImpl;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.model.SystemVipLevel;

public class SystemVipLevelDaoCacheImpl extends BasePreloadAble implements SystemVipLevelDao, ReloadAble {

	private SystemVipLevelDaoMysqlImpl systemVipLevelDaoMysqlImpl;
	private Map<Integer, SystemVipLevel> getMap = new ConcurrentHashMap<Integer, SystemVipLevel>();

	@Override
	public SystemVipLevel get(int vipLevel) {
		return getMap.get(vipLevel);
	}

	@Override
	public SystemVipLevel getBuyMoney(int money) {

		SystemVipLevel result = null;

		for (SystemVipLevel systemVipLevel : getMap.values()) {
			if (result == null) {
				if (systemVipLevel.getNeedMoney() <= money) {
					result = systemVipLevel;
				}
			} else {
				if (systemVipLevel.getNeedMoney() <= money && systemVipLevel.getVipLevel() > result.getVipLevel()) {
					result = systemVipLevel;
				}
			}
		}

		return result;
	}

	@Override
	public List<SystemVipLevel> getList() {
		throw new NotImplementedException();
	}

	public void setSystemVipLevelDaoMysqlImpl(SystemVipLevelDaoMysqlImpl systemVipLevelDaoMysqlImpl) {
		this.systemVipLevelDaoMysqlImpl = systemVipLevelDaoMysqlImpl;
	}

	protected void initData() {

		getMap.clear();

		List<SystemVipLevel> list = systemVipLevelDaoMysqlImpl.getList();
		for (SystemVipLevel systemVipLevel : list) {
			getMap.put(systemVipLevel.getVipLevel(), systemVipLevel);
		}
	}

}
