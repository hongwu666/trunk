package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.GiftDropDao;
import com.lodogame.model.GiftDrop;

public class GiftDropDaoMysqlImpl implements GiftDropDao {

	private Jdbc jdbcCommon;

	public void setJdbcCommon(Jdbc jdbcCommon) {
		this.jdbcCommon = jdbcCommon;
	}

	@Override
	public GiftDrop get(int giftType, int giftBagType) {

		String sql = "select * from gift_drop where gift_type = ? and gift_bag_type = ? limit 1";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(giftType);
		parameter.setInt(giftBagType);

		return jdbcCommon.get(sql, GiftDrop.class, parameter);
	}

	@Override
	public List<GiftDrop> getAllDrops() {
		String sql = "select * from gift_drop";
		return jdbcCommon.getList(sql, GiftDrop.class);
	}

}
