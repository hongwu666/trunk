package com.lodogame.game.dao.impl.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.SystemArenaGiftDao;
import com.lodogame.game.dao.impl.mysql.SystemArenaGiftDaoMysqlImpl;
import com.lodogame.model.SystemArenaGift;

public class SystemArenaGiftDaoCacheImpl implements SystemArenaGiftDao {

	private SystemArenaGiftDaoMysqlImpl systemArenaGiftDaoMysqlImpl;
	
	private Map<String, SystemArenaGift> cacheMap = new ConcurrentHashMap<String, SystemArenaGift>();
	
	private Map<Integer, List<SystemArenaGift>> listMap = new ConcurrentHashMap<Integer, List<SystemArenaGift>>();
	
	public void setSystemArenaGiftDaoMysqlImpl(
			SystemArenaGiftDaoMysqlImpl systemArenaGiftDaoMysqlImpl) {
		this.systemArenaGiftDaoMysqlImpl = systemArenaGiftDaoMysqlImpl;
	}

	@Override
	public SystemArenaGift get(int groupId, int rank) {
		String key = groupId + "-" + rank;
		if(cacheMap.containsKey(key)){
			return cacheMap.get(key);
		}
		SystemArenaGift systemArenaGift = this.systemArenaGiftDaoMysqlImpl.get(groupId, rank);
		if(null != systemArenaGift){
			cacheMap.put(key, systemArenaGift);
			return systemArenaGift;
		}
		return null;
	}

	@Override
	public List<SystemArenaGift> getList(int groupId) {
		if(listMap.containsKey(groupId)){
			return listMap.get(groupId);
		}
		List<SystemArenaGift> list = this.systemArenaGiftDaoMysqlImpl.getList(groupId);
		if(null != list && list.size() > 0 ){
			listMap.put(groupId, list);
			return list;
		}
		return null;
	}

	@Override
	public int getGroupItemCount() {
		return this.systemArenaGiftDaoMysqlImpl.getGroupItemCount();
	}

}
