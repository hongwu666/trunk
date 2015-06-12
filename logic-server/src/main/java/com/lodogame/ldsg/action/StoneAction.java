package com.lodogame.ldsg.action;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.server.response.Response;
import com.lodogame.ldsg.bo.UserEquipBO;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.bo.UserToolBO;
import com.lodogame.ldsg.constants.ToolId;
import com.lodogame.ldsg.service.EquipService;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.StoneService;
import com.lodogame.ldsg.service.ToolService;

public class StoneAction extends LogicRequestAction {

	@Autowired
	private StoneService stoneService;
	
	@Autowired
	private ToolService toolService;
	
	@Autowired
	private EquipService equipService;
	
	@Autowired
	private HeroService heroService;
	
	public Response enterStone() throws IOException {
		UserToolBO userToolBO = toolService.getUserToolBO(getUid(), ToolId.TOOL_PROTECT);
		
		set("psn", userToolBO !=null ? userToolBO.getToolNum() : 0);
		
		return this.render();
	}
	
	public Response upgradeStone() throws IOException {
		int stoneId = getInt("sid", 0);
		int isAuto = getInt("is", 0);
		int isProtect = getInt("ispt", 0);
		Map<String, Object> map = stoneService.upgradeStone(getUid(), isAuto, isProtect, stoneId);
		
		set("sid", map.get("sid"));
		set("rss", map.get("rss"));
		set("sc", map.get("sc"));

		return this.render();
	}
	
	public Response dressStone() throws IOException {
		int stoneId = getInt("sid", 0);
		String userEquipId = getString("ueid", null);
		int pos = getInt("pos", 0);

		this.stoneService.dressStone(getUid(), userEquipId, stoneId, pos);
		
		UserEquipBO userEquipBO = this.equipService.getUserEquipBO(getUid(), userEquipId);
		
		if (userEquipBO.getUserHeroId() != null) {
			UserHeroBO userHeroBO = heroService.getUserHeroBO(getUid(), userEquipBO.getUserHeroId());
			set("hero", userHeroBO);
		}
		
		set("eq", userEquipBO);
		set("sid", stoneId);
		set("pos", pos);
		set("ueid", userEquipId);
		
		return this.render();
	}
	
	public Response sellStone(){
		String userId = getUid();
		@SuppressWarnings("unchecked")
		List<String> ids = (List<String>) getList("ids");
		int v= stoneService.sellStone(ids, userId);
		set("v", v);
		return render();
	}
	
	public Response undressStone() throws IOException {
		String userEquipId = getString("ueid", null);
		int pos = getInt("pos", 0);

		Map<String, Object> map = this.stoneService.undressStone(getUid(), userEquipId, pos);

		UserEquipBO userEquipBO = this.equipService.getUserEquipBO(getUid(), userEquipId);
		if (userEquipBO.getUserEquipId() != null) {
			UserHeroBO userHeroBO = heroService.getUserHeroBO(getUid(), userEquipBO.getUserHeroId());
			set("hero", userHeroBO);
		}
		
		set("eq", userEquipBO);
		set("sid", map.get("sid"));
		set("pos", pos);
		set("ueip", userEquipId);
		
		return this.render();
	}
	
	
}
