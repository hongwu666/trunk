package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserArenaInfoDao;
import com.lodogame.model.UserArenaHero;
import com.lodogame.model.UserArenaInfo;
import com.lodogame.model.UserArenaSeriesGift;
import com.lodogame.model.UserArenaSeriesGiftInfo;

public class UserArenaInfoDaoMysqlImpl implements UserArenaInfoDao {

	@Autowired
	private Jdbc jdbc;

	@Override
	public UserArenaInfo get(String userId) {
		String sql = "select * from user_arena_info where user_id = ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);

		return jdbc.get(sql, UserArenaInfo.class, parameter);
	}

	@Override
	public boolean update(UserArenaInfo userArenaInfo) {
		String sql = "INSERT INTO user_arena_info(user_id, score,refresh_num,pk_num,user_name,user_level,win_count) VALUES(?, ?, ?, ?, ?, ?, ?) ";
		sql += "ON DUPLICATE KEY UPDATE user_id = ?,score = ?,refresh_num = ?,pk_num = ?, user_name = ?,user_level = ?, win_count = ?,buy_num=?,max_win_count=?,win_num=?";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userArenaInfo.getUserId());
		parameter.setInt(userArenaInfo.getScore());
		parameter.setInt(userArenaInfo.getRefreshNum());
		parameter.setInt(userArenaInfo.getPkNum());
		parameter.setString(userArenaInfo.getUserName());
		parameter.setInt(userArenaInfo.getUserLevel());
		parameter.setInt(userArenaInfo.getWinCount());
		parameter.setString(userArenaInfo.getUserId());
		parameter.setInt(userArenaInfo.getScore());
		parameter.setInt(userArenaInfo.getRefreshNum());
		parameter.setInt(userArenaInfo.getPkNum());
		parameter.setString(userArenaInfo.getUserName());
		parameter.setInt(userArenaInfo.getUserLevel());
		parameter.setInt(userArenaInfo.getWinCount());
		parameter.setInt(userArenaInfo.getBuyNum());
		parameter.setInt(userArenaInfo.getMaxWinCount());
		parameter.setInt(userArenaInfo.getWinNum());
		return jdbc.update(sql, parameter) > 0;
	}

	@Override
	public int getRank(String userId) {
		throw new NotImplementedException();
	}

	@Override
	public boolean setScore(String userId, int score) {
		String sql = "update user_arena_info set score = score + ? where user_id = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(score);
		parameter.setString(userId);

		return jdbc.update(sql, parameter) > 0;
	}

	@Override
	public List<String> getRangeRank(int rank) {
		throw new NotImplementedException();
	}

	@Override
	public List<UserArenaInfo> getRandByScore(int lowerScore, int upperScore, String userId) {
		String sql = "select * from user_arena_info where score >= ? and score <= ? and user_id <> ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(lowerScore);
		parameter.setInt(upperScore);
		parameter.setString(userId);

		return jdbc.getList(sql, UserArenaInfo.class, parameter);
	}

	@Override
	public List<String> getRangeRankTen() {
		throw new NotImplementedException();
	}

	@Override
	public void clear() {
		String sql = "update user_arena_info set win_count = 0,refresh_num = 0,pk_num=0,buy_num=0,max_win_count=0,win_num=0";
		jdbc.update(sql, new SqlParameter());
	}

	@Override
	public void clearScore() {
		String sql = "update user_arena_info set score = 0,max_win_count=0,win_num=0";
		jdbc.update(sql, new SqlParameter());
	}

	@Override
	public List<String> getRangeRankHun() {
		throw new NotImplementedException();
	}

	@Override
	public boolean isSendReward(String date) {
		String sql = "select count(*) from user_send_reward_log where created_time = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(date);
		return jdbc.getInt(sql, parameter) > 0;
	}

	@Override
	public boolean addSendReward(String date) {
		String sql = "insert into user_send_reward_log(created_time) values(?)";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(date);
		return jdbc.update(sql, parameter) > 0;
	}

	@Override
	public List<UserArenaInfo> getList() {
		String sql = "select * from user_arena_info order by score";

		return jdbc.getList(sql, UserArenaInfo.class);
	}

	@Override
	public boolean updateUserLevel(String userId, int level) {
		String sql = "update user_arena_info set user_level = ? where user_id = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(level);
		parameter.setString(userId);

		return jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean backup() {
		String sql = "INSERT INTO `user_arena_info_log`(`user_id`, `rank`,`score`,`refresh_num`, `pk_num`,`user_name`,`user_level`,`win_count`,`create_time`) ";
		sql += "SELECT t.`user_id`, @rownum:=@rownum+1 rank,t.`score`,t.`refresh_num`, t.`pk_num`,t.`user_name`,t.`user_level`,t.`win_count`,NOW() FROM (SELECT @rownum:=0) r, user_arena_info t ORDER BY t.score DESC;";
		return jdbc.update(sql, new SqlParameter()) > 0;
	}

	@Override
	public List<UserArenaSeriesGift> getSeriesGift(String userId) {
		String sql = "select * from user_arena_series_gift where user_id=?";
		SqlParameter sp = new SqlParameter();
		sp.setString(userId);
		return jdbc.getList(sql, UserArenaSeriesGift.class, sp);
	}

	@Override
	public void insertSeriesGift(UserArenaSeriesGift gift) {
		String sql = "insert into user_arena_series_gift(user_id,win_count,num) values(?,?,?)";
		SqlParameter sp = new SqlParameter();
		sp.setString(gift.getUserId());
		sp.setInt(gift.getWinCount());
		sp.setInt(gift.getNum());
		jdbc.update(sql, sp);
	}

	@Override
	public void updateSeriesGift(UserArenaSeriesGift gift) {
		String sql = "update user_arena_series_gift set num=? where user_id=? and win_count=?";
		SqlParameter sp = new SqlParameter();
		sp.setInt(gift.getNum());
		sp.setString(gift.getUserId());
		sp.setInt(gift.getWinCount());
		jdbc.update(sql, sp);
	}

	public void clearGift() {
		String sql = "delete from user_arena_series_gift";
		jdbc.update(sql, new SqlParameter());
	}

	@Override
	public UserArenaSeriesGiftInfo getSeriesGifts(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean insertArenaHero(List<UserArenaHero> list) {

		if (list.isEmpty()) {
			return false;
		}
		String userId = list.get(0).getUserId();

		String sql = "DELETE FROM user_arena_hero WHERE user_id = ? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		this.jdbc.update(sql, parameter);

		this.jdbc.insert(list);
		return true;
	}

	@Override
	public List<UserArenaHero> getUserArenaHero(String userId) {
		String sql = "SELECT * FROM user_arena_hero WHERE user_id = ? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		return this.jdbc.getList(sql, UserArenaHero.class, parameter);
	}

}
