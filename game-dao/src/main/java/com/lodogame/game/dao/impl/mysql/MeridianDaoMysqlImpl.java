package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.MeridianDao;
import com.lodogame.game.utils.TableUtils;
import com.lodogame.model.MeridianUser;
import com.lodogame.model.MeridianUserInfo;

public class MeridianDaoMysqlImpl implements MeridianDao {
	@Autowired
	private Jdbc jdbc;

	public List<MeridianUserInfo> getMeridianUserInfo(String userId) {
		String sql = "select * from " + TableUtils.getUserMeridianTable(userId) + " where user_id=?";
		SqlParameter sp = new SqlParameter();
		sp.setString(userId);
		return jdbc.getList(sql, MeridianUserInfo.class, sp);
	}

	public void insertMeridianUserInfo(MeridianUserInfo info) {
		String sql = "insert into " + TableUtils.getUserMeridianTable(info.getUserId()) + " values(?,?,?,?,?,?,?)";
		SqlParameter sp = new SqlParameter();
		sp.setString(info.getUserId());
		sp.setInt(info.getMeridianType());
		sp.setInt(info.getMeridianNode());
		sp.setString(info.getUserHeroId());
		sp.setInt(info.getLuck());
		sp.setInt(info.getExp());
		sp.setInt(info.getLevel());
		jdbc.update(sql, sp);
	}

	public void updateMeridianUserInfo(MeridianUserInfo info) {
		String sql = "update " + TableUtils.getUserMeridianTable(info.getUserId())
				+ " set luck=?,exp=?,level=? where user_id=? and meridian_type=? and meridian_node=? and user_hero_id=?";
		SqlParameter sp = new SqlParameter();
		sp.setInt(info.getLuck());
		sp.setInt(info.getExp());
		sp.setInt(info.getLevel());
		sp.setString(info.getUserId());
		sp.setInt(info.getMeridianType());
		sp.setInt(info.getMeridianNode());
		sp.setString(info.getUserHeroId());
		jdbc.update(sql, sp);
	}

	@Override
	public MeridianUser getUserMeridian(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

}
