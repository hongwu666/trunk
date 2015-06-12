package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.RobotDao;
import com.lodogame.model.RobotUser;
import com.lodogame.model.RobotUserHero;

public class RobotDaoMysqlImpl implements RobotDao {

	@Autowired
	private Jdbc jdbc;

	@Override
	public boolean insert(List<RobotUserHero> robotUserHeroList) {
		this.jdbc.insert(robotUserHeroList);
		return true;
	}

	@Override
	public boolean insertUser(List<RobotUser> userList) {
		this.jdbc.insert(userList);
		return true;
	}

	@Override
	public RobotUser getUser(long lodoId) {
		String sql = "SELECT * FROM robot_user WHERE lodo_id = ? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setLong(lodoId);
		return this.jdbc.get(sql, RobotUser.class, parameter);
	}

	@Override
	public RobotUser getByName(String username) {
		String sql = "SELECT * FROM robot_user WHERE username = ? LIMIT 1";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(username);
		return this.jdbc.get(sql, RobotUser.class, parameter);
	}

	@Override
	public RobotUser getRobotUserByLevel(int level) {
		String sql = "SELECT * FROM robot_user WHERE level = ? LIMIT 1";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(level);
		return this.jdbc.get(sql, RobotUser.class, parameter);
	}

	@Override
	public int getMaxLodoId() {
		String sql = "SELECT MAX(lodo_id) FROM robot_user ";
		SqlParameter parameter = new SqlParameter();
		return this.jdbc.getInt(sql, parameter);
	}

	@Override
	public List<RobotUser> getByCapability(int minCapability, int maxCapability) {
		String sql = "SELECT * FROM robot_user WHERE capability >= ? AND capability <= ? LIMIT 200";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(minCapability);
		parameter.setInt(maxCapability);
		return this.jdbc.getList(sql, RobotUser.class, parameter);
	}

	@Override
	public int count() {
		String sql = "SELECT count(1) as total FROM robot_user";
		SqlParameter parameter = new SqlParameter();
		return this.jdbc.getInt(sql, parameter);
	}

	@Override
	public RobotUser getUser(String userId) {
		String sql = "SELECT * FROM robot_user WHERE user_id = ? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		return this.jdbc.get(sql, RobotUser.class, parameter);
	}

	@Override
	public List<RobotUserHero> getRobotUserHero(String userId) {
		String sql = "SELECT * FROM robot_user_hero WHERE user_id = ? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		return this.jdbc.getList(sql, RobotUserHero.class, parameter);
	}

}
