package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.game.dao.TavernDropToolDao;
import com.lodogame.model.TavernAmendDropTool;
import com.lodogame.model.TavernDropTool;

public class TavernDropToolDaoMysqlImpl implements TavernDropToolDao {

	@Autowired
	private Jdbc jdbc;

	public List<TavernDropTool> getTavernDropToolList(int type) {
		throw new NotImplementedException();
	}

	@Override
	public List<TavernAmendDropTool> getTavernAmendDropToolList(int type) {
		throw new NotImplementedException();
	}

	@Override
	public List<TavernDropTool> getTavernDropToolList() {

		String sql = "SELECT * FROM tavern_drop_tool  ";

		return this.jdbc.getList(sql, TavernDropTool.class);
	}

	@Override
	public List<TavernAmendDropTool> getTavernAmendDropToolList() {

		String sql = "SELECT * FROM tavern_amend_drop_tool ";

		return this.jdbc.getList(sql, TavernAmendDropTool.class);
	}

}
