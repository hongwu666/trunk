package com.lodogame.ldsg.action;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.server.response.Response;
import com.lodogame.ldsg.bo.ActivityCostBO;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.SystemActivityBO;
import com.lodogame.ldsg.bo.ToolExchangeCountBO;
import com.lodogame.ldsg.bo.UserActivityTaskBO;
import com.lodogame.ldsg.bo.UserActivityTaskRewardBO;
import com.lodogame.ldsg.bo.UserDrawBO;
import com.lodogame.ldsg.bo.UserLoginRewardInfoBO;
import com.lodogame.ldsg.bo.UserOnlineRewardBO;
import com.lodogame.ldsg.bo.UserPayRewardBO;
import com.lodogame.ldsg.bo.UserRecivePowerBO;
import com.lodogame.ldsg.constants.ActivityId;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.handler.PushHandler;
import com.lodogame.ldsg.service.ActivityService;
import com.lodogame.ldsg.service.ActivityTaskService;
import com.lodogame.ldsg.service.DrawService;

/**
 * 活动action
 * 
 * @author jacky
 * 
 */

public class ActivityAction extends LogicRequestAction {

	@Autowired
	private ActivityService activityService;

	@Autowired
	private ActivityTaskService activityTaskService;

	@Autowired
	private DrawService drawService;

	@Autowired
	private PushHandler pushHandler;

	private static final Logger LOG = Logger.getLogger(ActivityAction.class);

	/**
	 * 领取体力
	 * 
	 * @return
	 * @throws IOException
	 */
	public Response recivePower() throws IOException {

		LOG.debug("领取体力.userId[" + getUid() + "]");

		int type = this.getInt("sq", 1);
		this.activityService.receivePower(getUid(), type);

		return this.render();
	}

	public Response getReciveInfo() {

		LOG.debug("获取领取体力信息.userId[" + getUid() + "]");

		List<UserRecivePowerBO> userRecivePowerInfothis = this.activityService.getUserRecivePowerInfo(getUid());

		set("rpl", userRecivePowerInfothis);
		set("st", System.currentTimeMillis());

		return this.render();

	}

	public Response receiveGiftBag() {

		String userId = getUid();
		String code = this.getString("code", "");

		LOG.debug("领取礼包码礼包.userId[" + userId + "], code[" + code + "]");

		CommonDropBO commonDropBO = this.activityService.receiveGiftCodeGiftBag(userId, code, new EventHandle() {

			@Override
			public boolean handle(Event event) {

				return false;
			}
		});

		if (commonDropBO.isNeedPushUser()) {
			this.pushHandler.pushUser(userId);
		}

		set("dr", commonDropBO);

		return this.render();
	}

	public Response receiveVipGiftBag() {

		String userId = getUid();

		LOG.debug("领取VIP礼包.userId[" + userId + "]");

		CommonDropBO commonDropBO = this.activityService.receiveVipGiftBag(userId, new EventHandle() {

			@Override
			public boolean handle(Event event) {

				return false;
			}
		});

		if (commonDropBO.isNeedPushUser()) {
			this.pushHandler.pushUser(userId);
		}

		set("dr", commonDropBO);

		return this.render();
	}

	public Response receiveFirstPayGiftBag() {

		String userId = getUid();

		LOG.debug("领取首充礼包.userId[" + userId + "]");

		CommonDropBO commonDropBO = this.activityService.receiveFirstPayGiftBag(userId, new EventHandle() {

			@Override
			public boolean handle(Event event) {
				return false;
			}
		});

		if (commonDropBO.isNeedPushUser()) {
			this.pushHandler.pushUser(userId);
		}

		set("dr", commonDropBO);

		return this.render();
	}

	public Response receiveRookieGuideGiftBag() {
		int giftBagId = getInt("gbid", 0);

		CommonDropBO commonDropBO = this.activityService.receiveRookieGuideGiftBag(getUid(), giftBagId, new EventHandle() {
			@Override
			public boolean handle(Event event) {
				return false;
			}
		});

		if (commonDropBO.isNeedPushUser()) {
			this.pushHandler.pushUser(getUid());
		}

		set("dr", commonDropBO);

		return this.render();
	}

	public Response getGiftBagStatus() {

		String userId = this.getUid();
		int type = this.getInt("tp", 1);

		int status = this.activityService.getGiftBagStatus(userId, type);

		this.set("st", status);
		this.set("tp", type);

		return this.render();
	}

	/**
	 * 领取在线礼包
	 * 
	 * @return
	 */
	public Response receiveOnlineGiftBag() {

		String userId = this.getUid();

		CommonDropBO commonDropBO = this.activityService.receiveOnlineReward(userId);

		UserOnlineRewardBO userOnlineRewardBO = this.activityService.getUserOnlineRewardBO(userId);

		if (commonDropBO.isNeedPushUser()) {
			this.pushHandler.pushUser(userId);
		}

		set("dr", commonDropBO);
		set("olgb", userOnlineRewardBO);

		return this.render();

	}

	/**
	 * 查询活跃度任务列表
	 * 
	 * @return
	 */
	public Response getActivityTaskList() {

		String userId = this.getUid();

		List<UserActivityTaskBO> userActivityTaskBOList = this.activityTaskService.getUserActivityTaskListBO(userId);
		List<UserActivityTaskRewardBO> userActivityTaskRewardBOList = this.activityTaskService.getUserActivityTaskRewardBOList(userId);
		int point = this.activityTaskService.getUserActivityPoint(userId);

		set("atls", userActivityTaskBOList);
		set("rwls", userActivityTaskRewardBOList);
		set("pt", point);

		return this.render();
	}

	/**
	 * w
	 * 
	 * @return
	 */
	public Response receiveActivityReward() {

		String userId = this.getUid();
		int activityTaskRewardId = this.getInt("arid", 0);

		CommonDropBO commonDropBO = this.activityTaskService.receive(userId, activityTaskRewardId);

		if (commonDropBO.isNeedPushUser()) {
			this.pushHandler.pushUser(userId);
		}

		set("dr", commonDropBO);

		pushHandler.pushUser(userId);

		return this.render();
	}

	public Response receiveOncePayReward() {

		String userId = this.getUid();
		int aid = this.getInt("aid", 0);

		CommonDropBO commonDropBO = this.activityService.receiveOncePayReward(userId, aid);
		UserPayRewardBO userPayRewardBO = this.activityService.getUserOncePayRewardById(userId, aid);

		if (commonDropBO.isNeedPushUser()) {
			this.pushHandler.pushUser(userId);
		}

		set("dr", commonDropBO);
		set("uprl", userPayRewardBO);

		return this.render();
	}

	public Response getUserOncePayRewards() {

		String userId = this.getUid();

		List<UserPayRewardBO> list = this.activityService.getUserOncePayRewardList(userId);

		set("uprl", list);

		return this.render();
	}

	public Response getUserTotalPayRewards() {
		String userId = getUid();
		List<UserPayRewardBO> list = this.activityService.getUserTotalPayRewardList(userId);
		set("uprl", list);
		return this.render();
	}

	public Response receiveTotalPayReward() {

		String userId = this.getUid();
		int aid = this.getInt("aid", 0);

		CommonDropBO commonDropBO = this.activityService.receiveTotalPayReward(userId, aid);
		UserPayRewardBO userPayReward = this.activityService.getUserTotalPayRewardById(userId, aid);

		if (commonDropBO.isNeedPushUser()) {
			this.pushHandler.pushUser(userId);
		}

		set("dr", commonDropBO);
		set("uprl", userPayReward);

		return this.render();
	}

	/**
	 * 物品兑换
	 */
	public Response toolExchange() {

		String userId = this.getUid();
		int toolExchangeId = this.getInt("toolExchangeId", 0);

		// 暂时不需要前端传递兑换次数，次数写死为1
		// int num = this.getInt("num", 0);
		Map<String, Object> rtMap = activityService.toolExchange(userId, toolExchangeId, 1);
		set("tr", rtMap.get("tr"));
		set("te", rtMap.get("te"));

		set("ext", rtMap.get("ext"));

		this.pushHandler.pushUser(userId);
		return this.render();
	}

	/**
	 * 在进入物品兑换界面时，统计用户已经兑换的次数
	 */
	public Response toolExchangeCount() {

		String userId = this.getUid();
		List<ToolExchangeCountBO> boList = activityService.toolExchangeCount(userId);
		set("extc", boList);
		set("ecl", this.activityService.getToolExchangeBoList());
		return this.render();
	}

	/**
	 * 获取活动配置表
	 */
	public Response getActivityConfigData() {

		int type = this.getInt("tp", 1);

		if (type == 1) {
			set("ecl", this.activityService.getToolExchangeBoList());
		}

		set("tp", type);

		return this.render();
	}

	/**
	 * 获取活动面板信息
	 * 
	 * @return
	 */
	public Response getDisplayAcitvity() {

		List<SystemActivityBO> list = this.activityService.getDisplayActivityBOList(getUid());

		set("acl", list);

		return this.render();
	}

	/**
	 * 领取30天登入礼包
	 * 
	 * @return
	 */
	public Response receive30LoginReward() {

		String userId = this.getUid();
		int loginDay = getInt("day", 0);

		CommonDropBO commonDropBO = this.activityService.receiveLoginReward(userId, loginDay);
		if (commonDropBO.getExp() > 0 || commonDropBO.getGold() > 0 || commonDropBO.getCopper() > 0) {
			this.pushHandler.pushUser(getUid());
		}

		int gulri = this.activityService.checkLoginRewardHasGiven(userId);

		set("day", loginDay);
		set("gulri", gulri);
		set("dr", commonDropBO);

		return this.render();
	}

	/**
	 * 获取用户30天登入礼包领取信息
	 * 
	 * @return
	 */
	public Response get30LoginRewardInfo() {

		String userId = this.getUid();
		List<UserLoginRewardInfoBO> ulriList = this.activityService.getUserLoginRewardInfo(userId);
		set("ulri", ulriList);
		return this.render();
	}

	/**
	 * 刷新魂石商店
	 * 
	 * @return
	 */
	public Response refreshStoneMall() {
		String userId = this.getUid();
		int type = this.getInt("tp", 0);
		Map<String, Object> map = activityService.refreshStoneMall(userId, type);

		set("ngd", map.get("ngd"));
		set("mls", map.get("mls"));
		set("rct", map.get("rct"));
		set("nt", map.get("nt"));
		set("tp", type);
		set("act", activityService.isActivityOpen(ActivityId.MOON_DAY) ? 1 : 0);

		pushHandler.pushUser(userId);

		return this.render();
	}

	/**
	 * 兑换魂石商店物品
	 * 
	 * @return
	 */
	public Response exchageStoneTool() {

		String userId = this.getUid();
		int systemId = this.getInt("sid", 0);
		CommonDropBO commonDropBO = activityService.exchageStoneTool(userId, systemId);

		set("dr", commonDropBO);

		this.pushHandler.pushUser(getUid());

		return this.render();
	}

	/**
	 * 七天登陆-获取奖励列表
	 */
	public Response getLoginRewardList() {
		Map<String, Object> loginRewardList = activityService.getLoginRewardList(getUid());
		set("tls", loginRewardList.get("tls"));
		set("sts", loginRewardList.get("sts"));
		set("day", loginRewardList.get("day"));

		return this.render();
	}

	/**
	 * 七天登录-领取奖励
	 */
	public Response getLoginReward() {
		int day = getInt("day", 0);
		Map<String, Object> loginReward = activityService.recLoginReward(getUid(), day);

		set("dr", loginReward.get("dr"));
		set("day", loginReward.get("day"));

		pushHandler.pushUser(getUid());
		return this.render();
	}

	/**
	 * 获取抽奖列表
	 * 
	 * @return
	 */
	public Response getDrawList() {

		List<UserDrawBO> list = drawService.getList(getUid());
		set("list", list);

		return this.render();
	}

	/**
	 * 抽奖
	 * 
	 * @return
	 */
	public Response draw() {

		String userId = getUid();
		int id = getInt("id", 0);

		CommonDropBO commonDropBO = this.drawService.draw(userId, id);

		this.pushHandler.pushUser(userId);

		set("dr", commonDropBO);

		return this.render();
	}
	/**
	 * 展现消耗奖励
	 * @return
	 */
	public Response showCostReward(){
		String userId = getUid();
		int activityId = getInt("activityId", 0);
		ActivityCostBO bo=activityService.getActivityCostReward(userId, activityId);
		set("bo", bo);
		return this.render();
	}
	/**
	 * 展现每日活动
	 * @return
	 */
	public Response showAcitivtyDayReward(){
		String userId = getUid();
		int activityId = getInt("activityId", 0);
		ActivityCostBO bo=activityService.getActivityDayReward(userId, activityId);
		set("bo", bo);
		return this.render();
	}
	/**
	 * 领取每日活动
	 * @return
	 */
	public Response reciveAcitivtyDayReward(){
		String userId = getUid();
		int activityId = getInt("activityId", 0);
		int rewardId = getInt("rewardId", 0);
		CommonDropBO commonDropBO=activityService.reciveActivityDayReward(userId, activityId, rewardId);
		pushHandler.pushUser(getUid());
		return this.render();
	}
	/**
	 * 领取消耗获得奖励
	 * @return
	 */
	public Response reciveCostReward(){
		String userId = getUid();
		int activityId = getInt("activityId", 0);
		int rewardId = getInt("rewardId", 0);
		CommonDropBO commonDropBO=activityService.reciveActivityCostReward(userId, activityId, rewardId);
		set("bo", commonDropBO);
		return this.render();
	}
}
