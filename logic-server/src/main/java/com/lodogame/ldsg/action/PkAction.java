package com.lodogame.ldsg.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.connector.ServerConnectorMgr;
import com.lodogame.game.server.response.Response;
import com.lodogame.game.server.session.Session;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.UserEquipBO;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.bo.UserPkInfoBO;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.handler.PushHandler;
import com.lodogame.ldsg.helper.HeroHelper;
import com.lodogame.ldsg.helper.LodoIDHelper;
import com.lodogame.ldsg.service.EquipService;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.MailService;
import com.lodogame.ldsg.service.PkService;
import com.lodogame.ldsg.service.RobotService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.RobotUser;
import com.lodogame.model.User;
import com.lodogame.model.UserPkInfo;

public class PkAction extends LogicRequestAction {

	@Autowired
	private PkService pkService;

	@Autowired
	private HeroService heroService;

	@Autowired
	private UserService userService;

	@Autowired
	private EquipService equipService;

	@Autowired
	private PushHandler pushHandler;

	@Autowired
	private RobotService robotService;

	@Autowired
	private MailService mailService;

	public Response buyNum() {
		String userId = getUid();
		pkService.buyNum(userId);
		return render();
	}

	public Response replace() {
		String userId = getUid();
		pkService.replace(userId);
		UserPkInfoBO bo = pkService.getUserPkInfo(userId);
		set("ps", pkService.getChallengeList(bo.getRank(), userId));
		return render();
	}

	/**
	 * 争霸入口
	 * 
	 * @return
	 */
	public Response enter() {

		String userId = getUid();

		pkService.enter(userId, new EventHandle() {

			@Override
			public boolean handle(Event event) {

				UserPkInfo userPkInfo = (UserPkInfo) event.getObject("userPkInfo");
				UserPkInfoBO userPkInfoBO = pkService.createUserPkInfoBo(userPkInfo);
				set("upi", userPkInfoBO);
				set("ps", pkService.getChallengeList(userPkInfo.getRank(), userPkInfo.getUserId()));

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
	 * 读取前十排行
	 * 
	 * @return
	 */
	public Response loadRank() {

		set("ps", pkService.getTenList());
		return this.render();

	}

	/**
	 * 批量兑换奖励
	 * 
	 * @return
	 */
	public Response exchange() {

		String userId = getUid();
		int num = getInt("nm", 0);
		int aid = getInt("aid", 0);

		CommonDropBO commonDropBO = pkService.exchange(userId, aid, num);

		set("ad", commonDropBO);

		this.pushHandler.pushUser(userId);

		return this.render();
	}

	/**
	 * 查看用户详情
	 * 
	 * @return
	 */
	public Response userDetail() {
		int playid = 0;
		String username = "";
		playid = this.getInt("pid", 0);
		username = this.getString("un", "");

		String uid = null;
		User user = null;

		if (playid != 0 && LodoIDHelper.isRobotLodoId(playid)) {

			RobotUser robotUser = robotService.getById(playid);
			if (robotUser == null) {
				return this.render(2001);
			}

			List<UserHeroBO> userHeroList = this.robotService.getRobotUserHeroBOList(robotUser.getUserId());

			set("hls", userHeroList);
			set("eqs", new ArrayList<UserEquipBO>());
			set("ccp", robotUser.getCapability());
			set("userId", robotUser.getUserId());
			set("username", robotUser.getUsername());
			set("vip", 1);
			set("level", robotUser.getLevel());

		} else {

			if (playid != 0) {
				user = this.userService.getByPlayerId(String.valueOf(playid));
				if (user != null) {
					uid = user.getUserId();
				}
			} else if (StringUtils.isNotEmpty(username)) {
				user = userService.getUserByUserName(username);
				if (user != null) {
					uid = user.getUserId();
				}
			}

			if (uid != null) {

				user = this.userService.get(uid);

				List<UserHeroBO> userHeroList = this.heroService.getUserHeroList(uid, 1);
				List<UserEquipBO> userEquipList = this.equipService.getUserEquipList(uid, false);

				set("hls", userHeroList);
				set("eqs", userEquipList);
				set("ccp", HeroHelper.getCapability(userHeroList));
				set("userId", user.getUserId());
				set("username", user.getUsername());
				set("vip", user.getVipLevel());
				set("level", user.getLevel());
			} else {
				return this.render(2001);
			}
		}

		return this.render();
	}
}
