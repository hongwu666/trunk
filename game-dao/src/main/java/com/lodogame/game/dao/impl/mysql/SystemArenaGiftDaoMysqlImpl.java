package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.SystemArenaGiftDao;
import com.lodogame.model.SystemArenaGift;

public class SystemArenaGiftDaoMysqlImpl implements SystemArenaGiftDao {

	@Autowired
	private Jdbc jdbc;
	
	@Override
	public SystemArenaGift get(int groupId, int rank) {
		String sql = "select * from system_arena_gift where group_id = ? and lower_rank <= ? and upper_rank >= ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(groupId);
		parameter.setInt(rank);
		parameter.setInt(rank);
		return jdbc.get(sql, SystemArenaGift.class, parameter);
	}

	@Override
	public List<SystemArenaGift> getList(int groupId) {
		String sql = "select * from system_arena_gift where group_id = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(groupId);
		return jdbc.getList(sql, SystemArenaGift.class, parameter);
	}

	@Override
	public int getGroupItemCount() {
		String sql = "SELECT MAX(group_id) FROM system_arena_gift";
		
		return jdbc.getInt(sql, new SqlParameter());
	}

}
