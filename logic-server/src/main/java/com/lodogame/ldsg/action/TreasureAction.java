package com.lodogame.ldsg.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.server.response.Response;
import com.lodogame.ldsg.bo.TreasureGiftBo;
import com.lodogame.ldsg.bo.TreasurePriceBo;
import com.lodogame.ldsg.bo.TreasureReturnBO;
import com.lodogame.ldsg.bo.TreasureShowBO;
import com.lodogame.ldsg.service.TreasureService;

public class TreasureAction  extends LogicRequestAction{

	@Autowired
	TreasureService treasureService;
	
	public Response show(){
		String userId =  getUid();
		List<TreasureShowBO> re = treasureService.show(userId);
		TreasureGiftBo gift = treasureService.getGift(userId);
		TreasurePriceBo p = treasureService.getPriceBo(userId);
		set("g", gift);
		set("userData", re);
		set("p", p);
		return render();
	}
	
	public Response open(){
		String userId = getUid();
		int type = getInt("type",0);
		TreasureReturnBO re = treasureService.open(userId, type);
		set("returnData", re);
		return render();
	}
}
