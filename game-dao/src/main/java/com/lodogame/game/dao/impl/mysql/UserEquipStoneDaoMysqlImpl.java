package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserEquipStoneDao;
import com.lodogame.model.UserEquipStone;

public class UserEquipStoneDaoMysqlImpl implements UserEquipStoneDao {

	@Autowired
	private Jdbc jdbc;

	private String table = "user_equip_stone";

	@Override
	public List<UserEquipStone> getUserEquipStone(String userEquipId) {
		String sql = "SELECT * FROM " + table + " WHERE user_equip_id = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userEquipId);
		return this.jdbc.getList(sql, UserEquipStone.class, parameter);
	}

	@Override
	public UserEquipStone getUserEquipStone(String userEquipId, int pos) {
		String sql = "SELECT * FROM " + table + " WHERE user_equip_id = ? and pos = ? LIMIT 1 ";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userEquipId);
		parameter.setInt(pos);
		return this.jdbc.get(sql, UserEquipStone.class, parameter);
	}

	@Override
	public boolean insertUserEquipStone(UserEquipStone userEquipStone) {
		return this.jdbc.insert(userEquipStone) > 0;
	}

	@Override
	public boolean delUserEquipStone(String userEquipId, int pos) {
		String sql = "DELETE  FROM " + table + " WHERE user_equip_id = ? and pos = ?  ";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userEquipId);
		parameter.setInt(pos);
		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean delUserEquipStone(String userEquipId) {
		String sql = "DELETE  FROM " + table + " WHERE user_equip_id = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userEquipId);
		return this.jdbc.update(sql, parameter) > 0;
	}

}
