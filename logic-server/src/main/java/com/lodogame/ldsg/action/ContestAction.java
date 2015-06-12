package com.lodogame.ldsg.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.connector.ServerConnectorMgr;
import com.lodogame.game.server.response.Response;
import com.lodogame.game.server.session.Session;
import com.lodogame.ldsg.bo.ContestTopUserBO;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.service.ContestService;

/**
 * 跨服战 action
 * 
 * @author shixiangwen
 * 
 */
public class ContestAction extends LogicRequestAction {

	@Autowired
	private ContestService contestService;

	public Response enter() {

		String userId = this.getUid();

		contestService.enter(userId, new EventHandle() {

			@Override
			public boolean handle(Event event) {

				set("status", event.getInt("status"));
				set("st", event.getInt("st"));
				set("round", event.getInt("round"));
				set("rt", event.getInt("rt"));
				set("nst", event.getLong("nst"));

				set("carr", event.getObject("carr"));// 当前选择的队伍
				set("arrfn", event.getObject("arrfn"));// 已经出战过的队伍
				set("hls", event.getObject("hls"));// 英雄列表
				set("wininfo", event.getObject("wininfo"));// 输赢情况
				set("enarr", event.getObject("enarr"));// 可以出战的队伍
				set("uinfo", event.getObject("uinfo"));// 对手信息

				Session session = ServerConnectorMgr.getInstance().getServerSession("logic");
				try {
					response = render();
					session.send(response.getMessage());
				} catch (IOException e) {
					throw new ServiceException(ServiceReturnCode.FAILD, e.getMessage());
				}

				return true;
			}
		});

		return null;

	}

	/**
	 * 选择出战队伍
	 * 
	 * @return
	 */
	public Response selectTeam() {

		String userId = this.getUid();

		int team = this.getInt("team", 0);

		contestService.selectTeam(userId, team);

		return this.render();
	}

	/**
	 * 获取赛程
	 * 
	 * @return
	 */
	public Response getScheduleList() {

		this.contestService.getScheduleList(new EventHandle() {

			@Override
			public boolean handle(Event event) {

				set("list", event.getObject("list"));
				set("round", event.getObject("round"));
				set("rt", event.getObject("rt"));

				Session session = ServerConnectorMgr.getInstance().getServerSession("logic");
				try {
					response = render();
					session.send(response.getMessage());
				} catch (IOException e) {
					throw new ServiceException(ServiceReturnCode.FAILD, e.getMessage());
				}

				return true;
			}
		});

		return null;
	}

	/**
	 * 获取跨服神殿用户列表
	 * 
	 * @return
	 */
	public Response getTopUserList() {

		this.contestService.getTopUserList(new EventHandle() {

			@Override
			public boolean handle(Event event) {

				set("list", event.getObject("list"));

				Session session = ServerConnectorMgr.getInstance().getServerSession("logic");
				try {
					response = render();
					session.send(response.getMessage());
				} catch (IOException e) {
					throw new ServiceException(ServiceReturnCode.FAILD, e.getMessage());
				}

				return true;
			}
		});

		return this.render();
	}

	/**
	 * 获取对战列表
	 * 
	 * @return
	 */
	public Response getHistorys() {

		String userId = this.getUid();

		this.contestService.getHistorys(userId, new EventHandle() {

			@Override
			public boolean handle(Event event) {

				set("list", event.getObject("list"));

				Session session = ServerConnectorMgr.getInstance().getServerSession("logic");
				try {
					response = render();
					session.send(response.getMessage());
				} catch (IOException e) {
					throw new ServiceException(ServiceReturnCode.FAILD, e.getMessage());
				}

				return true;
			}
		});

		return null;
	}

	/**
	 * 显示玩家阵容
	 * 
	 * @return
	 */
	public Response showHeros() {

		String targetUserId = this.getString("tuid", "");

		this.contestService.getHeros(targetUserId, new EventHandle() {

			@Override
			public boolean handle(Event event) {

				set("vip", event.getObject("vip"));
				set("lv", event.getObject("lv"));
				set("name", event.getObject("name"));
				set("list", event.getObject("list"));

				Session session = ServerConnectorMgr.getInstance().getServerSession("logic");
				try {
					response = render();
					session.send(response.getMessage());
				} catch (IOException e) {
					throw new ServiceException(ServiceReturnCode.FAILD, e.getMessage());
				}

				return true;
			}
		});

		return null;
	}

	/**
	 * 保存阵容
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Response savePos() {

		String userId = this.getUid();

		Map<String, Integer> m1 = (Map<String, Integer>) this.getMap("m1");
		Map<String, Integer> m2 = (Map<String, Integer>) this.getMap("m2");
		Map<String, Integer> m3 = (Map<String, Integer>) this.getMap("m3");

		contestService.savePos(userId, m1, m2, m3);

		return this.render();
	}

}
