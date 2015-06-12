package com.lodogame.ldsg.handler.log;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.LogDao;
import com.lodogame.ldsg.handler.LogHandle;
import com.lodogame.ldsg.handler.LogHandlerFactory;
import com.lodogame.model.log.GoldLog;
import com.lodogame.model.log.ILog;

public class GoldLogHandlerImpl implements LogHandle {

	@Autowired
	private LogDao logDao;

	@Override
	public void handle(ILog log) {
		GoldLog goldLog = (GoldLog) log;
		logDao.goldLog(goldLog.getUserId(), goldLog.getUseType(), goldLog.getAmount(), goldLog.getFlag(), true, goldLog.getBeforeAmount(), goldLog.getAfterAmount());
	}

	@Override
	public void init() {
		LogHandlerFactory.getInstance().register(GoldLog.class, this);
	}

}
