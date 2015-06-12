package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.ResourceGk;
import com.lodogame.model.ResourceGkConfig;
import com.lodogame.model.ResourceGkPkLog;
import com.lodogame.model.ResourceGkStart;
import com.lodogame.model.ResourceNum;
import com.lodogame.model.ResourceStartConfig;
import com.lodogame.model.ResourceUserInfo;

public interface ResourceDao  {
	
	List<ResourceStartConfig> getStartConfigs();
	List<ResourceGkConfig> getGkConfigs();
	List<ResourceNum>  getNum(String userId,String date);
	void updateNum(ResourceNum num);
	
	void insertNum(ResourceNum num);
	
	
	List<ResourceGk> getGk(String userId,String date);
	
	void insertGk(ResourceGk gk);

	
	
	List<ResourceGkStart> getGkStart(int ids);
	
	void insertGkStart(ResourceGkStart start);
	
	void updateGkStart(ResourceGkStart start);
	
	
	
	List<ResourceGkPkLog> getGkPkLog(int ids);
	
	void insertGkPkLog(ResourceGkPkLog log);
	
	void updateGkPkLog(ResourceGkPkLog log);
	
	int getMaxIds();
	
	ResourceUserInfo getInfo(String userId);
	
	ResourceGkConfig getGkConfig(int type,int dif,int g,int start);
	
	int getStartByCurr(int curr);
	
	void replace();
	
	void reset(int id);
}
