package com.lodogame.game.dao.impl.mysql;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.SystemHeroStoneMallDao;
import com.lodogame.game.utils.SqlUtil;
import com.lodogame.model.StoneMallTimeLog;
import com.lodogame.model.SystemHeroStoneMall;
import com.lodogame.model.UserHeroStoneMallLog;

public class SystemHeroStoneMallDaoMysqlImpl implements SystemHeroStoneMallDao {

	@Autowired
	private Jdbc jdbc;
	
	@Override
	public List<SystemHeroStoneMall> getAllList() {
		String sql = "select * from system_hero_stone_mall where rate <> 0";
		
		return jdbc.getList(sql, SystemHeroStoneMall.class);
	}

	@Override
	public UserHeroStoneMallLog getUserStoneMallLog(String userId) {
		String sql = "select * from user_hero_stone_mall_log where user_id = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		
		return jdbc.get(sql,UserHeroStoneMallLog.class,parameter);
	}

	@Override
	public boolean updateUserStoneMallLog(UserHeroStoneMallLog userHeroStoneMallLog) {
		String sql = "INSERT INTO user_hero_stone_mall_log(user_id, exchange_ids,system_ids, created_time,updated_time) VALUES(?, ?, ?,?,?) " +
				"	ON DUPLICATE KEY UPDATE user_id= ?, exchange_ids = ?, system_ids= ?,updated_time = ? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userHeroStoneMallLog.getUserId());
		parameter.setString(userHeroStoneMallLog.getExchangeIds());
		parameter.setString(userHeroStoneMallLog.getSystemIds());
		parameter.setObject(userHeroStoneMallLog.getCreatedTime());
		parameter.setObject(userHeroStoneMallLog.getUpdatedTime());
		parameter.setString(userHeroStoneMallLog.getUserId());
		parameter.setString(userHeroStoneMallLog.getExchangeIds());
		parameter.setString(userHeroStoneMallLog.getSystemIds());
		parameter.setObject(userHeroStoneMallLog.getUpdatedTime());
		
		return jdbc.update(sql, parameter) > 0;
	}

	@Override
	public SystemHeroStoneMall get(int systemId) {
		String sql = "select * from system_hero_stone_mall where system_id = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(systemId);
		
		return jdbc.get(sql,SystemHeroStoneMall.class,parameter);
	}

	@Override
	public StoneMallTimeLog getMallTimeLog() {
		String sql = "select * from stone_mall_time_log where id = 1 ";
		
		SqlParameter parameter = new SqlParameter();
		
		return jdbc.get(sql, StoneMallTimeLog.class, parameter);
	}

	@Override
	public boolean updateMallTimesLog(StoneMallTimeLog stoneMallTimeLog) {
		String sql = "INSERT INTO stone_mall_time_log(id, system_ids, created_time,updated_time) VALUES(1, ?, ?, ?) " +
		"	ON DUPLICATE KEY UPDATE id= 1, system_ids= ?, updated_time = ? ";
		
		SqlParameter parameter = new SqlParameter();

		parameter.setString(stoneMallTimeLog.getSystemIds());
		parameter.setObject(stoneMallTimeLog.getCreatedTime());
		parameter.setObject(stoneMallTimeLog.getUpdatedTime());

		parameter.setString(stoneMallTimeLog.getSystemIds());
		parameter.setObject(stoneMallTimeLog.getUpdatedTime());
		
		return jdbc.update(sql, parameter) > 0;
	}

	@Override
	public List<SystemHeroStoneMall> getListBySystemIds(String ids) {
		String tids = SqlUtil.join(Arrays.asList(ids.split(",")));
		String sql = "select * from  system_hero_stone_mall where system_id in (" + tids + ")";
		
		return jdbc.getList(sql, SystemHeroStoneMall.class);
	}

	@Override
	public int getSumRate() {
		String sql = "select sum(rate) from system_hero_stone_mall ";
		
		SqlParameter parameter = new SqlParameter();
		
		return jdbc.getInt(sql, parameter);
	}

	@Override
	public boolean clearSystemIds() {
		String sql = "update user_hero_stone_mall_log set exchange_ids = ''";
		return jdbc.update(sql, new SqlParameter()) > 0;
	}

}
