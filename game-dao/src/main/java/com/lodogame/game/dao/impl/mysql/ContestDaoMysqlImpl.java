package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.ContestDao;
import com.lodogame.model.ContestBattleReport;
import com.lodogame.model.ContestHistory;
import com.lodogame.model.ContestInfo;
import com.lodogame.model.ContestReward;
import com.lodogame.model.ContestUser;
import com.lodogame.model.ContestUserHero;
import com.lodogame.model.log.ContestRewardLog;

public class ContestDaoMysqlImpl implements ContestDao {

	@Autowired
	private Jdbc jdbc;

	@Override
	public List<ContestUser> getList() {
		String sql = "SELECT * FROM contest_user";
		return this.jdbc.getList(sql, ContestUser.class);
	}

	@Override
	public void deleteNotArrayTeam() {

		String sql = "DELETE FROM contest_user WHERE array_finish = 0";
		this.jdbc.update(sql, new SqlParameter());
	}

	@Override
	public ContestUser get(String userId) {

		String sql = "SELECT * FROM contest_user WHERE user_id = ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);

		return this.jdbc.get(sql, ContestUser.class, parameter);
	}

	@Override
	public ContestUserHero getContestUserHero(String userHeroId) {

		String sql = "SELECT * FROM contest_user_hero WHERE user_hero_id = ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userHeroId);

		return this.jdbc.get(sql, ContestUserHero.class, parameter);
	}

	@Override
	public boolean updateSelectTeam(String userId, int selectTeam) {

		String sql = "UPDATE contest_user SET select_team = ? WHERE user_id = ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(selectTeam);
		parameter.setString(userId);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean updateDeadRound(String userId, int roundWin, int deadRound) {

		String sql = "UPDATE contest_user SET round_win = ?, dead_round = ? WHERE user_id = ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(roundWin);
		parameter.setInt(deadRound);
		parameter.setString(userId);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean updateArrayFinish(String userId) {

		String sql = "UPDATE contest_user SET array_finish = 1 WHERE user_id = ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean updateHeroPosAndTeam(String userHeroId, int team, int pos) {

		String sql = "UPDATE contest_user_hero SET team = ?, pos = ? WHERE user_hero_id = ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(team);
		parameter.setInt(pos);
		parameter.setString(userHeroId);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean replace(ContestUser contestUser) {
		return this.jdbc.replace(contestUser) > 0;
	}

	@Override
	public boolean replace(ContestHistory contestHistory) {
		return this.jdbc.replace(contestHistory) > 0;
	}

	@Override
	public List<ContestUserHero> getHeroList(String userId) {

		String sql = "SELECT * FROM contest_user_hero WHERE user_id = ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);

		return this.jdbc.getList(sql, ContestUserHero.class, parameter);

	}

	@Override
	public List<ContestUserHero> getContestUserHeroList(String userId, int team) {

		String sql = "SELECT * FROM contest_user_hero WHERE user_id = ? AND team = ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(team);

		return this.jdbc.getList(sql, ContestUserHero.class, parameter);

	}

	@Override
	public boolean replace(ContestUserHero contestUserHero) {
		return this.jdbc.replace(contestUserHero) > 0;
	}

	@Override
	public ContestInfo getContestStatus(int week) {

		String sql = "SELECT * FROM contest_info WHERE week = ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(week);

		return this.jdbc.get(sql, ContestInfo.class, parameter);
	}

	@Override
	public List<ContestHistory> getContestHistory(String userId) {

		String sql = "SELECT * FROM contest_history WHERE user_id = ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);

		return this.jdbc.getList(sql, ContestHistory.class, parameter);
	}

	@Override
	public boolean saveContestStatus(ContestInfo contestInfo) {
		return this.jdbc.replace(contestInfo) > 0;
	}

	@Override
	public boolean truncateContestUser() {

		String sql = "truncate table contest_user";
		SqlParameter parameter = new SqlParameter();
		this.jdbc.update(sql, parameter);

		String sql2 = "truncate table contest_history";
		this.jdbc.update(sql2, parameter);

		String sql3 = "truncate table contest_user_hero";
		this.jdbc.update(sql3, parameter);

		return true;
	}

	@Override
	public boolean truncateContestHero() {

		String sql = "truncate table contest_user";
		SqlParameter parameter = new SqlParameter();

		this.jdbc.update(sql, parameter);
		return true;
	}

	@Override
	public boolean setArrayNotFinish() {

		String sql = "INSERT INTO contest_user_history(user_id, username, dead_round, ind) SELECT user_id, username, dead_round, ind FROM contest_user WHERE is_empty = 0";
		SqlParameter parameter = new SqlParameter();
		this.jdbc.update(sql, parameter);

		String clean = "DELETE FROM contest_user WHERE dead_round <> 100";
		this.jdbc.update(clean, parameter);

		String update = "UPDATE contest_user SET array_finish = 0";
		this.jdbc.update(update, parameter);

		return true;
	}

	@Override
	public boolean add(ContestRewardLog contestRewardLog) {
		return this.jdbc.insert(contestRewardLog) > 0;
	}

	@Override
	public ContestReward getReward(int category, int rank) {

		String sql = "SELECT * FROM contest_reward WHERE category = ? AND rank = ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(category);
		parameter.setInt(rank);

		return this.jdbc.get(sql, ContestReward.class, parameter);
	}

	@Override
	public boolean addReport(ContestBattleReport report) {
		return this.jdbc.insert(report) > 0;
	}

}
