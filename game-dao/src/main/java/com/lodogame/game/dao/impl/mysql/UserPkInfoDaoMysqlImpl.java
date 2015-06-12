package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserPkInfoDao;
import com.lodogame.model.UserPkInfo;

public class UserPkInfoDaoMysqlImpl implements UserPkInfoDao {

	@Autowired
	private Jdbc jdbc;

	public final static String table = "user_pk_info";

	public final static String columns = "*";

	@Override
	public boolean add(UserPkInfo userPkInfo) {
		return this.jdbc.insert(userPkInfo) > 0;
	}

	@Override
	public boolean add(List<UserPkInfo> userPkInfoList) {
		this.jdbc.insert(userPkInfoList);
		return true;
	}

	@Override
	public UserPkInfo getByRank(int rank) {
		throw new NotImplementedException();
	}

	@Override
	public UserPkInfo getByUserId(String userId) {
		throw new NotImplementedException();
	}

	@Override
	public boolean update(UserPkInfo userPkInfo, String targetUserId) {

		String sql = "UPDATE " + table + " SET rank = ? , pk_times = ? , update_pk_time = ? , buy_pk_times = ? , last_buy_time = ?, level = ?,fast_rank=? WHERE user_id = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(userPkInfo.getRank());
		parameter.setInt(userPkInfo.getPkTimes());
		parameter.setDate(userPkInfo.getUpdatePkTime());
		parameter.setInt(userPkInfo.getBuyPkTimes());
		parameter.setDate(userPkInfo.getLastBuyTime());
		parameter.setInt(userPkInfo.getLevel());
		parameter.setInt(userPkInfo.getFastRank());
		parameter.setString(userPkInfo.getUserId());

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean updateRank(String userId, int random) {

		String sql = "UPDATE " + table + " SET rank = ? WHERE user_id = ? LIMIT 1";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(random);
		parameter.setString(userId);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public int getLastRank() {
		throw new NotImplementedException();
	}

	@Override
	public List<UserPkInfo> getList() {
		String sql = "SELECT * FROM " + table;
		return this.jdbc.getList(sql, UserPkInfo.class);
	}

	@Override
	public List<UserPkInfo> getRangeRank(int lowRank, int upperRank, String userId) {
		String sql = "select * from " + table + " where rank >= ? and rank <= ? and user_id <> ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(lowRank);
		parameter.setInt(upperRank);
		parameter.setString(userId);
		return this.jdbc.getList(sql, UserPkInfo.class, parameter);
	}
}
