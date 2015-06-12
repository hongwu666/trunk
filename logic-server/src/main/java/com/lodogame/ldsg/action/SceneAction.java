package com.lodogame.ldsg.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.SystemForcesDao;
import com.lodogame.game.dao.UserForcesDao;
import com.lodogame.game.server.response.Response;
import com.lodogame.ldsg.bo.ForcesStarRewardBO;
import com.lodogame.ldsg.bo.SweepInfoBO;
import com.lodogame.ldsg.bo.UserForcesBO;
import com.lodogame.ldsg.bo.UserSceneBO;
import com.lodogame.ldsg.constants.PriceType;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.handler.PushHandler;
import com.lodogame.ldsg.service.ForcesService;
import com.lodogame.ldsg.service.PriceService;
import com.lodogame.ldsg.service.SceneService;
import com.lodogame.model.UserSweepInfo;

/**
 * 关卡相关action
 * 
 * @author jacky
 * 
 */

public class SceneAction extends LogicRequestAction {

	private static final Logger LOG = Logger.getLogger(SceneAction.class);

	@Autowired
	private SceneService sceneService;

	@Autowired
	private ForcesService forcesService;

	@Autowired
	private PushHandler pushHandler;

	@Autowired
	private UserForcesDao userForcesDao;

	@Autowired
	private PriceService priceService;

	@Autowired
	private SystemForcesDao systemForcesDao;

	/**
	 * 读取用户场景
	 */
	public Response loadScenes() {

		LOG.debug("获取用户场景列表.uid[" + getUid() + "]");

		List<UserSceneBO> userSceneList = this.sceneService.getUserSceneList(getUid());

		set("us", userSceneList);

		return this.render();
	}

	/**
	 * 读取用户场景怪物列表
	 */
	public Response loadForces() {

		List<UserForcesBO> rt = new ArrayList<UserForcesBO>();
		List<UserForcesBO> userForcesBoList = this.forcesService.getUserForcesList(getUid(), 0);
		for (UserForcesBO userForcesBO : userForcesBoList) {
			if (userForcesBO.getTimes() > 0) {
				rt.add(userForcesBO);
			}
		}
		set("fs", rt);

		return this.render();
	}

	/**
	 * 突击
	 * 
	 * @return
	 */
	public Response assault() {
		Integer sid = (Integer) this.request.getParameter("sid");
		sceneService.assault(getUid(), sid, new EventHandle() {
			@Override
			public boolean handle(Event event) {
				set("dr", event.getObject("forcesDropBO"));
				pushHandler.pushUser(getUid());
				return true;
			}
		});
		return this.render();
	}

	/**
	 * 重置剧情
	 * 
	 * @return
	 */
	public Response resetForcesTimes() {

		String userId = this.getUid();

		int groupId = this.getInt("fid", 0);

		this.forcesService.resetForcesTimes(userId, groupId);

		this.pushHandler.pushUser(userId);

		int userDayResetForcesTimes = this.userForcesDao.getUserResetTimes(userId, groupId);

		int price = this.priceService.getPrice(PriceType.RESET_FORCES_TIMES, userDayResetForcesTimes + 1);
		set("frsm", price);

		return this.render();

	}

	/**
	 * 领取过图奖励
	 * 
	 * @return
	 */
	public Response drawForcesStarReward() {

		Integer sceneId = (Integer) this.request.getParameter("sid");
		Integer passStar = (Integer) this.request.getParameter("sn");
		int type = getInt("tp", 1); // 1 普通 2 精英;
		Map<String, Object> map = this.sceneService.drawForcesStarReward(getUid(), sceneId, passStar, type);

		pushHandler.pushUser(getUid());

		set("dr", map.get("dr"));
		set("fsr", map.get("fsr"));

		return this.render();

	}

	/**
	 * 读取关卡星级奖励
	 * 
	 * @return
	 */
	public Response getForcesStarReward() {

		int sceneId = getInt("sid", 0);
		ForcesStarRewardBO rewardBO = this.sceneService.getForcesStarReward(sceneId, getUid());

		set("fsr", rewardBO);

		return this.render();

	}

	/**
	 * 开始扫荡
	 * 
	 * @return
	 */
	public Response sweep() {
		Integer groupForcesId = (Integer) this.request.getParameter("gfid");
		final int times = getInt("ts", 10);

		if (times <= 0) {
			throw new ServiceException(ServiceReturnCode.FAILD, "数据异常");
		}

		sceneService.sweep(getUid(), groupForcesId, times, new EventHandle() {
			@Override
			public boolean handle(Event event) {
				UserSweepInfo sweepInfo = (UserSweepInfo) event.getObject("sweepInfo");
				set("et", sweepInfo.getEndTime());
				set("ts", times);
				set("dr", event.getObject("commonDropBO"));
				set("st", 0);
				pushHandler.pushUser(getUid());
				return true;
			}
		});

		return this.render();
	}

	/**
	 * 获取用户扫荡状态
	 * 
	 * @return
	 */
	public Response getSweepStatus() {
		SweepInfoBO bo = sceneService.getUserSweepInfo(getUid());
		set("st", bo.getStatus());
		set("et", bo.getEndTime());
		return this.render();
	}

	/**
	 * 获取消除扫荡时间需要消耗的金币
	 * 
	 * @return
	 */
	public Response getClearCDGold() {
		int gold = sceneService.getClearCDGold(getUid());
		set("gold", gold);
		return this.render();
	}

	/**
	 * 立刻完成扫荡
	 * 
	 * @return
	 */
	public Response clearCDTime() {
		sceneService.speedUpSweep(getUid());
		pushHandler.pushUser(getUid());
		return this.render();
	}

}
