package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.TreasureConfigInfo;
import com.lodogame.model.TreasureUserInfo;

/**
 * 聚宝盆dao
 * @author 
 *
 */
public interface TreasureDao {
	
	//聚宝盆配置
	List<TreasureConfigInfo> getConfig();
	
	TreasureConfigInfo getConfigByGrade(int type);
	
	//用户聚宝盆开启记录
	List<TreasureUserInfo> getUserInfo(String userId,String date);

	//更新开启记录
	void updateUserInfo(TreasureUserInfo info);
	
	//插入新的开启记录
	void insertUserInfo(TreasureUserInfo info);
	
	//清理
	void replace();
	
}
