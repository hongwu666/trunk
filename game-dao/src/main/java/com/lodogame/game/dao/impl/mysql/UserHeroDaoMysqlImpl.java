package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserHeroDao;
import com.lodogame.game.dao.asyncdb.AsyncDbModel;
import com.lodogame.game.dao.asyncdb.AsyncDbPool;
import com.lodogame.game.utils.SqlUtil;
import com.lodogame.game.utils.TableUtils;
import com.lodogame.model.UserHero;

public class UserHeroDaoMysqlImpl implements UserHeroDao {

	/**
	 * 表名
	 */
	public final static String table = "user_hero";

	/**
	 * 字段列表
	 */
	public final static String columns = "*";

	@Autowired
	private Jdbc jdbc;

	public List<UserHero> getUserHeroList(String userId) {

		String table = TableUtils.getUserHeroTable(userId);

		String sql = "SELECT " + columns + " FROM " + table + " WHERE user_id = ? ORDER BY hero_level DESC LIMIT 800";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);

		return this.jdbc.getList(sql, UserHero.class, parameter);
	}

	@Override
	public List<UserHero> getUserHeroList(String userId, int systemHeroId) {
		throw new NotImplementedException();
	}

	public boolean addUserHero(UserHero userHero) {
		String table = TableUtils.getUserHeroTable(userHero.getUserId());

		return this.jdbc.insert(table, userHero) > 0;
	}

	public UserHero getUserHeroByPos(String userId, int pos) {
		String table = TableUtils.getUserHeroTable(userId);

		String sql = "SELECT " + columns + " FROM " + table + " WHERE user_id = ? and pos = ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(pos);
		return this.jdbc.get(sql, UserHero.class, parameter);
	}

	@Override
	public int getUserHeroCount(String userId) {
		throw new NotImplementedException();
	}

	public boolean changePos(String userId, String userHeroId, int pos) {
		String table = TableUtils.getUserHeroTable(userId);

		String sql = "UPDATE " + table + " SET pos = ? WHERE user_id = ? AND user_hero_id = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(pos);
		parameter.setString(userId);
		parameter.setString(userHeroId);

		return this.jdbc.update(sql, parameter) > 0;
	}

	public boolean changeSystemHeroId(String userId, String userHeroId, int systemHeroId) {
		String table = TableUtils.getUserHeroTable(userId);

		String sql = "UPDATE " + table + " SET system_hero_id = ? WHERE user_id = ? AND user_hero_id = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(systemHeroId);
		parameter.setString(userId);
		parameter.setString(userHeroId);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean update(String userId, String userHeroId, int systemHeroId, int level, int exp) {
		String table = TableUtils.getUserHeroTable(userId);

		String sql = "UPDATE " + table + " SET system_hero_id = ?, hero_level = ? , hero_exp = ? WHERE user_id = ? AND user_hero_id = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(systemHeroId);
		parameter.setInt(level);
		parameter.setInt(exp);
		parameter.setString(userId);
		parameter.setString(userHeroId);

		return this.jdbc.update(sql, parameter) > 0;
	}

	public boolean delete(String userId, String userHeroId) {
		String table = TableUtils.getUserHeroTable(userId);

		String sql = "DELETE FROM " + table + " WHERE user_id = ? AND user_hero_id = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setString(userHeroId);

		return this.jdbc.update(sql, parameter) > 0;

	}

	public int delete(String userId, List<String> userHeroIdList) {

		String ids = SqlUtil.join(userHeroIdList);
		String table = TableUtils.getUserHeroTable(userId);

		String sql = "DELETE FROM " + table + " WHERE user_id = ? AND user_hero_id in (" + ids + ") ";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);

		return this.jdbc.update(sql, parameter);

	}

	public boolean updateExpLevel(String userId, String userHeroId, int exp, int level) {
		String table = TableUtils.getUserHeroTable(userId);

		String sql = "UPDATE " + table + " SET hero_exp = ?, hero_level = ? , updated_time = now() WHERE user_id = ? AND user_hero_id = ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(exp);
		parameter.setInt(level);
		parameter.setString(userId);
		parameter.setString(userHeroId);

		return this.jdbc.update(sql, parameter) > 0;
	}

	public int getBattleHeroCount(String userId) {
		throw new NotImplementedException();
	}

	@Override
	public boolean addUserHero(List<UserHero> userHeroList) {

		String table = TableUtils.getUserHeroTable(userHeroList.get(0).getUserId());

		AsyncDbModel<UserHero> model = new AsyncDbModel<UserHero>();
		model.setTable(table);
		model.setTs(userHeroList);
		AsyncDbPool.getInstance().add(model);

		return true;
	}

	@Override
	public void setUserHeroList(String userId, List<UserHero> userHeroList) {
		throw new NotImplementedException();
	}

	@Override
	public UserHero get(String userId, String userHeroId) {
		String table = TableUtils.getUserHeroTable(userId);

		String sql = "SELECT " + columns + " FROM " + table + " WHERE user_id = ? AND user_hero_id = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setString(userHeroId);

		return this.jdbc.get(sql, UserHero.class, parameter);
	}

	@Override
	public List<UserHero> listUserHeroByLevelAsc(String userId, int systemHeroId) {
		String table = TableUtils.getUserHeroTable(userId);

		String sql = "SELECT * FROM " + table + " WHERE user_id = ? AND system_hero_id = ? ORDER BY hero_level asc;";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(systemHeroId);
		return this.jdbc.getList(sql, UserHero.class, parameter);
	}

	@Override
	public boolean upgradeStage(String userId, String userHeroId) {
		String table = TableUtils.getUserHeroTable(userId);

		String sql = "UPDATE " + table + " SET blood_sacrifice_stage = blood_sacrifice_stage + 1 WHERE user_id = ? AND user_hero_id = ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setString(userHeroId);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean lockHero(String userId, String userHeroId) {
		String table = TableUtils.getUserHeroTable(userId);

		String sql = "UPDATE " + table + " SET lock_status = 1 WHERE user_id = ? AND user_hero_id = ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setString(userHeroId);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean unlockHero(String userId, String userHeroId) {
		String table = TableUtils.getUserHeroTable(userId);

		String sql = "UPDATE " + table + " SET lock_status = 0 WHERE user_id = ? AND user_hero_id = ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setString(userHeroId);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean upgradeDeifyNodeLevel(String userId, String userHeroId, int deifyNodeLevel) {
		String table = TableUtils.getUserHeroTable(userId);

		String sql = "UPDATE " + table + " SET deify_node_level = ? WHERE user_id = ? AND user_hero_id = ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(deifyNodeLevel);
		parameter.setString(userId);
		parameter.setString(userHeroId);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean updateHeroStatus(String userId, String userHeroId, int status) {
		String table = TableUtils.getUserHeroTable(userId);

		String sql = "UPDATE " + table + " SET is_dead = ? WHERE user_hero_id = ?";
		SqlParameter param = new SqlParameter();
		param.setInt(status);
		param.setString(userHeroId);
		return this.jdbc.update(sql, param) > 0;
	}

	@Override
	public void updateUpgradeNode(String userId, String userHeroId, String nodes) {
		String table = TableUtils.getUserHeroTable(userId);

		String sql = "UPDATE " + table + " SET upgrade_node = ? WHERE user_id = ? AND user_hero_id = ?";
		SqlParameter param = new SqlParameter();
		param.setString(nodes);
		param.setString(userId);
		param.setString(userHeroId);

		this.jdbc.update(sql, param);
	}

	@Override
	public void updateHeroStar(String userId, String userHeroId, int star, int exp, int newexp) {
		String table = TableUtils.getUserHeroTable(userId);
		String sql = "UPDATE " + table + " SET star_level = ? , star_exp = ?,newexp= ? WHERE user_id = ? AND user_hero_id = ?";
		SqlParameter sp = new SqlParameter();
		sp.setInt(star);
		sp.setInt(exp);
		sp.setInt(newexp);
		sp.setString(userId);
		sp.setString(userHeroId);
		jdbc.update(sql, sp);
	}
}
