package com.lodogame.game.server.action;

import org.apache.log4j.Logger;

import com.lodogame.game.connector.ServerConnectorMgr;
import com.lodogame.game.server.response.Response;

/**
 * 用于处理服务端连接请求， 当接收到源服务器请求时，将连接保存
 * 
 * @author CJ
 * 
 */
public class ServerConnectAction extends BaseRequestAction {

	protected static final Logger LOG = Logger.getLogger(ServerConnectAction.class);

	public Response connect() {
		String serverType = (String) this.request.getParameter("serverType");
		String connectorId = (String) this.request.getParameter("connectorId");
		LOG.debug(serverType + "服务器请求连接成功，sessionId：" + request.getSession().getSid());
		ServerConnectorMgr.getInstance().registerServer(serverType, connectorId, request.getSession().getSid());
		return null;
	}

	@Override
	public Response handle() {
		return null;
	}

}
