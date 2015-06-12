package com.lodogame.game.dao.impl.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.UserArenaRecordLogDao;
import com.lodogame.game.dao.impl.mysql.UserArenaRecordLogDaoMysqlImpl;
import com.lodogame.model.UserArenaRecordLog;

public class UserArenaRecordLogDaoCacheImpl implements UserArenaRecordLogDao {

	private UserArenaRecordLogDaoMysqlImpl userArenaRecordLogDaoMysqlImpl;
	
	private Map<String, List<UserArenaRecordLog>> cacheMap = new ConcurrentHashMap<String, List<UserArenaRecordLog>>();
	
	public void setUserArenaRecordLogDaoMysqlImpl(
			UserArenaRecordLogDaoMysqlImpl userArenaRecordLogDaoMysqlImpl) {
		this.userArenaRecordLogDaoMysqlImpl = userArenaRecordLogDaoMysqlImpl;
	}

	@Override
	public boolean add(UserArenaRecordLog userArenaRecordLog) {
		if(cacheMap.containsKey(userArenaRecordLog.getAttackUserId())){
			List<UserArenaRecordLog> list = cacheMap.get(userArenaRecordLog.getAttackUserId());
			list.add(userArenaRecordLog);
			cacheMap.put(userArenaRecordLog.getAttackUserId(), list);
		}
		if(cacheMap.containsKey(userArenaRecordLog.getDefenseUserId())){
			List<UserArenaRecordLog> list = cacheMap.get(userArenaRecordLog.getDefenseUserId());
			cacheMap.put(userArenaRecordLog.getDefenseUserId(), list);
		}
		return this.userArenaRecordLogDaoMysqlImpl.add(userArenaRecordLog);
	}

	@Override
	public boolean isEnemy(String userId, String enemyId) {
		return this.userArenaRecordLogDaoMysqlImpl.isEnemy(userId, enemyId);
	}

	@Override
	public boolean deleteRevenge(String userId, String enemyId) {
		return this.userArenaRecordLogDaoMysqlImpl.deleteRevenge(userId, enemyId);
	}

	@Override
	public List<UserArenaRecordLog> getListByAttackUserId(String userId,
			int itemNum) {
		return this.userArenaRecordLogDaoMysqlImpl.getListByAttackUserId(userId, itemNum);
	}

	@Override
	public boolean clearRecord() {
		cacheMap.clear();
		return userArenaRecordLogDaoMysqlImpl.clearRecord();
	}

}
