package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.TreasureDao;
import com.lodogame.model.TreasureConfigInfo;
import com.lodogame.model.TreasureUserInfo;

public class TreasureDaoMysqlImpl implements TreasureDao {

	@Autowired
	private Jdbc jdbc;

	public List<TreasureConfigInfo> getConfig() {

		String sql = "select * from treasure_config_info";
		return jdbc.getList(sql, TreasureConfigInfo.class);

	}

	public List<TreasureUserInfo> getUserInfo(String userId, String date) {

		String sql = "select * from treasure_user_info where user_id=? and date =?";
		SqlParameter sp = new SqlParameter();
		sp.setString(userId);
		sp.setString(date);
		return jdbc.getList(sql, TreasureUserInfo.class, sp);

	}

	public void updateUserInfo(TreasureUserInfo info) {

		String sql = "UPDATE treasure_user_info SET treasure_num=? WHERE user_id=? AND treasure_type=? AND date = ? ";
		SqlParameter sp = new SqlParameter();
		sp.setInt(info.getTreasureNum());
		sp.setString(info.getUserId());
		sp.setInt(info.getTreasureType());
		sp.setString(info.getDate());
		jdbc.update(sql, sp);

	}

	public void insertUserInfo(TreasureUserInfo info) {
		String sql = "insert into treasure_user_info(user_id ,treasure_type ,treasure_num, date, created_time) values(?,?,?,?,?)";
		SqlParameter sp = new SqlParameter();
		sp.setString(info.getUserId());
		sp.setInt(info.getTreasureType());
		sp.setInt(info.getTreasureNum());
		sp.setString(info.getDate());
		sp.setDate(info.getCreatedTime());

		jdbc.update(sql, sp);
	}

	public TreasureConfigInfo getConfigByGrade(int type) {
		return null;
	}

	public void replace() {

	}

}
