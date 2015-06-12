package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.SystemHeroUpgradeToolDao;
import com.lodogame.model.SystemHeroUpgradeTool;

public class SystemHeroUpgradeToolDaoMysqlImpl implements SystemHeroUpgradeToolDao {

	public final static String table = "system_hero_upgrade_tool";

	public final static String columns = "tool_type, tool_id, tool_num";

	@Autowired
	private Jdbc jdbc;

	@Override
	public List<SystemHeroUpgradeTool> get(int nodeId) {
		String sql = "SELECT * FROM " + table + " WHERE node_id = ?";
		SqlParameter param = new SqlParameter();
		param.setInt(nodeId);
		return this.jdbc.getList(sql, SystemHeroUpgradeTool.class, param);
	}



}
