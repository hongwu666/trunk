package com.lodogame.game.remote.handle;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.lodogame.game.remote.action.Action;
import com.lodogame.game.remote.callback.Callback;
import com.lodogame.game.remote.constants.StatusConstants;
import com.lodogame.game.remote.factory.BeanFactory;
import com.lodogame.game.remote.request.Request;
import com.lodogame.game.remote.response.Response;
import com.lodogame.game.remote.stub.StubFactory;
import com.lodogame.game.utils.IDGenerator;
import com.lodogame.game.utils.json.Json;

public class RemoteCallHandleImpl implements RemoteCallHandle {

	private static final Logger logger = Logger.getLogger(RemoteCallHandleImpl.class);

	@Autowired
	private JedisPool pool;

	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;

	private String sid;

	public void setSid(String sid) {
		this.sid = sid;
	}

	@Override
	public void call(Request req, Callback callback) {
		this.call("world", req, callback);
	}

	@Override
	public void call(String serverId, Request req, Callback callback) {
		String id = IDGenerator.getID();
		req.setId(id);
		req.put("sid", sid);
		req.setCallbackKey(this.getQueueKey(sid, "response"));
		StubFactory.getInstance().push(id, callback);
		pushMsg(getQueueKey(serverId, "request"), Json.toJson(req));
	}

	/**
	 * 获取各种队列的key
	 * 
	 * @param type
	 * @return
	 */
	private String getQueueKey(String sid, String tag) {
		return sid + "." + tag + ".pool.key";
	}

	@Override
	public Response handle(Request req) {

		String handlerName = req.getAction();
		String methodName = req.getMethod();

		Response response = new Response();

		if (BeanFactory.getInstance().containsBean(handlerName)) {
			Action action = (Action) BeanFactory.getInstance().getBean(handlerName);

			Method m = null;
			try {
				m = action.getClass().getMethod(methodName, Request.class);
			} catch (NoSuchMethodException nme) {
				response.setRc(StatusConstants.METHOD_NOT_FOUND);
				response.setMessage("action not found[" + handlerName + "." + methodName + "]");
				logger.error(nme.getMessage(), nme);
			}

			try {
				if (m != null) {
					response = (Response) m.invoke(action, new Object[] { req });
					if (response == null) {
						return null;
					}
				}
			} catch (Exception e) {
				response.setRc(StatusConstants.UNKNOW_ERROR);
				response.setMessage(e.getMessage());
				logger.error(e.getMessage(), e);
			}
		} else {
			response.setRc(StatusConstants.METHOD_NOT_FOUND);
			response.setMessage("action not found[" + handlerName + "." + methodName + "]");
		}

		response.setId(req.getId());

		return response;

	}

	public void handleResponse(Response resp) {

		String id = resp.getId();
		Callback callback = StubFactory.getInstance().get(id);
		if (callback != null) {
			callback.handle(resp);
		}
	}

	private String blockPopMsg(String key) {
		Jedis jedis = pool.getResource();
		try {
			List<String> ret = jedis.blpop(2, key);
			if (ret != null) {
				return ret.get(1);
			}
			return null;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			pool.returnResource(jedis);
		}
	}

	private void pushMsg(String key, String val) {

		Jedis jedis = pool.getResource();
		try {
			jedis.lpush(key, val);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			pool.returnResource(jedis);
		}

	}

	@Override
	public void asynResponse(Request req, Response res) {

		res.setId(req.getId());
		this.pushMsg(req.getCallbackKey(), Json.toJson(res));

	}

	private void checkRequest() {

		try {

			String json = blockPopMsg(this.getQueueKey(sid, "request"));
			if (json == null) {
				return;
			}

			final Request req = Json.toObject(json, Request.class);

			Runnable task = new Runnable() {

				@Override
				public void run() {

					Response response = handle(req);
					if (response != null) {
						try {
							pushMsg(req.getCallbackKey(), Json.toJson(response));
						} catch (Exception e) {
							logger.error(e.getMessage(), e);
						}
					}

				}
			};

			taskExecutor.execute(task);

		} catch (Throwable t) {
			logger.error(t.getMessage(), t);
			try {
				Thread.sleep(500);
			} catch (InterruptedException ie) {
				logger.error(ie.getMessage());
			}
		}
	}

	private void checkResponse() {

		try {

			String json = blockPopMsg(this.getQueueKey(sid, "response"));
			if (json == null) {
				// logger.info("当前没有响应");
				return;
			}

			Response res = Json.toObject(json, Response.class);

			if (res.getRc() != StatusConstants.OK) {
				logger.error("调用返回错误.rc[" + res.getRc() + "], message[" + res.getMessage() + "]");
			}

			this.handleResponse(res);

		} catch (Throwable t) {
			logger.error(t.getMessage(), t);
			try {
				Thread.sleep(500);
			} catch (InterruptedException ie) {
				logger.error(ie.getMessage());
			}
		}
	}

	@Override
	public void init() {

		new Thread(new Runnable() {

			public void run() {
				while (true) {
					checkRequest();
				}
			}

		}).start();

		new Thread(new Runnable() {

			public void run() {
				while (true) {
					checkResponse();
				}
			}

		}).start();
	}

}
