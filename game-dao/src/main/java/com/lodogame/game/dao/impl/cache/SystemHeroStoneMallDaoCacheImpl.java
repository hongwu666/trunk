package com.lodogame.game.dao.impl.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.SystemHeroStoneMallDao;
import com.lodogame.game.dao.impl.mysql.SystemHeroStoneMallDaoMysqlImpl;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.game.dao.reload.ReloadManager;
import com.lodogame.model.StoneMallTimeLog;
import com.lodogame.model.SystemHeroStoneMall;
import com.lodogame.model.UserHeroStoneMallLog;

public class SystemHeroStoneMallDaoCacheImpl implements SystemHeroStoneMallDao, ReloadAble {

	private List<SystemHeroStoneMall> cacheMap = new ArrayList<SystemHeroStoneMall>();

	private Map<Integer, SystemHeroStoneMall> idMap = new ConcurrentHashMap<Integer, SystemHeroStoneMall>();

	private Map<String, List<SystemHeroStoneMall>> idsMap = new ConcurrentHashMap<String, List<SystemHeroStoneMall>>();
	
 	private Map<String, UserHeroStoneMallLog> userHeroStoneMap = new ConcurrentHashMap<String, UserHeroStoneMallLog>(); 

	private SystemHeroStoneMallDaoMysqlImpl systemHeroStoneMallDaoMysqlImpl;

	public void setSystemHeroStoneMallDaoMysqlImpl(SystemHeroStoneMallDaoMysqlImpl systemHeroStoneMallDaoMysqlImpl) {
		this.systemHeroStoneMallDaoMysqlImpl = systemHeroStoneMallDaoMysqlImpl;
	}

	@Override
	public void reload() {
		cacheMap.clear();
		idMap.clear();
		idsMap.clear();
		userHeroStoneMap.clear();
	}

	@Override
	public void init() {
		cacheMap.clear();
		idMap.clear();
		idsMap.clear();
		userHeroStoneMap.clear();
		initCache();
		ReloadManager.getInstance().register(getClass().getSimpleName(), this);
	}

	private void initCache() {
		cacheMap = this.systemHeroStoneMallDaoMysqlImpl.getAllList();
	}

	@Override
	public List<SystemHeroStoneMall> getAllList() {
		if (cacheMap != null && cacheMap.size() > 0) {
			return cacheMap;
		}
		List<SystemHeroStoneMall> list = this.systemHeroStoneMallDaoMysqlImpl.getAllList();
		if (list != null && list.size() > 0) {
			cacheMap = list;
			return list;
		}
		return null;
	}

	@Override
	public UserHeroStoneMallLog getUserStoneMallLog(String userId) {
		if(userHeroStoneMap.containsKey(userId)){
			return this.userHeroStoneMap.get(userId);
		}
		UserHeroStoneMallLog userHeroStoneMallLog = this.systemHeroStoneMallDaoMysqlImpl.getUserStoneMallLog(userId);
		if(null != userHeroStoneMallLog){
			userHeroStoneMap.put(userId, userHeroStoneMallLog);
			return userHeroStoneMallLog;
		}
		return null;
	}

	@Override
	public boolean updateUserStoneMallLog(UserHeroStoneMallLog userHeroStoneMallLog) {
		boolean succ = this.systemHeroStoneMallDaoMysqlImpl.updateUserStoneMallLog(userHeroStoneMallLog);
		if(succ){
			userHeroStoneMap.put(userHeroStoneMallLog.getUserId(), userHeroStoneMallLog);
		}
		return succ;
	}

	@Override
	public SystemHeroStoneMall get(int systemId) {
		if (idMap.containsKey(systemId)) {
			return idMap.get(systemId);
		}
		SystemHeroStoneMall stoneMall = this.systemHeroStoneMallDaoMysqlImpl.get(systemId);
		if (null != stoneMall) {
			idMap.put(systemId, stoneMall);
			return stoneMall;
		}
		return null;
	}

	@Override
	public List<SystemHeroStoneMall> getListBySystemIds(String ids) {
		if (idsMap.containsKey(ids)) {
			return idsMap.get(ids);
		}

		List<SystemHeroStoneMall> list = this.systemHeroStoneMallDaoMysqlImpl.getListBySystemIds(ids);
		if (list != null && list.size() > 0) {
			idsMap.put(ids, list);
			return list;
		}
		return null;
	}

	@Override
	public StoneMallTimeLog getMallTimeLog() {
		return this.systemHeroStoneMallDaoMysqlImpl.getMallTimeLog();
	}

	@Override
	public boolean updateMallTimesLog(StoneMallTimeLog stoneMallTimeLog) {

		return this.systemHeroStoneMallDaoMysqlImpl.updateMallTimesLog(stoneMallTimeLog);
	}

	@Override
	public int getSumRate() {
		return this.systemHeroStoneMallDaoMysqlImpl.getSumRate();
	}

	@Override
	public boolean clearSystemIds() {
		boolean succ = this.systemHeroStoneMallDaoMysqlImpl.clearSystemIds();
		if(succ){
			userHeroStoneMap.clear();
		}
		return succ;
	}

}
