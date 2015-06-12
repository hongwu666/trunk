package com.lodogame.ldsg.action;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.server.response.Response;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.OnlyOneRankBO;
import com.lodogame.ldsg.bo.OnlyOneRegBO;
import com.lodogame.ldsg.bo.OnlyOneRewardBO;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.handler.PushHandler;
import com.lodogame.ldsg.service.OnlyOneService;

/**
 * 竞技场action
 * 
 * @author shixiangwen
 * 
 */

public class OnlyOneAction extends LogicRequestAction {

	public static Logger logger = Logger.getLogger(OnlyOneAction.class);

	@Autowired
	private OnlyOneService onlyOneService;

	@Autowired
	private PushHandler pushHandler;

	public Response enter() {

		String userId = getUid();

		logger.debug("进入千人人斩界面.uid[" + userId + "]");

		this.onlyOneService.enter(userId);

		OnlyOneRegBO onlyOneRegBO = this.onlyOneService.getRegBO(userId);

		List<OnlyOneRankBO> onlyOneRankBOList = this.onlyOneService.getRankList(userId, false);
		List<OnlyOneRankBO> myOnlyRankBOList = this.onlyOneService.getRankList(userId, true);
		List<OnlyOneRankBO> onlyOneWinRankBOList = this.onlyOneService.getWinRankList();

		set("rkl", onlyOneRankBOList);
		set("wrkl", onlyOneWinRankBOList);
		set("mrkl", myOnlyRankBOList);
		set("uinfo", onlyOneRegBO);

		return this.render();
	}

	public Response quit() {

		String userId = getUid();

		logger.debug("退出千人斩界面.uid[" + getUid() + "]");

		this.onlyOneService.quit(userId);

		return this.render();
	}

	public Response matcher() {

		String userId = getUid();

		logger.debug("继续战斗匹配.uid[" + getUid() + "]");

		this.onlyOneService.startMatcher(userId);

		return this.render();
	}

	public Response getRewardList() {

		String userId = getUid();

		logger.debug("获取用户奖励列表.uid[" + getUid() + "]");

		List<OnlyOneRewardBO> list = this.onlyOneService.getRewardList(userId);

		set("list", list);

		return this.render();
	}

	public Response receive() {

		String userId = getUid();

		int id = this.getInt("id", 0);

		logger.debug("领取奖励.uid[" + getUid() + "]");

		CommonDropBO commonDropBO = this.onlyOneService.receive(userId, id);

		if (commonDropBO.isNeedPushUser()) {
			this.pushHandler.pushUser(userId);
		}

		set("dr", commonDropBO);

		return this.render();
	}
	public Response showHero() {
		String userId = getUid();
		List<UserHeroBO> list = onlyOneService.showHero(userId);
		set("list", list);
		return render();
	}
	public Response changePos() {
		String userId = getUid();
		String h1 = getString("hId1", "");
		String h2 = getString("hId2", "");
		int pos1 = getInt("p1", 0);
		int pos2 = getInt("p2", 0);
		onlyOneService.changePos(userId, h1, h2, pos1,pos2);
		return render();
	}
}
