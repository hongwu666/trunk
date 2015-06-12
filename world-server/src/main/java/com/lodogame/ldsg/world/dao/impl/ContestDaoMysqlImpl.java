package com.lodogame.ldsg.world.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.ldsg.world.dao.ContestDao;
import com.lodogame.ldsg.world.model.ContestUserReady;
import com.lodogame.ldsg.world.model.ServerRegList;
import com.lodogame.model.ContestBattleReport;
import com.lodogame.model.ContestHistory;
import com.lodogame.model.ContestInfo;
import com.lodogame.model.ContestTopUser;
import com.lodogame.model.ContestWorldUser;

public class ContestDaoMysqlImpl implements ContestDao {

	@Autowired
	private Jdbc jdbc;

	@Override
	public boolean regServer(String serverId) {

		String sql = "INSERT IGNORE INTO server_reg_list(server_id, created_time) VALUES(?, now())";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(serverId);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public List<ContestWorldUser> getList() {
		String sql = "SELECT * FROM contest_world_user";
		return this.jdbc.getList(sql, ContestWorldUser.class);
	}

	@Override
	public List<ContestUserReady> getReadyList() {
		String sql = "SELECT * FROM contest_user_ready";
		return this.jdbc.getList(sql, ContestUserReady.class);
	}

	@Override
	public ContestWorldUser get(String userId) {

		String sql = "SELECT * FROM contest_world_user WHERE user_id = ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);

		return this.jdbc.get(sql, ContestWorldUser.class, parameter);
	}

	@Override
	public boolean replace(ContestWorldUser contestUser) {
		return this.jdbc.replace(contestUser) > 0;
	}

	@Override
	public boolean replace(ContestUserReady contestUserReady) {
		return this.jdbc.replace(contestUserReady) > 0;
	}

	@Override
	public boolean addReport(ContestBattleReport report) {
		return this.jdbc.replace(report) > 0;
	}

	@Override
	public boolean replace(ContestHistory history) {
		return this.jdbc.replace(history) > 0;
	}

	@Override
	public ContestInfo getContestStatus(int week) {

		String sql = "SELECT * FROM contest_info WHERE week = ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(week);

		return this.jdbc.get(sql, ContestInfo.class, parameter);
	}

	@Override
	public boolean saveContestStatus(ContestInfo contestInfo) {
		return this.jdbc.replace(contestInfo) > 0;
	}

	@Override
	public boolean truncateContestUser() {

		// 清用户表
		String sql = "truncate table contest_world_user";
		SqlParameter parameter = new SqlParameter();

		this.jdbc.update(sql, parameter);

		// 清记录表
		String sql2 = "truncate table contest_history";

		this.jdbc.update(sql2, parameter);

		return true;
	}

	@Override
	public boolean truncateContestUserReady() {

		// 清用户表
		String sql = "truncate table contest_user_ready";
		SqlParameter parameter = new SqlParameter();

		this.jdbc.update(sql, parameter);

		return true;
	}

	@Override
	public boolean replace(ContestTopUser contestTopUser) {
		return this.jdbc.replace(contestTopUser) > 0;
	}

	@Override
	public boolean truncateTopUser() {

		// 清用神殿玩家列表
		String sql = "truncate table contest_top_user";
		SqlParameter parameter = new SqlParameter();

		this.jdbc.update(sql, parameter);

		return true;
	}

	@Override
	public List<ContestTopUser> getContestTopUserList() {

		String sql = "SELECT * FROM contest_top_user ORDER by dead_round DESC";

		return this.jdbc.getList(sql, ContestTopUser.class);
	}

	@Override
	public List<ServerRegList> getServerRegList() {

		String sql = "SELECT * FROM server_reg_list";

		return this.jdbc.getList(sql, ServerRegList.class);

	}

	@Override
	public List<ContestHistory> getContestHistory(String userId) {

		String sql = "SELECT * FROM contest_history WHERE user_id = ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);

		return this.jdbc.getList(sql, ContestHistory.class, parameter);
	}

}
