package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserMysteryMallDao;
import com.lodogame.model.UserMysteryMallDetail;
import com.lodogame.model.UserMysteryMallInfo;

public class UserMysteryMallDaoMysqlImpl implements UserMysteryMallDao {

	@Autowired
	private Jdbc jdbc;

	@Override
	public UserMysteryMallInfo getUserMysteryMallInfo(String userId, int mallType) {

		String sql = "SELECT * FROM user_mystery_mall_info where user_id = ? AND mall_type = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(mallType);

		return this.jdbc.get(sql, UserMysteryMallInfo.class, parameter);
	}

	@Override
	public List<UserMysteryMallDetail> getUserMysteryMallDetail(String userId, int mallType) {

		String sql = "SELECT * FROM user_mystery_mall_detail where user_id = ? AND mall_type = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(mallType);

		return this.jdbc.getList(sql, UserMysteryMallDetail.class, parameter);

	}

	@Override
	public UserMysteryMallDetail getUserMysteryMallDetail(String userId, int mallType, int id) {

		String sql = "SELECT * FROM user_mystery_mall_detail where user_id = ? AND mall_type = ? AND id = ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(mallType);
		parameter.setInt(id);

		return this.jdbc.get(sql, UserMysteryMallDetail.class, parameter);
	}

	@Override
	public boolean updateUserMysteryMallFlag(String userId, int mallType, int id) {

		String sql = "UPDATE user_mystery_mall_detail SET flag = 1 WHERE user_id = ?  AND mall_type = ? AND id = ? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(mallType);
		parameter.setInt(id);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean addUserMysterMallDetail(String userId, int mallType, List<UserMysteryMallDetail> list) {

		String deleteSql = "DELETE FROM user_mystery_mall_detail WHERE user_id = ? AND mall_type = ? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(mallType);
		this.jdbc.update(deleteSql, parameter);

		this.jdbc.insert(list);
		return true;

	}

	@Override
	public boolean addUserMysterMallInfo(String userId, UserMysteryMallInfo userMysteryMallInfo) {

		String sql = "INSERT INTO user_mystery_mall_info(user_id, mall_type, normal_times, times, updated_time, last_refresh_time) ";
		sql = sql
				+ " VALUES(?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE normal_times = VALUES(normal_times), times = VALUES(times), updated_time = VALUES(updated_time), last_refresh_time = VALUES(last_refresh_time)";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(userMysteryMallInfo.getMallType());
		parameter.setInt(userMysteryMallInfo.getNormalTimes());
		parameter.setInt(userMysteryMallInfo.getTimes());
		parameter.setDate(userMysteryMallInfo.getUpdatedTime());
		parameter.setDate(userMysteryMallInfo.getLastRefreshTime());

		return this.jdbc.update(sql, parameter) > 0;
	}

}
