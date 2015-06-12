package com.lodogame.game.server.action;

import org.apache.log4j.Logger;

import com.lodogame.game.server.request.Request;
import com.lodogame.game.server.response.Response;

/**
 * RequestAction 基础类，用于实现一些基础的功能
 * 
 * @author CJ
 * 
 */
public abstract class BaseRequestAction implements RequestAction {

	protected static final Logger LOG = Logger.getLogger(BaseRequestAction.class);

	protected Request request;
	protected Response response;

	@Override
	public void init(Request request, Response response) {
		this.request = request;
		this.response = response;
	}

}
