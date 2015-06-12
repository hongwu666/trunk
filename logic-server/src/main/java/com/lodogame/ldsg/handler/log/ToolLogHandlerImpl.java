package com.lodogame.ldsg.handler.log;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.LogDao;
import com.lodogame.ldsg.handler.LogHandle;
import com.lodogame.ldsg.handler.LogHandlerFactory;
import com.lodogame.model.log.ILog;
import com.lodogame.model.log.ToolLog;

public class ToolLogHandlerImpl implements LogHandle {

	@Autowired
	private LogDao logDao;

	@Override
	public void handle(ILog log) {
		ToolLog toolLog = (ToolLog) log;
		logDao.toolLog(toolLog.getUserId(), toolLog.getToolType(), toolLog.getToolId(), toolLog.getNum(), toolLog.getUseType(), toolLog.getFlag(), toolLog.getExtinfo(), true);
	}

	@Override
	public void init() {
		LogHandlerFactory.getInstance().register(ToolLog.class, this);
	}

}
