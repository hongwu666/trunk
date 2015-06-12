package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.GemAltarGroupsOpenConfig;
import com.lodogame.model.GemAltarOpenConfig;
import com.lodogame.model.GemAltarUserAutoSell;
import com.lodogame.model.GemAltarUserInfo;
import com.lodogame.model.GemAltarUserOpen;
import com.lodogame.model.GemAltarUserPack;
import com.lodogame.model.log.GemAltarUserSellLog;

public interface GemAltarDao {
	
	void addLog(GemAltarUserSellLog log);
	List<GemAltarUserSellLog> getUserLog(String userId);
	
	List<GemAltarUserAutoSell> getAuto(String userId);
	void addAuto(String userId,int level);
	void removeAuto(String userId,int level);
	
	List<GemAltarGroupsOpenConfig> getGroupsOpenConfigs();
	List<GemAltarOpenConfig> getOpenConfigs();
	
	GemAltarGroupsOpenConfig getGroupsConfig(int groups);
	GemAltarUserInfo getUserInfo(String userId);
	
	List<GemAltarOpenConfig> getOpenConfig(int groups);
	List<GemAltarUserOpen> getUserOpen(String userId);
	List<GemAltarUserPack> getUserPack(String userId);
	
	void removeGroups(String userId,int groups);
	void addGroups(String userId,int groups);
	
	void removePack(GemAltarUserPack pack);
	void removeAllPack(String userId);
	void addPack(GemAltarUserPack pack);
}
