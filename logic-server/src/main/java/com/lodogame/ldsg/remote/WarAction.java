package com.lodogame.ldsg.remote;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.remote.action.BaseAction;
import com.lodogame.game.remote.request.Request;
import com.lodogame.game.remote.response.Response;
import com.lodogame.ldsg.bo.UserEquipBO;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.helper.HeroHelper;
import com.lodogame.ldsg.service.EquipService;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.User;

public class WarAction extends BaseAction {

	@Autowired
	private UserService userService;

	@Autowired
	private HeroService heroService;

	@Autowired
	private EquipService equipService;

	public Response userDetail(Request req) {

		String uid = req.get("uid", "");

		User user = this.userService.get(uid);

		List<UserHeroBO> userHeroList = this.heroService.getUserHeroList(uid, 1);
		List<UserEquipBO> userEquipList = this.equipService.getUserEquipList(uid, false);

		Response response = new Response();

		response.put("hls", userHeroList);
		response.put("eqs", userEquipList);
		response.put("ccp", HeroHelper.getCapability(userHeroList));
		response.put("userId", user.getUserId());
		response.put("username", user.getUsername());
		response.put("vip", user.getVipLevel());
		response.put("level", user.getLevel());

		return response;

	}
}
