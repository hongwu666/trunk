package com.lodogame.game.dao.impl.mysql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserForcesDao;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.game.utils.TableUtils;
import com.lodogame.model.UserForces;
import com.lodogame.model.UserForcesCount;
import com.lodogame.model.UserForcesResetTimes;

public class UserForcesDaoMysqlImpl implements UserForcesDao {

	public final static String columns = "*";

	private static final String tablePrex = "user_forces";
	private static final int tableCount = 128;
	@Autowired
	private Jdbc jdbc;

	public List<UserForces> getUserForcesList(String userId, int sceneId) {

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);

		String sql = "SELECT " + columns + " FROM " + TableUtils.getTableName(userId, tablePrex, tableCount) + " WHERE user_id = ? ";
		if (sceneId > 0) {
			sql += " and scene_id = ?";
			parameter.setInt(sceneId);
		}

		return this.jdbc.getList(sql, UserForces.class, parameter);
	}

	/**
	 * 此方法只能添加 userid一样的list 不然会出现插入出问题哦
	 * 
	 * @param forcesList
	 */
	public void add(List<UserForces> forcesList) {
		if (forcesList == null || forcesList.size() == 0) {
			return;
		}
		this.jdbc.insert(TableUtils.getTableName(forcesList.get(0).getUserId(), tablePrex, tableCount), forcesList);
	}

	@Override
	public boolean updateStatus(String userId, int forcesGroup, int status, int times) {
		String table = TableUtils.getTableName(userId, tablePrex, tableCount);

		String sql = "UPDATE " + table + " SET status = ?,  times = times + ?, updated_time = now() WHERE user_id = ? AND group_id = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(status);
		parameter.setInt(times);
		parameter.setString(userId);
		parameter.setInt(forcesGroup);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean add(UserForces userForces) {
		return this.jdbc.insert(TableUtils.getTableName(userForces.getUserId(), tablePrex, tableCount), userForces) > 0;
	}

	@Override
	public UserForces getUserCurrentForces(String userId, int forcesType) {
		String table = TableUtils.getTableName(userId, tablePrex, tableCount);
		String sql = "SELECT " + columns + " FROM " + table + " WHERE user_id = ? AND forces_type = ? ORDER BY group_id DESC LIMIT 1 ";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(forcesType);

		return this.jdbc.get(sql, UserForces.class, parameter);
	}

	@Override
	public UserForces get(String userId, int forcesGroup) {

		String sql = "SELECT " + columns + " FROM " + TableUtils.getTableName(userId, tablePrex, tableCount) + " WHERE user_id = ? AND group_id = ? LIMIT 1 ";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(forcesGroup);

		return this.jdbc.get(sql, UserForces.class, parameter);
	}

	@Override
	public boolean updateTimes(String userId, int forcesGroup, int times) {
		String table = TableUtils.getTableName(userId, tablePrex, tableCount);

		String sql = "UPDATE " + table + "  SET times = ? , updated_time = now() WHERE user_id = ? AND group_id = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(times);
		parameter.setString(userId);
		parameter.setInt(forcesGroup);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean updateTimes(String uid, int times, List<Integer> forcesGroups) {
		String table = TableUtils.getTableName(uid, tablePrex, tableCount);
		String sql = "UPDATE " + table + "  SET times = ? , updated_time = now() WHERE user_id = ? AND group_id in ";

		String forcesIdWhere = "";
		for (Integer fid : forcesGroups) {
			forcesIdWhere += fid + ", ";
		}

		forcesIdWhere = forcesIdWhere.substring(0, forcesIdWhere.length() - 2);

		sql += "(" + forcesIdWhere + ")";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(times);
		parameter.setString(uid);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public List<UserForcesCount> listOrderByForceCntDesc(int offset, int size) {

		List<UserForcesCount> list = new ArrayList<UserForcesCount>();
		for (int i = 0; i < tableCount; i++) {
			String sql = "select user_id, count(group_id) as forces_count from user_forces_" + i
					+ " where forces_type = 1 or forces_type = 2 group by user_id order by forces_count desc limit ?, ?";
			SqlParameter param = new SqlParameter();
			param.setInt(offset);
			param.setInt(size);
			list.addAll(this.jdbc.getList(sql, UserForcesCount.class, param));
		}
		Collections.sort(list, new Comparator<UserForcesCount>() {
			@Override
			public int compare(UserForcesCount o1, UserForcesCount o2) {
				if (o1.getForcesCount() > o2.getForcesCount()) {
					return -1;
				} else if (o1.getForcesCount() < o2.getForcesCount()) {
					return 1;
				}
				return 0;
			}
		});
		return list.subList(offset, offset + size);

	}

	@Override
	public boolean resetForcesTimes(String userId, int groupId) {

		String sql = "UPDATE " + TableUtils.getTableName(userId, tablePrex, tableCount) + " SET times = 0 WHERE user_id = ? AND group_id = ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(groupId);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public long getAmendEmbattleTime(String userId) {
		return 0;
	}

	@Override
	public void setAmendEmbattleTime(String userId, long timestamp) {

	}

	@Override
	public boolean updatePassStar(String userId, int forcesGroup, int passStar) {
		String table = TableUtils.getTableName(userId, tablePrex, tableCount);
		String sql = "UPDATE " + table + "  SET pass_star = ?  WHERE user_id = ? AND group_id = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(passStar);
		parameter.setString(userId);
		parameter.setInt(forcesGroup);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public Map<Integer, Integer> getUserResetTimes(String userId) {

		String sql = "SELECT * FROM user_forces_reset_times WHERE user_id = ? AND date = ? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setString(DateUtils.getDate());

		List<UserForcesResetTimes> list = this.jdbc.getList(sql, UserForcesResetTimes.class, parameter);

		Map<Integer, Integer> result = new HashMap<Integer, Integer>();

		for (UserForcesResetTimes userForcesResetTimes : list) {
			result.put(userForcesResetTimes.getForcesId(), userForcesResetTimes.getTimes());
		}

		return result;

	}

	@Override
	public boolean setUserResetTimes(String userId, int forcesGroup, int times) {

		String date = DateUtils.getDate();

		String sql = "INSERT INTO user_forces_reset_times(user_id, forces_id, date, times, created_time) VALUES(?, ?, ?, ?, now()) ";
		sql += "ON DUPLICATE KEY update times = VALUES(times)";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(forcesGroup);
		parameter.setString(date);
		parameter.setInt(times);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public int getUserResetTimes(String userId, int forcesGroup) {
		throw new NotImplementedException();
	}

}
