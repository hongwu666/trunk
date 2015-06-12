package com.lodogame.ldsg.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.server.response.Response;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.handler.PushHandler;
import com.lodogame.ldsg.ret.TavernDrawRet;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.TavernService;

/**
 * 任务相关action
 * 
 * @author jacky
 * 
 */

public class TavernAction extends LogicRequestAction {

	private static final Logger logger = Logger.getLogger(TavernAction.class);

	@Autowired
	private TavernService tavernService;

	@Autowired
	private HeroService heroService;

	@Autowired
	private PushHandler pushHandler;

	/**
	 * 酒馆抽武将
	 * 
	 * @return
	 */
	public Response draw() {

		int type = this.getInt("tp", 0);
		int times = this.getInt("ts", 1);

		logger.debug("用户酒馆抽奖.uid[" + getUid() + "], type[" + type + "]");

		if (times != 1 && times != 10) {
			throw new ServiceException(ServiceReturnCode.FAILD, "抽奖失败,抽奖次数不对.userId[" + getUid() + "], times[" + times + "]");
		}

		TavernDrawRet tavernDrawRet = tavernService.draw(getUid(), type, times);

		List<UserHeroBO> userHeroBOList = new ArrayList<UserHeroBO>();
		for (String userHeroId : tavernDrawRet.getUserHeroIds()) {
			UserHeroBO userHeroBO = heroService.getUserHeroBO(getUid(), userHeroId);
			userHeroBOList.add(userHeroBO);
		}

		set("hs", userHeroBOList);
		set("ti", tavernDrawRet.getUserTavernBO());
		set("dr", tavernDrawRet.getCommonDropBO());

		
		pushHandler.pushUser(getUid());

		return this.render();
	}
}
