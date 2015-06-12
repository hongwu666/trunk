package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserOnlyOneHeroDao;
import com.lodogame.game.utils.SqlUtil;
import com.lodogame.game.utils.TableUtils;
import com.lodogame.model.UserOnlyOneHero;

public class UserOnlyOneHeroDaoMysqlImpl implements UserOnlyOneHeroDao {

	@Autowired
	private Jdbc jdbc;

	@Override
	public List<UserOnlyOneHero> getHeros(String userId) {
		String table = TableUtils.getUserOnlyOneHeroTable(userId);
		String sql = "select * from " + table + " where user_id = ?";
		SqlParameter spm = new SqlParameter();
		spm.setString(userId);
		return this.jdbc.getList(sql, UserOnlyOneHero.class, spm);
	}

	@Override
	public List<UserOnlyOneHero> getPosHeros(String userId) {
		String table = TableUtils.getUserOnlyOneHeroTable(userId);
		String sql = "select * from " + table + " where user_id = ? and pos <> 0";
		SqlParameter spm = new SqlParameter();
		spm.setString(userId);
		return this.jdbc.getList(sql, UserOnlyOneHero.class, spm);
	}

	@Override
	public void updateHero(String userId, List<UserOnlyOneHero> heos) {
		String table = TableUtils.getUserOnlyOneHeroTable(userId);
		String sql = "update " + table + " set life = ?,pos = ? where user_id = ? and user_hero_id = ?";
		for (UserOnlyOneHero userOnlyOneHero : heos) {
			SqlParameter spm = new SqlParameter();
			spm.setInt(userOnlyOneHero.getLife());
			spm.setInt(userOnlyOneHero.getPos());
			spm.setString(userOnlyOneHero.getUserId());
			spm.setString(userOnlyOneHero.getUserHeroId());
			this.jdbc.update(sql, spm);
		}
	}

	@Override
	public void updateHero(String userId, UserOnlyOneHero heo) {
		String table = TableUtils.getUserOnlyOneHeroTable(userId);
		String sql = "update " + table + " set life = ?,pos = ?,morale = ? where user_id = ? and user_hero_id = ?";
		SqlParameter spm = new SqlParameter();
		spm.setInt(heo.getLife());
		spm.setInt(heo.getLife() == 0 ? 0 : heo.getPos());
		spm.setInt(heo.getLife() == 0 ? 0 : heo.getMorale());
		spm.setString(heo.getUserId());
		spm.setString(heo.getUserHeroId());
		this.jdbc.update(sql, spm);

	}

	@Override
	public void updateHeroPos(String userId, UserOnlyOneHero heo) {
		String table = TableUtils.getUserOnlyOneHeroTable(userId);

		if (heo.getPos() > 0) {
			String sql1 = "update " + table + " set pos = 0 where user_id = ? and pos = ?";
			SqlParameter spm1 = new SqlParameter();
			spm1.setString(heo.getUserId());
			spm1.setInt(heo.getPos());
			this.jdbc.update(sql1, spm1);
		}

		String sql = "update " + table + " set pos = ? where user_id = ? and user_hero_id = ?";
		SqlParameter spm = new SqlParameter();
		spm.setInt(heo.getPos());
		spm.setString(heo.getUserId());
		spm.setString(heo.getUserHeroId());
		this.jdbc.update(sql, spm);
	}

	@Override
	public void insertHero(String userId, List<UserOnlyOneHero> heos) {
		String table = TableUtils.getUserOnlyOneHeroTable(userId);
		this.jdbc.insert(table, heos);
	}

	@Override
	public UserOnlyOneHero getHero(String userId, String userHeroId) {
		String table = TableUtils.getUserOnlyOneHeroTable(userId);
		String sql = "select * from " + table + " where user_id = ? and user_hero_id = ?";
		SqlParameter spm = new SqlParameter();
		spm.setString(userId);
		spm.setString(userHeroId);
		return this.jdbc.get(sql, UserOnlyOneHero.class, spm);
	}

	@Override
	public void delteHero(String userId) {
		String table = TableUtils.getUserOnlyOneHeroTable(userId);
		String sql = "delete from " + table + " where user_id = ? ";
		SqlParameter spm = new SqlParameter();
		spm.setString(userId);
		this.jdbc.update(sql, spm);
	}

	@Override
	public void delteHero(String userId, String userHeroId) {
		String table = TableUtils.getUserOnlyOneHeroTable(userId);
		String sql = "delete from " + table + " where user_id = ? and user_hero_id = ?";
		SqlParameter spm = new SqlParameter();
		spm.setString(userId);
		spm.setString(userHeroId);
		this.jdbc.update(sql, spm);
	}

	@Override
	public void delteHero(String userId, List<String> userHeroIds) {

		String table = TableUtils.getUserOnlyOneHeroTable(userId);

		String ids = SqlUtil.join(userHeroIds);
		String sql = "delete from " + table + " where user_id = ? and user_hero_id in (" + ids + ") ";

		SqlParameter spm = new SqlParameter();
		spm.setString(userId);

		this.jdbc.update(sql, spm);
	}
}
