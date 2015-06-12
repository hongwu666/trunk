package com.lodogame.game.dao.impl.cache;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.EmpireHistoryDao;
import com.lodogame.game.dao.impl.mysql.EmpireHistoryDaoMysqlImpl;
import com.lodogame.model.EmpireHistory;

public class EmpireHistoryDaoCacheImpl implements EmpireHistoryDao {
	
	@Autowired
	private EmpireHistoryDaoMysqlImpl empireHistoryDaoMysqlImpl;
	
	public void setEmpireHistoryDaoMysqlImpl(EmpireHistoryDaoMysqlImpl empireHistoryDaoMysqlImpl) {
		this.empireHistoryDaoMysqlImpl = empireHistoryDaoMysqlImpl;
	}

	@Override
	public void addEmpireHistory(EmpireHistory empireHistory) {
		this.empireHistoryDaoMysqlImpl.addEmpireHistory(empireHistory);
	}

	@Override
	public void deleteEmpireHistory(EmpireHistory empireHistory) {
		this.empireHistoryDaoMysqlImpl.deleteEmpireHistory(empireHistory);
	}

	@Override
	public List<EmpireHistory> getEmpireHistory(String userId) {
		return this.empireHistoryDaoMysqlImpl.getEmpireHistory(userId);
	}

}
