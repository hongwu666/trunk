package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.EmpireHistoryDao;
import com.lodogame.model.EmpireHistory;

public class EmpireHistoryDaoMysqlImpl implements EmpireHistoryDao{

	@Autowired
	private Jdbc jdbc;
	@Override
	public void addEmpireHistory(EmpireHistory empireHistory) {

		this.jdbc.insert(empireHistory);
	}

	@Override
	public void deleteEmpireHistory(EmpireHistory empireHistory) {
		
		String sql="";
		SqlParameter parameter=new SqlParameter();
		
	}

	@Override
	public List<EmpireHistory> getEmpireHistory(String userId) {
		String sql="SELECT * FROM empire_history WHERE user_id = ?";
		
		SqlParameter parameter=new SqlParameter();
		parameter.setString(userId);
		
		return this.jdbc.getList(sql, EmpireHistory.class, parameter);
	}

}
