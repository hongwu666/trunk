package com.lodogame.game.dao;

import java.util.List;
import java.util.Set;

import com.lodogame.model.ExpeditionConfig;
import com.lodogame.model.ExpeditionMaxExId;
import com.lodogame.model.ExpeditionNum;
import com.lodogame.model.UserExInfo;
import com.lodogame.model.UserExpeditionHero;
import com.lodogame.model.UserExpeditionVsTable;

public interface ExDao {

	UserExInfo getExInfo(String userId);

	List<UserExpeditionHero> getHeros(String userId);

	void updateHero(String userId, List<UserExpeditionHero> heos);

	void updateMyHero(String userId, List<UserExpeditionHero> heos);

	void insertHero(String userId, List<UserExpeditionHero> heos);

	void deleteHeroAll(String userId, Set<Long> exIds);

	List<UserExpeditionVsTable> getVs(String userId);

	void updateVs(UserExpeditionVsTable vs);

	void insertVs(UserExpeditionVsTable vs);

	void deleteVs(UserExpeditionVsTable vs);

	void deleteVsAll(String userId);

	ExpeditionMaxExId getMaxExId();

	long getExId();

	List<ExpeditionConfig> getLevelConfigs();

	ExpeditionConfig getLevelConfig(int indx);

	void clearCache(String userId);

	ExpeditionNum getNum(String userId);

	void clearNum();

	void insertNum(ExpeditionNum num);

	void updateNum(ExpeditionNum num);
}
