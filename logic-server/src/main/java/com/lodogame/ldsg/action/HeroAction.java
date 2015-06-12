package com.lodogame.ldsg.action;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.server.response.Response;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.handler.PushHandler;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.UserService;

/**
 * 武将相关action
 * 
 * @author jacky
 * 
 */

public class HeroAction extends LogicRequestAction {
	public static final int NEED_THREE_HEROD = 2007;

	private static final Logger LOG = Logger.getLogger(HeroAction.class);

	@Autowired
	private HeroService heroService;

	@Autowired
	private UserService userService;

	@Autowired
	private PushHandler pushHandler;

	public Response eatHero() {

		String userId = getUid();
		String userHeroId = getString("userHeroId", "");
		List<String> ids = (List<String>) getList("eatHeroIds");

		CommonDropBO dropBO = heroService.eatHero(userId, userHeroId, ids);
		UserHeroBO userHeroBO = heroService.getUserHeroBO(getUid(), userHeroId);
		pushHandler.pushUser(userId);
		set("bo", userHeroBO);
		set("dr", dropBO);
		return render();
	}

	public Response eatFragment() {

		String userId = getUid();
		String userHeroId = getString("userHeroId", "");
		List<Integer> ids = (List<Integer>) getList("ids");

		if (ids.isEmpty()) {
			return null;
		}

		heroService.eatFragment(userId, userHeroId, ids);
		UserHeroBO userHeroBO = heroService.getUserHeroBO(getUid(), userHeroId);
		set("bo", userHeroBO);
		return render();
	}

	public Response heroBreak() {

		String userId = getUid();
		String userHeroId = getString("userHeroId", "");
		List<String> ids = (List<String>) getList("eatHeroIds");

		if (ids.isEmpty()) {
			return null;
		}

		heroService.heroBreak(userId, userHeroId, ids);
		UserHeroBO userHeroBO = heroService.getUserHeroBO(getUid(), userHeroId);
		set("bo", userHeroBO);
		return render();
	}

	public Response loadHeros() {

		int type = this.getInt("ft", 0);

		LOG.debug("获取武将列表.uid[" + getUid() + "], type[" + type + "]");

		List<UserHeroBO> userHeroList = this.heroService.getUserHeroList(getUid(), type);

		set("hls", userHeroList);
		return this.render();

	}

	/**
	 * 进阶预览
	 * 
	 * @return
	 */
	public Response upgradePre() {

		String userHeroId = this.getString("uhid", null);

		UserHeroBO userHeroBO = this.heroService.upgradePre(getUid(), userHeroId);
		set("hero", userHeroBO);

		return this.render();
	}

	/**
	 * 出售
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Response sell() {

		final List<String> userHeroIdList = (List<String>) getList("uhids");

		CommonDropBO dropBO = this.heroService.sell(getUid(), userHeroIdList, new EventHandle() {

			public boolean handle(Event event) {
				return true;
			}
		});

		this.pushHandler.pushUser(getUid());

		set("uhids", userHeroIdList);
		set("dr", dropBO);

		return this.render();
	}

	/**
	 * 进阶 - 点亮节点
	 */
	public Response lightNode() {
		int nodeId = getInt("nid", 0);
		String userHeroId = getString("uhid", "");
		// 1表示普通类型点亮。2表示元宝点亮
		int type = getInt("tp", 1);
		Map<String, Object> rt = heroService.lightNode(getUid(), userHeroId, nodeId, type);
		set("hero", rt.get("hero"));
		set("tls", rt.get("tls"));

		pushHandler.pushUser(getUid());

		return this.render();
	}

	public Response upgrade() {
		String userHeroId = getString("uhid", "");
		UserHeroBO bo = heroService.upgrade(getUid(), userHeroId);

		set("hero", bo);
		return this.render();
	}

	public Response retrieve() {
		String userHeroId = getString("uhid", null);
		heroService.retrieve(getUid(), userHeroId);
		set("uhid", userHeroId);
		return this.render();
	}

	/**
	 * 武将升级
	 */
	public Response advance() {
		int type = getInt("tp", 0);
		String userHeroId = getString("uhid", "");
		Map<String, Object> rt = heroService.advance(getUid(), type, userHeroId);

		set("mnum", rt.get("mnum"));
		set("snum", rt.get("snum"));
		set("bho", rt.get("bho"));
		set("aho", rt.get("aho"));
		set("tp", type);
		pushHandler.pushUser(getUid());
		return this.render();
	}

	/**
	 * 武将一键升级
	 */
	public Response autoAdvance() {
		String userHeroId = getString("uhid", "");
		Map<String, Object> rt = heroService.autoAdvance(getUid(), userHeroId);

		set("mnum", rt.get("mnum"));
		set("snum", rt.get("snum"));
		set("bho", rt.get("bho"));
		set("aho", rt.get("aho"));
		pushHandler.pushUser(getUid());
		return this.render();
	}

	/**
	 * 碎片和成
	 */
	public Response merge() {
		int fragmentId = getInt("fid", 0);
		CommonDropBO dr = heroService.merge(getUid(), fragmentId);
		set("dr", dr);
		return this.render();
	}
}
