package com.lodogame.game.dao.impl.mysql;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.OnlyOneDao;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.model.OnlyoneStatus;
import com.lodogame.model.OnlyoneUserHero;
import com.lodogame.model.OnlyoneUserReg;
import com.lodogame.model.OnlyoneUserReward;
import com.lodogame.model.OnlyoneWeekLog;
import com.lodogame.model.OnlyoneWeekRank;

public class OnlyOneDaoMysqlImpl implements OnlyOneDao {

	@Autowired
	private Jdbc jdbc;

	@Override
	public boolean setStatus(int status) {

		String sql = "INSERT INTO onlyone_status(date, status, created_time) VALUES(?, ?, now()) ON DUPLICATE KEY update status = VALUES(status)";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(DateUtils.getDate());
		parameter.setInt(status);
		return this.jdbc.update(sql, parameter) > 0;

	}

	@Override
	public boolean isWeekCutOff() {
		String sql = "SELECT * FROM onlyone_week_log WHERE date = ? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(DateUtils.getDate());
		OnlyoneWeekLog log = this.jdbc.get(sql, OnlyoneWeekLog.class, parameter);
		return log != null;
	}

	@Override
	public boolean addWeekCutOffLog() {
		OnlyoneWeekLog log = new OnlyoneWeekLog();
		log.setWeek(DateUtils.getWeekOfYear());
		log.setDate(DateUtils.getDate());
		log.setCreatedTime(new Date());
		return this.jdbc.insert(log) > 0;
	}

	@Override
	public void loadData() {
		throw new NotImplementedException();
	}

	@Override
	public int getStatus() {
		String sql = "SELECT * FROM onlyone_status WHERE date = ? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(DateUtils.getDate());
		OnlyoneStatus onlyoneStatus = this.jdbc.get(sql, OnlyoneStatus.class, parameter);
		if (onlyoneStatus != null) {
			return onlyoneStatus.getStatus();
		}
		return 1;
	}

	@Override
	public boolean add(OnlyoneUserReg onlyOneReg) {
		return this.jdbc.replace(onlyOneReg) > 0;
	}

	@Override
	public boolean addReward(OnlyoneUserReward onlyoneUserReward) {
		return this.jdbc.insert(onlyoneUserReward) > 0;
	}

	@Override
	public OnlyoneUserReward getReward(String userId, int id) {
		String sql = "SELECT * FROM onlyone_user_reward WHERE user_id = ?  AND id = ? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(id);
		return this.jdbc.get(sql, OnlyoneUserReward.class, parameter);
	}

	@Override
	public List<OnlyoneUserReward> getRewardList(String userId) {
		String sql = "SELECT * FROM onlyone_user_reward WHERE user_id = ?  AND status = 0 ORDER BY type desc ";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		return this.jdbc.getList(sql, OnlyoneUserReward.class, parameter);
	}

	@Override
	public boolean updateReward(String userId, int id, int status) {
		String sql = "UPDATE onlyone_user_reward SET status = ? WHERE user_id = ?  AND id = ? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(status);
		parameter.setString(userId);
		parameter.setInt(id);
		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean addWeekPoint(String userId, String username, double point) {
		String sql = "INSERT INTO onlyone_week_rank(user_id, username, point, created_time) VALUES(?, ?, ?, now()) ON DUPLICATE KEY update point = point + VALUES(point);";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setString(username);
		parameter.setDouble(point);
		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public OnlyoneUserReg getByUserId(String userId) {
		throw new NotImplementedException();
	}

	@Override
	public List<OnlyoneUserReg> getRankList() {
		return null;
	}

	@Override
	public void cleanData() {

		String backup = "INSERT INTO onlyone_user_reg_history(user_id, username, status, total_count, win_count, win_times, vigour, point, add_vigour_time, created_time)";
		backup += " SELECT user_id, username, status, total_count, win_count, win_times, vigour, point, add_vigour_time, created_time FROM onlyone_user_reg";

		this.jdbc.update(backup, new SqlParameter());

		String truncate = "TRUNCATE TABLE onlyone_user_reg";

		this.jdbc.update(truncate, new SqlParameter());

		truncate = "TRUNCATE TABLE onlyone_user_hero";

		this.jdbc.update(truncate, new SqlParameter());

	}

	@Override
	public void cleanWeekRank() {

		String backup = "INSERT INTO onlyone_week_rank_history(user_id, username, point, created_time)";
		backup += " SELECT user_id, username, point, created_time FROM onlyone_week_rank";

		this.jdbc.update(backup, new SqlParameter());

		String truncate = "TRUNCATE TABLE onlyone_week_rank";

		this.jdbc.update(truncate, new SqlParameter());
	}

	@Override
	public Collection<OnlyoneUserReg> getRegList() {
		String sql = "SELECT * FROM onlyone_user_reg";
		return this.jdbc.getList(sql, OnlyoneUserReg.class);
	}

	@Override
	public List<OnlyoneUserReg> getRegList(int status) {
		throw new NotImplementedException();
	}

	@Override
	public List<OnlyoneUserReg> getWinRankList() {
		throw new NotImplementedException();
	}

	@Override
	public List<OnlyoneWeekRank> getWeekRank() {
		String sql = "SELECT * FROM onlyone_week_rank ORDER BY point DESC";
		return this.jdbc.getList(sql, OnlyoneWeekRank.class);
	}

	@Override
	public int getRank(String userId) {
		throw new NotImplementedException();
	}

	@Override
	public boolean addOnlyoneUserhero(OnlyoneUserHero onlyoneUserHero) {
		return this.jdbc.replace(onlyoneUserHero) > 0;
	}

	@Override
	public int getOnlyoneUserHeroLife(String userHeroId) {
		String sql = "SELECT * FROM onlyone_user_hero WHERE user_hero_id = ? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userHeroId);
		OnlyoneUserHero hero = this.jdbc.get(sql, OnlyoneUserHero.class, parameter);
		if (hero != null) {
			return hero.getLife();
		}
		return -1;
	}

	@Override
	public int getOnlyoneUserHeroMorale(String userHeroId) {
		String sql = "SELECT * FROM onlyone_user_hero WHERE user_hero_id = ? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userHeroId);
		OnlyoneUserHero hero = this.jdbc.get(sql, OnlyoneUserHero.class, parameter);
		if (hero != null) {
			return hero.getMorale();
		}
		return 0;
	}

	@Override
	public boolean deleteOnlyoneUserHero(String userId) {
		String sql = "DELETE FROM onlyone_user_hero WHERE user_id = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		return this.jdbc.update(sql, parameter) > 0;
	}

}
