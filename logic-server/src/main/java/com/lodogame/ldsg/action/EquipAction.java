package com.lodogame.ldsg.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.server.response.Response;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.EquipEnchantBO;
import com.lodogame.ldsg.bo.EquipRefineBO;
import com.lodogame.ldsg.bo.EquipRefineSoulBO;
import com.lodogame.ldsg.bo.UserEquipBO;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.bo.UserToolBO;
import com.lodogame.ldsg.event.CopperUpdateEvent;
import com.lodogame.ldsg.event.EquipUpdateEvent;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.HeroUpdateEvent;
import com.lodogame.ldsg.event.ToolUpdateEvent;
import com.lodogame.ldsg.handler.PushHandler;
import com.lodogame.ldsg.service.EquipService;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.model.SystemEquip;
import com.lodogame.model.UserEquip;
import com.lodogame.model.UserTool;

/**
 * 武将相关action
 * 
 * @author jacky
 * 
 */

public class EquipAction extends LogicRequestAction {

	private static final Logger LOG = Logger.getLogger(EquipAction.class);

	@Autowired
	private EquipService equipService;

	@Autowired
	private PushHandler pushHandler;

	@Autowired
	private HeroService heroService;

	public Response loadEquipments() {

		LOG.debug("获取用户装备列表.uid[" + getUid() + "]");

		List<UserEquipBO> userEquipList = this.equipService.getUserEquipList(getUid());

		set("eql", userEquipList);

		return this.render();

	}

	// 装备精炼预览
	public Response refinePre() {

		final String userEquipId = (String) this.request.getParameter("ueid");

		// 装备预览
		List<EquipRefineBO> retVal = this.equipService.refinePre(getUid(), userEquipId);

		set("er", retVal);
		return this.render();
	}

	// 装备精炼
	public Response refine() {

		final String userEquipId = (String) this.request.getParameter("ueid");

		final int type = this.getInt("tp", 1);

		List<UserToolBO> toolbo = this.equipService.refine(getUid(), userEquipId, type);

		pushHandler.pushUser(getUid());
		// 装备预览

		List<EquipRefineBO> retVal = this.equipService.refinePre(getUid(), userEquipId);
		// 返加装备BO对象
		UserEquipBO userEquipBO = equipService.getUserEquipBO(getUid(), userEquipId);
		// 若装备在英雄身上，刷新英雄
		if (StringUtils.isNotEmpty(userEquipBO.getUserHeroId())) {
			UserHeroBO userHeroBO = heroService.getUserHeroBO(getUid(), userEquipBO.getUserHeroId());
			set("hero", userHeroBO);
		}
		set("eq", userEquipBO);
		set("er", retVal);
		set("bo", toolbo);
		return this.render();

	}

	// 装备点化预览
	public Response enchantPre() {
		final String userEquipId = (String) this.request.getParameter("ueid");
		// 装备点化预览
		EquipEnchantBO retVal = this.equipService.enchantPre(getUid(), userEquipId);

		set("er", retVal);
		return this.render();
	}

	// 装备点化
	public Response enchant() {
		final String userEquipId = (String) this.request.getParameter("ueid");
		// 装备点化
		EquipEnchantBO retVal = this.equipService.enchant(getUid(), userEquipId);
		pushHandler.pushUser(getUid());

		set("er", retVal);
		return this.render();
	}

	// 装备点化保存
	public Response save() {
		final String userEquipId = (String) this.request.getParameter("ueid");
		// 装备点化保存
		EquipEnchantBO retVal = this.equipService.save(getUid(), userEquipId);
		// 返加装备BO对象
		UserEquipBO userEquipBO = equipService.getUserEquipBO(getUid(), userEquipId);
		// 若装备在英雄身上，刷新英雄
		if (StringUtils.isNotEmpty(userEquipBO.getUserHeroId())) {
			UserHeroBO userHeroBO = heroService.getUserHeroBO(getUid(), userEquipBO.getUserHeroId());
			set("hero", userHeroBO);
		}
		set("eq", userEquipBO);
		set("er", retVal);
		return this.render();
	}

	// 装备炼魂预览
	public Response refineSoulPre() {

		final String userEquipId = (String) this.request.getParameter("ueid");

		// 装备预览
		List<EquipRefineSoulBO> retVal = this.equipService.refineSoulPre(getUid(), userEquipId);

		set("eql", retVal);

		return this.render();
	}

	// 装备炼魂
	public Response refineSoul() {

		final String userEquipId = (String) this.request.getParameter("ueid");
		final int refineEquipId = getInt("reid", 0);
		EquipRefineSoulBO bo = this.equipService.refineSoul(getUid(), userEquipId, refineEquipId);
		if (bo.getLuck() == 0) {
			pushHandler.pushEquipList(getUid());
		}

		UserEquipBO userEquipBO = equipService.getUserEquipBO(getUid(), userEquipId);

		if (StringUtils.isNotEmpty(userEquipBO.getUserHeroId())) {
			UserHeroBO userHeroBO = heroService.getUserHeroBO(getUid(), userEquipBO.getUserHeroId());
			set("hero", userHeroBO);
		}

		pushHandler.pushUser(getUid());
		set("bo", bo);
		return this.render();
	}

	/**
	 * 装备穿戴
	 * 
	 * @return
	 */
	public Response dress() {

		Integer equipType = this.getInt("dp", 1);
		String userEquipId = this.getString("ueid", null);
		final String userHeroId = this.getString("uhid", null);

		LOG.debug("武将穿戴装备.uid[" + getUid() + "], userEquipId[" + userEquipId + "], userHeroId[" + userHeroId + "], equipType[" + equipType + "]");

		final List<UserHeroBO> userHeroBOList = new ArrayList<UserHeroBO>();

		this.equipService.updateEquipHero(getUid(), userEquipId, userHeroId, equipType, new EventHandle() {

			@Override
			public boolean handle(Event event) {

				if (event instanceof EquipUpdateEvent) {
					UserEquip userEquip = (UserEquip) event.getObject("userEquip");
					if (userEquip.getUserHeroId() == null) {
						set("uueid", userEquip.getUserEquipId());
					}
				} else if (event instanceof HeroUpdateEvent) {
					UserHeroBO userHero = heroService.getUserHeroBO(getUid(), event.getString("userHeroId"));
					userHeroBOList.add(userHero);
				}

				return true;

			}
		});

		set("uhid", userHeroId);
		set("hls", userHeroBOList);

		if (StringUtils.isEmpty(userHeroId)) {
			set("uueid", userEquipId);
		} else {
			set("ueid", userEquipId);
		}

		return this.render();

	}

	/**
	 * 装备出售
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Response sell() {

		final List<String> userEquipIds = (List<String>) this.getList("ueids");

		CommonDropBO dr = this.equipService.sell(getUid(), userEquipIds, new EventHandle() {

			public boolean handle(Event event) {
				if (event instanceof CopperUpdateEvent) {
					pushHandler.pushUser(getUid());
				}
				return true;
			}
		});

		set("ueids", userEquipIds);
		set("dr", dr);

		return this.render();

	}

	/**
	 * 武将预览
	 * 
	 * @return
	 */
	public Response upgradePre() {

		final String userEquipId = (String) this.request.getParameter("ueid");

		// 当前装备的再一次预览
		Map<String, Object> retVal = this.equipService.upgradePre(getUid(), userEquipId);
		set("eq", retVal.get("userEquipBO"));
		set("st", retVal.get("status"));
		set("co", retVal.get("needCopper"));

		return this.render();
	}

	/**
	 * 装备一键升级
	 * 
	 * @return
	 */
	public Response autoUpgrade() {

		final String userEquipId = (String) this.request.getParameter("ueid");

		List<Integer> addLevelList = new ArrayList<Integer>();

		int stopResult = this.equipService.autoUpgrade(getUid(), userEquipId, addLevelList);

		UserEquipBO userEquipBO = equipService.getUserEquipBO(getUid(), userEquipId);
		set("eq", userEquipBO);
		if (StringUtils.isNotEmpty(userEquipBO.getUserHeroId())) {
			UserHeroBO userHeroBO = heroService.getUserHeroBO(getUid(), userEquipBO.getUserHeroId());
			set("hero", userHeroBO);
		}

		set("rt", stopResult);
		set("al", addLevelList);

		pushHandler.pushUser(getUid());

		// 当前装备的再一次预览
		Map<String, Object> retVal = this.equipService.upgradePre(getUid(), userEquipId);
		set("eqPre", retVal.get("userEquipBO"));
		set("st", retVal.get("status"));
		set("co", retVal.get("needCopper"));

		return this.render();

	}

	/**
	 * 装备打造
	 * 
	 * @return
	 */
	public Response upgrade() {

		final String userEquipId = (String) this.request.getParameter("ueid");

		this.equipService.upgrade(getUid(), userEquipId, new EventHandle() {

			public boolean handle(Event event) {
				if (event instanceof HeroUpdateEvent) {
					UserHeroBO userHeroBO = heroService.getUserHeroBO(getUid(), event.getString("userHeroId"));
					set("hero", userHeroBO);
				} else if (event instanceof EquipUpdateEvent) {
					// 返加装备BO对象
					UserEquipBO userEquipBO = equipService.getUserEquipBO(getUid(), event.getString("userEquipId"));
					set("eq", userEquipBO);
				}
				return true;
			}
		});

		pushHandler.pushUser(getUid());

		// 当前装备的再一次预览
		Map<String, Object> retVal = this.equipService.upgradePre(getUid(), userEquipId);
		set("eqPre", retVal.get("userEquipBO"));
		set("st", retVal.get("status"));
		set("co", retVal.get("needCopper"));

		return this.render();

	}

	public Response merge() {

		final String userEquipId = (String) this.request.getParameter("ueid");
		int type = this.getInt("tp", 0);

		Map<String, Object> map = this.equipService.mergeEquip(getUid(), userEquipId, type == 1, new EventHandle() {

			@Override
			public boolean handle(Event event) {

				if (event instanceof HeroUpdateEvent) {
					UserHeroBO userHeroBO = heroService.getUserHeroBO(getUid(), event.getString("userHeroId"));
					set("hero", userHeroBO);
				} else if (event instanceof EquipUpdateEvent) {
					// 返加装备BO对象
					UserEquipBO userEquipBO = equipService.getUserEquipBO(getUid(), event.getString("userEquipId"));
					set("eq", userEquipBO);
				} else if (event instanceof ToolUpdateEvent) {
					// pushHandler.pushToolList(getUid());
				}

				return true;
			}
		});

		if (type == 1) {// 金币刷新
			this.pushHandler.pushUser(getUid());
		}

		set("tp", type);

		UserEquipBO userEquipBO = this.equipService.mergeEquipPre(getUid(), userEquipId);
		set("eqPre", userEquipBO);
		set("st", 1);
		set("tr", map.get("tr"));
		if (userEquipBO == null) {
			set("st", -3);
		}
		set("ng", map.get("ng"));
		return this.render();
	}

	public Response mergePre() {
		String userId = getUid();
		final String userEquipId = (String) this.request.getParameter("ueid");

		UserEquipBO userEquipBO = this.equipService.mergeEquipPre(userId, userEquipId);

		UserEquip userEquip = this.equipService.getUserEquip(userId, userEquipId);
		SystemEquip systemEquip = this.equipService.getSysEquip(userEquip.getEquipId());

		set("ng", equipService.calEquipUpgradeNeedGold(userId, systemEquip.getEquipId()));
		set("eq", userEquipBO);
		set("st", 1);
		if (userEquipBO == null) {
			set("st", -3);
		}

		return this.render();
	}
}
