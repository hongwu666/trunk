package com.lodogame.ldsg.action;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.server.response.Response;
import com.lodogame.ldsg.bo.ArenaDefiantBO;
import com.lodogame.ldsg.bo.ArenaScoreRankBo;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.UserArenaBo;
import com.lodogame.ldsg.bo.UserArenaGiftBO;
import com.lodogame.ldsg.bo.UserArenaRecordBo;
import com.lodogame.ldsg.handler.PushHandler;
import com.lodogame.ldsg.helper.BOHelper;
import com.lodogame.ldsg.service.ArenaService;
import com.lodogame.model.UserArenaSeriesGift;

/**
 * 竞技场action
 * 
 * @author Candon
 * 
 */

public class ArenaAction extends LogicRequestAction {

	
	@Autowired
	private ArenaService arenaService;

	@Autowired
	private PushHandler pushHandler;

	/**
	 * 获取商城商品列表
	 * 
	 * @return
	 */
	public Response enter() {

		String userId = getUid();

		Map<String, Object> map = this.arenaService.enter(userId);
		UserArenaBo userArenaBo = this.arenaService.get(userId);

		set("pls", map.get("pls"));
		set("uaf", userArenaBo);
		set("wls", map.get("wls"));
		set("bn", map.get("bn"));
		//set("rls", map.get("rls"));
		List<UserArenaSeriesGift> list = arenaService.showGift(userId);
		List<UserArenaGiftBO> re = BOHelper.getArenaGiftBOs(list);
		set("bo", re);
		return this.render();
	}

	public Response scoreRank() {

		String userId = getUid();

		List<ArenaScoreRankBo> list = this.arenaService.getArenaScoreRankBos();
		UserArenaBo userArenaBo = this.arenaService.get(userId);
		int rank = this.arenaService.getRank(userId);

		set("srls", list);
		set("sc", userArenaBo.getMaxWinCount());
		set("rk", rank);
		return this.render();
	}

	public Response pkRecord() {

		String userId = getUid();

		List<UserArenaRecordBo> list = this.arenaService.getArenaRecordBos(userId);

		set("prl", list);

		return this.render();
	}

	public Response refresh() {

		String userId = getUid();
		
		List<ArenaDefiantBO> list = this.arenaService.refresh(userId);

		//pushHandler.pushUser(userId);

		set("pls", list);

		return this.render();
	}
	
	public Response getGift(){
		String userId = getUid();
		int winCount = getInt("winNum", -1);
		CommonDropBO dropBO = arenaService.crateCommonDropBO(userId, winCount, true, true);
		set("bo", dropBO);
		//pushHandler.pushUser(userId);
		return render();
	}
	
	public Response showGift(){
		String userId = getUid();
	
		return render();
	}
	
	public Response buyNum(){
		String userId = getUid();
		int b = getInt("buyNum", 1);
		int num = arenaService.buyNum(userId,b);
		set("num", num);
		return this.render();
	}
}
