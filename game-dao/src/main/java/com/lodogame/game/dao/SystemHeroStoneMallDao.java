package com.lodogame.game.dao;

import java.util.List;
import com.lodogame.model.StoneMallTimeLog;
import com.lodogame.model.SystemHeroStoneMall;
import com.lodogame.model.UserHeroStoneMallLog;

public interface SystemHeroStoneMallDao {
	public SystemHeroStoneMall get(int systemId);
	
	public List<SystemHeroStoneMall> getListBySystemIds(String ids);
	
	public List<SystemHeroStoneMall> getAllList();
	
	public UserHeroStoneMallLog getUserStoneMallLog(String userId);
	
	public boolean updateUserStoneMallLog(UserHeroStoneMallLog userHeroStoneMallLog);
	
	public boolean clearSystemIds();
	
	public StoneMallTimeLog getMallTimeLog();
	
	public boolean updateMallTimesLog(StoneMallTimeLog stoneMallTimeLog);
	
	public int getSumRate();
}
