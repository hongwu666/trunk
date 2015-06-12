package com.lodogame.game.dao.impl.mysql;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.RankDao;
import com.lodogame.model.OnlyoneWeekRank;
import com.lodogame.model.PayRankInfo;
import com.lodogame.model.ProcessRankInfo;
import com.lodogame.model.StarRankInfo;
import com.lodogame.model.UserDrawLog;
import com.lodogame.model.UserHeroInfo;
import com.lodogame.model.UserLevelRank;
import com.lodogame.model.UserPowerInfo;
import com.lodogame.model.VipRankInfo;

public class RankDaoMysqlImpl implements RankDao {

	@Autowired
	private Jdbc jdbc;

	@Autowired
	private Jdbc jdbcLog;

	@Override
	public List<UserPowerInfo> getUserPowerInfo(int size) {
		String sql = "SELECT * FROM user_power_info  ORDER BY powers DESC LIMIT ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(size);

		return this.jdbc.getList(sql, UserPowerInfo.class, parameter);

	}

	@Override
	public int getUserPowerByRank(int rank) {
		String sql = "SELECT powers FROM user_power_info  ORDER BY powers DESC LIMIT ?,1";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(rank - 1);
		return this.jdbc.getInt(sql, parameter);

	}

	public int getUserPowerByUserId(String userId) {
		String sql = "SELECT powers FROM user_power_info WHERE user_id=?";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		return this.jdbc.getInt(sql, parameter);

	}

	@Override
	public List<UserHeroInfo> getUserHeroInfo(int size) {
		String sql = "SELECT * FROM user_hero_info ORDER BY power DESC LIMIT ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(size);

		return this.jdbc.getList(sql, UserHeroInfo.class, parameter);
	}

	@Override
	public List<UserLevelRank> getUserLevelRank(int size) {

		String sql = "select user_id, max(level) as level, created_time from user_level_up_log group by user_id order by level desc, created_time asc limit ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(size);

		return this.jdbcLog.getList(sql, UserLevelRank.class, parameter);
	}

	@Override
	public List<ProcessRankInfo> getStoryRank(int size) {
		String sql = "SELECT * FROM story_rank_info ORDER BY proce DESC LIMIT ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(size);

		return this.jdbc.getList(sql, ProcessRankInfo.class, parameter);
	}

	@Override
	public List<ProcessRankInfo> getEliteRank(int size) {
		String sql = "SELECT * FROM elite_rank_info ORDER BY proce DESC LIMIT ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(size);

		return this.jdbc.getList(sql, ProcessRankInfo.class, parameter);
	}

	@Override
	public List<StarRankInfo> getEctypeStarRank(int size) {
		String sql = "SELECT * FROM ectype_rank_info ORDER BY star DESC LIMIT ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(size);

		return this.jdbc.getList(sql, StarRankInfo.class, parameter);
	}

	@Override
	public List<StarRankInfo> getResourceStarRank(int size) {
		String sql = "SELECT * FROM resource_rank_info ORDER BY star DESC LIMIT ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(size);

		return this.jdbc.getList(sql, StarRankInfo.class, parameter);
	}

	@Override
	public List<VipRankInfo> getVipRank(int size) {
		String sql = "SELECT user_id,username,vip_level FROM user ORDER BY vip_level DESC LIMIT ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(size);

		return this.jdbc.getList(sql, VipRankInfo.class, parameter);
	}

	@Override
	public List<PayRankInfo> getPayRank(int size) {
		String sql = "SELECT * FROM pay_rank_info ORDER BY pay DESC LIMIT ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(size);

		return this.jdbc.getList(sql, PayRankInfo.class, parameter);
	}

	@Override
	public List<UserDrawLog> getDrawRank(int size, Date date) {
		String sql = "SELECT user_id,sum(point) AS point FROM user_draw_log WHERE created_time >= ? group by user_id ORDER BY point DESC LIMIT ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setDate(date);
		parameter.setInt(size);
		return this.jdbc.getList(sql, UserDrawLog.class, parameter);
	}

	@Override
	public List<OnlyoneWeekRank> getOnlyoneRank(int size) {
		String sql = "SELECT * FROM onlyone_week_rank ORDER BY point DESC LIMIT ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(size);
		return this.jdbc.getList(sql, OnlyoneWeekRank.class, parameter);

	}

	@Override
	public void cleanPayRank() {
		String sql = "truncate table pay_rank_info";
		SqlParameter sp = new SqlParameter();
		this.jdbc.update(sql, sp);
	}

	@Override
	public void cleanOnlyoneRank() {
		String sql = "truncate table onlyone_week_rank";
		SqlParameter sp = new SqlParameter();
		this.jdbc.update(sql, sp);
	}

	@Override
	public void updateVipRank(VipRankInfo vipRankInfo) {
		String sql = "REPLACE INTO vip_rank_info (user_id,username,vip) VALUES(?,?,?)";
		SqlParameter sp = new SqlParameter();
		sp.setString(vipRankInfo.getUserId());
		sp.setString(vipRankInfo.getUsername());
		sp.setInt(vipRankInfo.getVipLevel());
		jdbc.update(sql, sp);
	}

	@Override
	public void updateStoryRank(ProcessRankInfo processRankInfo) {
		String sql = "REPLACE INTO story_rank_info (user_id,username,vip,proce) VALUES(?,?,?,?)";
		SqlParameter sp = new SqlParameter();
		sp.setString(processRankInfo.getUserId());
		sp.setString(processRankInfo.getUsername());
		sp.setInt(processRankInfo.getVip());
		sp.setDouble(processRankInfo.getProce());
		jdbc.update(sql, sp);

	}

	@Override
	public void updateEliteRank(ProcessRankInfo processRankInfo) {
		String sql = "REPLACE INTO elite_rank_info (user_id,username,vip,proce) VALUES(?,?,?,?)";
		SqlParameter sp = new SqlParameter();
		sp.setString(processRankInfo.getUserId());
		sp.setString(processRankInfo.getUsername());
		sp.setInt(processRankInfo.getVip());
		sp.setDouble(processRankInfo.getProce());
		jdbc.update(sql, sp);

	}

	@Override
	public void updateResourceStarRank(StarRankInfo starRankInfo) {
		String sql = "REPLACE INTO resource_rank_info (user_id,username,vip,star) VALUES(?,?,?,?)";
		SqlParameter sp = new SqlParameter();
		sp.setString(starRankInfo.getUserId());
		sp.setString(starRankInfo.getUsername());
		sp.setInt(starRankInfo.getVip());
		sp.setInt(starRankInfo.getStar());
		jdbc.update(sql, sp);

	}

	@Override
	public void updateEctypeStarRank(StarRankInfo starRankInfo) {
		String sql = "REPLACE INTO ectype_rank_info (user_id,username,vip,star) VALUES(?,?,?,?)";
		SqlParameter sp = new SqlParameter();
		sp.setString(starRankInfo.getUserId());
		sp.setString(starRankInfo.getUsername());
		sp.setInt(starRankInfo.getVip());
		sp.setInt(starRankInfo.getStar());
		jdbc.update(sql, sp);
	}

	public int getResourceStarByUserId(String userId) {
		String sql = "select star from resource_rank_info where user_id= ?";
		SqlParameter sp = new SqlParameter();
		sp.setString(userId);
		return jdbc.getInt(sql, sp);
	}

	public int getEctypeStarByUserId(String userId) {
		String sql = "select star from ectype_rank_info where user_id= ?";
		SqlParameter sp = new SqlParameter();
		sp.setString(userId);
		return jdbc.getInt(sql, sp);
	}

	@Override
	public void updatePayRank(PayRankInfo payRankInfo) {
		String sql = "REPLACE INTO pay_rank_info (user_id,username,vip,pay) VALUES(?,?,?,?)";
		SqlParameter sp = new SqlParameter();
		sp.setString(payRankInfo.getUserId());
		sp.setString(payRankInfo.getUsername());
		sp.setInt(payRankInfo.getVip());
		sp.setInt(payRankInfo.getPay());
		jdbc.update(sql, sp);

	}

	@Override
	public void cleanResourceStarRank() {
		String sql = "truncate table resource_rank_info";
		SqlParameter sp = new SqlParameter();
		this.jdbc.update(sql, sp);
	}

}
