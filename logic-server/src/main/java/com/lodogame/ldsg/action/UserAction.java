package com.lodogame.ldsg.action;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.UserExtinfoDao;
import com.lodogame.game.remote.callback.Callback;
import com.lodogame.game.remote.handle.RemoteCallHandle;
import com.lodogame.game.remote.request.Request;
import com.lodogame.game.server.response.Response;
import com.lodogame.game.utils.json.Json;
import com.lodogame.ldsg.bo.UserBO;
import com.lodogame.ldsg.constants.PriceType;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.handler.PushHandler;
import com.lodogame.ldsg.helper.CopperHelper;
import com.lodogame.ldsg.service.ActivityService;
import com.lodogame.ldsg.service.PriceService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.User;
import com.lodogame.model.UserExtinfo;

/**
 * 用户相关action
 * 
 * @author jacky
 * 
 */

public class UserAction extends LogicRequestAction {

	private static final Logger LOG = Logger.getLogger(UserAction.class);

	@Autowired
	private UserService userService;

	@Autowired
	private UserExtinfoDao userExtinfoDao;

	@Autowired
	private ActivityService activityService;

	@Autowired
	private PriceService priceService;

	@Autowired
	private PushHandler pushHandler;

	@Autowired
	private RemoteCallHandle remoteCallHandle;

	public Response login() throws IOException {

		String uid = getUid();
		String userIp = this.getString("ip", "");

		LOG.debug("用户登录.uid[" + uid + "]");

		User user = this.userService.get(uid);

		LOG.debug("用户登录成功.uid[" + uid + "], username[" + user.getUsername() + "]");

		this.activityService.checkUserLoginRewardInfo(uid);

		this.userService.login(uid, userIp);

		this.pushHandler.pushUserData(uid);

		// Test
		Request req = new Request();
		req.setAction("WarAction");
		req.setMethod("reg");
		req.put("uid", uid);

		remoteCallHandle.call(req, new Callback() {

			@Override
			public void handle(com.lodogame.game.remote.response.Response resp) {
				LOG.info(Json.toJson(resp));
			}
		});

		return this.render();
	}

	public Response reLogin() throws IOException {

		String uid = getUid();
		String userIp = this.getString("ip", "");

		LOG.debug("用户登录.uid[" + uid + "]");

		User user = this.userService.get(uid);

		LOG.debug("用户登录成功.uid[" + uid + "], username[" + user.getUsername() + "]");

		this.userService.login(uid, userIp);

		this.pushHandler.pushUserReLogin(uid);

		return null;
	}

	public Response resumePower() {

		final String uid = getUid();

		int times = this.userService.resumePower(uid, new EventHandle() {

			@Override
			public boolean handle(Event event) {
				set("uinfo", userService.getUserBO(uid));
				return false;
			}
		});

		int needMoney = this.priceService.getPrice(PriceType.BUY_POWER, times + 1);
		set("nbpc", needMoney);
		this.pushHandler.pushUser(uid);
		return this.render();

	}

	public Response guideStep() {
		final String uid = getUid();
		int guideStep = this.getInt("gs", 0);
		String userIp = this.getString("ip", "");
		int rc = 1000;
		if (!this.userService.saveGuideStep(uid, guideStep, userIp)) {
			rc = ServiceReturnCode.FAILD;
		}
		return this.render(rc);
	}

	/**
	 * 记录新手引导步骤
	 */
	public Response recordGuideStep() {
		String uid = getUid();
		int guideStep = this.getInt("gs", 0);
		String userIp = this.getString("ip", "");
		int rc = 1000;
		if (!this.userService.recordGuideStep(uid, guideStep, userIp)) {
			rc = ServiceReturnCode.FAILD;
		}
		return this.render(rc);
	}

	/**
	 * 创建角色
	 * 
	 * @return
	 */
	public Response create() {

		String uid = getUid();
		int systemHeroId = this.getInt("sh", 1);
		String username = this.getString("rn", null);
		String userIp = this.getString("ip", "");

		LOG.debug("用户创建.systemHeroId[" + systemHeroId + "], username[" + username + "]");

		this.userService.create(uid, systemHeroId, username, new EventHandle() {

			public boolean handle(Event event) {
				return true;
			}

		});

		this.activityService.checkUserLoginRewardInfo(uid);

		this.pushHandler.pushUserData(uid);

		this.userService.login(uid, userIp);

		return this.render();
	}

	/**
	 * 邀请注册
	 */
	public Response invited() {
		String uid = getUid();
		int systemHeroId = this.getInt("sh", 1);
		String userIp = this.getString("ip", "");
		String username = this.getString("rn", null);
		int code = this.getInt("code", 0);

		LOG.debug("邀请注册创建.systemHeroId[" + systemHeroId + "], username[" + username + "] , code[" + code + "]");

		this.activityService.checkUserLoginRewardInfo(uid);
		this.pushHandler.pushUserData(uid);
		this.userService.login(uid, userIp);
		return this.render();
	}

	/**
	 * 邀请注册 - 检测邀请码
	 */
	public Response checkCode() {
		int code = this.getInt("code", 0);

		this.userService.checkCode(code);

		return this.render();
	}

	/**
	 * 用户退出登录
	 * 
	 * @return
	 */
	public Response logout() {

		LOG.debug("用户退出登录.uid[" + getUid() + "]");

		this.userService.logout(getUid());

		return null;
	}

	public Response buyCopper() {

		int times = 1;

		this.userService.buyCopper(getUid());

		UserExtinfo userExtinfo = this.userExtinfoDao.get(getUid());
		User user = this.userService.get(getUid());

		Date now = new Date();
		if (userExtinfo != null && DateUtils.isSameDay(now, userExtinfo.getLastBuyCopperTime())) {
			times = userExtinfo.getBuyCopperTimes();
		}

		Map<String, Integer> m = CopperHelper.getCopperInfo(times, user.getLevel());
		Map<String, Integer> m2 = CopperHelper.getCopperInfo(times + 1, user.getLevel());

		set("ts", times);
		set("cm", m.get("ncm"));
		set("co", m.get("nco"));
		set("ncm", m2.get("ncm"));
		set("nco", m2.get("nco"));

		return this.render();

	}

	public Response loadInfo() {

		String uid = getUid();

		LOG.debug("获取用户信息.uid[" + uid + "]");

		UserBO userBO = this.userService.getUserBO(uid);

		set("userInfo", userBO);

		return this.render();
	}

	public Response checkName() {
		String username = this.getString("un", "");
		userService.checkUsername(username);
		return this.render();
	}

}
