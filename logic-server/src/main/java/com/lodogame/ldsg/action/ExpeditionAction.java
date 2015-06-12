package com.lodogame.ldsg.action;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.server.response.Response;
import com.lodogame.ldsg.bo.ExpeditionShowBO;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.handler.PushHandler;
import com.lodogame.ldsg.helper.HeroHelper;
import com.lodogame.ldsg.service.ExpeditionService;
import com.lodogame.ldsg.service.RobotService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.IUser;

public class ExpeditionAction extends LogicRequestAction {

	@Autowired
	private ExpeditionService expeditionService;

	@Autowired
	private UserService userService;

	@Autowired
	private RobotService robotService;

	@Autowired
	private PushHandler pushHandler;

	public Response show() {
		String userId = getUid();
		ExpeditionShowBO bo = expeditionService.show(userId);
		set("bo", bo);
		return render();
	}

	public Response getGift() {
		String userId = getUid();
		long exId = getLong("exId", -1L);
		Map<Integer, Object> bo = expeditionService.getGift(userId, exId);
		set("setp", bo.get(1));
		set("gift", bo.get(2));
		pushHandler.pushUser(userId);
		return render();
	}

	public Response showHero() {
		String userId = getUid();
		long exId = getLong("exId", -1L);
		List<UserHeroBO> hero = expeditionService.showHero(userId, exId);

		set("bo", hero);
		IUser user = null;

		// TODO 不好的代码
		try {
			user = userService.get(hero.get(0).getUserId());
		} catch (Exception e) {
			user = robotService.get(hero.get(0).getUserId());
			if (user != null) {
				int level = user.getLevel() > 60 ? 60 : user.getLevel();
				for (UserHeroBO bo : hero) {
					bo.setHeroLevel(level);
				}
			}
		}

		set("name", user.getUsername());
		set("level", user.getLevel());
		set("id", user.getUserId());
		return render();
	}

	public Response changePos() {
		String userId = getUid();
		String h1 = getString("hId1", "");
		String h2 = getString("hId2", "");
		int pos1 = getInt("p1", 0);
		int pos2 = getInt("p2", 0);
		expeditionService.changePos(userId, h1, h2, pos1, pos2);
		return render();
	}

	public Response replace() {
		String userId = getUid();
		expeditionService.replace(userId);
		pushHandler.pushUser(userId);
		return render();
	}
}
