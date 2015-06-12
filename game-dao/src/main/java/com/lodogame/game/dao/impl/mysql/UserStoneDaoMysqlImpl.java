package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserStoneDao;
import com.lodogame.game.utils.IDGenerator;
import com.lodogame.model.UserStone;

public class UserStoneDaoMysqlImpl implements UserStoneDao {

	@Autowired
	private Jdbc jdbc;

	@Override
	public List<UserStone> getUserStoneList(String userId) {

		String sql = "select * from user_stone where user_id = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);

		return jdbc.getList(sql, UserStone.class, parameter);
	}

	@Override
	public boolean addUserStone(String userId, int stoneId, int stoneNum) {

		String sql = "INSERT INTO user_stone(user_stone_id, user_id,stone_id,stone_num) VALUES(?, ?, ?, ?) ";
		sql += "ON DUPLICATE KEY UPDATE stone_num = stone_num + VALUES(stone_num)";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(IDGenerator.getID());
		parameter.setString(userId);
		parameter.setInt(stoneId);
		parameter.setInt(stoneNum);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean reduceUserStone(String userId, int stoneId, int stoneNum) {

		String sql = "UPDATE user_stone SET stone_num = stone_num - ? WHERE user_id = ? AND stone_id = ? AND stone_num >= ? LIMIT 1";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(stoneNum);
		parameter.setString(userId);
		parameter.setInt(stoneId);
		parameter.setInt(stoneNum);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public UserStone get(String userId, int stoneId) {
		throw new NotImplementedException();
	}

	@Override
	public boolean deleteZero(String userId) {

		String sql = "DELETE FROM user_stone WHERE user_id = ? AND stone_num = 0";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);

		return jdbc.update(sql, parameter) > 0;

	}

	@Override
	public boolean initCache(String userId, List<UserStone> list) {
		throw new NotImplementedException();
	}

}
