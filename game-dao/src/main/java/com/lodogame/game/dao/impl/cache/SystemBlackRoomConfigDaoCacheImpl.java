package com.lodogame.game.dao.impl.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.SystemBlackRoomConfigDao;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.model.SystemBlackRoomConfig;

public class SystemBlackRoomConfigDaoCacheImpl implements SystemBlackRoomConfigDao, ReloadAble {

	private SystemBlackRoomConfigDao systemBlackRoomConfigDaoMysqlImpl;

	private Map<Integer, SystemBlackRoomConfig> cacheMap = new ConcurrentHashMap<Integer, SystemBlackRoomConfig>();

	public void setSystemBlackRoomConfigDaoMysqlImpl(SystemBlackRoomConfigDao systemBlackRoomConfigDaoMysqlImpl) {
		this.systemBlackRoomConfigDaoMysqlImpl = systemBlackRoomConfigDaoMysqlImpl;
	}

	@Override
	public SystemBlackRoomConfig getBlackRoomConfigByTime(int time) {
		if (!cacheMap.containsKey(time)) {
			SystemBlackRoomConfig systemBlackRoomConfig = this.systemBlackRoomConfigDaoMysqlImpl.getBlackRoomConfigByTime(time);
			if (systemBlackRoomConfig != null) {
				cacheMap.put(time, systemBlackRoomConfig);
			}
		}

		return cacheMap.get(time);
	}

	@Override
	public List<SystemBlackRoomConfig> getBlackRoomConfigByTime() {
		return this.systemBlackRoomConfigDaoMysqlImpl.getBlackRoomConfigByTime();
	}

	@Override
	public void reload() {
		cacheMap.clear();
	}

	@Override
	public void init() {
		List<SystemBlackRoomConfig> systemBlackRoomConfigList = this.systemBlackRoomConfigDaoMysqlImpl.getBlackRoomConfigByTime();
		for (SystemBlackRoomConfig systemBlackRoomConfig : systemBlackRoomConfigList) {
			cacheMap.put(systemBlackRoomConfig.getChallengeTime(), systemBlackRoomConfig);
		}

	}

}
