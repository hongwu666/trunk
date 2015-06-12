package com.lodogame.game.dao.impl.mysql;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.PkAwardDao;
import com.lodogame.model.PkAward;
import com.lodogame.model.PkGiftDay;
import com.lodogame.model.PkRankUpGift;

public class PkAwardDaoMysqlImpl implements PkAwardDao {

	@Autowired
	private Jdbc jdbc;

	public final static String table = "pk_award";

	public final static String columns = "*";

	@Override
	public List<PkAward> getAll() {
		String sql = "select " + columns + " from " + table + " ";
		return jdbc.getList(sql, PkAward.class);
	}

	@Override
	public PkAward getById(int awardId) {
		String sql = "select " + columns + " from " + table + " where award_id = ?";
		SqlParameter param = new SqlParameter();
		param.setInt(awardId);
		return jdbc.get(sql, PkAward.class, param);
	}

	@Override
	public boolean isAwardSended(String date) {

		String sql = "SELECT pk_award_log_id FROM pk_award_log WHERE date = ? ";
		SqlParameter param = new SqlParameter();
		param.setString(date);

		return this.jdbc.getInt(sql, param) > 0;
	}

	@Override
	public boolean addAwardSendLog(String date) {

		String sql = "INSERT INTO pk_award_log(date, created_time) VALUES(?, ?)";
		SqlParameter param = new SqlParameter();
		param.setString(date);
		param.setObject(new Date());

		return this.jdbc.update(sql, param) > 0;
	}
	

	@Override
	public List<PkGiftDay> getDayGift() {
		String sql = "select * from pk_rank_score";
		return jdbc.getList(sql, PkGiftDay.class);
	}

	@Override
	public List<PkRankUpGift> getUpGifts() {
		String sql = "select * from pk_rank_up_gift";
		return jdbc.getList(sql,PkRankUpGift.class);
	}

	@Override
	public int getUpGift(int old, int news) {
		return 0;
	}

}
