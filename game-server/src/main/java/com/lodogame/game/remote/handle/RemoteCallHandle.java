package com.lodogame.game.remote.handle;

import com.lodogame.game.remote.callback.Callback;
import com.lodogame.game.remote.request.Request;
import com.lodogame.game.remote.response.Response;

public interface RemoteCallHandle {

	/**
	 * 处理一个请求
	 * 
	 * @param req
	 */
	public Response handle(Request req);

	/**
	 * 调用跨服服务器接口
	 * 
	 * @param req
	 * @param callback
	 */
	public void call(Request req, Callback callback);

	/**
	 * 调用游戏服服务器接口口
	 * 
	 * @param serverId
	 * @param req
	 * @param callback
	 */
	public void call(String serverId, Request req, Callback callback);

	/**
	 * 异步响应
	 * 
	 * @param req
	 * @param res
	 */
	public void asynResponse(Request req, Response res);

	/**
	 * init
	 */
	public void init();
}
