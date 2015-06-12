package com.lodogame.game.dao.impl.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.GemAltarDao;
import com.lodogame.game.dao.clearcache.ClearCacheOnLoginOut;
import com.lodogame.game.dao.impl.mysql.GemAltarDaoMySqlImpl;
import com.lodogame.model.GemAltarGroupsOpenConfig;
import com.lodogame.model.GemAltarOpenConfig;
import com.lodogame.model.GemAltarUserAutoSell;
import com.lodogame.model.GemAltarUserInfo;
import com.lodogame.model.GemAltarUserOpen;
import com.lodogame.model.GemAltarUserPack;
import com.lodogame.model.log.GemAltarUserSellLog;

public class GemAltarDaoCacheImpl implements GemAltarDao,ClearCacheOnLoginOut{
	
	private GemAltarDaoMySqlImpl gemAltarDaoMysqlImpl;


	

	public void setGemAltarDaoMysqlImpl(GemAltarDaoMySqlImpl gemAltarDaoMysqlImpl) {
		this.gemAltarDaoMysqlImpl = gemAltarDaoMysqlImpl;
	}

	private Map<String, GemAltarUserInfo> userInfo = new ConcurrentHashMap<String, GemAltarUserInfo>();
	private Map<Integer, GemAltarGroupsOpenConfig> GROUPS_CONFIG = new HashMap<Integer, GemAltarGroupsOpenConfig>();
	private Map<Integer, List<GemAltarOpenConfig>> OPEN_CONFIG = new HashMap<Integer, List<GemAltarOpenConfig>>();	//宝石奖励，阶段-宝石机会
	
	public void init(){
		List<GemAltarOpenConfig> open = gemAltarDaoMysqlImpl.getOpenConfigs();
		List<GemAltarGroupsOpenConfig> group = gemAltarDaoMysqlImpl.getGroupsOpenConfigs();
		
		for(GemAltarGroupsOpenConfig temp : group){
			GROUPS_CONFIG.put(temp.getGroups(), temp);
		}
		
		for(GemAltarOpenConfig temp : open){
			if(!OPEN_CONFIG.containsKey(temp.getGroups())){
				OPEN_CONFIG.put(temp.getGroups(), new ArrayList<GemAltarOpenConfig>());
			}
			OPEN_CONFIG.get(temp.getGroups()).add(temp);
		}
	}

	public GemAltarUserInfo getUserInfo(String userId) {
		GemAltarUserInfo info = userInfo.get(userId);
		if(info == null){
			List<GemAltarUserOpen> open = gemAltarDaoMysqlImpl.getUserOpen(userId);
			List<GemAltarUserPack> pack = gemAltarDaoMysqlImpl.getUserPack(userId);
			List<GemAltarUserAutoSell> auto = gemAltarDaoMysqlImpl.getAuto(userId);
			List<GemAltarUserSellLog> logs = gemAltarDaoMysqlImpl.getUserLog(userId);
			info = new GemAltarUserInfo(pack,open,auto,logs);
			userInfo.put(userId, info);
		}
		return info;
	}
	
	
	
	public List<GemAltarGroupsOpenConfig> getGroupsOpenConfigs() {
		return null;
	}

	public List<GemAltarOpenConfig> getOpenConfigs() {
		return null;
	}
	
	public List<GemAltarUserOpen> getUserOpen(String userId) {
		return null;
	}

	public List<GemAltarUserPack> getUserPack(String userId) {
		return null;
	}





	public void clearOnLoginOut(String userId) throws Exception {
		userInfo.remove(userId);
	}

	public List<GemAltarOpenConfig> getOpenConfig(int groups) {
		return OPEN_CONFIG.get(groups);
	}

	@Override
	public void removeGroups(String userId, int groups) {
		gemAltarDaoMysqlImpl.removeGroups(userId, groups);
		
	}

	@Override
	public void addGroups(String userId, int groups) {
		gemAltarDaoMysqlImpl.addGroups(userId, groups);
		
	}

	@Override
	public void removePack(GemAltarUserPack pack) {
		gemAltarDaoMysqlImpl.removePack(pack);
		
	}

	@Override
	public void addPack(GemAltarUserPack pack) {
		gemAltarDaoMysqlImpl.addPack(pack);
		
	}

	@Override
	public GemAltarGroupsOpenConfig getGroupsConfig(int groups) {
		return GROUPS_CONFIG.get(groups);
	}

	@Override
	public List<GemAltarUserAutoSell> getAuto(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addAuto(String userId, int level) {
		gemAltarDaoMysqlImpl.addAuto(userId, level);
	}

	@Override
	public void removeAuto(String userId, int level) {
		gemAltarDaoMysqlImpl.removeAuto(userId, level);
	}

	public void addLog(GemAltarUserSellLog log) {
		gemAltarDaoMysqlImpl.addLog(log);
	}

	public List<GemAltarUserSellLog> getUserLog(String userId) {
		return gemAltarDaoMysqlImpl.getUserLog(userId);
	}

	@Override
	public void removeAllPack(String userId) {
		gemAltarDaoMysqlImpl.removeAllPack(userId);
	}

}
