/*
 * Powered By [rapid-framework]
 * Web Site: http://www.rapid-framework.org.cn
 * Google Code: http://code.google.com/p/rapid-framework/
 * Since 2008 - 2013
 */

package com.lodogame.game.dao.impl.mysql;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserSweepInfoDao;
import com.lodogame.model.UserSweepInfo;

public class UserSweepInfoDaoMysqlImpl implements UserSweepInfoDao {
	@Autowired
	private Jdbc jdbc;
	private final static String TABLE = "user_sweep_info";

	@Override
	public boolean add(UserSweepInfo sweepInfo) {
		
		String sql = " INSERT INTO user_sweep_info(user_id, created_time, end_time, updated_time)";
		sql += " VALUES(?,now(), ?, now()) ON DUPLICATE KEY UPDATE ";
		sql += " created_time = now(), end_time= ?, updated_time = now()";
		
		SqlParameter parameter = new SqlParameter();
		parameter.setString(sweepInfo.getUserId());
		parameter.setObject(sweepInfo.getEndTime());
		parameter.setObject(sweepInfo.getEndTime());

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public UserSweepInfo getCurrentSweep(String userId) {
		String sql = "select * from " + TABLE + " where user_id = ? ";
		SqlParameter params = new SqlParameter();
		params.setString(userId);
		return jdbc.get(sql, UserSweepInfo.class, params);
	}

	@Override
	public boolean updateSweepComplete(String userId, Date date) {
		String sql = "update " + TABLE + " set end_time=now() where user_id = ? ";
		SqlParameter params = new SqlParameter();
		params.setString(userId);
		return jdbc.update(sql, params) > 0;
	
	}




}
