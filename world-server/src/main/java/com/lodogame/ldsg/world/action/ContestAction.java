package com.lodogame.ldsg.world.action;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.remote.action.BaseAction;
import com.lodogame.game.remote.handle.RemoteCallHandle;
import com.lodogame.game.remote.request.Request;
import com.lodogame.game.remote.response.Response;
import com.lodogame.ldsg.bo.ContestUserBO;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.world.dao.ContestDao;
import com.lodogame.ldsg.world.model.ContestUserReady;
import com.lodogame.ldsg.world.service.ContestService;

/**
 * 跨服战action
 * 
 * @author shixiangwen
 * 
 */
public class ContestAction extends BaseAction {

	private static Logger logger = Logger.getLogger(ContestAction.class);

	@Autowired
	private ContestDao contestDao;

	@Autowired
	private ContestService contestService;

	@Autowired
	private RemoteCallHandle remoteCallHandle;

	/**
	 * 服务器注册
	 * 
	 * @param req
	 * @return
	 */
	public Response reg(Request req) {

		String sid = req.get("sid", "");

		logger.debug("server reg.sid[" + sid + "]");

		contestDao.regServer(sid);

		Response response = new Response();

		return response;
	}

	/**
	 * 进入
	 * 
	 * @param req
	 * @return
	 */
	public Response enter(Request req) {

		String userId = req.get("uid", "");

		logger.debug("enter.userId[" + userId + "]");

		Map<String, Object> data = this.contestService.enter(userId);

		Response response = new Response();
		response.setData(data);

		return response;
	}

	public Response getTargetInfo(Request req) {

		Response response = new Response();

		String userId = req.get("uid", "");

		ContestUserBO bo = this.contestService.getTargetInfo(userId);

		response.put("uinfo", bo);

		return response;
	}

	/**
	 * 上报这届的参赛队伍
	 * 
	 * @param req
	 * @return
	 */
	public Response reportContestUser(Request req) {

		String sid = req.get("sid", "");

		List<ContestUserReady> list = req.getList("list", ContestUserReady.class);

		this.contestService.saveContestReadyUser(sid, list);

		Response response = new Response();

		return response;
	}

	public Response getStatus(Request req) {

		Response response = new Response();

		response.put("status", this.contestService.getStatus());

		return response;

	}

	/**
	 * 获取玩家
	 * 
	 * @param req
	 * @return
	 */
	public Response getHeros(final Request req) {

		String userId = req.get("uid", "");

		this.contestService.getHeros(userId, new EventHandle() {

			@Override
			public boolean handle(Event event) {

				Response res = new Response();
				res.put("lv", event.getInt("lv", 0));
				res.put("vip", event.getInt("vip", 0));
				res.put("name", event.getString("name"));
				res.put("list", event.getObject("list"));
				remoteCallHandle.asynResponse(req, res);

				return false;
			}
		});

		return null;
	}

	/**
	 * 获取历史记录
	 * 
	 * @param req
	 * @return
	 */
	public Response getHistorys(Request req) {

		Response response = new Response();

		String userId = req.get("uid", "");

		response.put("list", this.contestService.getHistorys(userId));

		return response;

	}

	/**
	 * 获取神殿玩家表表
	 * 
	 * @param req
	 * @return
	 */
	public Response getTopUserList(Request req) {

		Response response = new Response();

		response.put("list", this.contestService.getContestTopUserList());

		return response;

	}

	/**
	 * 获取赛程
	 * 
	 * @param req
	 * @return
	 */
	public Response getScheduleList(Request req) {

		Response response = new Response();

		response.put("list", this.contestService.getScheduleList());

		return response;

	}
}
