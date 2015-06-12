package com.lodogame.ldsg.handler.log;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.LogDao;
import com.lodogame.ldsg.handler.LogHandle;
import com.lodogame.ldsg.handler.LogHandlerFactory;
import com.lodogame.model.log.CopperLog;
import com.lodogame.model.log.ILog;

public class CopperLogHandlerImpl implements LogHandle {

	@Autowired
	private LogDao logDao;

	@Override
	public void handle(ILog log) {
		CopperLog copperLog = (CopperLog) log;
		logDao.copperLog(copperLog.getUserId(), copperLog.getUseType(), copperLog.getAmount(), copperLog.getFlag(), true);
	}

	@Override
	public void init() {
		LogHandlerFactory.getInstance().register(CopperLog.class, this);
	}

}
