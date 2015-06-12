package com.lodogame.ldsg.remote;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.ContestDao;
import com.lodogame.game.remote.action.BaseAction;
import com.lodogame.game.remote.request.Request;
import com.lodogame.game.remote.response.Response;
import com.lodogame.ldsg.bo.BattleBO;
import com.lodogame.ldsg.handler.ContestPushHandler;
import com.lodogame.ldsg.service.ContestService;
import com.lodogame.model.ContestBattleReport;
import com.lodogame.model.ContestUser;

/**
 * 跨服战远程调用接口
 * 
 * @author shixiangwen
 * 
 */

public class ContestRemote extends BaseAction {

	private static final Logger logger = Logger.getLogger(ContestRemote.class);

	@Autowired
	public ContestService contestService;

	@Autowired
	public ContestDao contestDao;

	@Autowired
	public ContestPushHandler contestPushHandler;

	/**
	 * 获取参赛的用户列表
	 * 
	 * @param req
	 * @return
	 */
	public Response getBattleBO(Request req) {

		String userId = req.get("userId", "");
		int force = req.get("force", 0);

		logger.debug("get battle bo[" + userId + "]");

		Response resposne = new Response();

		BattleBO battleBO = this.contestService.getBattleBO(userId, force == 1);

		resposne.put("battleBO", battleBO);

		return resposne;

	}

	/**
	 * 跨服开始
	 * 
	 * @return
	 */
	public Response worldStart(Request req) {

		logger.debug("worldStart");

		Response resposne = new Response();

		this.contestService.worldStart();

		return resposne;

	}

	public Response cleanFinishTeam(Request req) {

		logger.debug("cleanFinishTeam");

		Response resposne = new Response();

		this.contestService.cleanFinishTeam();

		return resposne;

	}

	/**
	 * 发送消息
	 * 
	 * @return
	 */
	public Response sendMsg(Request req) {

		logger.debug("sendMsg");

		Response resposne = new Response();

		String username = req.get("username", "");
		String title1 = req.get("title1", "");
		String title2 = req.get("title2", "");

		this.contestService.sendMsg(username, title1, title2);

		return resposne;

	}

	/**
	 * 发送在线奖励
	 * 
	 * @return
	 */
	public Response sendOnlineReward(Request req) {

		logger.debug("sendOnlineReward");

		Response resposne = new Response();

		this.contestService.sendOnlineReward();

		return resposne;

	}

	/**
	 * 发送奖励
	 * 
	 * @return
	 */
	public Response sendReward(Request req) {

		logger.debug("sendReward");

		Response resposne = new Response();
		String userId = req.get("userId", "");
		int rank = req.get("rank", 0);

		this.contestService.sendReward(userId, rank);
		return resposne;

	}

	/**
	 * 状态变化通知
	 * 
	 * @param req
	 * @return
	 */
	public Response notifyStatus(Request req) {

		logger.debug("notifyStatus");

		Response resposne = new Response();

		int clientStatus = req.get("status", 0);

		this.contestService.pushStatus(clientStatus);

		return resposne;

	}

	/**
	 * 获取用户信息
	 * 
	 * @param req
	 * @return
	 */
	public Response getHeros(Request req) {

		logger.debug("getHeros");

		String userId = req.get("uid", "");

		Response resposne = new Response();

		ContestUser contestUser = this.contestDao.get(userId);
		if (contestUser != null) {
			resposne.put("lv", contestUser.getLevel());
			resposne.put("vip", contestUser.getVip());
			resposne.put("name", contestUser.getUsername());
		}

		resposne.put("list", this.contestService.getHeros(userId));

		return resposne;

	}

	/**
	 * 推送战斗
	 * 
	 * @param req
	 * @return
	 */
	public Response pushBattle(Request req) {

		String userId = req.get("uid", "");

		logger.debug("pushBattle.userId[" + userId + "]");

		ContestBattleReport report = new ContestBattleReport();
		report.setAttackUserId(req.get("attackUserId", ""));
		report.setAttackUsername(req.get("attackUsername", ""));
		report.setBaseCode(req.get("baseCode", ""));
		report.setDefenseUserId(req.get("defenseUserId", ""));
		report.setDefenseUsername(req.get("defenseUsername", ""));
		report.setFlag(req.get("flag", 1));
		report.setReport(req.get("report", ""));
		report.setWinInfo(req.get("winInfo", ""));

		contestPushHandler.pushBattle(userId, report);

		Response resposne = new Response();

		return resposne;
	}
}
