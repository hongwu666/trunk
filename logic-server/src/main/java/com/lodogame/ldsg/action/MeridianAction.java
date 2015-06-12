package com.lodogame.ldsg.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.MeridianDao;
import com.lodogame.game.dao.UserHeroDao;
import com.lodogame.game.server.response.Response;
import com.lodogame.ldsg.bo.MeridianMeridianBO;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.handler.PushHandler;
import com.lodogame.ldsg.helper.BOHelper;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.MeridianService;
import com.lodogame.model.MeridianUserInfo;
import com.lodogame.model.UserHero;

public class MeridianAction extends LogicRequestAction {

	@Autowired
	private MeridianService meridianService;

	@Autowired
	private UserHeroDao userHeroDao;

	@Autowired
	private HeroService heroService;

	@Autowired
	private PushHandler pushHandler;

	@Autowired
	private MeridianDao meridianDao;

	public Response show() {
		String userId = getUid();
		List<MeridianMeridianBO> list = meridianService.show(userId);
		set("list", list);
		return render();
	}

	public Response open() {
		String userId = getUid();
		int type = getInt("type", -1);
		int nodeId = getInt("nodeId", -1);
		String userHeroId = getString("userHeroId", "");
		MeridianUserInfo info = meridianService.open(userId, type, nodeId, userHeroId);
		MeridianMeridianBO bo = BOHelper.getMeridianBO(info);

		UserHero userHero = userHeroDao.get(userId, userHeroId);
		UserHeroBO previousHeroBO = heroService.createUserHeroBO(userHero);
		set("info", bo);
		set("code", bo.getLevel() == 1 ? 0 : 1);
		set("hero", previousHeroBO);
		pushHandler.pushUser(userId);
		return render();
	}

	public Response upLevel() {
		String userId = getUid();
		int type = getInt("type", -1);
		int nodeId = getInt("nodeId", -1);
		String userHeroId = getString("userHeroId", "");
		MeridianUserInfo info = meridianService.upLevel(userId, type, nodeId, userHeroId);
		MeridianMeridianBO bo = BOHelper.getMeridianBO(info);
		UserHero userHero = userHeroDao.get(userId, userHeroId);
		UserHeroBO previousHeroBO = heroService.createUserHeroBO(userHero);
		set("info", bo);
		set("hero", previousHeroBO);
		pushHandler.pushUser(userId);
		return render();
	}

}
